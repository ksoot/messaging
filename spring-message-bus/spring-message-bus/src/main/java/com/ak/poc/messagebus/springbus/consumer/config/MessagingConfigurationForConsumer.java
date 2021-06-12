package com.ak.poc.messagebus.springbus.consumer.config;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.LogIfLevelEnabled;

import com.ak.poc.messagebus.springbus.common.config.CommonProperties;
import com.ak.poc.messagebus.springbus.consumer.MyConsumer;
import com.ak.poc.messagebus.springbus.consumer.MyRecordInterceptor;

@Configuration
public class MessagingConfigurationForConsumer {

	@Inject
	private ConsumerProperties consumerProp;

	@Inject
	private CommonProperties commonProp;

	@Bean(name = "consumerProperties")
	public Map<String, Object> consumerProperties() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, commonProp.getBootstrapServers());
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, consumerProp.getConsumerKeyDeSerializerClass());
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				consumerProp.getConsumerValueDeSerializerClass());
		properties.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, consumerProp.getFetchMaxWaitMs());
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerProp.getAutoOffsetReset());
		properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, consumerProp.getAutoCommitIntervalMs());
		properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, consumerProp.getSessionTimeoutMs());
		properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, consumerProp.getEnableAutoCommit());
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerProp.getGroupId());
		return properties;
	}

	@Bean
	public <K, V> ContainerProperties containerProperties() {
		ContainerProperties containerProps = new ContainerProperties("letsdoitbro");
		// containerProps.setMessageListener(new MyConsumer<K, V>());
		containerProps.setLogContainerConfig(true);
		containerProps.setCommitLogLevel(LogIfLevelEnabled.Level.DEBUG);
		containerProps.setMissingTopicsFatal(true);

		// containerProps.setAuthorizationExceptionRetryInterval(Duration.ofMinutes(1L));

		return containerProps;
	}

	@Bean
	public <K, V> ConsumerFactory<K, V> consumerFactory(@Named(value = "consumerProperties") Map consumerProperties) {
		return new DefaultKafkaConsumerFactory<K, V>(consumerProperties);
	}

	@Bean
	public <K, V> MessageListenerContainer messageListenerContainer(ConsumerFactory<K, V> consumerFactory,
			ContainerProperties containerProperties) {
		KafkaMessageListenerContainer<K, V> listenerContainer = new KafkaMessageListenerContainer<K, V>(consumerFactory,
				containerProperties);
		listenerContainer.setRecordInterceptor(new MyRecordInterceptor());
		listenerContainer.setupMessageListener(new MyConsumer<K, V>());
		listenerContainer.setInterceptBeforeTx(true);

		System.out.println("---------------created container ------------");
		return listenerContainer;
	}

}
