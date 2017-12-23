package com.swan.twoafterfour.globaldata;

/**
 * 静态数据类
 * Created by TongYuliang on 2017/5/6.
 */

public class GlobalData {

	private static boolean ONLINE = false;

	/**
	 * 接口前缀 http://api.yinglaijinrong.com/ || http://192.168.10.69/
	 */
	public static final String REMOTE_SERVER_ADDRESS = ONLINE ? "http://roomapi.work-oa.com/"
			: "http://192.168.10.47:9080/";

	/**
	 * 图片 url 前缀 http://img.yinglaijinrong.com || http://192.168.10.69:8081
	 */
	public static final String IMAGE_SERVER_ADDRESS = ONLINE ? "http://img.work-oa.com"
			: "http://192.168.10.47:9081";

	/**
	 * 新版本 url 前缀 http://noteapi.yinglaijinrong.com/APK/ || http://192.168.10.69:8081
	 */
	public static final String DOWNLOAD_SERVER_ADDRESS = ONLINE ? "http://roomapi.work-oa.com/APK"
			: "http://192.168.10.47:9080/sf";

	/**
	 * 屏幕宽度
	 */
	public static int width;

	/**
	 * 屏幕高度
	 */
	public static int height;

	/**
	 * 屏幕密度
	 */
	public static float density;

	/**
	 * 状态栏高度
	 */
	public static int statusBarHeight;
}
