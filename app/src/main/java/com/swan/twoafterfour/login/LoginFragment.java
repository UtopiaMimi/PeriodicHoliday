package com.swan.twoafterfour.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.swan.twoafterfour.R;
import com.swan.twoafterfour.broadcastreceiver.NetworkStatusBroadcastReceiver;
import com.swan.twoafterfour.globalmethod.GlobalMethod;
import com.swan.twoafterfour.netutil.OkHttp3Util;
import com.swan.twoafterfour.netutil.RequestObjectNetCallBack;
import com.swan.twoafterfour.staticconstant.GlobalData;
import com.swan.twoafterfour.staticconstant.PostEvent;
import com.swan.twoafterfour.utils.SharedPreferencesManager;
import com.swan.twoafterfour.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by TongYuliang on 2017/4/27.
 * 登录
 */

public class LoginFragment extends Fragment implements View.OnClickListener {
	EditText numberEt;
	EditText verificationCodeEt;
	Button verificationCodeBtn;
	Button enterBtn;
	TextView agreementTv;

	private String IMEI;
	private String queueId;
	private TimeCount timeCount;

	LoginActivity Act;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Act = (LoginActivity) getActivity();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		setTopAndBottom();
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		initialView(view);
		initialEvent();
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		IMEI = SharedPreferencesManager.getString(Act, Act.getString(R.string.IMEI), "");
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void setTopAndBottom() {
	}

	/**
	 * 初始化组件
	 *
	 * @param view
	 */
	private void initialView(View view) {
		numberEt = (EditText) view.findViewById(R.id.login_phone_number_et);
		verificationCodeEt = (EditText) view.findViewById(R.id.login_verification_code_et);
		verificationCodeBtn = (Button) view.findViewById(R.id.login_obtain_verification_code_btn);
		enterBtn = (Button) view.findViewById(R.id.login_enter_btn);
		agreementTv = (TextView) view.findViewById(R.id.login_agreement_tv);
	}

	private void initialEvent() {
		verificationCodeBtn.setOnClickListener(this);
		enterBtn.setOnClickListener(this);
		agreementTv.setOnClickListener(this);
		/*
		setOnClickListener() 会将 CLICKABLE 设置为 true，需要重新设置为 false
		 */
		verificationCodeBtn.setClickable(false);
		enterBtn.setClickable(false);

		EditTextWatcher numberWatcher = new EditTextWatcher(1);
		numberEt.addTextChangedListener(numberWatcher);
		EditTextWatcher verificationCodeWatcher = new EditTextWatcher(2);
		verificationCodeEt.addTextChangedListener(verificationCodeWatcher);

	}

	@Override
	public void onClick(View v) {
		String number;
		switch (v.getId()) {
			case R.id.login_obtain_verification_code_btn:
				verificationCodeEt.requestFocus();
				number = numberEt.getText().toString();
				if (GlobalMethod.checkFormat(number)) {
					getVerificationCode();
				} else {
					ToastUtils.showToast(Act, getString(R.string.phone_number_error));
				}
				break;
			case R.id.login_enter_btn:
				number = numberEt.getText().toString();
				if (GlobalMethod.checkFormat(number)) {
					validateVerificationCode();
				} else {
					ToastUtils.showToast(Act, getString(R.string.phone_number_error));
				}
				break;
			case R.id.login_agreement_tv:
				// 用户条款
				enterServiceAgreementFragment();
				break;
			default:
				break;
		}
	}

	/**
	 * 获取验证码
	 */
	private void getVerificationCode() {
		if (!NetworkStatusBroadcastReceiver.isConnected) {
			ToastUtils.showToast(Act, Act.getString(R.string.no_network));
			return;
		}

//		if (Act.loadingGif.getVisibility() == View.VISIBLE) {
//			return;
//		}
//		Act.loadingGif.setVisibility(View.VISIBLE);

		int countdown = 60000;
		timeCount = new TimeCount(countdown, 1000);
		timeCount.start();
		System.out.println("numberEt.getText().toString():" + numberEt.getText().toString());
		System.out.println("IMEI:" + IMEI);
		OkHttp3Util util = OkHttp3Util.getInstance(Act);
		LoginRequestNetCallBack callback = new LoginRequestNetCallBack(1);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("AccountName", numberEt.getText().toString());
			jsonObject.put("AccountType", 1);
			jsonObject.put("IMEI", IMEI);
			jsonObject.put("DeviceType", 0);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		util.doPostByJson(GlobalData.REMOTE_SERVER_ADDRESS + "api/Login/sendcode",
				jsonObject.toString(), callback);
	}

	/**
	 * 验证验证码的有效性
	 */
	private void validateVerificationCode() {
		if (!NetworkStatusBroadcastReceiver.isConnected) {
			ToastUtils.showToast(Act, Act.getString(R.string.no_network));
			return;
		}

//		if (Act.loadingGif.getVisibility() == View.VISIBLE) {
//			return;
//		}
//		Act.loadingGif.setVisibility(View.VISIBLE);

		OkHttp3Util util = OkHttp3Util.getInstance(Act);
		LoginRequestNetCallBack callback = new LoginRequestNetCallBack(2);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("Account", numberEt.getText().toString());
			jsonObject.put("AccountType", 1);
			jsonObject.put("VerificationCode", verificationCodeEt.getText().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		util.doPostByJson(GlobalData.REMOTE_SERVER_ADDRESS + "api/Login/Verificat",
				jsonObject.toString(), callback);
	}

	/**
	 * 登录
	 */
	private void login() {
		OkHttp3Util util = OkHttp3Util.getInstance(Act);
		LoginRequestNetCallBack callback = new LoginRequestNetCallBack(3);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("QueueId", queueId);
			jsonObject.put("AccountName", numberEt.getText().toString());
			jsonObject.put("AccountType", 1);
			jsonObject.put("VerificationCode", verificationCodeEt.getText().toString());
			jsonObject.put("IMEI", IMEI);
			jsonObject.put("DeviceType", 0);
			jsonObject.put("Nick", "");
			jsonObject.put("Password", "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		util.doPostByJson(GlobalData.REMOTE_SERVER_ADDRESS + "api/Login/Login",
				jsonObject.toString(), callback);
	}

	/**
	 * 登录成功
	 *
	 * @param token       用户 token
	 * @param isFirstTime 是否是第一次登录
	 */
	private void loginSuccessful(String token, boolean isFirstTime) {
		Bundle bundle = new Bundle();
		bundle.putInt(PostEvent.TAG, PostEvent.LOGIN_SUCCESS);
		bundle.putString(PostEvent.TAG, token);
		bundle.putBoolean(PostEvent.TAG, isFirstTime);
		EventBus.getDefault().post(bundle);
		Act.finish();
	}

	/**
	 * 输入框监听器
	 */
	private class EditTextWatcher implements TextWatcher {
		int action;

		EditTextWatcher(int action) {
			this.action = action;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			switch (action) {
				case 1:
					numberChanged(s);
					break;
				case 2:
					verificationCodeChanged(s);
					break;
				default:
					break;
			}
		}

		private void numberChanged(Editable s) {
			if (timeCount != null) {
				return;
			}
			if (s.length() == 11) {
				verificationCodeBtn.setClickable(true);
				verificationCodeBtn.setBackgroundResource(R.drawable.juxing_orange);
			} else {
				verificationCodeBtn.setClickable(false);
				verificationCodeBtn.setBackgroundResource(R.drawable.juxing_gray_small);
			}
		}

		private void verificationCodeChanged(Editable s) {
			if (s.length() == 0) {
				enterBtn.setClickable(false);
				enterBtn.setBackgroundResource(R.drawable.juxing_gray_big);
			} else {
				enterBtn.setClickable(true);
				enterBtn.setBackgroundResource(R.drawable.juxing_blue);
			}
		}

	}

	/**
	 * 重置密码——发送验证码倒计时
	 */
	private class TimeCount extends CountDownTimer {

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			verificationCodeBtn.setClickable(false);
			verificationCodeBtn.setText("(" + millisUntilFinished / 1000 + "S)");
			verificationCodeBtn.setBackgroundResource(R.drawable.juxing_gray_small);
		}

		@Override
		public void onFinish() {
			verificationCodeBtn.setText(Act.getString(R.string.login_obtain_verification_code));
			if (numberEt.getText().toString().length() == 11) {
				verificationCodeBtn.setClickable(true);
				verificationCodeBtn.setBackgroundResource(R.drawable.juxing_orange);
			}
			System.out.println("timeCount:" + timeCount);
			timeCount = null;
		}
	}

	private class LoginRequestNetCallBack extends RequestObjectNetCallBack<String> {
		int action;

		public LoginRequestNetCallBack(int action) {
			this.action = action;
		}

		@Override
		protected void onFailed(int errorCode, String message) {
			Act.runOnUiThread(() -> {
//				Act.loadingGif.setVisibility(View.GONE);
				ToastUtils.showToast(Act, message);
			});
		}

		@Override
		protected void onSuccessful(String data) {
			try {
				switch (action) {
					case 1:
						getVerificationCodeSuccessful();
						break;
					case 2:
						validateVerificationCodeSuccessful(new JSONObject(data));
						break;
					case 3:
						loginSuccessful(new JSONObject(data));
						break;
					default:
						break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		/**
		 * 获取验证码
		 */
		private void getVerificationCodeSuccessful() {
			/*
			{"Data":"成功","Code":0,"Message":null,"successful":true}
			 */
			Act.runOnUiThread(() -> {
//				Act.loadingGif.setVisibility(View.GONE);
				ToastUtils.showToast(Act, getString(R.string.send_successful));
			});
		}

		/**
		 * 验证验证码有效性成功
		 *
		 * @param data
		 */
		private void validateVerificationCodeSuccessful(JSONObject data) throws JSONException {
			queueId = data.getString("QueueId");
			Act.runOnUiThread(LoginFragment.this::login);
		}

		/**
		 * 登录成功
		 *
		 * @param result
		 */
		private void loginSuccessful(JSONObject result) throws JSONException {
			JSONObject data = result.getJSONObject("Data");
			final String token = data.getString("Token");
			final boolean isFirstTime = data.getBoolean("IsFirstTime");
			String accountName = data.getString("AccountName");
			int identity = data.getInt("Identity");
			JSONObject profileJson = data.getJSONObject("Profile");
			SharedPreferencesManager.putInt(Act, "identity", identity);
			SharedPreferencesManager.putStringArray(Act,
					new String[]{"token", "accountName", "profile"},
					new String[]{token, accountName, profileJson.toString()});

			Act.runOnUiThread(() -> {
//				Act.loadingGif.setVisibility(View.GONE);
				LoginFragment.this.loginSuccessful(token, isFirstTime);
			});
		}

	}

	/**
	 * 进入服务协议界面
	 */
	private void enterServiceAgreementFragment() {
		Fragment fg = Act.Fm.findFragmentByTag("ServiceAgreementFragment");
		FragmentTransaction Ft = Act.Fm.beginTransaction();
		Ft.addToBackStack(null);
		if (fg == null) {
			ServiceAgreementFragment fragment = new ServiceAgreementFragment();
			Ft.replace(R.id.activity_container, fragment, "ServiceAgreementFragment");
		} else {
			Ft.replace(R.id.activity_container, fg);
		}
		Ft.commit();
	}

}
