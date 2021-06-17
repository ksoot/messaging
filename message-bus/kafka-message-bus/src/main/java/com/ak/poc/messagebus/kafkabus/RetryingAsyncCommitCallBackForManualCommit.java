package com.ak.poc.messagebus.kafkabus;

import java.util.Map;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;

public class RetryingAsyncCommitCallBackForManualCommit<K, V> implements OffsetCommitCallback {

	private KafkaConsumer<K, V> consumer;

	public RetryingAsyncCommitCallBackForManualCommit(KafkaConsumer<K, V> consumer) {
		this.consumer = consumer;
	}

	public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
		if (exception == null) {
			for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : offsets.entrySet()) {
				System.out.println("Successfully committed topic = " + entry.getKey().topic() + " , partition = "
						+ entry.getKey().partition() + " , offset = " + entry.getValue().offset());
			}
		} else {
			System.out.println("");
		}

	}

}
