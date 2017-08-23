package com.fy.tnzbsq.activity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.utils.KJLoger;

import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.GameInfo;
import com.fy.tnzbsq.bean.Result;
import com.fy.tnzbsq.bean.UserRet;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.CustomWebViewDelegate;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.util.HeadImageUtils;
import com.fy.tnzbsq.util.NetUtil;
import com.fy.tnzbsq.view.CustomWebView;
import com.fy.tnzbsq.view.PicSelectDialog;
import com.fy.tnzbsq.view.PicSelectDialog.PicDialogItemOnClick;
import com.google.gson.reflect.TypeToken;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfoActivity extends BaseActivity implements CustomWebViewDelegate {

	/*
	 * @BindView(id = R.id.image_btn, click = true) public Button showImg;
	 * 
	 * @BindView(id = R.id.left_user_icon, click = true) private RoundImageView
	 * userImage;
	 */

	@BindView(id = R.id.back_img, click = true)
	private ImageView backImg;

	@BindView(id = R.id.top_title)
	private TextView titleNameTv;

	@BindView(id = R.id.webview)
	private CustomWebView customWebView;

	@Override
	public void setRootView() {
		setContentView(R.layout.user_info);
	}

	private String picUploadUrl = Server.URL_UPDATE_USER;

	private String urlpath;
	private String resultStr = "";

	@Override
	public void initData() {
		super.initData();
		customWebView.delegate = this;
		titleNameTv.setText(getResources().getString(R.string.my_text));
		customWebView.loadUrl("file:///android_asset/my.html");
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

	/**
	 * 获取用户信息
	 */
	public void getUserInfo() {

		KJHttp kjh = new KJHttp();
		kjh.cleanCache();
		HttpParams params = new HttpParams();
		params.put("mime", App.ANDROID_ID);
		kjh.post(Server.URL_GET_USER_MESSAGE, params, new HttpCallBack() {
			@Override
			public void onSuccess(Map<String, String> headers, byte[] bt) {
				super.onSuccess(headers, bt);
				// 获取cookie
				KJLoger.debug("===" + headers.get("Set-Cookie"));

				if (bt != null && bt.length > 0) {
					String result = new String(bt);
					UserRet userRet = Contants.gson.fromJson(result, new TypeToken<UserRet>() {
					}.getType());
					if (Result.checkResult(UserInfoActivity.this, userRet)) {
						/*if (userRet.data != null && userRet.data.name != null) {

							// tempUserName = userRet.data.name;
							//
							// userNameTv.setText(tempUserName);
							// KJBitmap kjb = new KJBitmap();
							// kjb.display(userImage,userRet.data.avatar);//设置用户头像

							customWebView.loadUrl(
									"javascript:setUserInfo('" + userRet.data.avatar + "','" + userRet.data.name + "','"
											+ userRet.data.qq + "','" + userRet.data.fill_name + "')");
						}*/
					}
				}

			}

			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
			}
		});
	}

	/**
	 * 选择图片
	 */
	private void PicShowDialog() {
		PicSelectDialog.showPhotoDialog(context, "选择图片",
				new int[] { R.mipmap.conversation_options_secretfile, R.mipmap.conversation_options_camera },
				new String[] { "相册", "拍照" }, new PicDialogItemOnClick() {
					@Override
					public void itemSelect(String itemSelect) {
						if (itemSelect.equals("相册")) {
							HeadImageUtils.getFromLocation(UserInfoActivity.this);

						}
						if (itemSelect.equals("拍照")) {
							HeadImageUtils.getFromCamara(UserInfoActivity.this);
						}

					}

				});
	}

	/**
	 * 结果处理
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case HeadImageUtils.FROM_CRAMA:
			if (HeadImageUtils.photoCamare != null) {
				HeadImageUtils.cutCorePhoto(UserInfoActivity.this, HeadImageUtils.photoCamare);
			}
			break;
		case HeadImageUtils.FROM_LOCAL:
			if (data != null && data.getData() != null) {
				HeadImageUtils.cutCorePhoto(UserInfoActivity.this, data.getData());
			}
			break;
		case HeadImageUtils.FROM_CUT:
			// 选择图像之后，修改用户的图像
			if (HeadImageUtils.cutPhoto != null) {
				if (HeadImageUtils.imgPath != null && HeadImageUtils.imgPath.length() > 0) {
					customWebView.loadUrl("javascript:setImg('" + HeadImageUtils.imgPath + "');");
					updateUserInfo("", "", HeadImageUtils.imgPath);
				}
			}
			break;
		}
	}

	/**
	 * 修改用户信息
	 * 
	 * @param userName
	 * @param qqNumber
	 * @param imgPatth
	 */
	public void updateUserInfo(String userName, String qqNumber, String imgPath) {
		KJHttp kjh = new KJHttp();
		kjh.cleanCache();
		HttpParams params = new HttpParams();

		params.put("mime", App.ANDROID_ID);

		if (userName != null && userName.length() > 0) {
			params.put("name", userName);
		}

		if (qqNumber != null && qqNumber.length() > 0) {
			params.put("qq", qqNumber);
		}

		if (imgPath != null && imgPath.length() > 0) {
			params.put("avatar", new File(imgPath));
		}

		params.putHeaders("Cookie", "cookie_tnzbsq");
		kjh.post(Server.URL_UPDATE_USER, params, new HttpCallBack() {
			@Override
			public void onSuccess(Map<String, String> headers, byte[] t) {
				super.onSuccess(t);
				if (t != null && t.length > 0) {
					// String result = new String(t);
					/*
					 * UserRet userRet = Contants.gson.fromJson(result, new
					 * TypeToken<UserRet>() { }.getType()); if
					 * (Result.checkResult(UserInfoActivity.this, userRet)) {
					 * Toast.makeText(UserInfoActivity.this, "修改成功",
					 * Toast.LENGTH_SHORT).show(); }
					 */
					Toast.makeText(UserInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(UserInfoActivity.this, "修改用户信息失败", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	Runnable uploadImageRunnable = new Runnable() {
		@Override
		public void run() {

			if (picUploadUrl == null) {
				Toast.makeText(UserInfoActivity.this, "还没有设置上传的路径！", Toast.LENGTH_SHORT).show();
				return;
			}

			Map<String, String> textParams = new HashMap<String, String>();
			Map<String, File> fileparams = new HashMap<String, File>();

			try {
				// 创建一个URL对象
				URL url = new URL(picUploadUrl);
				textParams = new HashMap<String, String>();
				fileparams = new HashMap<String, File>();
				textParams.put("mime", App.ANDROID_ID);

				if (urlpath != null && urlpath.length() > 0) {
					// 要上传的图片文件
					File file = new File(urlpath);
					// fileparams.put("image", file);//gy:hide
					fileparams.put("avatar", file);// gy:与接口文档输入参数保持一致
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
					// Toast.makeText(UserInfoActivity.this, "上传成功",
					// Toast.LENGTH_SHORT);
					Message message = new Message();
					message.what = 0;
					handler.sendMessage(message);

				} else {
					Message message = new Message();
					message.what = 1;
					handler.sendMessage(message);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public void updateUserName(String userName) {
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
		HeadImageUtils.getFromLocation(UserInfoActivity.this);
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
		getUserInfo();
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
		HeadImageUtils.getFromCamara(UserInfoActivity.this);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(UserInfoActivity.this, "投稿成功!", Toast.LENGTH_SHORT).show();
				customWebView.loadUrl("javascript:clear();");
				break;
			case 1:
				Toast.makeText(UserInfoActivity.this, "投稿失败,请重试!", Toast.LENGTH_SHORT).show();
				break;

			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void fightMenu() {
		// TODO Auto-generated method stub
		
	}
}
