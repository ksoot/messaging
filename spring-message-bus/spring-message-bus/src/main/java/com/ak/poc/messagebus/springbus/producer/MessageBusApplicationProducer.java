package com.ak.poc.messagebus.springbus.producer;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaAdmin;

import com.ak.poc.messagebus.springbus.EnableProducer;
import com.ak.poc.messagebus.springbus.common.config.CommonProperties;
import com.ak.poc.messagebus.springbus.common.message.Message;
import com.ak.poc.messagebus.springbus.producer.config.ProducerProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@SpringBootApplication
@EnableConfigurationProperties(value = { ProducerProperties.class, CommonProperties.class })
@EnableProducer
public class MessageBusApplicationProducer {

	@Inject
	KafkaAdmin kafkaAdmin;

	public static void main(String[] args) {

		SpringApplication.run(MessageBusApplicationProducer.class, args);
		System.out.println();
	}

	@Bean
	ApplicationRunner applicationRunner(
			@Named(value = "syncProducer") MessageBusProducer<Integer, String> producer) {
		return args -> {
			kafkaAdmin.initialize();
			System.out.println(".........Starting");
			int count = 0;
			while (count < 10) {
				String baseMessage = "Hello count no. " + count;
				Message<Integer> message = buildmessage(count, baseMessage);
				producer.produceMessage(message);
				System.out.println("-----------message sent now waiting-------");
				Thread.currentThread().sleep(5000);
				count++;
			}
			System.out.println(".........Finished");
		};
	}

	private <K> Message<K> buildmessage(int count, String baseMessage) {
		Message<K> message = new Message("letsdoitbro", 0, count, new Person(baseMessage, count));
		return message;
	}
	
	
}

//@JsonInclude(value = Include.NON_DEFAULT)
class Person{
	
	public String name;
	public int id;
	
	public Person(String name, int id) {
		super();
		this.name = name;
		this.id = id;
	}
	
}
