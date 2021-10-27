package com.boot.demo.config;

import org.springframework.amqp.core.*;
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
