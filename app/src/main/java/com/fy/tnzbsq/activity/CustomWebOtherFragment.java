package com.fy.tnzbsq.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.Acts;
import com.fy.tnzbsq.bean.GameInfo;
import com.fy.tnzbsq.bean.MyCreateRet;
import com.fy.tnzbsq.bean.Result;
import com.fy.tnzbsq.bean.UserRet;
import com.fy.tnzbsq.bean.VersionUpdateServiceRet;
import com.fy.tnzbsq.common.AppCustomViews;
import com.fy.tnzbsq.common.AppCustomViews.onAlertDialogBtnClickListener;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.CustomWebViewDelegate;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.common.ServiceInterface;
import com.fy.tnzbsq.common.StatusCode;
import com.fy.tnzbsq.service.UpdateService;
import com.fy.tnzbsq.util.AlertUtil;
import com.fy.tnzbsq.util.CommUtils;
import com.fy.tnzbsq.util.CustomUtils;
import com.fy.tnzbsq.util.NetUtil;
import com.fy.tnzbsq.view.CustomProgress;
import com.fy.tnzbsq.view.CustomWebView;
import com.fy.tnzbsq.view.NavLineLayout;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.utils.Log;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.utils.KJLoger;
import org.kymjs.kjframe.utils.PreferenceHelper;
import org.kymjs.kjframe.utils.SystemTool;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomWebOtherFragment extends CustomBaseFragment
        implements CustomWebViewDelegate, NavLineLayout.NavDelegate, OnRefreshListener {

    public final int MVALUE = 128;

    private static final int REQUESTCODE_PICK = 0;// 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;// 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2;// 图片裁切标记
    public CustomWebView customWebView;
    // private NavLineLayout navLineLayout;
    private SwipeRefreshLayout swipeLayout;
    public String url = "file:///android_asset/index-dt.html";
    private AsyncTask<?, ?, ?> task;

    private AsyncTask<?, ?, ?> addKeppTask;

    private ProgressDialog progressDialog;

    private String picUploadUrl = Server.URL_TOU_GAO;

    private String urlpath;
    private String resultStr = "";

    public AppCustomViews customWidgets;

    private AsyncTask<?, ?, ?> checkVersionTask;

    private CustomProgress dialog;

    private String qqStr;

    private String description;

    // private DownloadService service;

    private int currentPage = 1;

    private List<String> imageList;

    private List<Acts> actsList;

    public boolean isShow = false;

    private String tempName = "";

    private View mRooView;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(getActivity(), "投稿成功!", Toast.LENGTH_SHORT).show();
                    customWebView.loadUrl("javascript:clear();");
                    break;
                case 1:
                    Toast.makeText(getActivity(), "投稿失败,请重试!", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getActivity(), "缓存已清除", Toast.LENGTH_SHORT).show();
                    // 最后通知更新
                    getActivity().sendBroadcast(
                            new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Contants.BASE_SD_DIR)));
                    break;
                case 3:
                    // if (service != null) {
                    // 此条不要 service.cancelTask();
                    // }

                    // customWebView.loadUrl("javascript:refresh();");
                    // Toast.makeText(getActivity(), "下载完成",
                    // Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    // Toast.makeText(getActivity(), "下载失败",
                    // Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(getActivity(), "下载地址有误，请稍后重试", Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

	/*
     * public void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState); String tempStr =
	 * FileUtil.readFile(Contants.ALL_DATA_DIR_PATH + "/" +
	 * Contants.ALL_DATA_FILENAME); tempStr = tempStr.replaceAll("var data=",
	 * " "); KJLoger.debug(tempStr); ActsRet ar =
	 * Contants.gson.fromJson(tempStr, new TypeToken<ActsRet>() { }.getType());
	 * actsList = ar.data; if (actsList == null || actsList.size() == 0) {
	 * actsList = new ArrayList<Acts>(); } imageList = new ArrayList<String>();
	 * cacheTopImage(); cacheImage(); };
	 */

    public float x, y;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRooView == null) {
            mRooView = inflater.inflate(R.layout.main_other_fragment, null);

            customWebView = (CustomWebView) mRooView.findViewById(R.id.webview);
            // navLineLayout = (NavLineLayout)view.findViewById(R.id.nav);

            swipeLayout = (SwipeRefreshLayout) mRooView.findViewById(R.id.swipe);
            swipeLayout.setColorSchemeResources(
                    android.R.color.holo_red_light,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_blue_light);
            swipeLayout.setOnRefreshListener(this);

            customWebView.delegate = this;
            // navLineLayout.delegate = this;
            // 加载数据
            // initData();
            loadUrl(url);
            // Log.i("-----debug--", url);
            customWidgets = new AppCustomViews(getActivity());

            //每次都检测更新
            task = new VersionUpdateServiceTask(true).execute();

            customWebView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            x = event.getX();
                            y = event.getY();
                            Log.i("ACTION_DOWN", x + "--" + y);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (event.getX() != x) {
                                swipeLayout.setEnabled(false);
                            }

                            Log.i("ACTION_MOVE", x + "--" + y);
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            swipeLayout.setEnabled(true);
                            break;
                    }
                    return false;
                }
            });
        }
        return mRooView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

	/*
     * public void cacheTopImage(){ List<String> topImageList = new
	 * ArrayList<String>(); for(int i=0;i<actsList.size();i++){
	 * if(actsList.get(i).isbest >0){ topImageList.add(actsList.get(i).bestimg);
	 * } } service = new DownloadService(Contants.ALL_SMALL_IMAGE_PATH,
	 * topImageList, new DownloadStateListener() {
	 * 
	 * @Override public void onFinish() { // 图片下载成功后，实现您的代码 Message message =
	 * new Message(); message.what = 3; handler.sendMessage(message); }
	 * 
	 * @Override public void onFailed() { // 图片下载成功后，实现您的代码 Message message =
	 * new Message(); message.what = 4; handler.sendMessage(message); } });
	 * service.startDownload(); }
	 * 
	 * public void cacheImage() {
	 * 
	 * if (imageList != null && imageList.size() > 0) { imageList.clear(); }
	 * 
	 * int totalPage = 0; if (actsList.size() % 10 == 0) { totalPage =
	 * actsList.size() / 10; } else { totalPage = actsList.size() / 10 + 1; }
	 * 
	 * if (totalPage >= currentPage) { int temp = 0; if(totalPage >
	 * currentPage){ temp = currentPage * 10; } if(totalPage == currentPage){
	 * temp = actsList.size(); }
	 * 
	 * for (int i = (currentPage - 1) * 10; i < temp; i++) {
	 * imageList.add(actsList.get(i).smallimg); } }
	 * 
	 * service = new DownloadService(Contants.ALL_SMALL_IMAGE_PATH, imageList,
	 * new DownloadStateListener() {
	 * 
	 * @Override public void onFinish() { // 图片下载成功后，实现您的代码 Message message =
	 * new Message(); message.what = 3; handler.sendMessage(message); }
	 * 
	 * @Override public void onFailed() { // 图片下载成功后，实现您的代码 Message message =
	 * new Message(); message.what = 4; handler.sendMessage(message); } });
	 * 
	 * service.startDownload(); }
	 */

    public void initData() {
        File file = new File(Contants.ALL_DATA_DIR_PATH, Contants.ALL_DATA_FILENAME);
        if (file.exists()) {
            loadUrl(this.url);
            // 历史更新时间
            String hisData = PreferenceHelper.readString(getActivity(), Contants.UPDATE_TIME, Contants.CURRENT_DATA,
                    "");
            // 当前时间
            String currentData = SystemTool.getDataTime("yyyy-MM-dd");

            if (hisData != null && !currentData.equals(hisData)) {
                progressDialog = ProgressDialog.show(getActivity(), "加载数据", "正在初始化加载数据,请稍等", true, false);
                task = new ActsAllDataTask().execute();
            }
        } else {
            progressDialog = ProgressDialog.show(getActivity(), "加载数据", "正在初始化加载数据,请稍等", true, false);
            task = new ActsAllDataTask().execute();
        }
    }

    public void loadUrl(final String url) {
        customWebView.post(new Runnable() {
            @Override
            public void run() {
                customWebView.loadUrl(url);
            }
        });
    }

    @Override
    public void setTitle(String title) {
        // navLineLayout.setTitle(title.split("_")[0]);
    }

    @Override
    public void show() {
        // navLineLayout.show();
    }

    @Override
    public void hide() {
        // navLineLayout.hide();
    }

    @Override
    public void back() {
        customWebView.goBack();
    }

    @Override
    public void startFullActivity(GameInfo gameInfo) {

    }

    public void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void Horizontal() {

    }

    @Override
    public void networkSet() {
        CustomUtils.networkSet(getActivity());
    }

    @Override
    public void reload() {
        customWebView.post(new Runnable() {
            @Override
            public void run() {
                customWebView.reload();
            }
        });
        getUserInfo();
    }

    @Override
    public void setUrl(String url) {
        if (!url.contains("no-wifi")) {
            this.url = url;
        }
    }

    @Override
    public void showWheelView() {

    }

    private class ActsAllDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            return ServiceInterface.getAllData(getActivity());
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                super.onPostExecute(result);

                swipeLayout.setRefreshing(false);
                reload();

                if (result != null) {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                        File fileDir = new File(Contants.ALL_DATA_DIR_PATH);
                        if (!fileDir.exists()) {
                            fileDir.mkdirs();
                        }
                        File file = new File(Contants.ALL_DATA_DIR_PATH, Contants.ALL_DATA_FILENAME);
                        if (file.exists()) {
                            file.delete();
                        }
                        // if (!file.exists()) {
                        file.createNewFile();
                        // }
                        try {
                            result = " var data= " + result;// 重新封装
                            FileOutputStream fos = new FileOutputStream(file);
                            fos.write(result.getBytes());
                            fos.close();
                            // Toast.makeText(MainActivity.this, "写入文件成功",
                            // Toast.LENGTH_LONG).show();

                            PreferenceHelper.write(getActivity(), Contants.UPDATE_TIME, Contants.CURRENT_DATA,
                                    SystemTool.getDataTime("yyyy-MM-dd"));

                            // 最后通知更新文件
                            if (file != null) {
                                getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                        Uri.parse("file://" + file.getAbsolutePath())));
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "数据加载失败", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // 此时SDcard不存在或者不能进行读写操作的
                        Toast.makeText(getActivity(), "SD卡不存在或者不能进行读写操作", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "获取数据失败,请稍后重试!", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            loadUrl(url);
            customWebView.loadUrl("javascript:init()");
        }
    }

    private class AddKeepTask extends AsyncTask<Void, Void, MyCreateRet> {
        private String id;
        private String mime;

        public AddKeepTask(String id, String mime) {
            this.id = id;
            this.mime = mime;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected MyCreateRet doInBackground(Void... params) {
            return ServiceInterface.getAddKeepData(getActivity(), id, mime);
        }

        @Override
        protected void onPostExecute(MyCreateRet result) {
            try {
                super.onPostExecute(result);

                if (Result.checkResult(getActivity(), result)) {
                    Toast.makeText(getActivity(), result.message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "操作失败,请稍后重试!", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void createImage(String id, String data, String isVip, String price) {

    }

    @Override
    public void saveImage(String imageRealPath) {

    }

    @Override
    public void addKeep(String id) {
        addKeppTask = new AddKeepTask(id, App.ANDROID_ID).execute();
    }

    @Override
    public void imageShow(String path) {

    }

    @Override
    public void updateVersion() {
        checkVersionTask = new VersionUpdateServiceTask(false).execute();
        KJLoger.debug("CustomWebFragment----updateVersion");
    }

    @Override
    public void selectPic(int xvalue, int yvalue) {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
        // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(pickIntent, REQUESTCODE_PICK);
    }

    @Override
    public void submitMesage(String str, String descriptionStr) {

        qqStr = str;
        description = descriptionStr;

        if (dialog == null) {
            dialog = CustomProgress.create(getActivity(), "正在上传...", true, null);
        }
        dialog.setCanceledOnTouchOutside(false);

        if (isValidContext(getActivity()) && dialog != null) {
            dialog.show();
        }
        // 新线程后台上传服务端
        new Thread(uploadImageRunnable).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUESTCODE_PICK:// 直接从相册获取
                try {
                /*
				 * Bundle extras = data.getExtras(); if (extras != null) { //
				 * 取得SDCard图片路径做显示 Bitmap photo = extras.getParcelable("data");
				 * Drawable drawable = new BitmapDrawable(null, photo); urlpath
				 * = FileUtil.saveFile(getActivity(), "temp.jpg", photo);
				 * 
				 * }
				 */

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null,
                            null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    urlpath = picturePath;
				/*
				 * ImageView imageView = (ImageView) findViewById(R.id.imgView);
				 * imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath
				 * ));
				 */

                    customWebView.loadUrl("javascript:setImg('" + urlpath + "');");

                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;
            case REQUESTCODE_TAKE:// 调用相机拍照

                break;
            case REQUESTCODE_CUTTING:// 取得裁剪后的图片
                if (data != null) {
                    // setPicToView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    Runnable uploadImageRunnable = new Runnable() {
        @Override
        public void run() {

            if (picUploadUrl == null) {
                Toast.makeText(getActivity(), "还没有设置上传的路径！", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, String> textParams = new HashMap<String, String>();
            Map<String, File> fileparams = new HashMap<String, File>();

            try {
                // 创建一个URL对象
                URL url = new URL(picUploadUrl);
                textParams = new HashMap<String, String>();
                fileparams = new HashMap<String, File>();
                textParams.put("connect", qqStr);
                textParams.put("description", description);
                textParams.put("mime", App.ANDROID_ID);

                if (urlpath != null && urlpath.length() > 0) {
                    // 要上传的图片文件
                    File file = new File(urlpath);
                    // fileparams.put("image", file);//gy:hide
                    fileparams.put("img", file);// gy:与接口文档输入参数保持一致
                }

                // 利用HttpURLConnection对象从网络中获取网页数据
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 设置连接超时（记得设置连接超时,如果网络不好,Android系统在超过默认时间会收回资源中断操作）
                conn.setConnectTimeout(5000);
                // 设置允许输出（发送POST请求必须设置允许输出）
                conn.setDoOutput(true);
                // 设置使用POST的方式发送
                conn.setRequestMethod("POST");
                // 设置不使用缓存（容易出现问题）
                conn.setUseCaches(false);
                conn.setRequestProperty("Charset", "UTF-8");// 设置编码
                // 在开始用HttpURLConnection对象的setRequestProperty()设置,就是生成HTML文件头
                conn.setRequestProperty("ser-Agent", "Fiddler");
                // 设置contentType
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + NetUtil.BOUNDARY);
                OutputStream os = conn.getOutputStream();
                DataOutputStream ds = new DataOutputStream(os);
                NetUtil.writeStringParams(textParams, ds);
                NetUtil.writeFileParams(fileparams, ds);
                NetUtil.paramsEnd(ds);
                // 对文件流操作完,要记得及时关闭
                os.close();
                // 服务器返回的响应吗
                int code = conn.getResponseCode(); // 从Internet获取网页,发送请求,将网页以流的形式读回来
                // 对响应码进行判断
                if (code == 200) {// 返回的响应码200,是成功
                    // 得到网络返回的输入流
                    InputStream is = conn.getInputStream();
                    resultStr = NetUtil.readString(is);
                    // Toast.makeText(getActivity(), "上传成功",
                    // Toast.LENGTH_SHORT);
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);

                } else {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
                if (isValidContext(getActivity()) && dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private String downUrl = "";

    private String newVersionName = "";

    private String versionRemark = "";

    private int isForce = 0;//是否强制更新

    /**
     * 版本检测更新
     */
    private class VersionUpdateServiceTask extends AsyncTask<Void, Void, VersionUpdateServiceRet> {
        boolean isAutoUpdate = true;

        public VersionUpdateServiceTask(boolean isAutoUpdate) {
            this.isAutoUpdate = isAutoUpdate;
        }

        private onAlertDialogBtnClickListener clickListener = new onAlertDialogBtnClickListener() {

            @Override
            public void onOk() {
                customWidgets.hideAlertDialog();

                if (downUrl != null && downUrl.length() > 0) {
                    Intent intent = new Intent(getActivity(), UpdateService.class);
                    intent.putExtra(Contants.COMMON_URL, downUrl);
                    getActivity().startService(intent);
                } else {
                    Message message = new Message();
                    message.what = 5;
                    handler.sendMessage(message);
                    return;
                }
            }

            @Override
            public void onCancle() {
                customWidgets.hideAlertDialog();
                if (isForce == 1) {
                    ((Main5Activity) getActivity()).finish();
                }
            }
        };

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected VersionUpdateServiceRet doInBackground(Void... params) {
            try {
                return ServiceInterface.VersionUpdateService(getActivity(), getMetaDataValue("UMENG_CHANNEL"));
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(VersionUpdateServiceRet result) {
            super.onPostExecute(result);
            if (result != null) {
                if (result.errCode.equals(StatusCode.STATUS_CODE_SUCCESS)) {
                    if (result.data.version == null) {
                        if (!isAutoUpdate) {
                            AlertUtil.show(getActivity(), "没有版本信息!");
                        }
                        return;
                    }

                    if (result.data.version.equals(CommUtils.getVersionName(getActivity()))) {
                        if (!isAutoUpdate && isResumed()) {
                            customWidgets.showInfoDialog("检查更新",
                                    "当前已是最新版本！\n版本号：V" + CommUtils.getVersionName(getActivity()));
                        }
                    } else {
                        if (result.data.versionCode != null && Integer.parseInt(result.data.versionCode) > CommUtils.getVersionCode(getActivity())) {
                            if (!CommUtils.checkSdCardExist()) {
                                if (!isAutoUpdate) {
                                    AlertUtil.show(getActivity(), "sd卡无法使用或不存在！请插入sd卡。");
                                }
                            } else {
                                downUrl = result.data.download;
                                newVersionName = result.data.version;
                                versionRemark = result.data.updateLog;
                                isForce = result.data.isForce;

                                if (versionRemark != null) {
                                    versionRemark = versionRemark.replace(",", "\n");
                                }

                                String hisCheckDate = PreferenceHelper.readString(getContext(), Contants.VERSION_CHECK,
                                        Contants.VERSION_CHECK_DATA, "");
                                String currentData = SystemTool.getDataTime("yyyy-MM-dd");

                                if ((hisCheckDate != null && !currentData.equals(hisCheckDate)) || isForce == 1) {
                                    PreferenceHelper.write(getContext(), Contants.VERSION_CHECK, Contants.VERSION_CHECK_DATA,
                                            SystemTool.getDataTime("yyyy-MM-dd"));
                                    showUpdateDialog();
                                }
                            }
                        }
                    }
                } else {
                    Logger.e("获取版本信息失败" + result.errCode);
                }
            } else {
                AlertUtil.show(getActivity(), getActivity().getResources().getString(R.string.net_connect_error));
            }
        }

        private void showUpdateDialog() {
            if (isResumed()) {
                customWidgets.showAlertDialog(isForce, "检查更新", "当前版本号：V" + CommUtils.getVersionName(getActivity()) + "\n最新版本号：V"
                        + newVersionName + "\n版本信息：\n" + versionRemark + "\n是否下载并安装新版本？", clickListener);
            }
        }
    }

    /**
     * 获取所有装逼类数据
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
            return ServiceInterface.getAllFightData(getActivity());
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                super.onPostExecute(result);

                swipeLayout.setRefreshing(false);
                reload();

                if (result != null) {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                        File fileDir = new File(Contants.ALL_DATA_DIR_PATH);
                        if (!fileDir.exists()) {
                            fileDir.mkdirs();
                        }
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
                                getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "数据加载失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // 此时SDcard不存在或者不能进行读写操作的
                        Toast.makeText(getActivity(), "数据加载失败", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "数据加载失败", Toast.LENGTH_SHORT).show();
                }
                //customWebView.loadUrl("javascript:initData()");
                customWebView.loadUrl("javascript:init()");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void clearCache() {
        deleteFilesByDirectory(new File(Contants.BASE_IMAGE_DIR));
        deleteFilesByDirectory(new File(Contants.BASE_NORMAL_FILE_DIR));
        deleteFilesByDirectory(new File(Contants.ALL_SMALL_IMAGE_PATH));
        clearCacheFolder(getActivity().getCacheDir(), System.currentTimeMillis());
        Message message = new Message();
        message.what = 2;
        handler.sendMessage(message);
    }

    public void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    public int clearCacheFolder(File dir, long numDays) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, numDays);
                    }
                    if (child.lastModified() < numDays) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    @Override
    public void toSave() {
    }

    @Override
    public void toShare() {
    }

    @Override
    public void initWithUrl(String url) {
        getUserInfo();
    }

    private String getMetaDataValue(String name, String def) {

        String value = getMetaDataValue(name);
        return (value == null) ? def : value;
    }

    private String getMetaDataValue(String name) {

        Object value = null;

        PackageManager packageManager = getActivity().getPackageManager();

        ApplicationInfo applicationInfo;

        try {
            applicationInfo = packageManager.getApplicationInfo(getActivity().getPackageName(), MVALUE);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                value = applicationInfo.metaData.get(name);
            }
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Could not read the name in the manifest file.", e);
        }

        if (value == null) {
            throw new RuntimeException("The name '" + name + "' is not defined in the manifest file's meta data.");
        }

        return value.toString();
    }

    /**
     * 修改用户名称
     */
    @Override
    public void updateUserName(String userName) {
        KJHttp kjh = new KJHttp();
        kjh.cleanCache();
        HttpParams params = new HttpParams();
        if (userName != null && userName.length() > 0) {
            params.put("username", userName);
            params.put("mime", App.ANDROID_ID);
            kjh.post(Server.URL_UPDATE_USER, params, new HttpCallBack() {
                @Override
                public void onSuccess(Map<String, String> headers, byte[] t) {
                    super.onSuccess(headers, t);
                    // 获取cookie
                    KJLoger.debug("===" + headers.get("Set-Cookie"));
                    if (t != null && t.length > 0) {
                        String result = new String(t);
                        UserRet userRet = Contants.gson.fromJson(result, new TypeToken<UserRet>() {
                        }.getType());
                        if (Result.checkResult(getActivity(), userRet)) {
                            Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
							/*if (userRet.data != null && userRet.data.name != null) {
								customWebView.loadUrl("javascript:setMemi('" + userRet.data.name + "')");
							}*/
                        }
                    } else {
                        Toast.makeText(getActivity(), "修改用户名失败", Toast.LENGTH_SHORT).show();
                    }
                    //((MainActivity) getActivity()).isShow = false;
                }
            });
        } else {
            Toast.makeText(getActivity(), "修改用户名失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取用户信息
     */
    public void getUserInfo() {

        tempName = PreferenceHelper.readString(getActivity(), Contants.USER_NAME, Contants.USER_INFO_NAME, "");
        if (tempName != null && tempName.length() > 0) {
            customWebView.loadUrl("javascript:setMemi('" + tempName + "')");
        } else {
            customWebView.loadUrl("javascript:setMemi('" + App.ANDROID_ID + "')");
        }

        KJHttp kjh = new KJHttp();
        kjh.cleanCache();
        HttpParams params = new HttpParams();
        params.put("mime", App.ANDROID_ID);
        kjh.post(Server.URL_GET_USER_MESSAGE, params, new HttpCallBack() {
            @Override
            public void onSuccess(Map<String, String> headers, byte[] bt) {
                super.onSuccess(headers, bt);
                try {
                    // 获取cookie
                    KJLoger.debug("===" + headers.get("Set-Cookie"));
                    // 默认用户名
                    String tempUserName = App.ANDROID_ID;
                    if (bt != null && bt.length > 0) {
                        String result = new String(bt);
                        UserRet userRet = Contants.gson.fromJson(result, new TypeToken<UserRet>() {
                        }.getType());
                        if (Result.checkResult(getActivity(), userRet)) {
							/*if (userRet.data != null && userRet.data.name != null && userRet.data.name.length() >0 && !tempName.equals(userRet.data.name)) {
								tempUserName = userRet.data.name;
								PreferenceHelper.write(getActivity(), Contants.USER_NAME, Contants.USER_INFO_NAME,
										tempUserName);
								customWebView.loadUrl("javascript:setMemi('" + tempUserName + "')");
							}*/
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                //customWebView.loadUrl("javascript:setMemi('" + App.ANDROID_ID + "')");
            }
        });
    }

    @Override
    public void setShowState(boolean flag) {
        this.isShow = flag;
    }

    @Override
    public void loadImageList(int currentPage) {
        this.currentPage = currentPage;
        // cacheImage();
    }

    @Override
    public void search(String keyword) {
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
    public void photoGraph() {

    }

    @Override
    public void fightMenu() {
        Intent intent = new Intent(getActivity(), FightMenuActivity.class);
        getActivity().startActivity(intent);
    }

    @Override
    public void onRefresh() {
        new FightAllDataTask().execute();
    }

    @Override
    public void gifResult(String url) {

    }
}
