package main.java.com.xiangxiang.genCodeByDDL.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 根据 SQL 生成请求体
 *
 * @author 香香
 */
@Data
public class GenerateBySqlRequest implements Serializable {

    private String sql;

    private static final long serialVersionUID = 3191241716373120793L;

}
