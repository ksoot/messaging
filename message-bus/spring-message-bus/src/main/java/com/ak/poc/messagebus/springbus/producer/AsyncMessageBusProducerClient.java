package com.ak.poc.messagebus.springbus.producer;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import com.ak.poc.messagebus.springbus.common.message.Message;
import com.ak.poc.messagebus.springbus.common.message.MessageParser;

@Named(value = "asyncProducer")
public class AsyncMessageBusProducerClient<K, V> implements MessageBusProducer<K, V> {

	@Inject
	private KafkaTemplate<K, V> kafkaTemplate;

	@Override
	public void produceMessage(Message<K> message) {
		System.out.println("producing message -> " + message.toString());

		ListenableFuture<SendResult<K, V>> future = kafkaTemplate.send(message.topic(), message.partition(),
				message.key(), MessageParser.<K,V>parseToJSON(message));
		future.addCallback(new KafkaSendCallback<K, V>() {

			@Override
			public void onSuccess(SendResult<K, V> result) {
				handleSuccess(message, result);
			}

			@Override
			public void onFailure(Throwable ex) {
				handleFailure(message, Optional.empty(), ex);
			}

			@Override
			public void onFailure(KafkaProducerException ex) {
				handleFailure(message, Optional.empty(), ex);
			}
		});

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
