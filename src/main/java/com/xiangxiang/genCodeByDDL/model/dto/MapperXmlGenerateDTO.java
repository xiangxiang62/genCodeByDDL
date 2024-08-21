package main.java.com.xiangxiang.genCodeByDDL.model.dto;

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
public class MapperXmlGenerateDTO {


    /**
     * 包名
     */
    private String packageName;

    /**
     * 大写表名名
     */
    private String caseTableName;

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
        private String jdbcType;

        /**
         * 是否主键
         */
        private Boolean isPrimaryKey = false;
    }

}
