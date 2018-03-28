package com.risenb.witness.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {

	private static SharedPreferences sp;
	
	/**
	 * 存储boolean类型值
	 */
	public static void saveBoolean(Context context, String key, boolean value) {
		if (sp == null) {
			//name:保存信息的XML文件名/mode:操作模式
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		//类似于键值对儿方式保存,key:属性名/value:属性值
		sp.edit().putBoolean(key, value).commit();
	} 
	
	/**
	 * 获取boolean类型值
	 */
	public static boolean getBoolean(Context context, String key, boolean defValue) {
		if (sp == null) {
			//name:保存信息的XML文件名/mode:操作模式
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		//根据key获取值,如果没有找到对应值则返回默认值defValue
		return sp.getBoolean(key, defValue);
	}
	
	/**
	 * 存储String类型值
	 */
	public static void saveString(Context context, String key, String value) {
		if (sp == null) {
			//name:保存信息的XML文件名/mode:操作模式
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		//类似于键值对儿方式保存,key:属性名/value:属性值
		sp.edit().putString(key, value).commit();
	} 
	
	/**
	 * 获取String类型值
	 */
	public static String getString(Context context, String key, String defValue) {
		if (sp == null) {
			//name:保存信息的XML文件名/mode:操作模式
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		//根据key获取值,如果没有找到对应值则返回默认值defValue
		return sp.getString(key, defValue);
	}
	
	/**
	 * 存储int类型值
	 */
	public static void saveInt(Context context, String key, int value) {
		if (sp == null) {
			//name:保存信息的XML文件名/mode:操作模式
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		//类似于键值对儿方式保存,key:属性名/value:属性值
		sp.edit().putInt(key, value).commit();
	} 
	
	/**
	 * 获取int类型值
	 */
	public static int getInt(Context context, String key, int defValue) {
		if (sp == null) {
			//name:保存信息的XML文件名/mode:操作模式
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		//根据key获取值,如果没有找到对应值则返回默认值defValue
		return sp.getInt(key, defValue);
	}

	/**
	 * 存储long类型值
	 */
	public static void saveLong(Context context, String key, long value) {
		if (sp == null) {
			//name:保存信息的XML文件名/mode:操作模式
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		//类似于键值对儿方式保存,key:属性名/value:属性值
		sp.edit().putLong(key, value).commit();
	}

	/**
	 * 获取long类型值
	 */
	public static long getLong(Context context, String key, long defValue) {
		if (sp == null) {
			//name:保存信息的XML文件名/mode:操作模式
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		//根据key获取值,如果没有找到对应值则返回默认值defValue
		return sp.getLong(key, defValue);
	}

}