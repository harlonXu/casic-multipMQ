package com.casic.mq.properties;

import org.springframework.amqp.core.ExchangeTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * primary rabbitmq消息队列和交换机配置文件
 *
 * @author xxf
 */
public class PrimaryMQProperties {

    /**
     * 装载自定义配置交换机
     */
    private static PrimaryExchangeConfig exchange = new PrimaryExchangeConfig();

    /**
     * 装载自定义配置队列
     */
    private static List<PrimaryQueueConfig> queues = new ArrayList<>();


    public static class PrimaryQueueConfig {
        /**
         * 队列名称
         */
        private String queueName;
        /**
         * 交换机名称
         */
        private String exchangeName;
        /**
         * 路由键
         */
        private String routingKey;
        /**
         * 持久队列
         */
        private Boolean durable = Boolean.TRUE;
        /**
         * 排他队列（该队列仅由声明者的队列使用连接）
         */
        private Boolean exclusive = Boolean.FALSE;
        /**
         * 队列为空是否删除
         */
        private Boolean autoDelete = Boolean.FALSE;
        /**
         * 是否需要全部匹配
         */
        private Boolean whereAll = Boolean.FALSE;

        public String getQueueName() {
            return queueName;
        }

        public void setQueueName(String queueName) {
            this.queueName = queueName;
        }

        public String getExchangeName() {
            return exchangeName;
        }

        public void setExchangeName(String exchangeName) {
            this.exchangeName = exchangeName;
        }

        public String getRoutingKey() {
            return routingKey;
        }

        public void setRoutingKey(String routingKey) {
            this.routingKey = routingKey;
        }

        public Boolean getDurable() {
            return durable;
        }

        public void setDurable(Boolean durable) {
            this.durable = durable;
        }

        public Boolean getExclusive() {
            return exclusive;
        }

        public void setExclusive(Boolean exclusive) {
            this.exclusive = exclusive;
        }

        public Boolean getAutoDelete() {
            return autoDelete;
        }

        public void setAutoDelete(Boolean autoDelete) {
            this.autoDelete = autoDelete;
        }

        public Boolean getWhereAll() {
            return whereAll;
        }

        public void setWhereAll(Boolean whereAll) {
            this.whereAll = whereAll;
        }
    }

    public static class PrimaryExchangeConfig{
        /**
         * 交换机名称
         */
        private String exchangeName;
        /**
         * 交换机类型
         */
        private String customType;
        /**
         * 持久交换机
         */
        private Boolean durable = Boolean.TRUE;
        /**
         * 交换机为空是否删除
         */
        private Boolean autoDelete =Boolean.FALSE;

        public String getExchangeName() {
            return exchangeName;
        }

        public void setExchangeName(String exchangeName) {
            this.exchangeName = exchangeName;
        }

        public String getCustomType() {
            return customType;
        }

        public void setCustomType(String customType) {
            this.customType = customType;
        }

        public Boolean getDurable() {
            return durable;
        }

        public void setDurable(Boolean durable) {
            this.durable = durable;
        }

        public Boolean getAutoDelete() {
            return autoDelete;
        }

        public void setAutoDelete(Boolean autoDelete) {
            this.autoDelete = autoDelete;
        }
    }

//    static{
//        exchange.setExchangeName("casic.primary");
//        exchange.setCustomType(ExchangeTypes.TOPIC);
//
//        PrimaryQueueConfig primaryQueueConfig = new PrimaryQueueConfig();
//        primaryQueueConfig.setExchangeName("casic.primary");
//        primaryQueueConfig.setQueueName("casic.primary.queue11");
//        primaryQueueConfig.setRoutingKey("casic.primary.queue11");
//        queues.add(primaryQueueConfig);
//        PrimaryQueueConfig primaryQueueConfig1 = new PrimaryQueueConfig();
//        primaryQueueConfig1.setExchangeName("casic.primary");
//        primaryQueueConfig1.setQueueName("casic.primary.queue12");
//        primaryQueueConfig1.setRoutingKey("casic.primary.queue12");
//        queues.add(primaryQueueConfig1);
//    }

    public static PrimaryExchangeConfig getExchange() {
        return exchange;
    }

    public static void setExchange(PrimaryExchangeConfig exchange) {
        PrimaryMQProperties.exchange = exchange;
    }

    public static List<PrimaryQueueConfig> getQueues() {
        return queues;
    }

    public static void setQueues(List<PrimaryQueueConfig> queues) {
        PrimaryMQProperties.queues = queues;
    }
}
