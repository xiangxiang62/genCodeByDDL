package main.java.com.xiangxiang.genCodeByDDL.model.dto.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Java 实体生成封装类
 *
 * @author GenCodeByDDLPlugins
 * @date 2024/08/02
 */
@Data
@Accessors(chain = true)
public class JavaEntityGenerateDTO {

    private String packageName;

    /**
     * 类名
     */
    private String className;

    /**
     * 类注释
     */
    private String classComment;

    /**
     * 列信息列表
     */
    private List<FieldDTO> fieldList;

    /**
     * 列信息
     */
    @Data
    @Accessors(chain = true)
    public static class FieldDTO {
        /**
         * 字段名
         */
        private String fieldName;

        /**
         * Java 类型
         */
        private String entityType;

        /**
         * 注释（字段中文名）
         */
        private String comment;

        /**
         * 是否主键
         */
        private Boolean isPrimaryKey = false;
    }

}
