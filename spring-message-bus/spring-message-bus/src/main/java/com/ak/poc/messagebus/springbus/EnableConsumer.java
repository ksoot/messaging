package com.ak.poc.messagebus.springbus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;

import com.ak.poc.messagebus.springbus.consumer.config.MessagingConfigurationForConsumer;

@Import(MessagingConfigurationForConsumer.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableKafka
@ComponentScan(basePackages = {"com.ak.poc.messagebus.springbus.common", "com.ak.poc.messagebus.springbus.consumer"})
public @interface EnableConsumer {

}
