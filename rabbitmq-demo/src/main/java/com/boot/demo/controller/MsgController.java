package com.boot.demo.controller;

import com.boot.demo.config.RabbitmqConfig;
import com.boot.demo.service.Producer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author huanghao
 * date 2021-10-27
 */
@RestController
public class MsgController {
    @Resource
    Producer producer;
    @GetMapping("/testDeadLetter")
    public void testDeadLetter(String msg){
        producer.sendBusinessMsg(msg);
    }

    @GetMapping("/send")
    public void sendMsg(){
        producer.sendMsg();
    }

    /**
     * 队列设置延时
     */
    @GetMapping("/testDelayDeadLetter")
    public void testDelayDeadLetter(String msg){
        producer.sendDelayMsg(msg);
    }
    /**
     * 消息设置延时
     */
    @GetMapping("/testDelayDeadLetter2")
    public void testDelayDeadLetter(String msg,String delayTime){
        producer.sendDelayMsg2(msg,delayTime);
    }

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
