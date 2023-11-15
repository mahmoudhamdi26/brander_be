package com.mhamdi.core.global.storage;

public class StorageException extends RuntimeException {
    public StorageException(String message) {
		super(message);
	}

	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}
}
