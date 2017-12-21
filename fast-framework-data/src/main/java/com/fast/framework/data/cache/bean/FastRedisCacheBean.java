package com.fast.framework.data.cache.bean;

/**
 * 使用缓存策略
 * @author lion.chen
 * @version 1.0.0 2017年5月23日 下午5:53:12
 */
public interface FastRedisCacheBean {

	/**
	 * 生成缓存key
	 * @return
	 */
	String getCacheKey();
	
}
