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
public class JavaServiceGenerateDTO {

    private String packageName;


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

}
