package com.fast.framework.data.sqltemplate.transformer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * @author lion.chen
 * @version 1.0.0 2017年5月24日 下午4:51:59
 */
public class SingleResultTransformer {

	private static final Map<String, Class<?>> singleResultTransConfig = new HashMap<String, Class<?>>();
	
	static{
		singleResultTransConfig.put("string", String.class);
		singleResultTransConfig.put("map", Map.class);
		singleResultTransConfig.put("int", Integer.class);
		singleResultTransConfig.put("long", Long.class);
		singleResultTransConfig.put("boolean", Boolean.class);
		singleResultTransConfig.put("date", Date.class);
	}
	
	public static boolean isSingleResult(String resultClass){
		
		return singleResultTransConfig.containsKey(resultClass);
	}
	
	public static Class<?> getSingleResultClass(String resultClass){
		return singleResultTransConfig.get(resultClass);
	}
}
