package com.ak.poc.messagebus.springbus.consumer.event.listeners;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/*
 * If your error handler implements this interface, you can, for example, adjust the offsets accordingly. For example, to reset the offset to replay the failed message
 */

public class ReplayKafkaListenerErrorHandler implements ConsumerAwareListenerErrorHandler {

	@Override
	public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {
		MessageHeaders headers = message.getHeaders();
		consumer.seek(
				new TopicPartition(headers.get(KafkaHeaders.RECEIVED_TOPIC, String.class),
						headers.get(KafkaHeaders.RECEIVED_PARTITION_ID, Integer.class)),
				headers.get(KafkaHeaders.OFFSET, Long.class));
		return null;
	}

}
