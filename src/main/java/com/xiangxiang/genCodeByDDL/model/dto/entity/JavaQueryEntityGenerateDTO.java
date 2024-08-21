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
public class JavaQueryEntityGenerateDTO {


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
