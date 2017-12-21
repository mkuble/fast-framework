package com.fast.framework.data.jpa.bean;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;
import com.fast.framework.commons.utils.FastClassUtils;
import com.fast.framework.commons.utils.FastClassUtils.FieldMethodFilter;
import com.fast.framework.commons.utils.FastStringUtils;

/**
 * 数据库封装父级类
 * 	-适用于int id主键、create_time表结构
 * @author lion.chen
 * @version 1.0.0 2017年6月16日 下午6:28:20
 */
public class BaseIdEntity implements Serializable{
	
	private static final long serialVersionUID = 3011056011505594561L;

	private static final String k_id = "id";
	
	@JSONField(serialize = false)
	private Boolean isAdd = null;
	
	/**
	 * 是否为新增数据
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isAdd(){
		
		if(isAdd == null){
			try{
				
				Class<?> clazz = getClass();
				
				Map<Field, Method> fieldGetMethods = FastClassUtils.findFieldGetMethods(clazz, new FieldMethodFilter() {
					public boolean valid(Field f, Method m) {
						return FastStringUtils.equalsIgnoreCase(k_id, f.getName());
					}
				});
				
				if(fieldGetMethods != null && !fieldGetMethods.isEmpty()){
					Iterator<Field> iterator = fieldGetMethods.keySet().iterator();
					if(iterator.hasNext()){
						Field next = iterator.next();
						Object idValue = null;
						if(next.isAccessible()){
							idValue = next.get(this);
						}else{
							Method method = fieldGetMethods.get(next);
							if(method != null){
								idValue = method.invoke(this);
							}
						}
						if(idValue == null){
							isAdd = true;
						}else{
							if(idValue instanceof Number){
								Number idNumberValue = (Number) idValue;
								if(idNumberValue.longValue() == 0){
									isAdd = true;
								}else{
									isAdd = false;
								}
							}else{
								isAdd = false;
							}
						}
					}
				}
			}catch(Throwable e){
				e.printStackTrace();
				isAdd = true;
			}
		}
		
		return isAdd.booleanValue();
	}
	
	/**
	 * 是否为修改
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isUpdate(){
		return !isAdd();
	}
	
}
