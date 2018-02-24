package com.swan.twoafterfour.popupwindow;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swan.twoafterfour.R;
import com.swan.twoafterfour.customclass.BasePopupWindow;
import com.swan.twoafterfour.dialog.SetPeriodDialog;
import com.swan.twoafterfour.utils.KeyboardUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Swan on 2018/2/24 15:13.
 */

public class OperationPopupWindow extends BasePopupWindow {
	private Context context;
	private View view;

//	@BindView(R.id.operation_set_periodic_day_off_tv)
//	TextView setperiodicDayOffTv;
//	@BindView(R.id.operation_cancel_tv)
//	TextView cancelTv;

	public OperationPopupWindow(Context context) {
		super(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
				android.R.style.Animation_InputMethod);
		this.context = context;
		view = LayoutInflater.from(context).inflate(R.layout.popupwindow_operation, null,
				false);
		setContentView(view);
		ButterKnife.bind(this, view);
	}

	public void show() {
		showAtLocation(view, Gravity.BOTTOM, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
				.LayoutParams.WRAP_CONTENT);
	}

	@OnClick({R.id.operation_set_periodic_day_off_tv, R.id.operation_cancel_tv})
	protected void onClick(View v) {
		switch (v.getId()) {
			case R.id.operation_set_periodic_day_off_tv:
				new SetPeriodDialog(context).show();
				KeyboardUtils.showKeyBoard(context);
				dismiss();
				break;
			case R.id.operation_cancel_tv:
				dismiss();
				break;
			default:
				break;
		}
	}

}
