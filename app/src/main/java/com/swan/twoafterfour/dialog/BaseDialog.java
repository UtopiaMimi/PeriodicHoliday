package com.swan.twoafterfour.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.ButterKnife;

/**
 * 基础对话框
 * Created by TongYuliang on 2017/10/16.
 */

public abstract class BaseDialog extends Dialog {

	private Context context;

	private Bundle bundle;

	public Bundle getBundle() {
		return bundle;
	}

	public BaseDialog setBundle(Bundle bundle) {
		this.bundle = bundle;
		return this;
	}

	public BaseDialog(@NonNull Context context, @StyleRes int themeResId) {
		super(context, themeResId);
		this.context = context;
	}

	protected abstract int getLayoutResource();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(getLayoutResource(), null);
		addContentView(view, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		ButterKnife.bind(this, view);
		initData(view);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	protected abstract void initData(View view);

}
