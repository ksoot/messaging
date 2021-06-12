package com.ak.poc.messagebus.kafkabus;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

public abstract class AbstractMessageProcessor<K, V> implements MessageProcessor<K, V> {

	protected Map<TopicPartition, OffsetAndMetadata> offsetsToBeSaved;

	public AbstractMessageProcessor(Map<TopicPartition, OffsetAndMetadata> offsetsToBeSaved) {
		this.offsetsToBeSaved = offsetsToBeSaved;
	}

	public void storeOffsetAndProcessRecord(ConsumerRecord<K, V> record) throws RecordProcessingException {
		// start transaction here
		storePartitionOffset(record);
		checkAndProcess(record);
		// end transaction
	}

	protected abstract boolean checkIfProcessed(ConsumerRecord<K, V> record);

	public void commitOffsetAsync(KafkaConsumer<K, V> consumer) {
		System.out.println("commiting offset async...");
		consumer.commitAsync(new RetryingAsyncCommitCallBackForManualCommit<K, V>(consumer));
	}

	public void commitOffsetAsync(KafkaConsumer<K, V> consumer,
			Map<TopicPartition, OffsetAndMetadata> offsetsToBeSaved) {
		System.out.println("commiting offset async...");
		consumer.commitAsync(offsetsToBeSaved, new RetryingAsyncCommitCallBackForManualCommit<K, V>(consumer));
	}

	public void commitOffsetSync(KafkaConsumer<K, V> consumer,
			Map<TopicPartition, OffsetAndMetadata> offsetsToBeSaved) {
		System.out.println("commiting offset...");
		try {
			consumer.commitSync(offsetsToBeSaved);
			System.out.println("Successfully commited offsets...");
			offsetsToBeSaved.clear();
		} catch (Exception ex) {
			System.out.println("Offset commit failed...");
			retryCommit(consumer);
		}
	}

	public void retryCommit(KafkaConsumer<K, V> consumer) {
		System.out.println("Retrying offsets commit again...");
		try {
			consumer.commitSync(offsetsToBeSaved);
			System.out.println("Successfully commited offsets during retry...");
			offsetsToBeSaved.clear();
		} catch (Exception ex) {
			System.out.println("Offset commit failed in retry...");
		}
	}

	private void checkAndProcess(ConsumerRecord<K, V> record) throws RecordProcessingException {
		if (checkIfProcessed(record) == false) {
			processMessage(record);
		}
	}

}
