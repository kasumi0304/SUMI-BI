package com.kasumi.core.api;


import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Service;

@Service
public class OpenAiApi {
    public String doChat(String userInput) {
        String url = "https://api.openai-proxy.com/v1/chat/completions";

        // 构建请求数据
        JSONArray messages = new JSONArray();
        JSONObject userMessageJson = new JSONObject();
        userMessageJson.put("role", "user");
        userMessageJson.put("content", """
                你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容
                分析需求：
                {数据分析的需求或者目标}
                原始数据：
                {csv格式的原始数据，用,作为分隔符}
                请根据这两部分内容，按照以下指定格式生成内容（此外不要输出任何多余的开头、结尾、注释）
                【【【【【
                {前端 Echarts V5 的 option 配置对象js代码，合理地将数据进行可视化，不要生成任何多余的内容，比如注释}
                【【【【【
                {明确的数据分析结论、越详细越好，不要生成多余的注释}
                %s
                请根据上面的需求和原始数据，严格按照以下指定格式生成内容（此外不要输出任何多余的开头、结尾、注释）
                【【【【【
                [前端 Echarts V5 的 option 配置对象的JSON格式的数据，合理地将数据进行可视化，不要生成任何多余的内容，比如注释]
                【【【【【
                [明确的数据分析结论、越详细越好，不要生成多余的注释]
                """.formatted(userInput));
        messages.add(userMessageJson);

        JSONObject json = new JSONObject();
        json.put("model", "gpt-3.5-turbo");
        json.put("messages", messages);

        String result = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer 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")
                .body(json.toString())
                .execute()
                .body();

        JSONObject jsonObject = JSONUtil.parseObj(result);
        JSONObject messageObject = jsonObject.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message");

        String content = messageObject.getStr("content");


        return content;

    }
}
