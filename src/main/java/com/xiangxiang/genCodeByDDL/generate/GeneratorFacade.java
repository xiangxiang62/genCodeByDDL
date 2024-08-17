package main.java.com.xiangxiang.genCodeByDDL.generate;

import main.java.com.xiangxiang.genCodeByDDL.builder.JavaAddRequestBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.JavaEditRequestBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.JavaEntityCodeBuilder;
import main.java.com.xiangxiang.genCodeByDDL.builder.JavaControllerBuilder;
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


    public static GenerateBySQLVO generateAllBySQL(List<TableSchema> tableSchemas) {
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
        List<String> javaAddEntityCode = JavaAddRequestBuilder.buildJavaAddEntityCode(tableSchemas);
        List<String> javaEditEntityCode = JavaEditRequestBuilder.buildJavaEditEntityCode(tableSchemas);
        List<String> javaEntityCode = JavaEntityCodeBuilder.buildJavaEntityCode(tableSchemas);
        // 生成控制层代码
        List<String> javaControllerCode = JavaControllerBuilder.buildJavaControllerCode(tableSchemas);
//        // 生成 java 对象代码
//        String javaObjectCode = JavaCodeBuilder.buildJavaObjectCode(tableSchema, dataList);
//        // 生成 typescript 类型代码
//        List<String> typescriptTypeCode = TypeScriptBuilder.buildTypeScriptTypeCode(tableSchemas);
        // 生成 plantuml 类型代码
//        String plantUmlCode = PlantUmlBuilder.buildPlantUmlCode(tableSchemas);
        System.out.println("建数据结束");
        GenerateBySQLVO generateBySQLVO = new GenerateBySQLVO();
        generateBySQLVO.setJavaEntityCode(javaEntityCode);
        generateBySQLVO.setJavaControllerCode(javaControllerCode);
        generateBySQLVO.setJavaAddEntityCode(javaAddEntityCode);
        generateBySQLVO.setJavaEditEntityCode(javaEditEntityCode);
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
