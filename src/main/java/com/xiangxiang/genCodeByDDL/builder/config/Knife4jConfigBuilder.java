package main.java.com.xiangxiang.genCodeByDDL.builder.config;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import main.java.com.xiangxiang.genCodeByDDL.model.TableSchema;
import main.java.com.xiangxiang.genCodeByDDL.model.dto.Knife4jGenerateDTO;
import main.java.com.xiangxiang.genCodeByDDL.model.dto.entity.JavaEntityGenerateDTO;
import main.java.com.xiangxiang.genCodeByDDL.model.enums.SqlTypeToJavaTypeEnum;
import main.java.com.xiangxiang.genCodeByDDL.utils.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Knife4jConfigBuilder {

    public static Configuration configuration;

    static {
        try {
            // 初始化 FreeMarker 配置
            configuration = new Configuration(Configuration.VERSION_2_3_31);

            // 设置模板加载路径，相对于类路径
            configuration.setClassLoaderForTemplateLoading(
                    Knife4jConfigBuilder.class.getClassLoader(), "templates"
            );

            // 设置默认编码
            configuration.setDefaultEncoding("UTF-8");

            // 设置模板异常处理
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

            // 打印模板文件路径，检查是否可以找到模板
            if (Knife4jConfigBuilder.class.getClassLoader().getResource("templates/knife4j.java.ftl") != null) {
                System.out.println("模板文件存在knife4j.java.ftl");
            } else {
                System.out.println("模板文件不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setConfiguration(Configuration configuration) {
        Knife4jConfigBuilder.configuration = configuration;
    }

    /**
     * 构造 Knife4j 代码
     *
     * @param tableSchemas 表概要
     * @return 生成的 java 代码
     */
    public static String buildKnife4jConfig(List<TableSchema> tableSchemas, String packageName,String projectName) {
        // 填充模板
        Knife4jGenerateDTO jGenerateDTO = new Knife4jGenerateDTO()
                .setProjectName(projectName)
                .setPackageName(packageName);

        StringWriter stringWriter = new StringWriter();
        Template temp = null;
        try {
            temp = configuration.getTemplate("knife4j.java.ftl");
            temp.process(jGenerateDTO, stringWriter);
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        }
       return stringWriter.toString();
    }

    // 其他方法...
}
