package com.fy.tnzbsq.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.activity.AboutActivity;
import com.fy.tnzbsq.activity.CategoryActivity;
import com.fy.tnzbsq.activity.CreateNewActivity;
import com.fy.tnzbsq.activity.FightCategoryActivity;
import com.fy.tnzbsq.activity.FightCommonListActivity;
import com.fy.tnzbsq.activity.FightMenuActivity;
import com.fy.tnzbsq.activity.FightSearchActivity;
import com.fy.tnzbsq.activity.ImageMakeActivity;
import com.fy.tnzbsq.activity.MyCreateActivity;
import com.fy.tnzbsq.activity.MyInfoActivity;
import com.fy.tnzbsq.activity.MyKeepActivity;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.CustomWebViewDelegate;
import com.fy.tnzbsq.service.DownHeadStyleService;
import com.fy.tnzbsq.service.DownLsddService;
import com.fy.tnzbsq.util.CommUtils;
import com.fy.tnzbsq.util.NetUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

public class CustomWebView extends WebView {

    public CustomWebViewDelegate delegate;
    private Context context;

    public CustomWebView(Context context) {
        this(context, null);
    }

    public CustomWebView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initWebSettings();
        addJavascriptInterface(new AndroidJSObject(), "AndroidJSObject");
        this.setWebViewClient(new CustomWebViewClient(context));
        this.setWebChromeClient(new WebChromeClient());
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void initWebSettings() {
        WebSettings webSettings = this.getSettings();
        webSettings.setLoadsImagesAutomatically(false);
        //手机必须运行 在Android 4.2 或更高才能使用setAllowUniversalAccessFromFileURLs() API 级别 16 + 上才可用。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setNeedInitialFocus(false);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 默认不使用缓存
        webSettings.setAppCacheMaxSize(8 * 1024 * 1024); // 缓存最多可以有8M
        webSettings.setAllowFileAccess(true); // 可以读取文件缓存(manifest生效)
        String appCaceDir = context.getDir("cache", Context.MODE_PRIVATE).getPath();
        webSettings.setAppCachePath(appCaceDir);
        if ((Build.VERSION.SDK_INT >= 11) && (Build.MANUFACTURER.toLowerCase().contains("lenovo")))
            this.setLayerType(1, null);
    }

    public class DownAsyncTask extends AsyncTask<Integer, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... params) {
            return NetUtil.is404NotFound(Contants.HEAD_STYLE_DOWN_URL);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (!result) {
                Intent intent = new Intent(context, DownHeadStyleService.class);
                intent.putExtra("downUrl", Contants.HEAD_STYLE_DOWN_URL);
                context.startService(intent);
            } else {
                Toast.makeText(context, "下载地址有误，请稍后重试", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class DownLsddAsyncTask extends AsyncTask<Integer, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... params) {
            return NetUtil.is404NotFound(Contants.LSDD_DOWN_URL);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (!result) {
                Intent intent = new Intent(context, DownLsddService.class);
                intent.putExtra("downUrl", Contants.LSDD_DOWN_URL);
                context.startService(intent);
            } else {
                Toast.makeText(context, "下载地址有误，请稍后重试", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 打开
     */
    public void noInstallRun() {
        //FreeInstallUtil.openApk(context, Contants.GAME_PACKAGE_NAME);
        //判断是否安装
        /*if(FreeInstallUtil.isApkInstall(Contants.GAME_PACKAGE_NAME)){
			FreeInstallUtil.openApk(context, Contants.GAME_PACKAGE_NAME);
		}else{
			//Toast.makeText(context,"游戏初始化中",Toast.LENGTH_SHORT).show();
			//new DownGameAsyncTask().execute();
		}*/
    }

    public static boolean downLoadGame() {
        boolean flag = false;

        HttpURLConnection conn = null;
        InputStream is = null;
        OutputStream os = null;
        try {

            File fileDir = new File(Contants.GAME_DIR);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            final File file = new File(Contants.GAME_DOWN_FILE_PATH);
            URL url = new URL(Contants.GAME_INSTALL_URL);
            //LogUtil.msg("插件正在下载...");
            conn = (HttpURLConnection) url
                    .openConnection();
            if (file.exists() && file.length() == conn.getContentLength()) {
                flag = true;
                conn.disconnect();
            }
            is = conn.getInputStream();
            os = new FileOutputStream(file);
            byte[] bs = new byte[1024];
            int len;
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            flag = true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception e) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                }
            }
        }
        return flag;
    }

    //启动或者下载app
    public void startOrInstallApp(String urls, String mtags) {
        if (CommUtils.appInstalled(context, "com.feiyou.headstyle")) {

            //如果下载文件存在并且已经安装，直接删除下载文件
			/*File downFile = new File(Contants.HEAD_STYLE_DOWN_FILE_PATH);
			if(downFile.exists()){
				downFile.delete();
			}

			PackageManager packageManager = context.getPackageManager();
			Intent intent=new Intent();
			intent = packageManager.getLaunchIntentForPackage("com.feiyou.headstyle");
			context.startActivity(intent);*/

            //如果已安装，则继续之前的操作
            Intent intent = new Intent(context, CategoryActivity.class);
            String column = urls.substring(urls.lastIndexOf("=") + 1, urls.length());
            intent.putExtra("tags", mtags);
            intent.putExtra("column", column);
            context.startActivity(intent);

        } else {
            if (!CommUtils.isServiceWork(context, "com.fy.tnzbsq.service.DownHeadStyleService")) {
                //如果下载文件存在，直接启动安装
                File downFile = new File(Contants.HEAD_STYLE_DOWN_FILE_PATH);
                if (downFile.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.fromFile(downFile),
                            "application/vnd.android.package-archive");
                    context.startActivity(intent);
                } else {
                    //启动服务重新下载
                    new DownAsyncTask().execute();
                }
            } else {
                Toast.makeText(context, "个性头像下载中", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //启动或者下载铃声朵朵app
    public void startOrInstallLsddApp(String urls, String mtags) {
        if (CommUtils.appInstalled(context, "com.whfeiyou.sound")) {

            //如果已安装，则继续之前的操作
            Intent intent = new Intent(context, CategoryActivity.class);
            String column = urls.substring(urls.lastIndexOf("=") + 1, urls.length());
            intent.putExtra("tags", mtags);
            intent.putExtra("column", column);
            context.startActivity(intent);

        } else {
            if (!CommUtils.isServiceWork(context, "com.fy.tnzbsq.service.DownLsddService")) {
                //如果下载文件存在，直接启动安装
                File downFile = new File(Contants.LSDD_DOWN_FILE_PATH);
                if (downFile.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.fromFile(downFile),
                            "application/vnd.android.package-archive");
                    context.startActivity(intent);
                } else {
                    //启动服务重新下载
                    new DownLsddAsyncTask().execute();
                }
            } else {
                Toast.makeText(context, "铃声朵朵下载中", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 监听 所有点击的链接，如果拦截到我们需要的，就跳转到相对应的页面。
    private class CustomWebViewClient extends WebViewClient {

        private Context context;

        public CustomWebViewClient(Context context) {
            this.context = context;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null && url.contains("_detail.html")) {
                String tags = url.substring(url.indexOf("=") + 1, url.indexOf("&"));
                try {
                    tags = URLDecoder.decode(tags, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                /*if ("其他".equals(tags)) {
                    startOrInstallApp(url, tags);
                } else if ("游戏".equals(tags)) {
                    startOrInstallLsddApp(url, tags);
                } else {*/
                    Intent intent = new Intent(context, CategoryActivity.class);
                    String column = url.substring(url.lastIndexOf("=") + 1, url.length());
                    intent.putExtra("tags", tags);
                    intent.putExtra("column", column);
                    context.startActivity(intent);
                //}
                return true;
            }
            if (url != null && url.contains("scq1.html")) {
                Intent intent = new Intent(context, CreateNewActivity.class);

                String id = url.substring(url.indexOf("=") + 1, url.length());
                intent.putExtra("id", id);
                context.startActivity(intent);
                return true;
            }
            if (url != null && url.contains("mykeep.html")) {
                Intent intent = new Intent(context, MyKeepActivity.class);
                context.startActivity(intent);
                return true;
            }
            if (url != null && url.contains("mycreate.html")) {
                Intent intent = new Intent(context, MyCreateActivity.class);
                context.startActivity(intent);
                return true;
            }
            if (url != null && url.contains("about.html")) {
                Intent intent = new Intent(context, AboutActivity.class);
                context.startActivity(intent);
                return true;
            }
            if (url != null && url.contains("my.html")) {
                Intent intent = new Intent(context, MyInfoActivity.class);
                context.startActivity(intent);
                return true;
            }
            if (url != null && url.contains("create_doutu.html")) {
                Intent intent = new Intent(context, ImageMakeActivity.class);

                String img = url.substring(url.indexOf("img=") + 4, url.indexOf("&id="));
                String id = url.substring(url.indexOf("id=") + 3, url.indexOf("&position="));
                String position = url.substring(url.indexOf("position=") + 9, url.indexOf("&color="));
                String color = url.substring(url.indexOf("color=") + 6, url.length());

                intent.putExtra("imagePath", img);
                intent.putExtra("id", id);
                intent.putExtra("color", color);
                intent.putExtra("position", position);
                context.startActivity(intent);

                return true;
            }
            if (url != null && url.contains("index-dt-list.html")) {
                Intent intent = new Intent(context, FightMenuActivity.class);
                context.startActivity(intent);
                return true;
            }
            if (url != null && url.contains("index-dt-list-two.html")) {
                Intent intent = new Intent(context, FightCategoryActivity.class);
                String id = url.substring(url.indexOf("=") + 1, url.indexOf("&"));
                String childName = url.substring(url.lastIndexOf("=") + 1, url.length());
                try {
                    childName = URLDecoder.decode(childName, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                intent.putExtra("id", id);
                intent.putExtra("childName", childName);
                context.startActivity(intent);
                return true;
            }
            if (url != null && url.contains("_common_list.html")) {
                Intent intent = new Intent(context, FightCommonListActivity.class);
                String sortType = url.substring(url.indexOf("=") + 1, url.length());

                intent.putExtra("sortType", sortType);
                context.startActivity(intent);
                return true;
            }
            if (url != null && url.contains("index-dt-search.html")) {
                Intent intent = new Intent(context, FightSearchActivity.class);
                String keyword = url.substring(url.indexOf("=") + 1, url.length());
                intent.putExtra("keyword", keyword);
                context.startActivity(intent);
                return true;
            }

            if (url != null && url.contains("zbsq_list_ad.html")) {
                //已安装，直接启动
                if (CommUtils.appInstalled(context, "com.tencent.tmgp.sgame")) {

                    PackageManager packageManager = context.getPackageManager();
                    Intent intent = new Intent();
                    intent = packageManager.getLaunchIntentForPackage("com.tencent.tmgp.sgame");
                    context.startActivity(intent);
                } else {
                    final File fileDir = new File(Contants.BASE_NORMAL_FILE_DIR);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }

                    gameAdFile = new File(fileDir + "/10005700_com.tencent.tmgp.sgame_u146_1.20.1.21.apk");

                    try {
                        if (gameAdFile.exists()) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setDataAndType(Uri.fromFile(gameAdFile), "application/vnd.android.package-archive");
                            context.startActivity(intent);
                        } else {

                            if (NetUtil.isWIFIConnected(context)) {
                                Toast.makeText(context,"王者荣耀送金币版下载中",Toast.LENGTH_LONG).show();
                                String gameUrl = "http://tj.tt1386.com/452465851/0070000";
                                showProgress();
                                FileDownloader.setup(context);
                                downloadId = FileDownloader.getImpl().create(gameUrl).setPath(fileDir + "/10005700_com.tencent.tmgp.sgame_u146_1.20.1.21.apk", false).setListener(new FileDownloadListener() {
                                    @Override
                                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                        total = totalBytes / 1024 / 1024;
                                    }

                                    @Override
                                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                        total = totalBytes / 1024 / 1024;
                                        progresses = soFarBytes / 1024 / 1024;
                                        Log.e("progress total", "progress total--->" + total);
                                        Log.e("progress", "progress--->" + progresses);
                                    }

                                    @Override
                                    protected void completed(BaseDownloadTask task) {
                                        progresses = total;
                                        Log.e("completed progress", " completed progress--->" + progresses);
                                        NotifyUtil.cancelAll();
                                        //FileDownloader.getImpl().clearAllTaskData();
                                        if (gameAdFile.exists()) {
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.setDataAndType(Uri.fromFile(gameAdFile), "application/vnd.android.package-archive");
                                            context.startActivity(intent);
                                        }
                                    }

                                    @Override
                                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                                    }

                                    @Override
                                    protected void error(BaseDownloadTask task, Throwable e) {

                                    }

                                    @Override
                                    protected void warn(BaseDownloadTask task) {

                                    }
                                }).start();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                return true;
            }

            return super.shouldOverrideUrlLoading(view, url);
        }

        private int downloadId = 0;

        int progresses = 0;

        int total = 0;

        File gameAdFile;

        private void showProgress() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    /*if (progresses > total) {
                        return;
                    }*/

                    NotifyUtil.buildProgress(10201, R.mipmap.game_ad_logo, "正在下载", progresses, total, "下载进度:%dM/%dM").show();//"下载进度:%d%%"
                    showProgress();

                }
            }, 100);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (delegate != null) {
                delegate.initWithUrl(view.getUrl());
            }

            if (url != null) {
                if (url.contains("index.html") || url.contains("new_list.html") || url.contains("mykeep.html")) {
                    //view.loadUrl("javascript:initData()");
                    //view.loadUrl("javascript:init()");
                    if (App.IS_FIRST_RUN) {
                        view.loadUrl("javascript:clearSelectItem()");
                        App.IS_FIRST_RUN = false;
                    }
                }

				/*
				 * if (url.contains("my.html")) {
				 * view.loadUrl("javascript:setMemi('" + App.ANDROID_ID + "')");
				 * }
				 */
            }
        }

    }

    class AndroidJSObject {
        @JavascriptInterface
        public void getTitle(String title) {
            if (delegate != null) {
                if (title != null) {
                    delegate.setTitle(title);
                }
            }
        }

        @JavascriptInterface
        public void getWheelView() {
            if (delegate != null) {
                CustomWebView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        delegate.showWheelView();
                    }
                });
            }
        }

        @JavascriptInterface
        public void createImage(final String id, final String data,final String isVip, final String price) {
            if (delegate != null) {
                CustomWebView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        delegate.createImage(id, data,isVip,price);
                    }
                });
            }
        }

        @JavascriptInterface
        public void saveImage(final String imageRealPath) {
            if (delegate != null) {
                CustomWebView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        delegate.saveImage(imageRealPath);
                    }
                });
            }
        }

        @JavascriptInterface
        public void addKeep(final String id) {
            if (delegate != null) {
                CustomWebView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        delegate.addKeep(id);
                    }
                });
            }
        }

        @JavascriptInterface
        public void imageShow(final String path) {
            if (delegate != null) {
                CustomWebView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        delegate.imageShow(path);
                    }
                });
            }
        }

        @JavascriptInterface
        public void updateVersion() {
            if (delegate != null) {
                CustomWebView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        delegate.updateVersion();
                    }
                });
            }
        }

        // 清除缓存
        @JavascriptInterface
        public void clearCache() {
            if (delegate != null) {
                CustomWebView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        delegate.clearCache();
                    }
                });
            }
        }

        @JavascriptInterface
        public void selectPic(final int xvalue, final int yvalue) {
            if (delegate != null) {
                CustomWebView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        delegate.selectPic(xvalue, yvalue);
                    }
                });
            }
        }

        @JavascriptInterface
        public void submitMesage(final String str, final String description) {
            if (delegate != null) {
                CustomWebView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        delegate.submitMesage(str, description);
                    }
                });
            }
        }

        @JavascriptInterface
        public void toSave() {
            if (delegate != null) {
                CustomWebView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        delegate.toSave();
                    }
                });
            }
        }

        @JavascriptInterface
        public void toShare() {
            if (delegate != null) {
                CustomWebView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        delegate.toShare();
                    }
                });
            }
        }

        @JavascriptInterface
        public void toToast(final String str) {
            if (delegate != null) {
                CustomWebView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "弹出了--" + str, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        @JavascriptInterface
        public void updateUserName(final String userName) {
            if (delegate != null) {
                CustomWebView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        delegate.updateUserName(userName);
                    }
                });
            }
        }

        @JavascriptInterface
        public void setShowState(final boolean flag) {
            if (delegate != null) {
                CustomWebView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        delegate.setShowState(flag);
                    }
                });
            }
        }

        @JavascriptInterface
        public void loadImageList(final int currentPage) {
            if (delegate != null) {
                CustomWebView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        delegate.loadImageList(currentPage);
                    }
                });
            }
        }

        @JavascriptInterface
        public void search(final String keyword) {
            if (delegate != null) {
                CustomWebView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        delegate.search(keyword);
                    }
                });
            }
        }

        @JavascriptInterface
        public String loadDataPath() {
            return App.sdPath;
        }

        @JavascriptInterface
        public void fightMenu() {
            if (delegate != null) {
                CustomWebView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        delegate.fightMenu();
                    }
                });
            }
        }

        @JavascriptInterface
        public void gifResult(final String url) {
            if (delegate != null) {
                CustomWebView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        delegate.gifResult(url);
                    }
                });
            }
        }
    }
}
