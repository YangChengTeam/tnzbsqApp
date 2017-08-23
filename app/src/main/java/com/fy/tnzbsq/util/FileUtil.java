/**
 * FileUtil.java
 * 2013-12-23
 */
package com.fy.tnzbsq.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.kymjs.kjframe.utils.KJLoger;

import com.fy.tnzbsq.common.Contants;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

/**
 * @author
 * 
 */
public class FileUtil {
	public static boolean isFileExist(String fileName) {
		if (new File(fileName).exists()) {
			return false;
		} else {
			return true;
		}
	}

	public static File createNewFile(String fileName, boolean isCover) {
		if (isFileExist(fileName) && !isCover) {
			return null;
		}
		return new File(fileName);
	}

	public static boolean deleteFile(String fileName) {
		File tmp = new File(fileName);
		if (tmp.exists() && tmp.isFile()) {
			return tmp.delete();
		}
		return true;
	}

	// 保存到手机内存中
	public static boolean saveObjectToFile(Context context, String fileName, Object obj) {
		File file = new File(context.getFilesDir() + "/" + fileName);
		ObjectOutputStream outputStream = null;
		try {
			outputStream = new ObjectOutputStream(new FileOutputStream(file));
			if (outputStream != null) {
				outputStream.writeObject(obj);
				outputStream.flush();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
					outputStream = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public static Object getObjFromFile(Context context, String fileName) {
		File file = new File(context.getFilesDir() + "/" + fileName);
		if (!file.exists()) {
			return null;
		}

		ObjectInputStream inputStream = null;
		Object obj = null;
		try {
			inputStream = new ObjectInputStream(new FileInputStream(file));
			if (inputStream != null) {
				obj = inputStream.readObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
					inputStream = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return obj;
	}

	public static String formatSize(long size) {
		double tmpSize = size;
		if (tmpSize / 1024 < 1) {
			return "" + (int) (tmpSize) + "B";
		} else if (tmpSize / 1024 / 1024 < 1) {
			return "" + (int) (tmpSize / 1024) + "KB";
		} else if (tmpSize / 1024 / 1024 / 1024 < 1) {
			return "" + leaveTwo(tmpSize, 1024 * 1024) + "MB";
		} else if (tmpSize / 1024 / 1024 / 1024 / 1024 < 1) {
			return "" + leaveTwo(tmpSize, 1024 * 1024 * 1024) + "GB";
		} else if (tmpSize / 1024 / 1024 / 1024 / 1024 / 1024 < 1) {
			return "" + leaveTwo(tmpSize, 1024 * 1024 * 1024 * 1024) + "TB";
		} else {
			return null;
		}
	}

	private static double leaveTwo(double num, long base) {
		int tmp = (int) (num / base * 100);
		return 1.0 * tmp / 100;
	}

	public static String[] getDirAndFileName(String path) {
		try {
			int index = path.lastIndexOf("/");
			if (index > 0) {
				String dir = path.substring(0, index);
				String fileName = path.substring(index + 1);
				return new String[] { dir, fileName };
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定文件大小
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static long getFileSize(File file) throws Exception {
		long size = 0;
		if (file.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			size = fis.available();
		} else {
			file.createNewFile();
			Log.e("获取文件大小", "文件不存在!");
		}
		return size;
	}

	public static void saveMyBitmap(String bitName, Bitmap mBitmap) {
		// File f = new File(Environment.getExternalStorageState() + "/TNZBSQ/"
		// + bitName + ".jpg");

		File fileDir = new File(Contants.ALL_DATA_DIR_PATH);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		File file = new File(Contants.ALL_DATA_DIR_PATH, bitName + ".jpg");
		// if (!file.exists()) {
		// file.createNewFile();
		// }

		try {
			file.createNewFile();
		} catch (IOException e) {
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(CompressFormat.JPEG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将Bitmap 图片保存到本地路径，并返回路径
	 * 
	 * @param c
	 * @param mType
	 *            资源类型，参照 MultimediaContentType 枚举，根据此类型，保存时可自动归类
	 * @param fileName
	 *            文件名称
	 * @param bitmap
	 *            图片
	 * @return
	 */
	public static String saveFile(Context c, String fileName, Bitmap bitmap) {
		return saveFile(c, "", fileName, bitmap);
	}

	public static String saveFile(Context c, String filePath, String fileName, Bitmap bitmap) {
		byte[] bytes = bitmapToBytes(bitmap);
		return saveFile(c, filePath, fileName, bytes);
	}

	public static byte[] bitmapToBytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}

	public static String saveFile(Context c, String filePath, String fileName, byte[] bytes) {
		String fileFullName = "";
		FileOutputStream fos = null;
		String dateFolder = new SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(new Date());
		try {
			String suffix = ".jpg";
			if (filePath == null || filePath.trim().length() == 0) {
				filePath = Environment.getExternalStorageState() + "/TNZBSQ/";
			}
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}
			File fullFile = new File(filePath, fileName + suffix);
			fileFullName = fullFile.getPath();
			fos = new FileOutputStream(new File(filePath, fileName + suffix));
			fos.write(bytes);
		} catch (Exception e) {
			fileFullName = "";
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					fileFullName = "";
				}
			}
		}
		return fileFullName;
	}

	public static String readFile(String fileName) {
		String result = "";
		try {
			File file = new File(fileName);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String readline = "";
			StringBuffer sb = new StringBuffer();
			while ((readline = br.readLine()) != null) {
				System.out.println("readline:" + readline);
				sb.append(readline);
			}
			br.close();
			result = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String readFile1(String fileName) {
		String result = "";
		try {
			File file = new File(fileName);
			FileInputStream is = new FileInputStream(file);
			byte[] b = new byte[1024];
			is.read(b);
			result = new String(b);
			KJLoger.debug(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
