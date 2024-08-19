package main.java.com.xiangxiang.genCodeByDDL.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 生成 VO 创建多表
 *
 * @author GenCodeByDDLPlugins
 * @date 2024/08/09
 */
@Data
public class GenerateBySQLVO {

    /**
     * README
     */
    private String README;

    /**
     * 依赖
     */
    private String pomDep;

    /**
     * 数据列表
     */
    private List<Map<String, Object>> dataList;

    /**
     * 数据 json
     */
    private String dataJson;

    /**
     * Java 实体代码
     */
    private List<String> javaEntityCode;

    /**
     * Java 请求实体代码
     */
    private List<String> javaAddEntityCode;

    /**
     * Java 编辑实体代码
     */
    private List<String> javaEditEntityCode;

    /**
     * Java 查询实体代码
     */
    private List<String> javaQueryEntityCode;

    /**
     * Java 更新实体代码
     */
    private List<String> javaUpdateEntityCode;

    /**
     * Java 视图实体代码
     */
    private List<String> javaEntityVOCode;

    /**
     * MapperXml代码
     */
    private List<String> mapperXmlCode;

    /**
     * Java 对象代码
     */
    private List<String> javaObjectCode;

    /**
     * Java 持久层代码
     */
    private List<String> javaMapperCode;

    /**
     * Controller 类型代码
     */
    private List<String> javaControllerCode;

    /**
     * Service 类型代码
     */
    private List<String> javaServiceCode;

    /**
     * ServiceImpl 类型代码
     */
    private List<String> javaServiceImplCode;

    /**
     * Service 类型代码
     */
    private List<String> javaCorsConfigCode;

    /**
     * PlantUML 类型代码
     */
    private String plantUmlCode;

//    // 默认构造方法
//    public GenerateBySQLVO() {
//    }
//
//    // 全参构造方法
//    public GenerateBySQLVO(String README, List<Map<String, Object>> dataList, String dataJson,
//                           List<String> javaEntityCode, List<String> javaAddEntityCode,
//                           List<String> javaEditEntityCode, List<String> javaObjectCode,
//                           List<String> javaQueryEntityCode, List<String> javaUpdateEntityCode,
//                           List<String> javaControllerCode, List<String> javaEntityVOCode,
//                           String plantUmlCode, List<String> javaMapperCode,
//                           List<String> mapperXmlCode, List<String> javaServiceCode,
//                           List<String> javaServiceImplCode) {
//        this.README = README;
//        this.dataList = dataList;
//        this.dataJson = dataJson;
//        this.javaEntityCode = javaEntityCode;
//        this.javaObjectCode = javaObjectCode;
//        this.javaControllerCode = javaControllerCode;
//        this.plantUmlCode = plantUmlCode;
//        this.javaAddEntityCode = javaAddEntityCode;
//        this.javaEditEntityCode = javaEditEntityCode;
//        this.javaQueryEntityCode = javaQueryEntityCode;
//        this.javaUpdateEntityCode = javaUpdateEntityCode;
//        this.javaEntityVOCode = javaEntityVOCode;
//        this.javaMapperCode = javaMapperCode;
//        this.mapperXmlCode = mapperXmlCode;
//        this.javaServiceCode = javaServiceCode;
//        this.javaServiceImplCode = javaServiceImplCode;
//    }
//
//    public List<String> getJavaServiceImplCode() {
//        return javaServiceImplCode;
//    }
//
//    public void setJavaServiceImplCode(List<String> javaServiceImplCode) {
//        this.javaServiceImplCode = javaServiceImplCode;
//    }
//
//    public List<String> getJavaServiceCode() {
//        return javaServiceCode;
//    }
//
//    public void setJavaServiceCode(List<String> javaServiceCode) {
//        this.javaServiceCode = javaServiceCode;
//    }
//
//    public List<String> getMapperXmlCode() {
//        return mapperXmlCode;
//    }
//
//    public void setMapperXmlCode(List<String> mapperXmlCode) {
//        this.mapperXmlCode = mapperXmlCode;
//    }
//
//    public List<String> getJavaMapperCode() {
//        return javaMapperCode;
//    }
//
//    public void setJavaMapperCode(List<String> javaMapperCode) {
//        this.javaMapperCode = javaMapperCode;
//    }
//
//    public List<String> getJavaEntityVOCode() {
//        return javaEntityVOCode;
//    }
//
//    public void setJavaEntityVOCode(List<String> javaEntityVOCode) {
//        this.javaEntityVOCode = javaEntityVOCode;
//    }
//
//    public List<String> getJavaQueryEntityCode() {
//        return javaQueryEntityCode;
//    }
//
//    public void setJavaQueryEntityCode(List<String> javaQueryEntityCode) {
//        this.javaQueryEntityCode = javaQueryEntityCode;
//    }
//
//    public List<String> getJavaUpdateEntityCode() {
//        return javaUpdateEntityCode;
//    }
//
//    public void setJavaUpdateEntityCode(List<String> javaUpdateEntityCode) {
//        this.javaUpdateEntityCode = javaUpdateEntityCode;
//    }
//
//    public List<String> getJavaEditEntityCode() {
//        return javaEditEntityCode;
//    }
//
//    public void setJavaEditEntityCode(List<String> javaEditEntityCode) {
//        this.javaEditEntityCode = javaEditEntityCode;
//    }
//
//    public List<String> getJavaAddEntityCode() {
//        return javaAddEntityCode;
//    }
//
//    public void setJavaAddEntityCode(List<String> javaAddEntityCode) {
//        this.javaAddEntityCode = javaAddEntityCode;
//    }
//
//    // Getter 和 Setter 方法
//    public String getREADME() {
//        return README;
//    }
//
//    public void setREADME(String insertSql) {
//        this.README = insertSql;
//    }
//
//    public List<Map<String, Object>> getDataList() {
//        return dataList;
//    }
//
//    public void setDataList(List<Map<String, Object>> dataList) {
//        this.dataList = dataList;
//    }
//
//    public String getDataJson() {
//        return dataJson;
//    }
//
//    public void setDataJson(String dataJson) {
//        this.dataJson = dataJson;
//    }
//
//    public List<String> getJavaEntityCode() {
//        return javaEntityCode;
//    }
//
//    public void setJavaEntityCode(List<String> javaEntityCode) {
//        this.javaEntityCode = javaEntityCode;
//    }
//
//    public List<String> getJavaObjectCode() {
//        return javaObjectCode;
//    }
//
//    public void setJavaObjectCode(List<String> javaObjectCode) {
//        this.javaObjectCode = javaObjectCode;
//    }
//
//    public List<String> getJavaControllerCode() {
//        return javaControllerCode;
//    }
//
//    public void setJavaControllerCode(List<String> typescriptTypeCode) {
//        this.javaControllerCode = typescriptTypeCode;
//    }
//
//    public String getPlantUmlCode() {
//        return plantUmlCode;
//    }
//
//    public void setPlantUmlCode(String plantUmlCode) {
//        this.plantUmlCode = plantUmlCode;
//    }
}
