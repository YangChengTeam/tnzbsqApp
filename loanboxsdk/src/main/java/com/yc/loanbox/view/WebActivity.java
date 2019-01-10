package com.yc.loanbox.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;
import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.LogUtils;
import com.just.agentweb.WebListenerManager;
import com.just.agentweb.download.AgentWebDownloader;
import com.just.agentweb.download.DefaultDownloadImpl;
import com.just.agentweb.download.DownloadListenerAdapter;
import com.just.agentweb.download.DownloadingService;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.utils.LogUtil;
import com.kk.utils.VUiKit;
import com.tencent.mmkv.MMKV;
import com.yc.loanbox.R;
import com.yc.loanbox.R2;
import com.yc.loanbox.helper.AndroidBug5497Workaround;
import com.yc.loanbox.helper.GlideHelper;
import com.yc.loanbox.model.engin.ClickEngin;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Subscriber;

public class WebActivity extends BaseActivity {
    public final static String TAG = "WebActivity";
    @BindView(R2.id.webView1)
    FrameLayout webView1;

    @BindView(R2.id.name)
    TextView textView;

    @BindView(R2.id.logo)
    ImageView logoImageView;

    @BindView(R2.id.back_btn)
    ImageView backImageView;

    private AgentWeb mAgentWeb;

    private String phone;

    private DownloadingService mDownloadingService;

    private ProgressDialog progressDialog;

    private String id = "";
    private String ad_id = "";
    private String url = "";
    private String ptype = "";
    @Override
    protected int getLayoutId() {
        return R.layout.loanbox_activity_webview;
    }

    @Override
    protected void initViews() {

        AndroidBug5497Workaround.assistActivity(this);
        Intent intent = getIntent();
        String title = "";
        String ico = "";
        if (intent != null) {
            title = intent.getStringExtra("title");
            url = intent.getStringExtra("url");
            ad_id = intent.getStringExtra("ad_id");
            ico = intent.getStringExtra("ico");
            id = intent.getStringExtra("id");
            ptype = intent.getStringExtra("ptype");
            phone = MMKV.defaultMMKV().getString("phone", "");

            textView.setText(title);
            if (ico != null && ico.length() > 8) {
                GlideHelper.circleBorderImageView(this, logoImageView, ico, R.mipmap.product_default_image, 1, ContextCompat.getColor(this, R.color.white), 0, 0);
            } else {
                logoImageView.setVisibility(View.GONE);
            }
            LogUtil.msg(url);
            mAgentWeb = AgentWeb.with(this)
                    .setAgentWebParent(webView1, new FrameLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator(ContextCompat.getColor(this, R.color.colorAccent))
                    .setAgentWebWebSettings(getSettings())
                    .setWebViewClient(new WebViewClient() {
                        public void onPageFinished(WebView view, String url) {
                            view.loadUrl("javascript: for(i=0;i<document.getElementsByTagName(\"input\").length;i++){\n" +
                                    "\tvar ph = document.getElementsByTagName(\"input\")[i].getAttribute(\"placeholder\");\n" +
                                    "    if(ph && ph.indexOf(\"手机号\") != -1){\n" +
                                    "\t\t\tdocument.getElementsByTagName(\"input\")[i].setAttribute(\"value\", \""+phone+"\");\n" +
                                    "var event = document.createEvent('HTMLEvents');\n" +
                                    "event.initEvent(\"input\", true, true);\n" +
                                    "event.eventType = 'message';\n" +
                                    "document.getElementsByTagName(\"input\")[i].dispatchEvent(event);"+"   \n" +
                                    "document.getElementsByTagName(\"input\")[i].oninput=function(e){" +
                                            "if(this.value.length==11){window.android.setPhone(this.value, '"+ WebActivity.this.url+"');" +
                                    "}\n" +
                                    "} }\n"+
                                    "}");
                            super.onPageFinished(view, url);
                        }
                    })
                    .createAgentWeb()
                    .ready()
                    .go(this.url);
            mAgentWeb.getWebCreator().getWebView().addJavascriptInterface(new AndroidInterface(this, id), "android");

            new ClickEngin(this).click(id, "1", phone, url, ad_id, ptype).subscribe();
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle(getString(R.string.app_name));
        progressDialog.setMessage("正在下载" + title);
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setCanceledOnTouchOutside(false);

        RxView.clicks(backImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view -> {
            if (!mAgentWeb.back()) {
                finish();
            }
        });


    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return false;
        }
        if (!mAgentWeb.back()) {
            finish();
        }
        return true;
    }

    /**
     * 更新于 AgentWeb  4.0.0
     */
    protected DownloadListenerAdapter mDownloadListenerAdapter = new DownloadListenerAdapter() {

        /**
         *
         * @param url                下载链接
         * @param userAgent          UserAgent
         * @param contentDisposition ContentDisposition
         * @param mimetype           资源的媒体类型
         * @param contentLength      文件长度
         * @param extra              下载配置 ， 用户可以通过 Extra 修改下载icon ， 关闭进度条 ， 是否强制下载。
         * @return true 表示用户处理了该下载事件 ， false 交给 AgentWeb 下载
         */
        @Override
        public boolean onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, AgentWebDownloader.Extra extra) {
            LogUtils.i(TAG, "onStart:" + url);
            VUiKit.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog.show();
                }
            });
            extra.setOpenBreakPointDownload(true) // 是否开启断点续传
                    .setIcon(R.drawable.ic_file_download_black_24dp) //下载通知的icon
                    .setConnectTimeOut(6000) // 连接最大时长
                    .setBlockMaxTime(10 * 60 * 1000)  // 以8KB位单位，默认60s ，如果60s内无法从网络流中读满8KB数据，则抛出异常
                    .setDownloadTimeOut(Long.MAX_VALUE) // 下载最大时长
                    .setParallelDownload(false)  // 串行下载更节省资源哦
                    .setEnableIndicator(true)  // false 关闭进度通知
                    .addHeader("Cookie", "xx") // 自定义请求头
                    .setAutoOpen(true) // 下载完成自动打开
                    .setForceDownload(true); // 强制下载，不管网络网络类型
            return false;
        }



        /**
         *
         * 不需要暂停或者停止下载该方法可以不必实现
         * @param url
         * @param downloadingService  用户可以通过 DownloadingService#shutdownNow 终止下载
         */
        @Override
        public void onBindService(String url, DownloadingService downloadingService) {
            super.onBindService(url, downloadingService);
            mDownloadingService = downloadingService;
            LogUtils.i(TAG, "onBindService:" + url + "  DownloadingService:" + downloadingService);
        }

        /**
         * 回调onUnbindService方法，让用户释放掉 DownloadingService。
         * @param url
         * @param downloadingService
         */
        @Override
        public void onUnbindService(String url, DownloadingService downloadingService) {
            super.onUnbindService(url, downloadingService);
            mDownloadingService = null;
            LogUtils.i(TAG, "onUnbindService:" + url);
        }

        /**
         *
         * @param url  下载链接
         * @param loaded  已经下载的长度
         * @param length    文件的总大小
         * @param usedTime   耗时 ，单位ms
         * 注意该方法回调在子线程 ，线程名 AsyncTask #XX 或者 AgentWeb # XX
         */
        @Override
        public void onProgress(String url, long loaded, long length, long usedTime) {
            int mProgress = (int) ((loaded) / Float.valueOf(length) * 100);
            LogUtils.i(TAG, "onProgress:" + mProgress);

            VUiKit.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog.setProgress(mProgress);
                }
            });
            super.onProgress(url, loaded, length, usedTime);
        }

        /**
         *
         * @param path 文件的绝对路径
         * @param url  下载地址
         * @param throwable    如果异常，返回给用户异常
         * @return true 表示用户处理了下载完成后续的事件 ，false 默认交给AgentWeb 处理
         */
        @Override
        public boolean onResult(String path, String url, Throwable throwable) {
            new ClickEngin(WebActivity.this).click(id, "2", phone, url, ad_id, ptype).subscribe(new Subscriber<ResultInfo<Void>>() {
                @Override
                public void onCompleted() {
                    finish();
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(final ResultInfo<Void> resultInfo) {

                }
            });
            VUiKit.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            });
            if (null == throwable) { //下载成功
                File apkFile = new File(path);
                Uri apkUri = getUriFromFile(WebActivity.this, apkFile);
                Intent installIntent = new Intent(Intent.ACTION_VIEW);
                installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                installIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                startActivity(installIntent);
            } else {//下载失败
            }
            return false; // true  不会发出下载完成的通知 , 或者打开文件
        }
    };

    /**
     * @return IAgentWebSettings
     */
    public IAgentWebSettings getSettings() {
        return new AbsAgentWebSettings() {
            private AgentWeb mAgentWeb;

            @Override
            protected void bindAgentWebSupport(AgentWeb agentWeb) {
                this.mAgentWeb = agentWeb;
            }

            /**
             * AgentWeb 4.0.0 内部删除了 DownloadListener 监听 ，以及相关API ，将 Download 部分完全抽离出来独立一个库，
             * 如果你需要使用 AgentWeb Download 部分 ， 请依赖上 compile 'com.just.agentweb:download:4.0.0 ，
             * 如果你需要监听下载结果，请自定义 AgentWebSetting ， New 出 DefaultDownloadImpl，传入DownloadListenerAdapter
             * 实现进度或者结果监听，例如下面这个例子，如果你不需要监听进度，或者下载结果，下面 setDownloader 的例子可以忽略。
             * @param webView
             * @param downloadListener
             * @return WebListenerManager
             */
            @Override
            public WebListenerManager setDownloader(WebView webView, android.webkit.DownloadListener downloadListener) {
                return super.setDownloader(webView,
                        DefaultDownloadImpl
                                .create((Activity) webView.getContext(),
                                        webView,
                                        mDownloadListenerAdapter,
                                        mDownloadListenerAdapter,
                                        this.mAgentWeb.getPermissionInterceptor()));
            }
        };
    }

    private Uri getUriFromFile(Context context, File file) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = getUriFromFileForN(context, file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    private Uri getUriFromFileForN(Context context, File file) {
        Uri fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".AgentWebFileProvider", file);
        return fileUri;
    }

}
