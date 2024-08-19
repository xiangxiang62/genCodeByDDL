package main.java.com.xiangxiang.genCodeByDDL.builder.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import main.java.com.xiangxiang.genCodeByDDL.model.TableSchema;
import main.java.com.xiangxiang.genCodeByDDL.model.dto.JavaControllerGenerateDTO;
import main.java.com.xiangxiang.genCodeByDDL.model.dto.JavaServiceGenerateDTO;
import main.java.com.xiangxiang.genCodeByDDL.utils.StringUtils;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 类型：脚本生成器
 *
 * @author GenCodeByDDLPlugins
 * @date 2024/08/01
 */
@Slf4j
public class JavaServiceBuilder {

    private static Configuration configuration;

    static {
        try {
            // 初始化 FreeMarker 配置
            configuration = new Configuration(Configuration.VERSION_2_3_31);

            // 设置模板加载路径，相对于类路径
            configuration.setClassLoaderForTemplateLoading(
                    JavaServiceBuilder.class.getClassLoader(), "templates"
            );

            // 设置默认编码
            configuration.setDefaultEncoding("UTF-8");

            // 设置模板异常处理
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

            // 打印模板文件路径，检查是否可以找到模板
            if (JavaServiceBuilder.class.getClassLoader().getResource("templates/TemplateService.java.ftl") != null) {
                System.out.println("文件存在 TemplateService.java.ftl");
            } else {
                System.out.println("模板文件不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setConfiguration(Configuration configuration) {
        JavaServiceBuilder.configuration = configuration;
    }

    /**
     * 构造 Service 类型代码
     *
     * @param
     * @return 生成的代码
     */
    @SneakyThrows
    public static List<String> buildJavaServiceCode(List<TableSchema> tableSchemas) {
        List<String> crud = new ArrayList<>();
        for (TableSchema tableSchema : tableSchemas) {
            String tableComment = tableSchema.getTableComment();
            String tableName = tableSchema.getTableName().toLowerCase();
            String upperCamelTableName = StringUtils.toPascalCase(tableSchema.getTableName());

            // 依次填充每一列
//            List<FieldTypeScriptDTO> fieldDTOList = new ArrayList<>();
//            tableSchema.getFieldList().forEach(field -> {
//                FieldTypeScriptDTO fieldDTO = new FieldTypeScriptDTO()
//                        .setComment(Optional.ofNullable(field.getComment()).orElse(""))
//                        .setTypescriptType(Optional.ofNullable(FieldTypeEnum.getEnumByValue(field.getFieldType())).orElse(FieldTypeEnum.TEXT).getTypescriptType())
//                        .set(CharSequenceUtil.toCamelCase(field.getFieldName()));
//                fieldDTOList.add(fieldDTO);
//            });

            // 传递参数
            JavaServiceGenerateDTO generateDTO = new JavaServiceGenerateDTO()
                    .setClassName(upperCamelTableName) // 类名为大写的表名
                    .setClassComment(Optional.ofNullable(tableComment).orElse(upperCamelTableName))
                    .setCaseTableName(upperCamelTableName)
                    .setTableName(StringUtils.toCamelCase(tableName));// 类注释为表注释或表名
            // 生成代码
            StringWriter stringWriter = new StringWriter();
            Template temp = configuration.getTemplate("TemplateService.java.ftl");
            temp.process(generateDTO, stringWriter);
            crud.add(stringWriter.toString());
        }
        return crud;
    }
}
