package com.kasumi.core.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

public class test {
    public static void main(String[] args) {
        String url = "https://openkey.cloud/v1/chat/completions";

        // 构建请求数据
        JSONArray messages = new JSONArray();
        JSONObject userMessageJson = new JSONObject();
        userMessageJson.put("role", "user");
        userMessageJson.put("content", """
                你是gpt几
                """);
        messages.add(userMessageJson);

        JSONObject json = new JSONObject();
        json.put("model", "gpt-3.5-turbo");
        json.put("messages", messages);

        String result = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer sk-3IZNz4cOAFfYiJYCCiK4H3xqlqGJ5IhM4b0AhKilnG2T4p3F")
                .body(json.toString())
                .execute()
                .body();


        System.out.println(result);
    }
}
