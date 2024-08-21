package main.java.com.xiangxiang.genCodeByDDL.builder.exception;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import main.java.com.xiangxiang.genCodeByDDL.model.dto.JavaThrowsGenerateDTO;

import java.io.StringWriter;

/**
 * 类型：脚本生成器
 *
 * @author GenCodeByDDLPlugins
 * @date 2024/08/01
 */
@Slf4j
public class JavaErrorBuilder {

    private static Configuration configuration;

    static {
        try {
            // 初始化 FreeMarker 配置
            configuration = new Configuration(Configuration.VERSION_2_3_31);

            // 设置模板加载路径，相对于类路径
            configuration.setClassLoaderForTemplateLoading(
                    JavaErrorBuilder.class.getClassLoader(), "templates"
            );

            // 设置默认编码
            configuration.setDefaultEncoding("UTF-8");

            // 设置模板异常处理
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

            // 打印模板文件路径，检查是否可以找到模板
            if (JavaErrorBuilder.class.getClassLoader().getResource("templates/ErrorCode.java.ftl") != null) {
                System.out.println("文件存在ErrorCode.java.ftl");
            } else {
                System.out.println("模板文件不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setConfiguration(Configuration configuration) {
        JavaErrorBuilder.configuration = configuration;
    }

    /**
     * 构造 Exception 类型代码
     *
     * @param
     * @return 生成的代码
     */
    @SneakyThrows
    public static String buildJavaErrorCodeCode(String packageName) {

        // 传递参数
        JavaThrowsGenerateDTO generateDTO = new JavaThrowsGenerateDTO()
                .setPackageName(packageName);
        // 生成代码
        StringWriter stringWriter = new StringWriter();
        Template temp = configuration.getTemplate("ErrorCode.java.ftl");
        temp.process(generateDTO, stringWriter);
        return stringWriter.toString();
    }
}
