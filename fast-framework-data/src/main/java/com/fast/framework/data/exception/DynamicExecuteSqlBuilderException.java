package com.fast.framework.data.exception;

/**
 * 动态构建参数异常
 * @author lion.chen
 *
 */
public class DynamicExecuteSqlBuilderException extends RuntimeException {

	private static final long serialVersionUID = -8347468875156268518L;
	
	public DynamicExecuteSqlBuilderException() {
		this("DynamicUpdateEntityException");
	}

	public DynamicExecuteSqlBuilderException(String message) {
		super(message);
	}
	
}
