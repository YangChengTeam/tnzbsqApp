package com.fy.tnzbsq.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

/**
 * 类说明：读取缓存图片和保存图�?
 * 
 * @创建时间 2012-1-10 下午04:44:36
 */
public class ImageCacheUtils
{
	private static final String TAG = "ImageCacheUtils";
//	public static final String PHOTOGRAPHY_CACHE_ROOT_PATH = Environment
//			.getExternalStorageDirectory().getAbsolutePath()
//			+ "/iptvLauncher/cache/";
	public static String PHOTOGRAPHY_CACHE_ROOT_PATH = "/data/data/com.ihome.kitchen/files/cache/";

	private static final int JPG_FILE_FORMAT = 1;

	private static final int PNG_FILE_FORMAT = 2;

	public static String picUrl;

	/**
	 * 根据url从缓存加载图�?
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getBitmapFormCache(String url, int width)
	{
		try
		{
			if (!TextUtils.isEmpty(url))
			{
				if (url.indexOf("jietu") > 0)
					return null;
//				String tempUrl = url.replace(".jpg", ".temp");
				String tempUrl = url;
				StringBuffer buf = new StringBuffer(tempUrl);
				if (tempUrl.startsWith("http://"))
				{
					buf.delete(0, "http://".length());
				}
				Log.d("abc", PHOTOGRAPHY_CACHE_ROOT_PATH + buf.toString());
				if (width > 0) {
					return ImageUtil.zoomByWidth(PHOTOGRAPHY_CACHE_ROOT_PATH + buf.toString(), width);
				} else {
					return decodeFile(PHOTOGRAPHY_CACHE_ROOT_PATH + buf.toString());
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据图片网络URL获取本地存储地址
	 * @param url
	 * @return
	 */
	public static String getLocalPath(String url) {
		try {
			if (!TextUtils.isEmpty(url)) {
				String tempUrl = url;
				StringBuffer buf = new StringBuffer(tempUrl);
				if (tempUrl.startsWith("http://")) {
					buf.delete(0, "http://".length());
				}
				return PHOTOGRAPHY_CACHE_ROOT_PATH + buf.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将制定url和图片添加到缓存
	 * 
	 * @param url
	 * @return
	 */
	public static void saveBitmapToCache(String url, Bitmap bitmap)
	{
		try
		{
			if (!TextUtils.isEmpty(url))
			{
				if (url.indexOf("jietu") > 0)
					return;
//				String tempUrl = url.replace(".jpg", ".temp");
				String tempUrl = url;
				String[] info = getDirAndFileName(tempUrl);
				if (info != null)
				{
					saveBitmap(bitmap, info[0], info[1]);
				}
			}
		}
		catch (Exception e)
		{
			// e.printStackTrace();
		}
	}

	/**
	 * 根据指定的文件路径加载图�?
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	private static Bitmap decodeFile(String path) throws Exception
	{
		File file = new File(path);
		if (file.exists())
		{
			 BitmapFactory.Options o2 = new BitmapFactory.Options();
			 o2.inSampleSize = 3;
			 o2.inJustDecodeBounds = false;

			 BitmapFactory.Options options = new BitmapFactory.Options();
			 options.inJustDecodeBounds = true;
			 Bitmap bitmap = BitmapFactory.decodeFile(path, options);
			 options.inJustDecodeBounds = false;
			 int be = (int)(options.outHeight / (float)88);
			 if (be <= 0)
			 be = 1;
			 options.inSampleSize = be;
//			Bitmap bitmap = BitmapFactory.decodeFile(path);
			return bitmap;
		}
		return null;
	}

	/**
	 * 得到文件类型
	 * 
	 * @param filename
	 * @return
	 */
	private static int getFileFormat(String filename)
	{
		if (filename.toUpperCase().endsWith(".PNG"))
		{
			return PNG_FILE_FORMAT;
		}
		return JPG_FILE_FORMAT;
	}

	/**
	 * 保存图片到指定位置
	 * 
	 * @param bitmap
	 * @param path
	 * @param filename
	 */
	private static void saveBitmap(Bitmap bitmap, String path, String filename)
	{
		FileOutputStream fOut = null;
		try
		{
			File dir = new File(path);
			if (!dir.exists())
			{
				dir.mkdirs();
			}
			File f = new File(path, filename);
			f.createNewFile();
			setPicUrl(f.getAbsolutePath());
			fOut = new FileOutputStream(f);
			int format = getFileFormat(filename);
			if (format == JPG_FILE_FORMAT)
			{
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			}
			else if (format == PNG_FILE_FORMAT)
			{
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			}
			fOut.flush();
		}
		catch (IOException e)
		{
			// e.printStackTrace();
		}
		finally
		{
			try
			{
				if (fOut != null)
				{
					fOut.close();
					fOut = null;
				}
			}
			catch (Exception e)
			{
			}
		}
	}

	/**
	 * 根据文件路径得到文件目录和文件名
	 * 
	 * @param url
	 * @return String[0]-目录，String[1]-文件�?
	 */
	private static String[] getDirAndFileName(String url)
	{
		try
		{
			StringBuffer buf = new StringBuffer(url);
			if (url.startsWith("http://"))
			{
				buf.delete(0, "http://".length());
			}
			String path = PHOTOGRAPHY_CACHE_ROOT_PATH + buf.toString();
			int index = path.lastIndexOf("/");
			if (index > 0)
			{
				String dir = path.substring(0, index);
				String fileName = path.substring(index + 1);
				return new String[]
					{ dir, fileName };
			}
		}
		catch (Exception e)
		{
			// e.printStackTrace();
		}
		return null;
	}

	public static String getPicUrl()
	{
		return picUrl;
	}

	public static void setPicUrl(String picUrl)
	{
		ImageCacheUtils.picUrl = picUrl;
	}
	
	/**
	 * 清除缓存的图片
	 */
	public static void deleteCacheBitmap() {
		delete(new File(PHOTOGRAPHY_CACHE_ROOT_PATH));
	}
	
	/**
	 * 删除指定的文件或者目录
	 * @param file 文件或者目录路径
	 */
	public static void delete(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}

		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i]);
			}
			file.delete();
		}
	}

}
