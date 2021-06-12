package com.ak.poc.spring.messagebus.springbus.producer;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.core.KafkaAdmin;

import com.ak.poc.spring.messagebus.springbus.config.MessageBusProperties;
import com.ak.poc.spring.messagebus.springbus.message.Message;

@SpringBootApplication
@EnableConfigurationProperties(value = MessageBusProperties.class)
public class MessageBusApplicationProducer {

	//@Inject
	KafkaAdmin kafkaAdmin;
	public static void main(String[] args) {
	
	//	SpringApplication.run(MessageBusApplicationProducer.class, args);
		System.out.println();
	}
	 

	/*
	 * @Bean ApplicationRunner applicationRunner(@Named(value = "syncProducer")
	 * MessageBusProducer<Integer, String, Message<Integer,String>> producer) {
	 * return args -> { kafkaAdmin.initialize();
	 * System.out.println(".........Starting"); int count = 0; while(count < 10) {
	 * String baseMessage = "Hello count no. "+count; Message<Integer, String>
	 * message = buildmessage(count, baseMessage); producer.produceMessage(message);
	 * System.out.println("-----------message sent now waiting-------");
	 * Thread.currentThread().sleep(5000); count++; }
	 * System.out.println(".........Finished"); }; }
	 */
	  private <K,V> Message<K, V> buildmessage(int count, String baseMessage) {
		  Message<K, V> message = new Message("letsdoitbro", 0, count, baseMessage);
		  return message;
	  }
	  
	
}
