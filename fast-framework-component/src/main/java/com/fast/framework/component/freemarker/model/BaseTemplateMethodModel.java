package com.fast.framework.component.freemarker.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * 自定义模板方法父类
 * @Title:  BaseTemplateMethodModel.java
 * @author  chenliaohua
 * @date    2013-7-31 下午1:38:25
 */
public abstract class BaseTemplateMethodModel implements TemplateMethodModelEx {
	
	/**
	 * 方法入口
	 * @param list
	 * @return
	 * @throws TemplateModelException
	 */
	@SuppressWarnings("rawtypes")
	public Object exec(List list) throws TemplateModelException {
		
		if(!checkParam(list)){
			
			return StringUtils.EMPTY;
		}
		
		return execEx(list);
	}
	
	/**
	 * 执行方法体
	 * @param list
	 * @return
	 * @throws TemplateModelException
	 */
	@SuppressWarnings("rawtypes")
	public abstract Object execEx(List list) throws TemplateModelException;
	
	/**
	 * 检查参数
	 * @param paramList
	 * @return
	 * @throws TemplateModelException
	 */
	@SuppressWarnings("rawtypes")
	public boolean checkParam(List paramList) throws TemplateModelException{
		
		return true;
	}
	
}
