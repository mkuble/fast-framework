package com.fast.framework.commons.exception;

/**
 * 自定义参数异常
 * @author lionchen
 */
public class FastParamException extends Exception{

	private static final long serialVersionUID = 3941133113562944154L;
	
	private static final String fmt = "%s error!";

	public static FastParamException getWsParamException(String paramName){
		return new FastParamException(String.format(fmt, String.valueOf(paramName)));
	}
	
	public FastParamException(String msg){
		super(msg);
	}
	
}
