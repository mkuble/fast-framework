package com.fast.framework.commons.utils;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * 数值工具类
 * 
 * @author lion.chen
 * @version 1.0.0 2017年8月18日 下午6:12:25
 */
public class FastNumberUtils extends NumberUtils{

	/**
	 * 金钱 分->元
	 * @param pennys
	 * @return
	 */
	public static double penny2Yuan(Integer pennys){
		return pennys == null ? 0 : pennys / 100.0;
	}
	
	public static double penny2Yuan(Double pennys){
		return pennys == null ? 0 : pennys / 100;
	}
	
	public static void main(String[] args) {
		
		String priceInt = String.valueOf(FastNumberUtils.penny2Yuan(0));
		System.out.println(priceInt);
		
	}
}
