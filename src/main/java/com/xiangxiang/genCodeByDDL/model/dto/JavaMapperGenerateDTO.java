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
public class JavaMapperGenerateDTO {


    private String packageName;

    /**
     * 大写表名名
     */
    private String caseTableName;

}
