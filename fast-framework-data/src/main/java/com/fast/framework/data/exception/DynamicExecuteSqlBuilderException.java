package com.fast.framework.data.exception;

public class DynamicExecuteSqlBuilderException extends RuntimeException {

	private static final long serialVersionUID = -8347468875156268518L;
	
	public DynamicExecuteSqlBuilderException() {
		this("DynamicUpdateEntityException");
	}

	public DynamicExecuteSqlBuilderException(String message) {
		super(message);
	}
	
}
