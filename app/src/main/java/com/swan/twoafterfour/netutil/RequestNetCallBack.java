package com.swan.twoafterfour.netutil;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * 网络请求回调
 * Created by TongYuliang on 2017/8/28.
 */

public abstract class RequestNetCallBack {
	private final String TAG = "LoginRequestNetCallBack";

	/**
	 * JSONException code
	 */
	public static final int JSON_EXCEPTION_CODE = -20170828;

	/**
	 * 请求失败
	 *
	 * @param e 异常
	 */
	public final void onFailure(IOException e) {
		e.printStackTrace();
		onFailed(-1, null);
	}

	/**
	 * 请求成功
	 *
	 * @param content 服务器返回的内容
	 */
	public final void onSuccess(String content) {
		Log.d(TAG, "content:" + content);
		try {
			JSONObject jsonObject = new JSONObject(content);

			if (jsonObject.getBoolean("successful")) {
				onSuccessful(jsonObject);
			} else {
				onFailed(jsonObject.getInt("Code"), jsonObject.getString("Message"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			onFailed(JSON_EXCEPTION_CODE, null);
		}
	}

	/**
	 * 处理请求失败的方法
	 *
	 * @param errorCode 错误码
	 * @param message   错误信息
	 */
	protected abstract void onFailed(int errorCode, String message);

	/**
	 * 解析数据
	 *
	 * @param jsonObject 要解析的 JSON 对象
	 * @throws JSONException
	 */
	protected abstract void onSuccessful(JSONObject jsonObject) throws JSONException;

}
