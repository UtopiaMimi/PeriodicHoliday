package com.swan.twoafterfour.utils;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 键盘工具类
 * Created by Swan on 2017/10/22.
 */

public class KeyboardUtils {

	/**
	 * 延迟操作输入法——如果显示则隐藏，反之则显示 目前只用于进入编辑页面时自动弹出（页面跳转前是隐藏状态）
	 *
	 * @param context 上下文对象
	 */
	public static void delayShowKeyBoard(final Context context) {
		Handler handler = new Handler();
		handler.postDelayed(() -> {
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		}, 200);
	}

	/**
	 * 延迟隐藏输入法
	 *
	 * @param context 上下文对象
	 * @param view    获得焦点的 view
	 */
	public static void delayHideKeyBoard(final Context context, final View view) {
		Handler handler = new Handler();
		handler.postDelayed(() -> {
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}, 200);
	}

	/**
	 * 显示输入法
	 *
	 * @param context 上下文对象
	 */
	public static void showKeyBoard(final Context context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
	}

	/**
	 * 隐藏输入法
	 *
	 * @param context 上下文对象
	 * @param view    获得焦点的 view
	 */
	public static void hideKeyBoard(final Context context, final View view) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}

}
