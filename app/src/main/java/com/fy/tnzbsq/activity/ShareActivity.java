package com.fy.tnzbsq.activity;

import org.kymjs.kjframe.ui.BindView;

import com.fy.tnzbsq.R;
import com.fy.tnzbsq.view.CustomWebView;
import com.umeng.analytics.MobclickAgent;

import android.widget.ImageView;
import android.widget.TextView;

public class ShareActivity extends BaseActivity {

	@BindView(id = R.id.back_img, click = true)
	private ImageView backImg;

	@BindView(id = R.id.top_title)
	private TextView titleNameTv;

	@BindView(id = R.id.webview)
	private CustomWebView customWebView;

	@Override
	public void setRootView() {
		setContentView(R.layout.share);
	}

	@Override
	public void initWidget() {
		super.initWidget();
	}

	@Override
	public void initData() {
		super.initData();
		
		titleNameTv.setText(getResources().getString(R.string.share_text));
		customWebView.loadUrl("file:///android_asset/share.html");

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

}
