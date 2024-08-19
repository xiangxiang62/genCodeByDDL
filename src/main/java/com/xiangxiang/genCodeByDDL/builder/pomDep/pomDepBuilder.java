package main.java.com.xiangxiang.genCodeByDDL.builder.pomDep;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import main.java.com.xiangxiang.genCodeByDDL.builder.readme.pluginsREADMEBuilder;

import java.io.IOException;
import java.io.StringWriter;

/**
 * @Author 香香
 * @Date 2024-08-19 20:20
 **/
public class pomDepBuilder {


    private static Configuration configuration;

    static {
        try {
            // 初始化 FreeMarker 配置
            configuration = new Configuration(Configuration.VERSION_2_3_31);

            // 设置模板加载路径，相对于类路径
            configuration.setClassLoaderForTemplateLoading(
                    pomDepBuilder.class.getClassLoader(), "templates"
            );

            // 设置默认编码
            configuration.setDefaultEncoding("UTF-8");

            // 设置模板异常处理
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

            // 打印模板文件路径，检查是否可以找到模板
            if (pomDepBuilder.class.getClassLoader().getResource("pomDependencies.xml.ftl") != null) {
                System.out.println("模板文件存在 pomDependencies.xml.ftl");
            } else {
                System.out.println("模板文件不存在TemplateREADME");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setConfiguration(Configuration configuration) {
        pomDepBuilder.configuration = configuration;
    }

    /**
     * 构造 Java 实体代码
     *
     * @return 生成的 java 代码
     */
    public static String buildPomDep() {
        StringWriter stringWriter = new StringWriter();
        Template temp = null;
        try {
            temp = configuration.getTemplate("pomDependencies.xml.ftl");
            temp.process(null, stringWriter);
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        }

        return stringWriter.toString();
    }


}
