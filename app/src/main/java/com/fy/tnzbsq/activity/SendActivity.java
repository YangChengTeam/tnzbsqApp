package com.fy.tnzbsq.activity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.kymjs.kjframe.ui.BindView;

import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.GameInfo;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.CustomWebViewDelegate;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.util.NetUtil;
import com.fy.tnzbsq.view.CustomProgress;
import com.fy.tnzbsq.view.CustomWebView;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SendActivity extends BaseActivity implements CustomWebViewDelegate {

	private static final int REQUESTCODE_PICK = 0;// 相册选图标记
	private static final int REQUESTCODE_TAKE = 1;// 相机拍照标记
	private static final int REQUESTCODE_CUTTING = 2;// 图片裁切标记
	
	private CustomProgress dialog;
	
	private String picUploadUrl = Server.URL_TOU_GAO;
	
	private String qqStr;

	private String description;
	
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

	private String url = "";

	private String urlpath;
	
	private String resultStr = "";
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(SendActivity.this, "投稿成功!", Toast.LENGTH_SHORT).show();
				customWebView.loadUrl("javascript:clear();");
				break;
			case 1:
				Toast.makeText(SendActivity.this, "投稿失败,请重试!", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(SendActivity.this, "缓存已清除", Toast.LENGTH_SHORT).show();
				// 最后通知更新
				SendActivity.this.sendBroadcast(
						new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Contants.BASE_SD_DIR)));
				break;
			case 3:
				//if (service != null) {
					//此条不要 service.cancelTask();
				//}
				
				//customWebView.loadUrl("javascript:refresh();");
				// Toast.makeText(SendActivity.this, "下载完成",
				// Toast.LENGTH_SHORT).show();
				break;
			case 4:
				// Toast.makeText(SendActivity.this, "下载失败",
				// Toast.LENGTH_SHORT).show();
				break;
			case 5:
				Toast.makeText(SendActivity.this, "下载地址有误，请稍后重试", Toast.LENGTH_SHORT).show();
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	@Override
	public void setRootView() {
		setContentView(R.layout.send);
	}

	@Override
	public void initWidget() {
		super.initWidget();
	}

	@Override
	public void initData() {
		super.initData();

		titleNameTv.setText(getResources().getString(R.string.send_text));

		customWebView.delegate = this;
		customWebView.loadUrl("file:///android_asset/tg.html");

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
		Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
		// 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
		pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(pickIntent, REQUESTCODE_PICK);
	}

	@Override
	public void submitMesage(String str, String descriptionStr) {
		qqStr = str;
		description = descriptionStr;

		if(dialog == null){
			dialog = CustomProgress.create(SendActivity.this, "正在上传...", true, null);
		}
		dialog.setCanceledOnTouchOutside(false);
		
		if (isValidContext(SendActivity.this) && dialog != null) {
			dialog.show();
		}
		// 新线程后台上传服务端
		new Thread(uploadImageRunnable).start();
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case REQUESTCODE_PICK:// 直接从相册获取
			try {
				/*
				 * Bundle extras = data.getExtras(); if (extras != null) { //
				 * 取得SDCard图片路径做显示 Bitmap photo = extras.getParcelable("data");
				 * Drawable drawable = new BitmapDrawable(null, photo); urlpath
				 * = FileUtil.saveFile(SendActivity.this, "temp.jpg", photo);
				 * 
				 * }
				 */

				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null,
						null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				urlpath = picturePath;
				/*
				 * ImageView imageView = (ImageView) findViewById(R.id.imgView);
				 * imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath
				 * ));
				 */

				customWebView.loadUrl("javascript:setImg('" + urlpath + "');");

			} catch (NullPointerException e) {
				e.printStackTrace();// 用户点击取消操作
			}
			break;
		case REQUESTCODE_TAKE:// 调用相机拍照

			break;
		case REQUESTCODE_CUTTING:// 取得裁剪后的图片
			if (data != null) {
				// setPicToView(data);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	Runnable uploadImageRunnable = new Runnable() {
		@Override
		public void run() {

			if (picUploadUrl == null) {
				Toast.makeText(SendActivity.this, "还没有设置上传的路径！", Toast.LENGTH_SHORT).show();
				return;
			}

			Map<String, String> textParams = new HashMap<String, String>();
			Map<String, File> fileparams = new HashMap<String, File>();

			try {
				// 创建一个URL对象
				URL url = new URL(picUploadUrl);
				textParams = new HashMap<String, String>();
				fileparams = new HashMap<String, File>();
				textParams.put("connect", qqStr);
				textParams.put("description", description);
				textParams.put("mime", App.ANDROID_ID);

				if (urlpath != null && urlpath.length() > 0) {
					// 要上传的图片文件
					File file = new File(urlpath);
					// fileparams.put("image", file);//gy:hide
					fileparams.put("img", file);// gy:与接口文档输入参数保持一致
				}

				// 利用HttpURLConnection对象从网络中获取网页数据
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				// 设置连接超时（记得设置连接超时,如果网络不好,Android系统在超过默认时间会收回资源中断操作）
				conn.setConnectTimeout(5000);
				// 设置允许输出（发送POST请求必须设置允许输出）
				conn.setDoOutput(true);
				// 设置使用POST的方式发送
				conn.setRequestMethod("POST");
				// 设置不使用缓存（容易出现问题）
				conn.setUseCaches(false);
				conn.setRequestProperty("Charset", "UTF-8");// 设置编码
				// 在开始用HttpURLConnection对象的setRequestProperty()设置,就是生成HTML文件头
				conn.setRequestProperty("ser-Agent", "Fiddler");
				// 设置contentType
				conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + NetUtil.BOUNDARY);
				OutputStream os = conn.getOutputStream();
				DataOutputStream ds = new DataOutputStream(os);
				NetUtil.writeStringParams(textParams, ds);
				NetUtil.writeFileParams(fileparams, ds);
				NetUtil.paramsEnd(ds);
				// 对文件流操作完,要记得及时关闭
				os.close();
				// 服务器返回的响应吗
				int code = conn.getResponseCode(); // 从Internet获取网页,发送请求,将网页以流的形式读回来
				// 对响应码进行判断
				if (code == 200) {// 返回的响应码200,是成功
					// 得到网络返回的输入流
					InputStream is = conn.getInputStream();
					resultStr = NetUtil.readString(is);
					// Toast.makeText(SendActivity.this, "上传成功",
					// Toast.LENGTH_SHORT);
					Message message = new Message();
					message.what = 0;
					handler.sendMessage(message);

				} else {
					Message message = new Message();
					message.what = 1;
					handler.sendMessage(message);
				}
				if (isValidContext(SendActivity.this) && dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	@SuppressLint("NewApi")
	private boolean isValidContext(Context ctx) {
		Activity activity = (Activity) ctx;
		
		if(Build.VERSION.SDK_INT > 17){
			if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
				return false;
			} else {
				return true;
			}
		}else{
			if (activity == null || activity.isFinishing()) {
				return false;
			} else {
				return true;
			}
		}
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
