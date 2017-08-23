package com.fy.tnzbsq.util;

import com.fy.tnzbsq.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.List;

/*
 * 和具体业务处理有关的api
 */
public class CommUtils {

    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void setViewsOnclick(Activity activity, int[] resIds, OnClickListener onClickListener) {
        for (int id : resIds) {
            activity.findViewById(id).setOnClickListener(onClickListener);
        }
    }

    public static void setViewsOnclick(View view, int[] resIds, OnClickListener onClickListener) {
        for (int id : resIds) {
            view.findViewById(id).setOnClickListener(onClickListener);
        }
    }

    public static int getPx(Context context, int dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp);
    }

    public static boolean checkSdCardExist() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static void addShortcutToDesktop(Context context) {
        // Intent shortcut = new
        // Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //
        // // 不允许重建
        // shortcut.putExtra("duplicate", false);
        // // 设置名字
        // shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
        // context.getString(R.string.app_name));
        // // 设置图标
        // shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
        // Intent.ShortcutIconResource.fromContext(context,
        // R.drawable.logo));
        // // 设置意图和快捷方式关联程序
        // shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(context,
        // SplashActivity.class).setAction(Intent.ACTION_MAIN));
        // // 发送广播
        // context.sendBroadcast(shortcut);
        // Log.e("addShortcutToDesktop1", ".....---99999");
        // 下面这部分和上面其实是一样的，貌似因为无法解决首次安装后，home后再点击快捷方式，会再次进入的问题，还得通过改闪屏页面解决了。
        Intent localIntent1 = new Intent("android.intent.action.MAIN");
        localIntent1.setClassName(context, context.getPackageName() + ".activity.SplashActivity");// your
        // first
        // launch
        // activity
        localIntent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent1.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        Intent localIntent6 = new Intent();
        localIntent6.putExtra("android.intent.extra.shortcut.INTENT", localIntent1);
        localIntent6.putExtra("android.intent.extra.shortcut.NAME",
                context.getResources().getString(R.string.app_name));
        localIntent6.putExtra("android.intent.extra.shortcut.ICON_RESOURCE",
                Intent.ShortcutIconResource.fromContext(context, R.drawable.logo));
        localIntent6.putExtra("duplicate", false);
        localIntent6.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(localIntent6);
    }

    public static boolean existSDCard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    public static String getRealPath(Uri imageUri, Activity context) {
        Uri uri = imageUri;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = context.getContentResolver().query(uri, proj, null, null, null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        return actualimagecursor.getString(actual_image_column_index);
    }

    /**
     * 判断应用是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }


    public static boolean appInstalled(Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(200);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {

            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

}
