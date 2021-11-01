package com.boot.demo.service;

import com.boot.demo.config.RabbitmqConfig;
import com.rabbitmq.client.AMQP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * @author huanghao
 * date 2021-10-26
 */
@Service
@Slf4j
public class Producer {
    @Resource
    AmqpTemplate amqpTemplate;

    public void sendMsg(){
        amqpTemplate.convertAndSend(RabbitmqConfig.DEMO_EXCHANGE,"demo.key","Hello mq!");
    }

    public void sendBusinessMsg(String msg){
        //向业务交换器发送消息
        amqpTemplate.convertAndSend(RabbitmqConfig.BUSINESS_EXCHANGE,"",msg);
    }

    public void sendDelayMsg(String msg){
        //向延时交换器发送消息
        log.info("发送消息...");
        amqpTemplate.convertAndSend(RabbitmqConfig.DELAY_EXCHANGE,RabbitmqConfig.DELAY_EXCHANGE_QUEUE_BINDING_KEY,msg);
    }

    public void sendDelayMsg2(String msg,String delayTime){
        //向延时交换器发送消息
        log.info("发送消息...");
        MessageProperties messageProperties=new MessageProperties();
        messageProperties.setExpiration(delayTime);
        Message message=new Message(msg.getBytes(), messageProperties);
        amqpTemplate.convertAndSend(RabbitmqConfig.DELAY_EXCHANGE,RabbitmqConfig.DELAY_EXCHANGE_QUEUE_BINDING_KEY2,message);

    }
}
