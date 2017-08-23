package com.fy.tnzbsq.util.download;

public interface DownloadStrage {
	public boolean downloadFile(String url, String fileName);
	public long getFileSize();
	public long getDownloadSize();
	public void stop();
}
