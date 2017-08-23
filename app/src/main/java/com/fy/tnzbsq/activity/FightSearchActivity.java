package com.fy.tnzbsq.activity;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.GameInfo;
import com.fy.tnzbsq.common.CustomWebViewDelegate;
import com.fy.tnzbsq.view.CustomWebView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class FightSearchActivity extends KJActivity implements CustomWebViewDelegate{ 

	@BindView(id = R.id.back_img, click = true)
	private ImageView backImg;
	
	@BindView(id = R.id.top_title)
	private TextView titleNameTv;
	
	@BindView(id = R.id.webview)
	private CustomWebView customWebView;
	
	private String keyword = "";
	
	@Override
	public void setRootView() {
		setContentView(R.layout.fight_search);
	}
	
	@Override
	public void initData() {
		super.initData();
		
		titleNameTv.setText(getResources().getString(R.string.search_result_text));
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		if(bundle != null && bundle.getString("keyword") != null && bundle.getString("keyword").length() >0){
			keyword = bundle.getString("keyword");
		}
		
		customWebView.delegate = this;
		customWebView.loadUrl("file:///android_asset/index-dt-search.html");
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);

		switch (v.getId()) {
		case R.id.back_img:
			finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void Horizontal() {
		// TODO Auto-generated method stub
	}

	@Override
	public void networkSet() {
		// TODO Auto-generated method stub
	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub
	}

	@Override
	public void setTitle(String title) {
		// TODO Auto-generated method stub
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	@Override
	public void startFullActivity(GameInfo gameInfo) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setUrl(String url) {
		// TODO Auto-generated method stub
	}

	@Override
	public void showWheelView() {
		// TODO Auto-generated method stub
	}

	@Override
	public void createImage(String id, String data,String isVip,String price) {
		// TODO Auto-generated method stub
	}

	@Override
	public void saveImage(String imageRealPath) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addKeep(String id) {
		// TODO Auto-generated method stub
	}

	@Override
	public void imageShow(String path) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateVersion() {
		// TODO Auto-generated method stub
	}

	@Override
	public void selectPic(int xvalue, int yvalue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void submitMesage(String str, String description) {
		// TODO Auto-generated method stub
	}

	@Override
	public void clearCache() {
		// TODO Auto-generated method stub
	}

	@Override
	public void toSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void toShare() {
		// TODO Auto-generated method stub
	}

	@Override
	public void initWithUrl(String url) {
		customWebView.loadUrl("javascript:init('" + keyword + "');");
	}

	@Override
	public void updateUserName(String userName) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setShowState(boolean flag) {
		// TODO Auto-generated method stub
	}

	@Override
	public void loadImageList(int currentPage) {
		// TODO Auto-generated method stub
	}

	@Override
	public void search(String keyword) {
		
	}

	@Override
	public void photoGraph() {
		
	}

	@Override
	public void fightMenu() {
		// TODO Auto-generated method stub
		
	}

}
