package com.fy.tnzbsq.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import android.util.Log;

/**
 * 批量图片下载类 无需与界面交互的下载类
 *
 */
public class DownloadService {

	private static String TAG = "DownloadService";

	public static final int IO_BUFFER_SIZE = 8 * 1024;

	private static final String CACHE_FILENAME_PREFIX = "cache_";

	private static Object lock = new Object();

	private ExecutorService mImageThreadPool = null;

	// 下载状态监听，提供回调
	DownloadStateListener listener;

	// 下载目录
	private String downloadPath;

	// 下载链接集合
	private List<String> listURL;
	// 下载个数
	private int size = 0;

	// 下载完成回调接口
	public interface DownloadStateListener {

		public void onFinish();

		public void onFailed();

	}

	/**
	 * 获取线程池的方法，因为涉及到并发的问题，我们加上同步锁
	 * 
	 * @return
	 */
	public ExecutorService getThreadPool() {
		if (mImageThreadPool == null) {
			synchronized (ExecutorService.class) {
				if (mImageThreadPool == null) {
					// 为了下载图片更加的流畅，我们用了2个线程来下载图片
					mImageThreadPool = Executors.newFixedThreadPool(3);
				}
			}
		}
		return mImageThreadPool;
	}

	public DownloadService(String downloadPath, List<String> listURL, DownloadStateListener listener) {
		this.downloadPath = downloadPath;
		this.listURL = listURL;
		this.listener = listener;
	}

	/**
	 * 暂未提供设置
	 */
	public void setDefaultExecutor() {
	}

	/**
	 * 开始下载
	 */
	public void startDownload() {
		// 首先检测path是否存在
		File downloadDirectory = new File(downloadPath);
		if (!downloadDirectory.exists()) {
			downloadDirectory.mkdirs();
		}

		for (final String url : listURL) {
			// 捕获线程池拒绝执行异常
			try {
				// 线程放入线程池
				getThreadPool().execute(new Runnable() {
					@Override
					public void run() {
						downloadBitmap(url);
					}
				});

			} catch (RejectedExecutionException e) {
				e.printStackTrace();
				Log.e(TAG, "thread pool rejected error");
				listener.onFailed();
			} catch (Exception e) {
				e.printStackTrace();
				listener.onFailed();
			}
		}
	}

	/**
	 * 下载图片
	 *
	 * @param urlString
	 * @return
	 */
	private File downloadBitmap(String urlString) {
		String fileName = urlString;
		// 图片命名方式
		final File cacheFile = new File(createFilePath(new File(downloadPath), fileName));
		//判断文件是否存在
		if(!cacheFile.exists()){
			HttpURLConnection urlConnection = null;
			BufferedOutputStream out = null;

			try {
				final URL url = new URL(urlString);
				urlConnection = (HttpURLConnection) url.openConnection();
				final InputStream in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
				out = new BufferedOutputStream(new FileOutputStream(cacheFile), IO_BUFFER_SIZE);

				int b;
				while ((b = in.read()) != -1) {
					out.write(b);
				}
				// 每下载成功一个，统计一下图片个数
				statDownloadNum();
				return cacheFile;

			} catch (final IOException e) {
				// 有一个下载失败，则表示批量下载没有成功
				Log.e(TAG, "download " + urlString + " error");
				listener.onFailed();
			} finally {
				if (urlConnection != null) {
					urlConnection.disconnect();
				}
				if (out != null) {
					try {
						out.close();
					} catch (final IOException e) {
						Log.e(TAG, "Error in downloadBitmap - " + e);
					}
				}
			}
			return null;
		}else{
			//每下载成功一个，统计一下图片个数
			statDownloadNum();
			return cacheFile;
		}
	}

	/**
	 * Creates a constant cache file path given a target cache directory and an
	 * image key.
	 *
	 * @param cacheDir
	 * @param key
	 * @return
	 */
	public static String createFilePath(File cacheDir, String key) {
		try {
			String tempName = "";
			if (key != null && key.length() > 0) {
				tempName = key.substring(key.lastIndexOf("/") + 1, key.length());
			}
			return cacheDir.getAbsolutePath() + File.separator + CACHE_FILENAME_PREFIX + tempName;
		} catch (final Exception e) {
			Log.e(TAG, "createFilePath - " + e);
		}
		return null;
	}

	/**
	 * 统计下载个数
	 */
	private void statDownloadNum() {
		synchronized (lock) {
			size++;
			if (size == listURL.size()) {
				Log.d(TAG, "download finished total " + size);
				// 释放资源
				// DEFAULT_TASK_EXECUTOR.shutdownNow();
				// 如果下载成功的个数与列表中 url个数一致，说明下载成功
				listener.onFinish(); // 下载成功回调
			}
		}
	}

	/**
	 * 取消正在下载的任务
	 */
	public synchronized void cancelTask() {
		if (mImageThreadPool != null) {
			mImageThreadPool.shutdownNow();
			mImageThreadPool = null;
			size = 0;
		}
	}
}