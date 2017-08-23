package com.fy.tnzbsq.activity;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.utils.KJLoger;

import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.ImageCreateRet;
import com.fy.tnzbsq.bean.Result;
import com.fy.tnzbsq.bean.UserRet;
import com.fy.tnzbsq.common.AndroidToastForJs;
import com.fy.tnzbsq.common.ServiceInterface;
import com.fy.tnzbsq.util.FileUtils;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class Main1Activity extends BaseActivity {

	@BindView(id = R.id.my_web_view)
	private WebView webView;

	@BindView(id = R.id.js_btn, click = true)
	private Button jsBtn;
	
	@BindView(id = R.id.login_btn, click = true)
	private Button loginBtn;
	
	@BindView(id = R.id.create_btn, click = true)
	private Button createBtn;
	
	protected ProgressDialog dialog;

	private Handler handler;

	private AsyncTask<?, ?, ?> task;
	
	private AsyncTask<?, ?, ?> taskCreate;
	
	private ProgressDialog progressDialog;
	
	@Override
	public void setRootView() {
		setContentView(R.layout.main1);
	}

	@SuppressLint("SetJavaScriptEnabled")
	public void setWebViewConfigure() {
		WebSettings setting = webView.getSettings();
		setting.setJavaScriptEnabled(true);
		setting.setDefaultTextEncodingName("UTF-8");
		webView.addJavascriptInterface(new AndroidToastForJs(), "JavaScriptInterface");
	}

	@Override
	public void initData() {
		super.initData();
		
		FileUtils.write(context, "test1.json", "测试1111");
		String test = FileUtils.read(context, "test1.json");
		KJLoger.debug(test);
	}

	@Override
	public void initWidget() {
		super.initWidget();

		handler = new Handler() {
			public void handleMessage(Message msg) {// 定义一个Handler，用于处理下载线程与UI间通讯
				if (!Thread.currentThread().isInterrupted()) {
					switch (msg.what) {
					case 0:
						dialog.show();// 显示进度对话框
						break;
					case 1:
						dialog.hide();// 隐藏进度对话框，不可使用dismiss()、cancel(),否则再次调用show()时，显示的对话框小圆圈不会动。
						break;
					}
				}
				super.handleMessage(msg);
			}
		};

		webView.loadUrl("file:///android_asset/jsonData.html");
		setWebViewConfigure();

		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					handler.sendEmptyMessage(1);// 如果全部载入,隐藏进度对话框
				}
				super.onProgressChanged(view, newProgress);
			}
		});

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				if (dialog == null) {
					dialog = ProgressDialog.show(context, "提示", "正在加载...", true);
					dialog.setCancelable(true);
					dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
						@Override
						public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
							if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {
								dialog.dismiss();
							}
							return true;
						}
					});
				}
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				view.loadUrl("file:///android_asset/no-wifi.html");
			}

		});
	}
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.js_btn:
			webView.loadUrl("javascript:funFromjs()");
			break;
		case R.id.login_btn:
			progressDialog = ProgressDialog.show(Main1Activity.this, "用户登录",
					"正在登录，请稍后...", true, false);
			task = new LoginTask("admin", "admin").execute();
			break;
		case R.id.create_btn:
			progressDialog = ProgressDialog.show(Main1Activity.this, "用户登录",
					"正在生成，请稍后...", true, false);
			task = new CreateTask("123123", "214234234","{\"a\":\"张三\",\"b\":\"李四\"}").execute();
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
	
	private class LoginTask extends AsyncTask<Void, Void, UserRet> {
		private String phoneNumer;
		private String password;

		public LoginTask(String phoneNumer, String password) {
			this.phoneNumer = phoneNumer;
			this.password = password;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.show();
		}

		@Override
		protected UserRet doInBackground(Void... params) {
			return ServiceInterface.login(Main1Activity.this, phoneNumer,
					password);
		}

		@Override
		protected void onPostExecute(UserRet result) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			try {
				super.onPostExecute(result);

				if (Result.checkResult(Main1Activity.this, result)) {
					Toast.makeText(Main1Activity.this, "登录成功",
							Toast.LENGTH_SHORT).show();
					
					//将当前用户存储
					if(result.data != null){
						Toast.makeText(Main1Activity.this, "数据"+result.data,
								Toast.LENGTH_SHORT).show();
					}
					
				} else {
					Toast.makeText(Main1Activity.this, "登录失败,请稍后重试!",
							Toast.LENGTH_SHORT).show();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private class CreateTask extends AsyncTask<Void, Void, ImageCreateRet> {
		private String id;
		private String mime;
		private String requestData;
		public CreateTask(String id, String mime,String requestData) {
			this.id = id;
			this.mime = mime;
			this.requestData = requestData;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.show();
		}

		@Override
		protected ImageCreateRet doInBackground(Void... params) {
			return ServiceInterface.ImageCreateService(context, id, mime, requestData);
		}

		@Override
		protected void onPostExecute(ImageCreateRet result) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			try {
				super.onPostExecute(result);

				if (Result.checkResult(Main1Activity.this, result)) {
					Toast.makeText(Main1Activity.this, "生成成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(Main1Activity.this, "生成失败,请稍后重试!",
							Toast.LENGTH_SHORT).show();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
