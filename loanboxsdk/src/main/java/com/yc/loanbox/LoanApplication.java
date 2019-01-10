package com.yc.loanbox;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;

import com.alibaba.fastjson.JSON;
import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.FileUtil;
import com.kk.utils.LogUtil;
import com.tencent.mmkv.MMKV;
import com.yc.loanbox.model.bean.ChannelInfo;
import com.yc.loanbox.model.bean.ProductInfo;
import com.yc.loanbox.model.bean.UserInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class LoanApplication {

    public ProductInfo init_img;
    public UserInfo userInfo;
    public ChannelInfo channelInfo;
    public String agent_id = "sdk";

    private static LoanApplication loanApplication;
    private Context mContext;

    public static LoanApplication getLoanApplication(){
        if(loanApplication == null){
            loanApplication = new LoanApplication();
        }
        return loanApplication;
    }

    public void inits(Context context){
        this.mContext = context;
        // 初始化MMKV
        MMKV.initialize(context);
        new Thread(()->{
            initHttpInfo();
        }).start();
    }

    private void initHttpInfo(){
        ApplicationInfo appinfo = mContext.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        ZipFile zf = null;
        try {
            zf = new ZipFile(sourceDir);
            ZipEntry ze = zf.getEntry("META-INF/channelconfig.json");
            InputStream in = zf.getInputStream(ze);
            String result = FileUtil.readString(in);
            channelInfo = JSON.parseObject(result, ChannelInfo.class);
            LogUtil.msg("渠道来源->" + result);
        } catch (Exception e) {
            LogUtil.msg("apk中channelconfig.json文件不存在", LogUtil.W);
        } finally {
            if (zf != null) {
                try {
                    zf.close();
                } catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
            }
        }
        GoagalInfo.get().init(mContext);
        HttpConfig.setPublickey("-----BEGIN PUBLIC KEY-----\n" +
                "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAq1wNiX3iQt+Q7juXZDNR\n" +
                "Eq2jGqx+2pXM4ddoZ1rkHb3XJFhrBguI/R11IfmTioPlTnheJqYkJf0NGzzxW2t1\n" +
                "nDKwbjoZ+d7UMehCDV44+FQMtzhRAFjmcQIXn6AaL2bkJFzHvoTtYPqgqgT5V4L6\n" +
                "+DhLSuPSwIVAC1aw1+iUk3jbg3ETzERSS6LDHTRi2ng7rpKAeHKeJ2RtbrcetCxv\n" +
                "YF+6QabnJhZGtr6cvp9CtFv5bSc2JsCqbJbsDGM6OPAjQjtpmImxQiXcI1gko8WP\n" +
                "+k1nx9GPJBhtdAXORRGRoHA8fUCveAJPDw1jSF3lBDf+1BHx+XeVX4/sVybd5Rn3\n" +
                "IE21UeuF+kbmwULJKUDzQNIwlXA+k4faRhdKeFCOeqldozwhP+575L/vVlyvxx/M\n" +
                "UJdA4vUziyO1l/IQEGzJ7b4AWfJ6sQEKDjODuLM+DM9MAuYddFnNfKj8XVi3jx9y\n" +
                "0OOAb/4Rb3UPeOUF9R4Sr0nLmL/1ITL8/9rJaue/e/D7H4xfQNbCtSTPhsa/+UOt\n" +
                "j3AQsNUjqkoLMXm7vtXEIshXEm4mlmMl98LsXyK3B6lMiV7jO4Vyp8muga8I/nH3\n" +
                "Snw5e86AHSZdnbQcLTDx9sgqN2mSL3MqLp9oiL4KGxNdNdt8EunGRycTsj09o7oz\n" +
                "Lfxf+/8xTiWygyUTThX+/GUCAwEAAQ==\n" +
                "-----END PUBLIC KEY-----");
        //设置http默认参数
        Map<String, String> params = new HashMap<>();

        params.put("agent_id", agent_id);
        params.put("ts", System.currentTimeMillis() + "");
        params.put("device_type", "2");
        params.put("imeil", GoagalInfo.get().uuid);
        String sv = android.os.Build.MODEL.contains(android.os.Build.BRAND) ? android.os.Build.MODEL + " " + android
                .os.Build.VERSION.RELEASE : Build.BRAND + " " + android
                .os.Build.MODEL + " " + android.os.Build.VERSION.RELEASE;
        params.put("sv", sv);
        if(channelInfo != null){
            params.put("site_id", channelInfo.getSite_id()+"");
            params.put("soft_id", channelInfo.getSoft_id()+"");
            params.put("referer_url", channelInfo.getNode_url()+"");
        }
        if (GoagalInfo.get().packageInfo != null) {
            params.put("app_version", GoagalInfo.get().packageInfo.versionCode + "");
        }
        HttpConfig.setDefaultParams(params);

    }


}
