package com.swan.twoafterfour.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.swan.twoafterfour.R;
import com.swan.twoafterfour.customclass.BaseDialog;
import com.swan.twoafterfour.staticconstant.PostEvent;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Swan on 2018/2/24 17:39.
 */

public class SetPeriodDialog extends BaseDialog {

	@BindView(R.id.dialog_set_period_input_et)
	EditText inputEt;

	public SetPeriodDialog(@NonNull Context context) {
		super(context, R.style.Dialog);
	}

	@Override
	protected int getLayoutResource() {
		return R.layout.dialog_set_period;
	}

	@Override
	protected void initData(View view) {

	}

	@OnClick(R.id.dialog_set_period_confirm_tv)
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.dialog_set_period_confirm_tv:
				Bundle bundle = new Bundle();
				bundle.putInt(PostEvent.TAG, PostEvent.SET_PERIOD);
				EventBus.getDefault().post(bundle);
				dismiss();
				break;
			default:
				break;
		}
	}

}
