package com.yiyan.careeryiyan.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MapUtil {
    //下划线后的第一个字符只能是26个小写字母之一，禁止使用数字
    private static final Pattern UNDERSCORE_PATTERN = Pattern.compile("_([a-z])");
    public static Map<String, Object> convertKeysToCamelCase(Map<String, Object> originalMap) {
        Map<String, Object> convertedMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : originalMap.entrySet()) {
            String camelCaseKey = underscoreToCamelCase(entry.getKey());
            convertedMap.put(camelCaseKey, entry.getValue());
        }
        return convertedMap;
    }
    public static List<Map<String, Object>> convertKeysToCamelCase(List<Map<String, Object>> originalMapList) {
        List<Map<String, Object>> convertedMapList = new ArrayList<>();
        for(Map<String, Object> originalMap : originalMapList) {
            convertedMapList.add(convertKeysToCamelCase(originalMap));
        }
        return convertedMapList;
    }

    private static String underscoreToCamelCase(String underscoreKey) {
        Matcher matcher = UNDERSCORE_PATTERN.matcher(underscoreKey);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
