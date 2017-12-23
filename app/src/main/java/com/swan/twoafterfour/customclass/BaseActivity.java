package com.swan.twoafterfour.customclass;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.swan.twoafterfour.R;
import com.swan.twoafterfour.utils.SharedPreferencesManager;
import com.swan.twoafterfour.versionmanager.VersionManager;

import butterknife.ButterKnife;

/**
 * Created by TongYuliang on 2017/4/27.
 * 基础 Activity
 */

public abstract class BaseActivity extends AppCompatActivity {
	private final String TAG = "BaseActivity";

	/**
	 * 用户凭证
	 */
	public String token;

	/**
	 * 版本管理器
	 */
	public VersionManager versionManager;

	public FragmentManager Fm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		android.util.Log.d("BaseActivity", getClass().getSimpleName());
		setContentView(getLayoutResource());
		ButterKnife.bind(this);
		Fm = getSupportFragmentManager();
		initViews();

		token = SharedPreferencesManager.getString(getBaseContext(), "token", "");
		Log.d(TAG, "token:" + token);
	}

	protected abstract int getLayoutResource();

	/**
	 * 系统设置字体不对软件字体(sp)产生影响
	 *
	 * @return
	 */
	@Override
	public Resources getResources() {
		Resources res = super.getResources();
		Configuration config = new Configuration();
		config.setToDefaults();
		this.createConfigurationContext(config);
		return res;
	}

	protected abstract void initViews();

	/**
	 * 设置 Activity 背景半透明
	 */
	public void setBackgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha;
		getWindow().setAttributes(lp);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	}

	/**
	 * 添加fragment
	 */
	public void replaceFragment(BaseFragment fragment, Bundle bundle) {
		BaseFragment fg = (BaseFragment) Fm.findFragmentByTag(fragment.getClass().getSimpleName());
		if (fg == null) {
			fragment.setBundle(bundle);
			Fm.beginTransaction()
					.replace(R.id.activity_container, fragment, fragment.getClass()
							.getSimpleName())
					.addToBackStack(fragment.getClass().getSimpleName()).commitAllowingStateLoss();
			android.util.Log.d(TAG, "fragment tag: " + fragment.getClass().getSimpleName());
		} else {
			fg.setBundle(bundle);
			Fm.beginTransaction()
					.replace(R.id.activity_container, fg, fg.getClass().getSimpleName())
					.addToBackStack(fg.getClass().getSimpleName()).commitAllowingStateLoss();
		}
	}

	/**
	 * 从回退栈中抛出 fragment
	 */
	public void popBackStack() {
		Fm.popBackStack();
	}

	/**
	 * 从回退栈中抛出指定的 fragment 之上的其他 fragment (不包括该 fragment)
	 */
	public void popBackStack(String fragmentName) {
		Fm.popBackStack(fragmentName, 0);
	}

}
