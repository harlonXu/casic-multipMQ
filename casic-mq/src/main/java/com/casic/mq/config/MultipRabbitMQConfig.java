package com.casic.mq.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;


/**
 * mq config
 *
 * @author xxf
 */
@Configuration
public class MultipRabbitMQConfig {

    private final Logger logger = LoggerFactory.getLogger(MultipRabbitMQConfig.class);

    @Autowired
    private Environment environment;

    @Bean(name = "primaryCachingConnectionFactory")
    @Primary
    public CachingConnectionFactory primaryCachingConnectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setHost(environment.getProperty("spring.rabbitmq.primary.host"));
        cachingConnectionFactory.setPort(environment.getProperty("spring.rabbitmq.primary.port", int.class));
        cachingConnectionFactory.setUsername(environment.getProperty("spring.rabbitmq.primary.username"));
        cachingConnectionFactory.setPassword(environment.getProperty("spring.rabbitmq.primary.password"));
        cachingConnectionFactory.setPublisherConfirms(environment.getProperty("spring.rabbitmq.publisher-confirms", Boolean.class));
        cachingConnectionFactory.setPublisherReturns(environment.getProperty("spring.rabbitmq.publisher-returns", Boolean.class));
        return cachingConnectionFactory;
    }

    @Bean(name = "secondCachingConnectionFactory")
    @ConditionalOnProperty(prefix = "spring.rabbitmq.second", name = "enable", havingValue = "true")
    public CachingConnectionFactory secondCachingConnectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setHost(environment.getProperty("spring.rabbitmq.second.host"));
        cachingConnectionFactory.setPort(environment.getProperty("spring.rabbitmq.second.port", int.class));
        cachingConnectionFactory.setUsername(environment.getProperty("spring.rabbitmq.second.username"));
        cachingConnectionFactory.setPassword(environment.getProperty("spring.rabbitmq.second.password"));
        cachingConnectionFactory.setPublisherConfirms(environment.getProperty("spring.rabbitmq.publisher-confirms", Boolean.class));
        cachingConnectionFactory.setPublisherReturns(environment.getProperty("spring.rabbitmq.publisher-returns", Boolean.class));
        return cachingConnectionFactory;
    }

    @Bean(name = "primaryRabbitTemplate")
    @Primary
    public RabbitTemplate primaryRabbitTemplate(@Qualifier("primaryCachingConnectionFactory") CachingConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //设置开启mandatory,才能触发回调函数，无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                logger.info("primaryConfirmCallBack相关配置信息:{}", correlationData);
                logger.info("primaryConfirmCallBack交换机是否收到消息:{}", ack);
                logger.info("primaryConfirmCallBack失败原因:{}", cause);
                //消息到达exchange
            }
        });

        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                logger.info("primaryReturnCallback消息:{}", message);
                logger.info("primaryReturnCallback回应码:{}", replyCode);
                logger.info("primaryReturnCallback回应信息:{}", replyText);

                logger.info("primaryReturnCallback交换机:{}", exchange);
                logger.info("primaryReturnCallback路由键:{}", routingKey);
                //消息失败返回，如路由不到队列触发
            }
        });
        return rabbitTemplate;
    }

    @Bean(name = "secondRabbitTemplate")
    @ConditionalOnProperty(prefix = "spring.rabbitmq.second", name = "enable", havingValue = "true")
    public RabbitTemplate secondRabbitTemplate(@Qualifier("secondCachingConnectionFactory")
                                                       CachingConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //设置开启mandatory,才能触发回调函数，无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                logger.info("secondConfirmCallBack相关配置信息:{}", correlationData);
                logger.info("secondConfirmCallBack交换机是否收到消息:{}", ack);
                logger.info("secondConfirmCallBack失败原因:{}", cause);
                //消息到达exchange
            }
        });

        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                logger.info("secondReturnCallback消息:{}", message);
                logger.info("secondReturnCallback回应码:{}", replyCode);
                logger.info("secondReturnCallback回应信息:{}", replyText);
                logger.info("secondReturnCallback交换机:{}", exchange);
                logger.info("secondReturnCallback路由键:{}", routingKey);
                //消息失败返回，如路由不到队列触发
            }
        });
        return rabbitTemplate;
    }

    @Bean(name = "primaryListenerContainerFactory")
    @Primary
    public SimpleRabbitListenerContainerFactory primaryListenerContainerFactory(
            @Qualifier("primaryCachingConnectionFactory") CachingConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

    @Bean(name = "secondListenerContainerFactory")
    @ConditionalOnProperty(prefix = "spring.rabbitmq.second", name = "enable", havingValue = "true")
    public SimpleRabbitListenerContainerFactory secondListenerContainerFactory(
            @Qualifier("secondCachingConnectionFactory") CachingConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

}

