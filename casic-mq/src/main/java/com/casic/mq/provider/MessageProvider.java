package com.casic.mq.provider;

import com.casic.mq.constant.MQConstants;
import com.casic.mq.enums.ExchangeEnum;
import com.casic.mq.enums.QueueEnum;
import com.casic.mq.service.ISendMessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;


/**
 * mq 发送者测试类
 * @author xxf
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageProvider {

   @Autowired
   RabbitTemplate rabbitTemplate;

   @Autowired
   @Qualifier("primaryRabbitTemplate")
   private RabbitTemplate primaryRabbitTemplate;

   @Autowired(required = false)
   @Qualifier("secondRabbitTemplate")
   private RabbitTemplate secondRabbitTemplate;

   @Autowired
   private ISendMessageService sendMessageService;

   /**
    *
    * @param syncData
    */
   public <T> void sendMessage(T syncData,String targetQueues) {
      MessageProperties messageProperties = new MessageProperties();
      MessageConverter messageConverter = new SimpleMessageConverter();
      Message message = messageConverter.toMessage(syncData, messageProperties);
      //设置消息过期时间
      message.getMessageProperties().setExpiration("180000");
      rabbitTemplate.send(ExchangeEnum.casicTopicExchange.getExchangeName(),
              QueueEnum.CASIC_QUEUE11.getQueueName(), message);
   }

   @Test
   public void sendSecondTopicMessage() {
      for (int i = 1; i <= 10; i++) {
         if (i % 2 == 0) {
            MessageProperties messageProperties = new MessageProperties();
            MessageConverter messageConverter = new SimpleMessageConverter();
            Message message = messageConverter.toMessage("syncData1", messageProperties);
            //设置消息过期时间
            message.getMessageProperties().setExpiration("180000");
            primaryRabbitTemplate.send(MQConstants.PRIMARY_BASIC_EXCHANGE,
                    MQConstants.PRIMARY_BASIC_QUEUE+"192.168.1.101",message);
         } else {
            MessageProperties messageProperties = new MessageProperties();
            MessageConverter messageConverter = new SimpleMessageConverter();
            Message message = messageConverter.toMessage("syncData2", messageProperties);
            //设置消息过期时间
            message.getMessageProperties().setExpiration("180000");
            if(!StringUtils.isEmpty(secondRabbitTemplate)){
               secondRabbitTemplate.send(MQConstants.SECOND_BASIC_EXCHANGE,
                       MQConstants.SECOND_BASIC_QUEUE+"192.168.1.101",message);
            }
         }
         //保存消息到数据库中，作持久化处理

      }
   }
}
