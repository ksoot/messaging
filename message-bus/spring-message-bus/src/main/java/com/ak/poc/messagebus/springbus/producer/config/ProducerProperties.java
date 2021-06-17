package com.ak.poc.messagebus.springbus.producer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties()
public class ProducerProperties {

	
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

	@Value("${kafka.producer.clientID}")
	private String clientID;

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

	public String getClientId() {
		return clientID;
	}


}
