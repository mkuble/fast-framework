package com.fast.framework.data.jpa.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.fast.framework.commons.bean.FastPage;
import com.fast.framework.data.jpa.DynamicExecuteSqlBuilder;
import com.fast.framework.data.jpa.specification.BaseFilterParamSpecification;

@NoRepositoryBean
public interface BaseRepository<T ,ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T>{
	
	/**
	 * 获取订单SQL构建处理器
	 * @return
	 */
	DynamicExecuteSqlBuilder getExecuteSqlBuilder();
	
	/**
	 * 获取实体manager
	 * @return
	 */
	EntityManager getEntityManager();
	
    /**
     * 删除实体
     *
     * @param entityClass 实体类
     * @param entityid    实体id
     */
    void delete(Class<T> entityClass, Object entityid);

    /**
     * 批量删除实体
     *
     * @param entityClass 实体类
     * @param entityids   实体id数组
     */
    void delete(Class<T> entityClass, Object[] entityids);

    /**
	 * 动态执行update - set参数与where拼接动态生成
	 * @param setParam
	 * @param whereParam
	 * @return
	 */
	int dynamicUpdate(Object setParam, Object whereParam);
	
	/**
	 * 动态执行update - set参数与where拼接动态生成
	 * @param obj
	 * @return
	 */
	int dynamicUpdate(Object obj);

	/**
	 * 动态执行update - set参数与where拼接动态生成
	 * @param paramMap
	 * @return
	 */
	int dynamicUpdate(Map<String, Object> paramMap);
	
	/**
	 * 动态执行update - set参数与where拼接动态生成
	 * @param setParamMap
	 * @param whereParamMap
	 * @return
	 */
	int dynamicUpdate(Map<String, Object> setParamMap, Map<String, Object> whereParamMap);
	
	/**
	 * 删除操作 - 条件动态拼接
	 * @param obj
	 * @return
	 */
	int dynamicDelete(Object obj);

	/**
	 * 删除操作 - 条件动态拼接
	 * @param paramMap
	 * @return
	 */
	int dynamicDelete(Map<String, Object> paramMap);
	
	/**
	 * 修改/删除操作
	 * @param sql
	 * @return
	 */
	int execute(String sql);
	
	/**
	 * 修改/删除操作
	 * @param sql
	 * @param obj
	 * @return
	 */
	int execute(String sql, Object obj);
	
	/**
	 * 修改/删除操作
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	int execute(String sql, Map<String, Object> paramMap);
	
	/**
	 * 修改/删除操作
	 * @param sqlId
	 * @return
	 */
	int executeBySqlId(String sqlId);

	/**
	 * 修改/删除操作
	 * @param sqlId
	 * @param obj
	 * @return
	 */
	int executeBySqlId(String sqlId, Object obj);

	/**
	 * 修改/删除操作
	 * @param sqlId
	 * @param paramMap
	 * @return
	 */
	int executeBySqlId(String sqlId, Map<String, Object> paramMap);
	
	Object findOneBySqlId(String sqlId);
	
	Object findOneBySqlId(String sqlId, Object obj);
	
	Object findOneBySqlId(String sqlId, Map<String, Object> paramMap);

	List<?> findBySqlId(String sqlId);
	
	List<?> findBySqlId(String sqlId, Object obj);
	
	List<?> findBySqlId(String sqlId, Map<String, Object> paramMap);
	
	FastPage findPageBySqlId(String sqlId, FastPage page);
	
	int countBySql(String sql, Map<String, Object> paramMap);
	
	Object getBySql(String sql, Map<String, Object> paramMap, Class<?> resultClass);

	List<?> findBySql(String sql, Object obj,  Class<?> resultClass);
	
	List<?> findBySql(String sql, Map<String, Object> paramMap, Class<?> resultClass);
	
	FastPage findPageBySql(String sql, FastPage page, Class<?> resultClass);
	
	FastPage findPageBySql(String sql, Map<String, Object> paramMap, FastPage page, Class<?> resultClass);
	
	FastPage findPage(BaseFilterParamSpecification<T> filterParamSpecification);
	
}
