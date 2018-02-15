package com.swan.twoafterfour.customclass;

import android.support.multidex.MultiDexApplication;

import com.blankj.utilcode.util.Utils;

/**
 * Created by Swan on 2017/12/23 20:45.
 */

public class BaseApplication extends MultiDexApplication {
	@Override
	public void onCreate() {
		super.onCreate();
		Utils.init(this);
	}
}
