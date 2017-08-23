package com.fy.tnzbsq.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.fy.tnzbsq.common.Contants;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.util.Log;


public class ImageUtil {
    private static final String TAG = ImageUtil.class.getSimpleName();

    public static Bitmap zoomByWidth(Bitmap bitmap, int width) throws Exception {
        // 获取这个图片的宽和高
        float originalWidth = bitmap.getWidth();
        float originalHeight = bitmap.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scale = ((float) width) / originalWidth;
        // 缩放图片动作
        matrix.postScale(scale, scale);
        Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, (int) originalWidth,
                (int) originalHeight, matrix, true);
        return newBmp;
    }

    /**
     * 根据width对指定图片路径对图片进行缩放，若图片宽度比给出宽度小，不进行缩放
     *
     * @param path
     * @param width
     * @return
     * @throws Exception
     */
    public static Bitmap zoomByWidth(String path, int width) throws Exception {
        Log.d(TAG, "width=" + width);
        File file = new File(path);
        if (file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            //options.inJustDecodeBounds = false;
            Log.d(TAG, "outWidth=" + options.outWidth + "; outHeight" + options.outHeight);
            int be = (int) (Math.ceil(options.outWidth / (float) width));
            Log.d(TAG, "be=" + be);
            if (be <= 0)
                be = 1;
            options.inSampleSize = be;
            options.inJustDecodeBounds = false;
            Bitmap bmp = BitmapFactory.decodeFile(path, options);
            return bmp;
        }
        return null;
    }

    /**
     * 根据width对指定图片路径对图片进行缩放，若图片宽度比给出宽度小，不进行缩放
     *
     * @param path
     * @param width
     * @return
     * @throws Exception
     */
    public static Bitmap zoomByWidth(byte imgData[], int width) throws Exception {
        Log.d(TAG, "width=" + width);
        try {
            if (imgData != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(imgData, 0, imgData.length, options);
                options.inJustDecodeBounds = false;
                int be = (int) (Math.ceil(options.outWidth / (float) width));
                Log.d(TAG, "be=" + be);
                if (be <= 0)
                    be = 1;
                //为位图设置100K的缓存
                options.inTempStorage = new byte[100 * 1024];
                //设置位图颜色显示优化方式
                //ALPHA_8：每个像素占用1byte内存（8位）
                //ARGB_4444:每个像素占用2byte内存（16位）
                //ARGB_8888:每个像素占用4byte内存（32位）
                //RGB_565:每个像素占用2byte内存（16位）
                //Android默认的颜色模式为ARGB_8888，这个颜色模式色彩最细腻，显示质量最高。
                //但同样的，占用的内存也最大。也就意味着一个像素点占用4个字节的内存。我们来做一个简单的计算题：3200*2400*4 bytes
                //=30M。如此惊人的数字！哪怕生命周期超不过10s，Android也不会答应的。
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                //设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
                options.inPurgeable = true;
                //设置位图缩放比例
                options.inSampleSize = be;
                //设置解码位图的尺寸信息
                options.inInputShareable = true;
                options.inJustDecodeBounds = false;
                Bitmap bmp = BitmapFactory.decodeByteArray(imgData, 0, imgData.length, options);
                return bmp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 缩放图片
    public static Bitmap zoomImg(String img, int newWidth, int newHeight) {
        // 图片源
        Bitmap bm = BitmapFactory.decodeFile(img);
        if (null != bm) {
            return zoomImg(bm, newWidth, newHeight);
        }
        return null;
    }

    public static Bitmap zoomImg(Context context, String img, int newWidth,
                                 int newHeight) {
        // 图片源
        try {
            Bitmap bm = BitmapFactory.decodeStream(context.getAssets()
                    .open(img));
            if (null != bm) {
                return zoomImg(bm, newWidth, newHeight);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    // 缩放图片
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        return newbm;
    }

    /**
     * 将Bitmap转换成Drawable
     *
     * @param bitmap
     * @return
     */
    public static Drawable convertBitmap2Drawable(Bitmap bitmap) {
        BitmapDrawable bd = new BitmapDrawable(bitmap);
        // 因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。
        return bd;
    }

    /**
     * 获取缩略图
     *
     * @param 'drawable'
     * @return
     */
    public static Bitmap getThumbnailBmp(Bitmap originalBmp) {
        return ThumbnailUtils.extractThumbnail(originalBmp,
                originalBmp.getWidth() / 4, originalBmp.getHeight() / 4);
    }

    /**
     * 将图片压缩至指定大小以内
     *
     * @param image
     * @param size  单位为kb
     * @return
     */
    public static Bitmap compressImage(Bitmap image, int size) {
        //图片允许最大空间   单位：KB
        double maxSize = size;
        //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        //将字节换成KB
        double mid = b.length / 1024;
        //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            //获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            image = zoomImageMakeImage(image, image.getWidth() / Math.sqrt(i),
                    image.getHeight() / Math.sqrt(i));
        }
        return image;
    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage   ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap zoomImageMakeImage(Bitmap bgimage, double newWidth,
                                            double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }


    private static Bitmap comp(Bitmap image, float toHeight, float toWidth, int size) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = toHeight;//这里设置高度为800f
        float ww = toWidth;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w >= h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w <= h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap, size);//压缩好比例大小后再进行质量压缩
    }

    private static Bitmap getimage(String srcPath, float toHeight, float toWidth) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
        float hh = toHeight;//这里设置高度为800f  
        float ww = toWidth;//这里设置宽度为480f  
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int be = 1;//be=1表示不缩放  
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例  
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        Log.v("wdktest", "wdk-touxiang-SELECT_LOCxxxxxxxxxxxxxxxxxxx");
        return compressImage(bitmap, 100);//压缩好比例大小后再进行质量压缩
    }

    /**
     * 将图片源文件压缩后重新保存到指定路径
     *
     * @param originalPath
     * @param toPath
     * @param size
     */
    public static String saveCompressBmp(String originalPath, int size) {
        try {
            Log.d(TAG, "test originalPath=" + originalPath);
            long size1 = FileUtil.getFileSize(new File(originalPath));
            Log.d(TAG, originalPath + " size1=" + FileUtil.formatSize(size1));
            String toPath = getCompressedPath(getDirAndFileName(originalPath)[1]);
            Log.d(TAG, "test toPath=" + toPath);
            Bitmap sizeBmp = BitmapFactory.decodeFile(originalPath);
            Bitmap comBmp = comp(sizeBmp, 800, 480, size);
            File toFile = new File(toPath);
            toFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(toFile);
            comBmp.compress(Bitmap.CompressFormat.PNG, 60, fOut);
            fOut.flush();
            fOut.close();
            return toPath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String compressFileToFile(String originalPath) {
        String toPath = getCompressedPath(getDirAndFileName(originalPath)[1]);
        File toFile = new File(toPath);
        try {
            toFile.createNewFile();
            compressBmpToFile1(originalPath, BitmapFactory.decodeFile(originalPath), toFile);
            return toPath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void compressBmpToFile1(String originalPath, Bitmap bmp, File file) {
        Bitmap bm = getimage(originalPath, 800f, 480f);
        int digree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(originalPath);
        } catch (IOException e) {
            e.printStackTrace();
            exif = null;
        }
        if (exif != null) {
            // 读取图片中相机方向信息  
            int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            // 计算旋转角度  
            switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    digree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    digree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    digree = 270;
                    break;
                default:
                    digree = 0;
                    break;
            }
        }
        if (digree != 0) {
            // 旋转图片  
            Matrix m = new Matrix();
            m.postRotate(digree);
            bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), m, true);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void compressBmpToFile(String originalPath, Bitmap bmp, File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream isBm = null;
        int digree = getRatateDigree(originalPath);

        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;//个人喜欢从80开始,
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩50%，把压缩后的数据存放到baos中
            options = 50;
            Log.v("test", "test cursize size is " + baos.toByteArray().length / 1024);
        }
        while (baos.toByteArray().length / 1024 > 100 && options > 10) {
            baos.reset();
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;
            Log.d(TAG, "test options=" + options);
            Log.v("test", "test cursize size is " + baos.toByteArray().length / 1024);
        }
        if (digree != 0) {
            isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
            Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
            // 旋转图片  
            Matrix m = new Matrix();
            m.postRotate(digree);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null != baos) {
            try {
                baos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (null != isBm) {
            try {
                isBm.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private static int getRatateDigree(String imgpath) {
        int digree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imgpath);
        } catch (IOException e) {
            e.printStackTrace();
            exif = null;
        }
        if (exif != null) {
            // 读取图片中相机方向信息  
            int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            // 计算旋转角度  
            switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    digree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    digree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    digree = 270;
                    break;
                default:
                    digree = 0;
                    break;
            }
        }
        return digree;
    }

    /**
     * 图片保存文件
     *
     * @param name
     * @return
     */
    public static String getCompressedPath(String name) {
        return Contants.BASE_IMAGE_DIR + name;
    }

    /**
     * 根据文件路径得到文件目录和文件名
     *
     * @param url
     * @return String[0]-目录，String[1]-文件名
     */
    private static String[] getDirAndFileName(String path) {
        try {
            int index = path.lastIndexOf("/");
            if (index > 0) {
                String dir = path.substring(0, index);
                String fileName = path.substring(index + 1);
                return new String[]
                        {dir, fileName};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 处理图片
     *
     * @param bm           所要转换的bitmap
     * @param newWidth新的宽
     * @param newHeight新的高
     * @return 指定宽高的bitmap
     */
    public static Bitmap ZoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    /**
     * 根据指定的宽高，透明度，创建一张图片
     *
     * @param width
     * @param height
     * @param number (范围0-100,0为全透明)
     * @return
     */
    public static Bitmap transparentBitmap(int width, int height, int number) {
        int[] argb = new int[width * height];
        number = number * 255 / 100;
        for (int i = 0; i < argb.length; i++) {
            argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);
        }
        return Bitmap.createBitmap(argb, width, height, Bitmap.Config.ARGB_8888);
    }
}
