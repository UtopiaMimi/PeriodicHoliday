package com.swan.twoafterfour.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;

/**
 * 文本格式工具类
 * Created by Swan on 2017/10/22.
 */

public class TextFormatUtils {

	/**
	 * 文本处理——将字符串分成字号、颜色不同的多段
	 *
	 * @param context     上下文对象
	 * @param text        输入的文本
	 * @param positions   分隔的下标数组
	 * @param appearances 文本 style 数组
	 * @return 输出的文本
	 */
	public static SpannableString changeTextColor(Context context, String text, @NonNull int[]
			positions,
	                                              @NonNull int[] appearances) {
		if (positions.length >= appearances.length) {
			throw new IndexOutOfBoundsException("Parameter positions's length must be less than " +
					"appearances's length.");
		} else if (positions.length < appearances.length - 1) {
			Log.w("GlobalMethod", "Parameter appearances has surplus.");
		}
		SpannableString styledText = new SpannableString(text);
		styledText.setSpan(new TextAppearanceSpan(context, appearances[0]), 0,
				positions[0], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		for (int i = 0; i < positions.length - 1; i++) {
			styledText.setSpan(new TextAppearanceSpan(context, appearances[i + 1]),
					positions[i], text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		styledText.setSpan(new TextAppearanceSpan(context, appearances[appearances.length - 1]),
				positions[positions.length - 1], text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return styledText;
	}

}
