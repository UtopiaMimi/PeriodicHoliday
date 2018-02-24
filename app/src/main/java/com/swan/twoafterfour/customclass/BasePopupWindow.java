package com.swan.twoafterfour.customclass;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by Swan on 2017/11/22.
 */

public class BasePopupWindow extends PopupWindow {
	private Context mContext;
	private float mShowAlpha = 0.6f;
	private Drawable mBackgroundDrawable;
	private boolean mChangeBackgroundAlpha = true;

	private OnDismissListener mOnDismissListener;
	private OnChildClickListener mOnChildClickListener;

	public void setOnDismissListener(OnDismissListener onDismissListener) {
		mOnDismissListener = onDismissListener;
	}

	public BasePopupWindow(Context context, int width, int height) {
		this.mContext = context;
		initBasePopupWindow(width, height, android.R.style.Animation_Dialog);
	}

	public BasePopupWindow(Context context, int width, int height, int animationStyle) {
		this.mContext = context;
		initBasePopupWindow(width, height, animationStyle);
	}

	public void setChangeBackgroundAlpha(boolean b) {
		this.mChangeBackgroundAlpha = b;
	}

	@Override
	public void setOutsideTouchable(boolean touchable) {
		super.setOutsideTouchable(touchable);
		if (touchable) {
			if (mBackgroundDrawable == null) {
				mBackgroundDrawable = new ColorDrawable(0x00000000);
			}
			super.setBackgroundDrawable(mBackgroundDrawable);
		} else {
			super.setBackgroundDrawable(null);
		}
	}

	@Override
	public void setBackgroundDrawable(Drawable background) {
		mBackgroundDrawable = background;
		setOutsideTouchable(isOutsideTouchable());
	}

	/**
	 * 初始化BasePopupWindow的一些信息
	 */
	private void initBasePopupWindow(int width, int height, int animationStyle) {
//		setAnimationStyle(android.R.style.Animation_Dialog);
//		setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//		setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		setAnimationStyle(animationStyle);
		setHeight(height);
		setWidth(width);
		setOutsideTouchable(true);  //默认设置outside点击无响应
		setFocusable(true);
//		setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED | WindowManager.LayoutParams
//				.SOFT_INPUT_ADJUST_RESIZE);
		setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}

	@Override
	public void setContentView(View contentView) {
		if (contentView != null) {
			contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
			super.setContentView(contentView);
			addKeyListener(contentView);
		}
	}

	public Context getContext() {
		return mContext;
	}

	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		if (mChangeBackgroundAlpha) {
			showAnimator().start();
		}
	}

	@Override
	public void showAsDropDown(View anchor) {
		if (Build.VERSION.SDK_INT >= 24) {
			//兼容Android7.0以后（该显示方式无法正确计算View的高度，导致View高度为match时整个页面都覆盖了）
			Rect rect = new Rect();
			anchor.getGlobalVisibleRect(rect);
			int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
			setHeight(h);
		}
		super.showAsDropDown(anchor);
		if (mChangeBackgroundAlpha) {
			showAnimator().start();
		}
	}

	@Override
	public void showAsDropDown(View anchor, int xoff, int yoff) {
		super.showAsDropDown(anchor, xoff, yoff);
		if (mChangeBackgroundAlpha) {
			showAnimator().start();
		}
	}

	@Override
	public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
		super.showAsDropDown(anchor, xoff, yoff, gravity);
		if (mChangeBackgroundAlpha) {
			showAnimator().start();
		}
	}

	@Override
	public void dismiss() {
		super.dismiss();
		if (Build.MODEL.contains("HUAWEI")) {    //华为特殊处理 不然会闪烁
			Window window = ((Activity) mContext).getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		}
		if (mChangeBackgroundAlpha) {
			dismissAnimator().start();
		}
		if (mOnDismissListener != null) {
			mOnDismissListener.onDismiss();
		}
	}

	/**
	 * 窗口显示，窗口背景透明度渐变动画
	 */
	private ValueAnimator showAnimator() {
		ValueAnimator animator = ValueAnimator.ofFloat(1.0f, mShowAlpha);
		animator.addUpdateListener(animation -> {
			float alpha = (float) animation.getAnimatedValue();
			setWindowBackgroundAlpha(alpha);
		});
		animator.setDuration(360);
		return animator;
	}

	/**
	 * 窗口隐藏，窗口背景透明度渐变动画
	 */
	private ValueAnimator dismissAnimator() {
		ValueAnimator animator = ValueAnimator.ofFloat(mShowAlpha, 1.0f);
		animator.addUpdateListener(animation -> {
			float alpha = (float) animation.getAnimatedValue();
			setWindowBackgroundAlpha(alpha);
		});
		animator.setDuration(320);
		return animator;
	}

	/**
	 * 为窗体添加outside点击事件
	 */
	private void addKeyListener(View contentView) {
		if (contentView != null) {
			contentView.setFocusable(true);
			contentView.setFocusableInTouchMode(true);
			contentView.setOnKeyListener((view, keyCode, event) -> {
				switch (keyCode) {
					case KeyEvent.KEYCODE_BACK:
						dismiss();
						return true;
					default:
						break;
				}
				return false;
			});
		}
	}

	/**
	 * 控制窗口背景的不透明度
	 */
	private void setWindowBackgroundAlpha(float alpha) {
		Window window = ((Activity) mContext).getWindow();
		WindowManager.LayoutParams layoutParams = window.getAttributes();
		layoutParams.alpha = alpha;
		if (Build.MODEL.contains("HUAWEI")) {
			window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		}
		window.setAttributes(layoutParams);
	}

	public interface OnDismissListener {
		void onDismiss();
	}

	public interface OnChildClickListener {
		void onViewClicked(View view);
	}
}