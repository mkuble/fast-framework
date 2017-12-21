package com.fast.framework.data.jpa.bean;

/**
 * jpa查询参数类型
 * @author lion.chen
 */
public enum FilterParamType {

	equal,
	notEqual,
	in,
	like, leftLike, rightLike,
	notLike, notLeftLike, notRightLike,
	gt, ge,	// 大于，大于等于
	lt, le,	// 小于，小于等于
	greaterThan, greaterThanOrEqualTo, 
	lessThan, lessThanOrEqualTo, 
	isNull,
	isNotNull;
	
}
