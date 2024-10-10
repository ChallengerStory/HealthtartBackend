package com.dev5ops.healthtart.inbody.service;

import net.minidev.json.JSONObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InbodyDataExtractor {

    // 각 line에서 데이터를 추출하여 JSON 형태로 반환
    public static String extractInbodyDataFromLines(String[] lines) {
        JSONObject inbodyData = new JSONObject();

        try {
            // 1. 신장: cm가 포함된 줄의 숫자를 추출
            inbodyData.put("height", extractHeight(lines));

            // 2. 검사일시: 날짜 형태의 값을 추출 (YYYY.MM.DD)
            inbodyData.put("dayOfInbody", extractDate(lines));

            // 3. 인바디 점수: 00/100 형태의 점수 추출
            inbodyData.put("inbodyScore", extractScore(lines));

            // 4. 체중, 골격근량, 체지방량: 소수점이 포함된 값을 추출
            inbodyData.put("weight", extractDoubleValueAfterKeyword(lines, "체중"));
            inbodyData.put("muscleWeight", extractDoubleValueAfterKeyword(lines, "골격근량"));
            inbodyData.put("fatWeight", extractDoubleValueAfterKeyword(lines, "체지방량"));

            // 5. BMI, 체지방률: 소수점이 포함된 값을 추출
            inbodyData.put("bmi", extractDoubleValueAfterKeyword(lines, "BMI"));
            inbodyData.put("fatPercentage", extractDoubleValueAfterKeyword(lines, "체지방률"));

            // 6. 기초대사량: kcal 뒤에 나오는 숫자를 추출
            inbodyData.put("basalMetabolicRate", extractBasalMetabolicRate(lines));

        } catch (Exception e) {
            System.err.println("Error extracting inbody data: " + e.getMessage());
        }

        return inbodyData.toString();
    }

    // 신장 추출
    private static String extractHeight(String[] lines) {
        for (String line : lines) {
            if (line.contains("cm")) {
                Matcher matcher = Pattern.compile("\\d+\\.\\d+|\\d+").matcher(line);
                if (matcher.find()) {
                    return matcher.group();
                }
            }
        }
        return "N/A";
    }

    // 검사일시 추출
    private static String extractDate(String[] lines) {
        for (String line : lines) {
            Matcher matcher = Pattern.compile("\\d{4}\\.\\d{2}\\.\\d{2}").matcher(line);
            if (matcher.find()) {
                return matcher.group();
            }
        }
        return "N/A";
    }

    // 인바디 점수 추출
    private static String extractScore(String[] lines) {
        for (String line : lines) {
            Matcher matcher = Pattern.compile("\\d+/100").matcher(line);
            if (matcher.find()) {
                return matcher.group();
            }
        }
        return "N/A";
    }

    // 특정 키워드 뒤의 값을 추출 (소수점 포함 숫자)
    private static String extractDoubleValueAfterKeyword(String[] lines, String keyword) {
        boolean foundKeyword = false;

        for (String line : lines) {
            if (foundKeyword) {
                // 키워드를 찾은 후 첫 번째 소수점 또는 숫자를 반환
                Matcher matcher = Pattern.compile("\\d+\\.\\d+|\\d+").matcher(line);
                if (matcher.find()) {
                    return matcher.group();
                }
            }

            // 키워드가 발견되면 그 이후부터 값을 찾음
            if (line.contains(keyword)) {
                foundKeyword = true;
            }
        }
        return "N/A";
    }

    // 기초대사량 추출
    private static String extractBasalMetabolicRate(String[] lines) {
        for (String line : lines) {
            if (line.contains("kcal")) {
                Matcher matcher = Pattern.compile("\\d+").matcher(line);
                if (matcher.find()) {
                    return matcher.group();
                }
            }
        }
        return "N/A";
    }
}
