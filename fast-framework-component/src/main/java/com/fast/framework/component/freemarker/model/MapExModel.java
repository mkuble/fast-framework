package com.fast.framework.component.freemarker.model;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

import freemarker.core.CollectionAndSequence;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.MapModel;
import freemarker.ext.util.ModelFactory;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateModel;

/**
 * 自定义freemarker map模板
 * @Title:  MapModelEx.java
 * @author  chenliaohua
 * @email   chenlh@travelsky.com
 * @date    2013-8-8 下午12:40:36
 */
@SuppressWarnings("unchecked")
public final class MapExModel extends MapModel implements BaseModel{

	public static final ModelFactory FACTORY = new ModelFactory() {
		public TemplateModel create(Object object, ObjectWrapper wrapper) {
			return new MapExModel((Map<String, Object>) object,
					(BeansWrapper) wrapper);
		}
	};
	
	public MapExModel(Map<String, Object> map, BeansWrapper wrapper) {
		super(map, wrapper);
	}
	
	public boolean isEmpty() {
		return ((Map<String, Object>) object).isEmpty();
	}

	protected Set<String> keySet() {
		return ((Map<String, Object>) object).keySet();
	}

	public String put(String key, Object value){
		
		Map<String, Object> map = (Map<String, Object>) object;
		
		map.put(key, value);
		
		return StringUtils.EMPTY;
	}
	
	public TemplateCollectionModel values() {
		return new CollectionAndSequence(new SimpleSequence(
				((Map<String, Object>) object).values(), wrapper));
	}

	/**
	 * 转换为json
	 */
	public String toJson() {
		return JSONObject.toJSONString(object);
	}
	
}
