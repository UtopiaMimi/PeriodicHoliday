package com.swan.twoafterfour.netutil;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 网络请求回调，解析对象
 * Created by TongYuliang on 2017/10/19.
 */

public abstract class RequestObjectNetCallBack<T> extends RequestNetCallBack {
	private final String TAG = "RequestObjectNetCallBack";

	@Override
	protected void onSuccessful(JSONObject jsonObject) throws JSONException {
		onSuccessful(parseObject(jsonObject.isNull("Data") ? null
				: jsonObject.getString("Data")));
	}

	private T parseObject(String data) {
		Type type = getClass().getGenericSuperclass();
		Type trueType = ((ParameterizedType) type).getActualTypeArguments()[0];
//		Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
//				.getActualTypeArguments()[0];
		Gson gson = new Gson();
		return gson.fromJson(data, trueType);
	}

	/**
	 * 处理请求成功的方法
	 *
	 * @param data 对象
	 */
	protected abstract void onSuccessful(T data);

}
