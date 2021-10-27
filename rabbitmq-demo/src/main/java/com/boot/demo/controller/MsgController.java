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
        producer.sendDeadLetterMsg(msg);
    }

    @GetMapping("/send")
    public void sendMsg(){
        producer.sendMsg();
    }
}
