package com.fy.tnzbsq.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.GameInfo;
import com.fy.tnzbsq.bean.Result;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.CustomWebViewDelegate;
import com.fy.tnzbsq.common.ServiceInterface;
import com.fy.tnzbsq.view.CustomWebView;
import com.umeng.analytics.MobclickAgent;

import org.kymjs.kjframe.ui.BindView;

import java.io.File;
import java.io.FileOutputStream;

public class SearchResultActivity extends BaseActivity implements CustomWebViewDelegate {

	@BindView(id = R.id.title_left_iv, click = true)
	private ImageView backImg;

	@BindView(id = R.id.search_key)
	private EditText searchKeyEv;
	
	@BindView(id = R.id.search_btn, click = true)
	private Button searchbtn;
	
	@BindView(id = R.id.webview)
	private CustomWebView customWebView;

	public String url = "";

	private AsyncTask<?, ?, ?> addKeppTask;
	
	private AsyncTask<?, ?, ?> searchTask;

	private String searchKey = "";
	
	@Override
	public void setRootView() {
		setContentView(R.layout.search_result);
	}
	
	@Override
	public void initWidget() {
		super.initWidget();
	}

	@Override
	public void initData() {
		super.initData();
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		if(bundle != null && bundle.getString("searchKey") != null && bundle.getString("searchKey").length() >0){
			searchKey = bundle.getString("searchKey");
		}
		
		if(searchKey != null && searchKey.length() > 0){
			searchKeyEv.setText(searchKey);
			searchKeyEv.setSelection(searchKey.length());
		}
		
		customWebView.delegate = this;
		//customWebView.loadUrl("file:///android_asset/search_list.html");

		searchTask = new SearchDataTask(searchKey,1).execute();
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
				Toast.makeText(SearchResultActivity.this, "请输入关键词搜索", Toast.LENGTH_SHORT).show();
				return;
			}
			searchKey = searchKeyEv.getText().toString();
			searchTask = new SearchDataTask(searchKey == null ? "" : searchKey,2).execute();
			
			break;
		default:
			break;
		}
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

	@Override
	public void showWheelView() {

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
		addKeppTask = new AddKeepTask(id, App.ANDROID_ID).execute();
	}

	@Override
	public void imageShow(String path) {

	}

	private class AddKeepTask extends AsyncTask<Void, Void, Result> {
		private String id;
		private String mime;

		public AddKeepTask(String id, String mime) {
			this.id = id;
			this.mime = mime;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Result doInBackground(Void... params) {
			return ServiceInterface.getAddKeepData(context, id, mime);
		}

		@Override
		protected void onPostExecute(Result result) {
			try {
				super.onPostExecute(result);

				if (Result.checkResult(context, result)) {
					Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "操作失败,请稍后重试!", Toast.LENGTH_SHORT).show();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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

	}

	@Override
	public void toShare() {

	}

	@Override
	public void initWithUrl(String url) {
		customWebView.loadUrl("javascript:init('"+searchKey+"');");
	}

	@Override
	public void updateUserName(String userName) {

	}

	@Override
	public void setShowState(boolean flag) {

	}

	@Override
	public void loadImageList(int currentPage) {
	}

	private class SearchDataTask extends AsyncTask<Void, Void, String> {

		private String keyword;
		private int type;
		public SearchDataTask(String keyword,int type) {
			this.keyword = keyword;
			this.type = type;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			return ServiceInterface.getSearchData(context, keyword);
		}

		@Override
		protected void onPostExecute(String result) {

			try {
				super.onPostExecute(result);

				if (result != null) {
					if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

						File fileDir = new File(Contants.ALL_DATA_DIR_PATH);
						if (!fileDir.exists()) {
							fileDir.mkdirs();
						}
						File file = new File(Contants.ALL_DATA_DIR_PATH, Contants.SEARCH_DATA_FILENAME);
						if (file.exists()) {
							file.delete();
						}
						// if (!file.exists()) {
						file.createNewFile();
						// }
						try {
							//result = " var data= " + result;// 重新封装
							FileOutputStream fos = new FileOutputStream(file);
							fos.write(result.getBytes());
							fos.close();
							//if(type ==2){
								
								//customWebView.loadUrl("javascript:init();");
							//}
//								if(type ==3){
//									customWebView.loadUrl("javascript:init('"+searchKey+"');");
//								}else{
									customWebView.loadUrl("file:///android_asset/search_list.html");
//								}
						} catch (Exception e) {
							Toast.makeText(context, "数据加载失败", Toast.LENGTH_SHORT).show();
						}
					} else {
						// 此时SDcard不存在或者不能进行读写操作的
						Toast.makeText(context, "数据加载失败", Toast.LENGTH_SHORT).show();
					}

				} else {
					Toast.makeText(context, "数据加载失败", Toast.LENGTH_SHORT).show();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void search(String keyword) {
		if(keyword==null ||keyword.length()==0){
			Toast.makeText(SearchResultActivity.this, "请输入关键词搜索", Toast.LENGTH_SHORT).show();
			return;
		}
		
//		Intent intent = new Intent(SearchResultActivity.this, SearchResultActivity.class);
//		intent.putExtra("searchKey", keyword.toString());
//		startActivity(intent);
		
		if(keyword==null ||keyword.length()==0){
			Toast.makeText(SearchResultActivity.this, "请输入关键词搜索", Toast.LENGTH_SHORT).show();
			return;
		}
		searchKey = keyword.toString();
		searchKeyEv.setText(keyword.toString());
		searchTask = new SearchDataTask(keyword == null ? "" : keyword.toString(),3).execute();
		
	}

	@Override
	public void photoGraph() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fightMenu() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gifResult(String url) {

	}
}
