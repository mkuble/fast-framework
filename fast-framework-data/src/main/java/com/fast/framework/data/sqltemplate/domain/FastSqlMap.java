package com.fast.framework.data.sqltemplate.domain;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fast.framework.commons.utils.FastStringUtils;
import com.fast.framework.component.freemarker.utils.FastFreemarkerTemplateEngine;

/**
 * SQLMap
 * @author lion.chen
 * 2017年11月17日 下午3:39:08
 */
public class FastSqlMap implements Serializable{

	private static final long serialVersionUID = 4330895586682342880L;
	
	protected FastSqlOpType sqlOpType = FastSqlOpType.QUERY;
	
	protected String sqlContext;
	
	protected Class<?> resultClass;
	
	protected boolean isBuilder = true;
	
//	private ResultTransformer resultTransformer;
	
	public FastSqlMap(){}
	
	/**
	 * 解析SQL
	 * @param sqlMap
	 * @return
	 */
	public String getSql(Map<String, Object> paramMap){
		
		String r = FastStringUtils.EMPTY;
		
		try {
			r = FastFreemarkerTemplateEngine.renderFtlStr(getSqlContext(), paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return r;
	}
	
	public FastSqlMap(String sqlContext, FastSqlOpType sqlOpType){
		this.sqlContext = sqlContext;
		this.sqlOpType = sqlOpType;
	}

	public String getSqlContext() {
		return sqlContext;
	}

	public void setSqlContext(String sqlContext) {
		this.sqlContext = sqlContext;
	}
	
	public void setResultClass(Class<?> entityClass) {
		this.resultClass = entityClass;
	}
	
	public void setResultClass(String resultClass) {
		
		if(StringUtils.isEmpty(resultClass)){
			return;
		}
		
		try{
			Class<?> entityClass = Class.forName(resultClass);
			setResultClass(entityClass);
		}catch(Exception e){
			isBuilder = false;
		}
	}

//	private void setResultTransformer(ResultTransformer resultTransformer){
//		this.resultTransformer = resultTransformer;
//	}
//	
//	public ResultTransformer getResultTransformer() {
//		return resultTransformer;
//	}

	public Class<?> getResultClass() {
		return resultClass;
	}

	public FastSqlOpType getSqlOpType() {
		return sqlOpType;
	}

}
