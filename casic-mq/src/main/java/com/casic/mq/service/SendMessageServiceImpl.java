package com.casic.mq.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.casic.mq.constant.MQConstants;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SendMessageServiceImpl implements ISendMessageService {

    @Autowired
    @Qualifier("primaryRabbitTemplate")
    private RabbitTemplate primaryRabbitTemplate;

    @Autowired(required = false)
    @Qualifier("secondRabbitTemplate")
    private RabbitTemplate secondRabbitTemplate;

    @Override
    public <T> void sendMessage(List<T> list, String queueName,
                                String domainName, String serviceName, String methodName) {
        if (!StringUtils.isEmpty(queueName)) {
            String type = serviceName + ";" + methodName + ";" + domainName;
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setAppId(queueName);
            MessageConverter messageConverter = new SimpleMessageConverter();
            Message message = messageConverter.toMessage(JSON.toJSONString(list), messageProperties);
            //设置消息过期时间
            message.getMessageProperties().setExpiration("180000");
            message.getMessageProperties().setType(type);
//            rabbitTemplate.send(ExchangeEnum.casicTopicExchange.getExchangeName(),
//                    queueName, message);
        }
    }

    @Override
    public String getTargetQueue(String sourceId) {
        String sourceStar = sourceId.substring(0, 2);
        if ("11".equals(sourceStar)) {
            return null;
        } else {
//         try {
//            Method method = ipAddrConfig.getClass().getMethod(MQConstants.BASIC_GETIPADDR + sourceStar);
//            String ip = (String) method.invoke(ipAddrConfig);
//            String targetQueue = MQConstants.BASIC_QUEUE + sourceStar;
////            Field field = ipAddrConfig.getClass().getField("ipAddr" + sourceStar);
////            String ip = (String) field.get(ipAddrConfig);
//            //如果当前ip即为源ip(除一种情况：二级挂掉，在一级登录)
//            if (ipAddrConfig.getIpAddr().equals(ip)) {
//               method = ipAddrConfig.getClass().getMethod(MQConstants.BASIC_GETIPADDR + "11");
//               ip = (String) method.invoke(ipAddrConfig);
//               targetQueue = MQConstants.BASIC_QUEUE + "11";
//            }
//            return targetQueue;
//         } catch (IllegalAccessException
//            | NoSuchMethodException | InvocationTargetException e) {
//            e.printStackTrace();
//         }
            return null;
        }
    }

    @Override
    public String getTargetQueueForSameLevel(String sourceId, String targetId) {
        String sourceStar = sourceId.substring(0, 2);
        String targetStar = targetId.substring(0, 2);
        if (sourceStar.equals(targetStar)) {
            return null;
        } else {
            String targetQueue = MQConstants.BASIC_QUEUE + targetStar;
            return targetQueue;
        }
    }

    @Override
    public <T> void sendMessageToTarget(T syncData, JSONArray arr) {
        if (arr.size() > 0) {
            JSONObject obj = arr.getJSONObject(0);
            if (!obj.isEmpty()) {
                String levelKey = obj.getString("level");
                String ip = obj.getString("targetQueueIP");
                MessageProperties messageProperties = new MessageProperties();
                MessageConverter messageConverter = new SimpleMessageConverter();
                Message message = messageConverter.toMessage(syncData, messageProperties);
                //设置消息过期时间
                message.getMessageProperties().setExpiration("180000");
                switch (levelKey) {
                    case "1-2":
                        primaryRabbitTemplate.send(MQConstants.PRIMARY_BASIC_EXCHANGE,
                                MQConstants.PRIMARY_BASIC_QUEUE + ip, message);
                        break;
                    case "2-1":
                        primaryRabbitTemplate.send(MQConstants.PRIMARY_BASIC_EXCHANGE,
                                MQConstants.PRIMARY_BASIC_QUEUE + ip, message);
                        break;
                    case "2-2":
                        primaryRabbitTemplate.send(MQConstants.PRIMARY_BASIC_EXCHANGE,
                                MQConstants.PRIMARY_BASIC_QUEUE + ip, message);
                        break;
                    case "2-3":
                        if (!StringUtils.isEmpty(secondRabbitTemplate)) {
                            secondRabbitTemplate.send(MQConstants.SECOND_BASIC_EXCHANGE,
                                    MQConstants.SECOND_BASIC_QUEUE + ip, message);
                        }
                        break;
                    case "3-2":
                        primaryRabbitTemplate.send(MQConstants.PRIMARY_BASIC_EXCHANGE,
                                MQConstants.PRIMARY_BASIC_QUEUE + ip, message);
                        break;
                    case "3-3":
                        primaryRabbitTemplate.send(MQConstants.PRIMARY_BASIC_EXCHANGE,
                                MQConstants.PRIMARY_BASIC_QUEUE + ip, message);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
