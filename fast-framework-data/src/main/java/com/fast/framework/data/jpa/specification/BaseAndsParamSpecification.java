package com.fast.framework.data.jpa.specification;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Sort;

import com.fast.framework.data.jpa.annotation.FilterParam;
import com.fast.framework.data.jpa.bean.FilterParamBean;
import com.fast.framework.data.jpa.bean.PageRequestParam;

/**
 * and条件合并实现
 * @author lion.chen
 * @param <T>
 */
public abstract class BaseAndsParamSpecification<T> extends PageRequestParam implements BaseFilterParamSpecification<T>{

	private List<FilterParamBean> filterParamBeanList = new ArrayList<FilterParamBean>();
	
	private static final Logger LOG = Logger.getLogger(BaseAndsParamSpecification.class);
	
	public BaseAndsParamSpecification() {
		
		Collection<Field> allClassFields = getAllClassFields(this.getClass());
		
		for(Field f : allClassFields){
			
			FilterParam filterParam = f.getAnnotation(FilterParam.class);
			
			if(filterParam != null){
				
				try{
					
					String key = filterParam.key();
					if(StringUtils.isBlank(key)) key = f.getName();
					
					filterParamBeanList.add(new FilterParamBean(f, key, this, filterParam));
					
				}catch(Exception e){
					LOG.error("BaseAndsParamSpecification init error:" + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 获取一个类的所有属性
	 * @param clazz
	 * @return
	 */
	private static Collection<Field> getAllClassFields(Class<?> clazz) {
		Map<String, Field> resutlMap = new LinkedHashMap<String, Field>();
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				resutlMap.put(field.getName(), field);
			}
		}
		return resutlMap.values();
	}
	
	/**
	 * 获取固定排序规则
	 */
	public Sort getFixedSort(){
		
		Sort sort = null;
		
		if(!filterParamBeanList.isEmpty()){
			for(FilterParamBean filterParamBean : filterParamBeanList){
				if(filterParamBean != null && filterParamBean.filterParam.sort()){
					Sort tempSort = new Sort(filterParamBean.filterParam.asc() ? Sort.Direction.ASC : Sort.Direction.DESC, filterParamBean.key);
					if(sort == null){
						sort = tempSort;
					} else{
						sort.and(tempSort);
					}
				}
			}
		}
		
		return sort;
	}
	
	/**
	 * 构建查询条件
	 * @param root
	 * @param query
	 * @param cb
	 * @return
	 */
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb){
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		// 前置条件
		List<Predicate> beforePredicateList = getBeforePredicateList(root, query, cb);
		if(beforePredicateList != null && !beforePredicateList.isEmpty()){
			predicates.addAll(beforePredicateList);
		}
		
		// 内置注解条件
		List<Predicate> predicateList = getPredicateList(root, query, cb);
		if(predicateList != null && !predicateList.isEmpty()){
			predicates.addAll(predicateList);
		}
		
		// 后置条件
		List<Predicate> afterPredicateList = getAfterPredicateList(root, query, cb);
		if(afterPredicateList != null && !afterPredicateList.isEmpty()){
			predicates.addAll(afterPredicateList);
		}
		
		return cb.and(predicates.toArray(new Predicate[predicates.size()]));
	}
	
	/**
	 * 获取前置条件
	 * @return
	 */
	public List<Predicate> getBeforePredicateList(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb){
		return null;
	}
	
	/**
	 * 获取后置条件
	 * @return
	 */
	public List<Predicate> getAfterPredicateList(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb){
		return null;
	}
	
	/**
	 * 处理内置条件
	 * @return
	 */
	private List<Predicate> getPredicateList(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb){
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		List<Predicate> indexParams = new ArrayList<Predicate>();
		List<Predicate> otherParams = new ArrayList<Predicate>();
		List<Predicate> compareParams = new ArrayList<Predicate>();
		List<Predicate> likeParams = new ArrayList<Predicate>();
		
		if(!filterParamBeanList.isEmpty()){
			
			for(FilterParamBean param : filterParamBeanList){
				if(param != null){
					Predicate predicate = param.getPredicate(root, cb);
					if(predicate == null) continue;
					if(param.isLike()) likeParams.add(predicate);
					else if(param.isIndex()) indexParams.add(predicate);
					else if(param.isCompare()) compareParams.add(predicate);
					else otherParams.add(predicate);
				}
			}
		}
		
		// 拼接顺序：索引、基础、比较、like
		predicates.addAll(indexParams);
		predicates.addAll(otherParams);
		predicates.addAll(compareParams);
		predicates.addAll(likeParams);
		
		return predicates;
	}
	
}
