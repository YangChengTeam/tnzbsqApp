package com.fy.tnzbsq.util.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author keqizwl
 * 
 */

public class DownloadFile {
	private DownloadStrage strage;
	private String url;
	private String fileName;

	/*
	 * default
	 */
	public DownloadFile(String url, String fileName) {
		this.strage = new HttpDownload();
		this.url = url;
		this.fileName = fileName;
	}

	public DownloadFile(String url, String fileName, DownloadStrage strage) {
		this.strage = strage;
		this.url = url;
		this.fileName = fileName;
	}
	
	public boolean downloadFile(){
		return strage.downloadFile(url, fileName);
	}
	
	public long getDownloadSize(){
	    return strage.getDownloadSize();
	}
	
	public long getFileSize(){
	    return strage.getFileSize();
	}
	
	public void stop(){
	    strage.stop();
	}
}
