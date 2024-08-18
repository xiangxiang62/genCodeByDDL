package main.java.com.xiangxiang.genCodeByDDL.model.dto.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import main.java.com.xiangxiang.genCodeByDDL.model.dto.JavaControllerGenerateDTO;

import java.util.List;

/**
 * Java 实体生成封装类
 *
 * @author GenCodeByDDLPlugins
 * @date 2024/08/02
 */
@Data
@Accessors(chain = true)
public class JavaAddEntityGenerateDTO {
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
    private List<JavaControllerGenerateDTO.FieldTypeScriptDTO> fieldList;

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
