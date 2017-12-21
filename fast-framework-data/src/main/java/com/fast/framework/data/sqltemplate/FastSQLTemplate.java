package com.fast.framework.data.sqltemplate;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fast.framework.commons.utils.FastClassUtils;
import com.fast.framework.commons.utils.FastStringUtils;
import com.fast.framework.data.sqltemplate.domain.FastSqlMap;
import com.fast.framework.data.sqltemplate.domain.FastSqlOpType;
import com.fast.framework.data.sqltemplate.exception.FastSQLTemplateException;
import com.fast.framework.data.sqltemplate.impl.XMLSQLTemplateProcessor;

/**
 * SQL 模板
 * @author lion.chen
 * 2017年11月17日 下午3:40:51
 */
public class FastSQLTemplate{

	private static final String sort_end_mark = "</sort>";
	private static final String mySql_count_sql_tpl = "select count(1) from (${sql}) as temp_count";
	
	private static final Map<String, FastSqlMap> CACHE = new Hashtable<String, FastSqlMap>();
	
	private static FastSQLTemplateProcessor sqlTemplateProcessor = new XMLSQLTemplateProcessor();
	
	static{
		init();
	}
	
	public static void init(){
		CACHE.putAll(sqlTemplateProcessor.getSQLMap());
	}
	
	private static FastSqlMap getSqlMap(String sqlId){
		return CACHE.get(sqlId);
	}
	
	/**
	 * 获取countSql
	 * @param sql
	 * @return
	 */
	public static String getCountSql(String sql){
		
		Pattern compile = Pattern.compile("(<sort>)|(</sort>)");
		Matcher matcher = compile.matcher(sql);
		
		List<String> tempStrs = new ArrayList<String>();
		
		int lastStart = 0;
		boolean isStart = true;
		int i = 0;
		while(matcher.find()){
			
			int start = matcher.start();
			int end = matcher.end();
			if(end >= sql.length()) end = sql.length() - 1;
			
			// <sort>
			if(i % 2 == 0){
				if(isStart){
					String sqlItem = sql.substring(0, start);
					tempStrs.add(sqlItem);
					isStart = false;
				}else{
					String sqlItem = sql.substring(lastStart, start);
					tempStrs.add(sqlItem);
				}
			}
			// </sort>
			else{
				lastStart = end;
			}
			i++;
		}
		
		String sqlContent = FastStringUtils.join(tempStrs, FastStringUtils.EMPTY);
		
		if(FastStringUtils.isEmpty(sqlContent)){
			sqlContent = sql;
		}else{
			int last_sort_end_mark_index = sql.lastIndexOf(sort_end_mark);
			if(sql.length() - sort_end_mark.length() > last_sort_end_mark_index){
				sqlContent = sqlContent + sql.substring(last_sort_end_mark_index + sort_end_mark.length());
			}
		}
		
		sqlContent = FastStringUtils.trimToEmpty(sqlContent);
		
		String tempSql = FastStringUtils.$format(mySql_count_sql_tpl, "sql", sqlContent);
		
		return tempSql;
	}
	
	/**
	 * 清除sort标记
	 * @param sql
	 * @return
	 */
	public static String cleanSortMark(String sql){
		
		String tempSql = sql;
		
		if(FastStringUtils.isNotEmpty(tempSql)){
			tempSql = tempSql.replaceAll("(<sort>)|(</sort>)", " ");
		}
		
		return tempSql;
	}
	
	/**
	 * 获取执行SQL
	 * @param sqlId
	 * @param paramMap
	 * @return
	 * @throws FastSQLTemplateException
	 */
	public static String getExecuteSql(String sqlId, Map<String, Object> paramMap) throws FastSQLTemplateException{
		
		return getSql(sqlId, FastSqlOpType.EXECUTE, paramMap);
	}
	
	/**
	 * 获取查询SQL
	 * @param sqlId
	 * @param obj
	 * @return
	 * @throws FastSQLTemplateException
	 */
	public static String getQuerySql(String sqlId, Object obj) throws FastSQLTemplateException{
		
		Map<String, Object> paramMap = FastClassUtils.obj2Map(obj);
		
		return getSql(sqlId, FastSqlOpType.QUERY, paramMap);
	}
	
	/**
	 * 获取查询SQL
	 * @param sqlId
	 * @param paramMap
	 * @return
	 * @throws FastSQLTemplateException
	 */
	public static String getQuerySql(String sqlId, Map<String, Object> paramMap) throws FastSQLTemplateException{
		
		return getSql(sqlId, FastSqlOpType.QUERY, paramMap);
	}
	
	/**
	 * 获取SQL
	 * @param sqlId
	 * @param aqlOpType
	 * @param paramMap
	 * @return
	 * @throws FastSQLTemplateException
	 */
	public static String getSql(String sqlId, FastSqlOpType aqlOpType, Map<String, Object> paramMap) throws FastSQLTemplateException{
		
		FastSqlMap sqlMap = getSqlMap(sqlId, FastSqlOpType.QUERY);
		
		return sqlMap.getSql(paramMap);
	}
	
	/**
	 * 获取SQL模板
	 * @param sqlId
	 * @param aqlOpType
	 * @return
	 * @throws FastSQLTemplateException
	 */
	public static FastSqlMap getSqlMap(String sqlId, FastSqlOpType aqlOpType) throws FastSQLTemplateException{
		
		FastSqlMap sqlMap = getSqlMap(sqlId);
		
		if(sqlMap == null){
			throw new FastSQLTemplateException("not find sqlId[" + sqlId + "]");
		}
		
		if(sqlMap.getSqlOpType() != aqlOpType){
			throw new FastSQLTemplateException("sqlId[" + sqlId + "][" + sqlMap.getSqlOpType() + "]  not is " + aqlOpType + "");
		}
		
		return sqlMap;
	}
	
}
