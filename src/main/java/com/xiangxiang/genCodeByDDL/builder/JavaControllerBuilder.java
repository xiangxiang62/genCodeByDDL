package main.java.com.xiangxiang.genCodeByDDL.builder;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import main.java.com.xiangxiang.genCodeByDDL.model.TableSchema;
import main.java.com.xiangxiang.genCodeByDDL.model.dto.JavaControllerGenerateDTO;
import main.java.com.xiangxiang.genCodeByDDL.utils.StringUtils;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 类型：脚本生成器
 *
 * @author cong
 * @date 2024/08/01
 */
@Slf4j
public class JavaControllerBuilder {

    private static Configuration configuration;

    static {
        try {
            // 初始化 FreeMarker 配置
            configuration = new Configuration(Configuration.VERSION_2_3_31);
            // 设置模板加载路径
            configuration.setClassLoaderForTemplateLoading(
                    JavaControllerBuilder.class.getClassLoader(), "main/java/com/xiangxiang/genCodeByDDL/templates"
            );
            // 设置默认编码
            configuration.setDefaultEncoding("UTF-8");
            // 设置模板异常处理
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            // 打印模板加载路径
            System.out.println("wenjian: " +
                    JavaControllerBuilder.class.getClassLoader().getResource("main/java/com/xiangxiang/genCodeByDDL/templates/java_entity.ftl"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setConfiguration(Configuration configuration) {
        JavaControllerBuilder.configuration = configuration;
    }

    /**
     * 构造 Typescript 类型代码
     *
     * @param
     * @return 生成的代码
     */
    @SneakyThrows
    public static List<String> buildJavaControllerCode(List<TableSchema> tableSchemas) {
        List<String> crud = new ArrayList<>();
        for (TableSchema tableSchema : tableSchemas) {
            String tableComment = tableSchema.getTableComment();
            String tableName = tableSchema.getTableName().toLowerCase();
            String upperCamelTableName = StringUtils.toCamelCase(tableSchema.getTableName());

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
            JavaControllerGenerateDTO generateDTO = new JavaControllerGenerateDTO()
                    .setClassName(upperCamelTableName) // 类名为大写的表名
                    .setClassComment(Optional.ofNullable(tableComment).orElse(upperCamelTableName))
                    .setCaseTableName(upperCamelTableName)
                    .setTableName(tableName);// 类注释为表注释或表名
            // 生成代码
            StringWriter stringWriter = new StringWriter();
            Template temp = configuration.getTemplate("java_controller.ftl");
            temp.process(generateDTO, stringWriter);
            crud.add(stringWriter.toString());
        }
        return crud;
    }
}
