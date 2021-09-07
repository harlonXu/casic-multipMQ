package com.casic.mq.receive;

import com.alibaba.fastjson.JSONArray;
import com.casic.mq.utils.SpringBeanUtils;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * mq 消费者测试类
 * @author xxf
 */
@Component
public class MessageReceiver implements ChannelAwareMessageListener {

//   @Autowired
//   private IpAddrAndQueueConfig ipAddrConfig;

   private List<?> list;

   public void classToList(Class<?> clazz) {
      List<Class<?>> list = new ArrayList<Class<?>>();
      this.list = list;
   }

   @Autowired
   RabbitTemplate rabbitTemplate;

   @RabbitHandler
//   @RabbitListener(queues = "${spring.rabbitmq.second.queue}",containerFactory = "secondListenerContainerFactory")
   @Override
   public void onMessage(Message message, Channel channel) throws Exception {
      String appId = message.getMessageProperties().getAppId();
      long tag = message.getMessageProperties().getDeliveryTag();
      String type = message.getMessageProperties().getType();
      String[] types = type.split(";");
      if (types.length == 3) {
         try {
            //核对标识，决定是否消费消息
//            if (!ipAddrConfig.getQueue().equals(appId)) {
//               channel.basicReject(tag, true);
//               return;
//            }

            //获取消息
            MessageConverter messageConverter = new SimpleMessageConverter();
            classToList(Class.forName(types[2]));
            list = JSONArray.parseArray((String) messageConverter.fromMessage(message),
                    Class.forName(types[2]));

//            ApplicationContext applicationContext = SpringBootBeanUtil.getApplicationContext();
            BeanFactory beanFactory = SpringBeanUtils.getBeanFactory();
            Class<?> serviceImplType = Class.forName(types[0]);
            Class<?> entityType = Class.forName(types[2]);

            Method method = serviceImplType.getDeclaredMethod(types[1], entityType);

            if (list.size() > 0) {
               if (list.get(0) instanceof String) {
//                  method.invoke(applicationContext.getBean(serviceImplType), list.toArray(new String[list.size()]));
                  method.invoke(beanFactory.getBean(serviceImplType),list.toArray(new String[list.size()]));
               }else if(list.get(0) instanceof Long){
//                  method.invoke(applicationContext.getBean(serviceImplType), list.toArray(new Long[list.size()]));
                  method.invoke(beanFactory.getBean(serviceImplType),list.toArray(new Long[list.size()]));
               }else{
                  for (int i = 0; i < list.size(); i++) {
//                     method.invoke(applicationContext.getBean(serviceImplType), list.get(i));
                     method.invoke(beanFactory.getBean(serviceImplType), list.get(i));
                  }
               }
            }
            channel.basicAck(tag, false);

         } catch (Exception e) {
            e.printStackTrace();
            channel.basicNack(tag, false, true);
         }
      }
   }


}
