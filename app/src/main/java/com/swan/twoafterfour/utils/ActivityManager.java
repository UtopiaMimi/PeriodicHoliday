package com.swan.twoafterfour.utils;

import android.content.Context;

import com.swan.twoafterfour.customclass.BaseActivity;

import java.util.Stack;

/**
 * Created by TongYuliang on 2017/4/27.
 * Activity 管理器
 */

public class ActivityManager {

	private static Stack<BaseActivity> activityStack;

	private static class SingletonHolder {
		private static final ActivityManager INSTANCE = new ActivityManager();
	}

	private ActivityManager() {

	}

	/**
	 * 单例模式
	 */
	public static ActivityManager getInstance() {
		return SingletonHolder.INSTANCE;
	}

	/**
	 * 添加 Activity 到堆栈
	 */
	public void addActivity(BaseActivity activity) {
		if (activityStack == null) {
			activityStack = new Stack<>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取class对应的Activity
	 *
	 * @param cls 对应的Activity.class文件
	 * @return 返回.class对应的Activity, 不存在此文件时返回空
	 */
	public BaseActivity getActivity(Class<?> cls) {
		for (BaseActivity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				return activity;
			}
		}
		return null;
	}

	/**
	 * 获取当前 Activity（堆栈中最后一个压入的）
	 */
	public BaseActivity currentActivity() {
		if (activityStack == null || activityStack.size() == 0) {
			return null;
		}
		return activityStack.lastElement();
	}

	/**
	 * 结束当前 Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		BaseActivity activity = activityStack.pop();
		activity.finish();
		activity = null;
	}

	/**
	 * 结束指定的 Activity
	 */
	public void finishActivity(BaseActivity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的 Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (BaseActivity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
				break;
			}
		}
	}

	/**
	 * 结束所有 Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (activityStack.get(i) != null) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 退出应用程序
	 */
	public void ExitApp(Context context) {
		try {
			finishAllActivity();
			android.app.ActivityManager activityMgr = (android.app.ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.killBackgroundProcesses(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
