package com.swan.twoafterfour.customclass;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swan.twoafterfour.utils.ActivityManager;

import butterknife.ButterKnife;

/**
 * 基础 Fragment
 */

public abstract class BaseFragment extends Fragment {
	private final String TAG = "BaseFragment";

	private Bundle bundle;

	public Bundle getBundle() {
		return bundle == null ? new Bundle() : bundle;
	}

	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}

	public Context getBaseContext() {
		return getCurrentActivity().getBaseContext();
	}

	public BaseActivity getCurrentActivity() {
		return ActivityManager.getInstance().currentActivity();
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		Log.d(TAG, "onAttach: " + context.toString());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
			Bundle
			savedInstanceState) {
		View view = inflater.inflate(getLayoutId(), container, false);
		ButterKnife.bind(this, view);
		initView(view, savedInstanceState);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initEvent();
		requestData();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
//		getCurrentActivity().loadingGif.setVisibility(View.GONE);
	}

	/**
	 * 初始化数据
	 */
	protected abstract void initData();

	/**
	 * 从子类获取 LayoutRes
	 *
	 * @return LayoutRes Id
	 */
	protected abstract int getLayoutId();

	/**
	 * 初始化内部控件
	 *
	 * @param view
	 * @param savedInstanceState
	 */
	protected abstract void initView(View view, Bundle savedInstanceState);

	/**
	 * 初始化事件
	 */
	protected abstract void initEvent();

	/**
	 * 请求服务器数据
	 */
	protected abstract void requestData();

	public void replaceFragment(BaseFragment fragment, Bundle bundle) {
		getCurrentActivity().replaceFragment(fragment, bundle);
	}

	protected void popBackStack() {
		getCurrentActivity().popBackStack();
	}

	protected void popBackStack(String fragmentName) {
		getCurrentActivity().popBackStack(fragmentName);
	}

	protected int getColor(@ColorRes int colorRes) {
		return ContextCompat.getColor(getBaseContext(), colorRes);
	}

}
