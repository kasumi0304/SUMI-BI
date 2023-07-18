package com.kasumi.core.mq;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author kasumi
 * @Description: TODO
 */
@SpringBootTest
class MyMessageProducerTest {


@Resource
MyMessageProducer myMessageProducer;
    @Test
    void sendMessage() {
        myMessageProducer.sendMessage("code_exchange", "my_routingKey", "你好呀");
    }
}