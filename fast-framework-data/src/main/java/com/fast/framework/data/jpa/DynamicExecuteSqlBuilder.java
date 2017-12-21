package com.fast.framework.data.jpa;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.fast.framework.commons.utils.FastClassUtils;
import com.fast.framework.commons.utils.FastClassUtils.FieldMethodFilter;
import com.fast.framework.data.exception.DynamicExecuteSqlBuilderException;

/**
 * 动态Update构建器
 * 	-只适用于条件都为相等的update/delete 数据库操作
 * @author lion.chen
 */
public class DynamicExecuteSqlBuilder {

	private static final String sql_str_del = "delete ";
	private static final String sql_str_update = "update ";
	private static final String sql_str_as = " as t";
	private static final String sql_str_set = " set ";
	private static final String sql_str_where = " where ";
	private static final String sql_str_and = " and ";
	
	private static final String sql_str_temp = "t.";
	private static final String sql_str_eq = " = :";
	private static final String sql_str_seg = ", ";
	private static final String sql_str_same_suffix = "1";
	
	private DynamicExecuteSqlBuilder(){}
	
	private String entityName;
	private String tableName;
	
	// 主键
	private Map<String, String> idColumns = new HashMap<String, String>();
	
	// entity.field - table.column
	private Map<String, String> columns = new HashMap<String, String>();
	
	/**
	 * 获取动态SQL构建器
	 * @param clazz
	 * @return
	 */
	public static DynamicExecuteSqlBuilder get(Class<?> clazz){

		if(clazz == null){
			throw new DynamicExecuteSqlBuilderException("DynamicUpdateEntity.get ==>> clazz is null!");	
		}
		
		Entity entity = clazz.getAnnotation(Entity.class);
		if(entity == null){
			throw new DynamicExecuteSqlBuilderException("DynamicUpdateEntity.get [" + clazz + "]==>> not's Entity!");
		}
		
		String entityName = entity.name();
		if(StringUtils.isBlank(entityName)) entityName = clazz.getSimpleName();
		
		Table table = clazz.getAnnotation(Table.class);
		String tableName = entityName;
		if(table != null){
			String tempTableName = table.name();
			if(StringUtils.isNotBlank(tempTableName)) tableName = tempTableName;
		}
		
		if(StringUtils.isBlank(tableName)){
			throw new DynamicExecuteSqlBuilderException("DynamicUpdateEntity.get ==>> tableName is empty!");
		}
		
		DynamicExecuteSqlBuilder dynamicUpdateEntity = new DynamicExecuteSqlBuilder();
		dynamicUpdateEntity.entityName = entityName;
		dynamicUpdateEntity.tableName = tableName.toLowerCase();
		
		buildColumnFields(clazz, dynamicUpdateEntity);
		
		if(dynamicUpdateEntity.columns.isEmpty()){
			throw new DynamicExecuteSqlBuilderException("DynamicUpdateEntity.get [tableName:" + tableName + ", entityName:" + entityName + "] ==>> columns is empty!]");
		}
		
		return dynamicUpdateEntity;
	}
	
	/**
	 * 构建mapping数据
	 * @param clazz
	 * @param dynamicUpdateEntity
	 */
	private static final void buildColumnFields(Class<?> clazz, DynamicExecuteSqlBuilder dynamicUpdateEntity){
		
		FastClassUtils.findFieldGetMethods(clazz, new FieldMethodFilter() {
			
			public boolean valid(Field f, Method m) {
				
				String fieldName = f.getName();
				int fieldModifiers = f.getModifiers();
				String columnName = fieldName;
				Id id = null;
				Column column = null;
				
				// 获取Id
				if(f.isAnnotationPresent(Id.class)){
					id = f.getAnnotation(Id.class);
				}else if(m != null && m.isAnnotationPresent(Id.class)){
					id = m.getAnnotation(Id.class);
				}
				
				// 获取Column
				if(f.isAnnotationPresent(Column.class)){
					column = f.getAnnotation(Column.class);
				}else if(m != null && m.isAnnotationPresent(Column.class)){
					column = m.getAnnotation(Column.class);
				}
				
				// 获取 columnName
				if(column != null){
					columnName = column.name();
					if(StringUtils.isBlank(columnName)) columnName = fieldName;
				}
				
				if(id != null){
					dynamicUpdateEntity.idColumns.put(fieldName, columnName);
				}else if(column != null){
					dynamicUpdateEntity.columns.put(fieldName, columnName);
				}
				
				// 如果为非静态属性 并且存在get方法则断定为数据库字段与实体属性名称一致
				else if(!Modifier.isStatic(fieldModifiers) && m != null && !Modifier.isStatic(m.getModifiers())){
					dynamicUpdateEntity.columns.put(fieldName, columnName);
				}
				
				return false;
			}
		});
		
	}
	
	/**
	 * 获取where部分
	 * @param whereParam
	 * @param isIdCheck - 是否坚持id主键
	 * @return
	 */
	private Map<String, String> getWhere(Map<String, Object> whereParam, boolean isIdCheck){
		
		Map<String, String> whereParamMap = new HashMap<String, String>();
		
		Iterator<String> iterator = whereParam.keySet().iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			if(idColumns.containsKey(key)){
				whereParamMap.put(key, idColumns.get(key));
			}
			else if(!isIdCheck && columns.containsKey(key)){
				whereParamMap.put(key, columns.get(key));
			}
		}
		
		return whereParamMap;
	}
	
	/**
	 * 获取set部分
	 * @param setParam
	 * @return
	 */
	private Map<String, String> getSet(Map<String, Object> setParam){
		
		Map<String, String> whereParamMap = new HashMap<String, String>();
		
		Iterator<String> iterator = setParam.keySet().iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			if(columns.containsKey(key)){
				whereParamMap.put(key, columns.get(key));
			}
		}
		
		return whereParamMap;
	}
	
	/**
	 * 假如set参数与where参数存在相同key
	 * @param setParamMap
	 * @param whereParamMap
	 * @return
	 */
	private Map<String, Object> getSameParam(Map<String, Object> setParamMap, Map<String, Object> whereParamMap){
		
		Map<String, Object> sameParamMap = new HashMap<String, Object>();
		
		if(setParamMap != null && whereParamMap != null && !setParamMap.isEmpty() && !whereParamMap.isEmpty()){
			Iterator<String> iterator = whereParamMap.keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				if(setParamMap.containsKey(key)){
					sameParamMap.put(key, whereParamMap.get(key));
				}
			}
		}
		
		return sameParamMap;
	}
	
	/**
	 * 构建update执行SQL 自定义参数集
	 * @param paramMap
	 * @param whereParamMap
	 * @return
	 */
	public String getUpdateSql(Map<String, Object> paramMap, Map<String, Object> whereParamMap){
		
		if(paramMap == null || paramMap.isEmpty()){
			throw new DynamicExecuteSqlBuilderException("DynamicUpdateEntity.sql [tableName:" + tableName + ", entityName:" + entityName + "] ==>> paramMap is empty!");
		}
		
		Map<String, String> setParams = getSet(paramMap);
		Map<String, String> whereParams = getWhere(whereParamMap, false);
		
		Map<String, Object> sameParam = getSameParam(paramMap, whereParamMap);
		if(!sameParam.isEmpty()){
			
			Iterator<String> iterator = sameParam.keySet().iterator();
			
			while(iterator.hasNext()){
				
				String key = iterator.next();
				Object object = whereParamMap.get(key);
				String newKey = new StringBuffer(key).append(sql_str_same_suffix).toString();
				
				// 改变参数key
				whereParamMap.put(newKey, object);
				whereParamMap.remove(key);
				
				// 改变条件key
				String columnName = whereParams.get(key);
				whereParams.put(newKey, columnName);
				whereParams.remove(key);
			}
		}
		
		return buildUpdateSql(setParams, whereParams);
	}
	
	/**
	 * 构建update执行SQL 根据主键生成where条件（参数集合必须包含主键值）
	 * @param paramMap
	 * @return
	 */
	public String getUpdateSql(Map<String, Object> paramMap){
		
		if(paramMap == null || paramMap.isEmpty()){
			throw new DynamicExecuteSqlBuilderException("DynamicUpdateEntity.sql [tableName:" + tableName + ", entityName:" + entityName + "] ==>> paramMap is empty!");
		}
		
		Map<String, String> setParams = getSet(paramMap);
		Map<String, String> whereParams = getWhere(paramMap, true);
		
		return buildUpdateSql(setParams, whereParams);
	}

	/**
	 * 构建 update SQL
	 * @param setParams (entity.field - table.column)
	 * @param whereParams (entity.field - table.column)
	 * @return
	 */
	public String buildUpdateSql(Map<String, String> setParams, Map<String, String> whereParams){
		
		if(setParams == null || setParams.isEmpty()){
			throw new DynamicExecuteSqlBuilderException("DynamicUpdateEntity.getQl [tableName:" + tableName + ", entityName:" + entityName + "] ==>> setParams is empty!");
		}
		
		if(whereParams == null || whereParams.isEmpty()){
			throw new DynamicExecuteSqlBuilderException("DynamicUpdateEntity.getQl [tableName:" + tableName + ", entityName:" + entityName + "] ==>> whereParams is empty!");
		}
		
		List<String> setSqls = new ArrayList<String>();
		List<String> whereSqls = new ArrayList<String>();
		
		setParams.keySet().forEach(new Consumer<String>() {
			@Override
			public void accept(String k) {
				setSqls.add(new StringBuffer(sql_str_temp).append(setParams.get(k)).append(sql_str_eq).append(k).toString());
			}
		});
		
		whereParams.keySet().forEach(new Consumer<String>() {
			@Override
			public void accept(String k) {
				whereSqls.add(new StringBuffer(sql_str_temp).append(whereParams.get(k)).append(sql_str_eq).append(k).toString());
			}
		});
		
		// SQL: update admin set login_name = :loginName, user_name = :userName where id = :id
		// SQL: update admin set loginName = :loginName, userName = :userName where id = :id
		
		String sql = new StringBuffer(sql_str_update).append(tableName).append(sql_str_as).append(sql_str_set).append(StringUtils.join(setSqls, sql_str_seg))
				.append(sql_str_where).append(StringUtils.join(whereSqls, sql_str_and)).toString();
		
		return sql;
	}
	
	/**
	 * 构建Delete执行SQL
	 * @param paramObj
	 * @return
	 */
	public String getDelSql(Object paramObj){

		Map<String, Object> paramMap = FastClassUtils.obj2Map(paramObj);
		
		Map<String, String> whereParams = getWhere(paramMap, false);
		
		return buildDelSql(whereParams);
	}
	
	/**
	 * 构建Delete执行SQL
	 * @param paramMap
	 * @return
	 */
	public String getDelSql(Map<String, Object> paramMap){

		Map<String, String> whereParams = getWhere(paramMap, false);
		
		return buildDelSql(whereParams);
	}
	
	/**
	 * 构建Delete执行SQL
	 * @param whereParams (entity.field - table.column)
	 * @return
	 */
	public String buildDelSql(Map<String, String> whereParams){
		
		List<String> whereSqls = new ArrayList<String>();
		
		whereParams.keySet().forEach(new Consumer<String>() {
			@Override
			public void accept(String k) {
				whereSqls.add(new StringBuffer(sql_str_temp).append(whereParams.get(k)).append(sql_str_eq).append(k).toString());
			}
		});
		
		String sql = new StringBuffer(sql_str_del).append(tableName).append(sql_str_as).append(sql_str_where).append(StringUtils.join(whereSqls, sql_str_and)).toString();
		
		return sql;
	}
	
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
	
}
