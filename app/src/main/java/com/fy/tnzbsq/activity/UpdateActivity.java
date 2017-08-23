package com.fy.tnzbsq.activity;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.utils.KJLoger;

import com.fy.tnzbsq.R;
import com.fy.tnzbsq.service.UpdateService;
import com.fy.tnzbsq.service.UpdateService.DownLoadInfo;
import com.fy.tnzbsq.util.FileUtil;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UpdateActivity extends BaseActivity {
	private static final int MSG_UPDATE = 0x0;

	@BindView(id = R.id.cur_size)
	private TextView tvCurSize;
	@BindView(id = R.id.total_size)
	private TextView tvTotalSize;
	@BindView(id = R.id.update_pb)
	private ProgressBar progressBar;

	@BindView(id = R.id.cancel, click = true)
	private Button cancelBtn;
	@BindView(id = R.id.hide, click = true)
	private Button hideBtn;

	private boolean isStop;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == MSG_UPDATE) {
				DownLoadInfo downLoadInfo = (DownLoadInfo) msg.obj;
				if (downLoadInfo != null) {
					tvCurSize.setText(FileUtil.formatSize(downLoadInfo.downloadSize));
					tvTotalSize.setText(FileUtil.formatSize(downLoadInfo.fileSize));
					if (downLoadInfo.fileSize != 0) {
						progressBar.setProgress((int) (100.0 * downLoadInfo.downloadSize / downLoadInfo.fileSize));
					}
				}
			}
		}
	};

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
	public void setRootView() {
		setFinishOnTouchOutside(false);
		setContentView(R.layout.activity_update);
	}

	@Override
	public void initData() {
		super.initData();
		if (UpdateService.hasStop()) {
			this.finish();
			startActivity(new Intent(context, SplashActivity.class));
		}
	}

	@Override
	public void initWidget() {
		super.initWidget();
		tvCurSize.setText("");
		tvTotalSize.setText("");
		progressBar.setProgress(0);
	}

	@Override
	public void initDataFromThread() {
		super.initDataFromThread();
		KJLoger.debug("initDataFromThread->isStop=" + isStop);
		while (!isStop) {
			KJLoger.debug("initDataFromThread->hasStop=" + UpdateService.hasStop());
			if (UpdateService.hasStop()) {
				return;
			}

			Message msg = handler.obtainMessage();
			msg.what = MSG_UPDATE;
			msg.obj = UpdateService.getDownLoadInfo();
			handler.sendMessage(msg);

			try {
				Thread.sleep(80);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void threadDataInited() {
		super.threadDataInited();
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.cancel:
			isStop = true;
			stopService(new Intent(context, UpdateService.class));
			finish();
			break;
		case R.id.hide:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
