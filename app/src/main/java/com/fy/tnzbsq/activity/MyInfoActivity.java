package com.fy.tnzbsq.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.GameInfo;
import com.fy.tnzbsq.bean.VersionUpdateServiceRet;
import com.fy.tnzbsq.common.AppCustomViews.onAlertDialogBtnClickListener;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.CustomWebViewDelegate;
import com.fy.tnzbsq.common.ServiceInterface;
import com.fy.tnzbsq.common.StatusCode;
import com.fy.tnzbsq.service.UpdateService;
import com.fy.tnzbsq.util.AlertUtil;
import com.fy.tnzbsq.util.CommUtils;
import com.fy.tnzbsq.view.CustomWebView;
import com.umeng.analytics.MobclickAgent;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.utils.KJLoger;

public class MyInfoActivity extends BaseActivity implements CustomWebViewDelegate {

	@BindView(id = R.id.back_img, click = true)
	private ImageView backImg;

	@BindView(id = R.id.top_title)
	private TextView titleNameTv;

	@BindView(id = R.id.webview)
	private CustomWebView customWebView;

	// @BindView(id = R.id.version_btn,click = true)
	// private Button versionBtn;

	private AsyncTask<?, ?, ?> checkVersionTask;

	@Override
	public void setRootView() {
		setContentView(R.layout.my_info);
	}

	@Override
	public void initWidget() {
		super.initWidget();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(MyInfoActivity.this, "缓存已清除", Toast.LENGTH_SHORT).show();
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	public void initData() {
		super.initData();

		titleNameTv.setText(getResources().getString(R.string.app_name));
		customWebView.delegate = this;

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				customWebView.loadUrl("file:///android_asset/my.html");
			}
		}, 200);
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

	private String url;
	private String newVersionName;
	private String versionRemark;

	private class VersionUpdateServiceTask extends AsyncTask<Void, Void, VersionUpdateServiceRet> {
		boolean isAutoUpdate = true;

		public VersionUpdateServiceTask(boolean isAutoUpdate) {
			this.isAutoUpdate = isAutoUpdate;
		}

		private onAlertDialogBtnClickListener clickListener = new onAlertDialogBtnClickListener() {

			@Override
			public void onOk() {
				customWidgets.hideAlertDialog();
				startActivity(new Intent(context, UpdateActivity.class));
				startService(new Intent(context, UpdateService.class).putExtra(Contants.COMMON_URL, url));
			}

			@Override
			public void onCancle() {
				customWidgets.hideAlertDialog();
			}
		};

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (!isAutoUpdate) {
				customWidgets.showProgressDialog("正在获取版本信息...");
			}
		}

		@Override
		protected void onPostExecute(VersionUpdateServiceRet result) {
			super.onPostExecute(result);
			customWidgets.hideProgressDialog();
			if (result != null) {
				if (result.errCode.equals(StatusCode.STATUS_CODE_SUCCESS)) {
					if (result.data.version == null) {
						if (!isAutoUpdate) {
							AlertUtil.show(context, "没有版本信息!");
						}
						return;
					}

					if (result.data.version.equals(CommUtils.getVersionName(context))) {
						if (!isAutoUpdate) {
							customWidgets.showInfoDialog("检查更新",
									"当前已是最新版本！\n版本号：V" + CommUtils.getVersionName(context));
						}
					} else {
						if(result.data.versionCode != null && Integer.parseInt(result.data.versionCode) > CommUtils.getVersionCode(context)){
							if (!CommUtils.checkSdCardExist()) {
								if (!isAutoUpdate) {
									AlertUtil.show(context, "sd卡无法使用或不存在！请插入sd卡。");
								}
							} else {
								url = result.data.download;
								newVersionName = result.data.version;
								versionRemark = result.data.updateLog;

								if (versionRemark != null) {
									versionRemark = versionRemark.replace(",", "\n");
								}

								showUpdateDialog();
							}
						}else{
							customWidgets.showInfoDialog("检查更新",
									"当前已是最新版本！\n版本号：V" + CommUtils.getVersionName(context));
						}
					}
				} else {
					// AlertUtil.show(context, result.body.remark);
				}
			} else {
				AlertUtil.show(context, getResources().getString(R.string.net_connect_error));
			}
		}

		private void showUpdateDialog() {
			customWidgets.showAlertDialog("检查更新", "当前版本号：V" + CommUtils.getVersionName(context) + "\n最新版本号：V"
					+ newVersionName + "\n版本信息：" + versionRemark + "\n是否下载并安装新版本？", clickListener);
		}

		@Override
		protected VersionUpdateServiceRet doInBackground(Void... params) {
			try {
				return ServiceInterface.VersionUpdateService(context,"");
			} catch (Exception e) {
			}
			return null;
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
		// checkVersionTask = new VersionUpdateServiceTask(false).execute();
		// Toast.makeText(context, "版本更新", Toast.LENGTH_SHORT);
		KJLoger.debug("updateVersion-------------");
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
