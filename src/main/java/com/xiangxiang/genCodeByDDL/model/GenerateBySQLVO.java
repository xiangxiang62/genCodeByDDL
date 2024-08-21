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
     * knife4jConfig
     */
    private String knife4jConfig;

    /**
     * README
     */
    private String README;

    /**
     * 依赖
     */
    private String pomDep;

    /**
     * errorCode
     */
    private String errorCode;


    /**
     * Exception
     */
    private String businessExceptionCode;

    /**
     * throwIf
     */
    private String throwIfCode;

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
     * 跨域 类型代码
     */
    private List<String> javaCorsConfigCode;

}
