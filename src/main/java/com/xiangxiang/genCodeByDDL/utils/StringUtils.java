package main.java.com.xiangxiang.genCodeByDDL.utils;

public class StringUtils {

    public static String toCamelCase(String input) {
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;

        for (char c : input.toCharArray()) {
            if (c == '_') {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                result.append(Character.toLowerCase(c));
            }
        }

        return result.toString();
    }

    public static String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }

    public static void main(String[] args) {
        String tableName = "my_table_name";
        String camelCaseTableName = toCamelCase(tableName);
        String upperCamelTableName = capitalize(camelCaseTableName);

        System.out.println("Original: " + tableName);
        System.out.println("CamelCase: " + camelCaseTableName);
        System.out.println("UpperCamelCase: " + upperCamelTableName);
    }
}
