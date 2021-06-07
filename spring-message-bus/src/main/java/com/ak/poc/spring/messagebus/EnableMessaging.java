package com.ak.poc.spring.messagebus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;

import com.ak.poc.spring.messagebus.consumer.config.MessagingConfigurationForConsumer;

@Import(MessagingConfigurationForConsumer.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableKafka
public @interface EnableMessaging {

}
