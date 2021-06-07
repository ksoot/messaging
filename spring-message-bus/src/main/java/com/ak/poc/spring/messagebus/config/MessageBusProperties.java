package com.ak.poc.spring.messagebus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties()
public class MessageBusProperties {

	// kafka Bootstrap servers properties
	@Value("${kafka.bootstrap-servers:localhost:9092}")
	private String bootstrapServers;

	// producers properties
	@Value("${kafka.producer.request-Timeout-Ms:10000}")
	private int requestTimeoutMs;

	@Value("${kafka.producer.max-Block-Ms:10000}")
	private int maxBlockMs;

	@Value("${kafka.producer.buffer-memory:50000}")
	private int bufferMemory;

	@Value("${kafka.producer.linger-Ms:1}")
	private int lingerMs;

	@Value("${kafka.producer.batch-size:50}")
	private int batchSize;

	@Value("${kafka.producer.retries:0}")
	private int retries;
	
	@Value("${kafka.producer.key-serializer}")
	private String producerKeySerializerClass;

	@Value("${kafka.producer.value-serializer}")
	private String producerValueSerializerClass;

	
	// consumer properties
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

	public String getBootstrapServers() {
		return bootstrapServers;
	}

	public int getRequestTimeoutMs() {
		return requestTimeoutMs;
	}

	public int getMaxBlockMs() {
		return maxBlockMs;
	}

	public int getBufferMemory() {
		return bufferMemory;
	}

	public int getLingerMs() {
		return lingerMs;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public int getRetries() {
		return retries;
	}


	public String getProducerKeySerializerClass() {
		return producerKeySerializerClass;
	}

	public String getProducerValueSerializerClass() {
		return producerValueSerializerClass;
	}

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
