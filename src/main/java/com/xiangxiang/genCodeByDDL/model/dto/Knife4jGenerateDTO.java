package main.java.com.xiangxiang.genCodeByDDL.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author 香香
 * @Date 2024-08-19 22:36
 **/
@Data
@Accessors(chain = true)
public class Knife4jGenerateDTO {

    /**
     * 包名
     */
    private String packageName;

    /**
     * 项目名称
     */
    private String projectName;

}
