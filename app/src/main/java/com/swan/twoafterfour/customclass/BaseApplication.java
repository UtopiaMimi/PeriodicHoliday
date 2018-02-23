package com.swan.twoafterfour.customclass;

import android.support.multidex.MultiDex;

import com.blankj.utilcode.util.Utils;

import org.litepal.LitePalApplication;

/**
 * Application 类
 * Created by Swan on 2017/12/23 20:45.
 */

public class BaseApplication extends LitePalApplication {
	@Override
	public void onCreate() {
		super.onCreate();
		MultiDex.install(this);
		Utils.init(this);
	}
}
