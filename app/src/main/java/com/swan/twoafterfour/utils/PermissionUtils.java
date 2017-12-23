package com.swan.twoafterfour.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.swan.twoafterfour.customclass.BaseActivity;


/**
 * 请求权限工具类
 * Created by TongYuliang on 2017/7/26.
 */

public class PermissionUtils {
	private static final String TAG = "PermissionUtils";

	public static final int LOCATION_PERMISSION_REQUEST = 127;
	public static final int STORAGE_PERMISSION_REQUEST = 784;

	/**
	 * 权限请求
	 *
	 * @param permission
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.M)
	private static boolean requestPermission(String permission, int requestCode) {
		BaseActivity activity = ActivityManager.getInstance().currentActivity();
		Log.i(TAG, "permission:" + permission);
		if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager
				.PERMISSION_GRANTED) {
			System.out.println("ActivityCompat.shouldShowRequestPermissionRationale(activity, " +
					"permission):"
					+ ActivityCompat.shouldShowRequestPermissionRationale(activity, permission));
			if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
				return false;
			}
			activity.requestPermissions(new String[]{permission}, requestCode);
			return false;
		}
		return true;
	}

	@TargetApi(23)
	public static boolean checkLocationPermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (!requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
					LOCATION_PERMISSION_REQUEST)) {
				return false;
			} else if (!requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION,
					LOCATION_PERMISSION_REQUEST)) {
				return false;
			}
		}
		return true;
	}

	@TargetApi(23)
	public static boolean checkStoragePermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (!requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
					STORAGE_PERMISSION_REQUEST)) {
				return false;
			}
		}
		return true;
	}

//	@TargetApi(23)
//	public static boolean requestSharePermission() {
//		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
//			String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
//					Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,
//					Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE,
//					Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,
//					Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,
//					Manifest.permission.WRITE_APN_SETTINGS};
//			ActivityCompat.requestPermissions(ActivityManager.getInstance().currentActivity(),
// mPermissionList,123);
//		}
//		return true;
//	}

}
