package main.java.com.xiangxiang.genCodeByDDL.builder;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import main.java.com.xiangxiang.genCodeByDDL.model.TableSchema;
import main.java.com.xiangxiang.genCodeByDDL.model.dto.JavaEntityGenerateDTO;
import main.java.com.xiangxiang.genCodeByDDL.model.dto.JavaUpdateEntityGenerateDTO;
import main.java.com.xiangxiang.genCodeByDDL.utils.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class pluginsREADMEBuilder {

    private static Configuration configuration;

    static {
        try {
            // 初始化 FreeMarker 配置
            configuration = new Configuration(Configuration.VERSION_2_3_31);

            // 设置模板加载路径，相对于类路径
            configuration.setClassLoaderForTemplateLoading(
                    pluginsREADMEBuilder.class.getClassLoader(), "templates"
            );

            // 设置默认编码
            configuration.setDefaultEncoding("UTF-8");

            // 设置模板异常处理
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

            // 打印模板文件路径，检查是否可以找到模板
            if (pluginsREADMEBuilder.class.getClassLoader().getResource("TemplateREADME.md.ftl") != null) {
                System.out.println("模板文件存在 TemplateREADME.md.ftl");
            } else {
                System.out.println("模板文件不存在TemplateREADME");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setConfiguration(Configuration configuration) {
        pluginsREADMEBuilder.configuration = configuration;
    }

    /**
     * 构造 Java 实体代码
     *
     * @return 生成的 java 代码
     */
    public static String buildREADME() {
        StringWriter stringWriter = new StringWriter();
        Template temp = null;
        try {
            temp = configuration.getTemplate("TemplateREADME.md.ftl");
            temp.process(null, stringWriter);
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        }

        return stringWriter.toString();
    }

    // 其他方法...
}
