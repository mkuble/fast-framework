package com.fast.framework.component.freemarker.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fast.framework.component.freemarker.domain.FastFreemarkerBeanWrapper;
import com.fast.framework.component.freemarker.method.JoinTemplateMethod;

import freemarker.cache.StringTemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

/**
 * Freemarker模板引擎
 * @author lion.chen
 * 2017年11月17日 下午4:22:00
 */
public class FastFreemarkerTemplateEngine {

	private static Logger LOG = Logger
			.getLogger(FastFreemarkerTemplateEngine.class);

	private static Configuration defaultConfiguration;

	private static Map<String, Object> COMMON_VALUE_MAP = new HashMap<String, Object>();

	static{
		defaultConfiguration = new Configuration();
		defaultConfiguration.setTemplateLoader(new StringTemplateLoader());
		defaultConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
		defaultConfiguration.setObjectWrapper(new FastFreemarkerBeanWrapper());
		defaultConfiguration.setDateFormat("yyyy-MM-dd");
		defaultConfiguration.setTimeFormat("HH:mm:Ss");
		defaultConfiguration.setDateTimeFormat("yyyy-MM-dd HH:mm:Ss");
		defaultConfiguration.setDefaultEncoding("utf-8");
		
		addCommonMethods();
	}
	
	private static final String method_join = "join";
	public static void addCommonMethods(){
		addCommonParams(method_join, new JoinTemplateMethod());
	}
	
	/**
	 * 新增公共属性或者自定义方法
	 * 
	 * @param methodName
	 * @param method
	 */
	public static void addCommonParams(Map<String, Object> map) {

		if (map == null || map.isEmpty()) {
			return;
		}

		synchronized (COMMON_VALUE_MAP) {
			COMMON_VALUE_MAP.putAll(map);
		}
	}

	/**
	 * 新增公共属性或者自定义方法
	 * 
	 * @param methodName
	 * @param method
	 */
	public static void addCommonParams(String paramName, Object paramValue) {
		synchronized (COMMON_VALUE_MAP) {
			COMMON_VALUE_MAP.put(paramName, paramValue);
		}
	}

	/**
	 * 模板引擎入口
	 * @param ftlString
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public static String renderFtlStr(String ftlString,
			Map<String, Object> paramMap) throws Exception {

		if (ftlString.indexOf("$") < 0 && ftlString.indexOf("#") < 0) {
			return ftlString;
		}

		StringWriter stringWriter = new StringWriter();
		BufferedWriter writer = new BufferedWriter(stringWriter);
		
		try {

			StringReader reader = new StringReader(ftlString);
			Template template = new Template(ftlString, reader,
						createDefaultConfiguration());

			Map<String, Object> paramMapEx = new HashMap<String, Object>();
			paramMapEx.putAll(COMMON_VALUE_MAP);
			
			if (paramMap != null) {
				paramMapEx.putAll(paramMap);
			}

			template.process(paramMapEx, writer,
					BeansWrapper.getDefaultInstance());

			writer.flush();

		} catch (Exception e) {
			LOG.error("==============>>>> FreemarkerTemplateEngine.error, message:"
					+ e.getMessage());
			throw e;
		} finally {
			try {
				if (stringWriter != null)
					stringWriter.close();
				if (writer != null)
					writer.close();
			} catch (IOException e) {
			}
		}

		return stringWriter.toString().trim();
	}

	/**
	 * 构建默认freemarker引擎配置
	 * 
	 * @return
	 */
	private static Configuration createDefaultConfiguration() {

		return defaultConfiguration;
	}

}
