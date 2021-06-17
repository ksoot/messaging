package com.ak.poc.messagebus.springbus.consumer.event.listeners;

import javax.inject.Inject;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;

/*
 * you wish to send a failure result to the sender, after some number of retries, after capturing the failed record in a dead letter topic
 */

public class DeadLetterPublisherKafkaListenerErrorHandler implements KafkaListenerErrorHandler {

	@Inject
	private DeadLetterPublishingRecoverer recoverer;

	@Override
	public Object handleError(Message<?> msg, ListenerExecutionFailedException ex) {
		if (msg.getHeaders().get(KafkaHeaders.DELIVERY_ATTEMPT, Integer.class) > 3) {
			recoverer.accept(msg.getHeaders().get(KafkaHeaders.RAW_DATA, ConsumerRecord.class), ex);
			return "FAILED";
		}
		throw ex;
	}

}
