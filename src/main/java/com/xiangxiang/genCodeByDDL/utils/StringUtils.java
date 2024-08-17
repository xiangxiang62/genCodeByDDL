package main.java.com.xiangxiang.genCodeByDDL.utils;

public class StringUtils {

//    public static String toCamelCase(String input) {
//        StringBuilder result = new StringBuilder();
//        boolean capitalizeNext = false;
//
//        for (char c : input.toCharArray()) {
//            if (c == '_') {
//                capitalizeNext = true;
//            } else if (capitalizeNext) {
//                result.append(Character.toUpperCase(c));
//                capitalizeNext = false;
//            } else {
//                result.append(Character.toLowerCase(c));
//            }
//        }
//
//        return result.toString();
//    }

    public static String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }


    /**
     * 将字符串转换为小驼峰命名法（camelCase）
     *
     * @param input 输入字符串
     * @return 转换后的字符串
     */
    public static String toCamelCase(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        StringBuilder camelCaseString = new StringBuilder();
        boolean capitalizeNext = false;

        for (char c : input.toCharArray()) {
            if (Character.isWhitespace(c) || c == '_' || c == '-') {
                // 当遇到分隔符时，下一字母需要大写
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    // 将下一个字母大写
                    camelCaseString.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    // 保持原样但确保小写
                    camelCaseString.append(Character.toLowerCase(c));
                }
            }
        }

        // 确保首字母小写
        if (camelCaseString.length() > 0) {
            camelCaseString.setCharAt(0, Character.toLowerCase(camelCaseString.charAt(0)));
        }

        return camelCaseString.toString();
    }


    /**
     * 将字符串转换为大驼峰命名法（PascalCase）
     *
     * @param input 输入字符串
     * @return 转换后的字符串
     */
    public static String toPascalCase(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        StringBuilder pascalCaseString = new StringBuilder();
        boolean capitalizeNext = true; // 首字母需要大写

        for (char c : input.toCharArray()) {
            if (Character.isWhitespace(c) || c == '_' || c == '-') {
                // 当遇到分隔符时，下一字母需要大写
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    // 将字母转换为大写
                    pascalCaseString.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    // 将字母转换为小写
                    pascalCaseString.append(Character.toLowerCase(c));
                }
            }
        }

        return pascalCaseString.toString();
    }

}
