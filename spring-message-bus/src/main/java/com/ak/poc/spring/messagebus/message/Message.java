package com.ak.poc.spring.messagebus.message;

public final class Message<K, V> {

	private final String topic;
	private final int partition;
	private final K key;
	private final V data;

	public Message(String topic, int partition, K key, V data) {
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

	public V data() {
		return data;
	}

	@Override
	public String toString() {
		return "Message [topic=" + topic + ", partition=" + partition + ", key=" + key + ", data=" + data + "]";
	}

}
