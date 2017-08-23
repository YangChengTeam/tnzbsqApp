package com.fy.tnzbsq.activity;

import com.fy.tnzbsq.R;
import com.umeng.analytics.MobclickAgent;

import android.view.View;

public class CreateActivity extends BaseActivity {
	
	@Override
	public void setRootView() {
		setFinishOnTouchOutside(false);
		setContentView(R.layout.pop_window_create);
	}
	
	@Override
	public void initData() {
		super.initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	public void initWidget() {
		super.initWidget();
	}
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.close_layout:
			finish();
			break;
		default:
			break;
		}
	}
	
}
