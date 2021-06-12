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

public class FinalAutoApp {

	private static Map<TopicPartition, OffsetAndMetadata> offsetsToBeSaved = new HashMap<TopicPartition, OffsetAndMetadata>();

	public static void main(String[] args) {
		ConsumerConfiguration config = new ConsumerConfiguration();
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(config.autoConsumerProperties());
		consumer.subscribe(Collections.singletonList("letsdo"), new RebalanceListenerSeekOffset(offsetsToBeSaved, consumer));
		System.out.println("consumer created... " + consumer.groupMetadata().memberId());
		boolean doProcessing = true;

		try {
			while (doProcessing) {
				// when enable.auto.commit=true, then offset will be committed on every current
				// poll, and we commit the largest offset that came in last poll
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(60));
				/*
				 * Re balance will be triggered when consumer leave/add group or partition
				 * updated. But re balance handler "onPartitonRevoked" will only invoked when
				 * consumer call poll. So if consumer processing records of last poll and re
				 * balance triggered, then even though consumer can commit on its partition
				 * because consumer has ownership on its partition. But once it do poll for next
				 * result then handler invoked onPartitonRevoked. Now consumer lose its
				 * ownership on partitions and can't commit. No impact of re balancing up to
				 * here. Because we have not processed anything. So nothing to commit on
				 * onPartitonRevoked. Let leave the partition on last offset committed index
				 */
				for (ConsumerRecord<String, String> record : records) {
					/*
					 * Now we are getting ready to handle re balancing by storing offsets of those
					 * records which we have successfully processed. And will pass these offsets to
					 * handler "onPartitonRevoked" on re balancing Let add these offsets to a map.
					 * But make sure only add offsets when you have successfully processed the
					 * corresponding record to that offset. Lets encapsulate offset store to map and
					 * processing the corresponding record in a transaction. Either both succeed or
					 * fail.
					 */

					// transaction start
					storeOffsetAndProcessRecord(record);
					// transaction end
				}
				/*
				 * If no exception, then do next poll, which automatically commit last offset of
				 * current poll. If exception then commit the offsets which are in map.
				 */
				System.out.println("Successfully Processed All Records");
			}
		} finally {
			/* time to exit the poll loop then commit offsets. */

			try {
				consumer.commitSync(offsetsToBeSaved);
				System.out.println("Successfully commited offsets when leaving poll lop...");
				offsetsToBeSaved.clear();
			} catch (Exception ex) {
				System.out.println("Offset commit failed...");
				retryCommit(consumer);
			}

			//consumer.close();// enable.auto.commit=true , on close we commit the largest offset
			doProcessing = false;
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

	private static void storeOffsetAndProcessRecord(ConsumerRecord<String, String> record) {
		savePartitionOffsets(record);
		processRecords(record);
	}

	private static void savePartitionOffsets(ConsumerRecord<String, String> record) {
		offsetsToBeSaved.put(new TopicPartition(record.topic(), record.partition()),
				new OffsetAndMetadata(record.offset() + 1));
	}

	private static void processRecords(ConsumerRecord<String, String> record) {
		checkIfAlreadyProcessed(record);
		System.out.println("topic = " + record.topic() + " , partition = " + record.partition() + " , offset = "
				+ record.offset() + " , key = " + record.key() + " , value = " + record.value());

		if (record.value().equalsIgnoreCase("fataal")) {
			offsetsToBeSaved.put(new TopicPartition(record.topic(), record.partition()),
					new OffsetAndMetadata(record.offset()));
			throw new RuntimeException();
		}

	}

	private static void checkIfAlreadyProcessed(ConsumerRecord<String, String> record) {
		// if we not able to commit offset by any reason then next poll contains result
		// from last committed stage which we already processed but failed to commit
		boolean canProcess = false;
		for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : offsetsToBeSaved.entrySet()) {

			if (entry.getKey().topic().equals(record.topic()) && entry.getKey().partition() == record.partition()
					&& entry.getValue().offset() - 1 == record.offset()) {
				canProcess = true;
			}

			if (canProcess == true) {
				System.out.println("Record eligible for processing for topic = " + record.topic() + " , partition = "
						+ record.partition() + " , offset = " + record.offset());
			} else {
				System.out.println("Record eligible for skipping for topic = " + record.topic() + " , partition = "
						+ record.partition() + " , offset = " + record.offset());

			}

		}
	}

	
}
