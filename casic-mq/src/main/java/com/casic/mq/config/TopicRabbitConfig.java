package com.casic.mq.config;

import com.casic.mq.enums.ExchangeEnum;
import com.casic.mq.enums.QueueEnum;
import com.casic.mq.utils.SpringBeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * mq config
 * @author xxf
 */
//@Configuration
public class TopicRabbitConfig {

   private final Logger logger = LoggerFactory.getLogger(TopicRabbitConfig.class);

   @Bean
   public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
      SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
      factory.setConnectionFactory(connectionFactory);
//      factory.setMessageConverter(messageConverter());
      factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
      return factory;
   }

//   @Bean
//   public MessageConverter messageConverter() {
//      return new Jackson2JsonMessageConverter();
//   }

   @Bean
   public Object createExchange() {
      TopicExchange topicExchange = new TopicExchange(ExchangeEnum.casicTopicExchange.getExchangeName(),
         ExchangeEnum.casicTopicExchange.isDurable(), ExchangeEnum.casicTopicExchange.isAutoDelete());
      if (topicExchange != null) {
         SpringBeanUtils.registerBean(ExchangeEnum.casicTopicExchange.getExchangeName(), topicExchange);
      }
      return null;
   }

   @Bean
   public Object createQueue() {
      QueueEnum.toList().forEach(queueEnum -> {
         Queue queue = new Queue(queueEnum.getQueueName(), queueEnum.isDurable(),
            queueEnum.isExclusive(), queueEnum.isAutoDelete());
         if (queue != null) {
            SpringBeanUtils.registerBean(queueEnum.getQueueName(), queue);
         }

         Binding binding = BindingBuilder.bind(queue)
            .to(SpringBeanUtils.getBean(ExchangeEnum.casicTopicExchange.getExchangeName(), TopicExchange.class))
            .with(queueEnum.getRoutingKey());
         if (binding != null) {
            SpringBeanUtils.registerBean("bindingKey"+queueEnum.getRoutingKey(), binding);
         }
      });
      return null;
   }

   @Bean
   public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory) {
      RabbitTemplate rabbitTemplate = new RabbitTemplate();
      rabbitTemplate.setConnectionFactory(connectionFactory);

      //设置开启mandatory,才能触发回调函数，无论消息推送结果怎么样都强制调用回调函数
      rabbitTemplate.setMandatory(true);

      rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
         @Override
         public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            logger.info("ConfirmCallBack相关配置信息:{}", correlationData);
            logger.info("ConfirmCallBack交换机是否收到消息:{}", ack);
            logger.info("ConfirmCallBack失败原因:{}", cause);
            //消息到达exchange
         }
      });

      rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
         @Override
         public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
            logger.info("ReturnCallback消息:{}", message);
            logger.info("ReturnCallback回应码:{}", replyCode);
            logger.info("ReturnCallback回应信息:{}", replyText);
            logger.info("ReturnCallback交换机:{}", exchange);
            logger.info("ReturnCallback路由键:{}", routingKey);
            //消息失败返回，如路由不到队列触发
         }
      });
      return rabbitTemplate;
   }


   /**
    * 暂未使用
    *
    * @return
    */
//   //TTL
//   @Bean
//   TopicExchange topicTTLExchange() {
//      return (TopicExchange) ExchangeBuilder
//         .topicExchange(QueueEnum.MESSAGE_TTL_QUEUE.getExchange()).durable(true).build();
//   }
//
//   //TTL
//   @Bean
//   public Binding messageTtlBinding(Queue messageTtlQueue, TopicExchange topicTTLExchange) {
//      return BindingBuilder
//         .bind(messageTtlQueue)
//         .to(topicTTLExchange)
//         .with(QueueEnum.MESSAGE_TTL_QUEUE.getRouteKey());
//   }
//
//   //TTL
//   @Bean
//   Queue messageTtlQueue() {
//      return QueueBuilder
//         .durable(QueueEnum.MESSAGE_TTL_QUEUE.getName())
//         // 配置到期后转发的交换
//         .withArgument("x-dead-letter-exchange", QueueEnum.MESSAGE_QUEUE.getExchange())
//         // 配置到期后转发的路由键
//         .withArgument("x-dead-letter-routing-key", QueueEnum.MESSAGE_QUEUE.getRouteKey())
//         .build();
//   }

//   @Bean
//   public Queue firstQueue() {
//      return new Queue(QueueContent.FIRST_TOPIC_KEY, true, false, false);
//   }
//
//   //绑定firstQueue和topicExchange,且绑定键值为topic.firstKey
//   //只有消息携带的路由键是topic.firstKey,才会分发到该队列
//   @Bean
//   Binding firstTopicBinding() {
//      return BindingBuilder.bind(firstQueue()).to(zbChangeExchange()).with(QueueContent.FIRST_TOPIC_KEY);
//   }
}

