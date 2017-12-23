package com.swan.twoafterfour.netutil;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

import static okhttp3.Request.Builder;

/**
 * 网络请求工具类
 * Created by Administrator on 2017/2/23.
 */

public class OkHttp3Util {
	private volatile static OkHttp3Util mInstance;
	private OkHttpClient mOkHttpClient;
	private Handler mHandler;
	private Context mContext;
	private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	private OkHttp3Util(Context context) {
		super();
		OkHttpClient.Builder clientBuilder = new OkHttpClient().newBuilder();
		clientBuilder.readTimeout(10, TimeUnit.SECONDS);
		clientBuilder.connectTimeout(5, TimeUnit.SECONDS);
		clientBuilder.writeTimeout(10, TimeUnit.SECONDS);
		mOkHttpClient = clientBuilder.build();
		this.mContext = context.getApplicationContext();
		mHandler = new Handler(mContext.getMainLooper());
	}

	public static OkHttp3Util getInstance(Context context) {
		OkHttp3Util temp = mInstance;
		if (temp == null) {
			synchronized (OkHttp3Util.class) {
				temp = mInstance;
				if (temp == null) {
					temp = new OkHttp3Util(context);
					mInstance = temp;
				}
			}
		}
		return temp;
	}

	/**
	 * 设置请求头
	 *
	 * @param headersParams
	 * @return
	 */
	private Headers SetHeaders(Map<String, String> headersParams) {
		Headers headers = null;
		Headers.Builder headersbuilder = new Headers.Builder();
		if (headersParams != null) {
			Iterator iterator = headersParams.keySet().iterator();
			String key = "";
			while (iterator.hasNext()) {
				key = iterator.next().toString();
				headersbuilder.add(key, headersParams.get(key));
			}
		}
		headers = headersbuilder.build();
		return headers;
	}

	/**
	 * post请求参数
	 *
	 * @param BodyParams
	 * @return
	 */
	private RequestBody SetPostRequestBody(Map<String, String> BodyParams) {
		RequestBody body = null;
		FormBody.Builder formEncodingBuilder = new FormBody.Builder();
		if (BodyParams != null) {
			Iterator iterator = BodyParams.keySet().iterator();
			String key = "";
			while (iterator.hasNext()) {
//                key = (String) iterator.next();
				key = iterator.next().toString();
				formEncodingBuilder.add(key, BodyParams.get(key));
			}
		}
//        RequestBody.create(MediaType.parse("application/json; charset=utf-8"),"");
		body = formEncodingBuilder.build();
		return body;
	}

	/**
	 * Post上传图片的参数
	 *
	 * @param BodyParams
	 * @param filePathParams
	 * @return
	 */
	private RequestBody SetFileRequestBody(Map<String, String> BodyParams, Map<String, String>
			filePathParams) {
		// 带文件的Post参数
		RequestBody body = null;
		MultipartBody.Builder MultipartBodyBuilder = new MultipartBody.Builder();
		MultipartBodyBuilder.setType(MultipartBody.FORM);
		RequestBody fileBody = null;
		if (BodyParams != null) {
			Iterator iterator = BodyParams.keySet().iterator();
			String key = "";
			while (iterator.hasNext()) {
				key = iterator.next().toString();
				MultipartBodyBuilder.addFormDataPart(key, BodyParams.get(key));
			}
		}
		if (filePathParams != null) {
			Iterator iterator = filePathParams.keySet().iterator();
			String key = "";
			int i = 0;
			while (iterator.hasNext()) {
				key = iterator.next().toString();
				i++;
				MultipartBodyBuilder.addFormDataPart(key, filePathParams.get(key));
				fileBody = RequestBody.create(MEDIA_TYPE_PNG, new File(filePathParams.get(key)));
				MultipartBodyBuilder.addFormDataPart(key, i + ".png", fileBody);
			}
		}
		body = MultipartBodyBuilder.build();
		return body;
	}

	/**
	 * 进度更新
	 */
	public static RequestBody createCustomRequestBody(final MediaType contentType, final File
			file, final ProgressListener listener) {
		return new RequestBody() {
			@Override
			public MediaType contentType() {
				return contentType;
			}

			@Override
			public long contentLength() {
				return file.length();
			}

			@Override
			public void writeTo(BufferedSink sink) throws IOException {
				Source source;
				try {
					source = Okio.source(file);
					//sink.writeAll(source);
					Buffer buf = new Buffer();
					Long remaining = contentLength();
					for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
						sink.write(buf, readCount);
						listener.onProgress(contentLength(), remaining -= readCount, remaining ==
								0);

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}


	interface ProgressListener {
		void onProgress(long totalBytes, long remainingBytes, boolean done);
	}

	/**
	 * get方法连接拼加参数
	 *
	 * @param mapParams
	 * @return
	 */
	private String setGetUrlParams(Map mapParams) {
		String strParams = "";
		if (mapParams != null) {
			Iterator iterator = mapParams.keySet().iterator();
			String key = "";
			while (iterator.hasNext()) {
				key = iterator.next().toString();
				strParams += "&" + key + "=" + mapParams.get(key);
			}
		}
		return strParams;
	}

	/**
	 * 实现post请求
	 *
	 * @param reqUrl
	 * @param headersParams
	 * @param params
	 * @param callback
	 */
	public void doPost(String reqUrl, Map headersParams, Map params, final RequestNetCallBack
			callback) {
		Log.d("OkHttp3", "OkHttp3.doPost():" + reqUrl);
		Builder RequestBuilder = new Builder();
		RequestBuilder.url(reqUrl);// 添加URL地址
		RequestBuilder.method("POST", SetPostRequestBody(params));
		RequestBuilder.headers(SetHeaders(headersParams));// 添加请求头
		Request request = RequestBuilder.build();
		mOkHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				callback.onFailure(e);
				call.cancel();
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				callback.onSuccess(response.body().string());
			}
		});
	}

	public void doPostByJson(String reqUrl, String json, final RequestNetCallBack callback) {
		Log.d("doPostByJson", json);
		Request request = new Request.Builder()
				.url(reqUrl)
				.post(postJson(json))
				.build();

		mOkHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				callback.onFailure(e);
				call.cancel();
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				callback.onSuccess(response.body().string());
			}
		});
	}

	private RequestBody postJson(String json) {
		//申明给服务端传递一个json串
		//创建一个RequestBody(参数1：数据类型 参数2传递的json串)
		//json为String类型的json数据
		RequestBody requestBody = RequestBody.create(JSON, json);
		return requestBody;
	}


	/**
	 * 实现get请求
	 *
	 * @param reqUrl
	 * @param headersParams
	 * @param params
	 * @param callback
	 */
	public void doGet(String reqUrl, Map headersParams, Map params, final RequestNetCallBack
			callback) {
		Log.d("OkHttp3", "OkHttp3.doGet():" + reqUrl + setGetUrlParams(params));
		Builder RequestBuilder = new Builder();
		RequestBuilder.url(reqUrl + setGetUrlParams(params));// 添加URL地址 自行加 ?
		RequestBuilder.headers(SetHeaders(headersParams));// 添加请求头
		Request request = RequestBuilder.build();
		mOkHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(final Call call, final Response response) throws IOException {
				callback.onSuccess(response.body().string());
				call.cancel();
			}

			@Override
			public void onFailure(final Call call, final IOException exception) {
				callback.onFailure(exception);
				call.cancel();
			}
		});
	}

	public void doUpFiles(String reqUrl, Map BodyParams, Map filePathParams, final
	RequestNetCallBack
			callback) {
		RequestBody requestBody = SetFileRequestBody(BodyParams, filePathParams);
		Request request = new Request.Builder()
				.url(reqUrl)
				.post(requestBody)
				.build();
		mOkHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				callback.onFailure(e);
				call.cancel();
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				callback.onSuccess(response.body().string());
			}
		});
	}

}
