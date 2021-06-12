package com.ak.poc.messagebus.springbus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;

import com.ak.poc.messagebus.springbus.producer.config.MessagingConfigurationForProducer;

@Import(MessagingConfigurationForProducer.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableKafka
@ComponentScan(basePackages = {"com.ak.poc.messagebus.springbus.common", "com.ak.poc.messagebus.springbus.producer"})
public @interface EnableProducer {

}
