package com.ak.poc.spring.messagebus.springbus.producer.config;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.ak.poc.spring.messagebus.springbus.config.MessageBusProperties;

@Configuration
public class MessagingConfigurationForProducer {

	@Inject
	private MessageBusProperties messageBusProp;

	
	@Bean(name = "producerProperties")
	public Map<String, Object> producerProperties() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, messageBusProp.getBootstrapServers());
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, messageBusProp.getProducerKeySerializerClass());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, messageBusProp.getProducerValueSerializerClass());
		properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, messageBusProp.getRequestTimeoutMs());
		properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, messageBusProp.getMaxBlockMs());
		properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, messageBusProp.getBufferMemory());
		properties.put(ProducerConfig.LINGER_MS_CONFIG, messageBusProp.getLingerMs());
		properties.put(ProducerConfig.BATCH_SIZE_CONFIG, messageBusProp.getBatchSize());
		properties.put(ProducerConfig.RETRIES_CONFIG, messageBusProp.getRetries());

		return properties;
	}


	@Bean
	public <K,V> ProducerFactory<K, V> producerFactory(@Named(value = "producerProperties") Map producerProperties) {
		return new DefaultKafkaProducerFactory<K, V>(producerProperties);
	}

	@Bean
	public KafkaTemplate<Integer, String> kafkaTemplte(ProducerFactory<Integer, String> producerFactory) {
		return new KafkaTemplate<Integer, String>(producerFactory);
	}

}
