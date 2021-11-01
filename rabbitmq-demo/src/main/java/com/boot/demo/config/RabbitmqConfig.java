package com.boot.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huanghao
 * date 2021-10-26
 */
@Configuration
public class RabbitmqConfig {
    public final static String DEMO_EXCHANGE = "demo-exchange";
    public final static String DEMO_QUEUE = "demo-queue";
    /**
     * *(star) can substitute for exactly one word.
     * #(hash) can substitute for zero or more words.
     */
    public final static String BINDING_DEMO_KEY = "demo.key.#";

    /**
     * 死信demo
     */
    public final static String DEAD_LETTER_EXCHANGE = "dead-letter-exchange";
    public final static String DEAD_LETTER_QUEUE = "dead-letter-queue";
    public static final String DEAD_LETTER_ROUTING_KEY =  "dead.letter.key";

    public final static String BUSINESS_EXCHANGE = "business-exchange";
    public final static String BUSINESS_QUEUE = "business-queue";
    public final static String BUSINESS_BINDING_KEY = "business.key.#";

    /**
     * 延时队列demo
     */
    public final static String DELAY_QUEUE = "delay-queue";
    public final static String DELAY_EXCHANGE = "delay-exchange";
    public final static String DELAY_EXCHANGE_QUEUE_BINDING_KEY = "delay.demo";

    public final static String DELAY_QUEUE2 = "delay-queue2";
    public final static String DELAY_EXCHANGE_QUEUE_BINDING_KEY2 = "delay.demo2";

    public final static String DELAY_DEAD_LETTER_EXCHANGE = "delay-dead-letter-exchange";
    public final static String DELAY_DEAD_LETTER_QUEUE = "delay-dead-letter-queue";
    public final static String DELAY_DEAD_LETTER_ROUTING_KEY = "delay.dead.#";
    //---------------------------------------延时队列demo(设置发送消息过期时间)

    /**
     * 创建一个队列
     * @author huanghao
     * @date 2021/10/26 18:46
     */
    @Bean
    public Queue delayQueue2() {
        Map<String,Object> map=new HashMap<>(16);
        map.put("x-dead-letter-exchange",DELAY_DEAD_LETTER_EXCHANGE);
        map.put("x-dead-letter-routing-key",DELAY_DEAD_LETTER_ROUTING_KEY);
        return QueueBuilder.durable(DELAY_QUEUE2).withArguments(map).build();
    }

    /**
     * 创建队列到延时交换器的绑定
     * @author huanghao
     * @date 2021/10/26 18:46
     */
    @Bean
    public Binding delayQueueBinding2(Queue delayQueue2,DirectExchange delayExchange) {
        return BindingBuilder.bind(delayQueue2).to(delayExchange).with(DELAY_EXCHANGE_QUEUE_BINDING_KEY2);
    }

//---------------------------------------延时队列demo(设置队列消息过期时间)
    /**
     * 创建一个 direct类型的延时交换器
     * @author huanghao
     * @date 2021/10/26 18:46
     */
    @Bean
    public DirectExchange delayExchange() {
        return new DirectExchange(DELAY_EXCHANGE);
    }

    /**
     * 创建一个延时队列
     * @author huanghao
     * @date 2021/10/26 18:46
     */
//    @Bean
    public Queue delayQueue() {
        Map<String,Object> map=new HashMap<>(16);
        map.put("x-dead-letter-exchange",DELAY_DEAD_LETTER_EXCHANGE);
        map.put("x-dead-letter-routing-key",DELAY_DEAD_LETTER_ROUTING_KEY);
        map.put("x-message-ttl",6000);
        return QueueBuilder.durable(DELAY_QUEUE).withArguments(map).build();
    }

    /**
     * 创建一个 延时队列到延时交换器的绑定
     * @author huanghao
     * @date 2021/10/26 18:46
     */
//    @Bean
    public Binding delayQueueBinding(Queue delayQueue,DirectExchange delayExchange) {
        return BindingBuilder.bind(delayQueue).to(delayExchange).with(DELAY_EXCHANGE_QUEUE_BINDING_KEY);
    }

    /**
     * 创建一个 延时死信队列
     * @author huanghao
     * @date 2021/10/26 18:46
     */
    @Bean
    public Queue delayDeadLetterQueue() {
        return QueueBuilder.durable(DELAY_DEAD_LETTER_QUEUE).build();
    }

    /**
     * 创建一个 延时死信交换器
     * @author huanghao
     * @date 2021/10/26 18:46
     */
    @Bean
    public TopicExchange delayDeadLetterExchange() {
        return new TopicExchange(DELAY_DEAD_LETTER_EXCHANGE);
    }

    /**
     * 创建一个 延时死信队列到延时死信交换器的绑定
     * @author huanghao
     * @date 2021/10/26 18:46
     */
    @Bean
    public Binding delayDeadLetterExchangeBinding(Queue delayDeadLetterQueue,TopicExchange delayDeadLetterExchange) {
        return BindingBuilder.bind(delayDeadLetterQueue).to(delayDeadLetterExchange).with(DELAY_DEAD_LETTER_ROUTING_KEY);
    }

//---------------------------------------死信demo
    /**
     * 创建一个 fanout类型的业务交换器
     * @author huanghao
     * @date 2021/10/26 18:46
     * @return org.springframework.amqp.core.FanoutExchange
     */
    @Bean
    public FanoutExchange businessExchange() {
        return new FanoutExchange(BUSINESS_EXCHANGE);
    }

    /**
     * 创建一个业务队列 并添加死信队列的参数
     * @author huanghao
     * @date 2021/10/26 18:46
     * @return org.springframework.amqp.core.Queue
     */
    @Bean
    public Queue businessQueue() {
        Map<String,Object> args=new HashMap<>();
        args.put("x-dead-letter-exchange",DEAD_LETTER_EXCHANGE);
        args.put("x-dead-letter-routing-key",DEAD_LETTER_ROUTING_KEY);
        return QueueBuilder.durable(BUSINESS_QUEUE).withArguments(args).build();
    }

    /**
     * 创建一个业务队列到业务交换器的绑定
     * @author huanghao
     * @date 2021/10/26 18:46
     * @return org.springframework.amqp.core.Binding
     */
    @Bean
    public Binding businessBinding(Queue businessQueue,FanoutExchange businessExchange) {
        return BindingBuilder.bind(businessQueue).to(businessExchange);
    }

    /**
     * 创建一个 direct类型的死信交换器
     * @author huanghao
     * @date 2021/10/26 18:46
     * @return org.springframework.amqp.core.DirectExchange
     */
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

    /**
     * 创建一个死信队列
     * @author huanghao
     * @date 2021/10/26 18:46
     * @return org.springframework.amqp.core.Queue
     */
    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DEAD_LETTER_QUEUE);
    }

    /**
     * 创建一个死信队列到死信交换器的绑定
     * @author huanghao
     * @date 2021/10/26 18:46
     * @return org.springframework.amqp.core.Binding
     */
    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue,DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with(DEAD_LETTER_ROUTING_KEY);
    }

//---------------------------------------普通demo

    /**
     * 创建一个 topic 类型的交换器
     * @author huanghao
     * @date 2021/10/26 18:46
     * @return org.springframework.amqp.core.TopicExchange
     */
    @Bean
    public TopicExchange demoExchange() {
        return new TopicExchange(DEMO_EXCHANGE);
    }

    /**
     * 创建队列
     * @author huanghao
     * @date 2021/10/26 18:46
     * @return org.springframework.amqp.core.Queue
     */
    @Bean
    public Queue demoQueue() {
        return new Queue(DEMO_QUEUE);
    }



    /**
     * 使用路由键（routingKey）把队列（Queue）绑定到交换器（Exchange）
     * @author huanghao
     * @date 2021/10/26 18:46
     * @param demoQueue 队列
     * @param demoExchange 交换器
     * @return org.springframework.amqp.core.Binding
     */
    @Bean
    public Binding demoBinding(Queue demoQueue, TopicExchange demoExchange) {
        return BindingBuilder.bind(demoQueue).to(demoExchange).with(BINDING_DEMO_KEY);
    }
}
