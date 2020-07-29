package com.test.util;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

/**
 * 解析文字工具类
 */
public class AnalyticText {
    private AnalyticText(){ }

    public static String analyticText(String jsonString){
        Map<String, Object> map = (Map<String, Object>) JSON.parse(jsonString);
        StringBuilder content = new StringBuilder();
        if (map.get("code").toString().equals("0")) {
            String data = map.get("data").toString();
            Map<String, Object> map1 = (Map<String, Object>) JSON.parse(data);
            String block = map1.get("block").toString();
            List<Map> blocks = JSON.parseArray(block, Map.class);
            Map map2 = blocks.get(0);
            String line = map2.get("line").toString();
            List<Map> lines = JSON.parseArray(line, Map.class);
            for (Map line1 : lines) {
                String word = line1.get("word").toString();
                List<Map> words = JSON.parseArray(word, Map.class);
                for (Map word1 : words) {
                    content.append(word1.get("content") + " ");
                }
                content.append("\r\n");
            }
        }
        return content.toString();
    }
}
