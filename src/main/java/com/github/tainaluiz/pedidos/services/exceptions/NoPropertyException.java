package com.github.tainaluiz.pedidos.services.exceptions;

public class NoPropertyException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoPropertyException(String msg) {
		super(msg);
	}

	public NoPropertyException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
