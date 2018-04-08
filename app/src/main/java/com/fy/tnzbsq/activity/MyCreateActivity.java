package com.fy.tnzbsq.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.GameInfo;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.CustomWebViewDelegate;
import com.fy.tnzbsq.common.ServiceInterface;
import com.fy.tnzbsq.util.CustomUtils;
import com.fy.tnzbsq.view.CustomWebView;
import com.umeng.analytics.MobclickAgent;

import org.kymjs.kjframe.ui.BindView;

import java.io.File;
import java.io.FileOutputStream;

public class MyCreateActivity extends BaseActivity implements CustomWebViewDelegate {
	private static final String DIR_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TNZBSQ";
	private static final String FILENAME = "my_create_data.js";
	@BindView(id = R.id.back_img, click = true)
	private ImageView backImg;

	@BindView(id = R.id.top_title)
	private TextView titleNameTv;

	@BindView(id = R.id.webview)
	private CustomWebView customWebView;

	public String url = "";
	
	private AsyncTask<?, ?, ?> task;
	
	private ProgressDialog progressDialog;
	
	private Handler handler = new Handler();

	@Override
	public void setRootView() {
		setContentView(R.layout.my_create);
	}

	@Override
	public void initWidget() {
		super.initWidget();
	}

	@Override
	public void initData() {
		super.initData();
		customWebView.delegate = this;
		
		File file = new File(Contants.ALL_DATA_DIR_PATH, FILENAME);
		if (file.exists()) {
			loadUrl();
		}
		titleNameTv.setText(getResources().getString(R.string.create_name));
		task = new MyKeepDataTask().execute();
	}

	public void loadUrl() {
		if (CustomUtils.isNetworkAvailable(context)) {
			customWebView.post(new Runnable() {
				@Override
				public void run() {
					customWebView.loadUrl("file:///android_asset/mycreate.html");
					//customWebView.loadUrl("javascript:init();");
				}
			});

		} else {
			customWebView.post(new Runnable() {
				@Override
				public void run() {
					customWebView.loadUrl("file:///android_asset/no-wifi.html");
				}
			});
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

	}

	@Override
	public void networkSet() {

	}

	@Override
	public void reload() {

	}

	@Override
	public void setTitle(String title) {
		titleNameTv.setText(title);
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
	public void createImage(String id,String data,String isVip,String price) {
		
	}

	private class MyKeepDataTask extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			return ServiceInterface.getMyCreateData(context, App.ANDROID_ID);
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
						File file = new File(Contants.ALL_DATA_DIR_PATH, FILENAME);
						if (!file.exists()) {
							file.delete();
						}
						file.createNewFile();
						try {
							FileOutputStream fos = new FileOutputStream(file);
							fos.write(result.getBytes());
							fos.close();

						} catch (Exception e) {
							Toast.makeText(context, "获取数据失败,请稍后重试!", Toast.LENGTH_SHORT).show();
						}
					} else {
						// 此时SDcard不存在或者不能进行读写操作的
						Toast.makeText(context, "获取数据失败,请稍后重试!", Toast.LENGTH_SHORT).show();
					}

				} else {
					Toast.makeText(context, "获取数据失败,请稍后重试!", Toast.LENGTH_SHORT).show();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			customWebView.loadUrl("file:///android_asset/mycreate.html");
			customWebView.loadUrl("javascript:init()");
		}
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
	@Override
	public void gifResult(String url) {

	}
}
