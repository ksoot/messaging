package com.ak.poc.messagebus.springbus.producer;

import java.util.Optional;

import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.support.SendResult;

import com.ak.poc.messagebus.springbus.common.message.Message;

public interface MessageBusProducer<K, V> {

	public void produceMessage(Message<K> message);

	public void handleSuccess(Message<K> message, SendResult<K, V> result);

	public void handleFailure(Message<K> message, Optional<SendResult<K, V>> optionalResult, Throwable ex);
	
	public void handleFailure(Message<K> message, Optional<SendResult<K, V>> optionalResult, KafkaProducerException ex);

}
