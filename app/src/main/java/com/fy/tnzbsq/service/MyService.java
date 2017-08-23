package com.fy.tnzbsq.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

/**
 * Created by admin on 2016/8/11.
 */
public class MyService extends Service {
    public static final String TAG = "MyService";

    private Context context;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = MyService.this;
        Log.w(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(TAG, "onStartCommand");
        startPush();
        return START_STICKY;
    }

    /**
     * 启动推送
     */
    public void startPush() {
        Resources resource = this.getResources();
        //百度推送
        PushManager.startWork(MyService.this, PushConstants.LOGIN_TYPE_API_KEY, "kNrcQdmhovXlSwSzANMIGs3r");

        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
                resource.getIdentifier("notification_custom_builder", "layout", context.getPackageName()),
                resource.getIdentifier("notification_icon", "id", context.getPackageName()),
                resource.getIdentifier("notification_title", "id", context.getPackageName()),
                resource.getIdentifier("notification_text", "id", context.getPackageName()));
        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
        cBuilder.setNotificationDefaults(Notification.DEFAULT_VIBRATE);
        cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
        cBuilder.setLayoutDrawable(resource.getIdentifier(
                "logo_108", "drawable", context.getPackageName()));

        // 推送高级设置，通知栏样式设置为下面的ID
        PushManager.setNotificationBuilder(this, 1, cBuilder);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy");
        Intent intent = new Intent();
        intent.setAction("com.fy.tnzbsq.pushData");
        context.sendOrderedBroadcast(intent,null);
    }
}
