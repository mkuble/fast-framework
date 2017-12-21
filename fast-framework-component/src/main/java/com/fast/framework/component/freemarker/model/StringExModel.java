package com.fast.framework.component.freemarker.model;

import java.util.List;

import com.fast.framework.commons.utils.FastStringUtils;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.StringModel;
import freemarker.ext.util.ModelFactory;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateModel;

/**
 * 自定义freemarker string 模板
 * @Title:  StringExModel.java
 * @author  chenliaohua
 * @date    2013-8-8 下午1:06:57
 */
public class StringExModel extends StringModel{

	public static final ModelFactory FACTORY = new ModelFactory() {
		public TemplateModel create(Object object, ObjectWrapper wrapper) {
			
			String str = object instanceof StringModel ? ((StringModel)object).getAsString() 
					: (String) object;
			
			return new StringExModel(str, (BeansWrapper) wrapper);
		}
	};
	
	public StringExModel(Object object, BeansWrapper wrapper) {
		super(object, wrapper);
	}
	
	private String getThis(){
		return (String) object;
	}
	
	public String[] split(String regex){
		return getThis().split(regex);
	}
	
	public String join(List<String> list){
		return FastStringUtils.join(list);
	}

	public String subString(int beginIndex){
		return getThis().substring(beginIndex);
	}
	
	public String subString(int beginIndex, int endIndex){
		return getThis().substring(beginIndex, endIndex);
	}
	
	public String trim(){
		return getThis().trim();
	}
	
	public int indexOf(String s){
		return getThis().indexOf(s);
	}
	
	public boolean indexOfRegex(String regex){
		
		return FastStringUtils.indexOfReg(getThis(), regex);
	}
	
}
