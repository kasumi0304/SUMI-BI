import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class APIClient {
    public static void main(String[] args) {
        String jsonString = "{\"id\":\"as-0gifrmf8y4\",\"object\":\"chat.completion\",\"created\":1689610948,\"result\":\"【【【【【\\n{\\n    \\\"title\\\": \\\"用户增长情况\\\",\\n    \\\"tooltip\\\": {\\n        \\\"trigger\\\": \\\"axis\\\",\\n        \\\"axisPointer\\\": {\\n            \\\"type\\\": \\\"shadow\\\"\\n        }\\n    },\\n    \\\"legend\\\": {\\n        \\\"data\\\": [\\\"用户增长\\\"]\\n    },\\n    \\\"grid\\\": {\\n        \\\"left\\\": \\\"3%\\\",\\n        \\\"right\\\": \\\"4%\\\",\\n        \\\"bottom\\\": \\\"3%\\\",\\n        \\\"containLabel\\\": true\\n    },\\n    \\\"xAxis\\\": [\\n        {\\n            \\\"type\\\": \\\"category\\\",\\n            \\\"boundaryGap\\\": false,\\n            \\\"data\\\": [\\\"1号\\\",\\\"2号\\\",\\\"3号\\\",\\\"4号\\\"]\\n        }\\n    ],\\n    \\\"yAxis\\\": [\\n        {\\n            \\\"type\\\": \\\"value\\\"\\n        }\\n    ],\\n    \\\"series\\\": [\\n        {\\n            \\\"name\\\":\\\"用户增长\\\",\\n            \\\"type\\\":\\\"line\\\",\\n            \\\"stack\\\": \\\"总量\\\",\\n            \\\"areaStyle\\\": {},\\n            \\\"data\\\":[10,10,30,40]\\n        }   ]   \\n}   ]   \\n【【【【【\\n{   \\\"用户增长情况的分析结论：\\\\n\\\"\\\\n    \\\"1. 1号和2号用户增长趋势平稳，没有明显增长或下降趋势。\\\\n\\\"\\\\n    \\\"2. 3号和4号用户增长趋势明显，尤其是4号，用户增长量较3号大幅度提高。\\\\n\\\"\\\\n    \\\"3. 整体来看，用户增长情况呈现波动性，但总体的增长趋势不明显。\\\\n\\\"\\\\n    \\\"4. 为了提高用户增长，需要进一步分析用户增长的原因和动力，针对性地进行优化和改进。\\\"}\"}";

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);

        // 获取result字段的内容
        String result = jsonObject.get("result").getAsString();

        System.out.println(result);
    }
}
