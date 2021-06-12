package com.ak.poc.messagebus.kafkabus;

import java.util.Map;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;

public class RetryingAsyncCommitCallBackForManualCommit implements OffsetCommitCallback {

	private KafkaConsumer<String, String> consumer;
	private long commuitSequence;

	public RetryingAsyncCommitCallBackForManualCommit(KafkaConsumer<String, String> consumer, long commuitSeq) {
		this.consumer = consumer;
		this.commuitSequence = commuitSeq;
	}

	public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
		if (exception == null) {
			for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : offsets.entrySet()) {
				System.out.println("Successfully committed topic = " + entry.getKey().topic() + " , partition = "
						+ entry.getKey().partition() + " , offset = " + entry.getValue().offset());
			}
		} else {
			System.out.println("commuitSequence " +commuitSequence);
		}
		
	}
	
	

	public long getCommuitSequence() {
		return commuitSequence;
	}

}
