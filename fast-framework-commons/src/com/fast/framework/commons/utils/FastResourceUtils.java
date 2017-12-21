package com.fast.framework.commons.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * 资源配置工具
 * 
 * @author lion.chen
 * @version 1.0.0 2017年11月27日 下午5:44:11
 */
public class FastResourceUtils {

	/**
	 * 获取资源
	 * @param locationPattern
	 * @return
	 */
	public static List<Resource> getResources(String locationPattern){
		
		List<Resource> r = new ArrayList<Resource>();
		
		PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
		
		try {
			Resource[] resources = resourceLoader.getResources(locationPattern);
			if(resources != null && resources.length > 0){
				for(Resource resource : resources){
					if(resource != null) r.add(resource);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return r;
	}
	
}
