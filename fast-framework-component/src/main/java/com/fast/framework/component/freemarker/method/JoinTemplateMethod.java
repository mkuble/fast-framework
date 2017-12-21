package com.fast.framework.component.freemarker.method;

import java.util.ArrayList;
import java.util.List;

import com.fast.framework.commons.utils.FastStringUtils;
import com.fast.framework.component.freemarker.model.BaseTemplateMethodModel;

import freemarker.ext.beans.CollectionModel;
import freemarker.template.TemplateModelException;

/**
 *  join(list, separator)
 * @author lion.chen
 */
public class JoinTemplateMethod extends BaseTemplateMethodModel {

	@SuppressWarnings("rawtypes")
	public Object execEx(List list) throws TemplateModelException {
		
		String r = FastStringUtils.EMPTY;
		
		if(list != null && !list.isEmpty()){
			
			int size = list.size();
			Object object = list.get(0);
			
			if(object instanceof CollectionModel){
				
				String separator = size > 1 ? (String)list.get(1) : null;
				
				CollectionModel c = (CollectionModel)object;
				List<String> tempList = new ArrayList<String>();
				for(int i = 0, len = c.size(); i < len; i++){
					tempList.add(c.get(i).toString());
				}
				
				if(separator != null){
					r = FastStringUtils.join(tempList, separator);
				}else{
					r = FastStringUtils.join(tempList);
				}
			}
		}
		
		return r;
	}

}
