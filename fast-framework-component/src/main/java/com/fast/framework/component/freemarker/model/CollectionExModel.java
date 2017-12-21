package com.fast.framework.component.freemarker.model;

import java.util.ArrayList;
import java.util.Collection;

import com.alibaba.fastjson.JSONObject;

import freemarker.core.CollectionAndSequence;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.CollectionModel;
import freemarker.ext.util.ModelFactory;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateModel;

/**
 *  自定义freemarker collection 模板
 * @Title:  CollectionExModel.java
 * @author  chenliaohua
 * @date    2013-8-8 下午12:48:00
 */
@SuppressWarnings("unchecked")
public final class CollectionExModel extends CollectionModel implements BaseModel{

	public static final ModelFactory FACTORY = new ModelFactory() {
		public TemplateModel create(Object object, ObjectWrapper wrapper) {
			return new CollectionExModel((ArrayList<Object>) object,
					(BeansWrapper) wrapper);
		}
	};
	
	public CollectionExModel(ArrayList<Object> collection, BeansWrapper wrapper) {
		super(collection, wrapper);
	}
	
	public ArrayList<Object> add(Object value){
		ArrayList<Object> c = (ArrayList<Object>)object;
		c.add(value);
		return c;
	}
	
	public ArrayList<Object> addAll(Collection<Object> values){
		ArrayList<Object> c = (ArrayList<Object>)object;
		c.addAll(values);
		return c;
	}
	
	public TemplateCollectionModel values() {
		return new CollectionAndSequence(new SimpleSequence(
				(ArrayList<Object>)object, wrapper));
	}

	public boolean isEmpty() {
		return ((ArrayList<Object>)object).isEmpty();
	}

	/**
	 * json输出
	 */
	public String toJson() {
		
		return JSONObject.toJSONString(object);
	}
	
}
