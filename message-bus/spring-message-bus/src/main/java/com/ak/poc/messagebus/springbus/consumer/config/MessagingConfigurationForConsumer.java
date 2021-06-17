package com.ak.poc.messagebus.springbus.consumer.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.LogIfLevelEnabled;
import org.springframework.kafka.support.converter.MessageConverter;
import org.springframework.kafka.support.converter.MessagingMessageConverter;

import com.ak.poc.messagebus.springbus.common.config.CommonProperties;
import com.ak.poc.messagebus.springbus.consumer.AutoCommitCapableMessageListener;
import com.ak.poc.messagebus.springbus.consumer.ManualCommitCapableMessageListener;
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
		if (consumerProp.getCommitMode().equals("auto")) {
			properties.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, consumerProp.getFetchMaxWaitMs());
			properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerProp.getAutoOffsetReset());
			properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, consumerProp.getAutoCommitIntervalMs());
			properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, consumerProp.getSessionTimeoutMs());
			properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
			properties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerProp.getGroupId());
		} else if (consumerProp.getCommitMode().equals("manual")) {
			properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerProp.getAutoOffsetReset());
			properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, consumerProp.getSessionTimeoutMs());
			properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
			properties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerProp.getGroupId());
		} else {
			throw new RuntimeException("Supported commitMode is either 'auto' or 'manual'. But you have provided "
					+ consumerProp.getCommitMode());
		}
		return properties;
	}

	@Bean
	public <K, V> ContainerProperties containerProperties() {
		ContainerProperties containerProps = new ContainerProperties("letsdoitbro");
		if (consumerProp.getCommitMode().equals("auto")) {
		} else {
		}
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
		listenerContainer.setupMessageListener(new AutoCommitCapableMessageListener<K, V>());
		listenerContainer.setInterceptBeforeTx(true);

		System.out.println("---------------created container ------------");
		return listenerContainer;
	}

	@Bean
	public MessagingMessageConverter messageConverter() {
		MessagingMessageConverter messageConverter = new MessagingMessageConverter();
		messageConverter.setRawRecordHeader(true);
		return messageConverter;
	}

	@Bean
	public <K, V> KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<K, V>> kafkaListenerContainerFactory(
			ConsumerFactory<K, V> consumerFactory) {
		ConcurrentKafkaListenerContainerFactory<K, V> factory = new ConcurrentKafkaListenerContainerFactory<K, V>();
		// this is global setting used by all container produced by this factory
		factory.setErrorHandler(null);
		factory.setConcurrency(1);
		factory.setRecordInterceptor(new MyRecordInterceptor<K, V>());
		factory.setMissingTopicsFatal(true);
		factory.setMessageConverter(messageConverter());
		factory.getContainerProperties().setCommitLogLevel(LogIfLevelEnabled.Level.DEBUG);
		if (consumerProp.getCommitMode().equals("auto")) {
			factory.getContainerProperties().setMessageListener(new AutoCommitCapableMessageListener<K, V>());
			factory.getContainerProperties().setAckMode(AckMode.RECORD);
		} else {
			factory.getContainerProperties().setMessageListener(new ManualCommitCapableMessageListener<K, V>());
			factory.getContainerProperties().setAckMode(AckMode.MANUAL_IMMEDIATE);
			// factory.getContainerProperties().setCommitCallback(null);
			// factory.getContainerProperties().setSyncCommits(true);
			// factory.getContainerProperties().setSyncCommitTimeout(Duration.ofSeconds(2));
		}
		

		// factory.setContainerCustomizer(consumerFactory);
		return factory;
	}

}
