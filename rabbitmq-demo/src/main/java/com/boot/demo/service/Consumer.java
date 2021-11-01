package com.boot.demo.service;

import com.boot.demo.config.RabbitmqConfig;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.impl.AMQImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

/**
 * @author huanghao
 * date 2021-10-26
 */
@Service
@Slf4j
public class Consumer {
    @RabbitListener(queues = {RabbitmqConfig.DEMO_QUEUE})
    public void receiveMsg(Message msg, Channel channel) throws IOException {
        System.out.println("receive:"+new String(msg.getBody()));
        channel.basicAck(msg.getMessageProperties().getDeliveryTag(),false);
    }

    /**
     * 监听业务消息队列
     */
    @RabbitListener(queues = {RabbitmqConfig.BUSINESS_QUEUE})
    public void receiveBusinessMsg(Message msg, Channel channel){
        String msgStr=new String(msg.getBody());
        boolean success=true;
        try {
            if(msgStr.contains("Dead")){
                throw new RuntimeException("接收到死信消息");
            }
        } catch (RuntimeException e) {
            success=false;
            System.out.println(e.getMessage());
        }
        try {
            if(success){
                System.out.println("业务消息队列收到消息:"+msgStr);
                channel.basicAck(msg.getMessageProperties().getDeliveryTag(),false);
            }else{
                System.out.println("业务消息队列出现异常");
                channel.basicNack(msg.getMessageProperties().getDeliveryTag(),false,false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 监听死信消息队列 Message msg, Channel channel
     */
    @RabbitListener(queues = {RabbitmqConfig.DEAD_LETTER_QUEUE})
    public void receiveDeadLetterMsg(Message msg, Channel channel) throws IOException {
        System.out.println("死信消息:"+new String(msg.getBody()));
        channel.basicAck(msg.getMessageProperties().getDeliveryTag(),false);
    }


    /**
     * 监听延时死信消息队列
     */
    @RabbitListener(queues = {RabbitmqConfig.DELAY_DEAD_LETTER_QUEUE})
    public void receiveDelayDeadLetterMsg(Message msg, Channel channel) throws IOException {
        log.info("收到死信消息...{}",new String(msg.getBody()));
        channel.basicAck(msg.getMessageProperties().getDeliveryTag(),false);
    }

    /**
     * 监听使用delay插件的消息队列
     */
    @RabbitListener(queues = {RabbitmqConfig.DELAY_PLUGIN_QUEUE})
    public void receiveDelayPluginMsg(Message msg, Channel channel) throws IOException {
        log.info("收到延时消息...{}",new String(msg.getBody()));
        channel.basicAck(msg.getMessageProperties().getDeliveryTag(),false);
    }
}
