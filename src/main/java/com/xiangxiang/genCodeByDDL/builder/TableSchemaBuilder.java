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
    public static GenerateBySQLVO buildFromDDL(String sql,String packageName) {
        // 检查传入的 SQL 字符串是否为空或仅包含空白字符
        if (StringUtils.isBlank(sql)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "SQL 语句不能为空");
        }

        List<TableSchema> tableSchemas = new ArrayList<>();
        try {
            // 1
            // 存储建表语句
            List<String> createTableStatements = new ArrayList<>();

            // 分割 SQL 语句为单独的部分
            int index = 0;
            while (index < sql.length()) {
                int createIndex = sql.indexOf("create table", index);
                if (createIndex == -1) break;

                int endIndex = sql.indexOf(";", createIndex);
                if (endIndex == -1) break;

                // 将 SQL 语句分隔到下一个分号位置
                String createTableStatement = sql.substring(createIndex, endIndex + 1).trim();
                createTableStatements.add(createTableStatement);

                // 更新索引以继续查找下一个 create table
                index = endIndex + 1;
            }
            // 2

            // 分割 SQL 语句为单独的创建表语句

            for (String statement : createTableStatements) {
                statement = statement.trim();
                if (StringUtils.isNotBlank(statement)) {
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

            return GeneratorFacade.generateAllBySQL(tableSchemas,packageName);
        } catch (Exception e) {
            // 记录异常并抛出业务异常
            System.out.println("SQL 解析错误");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请确认 SQL 语句正确");
        }
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
