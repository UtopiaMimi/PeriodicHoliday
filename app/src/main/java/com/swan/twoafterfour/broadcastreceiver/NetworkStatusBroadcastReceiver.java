package com.swan.twoafterfour.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;

import com.swan.twoafterfour.customclass.BaseActivity;
import com.swan.twoafterfour.utils.ActivityManager;
import com.swan.twoafterfour.versionmanager.VersionManager;


/**
 * 网络状态的广播
 *
 * @author TongYuliang
 * @from http://www.jianshu.com/p/cec09fb20c52
 */
public class NetworkStatusBroadcastReceiver extends BroadcastReceiver {
	private final String TAG = "NetworkStatusBroadcastReceiver";

	public static boolean isConnected;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onReceive__context:" + context);
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			Log.d("networkInfo", "networkInfo == null");
			isConnected = false;
		} else {
			State state = networkInfo.getState();
			if (state != State.CONNECTED) {
				Log.d("networkInfo", "state != State.CONNECTED");
				isConnected = false;
			} else {
				Log.d("networkInfo", "state == State.CONNECTED");
				isConnected = true;
			}
		}
		Log.d(TAG, "isConnected:" + isConnected);

		if (!VersionManager.haveChecked) {
			BaseActivity activity = ActivityManager.getInstance().currentActivity();
			if (activity.versionManager == null) {
				activity.versionManager = new VersionManager(activity);
			}
			activity.versionManager.requestRecentCode();
		}

		// State wifiState = connectivityManager.getNetworkInfo(
		// ConnectivityManager.TYPE_WIFI).getState();// 获取wifi网络状态
		// State mobileState = connectivityManager.getNetworkInfo(
		// ConnectivityManager.TYPE_MOBILE).getState();// 获取移动数据网络状态

		// 没有执行return,则说明当前无网络连接
		// if (wifiState != State.CONNECTED && mobileState != State.CONNECTED) {
		// System.out.println("------------> Network is validate");
		// intent.setClass(context, NetWorkErrorActivity.class);
		// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
		// | Intent.FLAG_ACTIVITY_NEW_TASK);
		// context.startActivity(intent);
		//
		// return;
		// }
		// 判断网络是否已经连接
		// System.out.println("------------> Network is ok");
	}

}
