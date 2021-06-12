package com.ak.poc.messagebus.kafkabus;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

public class RebalanceListenerSeekOffset implements ConsumerRebalanceListener {

	private Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
	KafkaConsumer<String, String> consumer;

	public RebalanceListenerSeekOffset(Map<TopicPartition, OffsetAndMetadata> currentOffsets,
			KafkaConsumer<String, String> consumer) {
		this.currentOffsets = currentOffsets;
		this.consumer = consumer;
	}

	/*
	 * Called before the rebalancing starts and after the consumer stopped consuming
	 * messages. This is where you want to commit offsets, so whoever gets this
	 * partition next will know where to start.
	 */
	@Override
	public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
		System.out.println("Rebalancing starts - revoking partitions..." + consumer.groupMetadata().memberId());
		partitions.forEach(partition -> {
			System.out.println("Lost partition -> " + partition.partition() + " for topic " + partition.topic()
					+ " on consumer... " + consumer.groupMetadata().memberId());
		});
		System.out.println(
				"Committing offsets:" + currentOffsets + " by consumer... " + consumer.groupMetadata().memberId());
		try {
			consumer.commitSync(currentOffsets);
			System.out.println("Successfully commited offsets during rebalancing...");
		} catch (Exception e) {
			System.out.println("Offset commit failed...");
			retryCommit();
		}
		System.out.println(
				"Rebalancing ends - revoked all partitions successfully... " + consumer.groupMetadata().memberId());
	}

	private void retryCommit() {
		System.out.println("Retrying offsets commit again...");
		try {
			consumer.commitSync(currentOffsets);
			System.out.println("Successfully commited offsets during rebalancing...");
		} catch (Exception e) {
			System.out.println("Offset commit failed in retry...");
		}

	}

	/*
	 * Called after partitions have been reassigned to the broker, but before the
	 * consumer starts consuming messages
	 */
	@Override
	public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
		System.out.println("Rebalancing starts - assiging partitions... " + consumer.groupMetadata().memberId());
		partitions.forEach(partition -> {
			final long committedOffset = consumer.position(partition);
			consumer.seek(partition, committedOffset);
			System.out.println("Assigned offset -> " + committedOffset + " to partition -> " + partition);
		});
		System.out.println(
				"Rebalancing ends - successfully assigned partitions... " + consumer.groupMetadata().memberId());
	}

}
