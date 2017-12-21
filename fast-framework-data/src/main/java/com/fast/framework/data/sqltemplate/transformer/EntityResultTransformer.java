package com.fast.framework.data.sqltemplate.transformer;

import javax.persistence.Entity;

import org.springframework.core.annotation.AnnotationUtils;

/**
 * 
 * @author lion.chen
 * @version 1.0.0 2017年5月24日 下午4:50:16
 */
public class EntityResultTransformer {

	/**
	 * 是否为entity
	 * @param entityClass
	 * @return
	 */
	public boolean isEntityResultMapping(Class<?> entityClass){
		return entityClass != null &&
				AnnotationUtils.findAnnotation(entityClass, Entity.class) != null;
	}
	
}
