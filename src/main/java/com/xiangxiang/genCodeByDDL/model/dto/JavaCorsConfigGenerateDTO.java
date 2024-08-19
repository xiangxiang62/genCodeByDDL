package main.java.com.xiangxiang.genCodeByDDL.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Typescript 类型生成封装类
 *
 * @author GenCodeByDDLPlugins
 * @date 2024/08/02
 */
@Data
@Accessors(chain = true)
public class JavaCorsConfigGenerateDTO {

    private String packageName = "xiangxiang";
    /**
     * 类名
     */
    private String className;

    /**
     * 类注释
     */
    private String classComment;

    /**
     * 表名名
     */
    private String tableName;

    /**
     * 大写表名名
     */
    private String caseTableName;

    /**
     * 列信息列表
     */
    private List<FieldTypeScriptDTO> fieldList;

    /**
     * 列信息
     */
    @Data
    @Accessors(chain = true)
    public static class FieldTypeScriptDTO {

        /**
         * Typescript 类型
         */
        private String typescriptType;

        /**
         * 字段注释
         */
        private String comment;
    }

}
