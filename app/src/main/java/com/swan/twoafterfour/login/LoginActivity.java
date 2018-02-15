package com.swan.twoafterfour.login;

import android.os.Bundle;

import com.swan.twoafterfour.R;
import com.swan.twoafterfour.broadcastreceiver.NetworkStatusBroadcastReceiver;
import com.swan.twoafterfour.customclass.BaseActivity;
import com.swan.twoafterfour.versionmanager.VersionManager;


/**
 * Created by TongYuliang on 2017/4/27.
 * 登录
 */

public class LoginActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected int getLayoutResource() {
		return R.layout.activity_base;
	}

	@Override
	protected void initial() {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 进入 Fragment
	 */
	private void enterLoginFragment() {
		LoginFragment fragment = new LoginFragment();
		Fm.beginTransaction()
				.add(R.id.activity_container, fragment, "LoginFragment")
				.commit();

		if (NetworkStatusBroadcastReceiver.isConnected) {
			if (versionManager == null) {
				versionManager = new VersionManager(this);
			}
			versionManager.requestRecentCode();
		}
	}

}
