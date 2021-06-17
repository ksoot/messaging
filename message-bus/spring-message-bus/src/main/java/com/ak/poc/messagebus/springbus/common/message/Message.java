package com.ak.poc.messagebus.springbus.common.message;

public final class Message<K> {

	private String topic;
	private int partition;
	private K key;
	private Object data;

	public Message() {
		
	}
	
	public Message(String topic, int partition, K key, Object data) {
		this.topic = topic;
		this.partition = partition;
		this.key = key;
		this.data = data;
	}


	public String topic() {
		return topic;
	}

	public int partition() {
		return partition;
	}

	public K key() {
		return key;
	}

	public Object data() {
		return data;
	}

	@Override
	public String toString() {
		return "Message [topic=" + topic + ", partition=" + partition + ", key=" + key + ", data=" + data + "]";
	}

}
