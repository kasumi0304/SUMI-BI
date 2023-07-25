package com.kasumi.core.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
public class OpenAiApi {

    public static void main(String[] args) {

        String url = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions?access_token=24.ce15d1f32edf20a159e1ed4e742b0687.2592000.1692111911.282335-36244159";

        JSONObject payload = new JSONObject();
        JSONObject message = new JSONObject();
        message.put("role", "user");
        StringBuilder input = new StringBuilder();
        input.append("你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容：\\n\" +\n" +
                "//                \"分析需求：\\n\" +\n" +
                "//                \"{数据分析的需求或者目标}\\n\" +\n" +
                "//                \"原始数据：\\n\" +\n" +
                "//                \"{csv格式的原始数据，用,作为分隔符}\\n\" +\n" +
                "//                \"请根据这两部分内容，按照以下指定格式生成内容（此外不要输出任何多余的开头、结尾、注释）\\n\" +\n" +
                "//                \"【【【【【\\n\" +\n" +
                "//                \"{前端 Echarts V5 的 option 配置对象js代码，合理地将数据进行可视化，不要生成任何多余的内容，比如注释}\\n\" +\n" +
                "//                \"【【【【【\\n\" +\n" +
                "//                \"{明确的数据分析结论、越详细越好，不要生成多余的注释}\n");
        input.append("分析需求：\n" +
                "用户增长情况\n" +
                "原始数据：" +
                "日期，用户\n"+"1号，10\n"+"2号，10\n"+"3号，30\n"+"4号，40\n");
        message.put("content", input);
        payload.put("messages", new JSONObject[]{message});


        String result = HttpRequest.post(url)
                .contentType("application/json")
                .body(payload.toString())
                .execute()
                .body();

        System.out.println(result);
    }

    private static String escapeString(String input) {
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
