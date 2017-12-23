package com.swan.twoafterfour.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Toast 工具类
 * Created by Swan on 2017/10/22.
 */

public class ToastUtils {

	/**
	 * 文本复用toast
	 */
	private static Toast toast;

	public static void showToast(Context context, String str) {
		if (toast != null) {
			toast.setText(str);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.show();
		} else {
			toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private static Toast toastImg;

	public static void showToastImage(Context context, int drawableId) {
		if (toastImg == null) {
			toastImg = new Toast(context);
			toastImg.setGravity(Gravity.CENTER, 0, 0);
		}
		/*
		图片复用toast
		 */
		ImageView toastImage = new ImageView(context);
		toastImage.setImageResource(drawableId);
		toastImage.setLayoutParams(new ViewGroup.LayoutParams(10, 100));
		toastImg.setView(toastImage);
		toastImg.setDuration(Toast.LENGTH_SHORT);
		toastImg.show();
	}

}
