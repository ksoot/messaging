package com.ak.poc.messagebus.springbus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableKafka
@EnableConsumer
@EnableProducer
@ComponentScan(basePackages = "com.ak.poc.messagebus.springbus")
public @interface EnableMessaging {

}
