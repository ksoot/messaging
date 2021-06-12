package com.ak.poc.spring.messagebus.springbus.producer;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import com.ak.poc.spring.messagebus.springbus.message.Message;

@Named(value = "asyncProducer")
public class AsyncMessageBusProducerClient<K, V, T extends Message<K, V>> implements MessageBusProducer<K, V, T> {

	@Inject
	private KafkaTemplate<K, V> kafkaTemplate;

	@Override
	public void produceMessage(T message) {
		System.out.println("producing message -> " + message.toString());

		ListenableFuture<SendResult<K, V>> future = kafkaTemplate.send(message.topic(), message.partition(),
				message.key(), message.data());
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
