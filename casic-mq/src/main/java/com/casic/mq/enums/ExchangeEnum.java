package com.casic.mq.enums;

import com.beust.jcommander.internal.Lists;

import java.util.List;

/**
 * mq exchange
 * @author xxf
 */
public enum ExchangeEnum {

    casicTopicExchange("casic.exchange", true,false);


    /**
     * 交换机名称
     */
    private String exchangeName;
    /**
     * 持久交换机
     */
    private boolean durable;
    /**
     * 交换机为空是否删除
     */
    private boolean autoDelete;

    ExchangeEnum(String exchangeName,  boolean durable,boolean autoDelete) {
        this.exchangeName = exchangeName;
        this.durable = durable;
        this.autoDelete = autoDelete;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public boolean isDurable() {
        return durable;
    }

    public void setDurable(boolean durable) {
        this.durable = durable;
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }

    public static List<ExchangeEnum> toList(){
        List<ExchangeEnum> list = Lists.newArrayList();
        for(ExchangeEnum exchangeEnum:ExchangeEnum.values()){
            list.add(exchangeEnum);
        }
        return list;
    }
}
