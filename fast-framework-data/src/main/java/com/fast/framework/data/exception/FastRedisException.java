package com.fast.framework.data.exception;

/**
 * FastRedisException
 * @author lion.chen
 * @version 1.0.0 2017年5月23日 下午5:49:28
 */
public class FastRedisException extends RuntimeException{

	private static final long serialVersionUID = 8412175656827949947L;

	public FastRedisException(String msg, Exception e){
		super(msg, e);
	}
	
}
