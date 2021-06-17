package com.ak.poc.messagebus.kafkabus;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

public class FinalAutoApp {

	private Map<TopicPartition, OffsetAndMetadata> offsetsToBeSaved;
	private MessageProcessor<String, String> processor;
	private ConsumerRebalanceListener listener;
	private ConsumerConfiguration config;
	private KafkaConsumer<String, String> consumer;

	public static void main(String[] args) {

		FinalAutoApp app = new FinalAutoApp();

		app.offsetsToBeSaved = new HashMap<TopicPartition, OffsetAndMetadata>();
		app.config = new ConsumerConfiguration();
		app.processor = new SimpleMessageProcessor<String, String>(app.offsetsToBeSaved);
		app.consumer = new KafkaConsumer<String, String>(app.config.autoConsumerProperties());
		app.listener = new RebalanceListenerSeekOffset(app.offsetsToBeSaved, app.consumer);
		app.consumer.subscribe(Collections.singletonList("letsdo"), app.listener);
		boolean doProcessing = true;

		System.out.println("consumer created... ");

		try {
			while (doProcessing) {
				// when enable.auto.commit=true, then offset will be committed on every current
				// poll, and we commit the largest offset that came in last poll
				ConsumerRecords<String, String> records = app.consumer.poll(Duration.ofSeconds(60));
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
					app.processRecords(records);
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
				app.processor.commitOffsetSync(app.consumer, app.offsetsToBeSaved);
				System.out.println("Successfully commited offsets when leaving poll lop...");
				app.offsetsToBeSaved.clear();
			} catch (Exception ex) {
				System.out.println("Offset commit failed...");
				app.processor.retryCommit(app.consumer);
			}

			app.consumer.close();// enable.auto.commit=true , on close we commit the largest
			// offset
			doProcessing = false;
		}
	}

	private void processRecords(ConsumerRecords<String, String> records) {
		for (ConsumerRecord<String, String> record : records) {
			try {
				this.processor.storeOffsetAndProcessRecord(record); // store and process each record
			} catch (RecordProcessingException e) {
				this.processor.handleMessageProcessingFailure(record, e); // handle this record, either save in db, or
																			// send event. etc
			}
		}
	}
}
