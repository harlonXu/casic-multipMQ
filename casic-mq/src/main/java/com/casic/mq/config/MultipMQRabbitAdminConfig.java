package com.casic.mq.config;

import com.casic.mq.properties.PrimaryMQProperties;
import com.casic.mq.utils.RabbitMQUtil;
import com.casic.mq.properties.SecondMQProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


/**
 * rabbitmq消息队列、交换机创建
 *
 * @author xxf
 */
@Configuration
public class MultipMQRabbitAdminConfig {

    @Bean(name = "createPriamryRabbitAdmin")
    @Primary
    public RabbitAdmin createPriamryRabbitAdmin(
            @Qualifier("primaryCachingConnectionFactory") CachingConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        TopicExchange topicExchange = RabbitMQUtil.createExchange(PrimaryMQProperties.getExchange().getExchangeName(),
                PrimaryMQProperties.getExchange().getDurable(), PrimaryMQProperties.getExchange().getAutoDelete());

        PrimaryMQProperties.getQueues().forEach(queueEnum -> {
            Queue queue = RabbitMQUtil.createQueue(queueEnum.getQueueName(), queueEnum.getDurable(),
                    queueEnum.getExclusive(), queueEnum.getAutoDelete());

            Binding binding = RabbitMQUtil.createBinding(queue, topicExchange, queueEnum.getRoutingKey());
            RabbitMQUtil.createRabbitAdmin(queue, topicExchange, binding, rabbitAdmin);
        });
        return rabbitAdmin;
    }

    @Bean(name = "createSecondQueueAndExchange")
    @ConditionalOnProperty(prefix = "spring.rabbitmq.second", name = "enable", havingValue = "true")
    public Object createSecondQueueAndExchange(
            @Qualifier("secondCachingConnectionFactory") CachingConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        TopicExchange topicExchange = RabbitMQUtil.createExchange(SecondMQProperties.getExchange().getExchangeName(),
                SecondMQProperties.getExchange().getDurable(), SecondMQProperties.getExchange().getAutoDelete());

        SecondMQProperties.getQueues().forEach(queueEnum -> {
            Queue queue = RabbitMQUtil.createQueue(queueEnum.getQueueName(), queueEnum.getDurable(),
                    queueEnum.getExclusive(), queueEnum.getAutoDelete());

            Binding binding = RabbitMQUtil.createBinding(queue, topicExchange, queueEnum.getRoutingKey());
            RabbitMQUtil.createRabbitAdmin(queue, topicExchange, binding, rabbitAdmin);
        });
        return rabbitAdmin;
    }
}
