package com.fast.framework.commons.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页对象
 * @author lion.chen
 * @param <T>
 */
public class FastPage implements Serializable {

	private static final long serialVersionUID = -2340770187488024722L;

	public static final String key_page = "page";
	public static final String key_pageSize = "pageSize";
	public static final String key_rows = "rows";
	
	/**当前分页列表*/
	public List<?> rows = new ArrayList<Object>();
	
	/**总页数*/
	public int pages;
	
	/**总记录数*/
	public int total;
	
	/**当前页数*/
	public int page;
	
	/**每页纪录数*/
	public int pageSize;
	
	public static FastPage getPage(int page, int pageSize, List<?> data, int total){
		
		FastPage fastPage = new FastPage();
		
		fastPage.page = page;
		fastPage.pageSize = pageSize;
		fastPage.rows = data;
		fastPage.total = total;
		if (pageSize > 0 && total > 0) {
			int perPage = total / pageSize;
			fastPage.pages = total % pageSize == 0 ? perPage : perPage + 1;
		}
		
		return fastPage;
	}
	
	/**
	 *	设置分页数据
	 * @param page
	 * @param pageSize
	 * @param data
	 * @param total
	 */
	public void setData(List<?> data, int total) {
		this.rows = data;
		this.total = total;
		if (pageSize > 0 && total > 0) {
			int perPage = total / pageSize;
			pages = total % pageSize == 0 ? perPage : perPage + 1;
		}
	}
	
}
