package com.fy.tnzbsq.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.utils.FileUtils;
import org.kymjs.kjframe.utils.KJLoger;
import org.kymjs.kjframe.utils.PreferenceHelper;
import org.kymjs.kjframe.utils.SystemTool;


import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.ActsRet;

import com.fy.tnzbsq.common.Contants;

import com.fy.tnzbsq.common.ServiceInterface;

import com.fy.tnzbsq.view.CustomDialog;

import com.fy.tnzbsq.view.RoundProgressBar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SplashActivity extends BaseActivity {

    public final static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").disableHtmlEscaping()
            .setPrettyPrinting()
            .create();

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void initData() {
        super.initData();

        //File file = new File(Contants.ALL_DATA_DIR_PATH, Contants.ALL_DATA_FILENAME);
        //if (file.exists()) {
            /*
             * // 历史更新时间 String historyData =
			 * PreferenceHelper.readString(context, Contants.UPDATE_TIME,
			 * Contants.CURRENT_DATA,""); // 当前时间 String currentData =
			 * SystemTool.getDataTime("yyyy-MM-dd");
			 * 
			 * if (historyData != null && !currentData.equals(historyData)) {
			 * task = new ActsAllDataTask().execute(); }
			 */
        //file.delete();
        //}

        //初始化时，创建默认的文件目录
        File fileDir = new File(Contants.ALL_DATA_DIR_PATH);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        new ActsAllDataTask().execute();
        new FightAllDataTask().execute();
    }

    /**
     * 暂时不使用
     */
    @SuppressWarnings("unused")
    private void getAllData() {
        KJHttp kjh = new KJHttp();
        HttpParams params = new HttpParams();
        params.put("mime", App.ANDROID_ID);
        kjh.post("http://zs.qqtn.com/zbsq/index.php?m=Home&c=Index&a=test", params, new HttpCallBack() {
            @Override
            public void onSuccess(Map<String, String> headers, byte[] t) {
                super.onSuccess(headers, t);
                // 获取cookie
                KJLoger.debug("===" + headers.get("Set-Cookie"));

                if (t != null && t.length > 0) {
                    String result = new String(t);
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                        File fileDir = new File(Contants.ALL_DATA_DIR_PATH);
                        if (!fileDir.exists()) {
                            fileDir.mkdirs();
                        }
                        File file = new File(Contants.ALL_DATA_DIR_PATH, Contants.ALL_DATA_FILENAME);
                        if (file.exists()) {
                            file.delete();
                        }

                        try {
                            file.createNewFile();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            fos.write(result.getBytes());
                            fos.close();

                            PreferenceHelper.write(context, Contants.UPDATE_TIME, Contants.CURRENT_DATA, SystemTool.getDataTime("yyyy-MM-dd"));

                        } catch (Exception e) {
                            Toast.makeText(context, "数据加载失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "数据加载失败", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // Toast.makeText(context, "获取数据失败,请稍后重试!",
                    // Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "数据加载失败", Toast.LENGTH_SHORT).show();
                }
            }

			/*public void onSuccess(String t) {
                super.onSuccess(t);
                KJLoger.debug("===onsuccess" + t);
            }*/
        });
    }

    /**
     * 获取所有装逼类数据
     *
     * @author admin
     */
    private class ActsAllDataTask extends AsyncTask<Void, Void, String> {

        private String lastId = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lastId = getLastActsDataId();
        }

        @Override
        protected String doInBackground(Void... params) {
            return ServiceInterface.getAllDataByLastId(context, lastId);
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                super.onPostExecute(result);

                if (result != null) {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        try {
                            File fileDir = new File(Contants.ALL_DATA_DIR_PATH);
                            if (!fileDir.exists()) {
                                fileDir.mkdirs();
                            }

                            String localData = "";
                            File file = new File(Contants.ALL_DATA_DIR_PATH, Contants.ALL_DATA_FILENAME);

                            if (file.exists()) {
                                localData = FileUtils.readFile(file.getAbsolutePath());
                                file.delete();
                            } else {
                                localData = FileUtils.readFileFromAssets(context, "data/data.js");
                            }

                            file.createNewFile();

                            localData = localData.substring(localData.indexOf("[") + 1, localData.lastIndexOf("]"));

                            localData = localData.substring(0, localData.lastIndexOf("]"));//截取中间部分

                            StringBuffer sbf = new StringBuffer("var data = ");
                            String first = result.substring(0, result.indexOf("[") + 1);
                            String last = result.substring(result.indexOf("[") + 1);

                            String indexData = result.substring(result.indexOf("[") + 1, result.indexOf("]"));
                            if (indexData != null && indexData.length() > 0) {
                                sbf.append(first).append(localData).append(",").append(last);
                            } else {
                                sbf.append(first).append(localData).append(last);
                            }

                            //result = " var data = " + sbf.toString();//重新封装
                            FileOutputStream fos = new FileOutputStream(file);
                            fos.write(sbf.toString().getBytes());
                            fos.close();

                            PreferenceHelper.write(context, Contants.UPDATE_TIME, Contants.CURRENT_DATA, SystemTool.getDataTime("yyyy-MM-dd"));

                            // 最后通知更新文件
                            if (file != null) {
                                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, "数据加载失败,请下拉刷新重试", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // 此时SDcard不存在或者不能进行读写操作的
                        Toast.makeText(context, "数据加载失败,请下拉刷新重试", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "数据加载失败,请下拉刷新重试", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取所有斗图类数据
     *
     * @author admin
     */
    private class FightAllDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            return ServiceInterface.getAllFightData(context);
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                super.onPostExecute(result);

                if (result != null) {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {


                        File file = new File(Contants.ALL_DATA_DIR_PATH, Contants.ALL_FIGHT_DATA_FILENAME);
                        if (file.exists()) {
                            file.delete();
                        }
                        //if (!file.exists()) {
                        file.createNewFile();
                        //}
                        try {
                            result = " var data= " + result;//重新封装
                            FileOutputStream fos = new FileOutputStream(file);
                            fos.write(result.getBytes());
                            fos.close();
                            // Toast.makeText(MainActivity.this, "写入文件成功",
                            // Toast.LENGTH_LONG).show();

                            // 最后通知更新文件
                            if (file != null) {
                                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
                            }
                        } catch (Exception e) {
                            //Toast.makeText(context, "数据加载失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // 此时SDcard不存在或者不能进行读写操作的
                        //Toast.makeText(context, "数据加载失败", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    //Toast.makeText(context, "数据加载失败", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void initDataFromThread() {
        super.initDataFromThread();
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void threadDataInited() {
        super.threadDataInited();

        boolean isAgree = PreferenceHelper.readBoolean(SplashActivity.this, Contants.MZ_DATA, Contants.IS_AGREE, false);
        if (!isAgree) {
            CustomDialog.Builder builder = new CustomDialog.Builder(SplashActivity.this);

            builder.setMessage("声明:本APP仅供娱乐使用,请勿用于非法用途，否则一切后果自己承担，如果不同意，请退出!");
            builder.setTitle("免责申明");
            builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (isValidContext(SplashActivity.this) && dialog != null) {
                        dialog.dismiss();
                    }
                    SplashActivity.this.finish();
                }
            });

            builder.setNegativeButton("同意", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (isValidContext(SplashActivity.this) && dialog != null) {
                        dialog.dismiss();
                    }
                    PreferenceHelper.write(SplashActivity.this, Contants.MZ_DATA, Contants.IS_AGREE, true);
                    skipNextActivity();
                    //addBdView();
                }
            });
            CustomDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            if (isValidContext(SplashActivity.this) && dialog != null) {
                dialog.show();
            }
        } else {
            skipNextActivity();
            //addBdView();
        }
    }

    /**
     * 获取装逼缓存数据中的最后一条记录的ID
     *
     * @return
     */
    public String getLastActsDataId() {

        String lastId = "700";//默认的最大ID
        String result = "";//读取的缓存数据

        try {
            File file = new File(Contants.ALL_DATA_DIR_PATH, Contants.ALL_DATA_FILENAME);

            boolean hasNewData = PreferenceHelper.readBoolean(context, Contants.NEW_DATA_SHOW, Contants.NEW_DATA_RESULT, false);

            //将最新的文件复制到缓存文件中
            if (hasNewData) {
                File newFile = new File(Contants.ALL_DATA_DIR_PATH, Contants.NEW_DATA_FILENAME);
                if (file.exists()) {
                    file.delete();
                }
                FileUtils.copyFile(newFile, file);
            }

            if (file.exists()) {
                result = FileUtils.readFile(file.getAbsolutePath());
            } else {
                //file.createNewFile();
                result = FileUtils.readFileFromAssets(context, "data/data.js");
            }
            //去除前面的 "var data = "然后解析
            result = result.substring(result.indexOf("{"), result.length());

            ActsRet acts = gson.fromJson(result, new TypeToken<ActsRet>() {
            }.getType());
            lastId = acts.maxDataId;
            Log.e("acts--data", "lastId===" + lastId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastId;
    }



    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void skipNextActivity() {
//		boolean isFirst = PreferenceHelper.readBoolean(SplashActivity.this, Contants.START_DATA, Contants.IS_FIRST,
//				true);
        //暂时注释，直接跳转到主页面
//		if (isFirst) {
//			skipActivity(aty, GuideActivity.class);
//		} else {
        //skipActivity(aty, MainActivity.class);
        //}

        Intent intent = new Intent();
        intent.setClass(SplashActivity.this,MainActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
    }

    @SuppressLint("NewApi")
    private boolean isValidContext(Context ctx) {
        Activity activity = (Activity) ctx;

        if (Build.VERSION.SDK_INT > 17) {
            if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
                return false;
            } else {
                return true;
            }
        } else {
            if (activity == null || activity.isFinishing()) {
                return false;
            } else {
                return true;
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle arg0) {
        super.onSaveInstanceState(arg0);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
