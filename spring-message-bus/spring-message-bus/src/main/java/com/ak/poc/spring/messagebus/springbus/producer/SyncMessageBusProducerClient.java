package com.ak.poc.spring.messagebus.springbus.producer;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import com.ak.poc.spring.messagebus.springbus.message.Message;

@Named(value = "syncProducer")
public class SyncMessageBusProducerClient<K, V, T extends Message<K, V>> implements MessageBusProducer<K, V, T> {

	@Inject
	private KafkaTemplate<K, V> kafkaTemplate;

	@Override
	public void produceMessage(T message) {
		SendResult<K, V> result = null;
		System.out.println("producing message -> " + message.toString());
		
		try {
			result = kafkaTemplate.send(message.topic(), message.partition(),
					message.key(), message.data()).get(10, TimeUnit.SECONDS);
	        handleSuccess(message, result);
	    }
	    catch (ExecutionException | TimeoutException | InterruptedException e) {
	        handleFailure(message, Optional.ofNullable(result), e);
	    }
	  
		
	}

	@Override
	public void handleSuccess(T message, SendResult<K, V> result) {
		System.out.println("produced message successfully with details ->" + result.toString());
	}

	@Override
	public void handleFailure(T message, Optional<SendResult<K, V>> optionalResult, Throwable ex) {
		System.out.println("Failed to produce message -> " + message.toString());
		System.out.println("Failed message -> " + ex.getMessage());
		System.out.println("Failed cause -> " + ex.getCause());
		System.out.println("With exception -> " + ex);
		throw new RuntimeException(ex.getMessage(), ex);
		
	}


}
