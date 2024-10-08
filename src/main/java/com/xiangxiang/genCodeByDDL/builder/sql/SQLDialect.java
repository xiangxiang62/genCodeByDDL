package main.java.com.xiangxiang.genCodeByDDL.builder.sql;

/**
 * SQL 方言
 *
 * @author 香香
 */
public interface SQLDialect {

    /**
     * 封装字段名
     *
     * @param name 名字
     * @return {@link String }
     */
    String wrapFieldName(String name);

    /**
     * 解析字段名
     *
     * @param fieldName 字段名称
     * @return {@link String }
     */
    String parseFieldName(String fieldName);

    /**
     * 封装表名
     *
     * @param name 名字
     * @return {@link String }
     */
    String wrapTableName(String name);

    /**
     * 解析表名
     *
     * @param tableName 表名
     * @return {@link String }
     */
    String parseTableName(String tableName);
}
