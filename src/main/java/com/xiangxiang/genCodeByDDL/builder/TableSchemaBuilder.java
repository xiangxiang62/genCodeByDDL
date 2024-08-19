package main.java.com.xiangxiang.genCodeByDDL.builder;

import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLPrimaryKey;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlCreateTableParser;
import main.java.com.xiangxiang.genCodeByDDL.builder.sql.MySQLDialect;
import main.java.com.xiangxiang.genCodeByDDL.common.ErrorCode;
import main.java.com.xiangxiang.genCodeByDDL.exception.BusinessException;
import main.java.com.xiangxiang.genCodeByDDL.generate.GeneratorFacade;
import main.java.com.xiangxiang.genCodeByDDL.model.GenerateBySQLVO;
import main.java.com.xiangxiang.genCodeByDDL.model.TableSchema;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SQL 转表概要构建器
 *
 * @Author 香香
 * @Date 2024-08-14 15:32
 **/

public class TableSchemaBuilder {

    /**
     * SQL 方言
     */
    private static final MySQLDialect sqlDialect = new MySQLDialect();

    /**
     * 私有化构造方法，防止被实例化
     */
    private TableSchemaBuilder() {
    }

    /**
     * 根据建表 SQL 构建
     *
     * @param sql 建表 SQL
     * @return 生成的 GenerateBySQLVO
     */
    public static GenerateBySQLVO buildFromDDL(String sql, String packageName, String projectName) {
        // 检查传入的 SQL 字符串是否为空或仅包含空白字符
        if (StringUtils.isBlank(sql)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "SQL 语句不能为空");
        }

        List<TableSchema> tableSchemas = new ArrayList<>();
        try {
            // 1. 存储建表语句
            List<String> createTableStatements = new ArrayList<>();

            // 正则表达式匹配 CREATE TABLE 语句（支持各种大小写组合和 IF NOT EXISTS 子句）
            Pattern pattern = Pattern.compile("(?i)CREATE\\s+TABLE\\s*(IF\\s+NOT\\s+EXISTS\\s*)?`?\\w+`?\\s*\\((.*?)\\)\\s*(ENGINE\\s*=\\s*\\w+)?\\s*(DEFAULT\\s+CHARSET\\s*=\\s*\\w+)?\\s*(COMMENT\\s*=\\s*'[^']*')?(;|$)", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(sql);

            while (matcher.find()) {
                // 获取完整的 CREATE TABLE 语句
                String createTableStatement = matcher.group();
                createTableStatements.add(createTableStatement);
            }


            // 2. 处理每个 CREATE TABLE 语句
            for (String statement : createTableStatements) {
                statement = statement.trim();
                if (StringUtils.isNotBlank(statement)) {
                    // 移除外键约束
                    statement = removeForeignKeyConstraints(statement);

                    // 创建 MySqlCreateTableParser 对象以解析 SQL 创建表语句
                    MySqlCreateTableParser parser = new MySqlCreateTableParser(statement);

                    // 解析 SQL 创建表语句并获取 SQLCreateTableStatement 对象
                    SQLCreateTableStatement sqlCreateTableStatement = parser.parseCreateTable();

                    // 创建 TableSchema 对象来存储解析结果
                    TableSchema tableSchema = new TableSchema();

                    // 设置数据库名称
                    tableSchema.setDbName(sqlCreateTableStatement.getSchema());

                    // 设置表名称，使用 MySQLDialect 进行表名解析
                    tableSchema.setTableName(sqlDialect.parseTableName(sqlCreateTableStatement.getTableName()));

                    // 获取并处理表注释
                    String tableComment = null;
                    if (sqlCreateTableStatement.getComment() != null) {
                        // 去除注释的引号
                        tableComment = getExtractComment(sqlCreateTableStatement.getComment().toString());
                    }
                    tableSchema.setTableComment(tableComment);

                    // 创建列表以存储表字段
                    List<TableSchema.Field> fieldList = new ArrayList<>();

                    // 遍历 SQL 元素列表
                    for (SQLTableElement sqlTableElement : sqlCreateTableStatement.getTableElementList()) {
                        // 处理主键约束
                        if (sqlTableElement instanceof SQLPrimaryKey) {
                            SQLPrimaryKey sqlPrimaryKey = (SQLPrimaryKey) sqlTableElement;
                            setPrimaryKey(sqlPrimaryKey, fieldList);
                            // 处理列相关
                        } else if (sqlTableElement instanceof SQLColumnDefinition) {
                            SQLColumnDefinition columnDefinition = (SQLColumnDefinition) sqlTableElement;
                            setColumnKey(columnDefinition, fieldList);
                        }
                    }

                    // 将字段列表设置到 TableSchema 对象中
                    tableSchema.setFieldList(fieldList);

                    // 添加到表架构列表中
                    tableSchemas.add(tableSchema);
                }
            }

            return GeneratorFacade.generateAllBySQL(tableSchemas, packageName,projectName);
        } catch (Exception e) {
            // 记录异常并抛出业务异常
            System.out.println("SQL 解析错误");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请确认 SQL 语句正确");
        }
    }

    // 移除外键约束的辅助方法
    /**
     * 移除外键约束，并处理外键后的逗号
     *
     * @param sql 原始的 SQL 语句
     * @return 移除外键约束后的 SQL 语句
     */
    private static String removeForeignKeyConstraints(String sql) {
        // 匹配外键约束部分
        Pattern pattern = Pattern.compile("(?i),\\s*FOREIGN\\s+KEY\\s*\\(.*?\\)\\s*REFERENCES\\s*\\w+\\s*\\(.*?\\)(?:\\s*COMMENT\\s*'.*?')?", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(sql);

        StringBuffer sb = new StringBuffer();
        int lastMatchEnd = 0;

        while (matcher.find()) {
            // 匹配到的 FOREIGN KEY 约束的起始位置
            int matchStart = matcher.start();
            // 匹配到的 FOREIGN KEY 约束的结束位置
            int matchEnd = matcher.end();

            // 获取 FOREIGN KEY 约束前面的文本
            String precedingText = sql.substring(lastMatchEnd, matchStart);

            // 检查 FOREIGN KEY 约束前是否有逗号
            if (precedingText.trim().endsWith(",")) {
                // 如果有逗号，去掉逗号
                precedingText = precedingText.trim().substring(0, precedingText.trim().length() - 1);
            }

            // 将处理后的文本拼接到最终结果中
            sb.append(precedingText);
            lastMatchEnd = matchEnd;
        }

        // 添加最后一个匹配结束后剩余的部分
        sb.append(sql.substring(lastMatchEnd));

        // 去掉多余的空白字符
        return sb.toString().trim();
    }







    private static String getExtractComment(String comment) {
        // 定义正则表达式
        String regex = "'([^']*)'";

        // 编译正则表达式
        Pattern pattern = Pattern.compile(regex);

        // 匹配字符串
        Matcher matcher = pattern.matcher(comment);

        // 查找并打印结果
        if (matcher.find()) {
            return matcher.group(1);  // 输出：用户
        } else {
            return "";
        }
    }

    private static void setColumnKey(SQLColumnDefinition columnDefinition, List<TableSchema.Field> fieldList) {
        // 创建并初始化 TableSchema.Field 对象
        TableSchema.Field field = new TableSchema.Field();
        field.setFieldName(sqlDialect.parseFieldName(columnDefinition.getNameAsString()));
        field.setFieldType(columnDefinition.getDataType().toString());

        // 获取列的默认值
        String defaultValue = null;
        if (columnDefinition.getDefaultExpr() != null) {
            defaultValue = columnDefinition.getDefaultExpr().toString();
        }
        field.setDefaultValue(defaultValue);

        // 设置列的非空约束
        field.setNotNull(columnDefinition.containsNotNullConstaint());

        // 获取并处理列注释
        String comment = null;
        if (columnDefinition.getComment() != null) {
            comment = columnDefinition.getComment().toString();
            // 去除注释的引号
            if (comment.length() > 2) {
                comment = comment.substring(1, comment.length() - 1);
            }
        }
        field.setComment(comment);

        // 设置列的主键标志、是否自增、更新值等属性
        field.setPrimaryKey(columnDefinition.isPrimaryKey());
        field.setAutoIncrement(columnDefinition.isAutoIncrement());

        String onUpdate = null;
        if (columnDefinition.getOnUpdate() != null) {
            onUpdate = columnDefinition.getOnUpdate().toString();
        }
        field.setOnUpdate(onUpdate);

        // 设置字段的 Mock 类型（默认为 NONE）
//        field.setMockType(MockTypeEnum.NONE.getValue());

        // 将字段添加到字段列表中
        fieldList.add(field);
    }

    private static void setPrimaryKey(SQLPrimaryKey sqlPrimaryKey, List<TableSchema.Field> fieldList) {
        // 获取主键字段名
        String primaryFieldName = sqlDialect.parseFieldName(sqlPrimaryKey.getColumns().get(0).toString());

        // 将主键字段设置为主键
        fieldList.forEach(field -> {
            if (field.getFieldName().equals(primaryFieldName)) {
                field.setPrimaryKey(true);
            }
        });
    }

}
