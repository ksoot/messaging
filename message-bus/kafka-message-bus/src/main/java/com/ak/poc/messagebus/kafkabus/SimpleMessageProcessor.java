package com.ak.poc.messagebus.kafkabus;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

public class SimpleMessageProcessor<K, V> extends AbstractMessageProcessor<K, V> {

	public SimpleMessageProcessor(Map<TopicPartition, OffsetAndMetadata> offsetsToBeSaved) {
		super(offsetsToBeSaved);
	}

	@Override
	public void processMessage(ConsumerRecord<K, V> record) throws RecordProcessingException {
		try {
			System.out.println("topic = " + record.topic() + " , partition = " + record.partition() + " , offset = "
					+ record.offset() + " , key = " + record.key() + " , value = " + record.value());
			if (record.value().equals("error")) {
				throw new Exception();
			}
		} catch (Exception e) {
			// remove current offset of partition from map, because we failed to process
			// this record, so should not be eligible for commit during rebalacing
			offsetsToBeSaved.put(new TopicPartition(record.topic(), record.partition()),
					new OffsetAndMetadata(record.offset()));
			System.out.println("Failed to process offset " + record.offset() + " for partiton " + record.partition());
			throw new RecordProcessingException("Failed to process offset " + record.offset() + " for partiton "
					+ record.partition() + " " + e.getMessage(), e.getCause());
		}
	}

	@Override
	public void handleMessageProcessingFailure(ConsumerRecord<K, V> record, RecordProcessingException e) {
		System.out.println("Record processing failed..." + "topic = " + record.topic() + " , partition = "
				+ record.partition() + " , offset = " + record.offset() + " , key = " + record.key() + " , value = "
				+ record.value());
	}

	public void storePartitionOffset(ConsumerRecord<K, V> record) {
		// save current offset and partition info
		offsetsToBeSaved.put(new TopicPartition(record.topic(), record.partition()),
				new OffsetAndMetadata(record.offset() + 1));
	}

	protected boolean checkIfProcessed(ConsumerRecord<K, V> record) {
		boolean alreadyProcessed = true;
		for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : offsetsToBeSaved.entrySet()) {
			if (entry.getKey().topic().equals(record.topic()) && entry.getKey().partition() == record.partition()
					&& entry.getValue().offset() - 1 == record.offset()) {
				alreadyProcessed = false;
			}
			if (alreadyProcessed == false) {
				System.out.println("Record eligible for processing for topic = " + record.topic() + " , partition = "
						+ record.partition() + " , offset = " + record.offset());
			} else {
				System.out.println("Record eligible for skipping for topic = " + record.topic() + " , partition = "
						+ record.partition() + " , offset = " + record.offset());

			}
		}
		return alreadyProcessed;
	}

}
