package com.fy.tnzbsq.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.GameInfo;
import com.fy.tnzbsq.common.CustomWebViewDelegate;
import com.fy.tnzbsq.common.ServiceInterface;
import com.fy.tnzbsq.view.CustomWebView;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

public class SearchActivity extends KJActivity implements CustomWebViewDelegate{ 

	@BindView(id = R.id.title_left_iv, click = true)
	private ImageView backImg;
	
	@BindView(id = R.id.search_key)
	private EditText searchKeyEv;
	
	@BindView(id = R.id.search_btn, click = true)
	private Button searchbtn;
	
	@BindView(id = R.id.webview)
	private CustomWebView customWebView;
	
	private AsyncTask<?, ?, ?> task;

	private Handler handler = new Handler();

	private String temp ="";

	@Override
	public void setRootView() {
		setContentView(R.layout.search);
	}

	@Override
	public void initData() {
		super.initData();
		customWebView.delegate = this;
		customWebView.loadUrl("file:///android_asset/search.html");
		//task = new HotDataTask().execute();
	}

	@Override
	protected void onResume() {
		super.onResume();
		customWebView.loadUrl("javascript:template2();");
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);

		switch (v.getId()) {
		case R.id.title_left_iv:
			finish();
			break;
		case R.id.search_btn:
			if(searchKeyEv.getText()==null ||searchKeyEv.getText().toString().length()==0){
				Toast.makeText(SearchActivity.this, "请输入关键词搜索", Toast.LENGTH_SHORT).show();
				return;
			}
			
			Intent intent = new Intent(SearchActivity.this, SearchResult1Activity.class);
			intent.putExtra("searchKey", searchKeyEv.getText().toString());
			startActivity(intent);
			
			break;
		default:
			break;
		}
	}

	/**
	 * 获取热门搜索数据
	 *
	 */
	private class HotDataTask extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			return ServiceInterface.getHotData(SearchActivity.this);
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				super.onPostExecute(result);
				if (result != null) {
					//customWebView.loadUrl("javascript:initHot('"+result+"');");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
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
		temp = keyword.toString();
		if(keyword==null ||keyword.length()==0){
			Toast.makeText(SearchActivity.this, "请输入关键词搜索", Toast.LENGTH_SHORT).show();
			return;
		}

		customWebView.loadUrl("javascript:addHotWord('"+temp+"');");

		Intent intent = new Intent(SearchActivity.this, SearchResult1Activity.class);
		intent.putExtra("searchKey", keyword.toString());
		startActivity(intent);
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
