package com.casic.mq.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * mq 配置属性初始化
 *
 * @author xxf
 */
@Component
public class MQDataConfig {

    private static String level;

   public static String getLevel() {
      return level;
   }

   @Value("${spring.application.level}")
   public void setLevel(String level) {
      MQDataConfig.level = level;
   }
}
