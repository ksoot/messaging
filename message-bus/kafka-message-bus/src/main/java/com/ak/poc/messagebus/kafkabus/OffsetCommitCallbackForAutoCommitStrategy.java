package com.ak.poc.messagebus.kafkabus;

import java.util.Map;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;

public class OffsetCommitCallbackForAutoCommitStrategy implements OffsetCommitCallback {

	private KafkaConsumer<String, String> consumer;
	
	public OffsetCommitCallbackForAutoCommitStrategy(KafkaConsumer<String, String> consumer) {
		this.consumer = consumer;
	}
	
	public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
		if (exception == null) {
			
			for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : offsets.entrySet()) {
				System.out.println("Successfully committed topic = " + entry.getKey().topic() + " , partition = " + entry.getKey().partition() + " , offset = "
						+ entry.getValue().offset());
			}
		} else {
			retrySyncCommitAgain(offsets);
		}
		

	}

	private void retrySyncCommitAgain(Map<TopicPartition, OffsetAndMetadata> offsets) {
		consumer.commitSync(offsets);
	}
	
	

}
