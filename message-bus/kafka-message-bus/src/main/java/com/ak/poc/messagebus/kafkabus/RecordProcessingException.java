package com.ak.poc.messagebus.kafkabus;

public class RecordProcessingException extends Exception {

	private static final long serialVersionUID = 1L;

	public RecordProcessingException() {
        super();
    }

    public RecordProcessingException(String message) {
        super(message);
    }

    public RecordProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecordProcessingException(Throwable cause) {
        super(cause);
    }


}
