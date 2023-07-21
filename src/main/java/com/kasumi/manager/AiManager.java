package com.kasumi.manager;

import com.kasumi.core.common.constant.ErrorCodeEnum;
import com.kasumi.core.common.exception.BusinessException;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author kasumi
 * @Description: Ai管理
 */

@Service
public class AiManager {

    @Resource
    YuCongMingClient yuCongMingClient;

    /**
     * AI 对话
     * @param modeId
     * @param message
     * @return
     */
    public String doChat(long modeId, String message){
        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(modeId);
        devChatRequest.setMessage(message);
        BaseResponse<DevChatResponse> response = yuCongMingClient.doChat(devChatRequest);
        if (response == null) {
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR);
        }
        return response.getData().getContent();
    }
}
