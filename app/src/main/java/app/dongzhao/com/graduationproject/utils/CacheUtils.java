package app.dongzhao.com.graduationproject.utils;

import android.content.Context;

/**
 * 网络缓存工具类
 */
public class CacheUtils {

	/**
	 * 以url为key, 以json为value,保存在本地
	 */
	public static void setCache(String url, String json, Context ctx) {
		PrefUtil.setString(ctx, url, json);
	}

	/**
	 * 获取缓存
	 */
	public static String getCache(String url, Context ctx) {
		return PrefUtil.getString(ctx, url, null);
	}
}
