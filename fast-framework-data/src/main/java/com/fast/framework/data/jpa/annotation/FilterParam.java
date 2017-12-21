/**
 * @author lion.chen
 * @Version 1.0.0 2017年5月15日 下午4:47:57
 */
package com.fast.framework.data.jpa.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fast.framework.data.jpa.bean.FilterParamType;

/**
 * jpa查询参数注解
 * @author lion.chen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface FilterParam {

	/**
	 * 数据库对应字段（实体bean属性名称）
	 * @return
	 */
	String key() default "";
	
	/**
	 * 是否为索引
	 * @return
	 */
	boolean index() default false;
	
	boolean sort() default false;
	
	boolean asc() default true;
	
	/**
	 * jpa查询类型
	 * @return
	 */
	FilterParamType type() default FilterParamType.equal;
	
}