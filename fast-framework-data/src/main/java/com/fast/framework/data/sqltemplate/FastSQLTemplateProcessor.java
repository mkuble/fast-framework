package com.fast.framework.data.sqltemplate;

import java.util.Map;

import com.fast.framework.data.sqltemplate.domain.FastSqlMap;

public interface FastSQLTemplateProcessor {

	public abstract Map<String, FastSqlMap> getSQLMap();
	
}
