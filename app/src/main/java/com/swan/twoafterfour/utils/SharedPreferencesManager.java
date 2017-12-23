package com.swan.twoafterfour.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * SharedPreference 管理器
 * Created by TongYuliang on 2017/8/9.
 */

public class SharedPreferencesManager {

	public static void putIntArray(Context context, String[] keys, int[] values) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		for (int i = 0; i < keys.length; i++) {
			editor.putInt(keys[i], values[i]);
		}
		editor.apply();
	}

	public static void putInt(Context context, String key, int value) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(key, value);
		editor.apply();
	}

	public static int getInt(Context context, String key, int defaultValue) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getInt(key, defaultValue);
	}

	public static void putLong(Context context, String key, long value) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		editor.putLong(key, value);
		editor.apply();
	}

	public static long getLong(Context context, String key, long defaultValue) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getLong(key, defaultValue);
	}

	public static void putString(Context context, String key, String value) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		editor.apply();
	}

	public static void putStringArray(Context context, String[] keys, String[] values) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		for (int i = 0; i < keys.length; i++) {
			editor.putString(keys[i], values[i]);
		}
		editor.apply();
	}

	public static String getString(Context context, String key, String defaultValue) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(key, defaultValue);
	}

	public static void putBoolean(Context context, String key, boolean value) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.apply();
	}

	public static boolean getBoolean(Context context, String key) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getBoolean(key, false);
	}

	public static boolean contains(Context context, String key) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.contains(key);
	}

	public static void remove(Context context, String key) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		editor.apply();
	}

}
