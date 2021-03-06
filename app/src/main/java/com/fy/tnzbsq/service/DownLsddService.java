package com.fy.tnzbsq.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.fy.tnzbsq.R;
import com.fy.tnzbsq.activity.UpdateActivity;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.util.FileUtils;
import com.fy.tnzbsq.util.download.DownloadFile;

import java.io.File;

public class DownLsddService extends Service {
	private final int MSG_UPDATE_PROGRESS = 0x0;
	protected int MSG_STOP_SEVICE = 0x2;

	private static boolean hasStop = true;

	private NotificationManager nm;
	private RemoteViews rv;
	private Notification notification;

	private DownLoadThread downloadThread;

	private static int progress = 0;

	private static DownLoadInfo downLoadInfo = new DownLoadInfo(0, 0);

	public static class DownLoadInfo {
		public long fileSize;
		public long downloadSize;

		public DownLoadInfo(long fileSize, long downloadSize) {
			this.downloadSize = downloadSize;
			this.fileSize = fileSize;
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == MSG_UPDATE_PROGRESS && !hasStop) {
				rv.setProgressBar(R.id.pb, 100, progress, false);
				rv.setTextViewText(R.id.pb_text, "正在下载，已完成 " + progress + "%");
				nm.notify(Contants.NOTIFICATION_LSDD_ID, notification);
			} else if (msg.what == MSG_STOP_SEVICE) {
				stopSelf();
			}
		}
	};

	@Override
	public void onCreate() {
		hasStop = false;
		super.onCreate();
	}

	private void createNotification() {
		nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		notification = new Notification(R.drawable.logo, "开始下载铃声朵朵", System.currentTimeMillis());
		notification.flags = Notification.FLAG_ONGOING_EVENT;

		Intent toUpdateActivity = new Intent(this, UpdateActivity.class);
		toUpdateActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, toUpdateActivity,
				PendingIntent.FLAG_UPDATE_CURRENT);

		rv = new RemoteViews(this.getPackageName(), R.layout.lsdd_notification_layout);
		notification.contentView = rv;
		notification.contentIntent = contentIntent;
		nm.notify(Contants.NOTIFICATION_LSDD_ID, notification);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		progress = 0;

		String downUrl = "";
		if(intent.getStringExtra("downUrl") != null && intent.getStringExtra("downUrl").length() >0){
			createNotification();
			downUrl = intent.getStringExtra("downUrl");
			// 开始下载
			downloadThread = new DownLoadThread(downUrl);
			downloadThread.start();
		}else{
			handler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(DownLsddService.this, "下载地址错误，请稍后重试",Toast.LENGTH_SHORT).show();
				}
			});
		}
		return 1;
	}

	public float getUpdateProgress() {
		return progress;
	}

	@Override
	public void onDestroy() {
		hasStop = true;
		if (downloadThread != null) {
			downloadThread.stopDownload();
		}

		nm.cancel(Contants.NOTIFICATION_LSDD_ID);
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private class DownLoadThread extends Thread {
		private DownloadFile downloadFile;
		private String url;
		private boolean isStop;

		public DownLoadThread(String url) {
			this.url = url;
		}

		public void stopDownload() {
			isStop = true;
			downloadFile.stop();
		}

		@Override
		public void run() {
			if (url == null) {
				handler.sendEmptyMessage(MSG_STOP_SEVICE);
				return;
			}

			downloadFile = new DownloadFile(url, Contants.LSDD_SOURCE_FILE_PATH);

			File file = new File(Contants.LSDD_SOURCE_FILE_PATH);
			if (file.exists()) {
				file.delete();
			}

			new Thread(new Runnable() {

				@Override
				public void run() {
					while (!isStop) {
						Message msg = Message.obtain(handler);
						msg.what = MSG_UPDATE_PROGRESS;
						progress = getDownloadProgress();
						handler.sendMessage(msg);
						downLoadInfo.downloadSize = getDownLoadSize();
						downLoadInfo.fileSize = getFileSize();
						try {
							sleep(800);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
				}
			}).start();

			if (downloadFile.downloadFile()) {

				boolean result = FileUtils.renameFile(Contants.LSDD_SOURCE_FILE_PATH,Contants.LSDD_DOWN_FILE_PATH);
				if(result){
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setDataAndType(Uri.fromFile(new File(Contants.LSDD_DOWN_FILE_PATH)),
							"application/vnd.android.package-archive");
					startActivity(intent);
				}

			}

			handler.sendEmptyMessage(MSG_STOP_SEVICE);
		}

		public int getDownloadProgress() {
			if (downloadFile != null) {
				return (int) (1.0 * downloadFile.getDownloadSize() / downloadFile.getFileSize() * 100);
			}
			return 0;
		}

		public long getFileSize() {
			return downloadFile == null ? 0 : downloadFile.getFileSize();
		}

		public long getDownLoadSize() {
			return downloadFile == null ? 0 : downloadFile.getDownloadSize();
		}
	}

	public static boolean hasStop() {
		return hasStop;
	}

	public static DownLoadInfo getDownLoadInfo() {
		return downLoadInfo;
	}
}
