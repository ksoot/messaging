package com.ak.poc.messagebus.springbus.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties()
public class CommonProperties {

	// kafka Bootstrap servers properties
	@Value("${kafka.bootstrap-servers:localhost:9092}")
	private String bootstrapServers;

	public String getBootstrapServers() {
		return bootstrapServers;
	}

}
