package com.ak.poc.spring.messagebus.springbus.consumer;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.ak.poc.spring.messagebus.springbus.config.MessageBusProperties;

@SpringBootApplication(scanBasePackages = "com.ak.poc.spring.messagebus")
@EnableConfigurationProperties(value = MessageBusProperties.class)
public class MessageBusApplicationConsumer {


	public static void main(String[] args) {
		
		SpringApplication.run(MessageBusApplicationConsumer.class, args);
		System.out.println();
	}
	 

	  
	/*
	 * @KafkaListener(topics = "letsdoitbro", groupId = "ak") public void
	 * printMessage(String message) {
	 * System.out.println("i am working -> "+message); }
	 */}
