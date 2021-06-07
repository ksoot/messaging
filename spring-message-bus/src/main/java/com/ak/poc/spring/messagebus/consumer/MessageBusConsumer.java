package com.ak.poc.spring.messagebus.consumer;

import java.util.Map;

import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.listener.ConsumerSeekAware;

public interface MessageBusConsumer<T> extends ConsumerSeekAware {

	public void consumeMessage(T message);

	@Override
	default void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
		assignments.keySet().forEach(topicPartition -> {
			callback.seekRelative(topicPartition.topic(), 0, 0, true);
		});
	}
}
