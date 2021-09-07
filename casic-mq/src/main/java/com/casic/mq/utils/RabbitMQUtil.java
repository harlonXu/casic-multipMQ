package com.casic.mq.utils;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

/**
 * rabbitmq消息队列、交换机创建绑定工具
 *
 * @author xxf
 */
public class RabbitMQUtil {

    /**
     * 声明topic交换机
     *
     * @param exchangeName
     * @param durable
     * @param autoDelete
     * @return
     */
    public static TopicExchange createExchange(String exchangeName, Boolean durable, Boolean autoDelete) {
        return new TopicExchange(exchangeName, durable, autoDelete);
    }

    /**
     * 声明队列
     *
     * @param queueName
     * @param durable
     * @param exclusive
     * @param autoDelete
     * @return
     */
    public static Queue createQueue(String queueName, Boolean durable, Boolean exclusive, Boolean autoDelete) {
        return new Queue(queueName, durable, exclusive, autoDelete);
    }

    /**
     * 声明绑定
     * @param queue
     * @param topicExchange
     * @param routingKey
     * @return
     */
    public static Binding createBinding(Queue queue, TopicExchange topicExchange, String routingKey) {
        return BindingBuilder.bind(queue).to(topicExchange).with(routingKey);
    }

    /**
     * RabbitAdmin创建队列、交换机，绑定交换机和队列
     * @param queue
     * @param topicExchange
     * @param binding
     * @param rabbitAdmin
     */
    public static void createRabbitAdmin(Queue queue, TopicExchange topicExchange, Binding binding, RabbitAdmin rabbitAdmin){
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareExchange(topicExchange);
        rabbitAdmin.declareBinding(binding);
    }
}
