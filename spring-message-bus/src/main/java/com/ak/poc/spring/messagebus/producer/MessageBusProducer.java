package com.ak.poc.spring.messagebus.producer;

import java.util.Optional;

import org.springframework.kafka.support.SendResult;

import com.ak.poc.spring.messagebus.message.Message;

public interface MessageBusProducer<K, V, T extends Message<K, V>> {

	public void produceMessage(T message);

	public void handleSuccess(T message, SendResult<K, V> result);

	public void handleFailure(T message, Optional<SendResult<K, V>> optionalResult, Throwable ex);

}
