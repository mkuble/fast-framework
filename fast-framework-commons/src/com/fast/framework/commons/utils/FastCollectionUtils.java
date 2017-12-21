package com.fast.framework.commons.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class FastCollectionUtils {

	public static <T> List<T> arrayToList(T[] arr) {
		
		return new ArrayList<T>(Arrays.asList(arr));
	}

	public static <K, V> Map<K, V> listAsMap(Collection<V> sourceList,
			KeyProvider<K, V> converter) {
		Map<K, V> newMap = new HashMap<K, V>();
		for (V item : sourceList) {
			newMap.put(converter.getKey(item), item);
		}
		return newMap;
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] cast(String[] arr, Class<T> type) {
		ArrayList<T> list = new ArrayList<T>(arr.length);
		for (String str : arr) {
			try {
				T t = type.getConstructor(String.class).newInstance(str);
				list.add(t);
			} catch (Exception e) {
			}
		}
		return list.toArray((T[]) Array.newInstance(type, list.size()));
	}

	public static String join(Object[] arr, String symbol) {
		if (arr == null || arr.length == 0)
			return FastStringUtils.EMPTY;
		StringBuilder sb = new StringBuilder();
		for (Object o : arr) {
			sb.append(symbol).append(o);
		}
		return sb.substring(symbol.length());
	}

	public static <T> boolean contains(T[] arr, T key) {
		if (arr == null)
			return false;
		for (T t : arr) {
			if (t == key || t.equals(key))
				return true;
		}
		return false;
	}

	public static <T> T[] split(String str, String symbol, Class<T> type) {
		String[] idArr = StringUtils.split(str, symbol);
		return cast(idArr, type);
	}

	public static interface KeyProvider<K, V> {
		public K getKey(V item);
	}

}
