package com.fast.framework.data.sqltemplate.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;
import org.xml.sax.InputSource;

import com.fast.framework.commons.utils.FastResourceUtils;
import com.fast.framework.data.sqltemplate.FastSQLTemplateProcessor;
import com.fast.framework.data.sqltemplate.domain.FastSqlMap;
import com.fast.framework.data.sqltemplate.domain.FastSqlOpType;

/**
 * XML SQL 模板处理器
 * @author lion.chen
 */
@SuppressWarnings("rawtypes")
public class XMLSQLTemplateProcessor implements FastSQLTemplateProcessor{

	private static final Logger LOG = Logger.getLogger(XMLSQLTemplateProcessor.class);
	
	private static final String SQL_ELEMENT_NAMESPACE = "namespace";
	private static final String SQL_ELEMENT_EXECUTE = "execute";
	private static final String SQL_ELEMENT_QUERY = "query";
	
	private static final String SQL_ATTR_ID = "id";
	private static final String SQL_ATTR_RESULT_CLASS = "resultClass";
	
	private String sqlTemplatePath = "classpath*:hbm/**/*.xml";
	
	/**
	 * 获取SQL模板
	 */
	public Map<String, FastSqlMap> getSQLMap() {
		
		if(LOG.isInfoEnabled()){
			LOG.info("===========Get SQLTemplate by XMLSQLTemplateProcessor");
		}
		
		try{
			List<Resource> resources = FastResourceUtils.getResources(sqlTemplatePath);
			return _builderSQLMap(resources);
		}catch(Exception e){
			LOG.error("===========XMLSQLTemplateProcessor Get Resource error, message:" + e.getMessage());
		}
		
		return new HashMap<String, FastSqlMap>();
	}
	
	/**
	 * 根据多个资源解析SQL模板
	 * @param resources
	 * @return
	 * @throws Exception
	 */
	private Map<String, FastSqlMap> _builderSQLMap(List<Resource> resources) throws Exception{
		
		Map<String, FastSqlMap> resultMap = new HashMap<String, FastSqlMap>();
		
		for(Resource resource : resources){
			resultMap.putAll(_builderSQLMap(resource));
		}
		
		return resultMap;
	}
	
	/**
	 * 根据资源解析SQL模板
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	private Map<String, FastSqlMap> _builderSQLMap(Resource resource) throws Exception{
		
		Map<String, FastSqlMap> resultMap = new HashMap<String, FastSqlMap>();
		
		InputSource inputSource = new InputSource(resource.getInputStream());
		
		SAXReader saxReader = new SAXReader();
		
		Document read = saxReader.read(inputSource);
		
		Element rootElement = read.getRootElement();
		
		String namespace = rootElement.attributeValue(SQL_ELEMENT_NAMESPACE);
		
		if(StringUtils.isNotEmpty(namespace)){
			resultMap.putAll(parserSqlMap(rootElement, namespace, FastSqlOpType.QUERY));
			resultMap.putAll(parserSqlMap(rootElement, namespace, FastSqlOpType.EXECUTE));
		}else{
			LOG.error("===========XMLSQLTemplateProcessor._builderSQLMap namespace is empty!");
		}
		
		return resultMap;
	}
	
	/**
	 * 转换SQL模板
	 * @param root
	 * @param namespace
	 * @param sqlOpType
	 * @return
	 */
	private Map<String, FastSqlMap> parserSqlMap(Element root, String namespace, FastSqlOpType sqlOpType){
		
		Map<String, FastSqlMap> _parserSqlMap = new HashMap<String, FastSqlMap>();
		
		try {
			_parserSqlMap = _parserSqlMap(root, namespace, sqlOpType);
		} catch (Exception e) {
			LOG.error("===========XMLSQLTemplateProcessor._parserSqlMap error, message:" + e.getMessage());
		}
		
		return _parserSqlMap;
	}
	
	/**
	 * XML读取SQL模板
	 * @param root
	 * @param namespace
	 * @param sqlOpType
	 * @return
	 * @throws Exception
	 */
	private Map<String, FastSqlMap> _parserSqlMap(Element root, String namespace, FastSqlOpType sqlOpType) throws Exception{
		
		Map<String, FastSqlMap> resultMap = new HashMap<String, FastSqlMap>();
			
		String elementName = sqlOpType == FastSqlOpType.EXECUTE ? SQL_ELEMENT_EXECUTE : SQL_ELEMENT_QUERY;
		Iterator elementIterator = root.elementIterator(elementName);
		
		String id = null;
		String content = null;
		String resultClass = null;
		while(elementIterator.hasNext()){
			Element next = (Element)elementIterator.next();
			if(next != null){
				
				Attribute attr_id = next.attribute(SQL_ATTR_ID);
				
				if(attr_id == null){
					continue;
				}
				
				id = attr_id.getText();
				
				content = next.getText();
				
				if(StringUtils.isEmpty(id)){
					LOG.error("===========XMLSQLTemplateProcessor._parserSqlMap id is empty");
					continue;
				}
				
				if(StringUtils.isEmpty(content)){
					LOG.error("===========XMLSQLTemplateProcessor._parserSqlMap content is empty");
					continue;
				}
				
				Attribute attr_resultClass = next.attribute(SQL_ATTR_RESULT_CLASS);
				if(attr_resultClass != null){
					resultClass = attr_resultClass.getText();
				}
				
				id = new StringBuffer(namespace).append(".").append(id).toString();
				
				if(resultMap.containsKey(id)){
					LOG.error("===========XMLSQLTemplateProcessor._parserSqlMap id[" + id + "] in the Same!");
					continue;
				}
				
				FastSqlMap querySqlMap = new FastSqlMap(content, sqlOpType);
				querySqlMap.setResultClass(resultClass);
				
				resultMap.put(id, querySqlMap);
			}
		}
		
		return resultMap;
	}
	
	public void setSqlTemplatePath(String sqlTemplatePath) {
		this.sqlTemplatePath = sqlTemplatePath;
	}

}
