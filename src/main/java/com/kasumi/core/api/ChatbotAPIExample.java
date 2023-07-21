package com.kasumi.core.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

public class ChatbotAPIExample {
    public static void main(String[] args) {
        String url = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions?access_token=24.ce15d1f32edf20a159e1ed4e742b0687.2592000.1692111911.282335-36244159";

        // 构建请求体
        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "user").put("content", "你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容\n" +
                "分析需求：\n" +
                "{数据分析的需求或者目标}\n" +
                "原始数据：\n" +
                "{csv格式的原始数据，用,作为分隔符}\n" +
                "请根据这两部分内容，按照以下指定格式生成内容（此外不要输出任何多余的开头、结尾、注释）\n" +
                "【【【【【\n" +
                "{前端 Echarts V5 的 option 配置对象js代码，合理地将数据进行可视化，不要生成任何多余的内容，比如注释}\n" +
                "【【【【【\n" +
                "{明确的数据分析结论、越详细越好，不要生成多余的注释}\n" +
                "分析需求:\n" +
                "用户数量变化,堆叠图" + "\n" +
                "原始数据:\n" +
                "日期,用户数" + "\n" +
                "1,10\n" +
                "2,20\n" +
                "3,30\n" +
                "4,45\n" +
                "5,78\n" +
                "6,98\n" +
                "7,54\n" +
                "8,43\n" +
                "9,55\n" +
                "10,76\n" +
                "11,55\n" +
                "12,5\n" +
                "13,55\n" +
                "14,76\n" +
                "15,55\n" +
                "16,546\n" +
                "17,98\n" +
                "18,18\n" +
                "19,78\n" +
                "请根据上面的需求和原始数据，严格按照以下指定格式生成内容（此外不要输出任何多余的开头、结尾、注释）\n" +
                "【【【【【\n" +
                "[前端 Echarts V5 的 option 配置对象js代码，合理地将数据进行可视化，不要生成任何多余的内容，比如注释]\n" +
                "【【【【【\n" +
                "[明确的数据分析结论、越详细越好，不要生成多余的注释]\n"));

        JSONObject requestBody = new JSONObject().put("messages", messages);
        

        // 发送POST请求
        HttpResponse response = HttpRequest.post(url)
                .body(requestBody.toString())
                .header("Content-Type", "application/json")
                .execute();

        // 获取响应内容
        String responseBody = response.body();

        // 打印响应内容
        System.out.println(responseBody);
    }
}
