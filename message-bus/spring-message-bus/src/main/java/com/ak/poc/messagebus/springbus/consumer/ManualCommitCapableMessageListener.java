package com.ak.poc.messagebus.springbus.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

public class ManualCommitCapableMessageListener<K, V> implements AcknowledgingMessageListener<K, V> {

	@Override
	public void onMessage(ConsumerRecord<K, V> data, Acknowledgment acknowledgment) {
		System.out.println("consuming offset -> "+data.offset());
		System.out.println(".......message value -> " + data.value());
		acknowledgment.acknowledge();

	}



	

	/*
	 * @Override public void onPartitionsAssigned(Map<TopicPartition, Long>
	 * assignments, ConsumerSeekCallback callback) { if
	 * (PartitionOffsetHolder.getSize() > 0) { for (Map.Entry<TopicPartition, Long>
	 * entry : assignments.entrySet()) { if (entry.getValue() ==
	 * PartitionOffsetHolder.getOffset(entry.getValue())) {
	 * callback.seek(entry.getKey().topic(), entry.getKey().partition(),
	 * entry.getValue()); } }
	 * 
	 * assignments.entrySet().stream() .filter(entry -> entry.getValue() !=
	 * PartitionOffsetHolder.getOffset(entry.getValue())) .forEach(entry -> {
	 * callback.seek(entry.getKey().topic(), entry.getKey().partition(),
	 * entry.getValue()); }); } System.out.println("here"); }
	 * 
	 * @Override public void onPartitionsRevoked(Collection<TopicPartition>
	 * partitions) { System.out.println("here"); }
	 * 
	 * @Override public void registerSeekCallback(ConsumerSeekCallback callback) {
	 * // TODO Auto-generated method stub System.out.println("here"); }
	 */
}
