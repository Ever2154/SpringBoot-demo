package com.boot.demo.service;

import com.boot.demo.config.RabbitmqConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * @author huanghao
 * date 2021-10-26
 */
@Service
public class Producer {
    @Resource
    AmqpTemplate amqpTemplate;

    public void sendMsg(){
        amqpTemplate.convertAndSend(RabbitmqConfig.DEMO_EXCHANGE,"demo.key","Hello mq!");
    }

    public void sendDeadLetterMsg(String msg){
        //向业务交换器发送消息
        amqpTemplate.convertAndSend(RabbitmqConfig.BUSINESS_EXCHANGE,"",msg);
    }
}
