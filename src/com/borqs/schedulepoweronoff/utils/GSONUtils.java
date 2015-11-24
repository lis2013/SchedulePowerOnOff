package com.borqs.schedulepoweronoff.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GSONUtils {
	public static Gson gson = null;

	static {
		if (gson == null) {
			gson = new GsonBuilder().create();
		}
	}

	private GSONUtils() {

	}

	/**
	 * 将对象转换成json格式
	 *
	 * @param ts
	 * @return
	 */
	public static String objectToJson(Object ts) {
		String jsonStr = null;
		if (gson != null) {
			jsonStr = gson.toJson(ts);
		}
		return jsonStr;
	}

	/**
	 * 将json转换成bean对象
	 *
	 * @param jsonStr
	 * @return
	 */
	public static <T> T jsonToBean(String jsonStr, Type cl) {
		T obj = null;
		if (gson != null) {
			obj = gson.fromJson(jsonStr, cl);
		}
		return obj;
	}

	/**
	 * 将json转换成bean对象
	 *
	 * @param jsonStr
	 * @return
	 */
	public static <T> T jsonToBean(String jsonStr, Class<T> cl) {
		T obj = null;
		if (gson != null) {
			obj = gson.fromJson(jsonStr, cl);
		}
		return obj;
	}

	/**
	 * 将json格式转换成list对象，并准确指定类型
	 *
	 * @param jsonStr
	 * @param type
	 * @return
	 */
	public static <T> List<T> jsonToList(String jsonStr,
			java.lang.reflect.Type type) {
		List<T> objList = null;
		if (gson != null) {
			objList = gson.fromJson(jsonStr, type);
		}
		if (objList == null) {
			objList = new ArrayList<T>();
		}
		return objList;
	}
}
