package com.fast.framework.commons.exception;

/**
 * 自定义业务异常
 * @author lionchen
 */
public class FastBusinessException extends RuntimeException{

	private static final long serialVersionUID = 6964686266291713696L;

	public FastBusinessException(String msg){
		super(msg);
	}

}
