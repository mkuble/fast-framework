package com.fast.framework.component.freemarker.domain;

import java.util.Collection;
import java.util.Map;

import com.fast.framework.component.freemarker.model.CollectionExModel;
import com.fast.framework.component.freemarker.model.MapExModel;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

@SuppressWarnings("deprecation")
public class FastFreemarkerBeanWrapper extends BeansWrapper{

	public FastFreemarkerBeanWrapper() {
	}

	public TemplateModel wrap(Object object) throws TemplateModelException {
		
		if (object instanceof Map){
			return getInstance(object, MapExModel.FACTORY);
		}
		
		if (object instanceof Collection){
			return getInstance(object, CollectionExModel.FACTORY);
		}
		
		return super.wrap(object);
	}
	
}
