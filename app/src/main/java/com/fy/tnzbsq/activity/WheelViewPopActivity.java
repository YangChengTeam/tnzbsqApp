package com.fy.tnzbsq.activity;

import java.util.ArrayList;

import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.GameInfo;
import com.fy.tnzbsq.common.CustomWebViewDelegate;
import com.fy.tnzbsq.view.CustomWebView;
import com.umeng.analytics.MobclickAgent;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.util.WheelUtils;
import com.wx.wheelview.widget.WheelView;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

public class WheelViewPopActivity extends Activity implements CustomWebViewDelegate {

	private CustomWebView customWebView;

	private PopupWindow popupWindow;
	private View popView;
	private EditText typeEdit;
	private WheelView typeWheel;

	private Button confirmBtn;
	private Button cancelBtn;
	private ArrayList<String> typeList;

	int currentPosition = 0;
	public String url = "";

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wheel_pop);

		customWebView = (CustomWebView) findViewById(R.id.webview);
		customWebView.delegate = this;
		customWebView.loadUrl("file:///android_asset/test.html");

		popView = this.getLayoutInflater().inflate(R.layout.pop_view, null);

		typeWheel = (WheelView) popView.findViewById(R.id.type_wheelview);
		confirmBtn = (Button) popView.findViewById(R.id.confirm_btn);
		cancelBtn = (Button) popView.findViewById(R.id.cancel_btn);

		createArrays();

		typeWheel.setWheelAdapter(new ArrayWheelAdapter(this));
		typeWheel.setWheelSize(5);
		typeWheel.setSkin(WheelView.Skin.Holo);
		typeWheel.setWheelData(typeList);

		typeWheel.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener<String>() {
			@Override
			public void onItemSelected(int position, String data) {
				currentPosition = position;
				WheelUtils.log("selected:" + position);
				// Toast.makeText(WheelViewPopActivity.this, "select" +
				// position, Toast.LENGTH_SHORT).show();
			}
		});

		popupWindow = new PopupWindow(popView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		popupWindow.setOutsideTouchable(true);
		// popupWindow.update();
		popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);

		typeEdit = (EditText) findViewById(R.id.type_edit);
		typeEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popupWindow != null && !popupWindow.isShowing()) {
					popupWindow.showAtLocation(typeEdit, Gravity.BOTTOM, 0, 0);
				} else {
					popupWindow.dismiss();
				}
			}
		});

		// 确认
		confirmBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popupWindow != null && popupWindow.isShowing()) {
					typeEdit.setText(typeList.get(currentPosition));
					// Toast.makeText(WheelViewPopActivity.this, "select---" +
					// typeList.get(currentPosition),
					// Toast.LENGTH_SHORT).show();
					popupWindow.dismiss();
					
					customWebView.loadUrl("javascript:showSelect('"+typeList.get(currentPosition)+"')");
				}
			}
		});

		// 取消
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});

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
	
	private void createArrays() {
		typeList = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			typeList.add("测试数据" + i);
		}
	}

	@Override
	public void Horizontal() {

	}

	@Override
	public void networkSet() {

	}

	@Override
	public void reload() {

	}

	@Override
	public void setTitle(String title) {

	}

	@Override
	public void hide() {

	}

	@Override
	public void show() {

	}

	@Override
	public void startFullActivity(GameInfo gameInfo) {
		
	}

	@Override
	public void setUrl(String url) {
		if (!url.contains("no-wifi")) {
			this.url = url;
		}
	}
	/**
	 * 展示wheelview控件
	 */
	@Override
	public void showWheelView() {
		//Toast.makeText(WheelViewPopActivity.this, "测试方法成功", Toast.LENGTH_SHORT).show();
		if (popupWindow != null && !popupWindow.isShowing()) {
			popupWindow.showAtLocation(typeEdit, Gravity.BOTTOM, 0, 0);
		} else {
			popupWindow.dismiss();
		}
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
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void photoGraph() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fightMenu() {
		// TODO Auto-generated method stub
		
	}

	
}
