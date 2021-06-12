package com.ak.poc.messagebus.kafkabus;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;

public class ConsumerConfiguration {

	public Properties autoConsumerProperties() {
		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "WKWIN4979297.global.publicisgroupe.net:9092");
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringDeserializer");
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringDeserializer");
		properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "ak-auto-cg-1");
		properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "ak-consumer-2");
		properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000);
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		return properties;
	}
	
	public Properties manualConsumerProperties() {
		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "WKWIN4979297.global.publicisgroupe.net:9092");
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringDeserializer");
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringDeserializer");
		properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "ak-manual-cg-1");
		properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "ak-consumer-1");
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		return properties;
	}
}
