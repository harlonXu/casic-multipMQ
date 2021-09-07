package com.casic.mq.service;

import com.alibaba.fastjson.JSONArray;

import java.util.List;

/**
 * mq service
 * @author xxf
 */
public interface ISendMessageService {

   /**
    * @param list        对象数据(Json)
    * @param queueName      目标队列
    * @param domainName  实体类
    * @param serviceName service类
    * @param methodName  方法名
    */
   <T> void sendMessage(List<T> list, String queueName,
                    String domainName, String serviceName, String methodName);

   /**
    * 获取目标地址(二级向一级同步)
    * @param sourceId
    * @return
    */
   String getTargetQueue(String sourceId);

   /**
    * 获取目标地址(二级向二级同步)
    * @param sourceId
    * @return
    */
   String getTargetQueueForSameLevel(String sourceId,String targetId);

   <T> void sendMessageToTarget(T syncData, JSONArray arr);
}
