package com.ak.poc.messagebus.springbus.producer.config;

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

import com.ak.poc.messagebus.springbus.common.config.CommonProperties;

@Configuration
public class MessagingConfigurationForProducer {

	@Inject
	private CommonProperties commonProp;

	@Inject
	private ProducerProperties producerProp;

	
	@Bean(name = "producerProperties")
	public Map<String, Object> producerProperties() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, commonProp.getBootstrapServers());
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, producerProp.getProducerKeySerializerClass());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, producerProp.getProducerValueSerializerClass());
		properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, producerProp.getRequestTimeoutMs());
		properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, producerProp.getMaxBlockMs());
		properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, producerProp.getBufferMemory());
		properties.put(ProducerConfig.LINGER_MS_CONFIG, producerProp.getLingerMs());
		properties.put(ProducerConfig.BATCH_SIZE_CONFIG, producerProp.getBatchSize());
		properties.put(ProducerConfig.RETRIES_CONFIG, producerProp.getRetries());
		properties.put(ProducerConfig.CLIENT_ID_CONFIG, producerProp.getClientId());

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
