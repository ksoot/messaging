package com.ak.poc.spring.messagebus.springbus.producer.event.listeners;

import org.apache.kafka.clients.producer.Producer;
import org.springframework.kafka.core.ProducerFactory.Listener;

public class ProducerListener<K, V> implements Listener<K, V>{
	
	@Override
	public void producerAdded(String id, Producer<K, V> producer) {
		System.out.println("producerAdded -> "+id);
	}
	
	@Override
	public void producerRemoved(String id, Producer<K, V> producer) {
		System.out.println("producerRemoved -> "+id);
	}

}
