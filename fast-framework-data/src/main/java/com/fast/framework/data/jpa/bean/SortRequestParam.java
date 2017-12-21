package com.fast.framework.data.jpa.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * 排序参数
 * @author lion.chen
 */
public class SortRequestParam {

	/**
	 * 排序字段
	 */
	public String field;
	
	/**
	 * 是否升序，否则降序
	 */
	public boolean asc;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
	
}
