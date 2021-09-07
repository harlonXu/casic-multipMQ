package com.casic.mq.enums;

import com.beust.jcommander.internal.Lists;

import java.util.List;


/**
 * mq queue
 * @author xxf
 */
public enum QueueEnum{

    CASIC_QUEUE11("casic.exchange.queue11", "casic.exchange.queue11", true,
            false, false, false, ExchangeEnum.casicTopicExchange),

    CASIC_QUEUE12("casic.exchange.queue12", "casic.exchange.queue12", true,
            false, false, false, ExchangeEnum.casicTopicExchange),

    CASIC_QUEUE13("casic.exchange.queue13", "casic.exchange.queue13", true,
            false, false, false, ExchangeEnum.casicTopicExchange),

    CASIC_QUEUE14("casic.exchange.queue14", "casic.exchange.queue14", true,
            false, false, false, ExchangeEnum.casicTopicExchange),

    CASIC_QUEUE15("casic.exchange.queue15", "casic.exchange.queue15", true,
            false, false, false, ExchangeEnum.casicTopicExchange);


    /**
     * 队列名称
     */
    private String queueName;
    /**
     * 路由键
     */
    private String routingKey;
    /**
     * 持久队列
     */
    private boolean durable;
    /**
     * 排他队列（该队列仅由声明者的队列使用连接）
     */
    private boolean exclusive;
    /**
     * 队列为空是否删除
     */
    private boolean autoDelete;
    /**
     * 是否需要全部匹配
     */
    private boolean whereAll;

    /**
     * 交换机
     */
    private ExchangeEnum exchangeEnum;


    QueueEnum(String queueName, String routingKey, boolean durable, boolean exclusive, boolean autoDelete,
              boolean whereAll, ExchangeEnum exchangeEnum) {
        this.queueName = queueName;
        this.routingKey = routingKey;
        this.durable = durable;
        this.exclusive = exclusive;
        this.autoDelete = autoDelete;
        this.whereAll = whereAll;
        this.exchangeEnum = exchangeEnum;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public boolean isDurable() {
        return durable;
    }

    public void setDurable(boolean durable) {
        this.durable = durable;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }

    public boolean isWhereAll() {
        return whereAll;
    }

    public void setWhereAll(boolean whereAll) {
        this.whereAll = whereAll;
    }

    public ExchangeEnum getExchangeEnum() {
        return exchangeEnum;
    }

    public void setExchangeEnum(ExchangeEnum exchangeEnum) {
        this.exchangeEnum = exchangeEnum;
    }
    public static List<QueueEnum> toList(){
        List<QueueEnum> list = Lists.newArrayList();
        for(QueueEnum exchangeEnum:QueueEnum.values()){
            list.add(exchangeEnum);
        }
        return list;
    }

}
