package com.fast.framework.data.jpa.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.alibaba.fastjson.JSONObject;
import com.fast.framework.commons.bean.FastPage;

public abstract class PageRequestParam {

	public static final int DEFAULT_PAGE = 1;
	public static final int DEFAULT_PAGE_SIZE = 10;
	
	/**当前页数*/
	public int page;
	
	/**每页纪录数*/
	public int pageSize;
	
	public List<SortRequestParam> sorts = new ArrayList<SortRequestParam>();
	
	/**
	 * jpa分页数据转换为PagingList
	 * @param page
	 * @return
	 */
	public static <T> FastPage toPagingList(Page<T> page){
		
		FastPage pagingList = null;
		
		if(page != null){
			pagingList = FastPage.getPage(page.getNumber() + 1, page.getSize(), page.getContent(), (int) page.getTotalElements());
		}
		
		return pagingList;
	}
	
	/**
	 * 获取排序规则
	 * @return
	 */
	public Sort getSort(){
		
		Sort sort = getFixedSort();
		
		if(sorts != null && !sorts.isEmpty()){
			for(SortRequestParam srp : sorts){
				if(srp != null && StringUtils.isNotBlank(srp.field)){
					Sort tempSort = new Sort(srp.asc ? Sort.Direction.ASC : Sort.Direction.DESC, srp.field);
					if(sort == null){
						sort = tempSort;
					} else {
						sort = sort.and(tempSort);
					}
				}
			}
		}
		
		return sort;
	}
	
	/**
	 * 获取排序规则
	 * @return Sort
	 */
	public abstract Sort getFixedSort();
	
	/**
	 * 获取查询分页查询器
	 * @return
	 */
	public PageRequest getPageRequest(){
		return getPageRequest(false);
	}
	
	/**
	 * 获取查询分页查询器
	 * @param isDefault
	 * @return
	 */
	public PageRequest getPageRequest(boolean isDefault){
		
		if(!isDefault && (page <= 0 || pageSize <= 0)){
			return null;
		}
		
		if(page <= 0) page = DEFAULT_PAGE;
		if(pageSize <= 0) page = DEFAULT_PAGE_SIZE;
		
		return new PageRequest(page - 1, pageSize, getSort());
	}
	
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
	
}
