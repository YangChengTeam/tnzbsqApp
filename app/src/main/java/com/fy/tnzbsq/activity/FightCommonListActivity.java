package com.fy.tnzbsq.activity;

import java.io.File;
import java.io.FileOutputStream;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.GameInfo;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.CustomWebViewDelegate;
import com.fy.tnzbsq.common.ServiceInterface;
import com.fy.tnzbsq.view.CustomWebView;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FightCommonListActivity extends KJActivity implements CustomWebViewDelegate, OnRefreshListener {

	@BindView(id = R.id.back_img, click = true)
	private ImageView backImg;

	@BindView(id = R.id.top_title)
	private TextView titleNameTv;

	@BindView(id = R.id.webview)
	private CustomWebView customWebView;

	@BindView(id = R.id.swipe)
	private SwipeRefreshLayout swipeLayout;

	private String sortType = "";

	@Override
	public void setRootView() {
		setContentView(R.layout.fight_common_list);
	}

	@Override
	public void initData() {
		super.initData();

		swipeLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light,
				android.R.color.holo_orange_light, android.R.color.holo_blue_light);
		swipeLayout.setOnRefreshListener(this);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		if (bundle != null && bundle.getString("sortType") != null && bundle.getString("sortType").length() > 0) {
			sortType = bundle.getString("sortType");
		}

		if (sortType.equals("add_time")) {
			titleNameTv.setText(getResources().getString(R.string.new_name));
		} else if (sortType.equals("num")) {
			titleNameTv.setText(getResources().getString(R.string.hot_name));
		} else if (sortType.equals("type")) {
			titleNameTv.setText(getResources().getString(R.string.gif_name));
		}else {
			titleNameTv.setText(getResources().getString(R.string.fight_text));
		}

		customWebView.delegate = this;
		customWebView.loadUrl("file:///android_asset/index-dt-common.html");
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
		customWebView.post(new Runnable() {
			@Override
			public void run() {
				customWebView.reload();
			}
		});
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
		customWebView.loadUrl("javascript:init('" + sortType + "');");
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
		
	}

	@Override
	public void onRefresh() {
		new FightAllDataTask().execute();
	}

	/**
	 * 获取所有装逼类数据
	 * 
	 * @author admin
	 *
	 */
	private class FightAllDataTask extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			return ServiceInterface.getAllFightData(FightCommonListActivity.this);
		}

		@Override
		protected void onPostExecute(String result) {

			try {
				super.onPostExecute(result);

				swipeLayout.setRefreshing(false);
				
				if (result != null) {
					if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

						File fileDir = new File(Contants.ALL_DATA_DIR_PATH);
						if (!fileDir.exists()) {
							fileDir.mkdirs();
						}
						File file = new File(Contants.ALL_DATA_DIR_PATH, Contants.ALL_FIGHT_DATA_FILENAME);
						if (file.exists()) {
							file.delete();
						}
						// if (!file.exists()) {
						file.createNewFile();
						// }
						try {
							result = " var data= " + result;// 重新封装
							FileOutputStream fos = new FileOutputStream(file);
							fos.write(result.getBytes());
							fos.close();
							// Toast.makeText(MainActivity.this, "写入文件成功",
							// Toast.LENGTH_LONG).show();

							// 最后通知更新文件
							if (file != null) {
								FightCommonListActivity.this
										.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
												Uri.parse("file://" + file.getAbsolutePath())));
							}
						} catch (Exception e) {
							Toast.makeText(FightCommonListActivity.this, "数据加载失败", Toast.LENGTH_SHORT).show();
						}
						reload();
					} else {
						// 此时SDcard不存在或者不能进行读写操作的
						Toast.makeText(FightCommonListActivity.this, "数据加载失败", Toast.LENGTH_SHORT).show();
					}

				} else {
					Toast.makeText(FightCommonListActivity.this, "数据加载失败", Toast.LENGTH_SHORT).show();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
