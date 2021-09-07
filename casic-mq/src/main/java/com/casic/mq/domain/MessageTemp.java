package com.casic.mq.domain;

import java.io.Serializable;


/**
 * mq 测试实体类
 * @author xxf
 */
public class MessageTemp implements Serializable {

   private String message;


   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }
}
