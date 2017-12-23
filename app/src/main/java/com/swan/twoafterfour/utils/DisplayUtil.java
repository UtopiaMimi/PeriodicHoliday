package com.swan.twoafterfour.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * 屏幕单位转换工具类
 *
 * @author Wyb
 */
public class DisplayUtil {

	private static final String TAG = "DisplayUtil";

	/**
	 * 根据手机的屏幕密度把 dp 单位转成为 px
	 * density: DisplayMetrics 类中属性 density
	 */
	public static int dip2Px(Context context, float dpValue) {
		final float density = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * density + 0.5f);
	}

	/**
	 * 将px值转换为dip或dp值
	 * density: DisplayMetrics 类中属性 density
	 */
	public static int px2dip(Context context, float pxValue) {
		final float density = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / density + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * scaledDensity: DisplayMetrics 类中属性 scaledDensity
	 */
	public static int px2sp(Context context, float pxValue) {
		final float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / scaledDensity + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * scaledDensity: DisplayMetrics 类中属性 scaledDensity
	 */
	public static int sp2px(Context context, float spValue) {
		final float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * scaledDensity + 0.5f);
	}

	/**
	 * 获取屏幕宽度和高度，单位为px
	 *
	 * @param context
	 * @return
	 */
	public static Point getScreenMetrics(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int w_screen = dm.widthPixels;
		int h_screen = dm.heightPixels;
		Log.i(TAG, "Screen---Width = " + w_screen + " Height = " + h_screen + " densityDpi = " +
				dm.densityDpi);
		return new Point(w_screen, h_screen);

	}

	/**
	 * 获取屏幕长宽比
	 *
	 * @param context
	 * @return
	 */
	public static float getScreenRate(Context context) {
		Point P = getScreenMetrics(context);
		float H = P.y;
		float W = P.x;
		return (H / W);
	}

}
