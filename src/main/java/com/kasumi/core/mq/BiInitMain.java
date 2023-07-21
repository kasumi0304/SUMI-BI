package com.kasumi.core.mq;

import com.kasumi.core.constant.BiMqConstant;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author kasumi
 * @description 用于创建程序用到的交换机和队列（只用在程序启动前执行一次）
 */
public class BiInitMain {

    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("43.136.59.100");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(BiMqConstant.BI_EXCHANGE_NAME, "direct");

            Map<String, Object> arg = new HashMap<>();
            arg.put("x-dead-letter-exchange", BiMqConstant.BI_DEAD_EXCHANGE_NAME);
            arg.put("x-dead-letter-routing-key", BiMqConstant.BI_DEAD_ROUTING_KEY);

            // 创建队列，随机分配一个队列名称
            String queueName = BiMqConstant.BI_QUEUE_NAME;
            channel.queueDeclare(queueName, true, false, false, arg);
            channel.queueBind(queueName, BiMqConstant.BI_EXCHANGE_NAME,  BiMqConstant.BI_ROUTING_KEY);

            //创建死信交换机
            channel.exchangeDeclare(BiMqConstant.BI_DEAD_EXCHANGE_NAME, "direct");
            channel.queueDeclare(BiMqConstant.BI_DEAD_QUEUE_NAME, true, false, false, null);
            channel.queueBind(BiMqConstant.BI_DEAD_QUEUE_NAME, BiMqConstant.BI_DEAD_EXCHANGE_NAME,BiMqConstant.BI_DEAD_ROUTING_KEY);
        } catch (Exception e) {

        }

    }
}
