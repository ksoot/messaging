package com.ak.poc.messagebus.springbus.common.config;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class CommonMessagingConfiguration {

	@Inject
	private CommonProperties commonProp;

	@Bean(name = "topicProperties")
	public Map<String, Object> topicProperties() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(TopicConfig.MESSAGE_TIMESTAMP_TYPE_CONFIG, "CREATE_TIME");
		return properties;
	}

	@Bean(name = "adminProperties")
	public Map<String, Object> adminProperties() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, commonProp.getBootstrapServers());
		return properties;
	}

	@Bean
	public KafkaAdmin kafkaAdmin(@Named(value = "adminProperties") Map adminProperties) {
		KafkaAdmin kafkaAdmin = new KafkaAdmin(adminProperties);
		kafkaAdmin.setAutoCreate(true);
		kafkaAdmin.setFatalIfBrokerNotAvailable(true);
		return kafkaAdmin;
	}
	

}
