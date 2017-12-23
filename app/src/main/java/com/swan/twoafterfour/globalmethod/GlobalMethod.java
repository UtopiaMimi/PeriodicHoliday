package com.swan.twoafterfour.globalmethod;

import android.app.Activity;
import android.text.TextUtils;
import android.view.WindowManager;

/**
 * Created by TongYuliang on 2017/5/6.
 * 静态公共方法类
 */

public class GlobalMethod {

	/**
	 * 设置背景透明度
	 *
	 * @param activity
	 * @param bgAlpha
	 */
	public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.alpha = bgAlpha; //0.0-1.0
		activity.getWindow().setAttributes(lp);
		activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	}

	/**
	 * 网络返回图片路径根据不同分辨率改变其路径
	 */
	public static String formatIntentImages(String uri, int width, int height,
	                                        String type) {
		String formatUri = "";
		formatUri = uri.replace("{0}", width + "");
		formatUri = formatUri.replace("{1}", height + "");
		formatUri = formatUri.replace("{2}", type);
		return formatUri;
	}

	/**
	 * 验证手机号格式
	 *
	 * @return
	 */
	public static boolean checkFormat(String number) {
		/*
		移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
	    联通：130、131、132、152、155、156、185、186
	    电信：133、153、170、180、189、（1349卫通）
	    总结起来就是第一位必定为1，第二位必定为3、5、7或8，其他位置的可以为0-9
	    */
		String telRegex = "[1][3578]\\d{9}";
		//"[1]"代表第1位为数字1，"[3578]"代表第二位可以为3、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(number))
			return false;
		else
			return number.matches(telRegex);
	}

}
