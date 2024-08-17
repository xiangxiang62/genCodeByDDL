package main.java.com.xiangxiang.genCodeByDDL.model;

import java.util.List;
import java.util.Map;

/**
 * 生成 VO 创建多表
 *
 * @author GenCodeByDDLPlugins
 * @date 2024/08/09
 */
public class GenerateBySQLVO {

    /**
     * 插入 SQL
     */
    private String insertSql;

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
     * Java 对象代码
     */
    private List<String> javaObjectCode;

    /**
     * TypeScript 类型代码
     */
    private List<String> javaControllerCode;

    /**
     * PlantUML 类型代码
     */
    private String plantUmlCode;

    // 默认构造方法
    public GenerateBySQLVO() {
    }

    // 全参构造方法
    public GenerateBySQLVO(String insertSql, List<Map<String, Object>> dataList, String dataJson,
                           List<String> javaEntityCode, List<String> javaAddEntityCode,
                           List<String> javaEditEntityCode, List<String> javaObjectCode,
                           List<String> javaQueryEntityCode, List<String> javaUpdateEntityCode,
                           List<String> javaControllerCode, String plantUmlCode) {
        this.insertSql = insertSql;
        this.dataList = dataList;
        this.dataJson = dataJson;
        this.javaEntityCode = javaEntityCode;
        this.javaObjectCode = javaObjectCode;
        this.javaControllerCode = javaControllerCode;
        this.plantUmlCode = plantUmlCode;
        this.javaAddEntityCode = javaAddEntityCode;
        this.javaEditEntityCode = javaEditEntityCode;
        this.javaQueryEntityCode = javaQueryEntityCode;
        this.javaUpdateEntityCode = javaUpdateEntityCode;
    }

    public List<String> getJavaQueryEntityCode() {
        return javaQueryEntityCode;
    }

    public void setJavaQueryEntityCode(List<String> javaQueryEntityCode) {
        this.javaQueryEntityCode = javaQueryEntityCode;
    }

    public List<String> getJavaUpdateEntityCode() {
        return javaUpdateEntityCode;
    }

    public void setJavaUpdateEntityCode(List<String> javaUpdateEntityCode) {
        this.javaUpdateEntityCode = javaUpdateEntityCode;
    }

    public List<String> getJavaEditEntityCode() {
        return javaEditEntityCode;
    }

    public void setJavaEditEntityCode(List<String> javaEditEntityCode) {
        this.javaEditEntityCode = javaEditEntityCode;
    }

    public List<String> getJavaAddEntityCode() {
        return javaAddEntityCode;
    }

    public void setJavaAddEntityCode(List<String> javaAddEntityCode) {
        this.javaAddEntityCode = javaAddEntityCode;
    }

    // Getter 和 Setter 方法
    public String getInsertSql() {
        return insertSql;
    }

    public void setInsertSql(String insertSql) {
        this.insertSql = insertSql;
    }

    public List<Map<String, Object>> getDataList() {
        return dataList;
    }

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }

    public String getDataJson() {
        return dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }

    public List<String> getJavaEntityCode() {
        return javaEntityCode;
    }

    public void setJavaEntityCode(List<String> javaEntityCode) {
        this.javaEntityCode = javaEntityCode;
    }

    public List<String> getJavaObjectCode() {
        return javaObjectCode;
    }

    public void setJavaObjectCode(List<String> javaObjectCode) {
        this.javaObjectCode = javaObjectCode;
    }

    public List<String> getJavaControllerCode() {
        return javaControllerCode;
    }

    public void setJavaControllerCode(List<String> typescriptTypeCode) {
        this.javaControllerCode = typescriptTypeCode;
    }

    public String getPlantUmlCode() {
        return plantUmlCode;
    }

    public void setPlantUmlCode(String plantUmlCode) {
        this.plantUmlCode = plantUmlCode;
    }
}
