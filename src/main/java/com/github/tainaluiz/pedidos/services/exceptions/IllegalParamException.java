package com.github.tainaluiz.pedidos.services.exceptions;

public class IllegalParamException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IllegalParamException(String msg) {
		super(msg);
	}

	public IllegalParamException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
