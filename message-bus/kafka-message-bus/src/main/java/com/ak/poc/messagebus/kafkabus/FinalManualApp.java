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

public class FinalManualApp {

	private Map<TopicPartition, OffsetAndMetadata> offsetsToBeSaved;
	private MessageProcessor<String, String> processor;
	private ConsumerRebalanceListener listener;
	private ConsumerConfiguration config;
	private KafkaConsumer<String, String> consumer;

	public static void main(String[] args) {
		FinalManualApp app = new FinalManualApp();

		app.offsetsToBeSaved = new HashMap<TopicPartition, OffsetAndMetadata>();
		app.config = new ConsumerConfiguration();
		app.processor = new SimpleMessageProcessor<String, String>(app.offsetsToBeSaved);
		app.consumer = new KafkaConsumer<String, String>(app.config.manualConsumerProperties());
		app.listener = new RebalanceListenerSeekOffset(app.offsetsToBeSaved, app.consumer);
		app.consumer.subscribe(Collections.singletonList("letsdo"), app.listener);
		boolean doProcessing = true;
		
		System.out.println("consumer created... ");
		

		while (doProcessing) {
			ConsumerRecords<String, String> records = app.consumer.poll(Duration.ofSeconds(100));
			try {
				if (!records.isEmpty()) {
					app.processRecords(records); // should be atomic operation
					app.processor.commitOffsetAsync(app.consumer); // if all good then do async commit here
				}
			} catch (Exception e) {
				System.out.println("Exception during processing, doing partial commit");
				app.processor.commitOffsetSync(app.consumer, app.offsetsToBeSaved); 
				// if any failure during processing other than
				// RecordProcessingException then do synch commit of already
				// processed records present in map
			}
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
