package com.fast.framework.data.jpa.specification;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * 分页参数
 * 
 * @author lion.chen
 * @version 1.0.0 2017年6月19日 下午2:10:57
 */
public interface BasePageParam {

	/**
	 * 获取分页参数
	 * @return
	 */
	PageRequest getPageRequest();
	
	/**
	 * 获取排序规则
	 * @return
	 */
	Sort getSort();
	
}
