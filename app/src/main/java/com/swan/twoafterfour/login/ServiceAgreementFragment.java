package com.swan.twoafterfour.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.swan.twoafterfour.R;
import com.swan.twoafterfour.customclass.BaseActivity;
import com.swan.twoafterfour.staticconstant.GlobalData;


/**
 * Created by Administrator on 2017/5/8.
 */

public class ServiceAgreementFragment extends Fragment implements View.OnClickListener {

	BaseActivity act;
	WebView webView;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		act = (BaseActivity) getActivity();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
			Bundle savedInstanceState) {
		setTopAndBottom();
		View view = inflater.inflate(R.layout.fragment_service_agrement, container, false);
		initialView(view);
		initialEvent();
		getData();
		return view;
	}

	private void setTopAndBottom() {
	}

	private void initialView(View view) {
		webView = (WebView) view.findViewById(R.id.wv_web);
	}

	private void initialEvent() {
	}

	private void getData() {
		//webview展示对应页面
		webView.loadUrl(GlobalData.REMOTE_SERVER_ADDRESS + "service.html");
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.left_top_image_fl_on_bar:
				act.Fm.popBackStack();
				break;
			default:
				break;
		}
	}
}
