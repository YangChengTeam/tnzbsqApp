package com.fy.tnzbsq;import android.app.Application;import android.content.Context;import android.os.Build;import android.util.Log;import com.fy.tnzbsq.bean.User;import com.fy.tnzbsq.common.Contants;import com.fy.tnzbsq.util.CommUtils;import com.fy.tnzbsq.util.DataCleanManager;import com.fy.tnzbsq.util.FileUtils;import com.fy.tnzbsq.view.NotifyUtil;import com.kk.securityhttp.domain.GoagalInfo;import com.kk.securityhttp.net.contains.HttpConfig;import com.kk.utils.FileUtil;import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;import com.nostra13.universalimageloader.core.ImageLoader;import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;import com.nostra13.universalimageloader.core.assist.QueueProcessingType;import com.tencent.tauth.Tencent;import com.umeng.socialize.PlatformConfig;import com.zxy.tiny.Tiny;import org.kymjs.kjframe.utils.PreferenceHelper;import org.kymjs.kjframe.utils.SystemTool;import java.util.HashMap;import java.util.Map;public class App extends Application {    public static String ANDROID_ID;    public static boolean IS_FIRST_RUN = true;    public static int CURRENT_INDEX = 0;    //默认值    public static String sdPath = "/storage/emulated/0/Android/data/com.ant.flying/cache";    public static Tencent mTencent;    public static String start = "";    public static boolean isInstall = false;    public static User loginUser = null;    public static float siglePrice;    public static float vipPrice;    public static String sigleRemark;    public static String vipRemark;    public String publicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAy/M1AxLZjZOyJToExpn1\n" +            "hudAWySRzS+aGwNVdX9QX6vK38O7WUA7h/bYqBu+6tTRC6RnL9BksMrIf5m6D3rt\n" +            "faYYmxfe/FI4ZD5ybIhFuRUi95e/J2vQVElsSNqSz7ewXquZpKZAqlzH4hGgOqmO\n" +            "THlrwiQwX66bS7x7kDmvxMd5ZRGhTvz62kpKb/orcnWQ1KElNc/bIzTtv3jsrMgH\n" +            "FVdFZfev91ew4Kf1YJbqGBGKslBsIoGsgTxI94T6d6XEFxSzdvrRwKhOobXIaOhZ\n" +            "o3GBCZIA/1ZOwLK6RyrWdprz+60xifcYIkILdZ7yPazSfHCVHFY6o/fQjK4dxQDW\n" +            "Gw0fxN9QX+v3+48nW7QIBx4KNYNIW/eetGhXpOwV4PjNt15fcwJkKsx2W3VQuh93\n" +            "jdYB4xMyDUnRwb9np/QR1rmbzSm5ySGkmD7NAj03V+O82Nx4uxsdg2H7EQdVcY7e\n" +            "6dEdpLYp2p+VkDd9t/5y1D8KtC35yDwraaxXveTMfLk8SeI/Yz4QaX6dolZEuUWa\n" +            "tLaye2uA0w25Ee35irmaNDLhDr804B7U7M4kkbwY7ijvvhnfb1NwFY5lw/2/dZqJ\n" +            "x2gH3lXVs6AM4MTDLs4BfCXiq2WO15H8/4Gg/2iEk8QhOWZvWe/vE8/ciB2ABMEM\n" +            "vvSb829OOi6npw9i9pJ8CwMCAwEAAQ==";    @Override    public void onCreate() {        super.onCreate();        PlatformConfig.setWeixin("wxf9506f64f1e46cbe", "d68dec4f0bc2b06f583ad18b81a174a4");        PlatformConfig.setQQZone("1105292195", "nQ381gRY5t7V9rj4");        mTencent = Tencent.createInstance("1105292195", getApplicationContext());        initImageLoader(getApplicationContext());        try {            ANDROID_ID = SystemTool.getPhoneIMEI(getApplicationContext());        } catch (Exception e) {            e.printStackTrace();        } finally {            if (ANDROID_ID == null || ANDROID_ID.length() == 0) {                ANDROID_ID = "35" + Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10                        + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10                        + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10                        + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 + Build.TYPE.length() % 10                        + Build.USER.length() % 10;            }        }        Log.i("ANDROID_ID", "ANDROID_ID->" + ANDROID_ID);        if (FileUtils.getDiskCacheDir(getApplicationContext()) != null) {            sdPath = FileUtils.getDiskCacheDir(getApplicationContext());        }        Log.i("APP-PATH", "PATH->" + sdPath);        int versionCode = PreferenceHelper.readInt(getApplicationContext(), Contants.VERSION_CODE, Contants.VERSION_CODE_DATA, -1);        if (versionCode > -1) {            if (CommUtils.getVersionCode(getApplicationContext()) > versionCode) {                DataCleanManager.cleanApplicationData(getApplicationContext());            }        } else {            DataCleanManager.cleanApplicationData(getApplicationContext());        }        PreferenceHelper.write(getApplicationContext(), Contants.VERSION_CODE, Contants.VERSION_CODE_DATA, CommUtils.getVersionCode(getApplicationContext()));        GoagalInfo.get().init(this);        FileUtil.setUuid(GoagalInfo.get().uuid);        //设置http默认参数        String agent_id = "1";        Map<String, String> params = new HashMap<>();        if (GoagalInfo.get().channelInfo != null && GoagalInfo.get().channelInfo.agent_id != null) {            params.put("from_id", GoagalInfo.get().channelInfo.from_id + "");            params.put("author", GoagalInfo.get().channelInfo.author + "");            agent_id = GoagalInfo.get().channelInfo.agent_id;        }        params.put("agent_id", agent_id);        params.put("ts", System.currentTimeMillis() + "");        params.put("imeil", GoagalInfo.get().uuid);        String sv = android.os.Build.MODEL.contains(android.os.Build.BRAND) ? android.os.Build.MODEL + " " + android                .os.Build.VERSION.RELEASE : Build.BRAND + " " + android                .os.Build.MODEL + " " + android.os.Build.VERSION.RELEASE;        params.put("sv", sv);        params.put("device_type", "2");        if (GoagalInfo.get().packageInfo != null) {            params.put("app_version", GoagalInfo.get().packageInfo.versionName + "");        }        HttpConfig.setDefaultParams(params);        HttpConfig.setPublickey(publicKey);        NotifyUtil.init(getApplicationContext());        Tiny.getInstance().init(this);    }    public static void initImageLoader(Context context) {        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)                .memoryCacheExtraOptions(1080, 1920)                .threadPriority(Thread.NORM_PRIORITY - 2)                .denyCacheImageMultipleSizesInMemory()                .diskCacheFileNameGenerator(new Md5FileNameGenerator())                .tasksProcessingOrder(QueueProcessingType.LIFO)                .writeDebugLogs()                .build();        ImageLoader.getInstance().init(config);    }}