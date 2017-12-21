package com.fast.framework.data.jpa.bean;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import com.alibaba.fastjson.JSONObject;
import com.fast.framework.data.jpa.annotation.FilterParam;

/**
 * 构建jpa查询参数
 * @author lion.chen
 */
public class FilterParamBean {

	private static final String like_split = "%";
	
	/**
	 * 比较相关查询参数
	 */
	private static List<FilterParamType> compareFilterParams = Arrays.asList(	FilterParamType.in, 
																				FilterParamType.ge, 
																				FilterParamType.gt, 
																				FilterParamType.le, 
																				FilterParamType.lt, 
																				FilterParamType.isNotNull);
	
	/**
	 * like相关查询参数
	 */
	private static List<FilterParamType> likeFilterParams = Arrays.asList(	FilterParamType.like, 
																			FilterParamType.leftLike,
																			FilterParamType.rightLike,
																			FilterParamType.notLike,
																			FilterParamType.notLeftLike,
																			FilterParamType.notRightLike);
	
	public String key;
	public Object target;
	public Field field;
	public FilterParam filterParam;
	
	public FilterParamBean(Field field, String key, Object target, FilterParam filterParam){
		this.field = field;
		this.key = key;
		this.target = target;
		this.filterParam = filterParam;
	}
	
	/**
	 * 获取当前数据值
	 * @return
	 */
	private Object getValue(){
		
		Object value = null;
		
		try {
			value = field.get(target);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return value;
	}
	
	/**
	 * 获取like值
	 * @return
	 */
	private String getLikeValue(){
		
		Object value = getValue();
		
		if(value != null){
			
			String tempvalue = value.toString();
			
			if(StringUtils.isNotEmpty(tempvalue)){
				
				if(FilterParamType.like == filterParam.type()){
					return new StringBuffer(like_split).append(tempvalue).append(like_split).toString();
				}
				
				if(FilterParamType.leftLike == filterParam.type()){
					return new StringBuffer(like_split).append(tempvalue).toString();
				}
				
				if(FilterParamType.rightLike == filterParam.type()){
					return new StringBuffer(tempvalue).append(like_split).toString();
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 获取非like属性值
	 * @return
	 */
	private String getNotLikeValue(){
		
		Object value = getValue();
		
		if(value != null){
			
			String tempvalue = value.toString();
			
			if(StringUtils.isNotEmpty(tempvalue)){
				
				if(FilterParamType.notLike == filterParam.type()){
					return new StringBuffer(like_split).append(tempvalue).append(like_split).toString();
				}
				
				if(FilterParamType.notLeftLike == filterParam.type()){
					return new StringBuffer(like_split).append(tempvalue).toString();
				}
				
				if(FilterParamType.notRightLike == filterParam.type()){
					return new StringBuffer(tempvalue).append(like_split).toString();
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 是否为比较类型查询
	 * @return
	 */
	public boolean isCompare(){
		return compareFilterParams.contains(filterParam.type());
	}
	
	/**
	 * 是否为索引
	 * @return
	 */
	public boolean isIndex(){
		return filterParam.index();
	}
	
	/**
	 * 是否为like查询
	 * @return
	 */
	public boolean isLike(){
		return likeFilterParams.contains(filterParam.type());
	}
	
	private static final List<FilterParamType> comparePredicateTypes = Arrays.asList(FilterParamType.greaterThan, FilterParamType.greaterThanOrEqualTo, FilterParamType.lessThan, FilterParamType.lessThanOrEqualTo);
	
	/**
	 * 构建查询参数
	 * @param root
	 * @param cb
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Predicate getPredicate(Root<?> root, CriteriaBuilder cb){
		
		Object value = getValue();
		
		if(value != null){
			
			Path<?> path = root.get(key);
			
			if(path == null){
				return null;
			}
			
			if(filterParam.type() == FilterParamType.equal) return cb.equal(path, value);
			
			if(filterParam.type() == FilterParamType.notEqual) return cb.notEqual(path, value);
			
			if(filterParam.type() == FilterParamType.isNull) return cb.isNull(path);
			
			if(filterParam.type() == FilterParamType.isNotNull) return cb.isNotNull(path);
			
			if(value != null){
				
				if(filterParam.type() == FilterParamType.in){
					if(value instanceof Collection && !((Collection<?>) value).isEmpty()){
						return root.get(key).in((Collection<?>)value);
					}else if(value.getClass().isArray()){
						return root.get(key).in((Array)value);
					}else{
						return null;
					}
				}
				
				if(comparePredicateTypes.indexOf(filterParam.type()) > -1){
					return getComparePredicate(value, root, cb);
				}
			}
			
			if(Number.class.isAssignableFrom(path.getJavaType()) && value instanceof Number){
				return getCompareNumberPredicate((Number) value, (Path<Number>) path, cb);
			}
			
			// like
			String likeValue = getLikeValue();
			if(StringUtils.isNotEmpty(likeValue)){
				return cb.like((Path<String>) path, likeValue);
			}
			
			// notLike
			String notLikeValue = getNotLikeValue();
			if(StringUtils.isNotEmpty(notLikeValue)){
				return cb.notLike((Path<String>) path, notLikeValue);
			}
			
		}
		
		return null;
	}
	
	private Predicate getCompareNumberPredicate(Number value, Path<Number> path, CriteriaBuilder cb){
		
		if(filterParam.type() == FilterParamType.ge){
			return cb.ge(path, value);
		}
		
		if(filterParam.type() == FilterParamType.gt){
			return cb.gt(path, value);
		}
		
		if(filterParam.type() == FilterParamType.le){
			return cb.le(path, value);
		}
		
		if(filterParam.type() == FilterParamType.lt){
			return cb.lt(path, value);
		}
		
		return null;
	}
	
	/**
	 * 获取比较查询条件
	 * @param value
	 * @param root
	 * @param cb
	 * @return
	 */
	private Predicate getComparePredicate(Object value, Root<?> root, CriteriaBuilder cb){
		
		Path<Object> path = root.get(key);
		
		Class<? extends Object> javaType = path.getJavaType();
		
		if(filterParam.type() == FilterParamType.greaterThan){
			
			if(javaType.isAssignableFrom(String.class)) return cb.greaterThan(root.get(key), (String) value);
			
			else if(javaType.isAssignableFrom(Date.class)) return cb.greaterThan(root.get(key), (Date) value);
			
			else if(javaType.isAssignableFrom(java.sql.Date.class)) return cb.greaterThan(root.get(key), (java.sql.Date) value);
			
			else if(javaType.isAssignableFrom(DateTime.class)) return cb.greaterThan(root.get(key), (DateTime) value);
			
			else if(javaType.isAssignableFrom(Timestamp.class)) return cb.greaterThan(root.get(key), (Timestamp) value);
		}
		
		else if(filterParam.type() == FilterParamType.greaterThanOrEqualTo){
			
			if(javaType.isAssignableFrom(String.class)) return cb.greaterThanOrEqualTo(root.get(key), (String) value);
			
			else if(javaType.isAssignableFrom(Date.class)) return cb.greaterThanOrEqualTo(root.get(key), (Date) value);
			
			else if(javaType.isAssignableFrom(java.sql.Date.class)) return cb.greaterThanOrEqualTo(root.get(key), (java.sql.Date) value);
			
			else if(javaType.isAssignableFrom(DateTime.class)) return cb.greaterThanOrEqualTo(root.get(key), (DateTime) value);
			
			else if(javaType.isAssignableFrom(Timestamp.class)) return cb.greaterThanOrEqualTo(root.get(key), (Timestamp) value);
		}
		
		else if(filterParam.type() == FilterParamType.lessThan){
			
			if(javaType.isAssignableFrom(String.class)) return cb.lessThan(root.get(key), (String) value);
			
			else if(javaType.isAssignableFrom(Date.class)) return cb.lessThan(root.get(key), (Date) value);
			
			else if(javaType.isAssignableFrom(java.sql.Date.class)) return cb.lessThan(root.get(key), (java.sql.Date) value);
			
			else if(javaType.isAssignableFrom(DateTime.class)) return cb.lessThan(root.get(key), (DateTime) value);
			
			else if(javaType.isAssignableFrom(Timestamp.class)) return cb.lessThan(root.get(key), (Timestamp) value);
		}
		
		else if(filterParam.type() == FilterParamType.lessThanOrEqualTo){
			
			if(javaType.isAssignableFrom(String.class)) return cb.lessThanOrEqualTo(root.get(key), (String) value);
			
			else if(javaType.isAssignableFrom(Date.class)) return cb.lessThanOrEqualTo(root.get(key), (Date) value);
			
			else if(javaType.isAssignableFrom(java.sql.Date.class)) return cb.lessThanOrEqualTo(root.get(key), (java.sql.Date) value);
			
			else if(javaType.isAssignableFrom(DateTime.class)) return cb.lessThanOrEqualTo(root.get(key), (DateTime) value);
			
			else if(javaType.isAssignableFrom(Timestamp.class)) return cb.lessThanOrEqualTo(root.get(key), (Timestamp) value);
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
	
}
