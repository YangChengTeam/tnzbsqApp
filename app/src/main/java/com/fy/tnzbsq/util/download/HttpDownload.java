package com.fy.tnzbsq.util.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.kymjs.kjframe.utils.KJLoger;

import com.fy.tnzbsq.util.FileUtil;


public  class HttpDownload implements DownloadStrage{
	private long wholeLength = 1;
	private long downloadLength;
	private boolean isStop;
	private HttpURLConnection urlConn;

	public float getDownloadProgress() {
		return downloadLength / wholeLength;
	}

	public boolean downloadFile(String url, String fileName) {
		if (fileName == null) {
			return false;
		}
		File directory = new File(FileUtil.getDirAndFileName(fileName)[0]);
		directory.mkdirs();  
		if (!directory.exists()) {  
            return false;  
        } 
		File file = new File(fileName);
		try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}  
        if (!file.isFile()) {  
            return false;  
        }  
		InputStream input = null;
		FileOutputStream outputStream = null;

		try {
			input = getInputStream(url);
			outputStream = new FileOutputStream(file);

			byte[] buffer = new byte[4 * 1024];
			int length = -1;
			while ((length = input.read(buffer)) != -1) {
			    if(isStop){
				return false;
			    }
				outputStream.write(buffer, 0, length);
				downloadLength += length;
			}
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
			    	if(urlConn != null){
			    	    urlConn.disconnect();
			    	}
				if (input != null) {
					input.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				return false;
			}
		}
		return true;
	}

	private InputStream getInputStream(String urlStr) {
		InputStream is = null;
		try {
			URL url = new URL(urlStr);
			urlConn = (HttpURLConnection) url
					.openConnection();
			urlConn.connect();
			wholeLength = urlConn.getContentLength();
			KJLoger.debug("getInputStream->wholeLength=" + wholeLength);
			is = urlConn.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}

	@Override
	public long getFileSize() {
	    
	    return wholeLength;
	}

	@Override
	public long getDownloadSize() {
	    return downloadLength;
	}

	@Override
	public void stop() {
	    isStop = true;
	}
}
