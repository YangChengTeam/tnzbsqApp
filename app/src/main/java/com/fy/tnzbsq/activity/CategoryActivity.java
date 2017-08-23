package com.fy.tnzbsq.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.Acts;
import com.fy.tnzbsq.bean.GameInfo;
import com.fy.tnzbsq.bean.Result;
import com.fy.tnzbsq.common.CustomWebViewDelegate;
import com.fy.tnzbsq.common.ServiceInterface;
import com.fy.tnzbsq.view.CustomProgress;
import com.fy.tnzbsq.view.CustomWebView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.utils.Log;

import org.kymjs.kjframe.ui.BindView;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends BaseActivity implements CustomWebViewDelegate, OnRefreshListener{

	@BindView(id = R.id.back_img, click = true)
	private ImageView backImg;

	@BindView(id = R.id.top_title)
	private TextView titleNameTv;

	@BindView(id = R.id.share_img, click = true)
	private ImageView shareImg;

	@BindView(id = R.id.share_text, click = true)
	private TextView shareTv;

	@BindView(id = R.id.webview)
	private CustomWebView customWebView;
	@BindView(id = R.id.swipe)
	private SwipeRefreshLayout swipeLayout;
	
	public String url = "";

	private AsyncTask<?, ?, ?> addKeppTask;
	
	//private DownloadService service;

	private int currentPage = 1;

	private List<String> imageList;

	private List<Acts> actsList;
	
	private List<Acts> tempList = new ArrayList<Acts>();
	
	private String tags = "";

	private String column = "";

	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 3:
				//if (service != null) {
					// service.cancelTask();
				//}
				customWebView.loadUrl("javascript:refresh();");
				// Toast.makeText(getActivity(), "下载完成",
				// Toast.LENGTH_SHORT).show();
				break;
			case 4:
				// Toast.makeText(getActivity(), "下载失败",
				// Toast.LENGTH_SHORT).show();
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	@Override
	public void setRootView() {
		setContentView(R.layout.category);
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

		if(bundle != null && bundle.getString("tags") != null && bundle.getString("tags").length() >0){
			tags = bundle.getString("tags");
		}

		if(bundle != null && bundle.getString("column") != null && bundle.getString("column").length() >0){
			column = bundle.getString("column");
		}

		Log.e("category--","tags--"+tags+"--column--"+column);

		if(column.equals("1")){
			titleNameTv.setText(tags+"-"+getResources().getString(R.string.app_name));
		}else{
			titleNameTv.setText(tags);
		}

		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorSchemeResources(
				android.R.color.holo_red_light,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light, 
				android.R.color.holo_blue_light);
		customWebView.delegate = this;
		customWebView.loadUrl("file:///android_asset/category_list.html");

		/*handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				customWebView.loadUrl("javascript:init('" + tags + "');");
			}
		}, 1000);*/
		CustomProgress dialog = CustomProgress.create(CategoryActivity.this, "正在分享...", true, null);
        dialog.setTitle("装B神器分享");
        
        /*String tempStr = FileUtil.readFile(Contants.ALL_DATA_DIR_PATH + "/" + Contants.ALL_DATA_FILENAME);
		tempStr = tempStr.replaceAll("var data=", " ");
	
		ActsRet ar = Contants.gson.fromJson(tempStr, new TypeToken<ActsRet>() {}.getType());
		
		actsList = ar.data;
		
		if (actsList == null || actsList.size() == 0) {
			actsList = new ArrayList<Acts>();
		}
		
		for(int i=0;i<actsList.size();i++){
			if(actsList.get(i).classtype.equals(tags)){
				tempList.add(actsList.get(i));
			}
		}
		
		imageList = new ArrayList<String>();
		
		cacheImage();*/
	}

	/**
	 * 缓存图片文件
	 */
	/*public void cacheImage() {
		if (imageList != null && imageList.size() > 0) {
			imageList.clear();
		}
		
		int totalPage = 0;
		if (tempList.size() % 10 == 0) {
			totalPage = tempList.size() / 10;
		} else {
			totalPage = tempList.size() / 10 + 1;
		}

		if (totalPage >= currentPage) {
			int temp = 0;
			if(totalPage > currentPage){
				temp = currentPage * 10;
			}
			if(totalPage == currentPage){
				temp = tempList.size();
			}
			
			for (int i = (currentPage - 1) * 10; i < temp; i++) {
				imageList.add(actsList.get(i).smallimg);
			}
		}
		
		service = new DownloadService(Contants.ALL_SMALL_IMAGE_PATH, imageList, new DownloadStateListener() {
			@Override
			public void onFinish() {
				// 图片下载成功后，实现您的代码
				Message message = new Message();
				message.what = 3;
				handler.sendMessage(message);
			}

			@Override
			public void onFailed() {
				// 图片下载成功后，实现您的代码
				Message message = new Message();
				message.what = 4;
				handler.sendMessage(message);
			}
		});

		service.startDownload();
	}*/
	


	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		UMImage image = new UMImage(CategoryActivity.this, R.mipmap.logo_108);
		final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[] { SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
				SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE };
		switch (v.getId()) {
		case R.id.back_img:
			finish();
			break;
		case R.id.share_img:

			break;
		case R.id.share_text:

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
		
		customWebView.loadUrl("javascript:clearSelectItem()");
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toShare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initWithUrl(String url) {
		swipeLayout.setRefreshing(false);
		customWebView.loadUrl("javascript:init('" + tags + "','" + column + "');");
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
		this.currentPage = currentPage;
		//cacheImage();
	}

	@Override
	public void search(String keyword) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRefresh() {
		customWebView.reload();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
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
