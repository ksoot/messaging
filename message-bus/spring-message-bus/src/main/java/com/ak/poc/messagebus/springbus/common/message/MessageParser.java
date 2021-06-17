package com.ak.poc.messagebus.springbus.common.message;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageParser<K,V> {

	public static <K,V> V parseToJSON(Message<K> message) {
		ObjectMapper mapper = new ObjectMapper();
		//mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		try {
			return (V) mapper.writeValueAsString(message.data());
		} catch (Exception e) {
			System.out.println("Failed to convert to JSON due to this reason.. " + e.getCause());
			e.printStackTrace();
			return null;
		}
	}
}
