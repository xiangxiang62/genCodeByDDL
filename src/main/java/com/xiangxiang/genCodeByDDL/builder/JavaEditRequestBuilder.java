package main.java.com.xiangxiang.genCodeByDDL.builder;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import main.java.com.xiangxiang.genCodeByDDL.model.TableSchema;
import main.java.com.xiangxiang.genCodeByDDL.model.dto.JavaAddEntityGenerateDTO;
import main.java.com.xiangxiang.genCodeByDDL.model.dto.JavaEditEntityGenerateDTO;
import main.java.com.xiangxiang.genCodeByDDL.model.dto.JavaEntityGenerateDTO;
import main.java.com.xiangxiang.genCodeByDDL.utils.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JavaEditRequestBuilder {

    private static Configuration configuration;

    static {
        try {
            // 初始化 FreeMarker 配置
            configuration = new Configuration(Configuration.VERSION_2_3_31);

            // 设置模板加载路径，相对于类路径
            configuration.setClassLoaderForTemplateLoading(
                    JavaEditRequestBuilder.class.getClassLoader(), "templates"
            );

            // 设置默认编码
            configuration.setDefaultEncoding("UTF-8");

            // 设置模板异常处理
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

            // 打印模板文件路径，检查是否可以找到模板
            if (JavaEditRequestBuilder.class.getClassLoader().getResource("templates/TemplateEditRequest.java.ftl") != null) {
                System.out.println("模板文件存在 TemplateEditRequest.java.ftl");
            } else {
                System.out.println("模板文件不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setConfiguration(Configuration configuration) {
        JavaEditRequestBuilder.configuration = configuration;
    }

    /**
     * 构造 Java 实体代码
     *
     * @param tableSchemas 表概要
     * @return 生成的 java 代码
     */
    public static List<String> buildJavaEditEntityCode(List<TableSchema> tableSchemas) {
        List<String> javaEntityCodeList = new ArrayList<>();
        for (TableSchema tableSchema : tableSchemas) {
            String tableName = tableSchema.getTableName();
            // StringUtils.capitalize 将字符串的第一个字符转换为大写，其余字符保持不变
            String upperCamelTableName = StringUtils.capitalize(StringUtils.toCamelCase(tableName));
            String tableComment = Optional.ofNullable(tableSchema.getTableComment()).orElse(upperCamelTableName);
            // 依次填充每一列
            List<JavaEntityGenerateDTO.FieldDTO> fieldDTOList = new ArrayList<>();
            tableSchema.getFieldList().forEach(field -> {
                JavaEntityGenerateDTO.FieldDTO fieldDTO = new JavaEntityGenerateDTO.FieldDTO()
                        .setComment(field.getComment())
                        .setEntityType(field.getFieldType())
                        .setFieldName(field.getFieldName());
                fieldDTOList.add(fieldDTO);
            });
            // 传递参数
            JavaEditEntityGenerateDTO javaEditEntityGenerateDTO = new JavaEditEntityGenerateDTO()
                    .setClassName(upperCamelTableName) // 类名为大写的表名
                    .setClassComment(Optional.ofNullable(tableComment).orElse(upperCamelTableName))
                    .setCaseTableName(upperCamelTableName)
                    .setTableName(tableName);// 类注释为表注释或表名

            StringWriter stringWriter = new StringWriter();
            Template temp = null;
            try {
                temp = configuration.getTemplate("TemplateEditRequest.java.ftl");
                temp.process(javaEditEntityGenerateDTO, stringWriter);
            } catch (TemplateException | IOException e) {
                e.printStackTrace();
            }
            javaEntityCodeList.add(stringWriter.toString());
        }

        return javaEntityCodeList;
    }

    // 其他方法...
}
