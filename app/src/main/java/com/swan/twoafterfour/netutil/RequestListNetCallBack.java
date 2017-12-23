package com.swan.twoafterfour.netutil;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 网络请求回调，解析数组
 * Created by TongYuliang on 2017/10/19.
 */

public abstract class RequestListNetCallBack<T> extends RequestNetCallBack {
	private final String TAG = "RequestListNetCallBack";

	@Override
	protected void onSuccessful(JSONObject jsonObject) throws JSONException {
		onSuccessful(parseArray(jsonObject.isNull("Data") ? null
				: jsonObject.getString("Data")));
	}

	private List<T> parseArray(String data) {
		/*
		元素类型属于 com.google.gson.internal.LinkedTreeMap，list.addAll() 方法正常运行，但在适配器中 get
		元素时，类型转换异常 com.google.gson.internal.LinkedTreeMap cannot be cast to...
		 */
//		Gson gson = new Gson();
//		return gson.fromJson(data,
//				new TypeToken<List<T>>() {
//				}.getType());

		List<T> mList = new ArrayList<>();
		JsonArray array = new JsonParser().parse(data).getAsJsonArray();
		for (final JsonElement elem : array) {
			mList.add(formatObject(elem.toString()));
		}
		return mList;
	}

	private T formatObject(String data) {
		Type type = getClass().getGenericSuperclass();
		Type trueType = ((ParameterizedType) type).getActualTypeArguments()[0];
		Gson gson = new Gson();
		return gson.fromJson(data, trueType);
	}

	/**
	 * 处理请求成功的方法
	 *
	 * @param data 集合
	 */
	public abstract void onSuccessful(List<T> data);

}
