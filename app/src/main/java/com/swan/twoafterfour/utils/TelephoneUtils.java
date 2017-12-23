package com.swan.twoafterfour.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 拨打电话工具类
 * Created by TongYuliang on 2017/7/26.
 */
public class TelephoneUtils {

	/**
	 * 打开拨号界面
	 */
	public static void call(Context context, String telephoneNumber) {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:" + telephoneNumber));
		context.startActivity(intent);

		/* 直接拨打电话需要申请权限
		Intent intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel:" + phoneNumber));
		// 判断 SDK 版本
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (ActivityCompat.checkSelfPermission(Act, Manifest.permission.CALL_PHONE) !=
			PackageManager.PERMISSION_GRANTED) {
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				Act.requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
				return;
			}
		}else {
			Act.startActivity(intent);
		}*/
	}

}
