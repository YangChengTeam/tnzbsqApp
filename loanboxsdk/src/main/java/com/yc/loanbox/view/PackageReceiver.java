package com.yc.loanbox.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.yc.loanbox.model.engin.InstallEngin;

import java.util.List;

public class PackageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }

        String packageName = intent.getDataString();
        if(packageName != null && packageName.split(":").length > 1){
            packageName = packageName.split(":")[1];
        }
        PackageManager pm = context.getPackageManager();
        // 查询所有已经安装的应用程序
        List<ApplicationInfo> listAppcations = pm
                .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);// GET_UNINSTALLED_PACKAGES代表已删除，但还有安装目录的
        String pkgName = "";
        String appLabel = "";
        for (ApplicationInfo app : listAppcations)
        {
            pkgName = app.packageName;
            if(pkgName.equals(packageName)){
                appLabel = (String) app.loadLabel(pm);
                break;
            }
        }
        String type = "0";
        // 安装
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            type = "0";
        }
        // 覆盖安装
        if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) {
            type = "1";
        }
        // 移除
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            type = "2";
        }
        new InstallEngin(context).install(packageName, appLabel, type).subscribe();
    }
}
