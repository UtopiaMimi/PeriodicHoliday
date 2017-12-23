package com.swan.twoafterfour.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.swan.twoafterfour.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 通知类对话框
 * Created by TongYuliang on 2017/10/16.
 */

public class NoticeDialog extends BaseDialog implements View.OnClickListener {
	public static final String HINT = "hint";
	public static final String ACTION = "action";

	@BindView(R.id.notice_dialog_hint_tv)
	TextView hintTv;
	@BindView(R.id.notice_dialog_confirm_tv)
	TextView confirmTv;

	public NoticeDialog(@NonNull Context context) {
		super(context, R.style.Dialog);
	}

	@Override
	protected int getLayoutResource() {
		return R.layout.dialog_notice;
	}

	@Override
	protected void initData(View view) {
		hintTv.setText(getBundle().getString(HINT));
		confirmTv.setText(getBundle().getString(ACTION));
	}

	@OnClick(R.id.notice_dialog_confirm_tv)
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.notice_dialog_confirm_tv:
				dismiss();
				break;
			default:
				break;
		}
	}

}
