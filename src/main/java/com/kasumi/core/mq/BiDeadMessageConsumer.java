package com.kasumi.core.mq;

import com.kasumi.core.common.constant.ErrorCodeEnum;
import com.kasumi.core.common.exception.BusinessException;
import com.kasumi.core.constant.BiMqConstant;
import com.kasumi.core.constant.ChartConstant;
import com.kasumi.dao.entity.Chart;
import com.kasumi.service.ChartService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author kasumi
 * @Description: 处理死信队列消息
 */
@Component
@Slf4j
public class BiDeadMessageConsumer {

    @Resource
    private ChartService chartService;

    @RabbitListener(queues = {BiMqConstant.BI_DEAD_QUEUE_NAME}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        log.info("死信队列receive message = {}", message);
        if (StringUtils.isBlank(message)) {
            //如果失败，拒绝消息
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR, "消息为空");
        }
        long chartId = Long.parseLong(message);
        Chart chart = chartService.getById(chartId);
        if (chart == null) {
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCodeEnum.USER_REQUEST_PARAM_ERROR, "图表为空");
        }
        //标记图表生成失败
        chart.setStatus(ChartConstant.FAILED);
        boolean updateResult = chartService.updateById(chart);
        if (!updateResult){
            log.info("处理死信队列消息失败,失败图表id:{}", chart.getId());
            throw new BusinessException(ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);
        }

        //确认消息
        channel.basicAck(deliveryTag, false);
    }
}
