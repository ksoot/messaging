package com.ak.poc.messagebus.springbus.producer;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import com.ak.poc.messagebus.springbus.common.message.Message;
import com.ak.poc.messagebus.springbus.common.message.MessageParser;

@Named(value = "syncProducer")
public class SyncMessageBusProducerClient<K, V> implements MessageBusProducer<K, V> {

	@Inject
	private KafkaTemplate<K, V> kafkaTemplate;

	@Override
	public void produceMessage(Message<K> message) {
		SendResult<K, V> result = null;
		System.out.println("producing message -> " + message.toString());
		
		try {
			result = kafkaTemplate.send(message.topic(), message.partition(),
					message.key(), MessageParser.<K,V>parseToJSON(message)).get(10, TimeUnit.SECONDS);
	        handleSuccess(message, result);
	    }
	    catch (ExecutionException e) {
	    	KafkaProducerException ex = (KafkaProducerException)e.getCause();
	        handleFailure(message, Optional.ofNullable(result), ex);
	    } catch (TimeoutException | InterruptedException e) {
	    	handleFailure(message, Optional.ofNullable(result), e);
		}
	  
		
	}

	@Override
	public void handleSuccess(Message<K> message, SendResult<K, V> result) {
		System.out.println("produced message successfully with details ->" + result.toString());
	}

	@Override
	public void handleFailure(Message<K> message, Optional<SendResult<K, V>> optionalResult, Throwable ex) {
		System.out.println("Failed to produce message -> " + message.toString());
		System.out.println("Failed message -> " + ex.getMessage());
		System.out.println("Failed cause -> " + ex.getCause());
		System.out.println("With exception -> " + ex);
		throw new RuntimeException(ex.getMessage(), ex);
		
	}

	@Override
	public void handleFailure(Message<K> message, Optional<SendResult<K, V>> optionalResult,
			KafkaProducerException ex) {
		KafkaProducerException producerException = (KafkaProducerException)ex;
		ProducerRecord<Integer, String> failed = producerException.getFailedProducerRecord();
		throw new RuntimeException(producerException.getMessage(), producerException);
	}

}
