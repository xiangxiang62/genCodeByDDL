package main.java.com.xiangxiang.genCodeByDDL.model.enums;

import lombok.Getter;

/**
 * @Author 香香
 * @Date 2024-08-17 21:19
 **/
@Getter
public enum SqlTypeToJavaTypeEnum {

    // SQL 数据类型与 Java 数据类型的映射
    CHAR("char", "String"),
    VARCHAR("varchar", "String"),
    TEXT("text", "String"),
    BIGINT("bigint", "Long"),
    INT("int", "Integer"),
    SMALLINT("smallint", "Short"),
    TINYINT("tinyint", "Byte"),
    FLOAT("float", "Float"),
    DOUBLE("double", "Double"),
    DECIMAL("decimal", "BigDecimal"),
    DATE("date", "LocalDate"),
    DATETIME("datetime", "LocalDateTime"),
    TIMESTAMP("timestamp", "Instant"),
    BOOLEAN("boolean", "Boolean");

    /**
     * SQL 数据类型
     */
    private final String sqlType;

    /**
     * 对应的 Java 数据类型
     */
    private final String javaType;

    SqlTypeToJavaTypeEnum(String sqlType, String javaType) {
        this.sqlType = sqlType;
        this.javaType = javaType;
    }

    /**
     * 获取 SQL 数据类型对应的 Java 数据类型
     *
     * @param sqlType SQL 数据类型
     * @return 对应的 Java 数据类型，如果没有匹配的类型则返回 {@code null}
     */
    public static String getJavaTypeBySqlType(String sqlType) {
        for (SqlTypeToJavaTypeEnum type : values()) {
            if (type.sqlType.equalsIgnoreCase(sqlType)) {
                return type.javaType;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.javaType;
    }
}
