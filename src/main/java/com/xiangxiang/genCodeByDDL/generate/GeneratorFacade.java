package main.java.com.xiangxiang.genCodeByDDL.generate;

import main.java.com.xiangxiang.genCodeByDDL.builder.config.JavaConfigBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.config.Knife4jConfigBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.controller.JavaControllerBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.dto.JavaAddRequestBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.dto.JavaEditRequestBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.dto.JavaQueryRequestBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.dto.JavaUpdateRequestBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.entity.JavaEntityCodeBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.exception.JavaBusinessExceptionBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.mapper.JavaMapperBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.mapper.MapperXmlBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.pomDep.pomDepBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.readme.pluginsREADMEBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.service.JavaServiceBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.service.JavaServiceImplBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.vo.JavaEntityVOBuilder;
import main.java.com.xiangxiang.genCodeByDDL.model.GenerateBySQLVO;
import main.java.com.xiangxiang.genCodeByDDL.model.TableSchema;


import java.util.Collections;
import java.util.List;

public class GeneratorFacade {
    GeneratorFacade() {
    }


    public static GenerateBySQLVO generateAllBySQL(List<TableSchema> tableSchemas,String packageName,String projectName) {
        // 校验
        // validSchema(tableSchemas);
        System.out.println("开始构建数据");

        // 生成 java 请求实体代码
        List<String> javaAddEntityCode = JavaAddRequestBuilder.buildJavaAddEntityCode(tableSchemas,packageName);

        List<String> javaEditEntityCode = JavaEditRequestBuilder.buildJavaEditEntityCode(tableSchemas,packageName);

        List<String> javaQueryEntityCode = JavaQueryRequestBuilder.buildJavaQueryEntityCode(tableSchemas,packageName);

        List<String> javaUpdateEntityCode = JavaUpdateRequestBuilder.buildJavaUpdateEntityCode(tableSchemas,packageName);

        List<String> javaEntityCode = JavaEntityCodeBuilder.buildJavaEntityCode(tableSchemas,packageName);

        List<String> javaEntityVOCode = JavaEntityVOBuilder.buildJavaEntityVOCode(tableSchemas,packageName);

        List<String> javaMapperCode = JavaMapperBuilder.buildJavaMapperCode(tableSchemas,packageName);

        List<String> mapperXmlCode = MapperXmlBuilder.buildMapperXmlCode(tableSchemas,packageName);

        List<String> javaServiceCode = JavaServiceBuilder.buildJavaServiceCode(tableSchemas,packageName);

        List<String> javaServiceImplCode = JavaServiceImplBuilder.buildJavaServiceImplCode(tableSchemas,packageName);

        String javaCorsConfigCode = JavaConfigBuilder.buildJavaCorsConfigCode(packageName);

        String pluginsREADME = pluginsREADMEBuilder.buildREADME();

        String PomDep = pomDepBuilder.buildPomDep();
        // 生成控制层代码
        List<String> javaControllerCode = JavaControllerBuilder.buildJavaControllerCode(tableSchemas,packageName);

        String knife4jConfig = Knife4jConfigBuilder.buildKnife4jConfig(packageName, projectName);

        String businessExceptionCode = JavaBusinessExceptionBuilder.buildJavaBusinessExceptionCode(packageName);

        System.out.println("建数据结束");
        GenerateBySQLVO generateBySQLVO = new GenerateBySQLVO();
        generateBySQLVO.setJavaEntityCode(javaEntityCode);
        generateBySQLVO.setJavaControllerCode(javaControllerCode);
        generateBySQLVO.setJavaAddEntityCode(javaAddEntityCode);
        generateBySQLVO.setJavaEditEntityCode(javaEditEntityCode);
        generateBySQLVO.setJavaQueryEntityCode(javaQueryEntityCode);
        generateBySQLVO.setJavaUpdateEntityCode(javaUpdateEntityCode);
        generateBySQLVO.setJavaEntityVOCode(javaEntityVOCode);
        generateBySQLVO.setJavaMapperCode(javaMapperCode);
        generateBySQLVO.setREADME(pluginsREADME);
        generateBySQLVO.setMapperXmlCode(mapperXmlCode);
        generateBySQLVO.setJavaServiceCode(javaServiceCode);
        generateBySQLVO.setJavaServiceImplCode(javaServiceImplCode);
        generateBySQLVO.setJavaCorsConfigCode(Collections.singletonList(javaCorsConfigCode));
        generateBySQLVO.setPomDep(PomDep);
        generateBySQLVO.setKnife4jConfig(knife4jConfig);
        generateBySQLVO.setBusinessExceptionCode(businessExceptionCode);
        // 封装返回
        return  generateBySQLVO;
    }

}
