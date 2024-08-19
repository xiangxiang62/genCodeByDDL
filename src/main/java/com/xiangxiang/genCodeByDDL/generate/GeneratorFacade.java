package main.java.com.xiangxiang.genCodeByDDL.generate;

import main.java.com.xiangxiang.genCodeByDDL.builder.config.JavaConfigBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.controller.JavaControllerBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.dto.JavaAddRequestBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.dto.JavaEditRequestBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.dto.JavaQueryRequestBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.dto.JavaUpdateRequestBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.entity.JavaEntityCodeBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.mapper.JavaMapperBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.mapper.MapperXmlBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.pomDep.pomDepBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.readme.pluginsREADMEBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.service.JavaServiceBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.service.JavaServiceImplBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.vo.JavaEntityVOBuilder;
import main.java.com.xiangxiang.genCodeByDDL.common.ErrorCode;
import main.java.com.xiangxiang.genCodeByDDL.exception.BusinessException;
import main.java.com.xiangxiang.genCodeByDDL.model.GenerateBySQLVO;
import main.java.com.xiangxiang.genCodeByDDL.model.TableSchema;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class GeneratorFacade {
    GeneratorFacade() {

    }


    public static GenerateBySQLVO generateAllBySQL(List<TableSchema> tableSchemas,String packageName) {
        // 校验
//        validSchema(tableSchemas);
        System.out.println("开始构建数据");
//        int mockNum = tableSchema.getMockNum();
        // 生成模拟数据
//        List<Map<String, Object>> dataList = MockDataBuilder.generateMockData(tableSchemas, 10);

//        // 生成数据 json
//        String dataJson = JsonBuilder.buildJson(dataList);
        // 生成 java 实体代码
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

        List<String> javaCorsConfigCode = JavaConfigBuilder.buildJavaCorsConfigCode(tableSchemas,packageName);

        String pluginsREADME = pluginsREADMEBuilder.buildREADME();

        String PomDep = pomDepBuilder.buildPomDep();
        // 生成控制层代码
        List<String> javaControllerCode = JavaControllerBuilder.buildJavaControllerCode(tableSchemas,packageName);

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
        generateBySQLVO.setJavaCorsConfigCode(javaCorsConfigCode);
        generateBySQLVO.setPomDep(PomDep);
        // 封装返回
        return  generateBySQLVO;
    }


    /**
     * 验证 schema
     *
     * @param tableSchema 表概要
     */
    public static void validSchema(TableSchema tableSchema) {
        if (tableSchema == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "数据为空");
        }
        String tableName = tableSchema.getTableName();
        if (StringUtils.isBlank(tableName)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "表名不能为空");
        }
        Integer mockNum = tableSchema.getMockNum();
        // 默认生成 20 条
        if (tableSchema.getMockNum() == null) {
            tableSchema.setMockNum(20);
            mockNum = 20;
        }
        if (mockNum > 100 || mockNum < 10) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "生成条数设置错误");
        }
        List<TableSchema.Field> fieldList = tableSchema.getFieldList();
        if (CollectionUtils.isEmpty(fieldList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "字段列表不能为空");
        }
        for (TableSchema.Field field : fieldList) {
            validField(field);
        }
    }

    /**
     * 校验字段
     */
    public static void validField(TableSchema.Field field) {
        String fieldName = field.getFieldName();
        String fieldType = field.getFieldType();
        if (StringUtils.isBlank(fieldName)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "字段名不能为空");
        }
        if (StringUtils.isBlank(fieldType)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "字段类型不能为空");
        }
    }
}
