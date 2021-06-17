package com.ak.poc.messagebus.springbus.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.ak.poc.messagebus.springbus.EnableConsumer;
import com.ak.poc.messagebus.springbus.common.config.CommonProperties;
import com.ak.poc.messagebus.springbus.consumer.config.ConsumerProperties;

@SpringBootApplication()
@EnableConfigurationProperties(value = { ConsumerProperties.class,
		CommonProperties.class })
@EnableConsumer
public class MessageBusApplicationConsumer {

	public static void main(String[] args) {

		SpringApplication.run(MessageBusApplicationConsumer.class, args);
		System.out.println();
	}

}
