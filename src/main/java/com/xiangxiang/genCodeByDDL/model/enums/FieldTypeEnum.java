package main.java.com.xiangxiang.genCodeByDDL.model.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 字段类型枚举
 *
 * @author cong
 * @date 2024/08/01
 */
public enum FieldTypeEnum {

    TINYINT("tinyint"),
    SMALLINT("smallint"),
    MEDIUMINT("mediumint"),
    INT("int"),
    BIGINT("bigint"),
    FLOAT("float"),
    DOUBLE("double"),
    DECIMAL("decimal"),
    DATE("date"),
    TIME("time"),
    YEAR("year"),
    DATETIME("datetime"),
    TIMESTAMP("timestamp"),
    CHAR("char"),
    VARCHAR("varchar"),
    TINYTEXT("tinytext"),
    TEXT("text"),
    MEDIUMTEXT("mediumtext"),
    LONGTEXT("longtext"),
    TINYBLOB("tinyblob"),
    BLOB("blob"),
    MEDIUMBLOB("mediumblob"),
    LONGBLOB("longblob"),
    BINARY("binary"),
    VARBINARY("varbinary");

    private final String value;

    FieldTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * 根据 SQL 类型字符串获取 FieldTypeEnum 枚举
     *
     * @param sqlType SQL 数据类型字符串
     * @return {@link FieldTypeEnum }
     */
    public static FieldTypeEnum getEnumBySqlType(String sqlType) {
        if (StringUtils.isBlank(sqlType)) {
            return null;
        }
        // 提取 SQL 类型的基本类型（例如 VARCHAR, INT）
        String basicType = sqlType.split("\\(")[0].trim().toLowerCase();
        return Arrays.stream(values())
                     .filter(enumType -> enumType.value.equals(basicType))
                     .findFirst()
                     .orElse(null);
    }
}
