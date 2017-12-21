package com.fast.framework.commons.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FastParamUtils {

	/**
	 * map参数过滤
	 * @param paramMap
	 */
	public static void filterParamMap(Map<String, Object> paramMap){
		if(paramMap != null && !paramMap.isEmpty()){
			Iterator<String> iterator = paramMap.keySet().iterator();
			List<String> removeKeyList = new ArrayList<String>();
			while(iterator.hasNext()){
				String k = iterator.next();
				if(FastStringUtils.isBlank(k)){
					removeKeyList.add(k);
					continue;
				}
				Object v = paramMap.get(k);
				if(v == null){
					removeKeyList.add(k);
					continue;
				}
				if(FastStringUtils.isBlank(String.valueOf(v))){
					removeKeyList.add(k);
					continue;
				}
			}
			if(!removeKeyList.isEmpty()){
				for(String k : removeKeyList){
					paramMap.remove(k);
				}
			}
		}
	}
	
}
