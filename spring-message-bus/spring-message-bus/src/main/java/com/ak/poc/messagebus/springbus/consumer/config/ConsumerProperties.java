package com.ak.poc.messagebus.springbus.consumer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties()
public class ConsumerProperties {

	@Value("${kafka.consumer.key-deserializer}")
	private String consumerKeyDeSerializerClass;

	@Value("${kafka.consumer.value-deserializer}")
	private String consumerValueDeSerializerClass;

	@Value("${kafka.consumer.fetch-max-wait:1000}")
	private int fetchMaxWaitMs;

	@Value("${kafka.consumer.auto-offset-reset:earliest}")
	private String autoOffsetReset;

	@Value("${kafka.consumer.auto-commit-interval:1000}")
	private int autoCommitIntervalMs;

	@Value("${kafka.consumer.session-Timeout-Ms:12000}")
	private int sessionTimeoutMs;

	@Value("${kafka.consumer.enable-auto-commit:true}")
	private boolean enableAutoCommit;

	@Value("${kafka.consumer.group-id:ak}")
	private String groupId;

	
	public String getConsumerKeyDeSerializerClass() {
		return consumerKeyDeSerializerClass;
	}

	public String getConsumerValueDeSerializerClass() {
		return consumerValueDeSerializerClass;
	}

	public int getFetchMaxWaitMs() {
		return fetchMaxWaitMs;
	}

	public String getAutoOffsetReset() {
		return autoOffsetReset;
	}

	public int getAutoCommitIntervalMs() {
		return autoCommitIntervalMs;
	}

	public int getSessionTimeoutMs() {
		return sessionTimeoutMs;
	}

	public boolean getEnableAutoCommit() {
		return enableAutoCommit;
	}

	public String getGroupId() {
		return groupId;
	}

}
