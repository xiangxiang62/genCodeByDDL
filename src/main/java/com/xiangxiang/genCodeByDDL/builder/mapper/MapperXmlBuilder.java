package main.java.com.xiangxiang.genCodeByDDL.builder.mapper;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import main.java.com.xiangxiang.genCodeByDDL.model.TableSchema;
import main.java.com.xiangxiang.genCodeByDDL.model.dto.MapperXmlGenerateDTO;
import main.java.com.xiangxiang.genCodeByDDL.model.dto.entity.JavaEntityGenerateDTO;
import main.java.com.xiangxiang.genCodeByDDL.model.enums.FieldTypeEnum;
import main.java.com.xiangxiang.genCodeByDDL.model.enums.SqlTypeToJavaTypeEnum;
import main.java.com.xiangxiang.genCodeByDDL.utils.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MapperXmlBuilder {

    public static Configuration configuration;

    static {
        try {
            // 初始化 FreeMarker 配置
            configuration = new Configuration(Configuration.VERSION_2_3_31);

            // 设置模板加载路径，相对于类路径
            configuration.setClassLoaderForTemplateLoading(
                    MapperXmlBuilder.class.getClassLoader(), "templates"
            );

            // 设置默认编码
            configuration.setDefaultEncoding("UTF-8");

            // 设置模板异常处理
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

            // 打印模板文件路径，检查是否可以找到模板
            if (MapperXmlBuilder.class.getClassLoader().getResource("templates/mapper.xml.ftl") != null) {
                System.out.println("模板文件存在 mapper.xml.ftl");
            } else {
                System.out.println("模板文件不存在 mapper.xml.ftl");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setConfiguration(Configuration configuration) {
        MapperXmlBuilder.configuration = configuration;
    }

    /**
     * 构造 Java 实体代码
     *
     * @param tableSchemas 表概要
     * @return 生成的 java 代码
     */
    public static List<String> buildMapperXmlCode(List<TableSchema> tableSchemas) {
        List<String> javaEntityCodeList = new ArrayList<>();
        for (TableSchema tableSchema : tableSchemas) {
            String tableName = tableSchema.getTableName();
            // StringUtils.capitalize 将字符串的第一个字符转换为大写，其余字符保持不变
            String upperCamelTableName = StringUtils.capitalize(StringUtils.toCamelCase(tableName));
            String tableComment = Optional.ofNullable(tableSchema.getTableComment()).orElse(upperCamelTableName);

            // 依次填充每一列
            List<MapperXmlGenerateDTO.FieldDTO> fieldDTOList = new ArrayList<>();
            tableSchema.getFieldList().forEach(field -> {
                String sqlType = field.getFieldType();
                // 根据 SQL 类型获取 Java 类型
                String Type = String.valueOf(FieldTypeEnum.getEnumBySqlType(sqlType));
                // 如果没有对应的 Java 类型，则使用 String 作为默认值
                if (Type == null) {
                    Type = "Text";
                }
                Boolean isPrimaryKey = false;
                if (field.isPrimaryKey()){
                    isPrimaryKey = true;
                }


                // todo
                MapperXmlGenerateDTO.FieldDTO fieldDTO = new MapperXmlGenerateDTO.FieldDTO()
                        .setComment(field.getComment())
                        .setJdbcType(Type)
                        .setIsPrimaryKey(isPrimaryKey)
                        .setFieldName(field.getFieldName());

                fieldDTOList.add(fieldDTO);
            });

            // 填充模板
            MapperXmlGenerateDTO mapperXmlGenerateDTO = new MapperXmlGenerateDTO()
                    .setCaseTableName(upperCamelTableName)
                    .setClassComment(tableComment)
                    .setFieldList(fieldDTOList);

            StringWriter stringWriter = new StringWriter();
            Template temp = null;
            try {
                temp = configuration.getTemplate("mapper.xml.ftl");
                temp.process(mapperXmlGenerateDTO, stringWriter);
            } catch (TemplateException | IOException e) {
                e.printStackTrace();
            }
            javaEntityCodeList.add(stringWriter.toString());
        }

        return javaEntityCodeList;
    }

    // 其他方法...
}
