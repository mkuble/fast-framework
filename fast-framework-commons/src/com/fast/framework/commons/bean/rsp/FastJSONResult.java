package com.fast.framework.commons.bean.rsp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.fast.framework.commons.exception.FastBusinessException;

/**
 * json数据集
 * @author lion.chen
 *
 */
public class FastJSONResult implements Serializable{

	private static final long serialVersionUID = -7286535024267922614L;

	private static final int none_auth_code = -100;
	private static final int SUCCESS_CODE = 1;
	
	public int status;
	public String msg;
	public JSON data;
	
	public Map<String, JSON> exData = new HashMap<String, JSON>();
	
	public FastJSONResult addExData(String key, JSON data){
		this.exData.put(key, data);
		return this;
	}
	
	public FastJSONResult setError(int status, String msg){
		this.status = status;
		this.msg = msg;
		return this;
	}
	
	public FastJSONResult setSysError(String msg){
		this.status = -2;
		this.msg = msg;
		return this;
	}
	
	public FastJSONResult setBusinessError(String msg){
		this.status = -1;
		this.msg = msg;
		return this;
	}
	
	public FastJSONResult setBusinessError(FastBusinessException e){
		return setBusinessError(e.getMessage());
	}
	
	public FastJSONResult setAuthError(String msg, JSON data){
		this.status = none_auth_code;
		this.msg = msg;
		this.data = data;
		return this;
	}
	
	public FastJSONResult setAuthError(String msg){
		this.status = none_auth_code;
		this.msg = msg;
		return this;
	}
	
	public FastJSONResult setAuthError(){
		this.status = none_auth_code;
		return this;
	}
	
	public FastJSONResult setSuccess(){
		status = 1;
		return this;
	}
	
	public FastJSONResult setSuccess(JSON data){
		status = 1;
		this.data = data;
		return this;
	}
	
	public FastJSONResult setSuccess(String msg, JSON data){
		status = 1;
		this.data = data;
		this.msg = msg;
		return this;
	}
	
	public boolean success(){
		return this.status == SUCCESS_CODE;
	}
	
	public boolean fail(){
		return !success();
	}
}
