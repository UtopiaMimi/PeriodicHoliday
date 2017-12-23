package com.swan.twoafterfour.versionmanager;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.swan.twoafterfour.utils.SharedPreferencesManager;

import java.util.List;

/**
 * 版本更新工具
 */
public class UpdaterUtils {
	private static final String TAG = "UpdaterUtils";

	private static final String KEY_DOWNLOAD_ID = "downloadId";

	public static void install(Context context, Uri uri) {
//		BaseActivity activity = ActivityManager.getInstance().currentActivity();
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setAction(Intent.ACTION_DEFAULT);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 未完成下载，用户点击 Notification
	 *
	 * @param context
	 */
	public static void downloadDetails(Context context) {
		Intent viewDownloadIntent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
		viewDownloadIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(viewDownloadIntent);
	}

	/**
	 * 下载的apk和当前程序版本比较
	 *
	 * @param context           当前运行程序的 Context
	 * @param path              apk file's location
	 * @param recentVersionCode 最新版版本号
	 * @return 如果 apk 的版本号大于当前应用的版本号，并且等于服务器最新版版本号，返回true
	 */
	public static boolean compare(Context context, String path, int recentVersionCode) {
		PackageInfo apkInfo = getApkInfo(context, path);
		if (apkInfo == null) {
			return false;
		}

		try {
			String localPackage = context.getPackageName();
			PackageInfo packageInfo;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				packageInfo = context.getPackageManager().getPackageInfo(localPackage,
						PackageManager.MATCH_UNINSTALLED_PACKAGES);
			} else {
				packageInfo = context.getPackageManager().getPackageInfo(localPackage,
						PackageManager.GET_UNINSTALLED_PACKAGES);
			}
			Log.d(TAG, "apk file packageName=" + apkInfo.packageName +
					",versionName=" + apkInfo.versionName +
					",versionCode=" + apkInfo.versionCode);
			Log.d(TAG, "current app packageName=" + packageInfo.packageName +
					",versionName=" + packageInfo.versionName +
					",versionCode=" + packageInfo.versionCode);
			Log.d(TAG, "recentVersionCode：" + recentVersionCode);
			if (apkInfo.packageName.equals(packageInfo.packageName)) {
				if (apkInfo.versionCode > packageInfo.versionCode
						&& apkInfo.versionCode == recentVersionCode) {
					return true;
				}
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}


	/**
	 * 获取 apk 程序信息[packageName,versionName...]
	 *
	 * @param context Context
	 * @param path    apk path
	 */
	public static PackageInfo getApkInfo(Context context, String path) {
		Log.d(TAG, "path:" + path);
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
		if (info != null) {
			return info;
		}
		return null;
	}

	/**
	 * 要启动的intent是否可用
	 *
	 * @return boolean
	 */
	public static boolean intentAvailable(Context context, Intent intent) {
		PackageManager packageManager = context.getPackageManager();
		List list = packageManager.queryIntentActivities(intent, PackageManager
				.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	/**
	 * 系统的下载组件是否可用
	 *
	 * @return boolean
	 */
	public static boolean checkDownloadState(Context context) {
		try {
			int state = context.getPackageManager().getApplicationEnabledSetting("com.android" +
					".providers.downloads");
			if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
					|| state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
					|| state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void showDownloadSetting(Context context) {
		String packageName = "com.android.providers.downloads";
		Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.setData(Uri.parse("package:" + packageName));
		if (UpdaterUtils.intentAvailable(context, intent)) {
			context.startActivity(intent);
		}
	}

	public static long getLocalDownloadId(Context context) {
		return SharedPreferencesManager.getLong(context, KEY_DOWNLOAD_ID, -1L);
	}

	public static void saveDownloadId(Context context, long id) {
		SharedPreferencesManager.putLong(context, KEY_DOWNLOAD_ID, id);
	}


}


