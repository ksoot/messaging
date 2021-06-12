package com.ak.poc.spring.messagebus.springbus.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.RecordInterceptor;

public class MyRecordInterceptor<K, V> implements RecordInterceptor<K, V> {

	

	@Override
	public ConsumerRecord<K, V> intercept(ConsumerRecord<K, V> record) {
		PartitionOffsetHolder.addOffset(record.offset());
		System.out.println("in record interceptor");
		return record;
	}

	@Override
	public void success(ConsumerRecord<K, V> record, Consumer<K, V> consumer) {
		System.out.println("removing offset -> "+record.offset());
		PartitionOffsetHolder.removeOffset(record.offset());
		System.out.println();
	}

	@Override
	public void failure(ConsumerRecord<K, V> record, Exception exception, Consumer<K, V> consumer) {
		System.out.println("Offset failed to commit "+PartitionOffsetHolder.getOffset(record.offset()));
	}

	@Override
	public ConsumerRecord<K, V> intercept(ConsumerRecord<K, V> record, Consumer<K, V> consumer) {
		System.out.println("saving offset -> "+record.offset());
		PartitionOffsetHolder.addOffset(record.offset());
		return record;
	}

}
