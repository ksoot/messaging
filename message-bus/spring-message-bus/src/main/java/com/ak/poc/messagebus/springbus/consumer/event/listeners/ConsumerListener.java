package com.ak.poc.messagebus.springbus.consumer.event.listeners;

import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.core.ConsumerFactory.Listener;

public class ConsumerListener<K, V> implements Listener<K, V> {

	@Override
	public void consumerAdded(String id, Consumer<K, V> consumer) {
		System.out.println("consumerAdded... -> " + id);
	}

	@Override
	public void consumerRemoved(String id, Consumer<K, V> consumer) {
		System.out.println("consumerRemoved... -> " + id);
	}

}
