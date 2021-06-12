package com.ak.poc.messagebus.kafkabus;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

public class FinalManualApp {

	private static Map<TopicPartition, OffsetAndMetadata> offsetsToBeSaved = new HashMap<TopicPartition, OffsetAndMetadata>();
	private static long commitSequence = 0;

	public static void main(String[] args) {
		ConsumerConfiguration config = new ConsumerConfiguration();
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(config.manualConsumerProperties());
		consumer.subscribe(Collections.singletonList("letsdo"),
				new RebalanceListenerSeekOffset(offsetsToBeSaved, consumer));
		System.out.println("consumer created... ");
		boolean doProcessing = true;

		while (doProcessing) {
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(100));
			try {
				if (!records.isEmpty()) {
					processRecords(records); // should be atomic operation
					commitOffset(consumer); // if all good then do async commit here
				}
			} catch (Exception e) {
				System.out.println("Exception during processing, doing partial commit");
				commitOffset(consumer, offsetsToBeSaved); // if any failure during processing other than
															// RecordProcessingException then do synch commit of already
															// processed records present in map
			}
		}

	}

	private static void processRecords(ConsumerRecords<String, String> records) {
		for (ConsumerRecord<String, String> record : records) {
			try {
				storeOffsetAndProcessRecord(record); // store and process each record
			} catch (RecordProcessingException e) {
				handleFailure(record, e); // handle this record, either save in db, or send event. etc
			}
		}
	}

	private static void handleFailure(ConsumerRecord<String, String> record, RecordProcessingException e) {
		System.out.println("Record processing failed..." + "topic = " + record.topic() + " , partition = "
				+ record.partition() + " , offset = " + record.offset() + " , key = " + record.key() + " , value = "
				+ record.value());
	}

	private static void storeOffsetAndProcessRecord(ConsumerRecord<String, String> record)
			throws RecordProcessingException {
		// start transaction here
		storePartitionOffset(record);
		processRecord(record);
		// end transaction
	}

	private static void storePartitionOffset(ConsumerRecord<String, String> record) {
		// save current offset and partition info
		offsetsToBeSaved.put(new TopicPartition(record.topic(), record.partition()),
				new OffsetAndMetadata(record.offset() + 1));
	}

	private static void processRecord(ConsumerRecord<String, String> record) throws RecordProcessingException {
		if (checkIfRecordAlreadyProcessed(record) == false) {
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
				System.out
						.println("Failed to process offset " + record.offset() + " for partiton " + record.partition());
				throw new RecordProcessingException("Failed to process offset " + record.offset() + " for partiton "
						+ record.partition() + " " + e.getMessage(), e.getCause());
			}
		}
	}

	private static boolean checkIfRecordAlreadyProcessed(ConsumerRecord<String, String> record) {
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

	private static void commitOffset(KafkaConsumer<String, String> consumer) {
		System.out.println("commiting offset async...");
		consumer.commitAsync(new RetryingAsyncCommitCallBackForManualCommit(consumer, commitSequence));
	}

	private static void commitOffset(KafkaConsumer<String, String> consumer,
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

	private static void retryCommit(KafkaConsumer<String, String> consumer) {
		System.out.println("Retrying offsets commit again...");
		try {
			consumer.commitSync(offsetsToBeSaved);
			System.out.println("Successfully commited offsets during retry...");
			offsetsToBeSaved.clear();
		} catch (Exception ex) {
			System.out.println("Offset commit failed in retry...");
		}

	}

	
}
