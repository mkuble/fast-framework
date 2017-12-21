package com.fast.framework.commons.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 处理Bean工具类
 * @author lion.chen
 * 2017年11月17日 下午2:17:00
 */
public class FastBeanUtils extends BeanUtils{

	private static final Logger LOG = Logger.getLogger(FastBeanUtils.class);
	
	private static final String GET = "get";
	
	private static final String IS = "is";
	
	public static Object getPropertyValue(Object bean, String propertyName, Object...objects) throws Throwable{
		
		Method method = getMethod(bean.getClass(), propertyName);
		
		if(method == null){
			return null;
		}
		
		return method.invoke(bean, objects);
	}
	
	/**
	 * 获取getXXX方法
	 * @param bean
	 * @param propertyName
	 * @return
	 */
	public static Method getMethod(Class<? extends Object> clazz, String propertyName) {
		
		if(clazz == null || StringUtils.isBlank(propertyName)){
			return null;
		}
		
		StringBuilder _get = new StringBuilder(GET).append(Character.toUpperCase(propertyName.charAt(0)));
		StringBuilder _is = new StringBuilder(IS).append(Character.toUpperCase(propertyName.charAt(0)));
		
		if (propertyName.length() > 1) {
			_get.append(propertyName.substring(1));
			_is.append(propertyName.substring(1));
		}
		
		Method method = null;
		
		try {
			method = clazz.getMethod(_get.toString());
		} catch (Exception e) {
			if(method == null){
				try {
					method = clazz.getMethod(_is.toString());
				} catch (Exception e1) {
				} 
			}
		} 
		
		return method;
	}

	/**
	 * 获取属性
	 * @param bean
	 * @param propertyName
	 * @return
	 */
	public static Field getField(Object bean, String propertyName) {
		for (Field f : bean.getClass().getDeclaredFields()) {
			if (propertyName.equals(f.getName())) {
				return f;
			}
		}
		return null;
	}

	/**
	 * 获取Long类型属性数据
	 * @param bean
	 * @param propertyName
	 * @return
	 * @throws NoSuchFieldException
	 */
	public static long getLongBeanProperty(Object bean, String propertyName)
			throws NoSuchFieldException {
		
		validateArgs(bean, propertyName);

		Object o = getBeanProperty(bean, propertyName);

		if (o == null)
			throw new NoSuchFieldException(propertyName);
		if (!(o instanceof Number)) {
			throw new IllegalArgumentException(propertyName + " not an Number");
		}
		
		return ((Number) o).longValue();
	}

	/**
	 * 判断是否存在该数据
	 * @param bean
	 * @param propertyName
	 * @return
	 */
	public static boolean hasProperty(Object bean, String propertyName){
		
		try{
			
			validateArgs(bean, propertyName);
			
			Field field = getField(bean, propertyName);
			
			if(field != null){
				return true;
			}
			
			Method getter = getMethod(bean.getClass(), propertyName);
			if(getter != null){
				return true;
			}
			
		}catch(Exception e){
		}
		
		return false;
	}
	
	/**
	 * 验证参数
	 * 
	 * @param bean
	 * @param propertyName
	 */
	public static void validateArgs(Object bean, String propertyName) throws IllegalArgumentException{
		if (bean == null) {
			throw new IllegalArgumentException("bean is null");
		}
		if (propertyName == null) {
			throw new IllegalArgumentException("propertyName is null");
		}
		if (propertyName.trim().length() == 0) {
			throw new IllegalArgumentException("propertyName is empty");
		}
	}
	
	/**
	 * 获取String属性数据
	 * @param bean
	 * @param fieldName
	 * @return
	 */
	public static String getStringProperty(Object bean, String fieldName) {

		String result = String.valueOf(getBeanProperty(bean, fieldName));

		return result;
	}

	/**
	 * 获取bean对应的属性
	 * @param bean
	 * @param propertyName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object getBeanProperty(Object bean, String propertyName) {
		
		Object obj = null;

		try{
			
			validateArgs(bean, propertyName);
			
			if(bean instanceof JSONObject){
				return ((JSONObject)bean).get(propertyName);
			}
			
			else if(bean instanceof Map){
				return ((Map)bean).get(propertyName);
			}
			
			else{
				
				Method getter = getMethod(bean.getClass(), propertyName);

				if (getter != null) {
					try {
						obj = getter.invoke(bean, new Object[0]);
					} catch (Exception ex) {
						Field field = getField(bean, propertyName);
						if (field != null){
							try {
								field.setAccessible(true);
								obj = field.get(bean);
							} catch (Exception e) {
								throw e;
							}
						}
					}
				}
			}
			
		}catch(Exception e){
			LOG.error("BeanExUtils.getBeanProperty error, message:" + e.getMessage());
		}

		return obj;
	}

	/**
	* 得到某个类的静态属性
	* @param ownerClass
	* @param fieldName
	* @return
	*/
	@SuppressWarnings("rawtypes")
	public static Object getStaticField(Class ownerClass, String fieldName){
		
		Object obj = null;
		
		try{
			Field field = ownerClass.getField(fieldName);
			obj = field.get(ownerClass);
		}catch(Exception e){
			LOG.error("====>>FastBeanUtils.getStaticField not find:" + ownerClass + "." + fieldName);
		}
		
		return obj;
	} 
	
	/**
	 * 获取泛型类型
	 * @param clazz
	 * @param index
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Class getGenericType(Class clazz, int index) {
		
		Type genType = clazz.getGenericSuperclass();
		
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		
		if (index >= params.length || index < 0) {
			throw new RuntimeException("Index outof bounds");
		}
		
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}
		
		return (Class) params[index];
	}
	
	public static Class<?> getGenericType(Class<?> type) {
		return getGenericType(type, 0);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getFieldValue(Object o, String prop) {
		try {
			PropertyDescriptor pd = new PropertyDescriptor(prop, o.getClass());
			return (T) pd.getReadMethod().invoke(o);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取list中所有对象指定属性不为空的值
	 * @param list
	 * @param prop
	 * @return
	 */
	public static Object[] getFieldValues(List<?> list, String prop) {
		Object[] arr = new Object[list.size()];
		int idx = 0;
		PropertyDescriptor pd = null;
		try {
			for (Object t : list) {
				if (pd == null) {
					pd = new PropertyDescriptor(prop, t.getClass());
				}
				Object val = pd.getReadMethod().invoke(t);
				if (val != null) {
					arr[idx++] = val;
				}
			}
		} catch (Exception e) {
		}
		
		return arr;
	}
	
	public static <T> T[] getFieldValues(List<?> list, String prop, Class<T> destType) {
		Object[] values = getFieldValues(list, prop);
		return convert(values, destType);
	}
	
//	public static <T> List<T> getFieldValueList(List<?> list, String prop, Class<T> destType) {
//		T[] values = getFieldValues(list, prop, destType);
//		return FastCollectionUtils.arrayToList(values);
//	}
	
	/**
	 * 转换数组为指定的类型
	 * @param arr
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] convert(Object[] arr, Class<T> clazz) {
		T[] rs = (T[]) Array.newInstance(clazz, arr.length);
		for (int i = 0; i < arr.length; i++) {
			Object o = arr[i];
			if (o.getClass().equals(clazz)) {
				rs[i] = (T) o;
				continue;
			}
			try {
				Constructor<T> c = clazz.getConstructor(o.getClass());
				rs[i] = c.newInstance(o);
			} catch (Exception e) {
				break;
			}
		}
		return rs;
	}
	
	public static void fillValues(List<?> src, List<?> dest, String srcProp, String destProp, String srcValProp, String destValProp) {
		if (dest.isEmpty() || src.isEmpty()) return;
		PropertyDescriptor srcPd = null;
		PropertyDescriptor destPd = null;
		PropertyDescriptor srcValPd = null;
		PropertyDescriptor destValPd = null;
		try {
			srcPd = new PropertyDescriptor(srcProp, src.get(0).getClass());
			destPd = new PropertyDescriptor(destProp, dest.get(0).getClass());
			srcValPd = new PropertyDescriptor(srcValProp, src.get(0).getClass());
			destValPd = new PropertyDescriptor(destValProp, dest.get(0).getClass());
			for (Object o : dest) {
				Object destValue = destPd.getReadMethod().invoke(o);
				for (Object s : src) {
					Object val = srcPd.getReadMethod().invoke(s);
					if (val.toString().equals(destValue.toString())) {
						Object v = srcValPd.getReadMethod().invoke(s);
						destValPd.getWriteMethod().invoke(o, v);
					}
				}
			}
		} catch (Exception e) {
			return;
		}
	}
	
}
