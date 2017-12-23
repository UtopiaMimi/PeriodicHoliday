package com.swan.twoafterfour.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.swan.twoafterfour.R;

import butterknife.ButterKnife;

/**
 * 新版本对话框
 * Created by TongYuliang on 2017/8/8.
 */

public class NewVersionDialog extends Dialog {
	public LinearLayout introductionLl;
	public TextView introductionTv;
	public TextView updatingTv;
	public LinearLayout oneOptionUpdateLl;
	public LinearLayout oneOptionCancelLl;
	public LinearLayout twoOptionLl;
	public TextView anotherTimeTv;
	public TextView updateNowTv;

	private NewVersionDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {

		private Context context;

		public Builder(Context context) {
			this.context = context;
		}

		/**
		 * Create the custom newCouponDialog
		 */
		public NewVersionDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the newCouponDialog with the custom Theme
			NewVersionDialog dialog = new NewVersionDialog(context, R.style.Dialog);
			View view = inflater.inflate(
					R.layout.dialog_new_version, null);
			dialog.addContentView(view, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams
					.WRAP_CONTENT));

			dialog.introductionLl = ButterKnife.findById(view,
					R.id.dialog_new_version_introduction_ll);
			dialog.introductionTv = ButterKnife.findById(view,
					R.id.dialog_new_version_introduction_tv);
			dialog.oneOptionUpdateLl = ButterKnife.findById(view,
					R.id.dialog_new_version_one_option_update_now_ll);
			dialog.updatingTv = ButterKnife.findById(view,
					R.id.dialog_new_version_updating_tv);
			dialog.oneOptionCancelLl = ButterKnife.findById(view,
					R.id.dialog_new_version_one_option_cancel_ll);
			dialog.twoOptionLl = ButterKnife.findById(view,
					R.id.dialog_new_version_two_option_ll);
			dialog.anotherTimeTv = ButterKnife.findById(view,
					R.id.dialog_new_version_two_option_another_time_tv);
			dialog.updateNowTv = ButterKnife.findById(view,
					R.id.dialog_new_version_two_option_update_now_tv);
			dialog.introductionTv.setMovementMethod(ScrollingMovementMethod.getInstance());

			return dialog;
		}
	}

}
