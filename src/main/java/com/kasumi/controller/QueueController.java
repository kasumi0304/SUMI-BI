package com.kasumi.controller;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.kasumi.core.config.ThreadPoolExecutorConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author kasumi
 * @Description: TODO
 */
@RestController
@RequestMapping("/queue")
@Slf4j
@Profile({"dev", "local"})
public class QueueController {

    @Resource
    ThreadPoolExecutor threadPoolExecutor;

    @GetMapping("/add")
    public void add(String name) {
        CompletableFuture.runAsync(() ->{


        System.out.println("任务执行中:" + name + "，执行人：" + Thread.currentThread().getName());
        try {
            Thread.sleep(70000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        }, threadPoolExecutor);
    }

    @GetMapping("/get")
    public String get(){
        HashMap<String, Object> map = new HashMap<>();
        int size = threadPoolExecutor.getQueue().size();
        map.put("队列长度", size);
        long taskCount = threadPoolExecutor.getTaskCount();
        map.put("任务总数", taskCount);
        long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
        map.put("任务完成数", completedTaskCount);
        int activeCount = threadPoolExecutor.getActiveCount();
        map.put("正在工作的线程数：", activeCount);
        return JSONUtil.toJsonStr(map);
    }
}
