package com.swan.twoafterfour.customclass;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.swan.twoafterfour.utils.ActivityManager;
import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;

/**
 * 基础 Fragment
 */

public abstract class BaseFragment extends RxFragment {
	private final String TAG = "BaseFragment";

	private Bundle bundle;

	public BaseActivity activity;

	public View mRootView;

	private boolean isActive;

	public Bundle getBundle() {
		return bundle == null ? new Bundle() : bundle;
	}

	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}

	public boolean isActive() {
		return isActive;
	}

	public Context getBaseContext() {
		return getCurrentActivity().getBaseContext();
	}

	public BaseActivity getCurrentActivity() {
		return ActivityManager.getInstance().currentActivity();
	}

//	public GifView getLoadingGif() {
//		return getCurrentActivity().loadingGif;
//	}
//
//	public void setLoadingGifVisible() {
//		getLoadingGif().setVisibility(View.VISIBLE);
//	}
//
//	public void setLoadingGifGone() {
//		getLoadingGif().setVisibility(View.GONE);
//	}
//
//	public WinShareRoomService getService() {
//		return getCurrentActivity().mService;
//	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		LogUtils.d(TAG, "onAttach: " + context.toString());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView: ");
		mRootView = inflater.inflate(getLayoutId(), container, false);
		ButterKnife.bind(this, mRootView);
		initView(mRootView, savedInstanceState);
		return mRootView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		Log.d(TAG, "onViewCreated: ");
		super.onViewCreated(view, savedInstanceState);
		initEvent();
		requestData();
	}

	@Override
	public void onResume() {
		super.onResume();
		isActive = true;
		Log.d(TAG, "onResume: isActive = true");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "onPause: isActive = false");
		isActive = false;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (getCurrentActivity() != null) {
//			getCurrentActivity().loadingGif.setVisibility(View.GONE);
		}
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
