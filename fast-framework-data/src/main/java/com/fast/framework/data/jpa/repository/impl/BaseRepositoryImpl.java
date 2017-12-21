package com.fast.framework.data.jpa.repository.impl;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.fast.framework.commons.bean.FastPage;
import com.fast.framework.commons.utils.FastClassUtils;
import com.fast.framework.commons.utils.FastStringUtils;
import com.fast.framework.data.jpa.DynamicExecuteSqlBuilder;
import com.fast.framework.data.jpa.repository.BaseRepository;
import com.fast.framework.data.jpa.specification.BaseFilterParamSpecification;
import com.fast.framework.data.sqltemplate.FastSQLTemplate;
import com.fast.framework.data.sqltemplate.domain.FastSqlMap;
import com.fast.framework.data.sqltemplate.domain.FastSqlOpType;

public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

	// 通过构造方法初始化EntityManager
	private final EntityManager entityManager;

	private DynamicExecuteSqlBuilder executeSqlBuilder;

	public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager);
		this.entityManager = entityManager;
		this.executeSqlBuilder = DynamicExecuteSqlBuilder.get(domainClass);
	}

	@Override
	public DynamicExecuteSqlBuilder getExecuteSqlBuilder() {
		return executeSqlBuilder;
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public void delete(Class<T> entityClass, Object entityid) {
		delete(entityClass, new Object[] { entityid });
	}

	@Override
	public void delete(Class<T> entityClass, Object[] entityids) {
		for (Object id : entityids) {
			entityManager.remove(entityManager.getReference(entityClass, id));
		}
	}

	@Override
	public int dynamicUpdate(Object setParam, Object whereParam) {
		
		Map<String, Object> setParamMap = FastClassUtils.obj2Map(setParam);
		Map<String, Object> whereParamMap = FastClassUtils.obj2Map(whereParam);

		return dynamicUpdate(setParamMap, whereParamMap);
	}

	@Override
	public int dynamicUpdate(Object obj) {
		return dynamicUpdate(FastClassUtils.obj2Map(obj));
	}

	@Override
	public int dynamicUpdate(Map<String, Object> paramMap) {
		
		String updateSql = executeSqlBuilder.getUpdateSql(paramMap);

		return execute(updateSql, paramMap);
	}

	@Override
	public int dynamicUpdate(Map<String, Object> setParamMap, Map<String, Object> whereParamMap) {
		
		String updateSql = executeSqlBuilder.getUpdateSql(setParamMap, whereParamMap);

		return execute(updateSql, whereParamMap);
	}

	@Override
	public int dynamicDelete(Object obj) {
		return dynamicDelete(FastClassUtils.obj2Map(obj));
	}

	@Override
	public int dynamicDelete(Map<String, Object> paramMap) {
		
		String deleteSql = executeSqlBuilder.getDelSql(paramMap);

		return execute(deleteSql, paramMap);
	}

	@Override
	public int execute(String sql) {
		return execute(sql, null);
	}

	@Override
	public int execute(String sql, Object obj) {
		
		Map<String, Object> paramMap = FastClassUtils.obj2Map(obj);

		return execute(sql, paramMap);
	}

	@Override
	public int execute(String sql, Map<String, Object> paramMap) {
		
		Query query = entityManager.createNativeQuery(sql);

		_builderQueryParam(query, paramMap);

		return query.executeUpdate();
	}

	@Override
	public int executeBySqlId(String sqlId) {
		
		return execute(sqlId, null);
	}

	@Override
	public int executeBySqlId(String sqlId, Object obj) {
		
		Map<String, Object> paramMap = FastClassUtils.obj2Map(obj);

		return executeBySqlId(sqlId, paramMap);
	}

	@Override
	public int executeBySqlId(String sqlId, Map<String, Object> paramMap) {
		
		FastSqlMap sqlMap = FastSQLTemplate.getSqlMap(sqlId, FastSqlOpType.EXECUTE);
		
		String sql = sqlMap.getSql(paramMap);
		
		Query query = entityManager.createNativeQuery(sql);

		_builderQueryParam(query, paramMap);

		return query.executeUpdate();
	}

	@Override
	public Object findOneBySqlId(String sqlId) {
		
		return findOneBySqlId(sqlId, null);
	}

	@Override
	public Object findOneBySqlId(String sqlId, Object obj) {
		
		Map<String, Object> paramMap = FastClassUtils.obj2Map(obj);
		
		return findOneBySqlId(sqlId, paramMap);
	}

	@Override
	public Object findOneBySqlId(String sqlId, Map<String, Object> paramMap) {
		
		List<?> list = findBySqlId(sqlId, paramMap);
		
		return FastStringUtils.isNotEmpty(list) ? list.get(0) : null;
	}

	@Override
	public List<?> findBySqlId(String sqlId) {
		return findBySqlId(sqlId, null);
	}

	@Override
	public List<?> findBySqlId(String sqlId, Object param) {
		
		Map<String, Object> paramMap = FastClassUtils.obj2Map(param);
		
		return findBySqlId(sqlId, paramMap);
	}

	@Override
	public List<?> findBySqlId(String sqlId, Map<String, Object> paramMap) {
		
		FastSqlMap sqlMap = FastSQLTemplate.getSqlMap(sqlId, FastSqlOpType.QUERY);
		
		Class<?> resultClass = sqlMap.getResultClass();
		
		String sql = sqlMap.getSql(paramMap);
		
		return findBySql(sql, paramMap, resultClass);
	}

	@Override
	public FastPage findPageBySqlId(String sqlId, FastPage page) {
		
		Map<String, Object> paramMap = FastClassUtils.obj2Map(page);
		
		FastSqlMap sqlMap = FastSQLTemplate.getSqlMap(sqlId, FastSqlOpType.QUERY);
		
		Class<?> resultClass = sqlMap.getResultClass();
		
		String sql = sqlMap.getSql(paramMap);
	
		return findPageBySql(sql, paramMap, page, resultClass);
	}

	@Override
	public int countBySql(String sql, Map<String, Object> paramMap) {
		
		int r = 0;

		String tempSql = FastSQLTemplate.getCountSql(sql);
		
		Query query = entityManager.createNativeQuery(tempSql);
		
		_builderQueryParam(query, paramMap);
		
		r = ((Number) query.getSingleResult()).intValue();
		
		return r;
	}

	@Override
	public Object getBySql(String sql, Map<String, Object> paramMap, Class<?> resultClass) {
		
		List<?> list = findBySql(sql, paramMap, resultClass);
		
		return FastStringUtils.isNotEmpty(list) ? list.get(0) : null;
	}

	@Override
	public List<?> findBySql(String sql, Object obj, Class<?> resultClass) {
		
		Map<String, Object> paramMap = FastClassUtils.obj2Map(obj);
		
		return findBySql(sql, paramMap, resultClass);
	}

	@Override
	public List<?> findBySql(String sql, Map<String, Object> paramMap, Class<?> resultClass) {
		
		String tempSql = FastSQLTemplate.cleanSortMark(sql);
		
		Query query = entityManager.createNativeQuery(tempSql, resultClass);
		
		_builderQueryParam(query, paramMap);
		
		List<?> resultList = (List<?>)query.getResultList();
		
		return resultList;
	}

	@Override
	public FastPage findPageBySql(String sql, FastPage page, Class<?> resultClass) {
		
		Map<String, Object> paramMap = FastClassUtils.obj2Map(page);
		
		return findPageBySql(sql, paramMap, page, resultClass);
	}

	@Override
	@SuppressWarnings("unchecked")
	public FastPage findPageBySql(String sql, Map<String, Object> paramMap, FastPage page, Class<?> resultClass) {
		
		int total = 0;
		
		String tempSql = FastSQLTemplate.getCountSql(sql);
		
		tempSql = FastSQLTemplate.cleanSortMark(sql);
		
		Query query = entityManager.createNativeQuery(tempSql, resultClass);
		
		int firstResult = (page.page - 1) * page.pageSize;
		
		_builderQueryParam(query, paramMap, firstResult, page.pageSize);
		
		List<Object> resultList = query.getResultList();
		
		if(page.page == 1 && resultList.size() < page.pageSize){
			total = resultList.size();
		}else{
			total = countBySql(sql, paramMap);
		}
		
		page.setData((List<Object>) resultList, total);
		
		return null;
	}

	@Override
	public FastPage findPage(BaseFilterParamSpecification<T> filterParamSpecification) {
		
		Page<T> page = findAll(filterParamSpecification, filterParamSpecification.getPageRequest());
		
		return FastPage.getPage(page.getNumber() + 1, page.getSize(), page.getContent(), (int) page.getTotalElements());
	}
	
	/**
	 * 设置参数
	 * @param query
	 * @param paramMap
	 */
	private void _builderQueryParam(Query query, Map<String, Object> paramMap){
		_builderQueryParam(query, paramMap, null, null);
	}

	/**
	 * 设置查询参数
	 * @param query
	 * @param paramMap
	 * @param firstResult
	 * @param maxResults
	 */
	private void _builderQueryParam(Query query, Map<String, Object> paramMap, Integer firstResult, Integer maxResults){
		
		if(query != null && paramMap != null && !paramMap.isEmpty()){
			
			Set<Parameter<?>> parameters = query.getParameters();
			
			if(parameters != null && !parameters.isEmpty()){
				Iterator<Parameter<?>> iterator = parameters.iterator();
				while (iterator.hasNext()) {
					Parameter<?> next = iterator.next();
					String key = next.getName();
					if (FastStringUtils.isNotBlank(key)) {
						query.setParameter(key, paramMap.get(key));
					}
				}
			}
			
			if(firstResult != null && maxResults != null){
				query.setFirstResult(firstResult);
				query.setMaxResults(maxResults);
			}
			
		}
	}
	
}
