package com.ak.poc.messagebus.kafkabus;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

public class RebalanceListener implements ConsumerRebalanceListener {

	private Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
	KafkaConsumer<String, String> consumer;

	public RebalanceListener(Map<TopicPartition, OffsetAndMetadata> currentOffsets,
			KafkaConsumer<String, String> consumer) {
		this.currentOffsets = currentOffsets;
		this.consumer = consumer;
	}

	// Called before the rebalancing starts and after the consumer stopped consuming
	// messages. This is where you want to commit offsets, so whoever gets this
	// partition
	// next will know where to start.
	@Override
	public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
		System.out.println("Rebalancing starts - revoking partitions"+consumer.groupMetadata().groupInstanceId());
		partitions.forEach(partition -> {System.out.println("Lost partition -> "+partition.partition()+" for topic "+partition.topic() +" on consumer "+consumer.groupMetadata().groupInstanceId());});
		System.out.println("Lost partitions in rebalance. Committing current offsets:" + currentOffsets +" by consumer "+consumer.groupMetadata().groupInstanceId());
		consumer.commitSync(currentOffsets);
		System.out.println("Rebalancing ends - revoked all partitions successfully"+consumer.groupMetadata().groupInstanceId());
	}

	/*
	 * Called after partitions have been reassigned to the broker, but before the
	 * consumer starts consuming messages
	 */
	@Override
	public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
		
	}

}
