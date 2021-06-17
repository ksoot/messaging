package com.ak.poc.messagebus.kafkabus;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

public interface MessageProcessor<K, V> {

	void processMessage(ConsumerRecord<K, V> record) throws RecordProcessingException;

	void handleMessageProcessingFailure(ConsumerRecord<K, V> record, RecordProcessingException e);

	void commitOffsetAsync(KafkaConsumer<K, V> consumer);

	void commitOffsetAsync(KafkaConsumer<K, V> consumer, Map<TopicPartition, OffsetAndMetadata> offsetsToBeSaved);

	void commitOffsetSync(KafkaConsumer<K, V> consumer, Map<TopicPartition, OffsetAndMetadata> offsetsToBeSaved);

	void retryCommit(KafkaConsumer<K, V> consumer);

	void storePartitionOffset(ConsumerRecord<K, V> record);

	void storeOffsetAndProcessRecord(ConsumerRecord<K, V> record) throws RecordProcessingException;

}
