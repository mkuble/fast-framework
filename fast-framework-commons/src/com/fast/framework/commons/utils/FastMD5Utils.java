package com.fast.framework.commons.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具
 * 
 * @author lion.chen
 * @version 1.0.0 2017年12月20日 下午5:54:26
 */
public class FastMD5Utils {

	private static final String algorithm_md5 = "MD5";
	
	/**
	 * 生成新的token
	 * @param key
	 * @param timeChange
	 * @return
	 */
	public static String getToken(String key, boolean timeChange) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm_md5);
			md.update(key.getBytes());
			return toHex(md.digest());
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
	/**
	 * 32位md5加密
	 * @param str
	 */
	public static String get32MD5(String str) {
		StringBuilder buf = new StringBuilder("");
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte b[] = md.digest();
			int i;
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return buf.toString();// 32位的加密
	}

	/**
	 * 16位md5加密
	 * @param str
	 */
	public static String get16MD5(String str) {
		return get32MD5(str).substring(8, 24);// 16位的加密
	}

	private static String toHex(byte buffer[]) {
		StringBuffer sb = new StringBuffer(buffer.length * 2);
		for (int i = 0; i < buffer.length; i++) {
			sb.append(Character.forDigit((buffer[i] & 240) >> 4, 16));
			sb.append(Character.forDigit(buffer[i] & 15, 16));
		}
		return sb.toString();
	}
	
}
