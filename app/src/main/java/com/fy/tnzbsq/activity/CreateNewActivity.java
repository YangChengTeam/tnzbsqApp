package com.fy.tnzbsq.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.GameInfo;
import com.fy.tnzbsq.bean.ImageCreateRet;
import com.fy.tnzbsq.bean.Result;
import com.fy.tnzbsq.bean.User;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.CustomWebViewDelegate;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.common.ServiceInterface;
import com.fy.tnzbsq.util.HeadImageUtils;
import com.fy.tnzbsq.util.PreferencesUtils;
import com.fy.tnzbsq.util.StringUtils;
import com.fy.tnzbsq.view.ChargeDialog;
import com.fy.tnzbsq.view.CustomProgress;
import com.fy.tnzbsq.view.CustomWebView;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.utils.Log;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Map;

public class CreateNewActivity extends BaseActivity implements CustomWebViewDelegate, ActivityCompat.OnRequestPermissionsResultCallback {

    public static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1000;

    @BindView(id = R.id.back_img, click = true)
    private ImageView backImg;

    @BindView(id = R.id.top_title)
    private TextView titleNameTv;

    @BindView(id = R.id.webview)
    private CustomWebView customWebView;

    public String url = "";

    private AsyncTask<?, ?, ?> taskCreate;

    private String id = "";

    private CustomProgress dialog;

    private boolean isAllow = false;

    private int xcrop;

    private int ycrop;

    private String path = "";

    private boolean isBuy = false;

    @Override
    public void setRootView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.create_new);
    }

    @Override
    public void initWidget() {
        super.initWidget();
    }

    @Override
    public void initData() {
        super.initData();
        //HeadImageUtils.imgPath = null;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null && bundle.getString("id") != null && bundle.getString("id").length() > 0) {
            id = bundle.getString("id");
        }

        customWebView.delegate = this;
        customWebView.loadUrl("file:///android_asset/scq1.html");

        User user = (User) PreferencesUtils.getObject(context, "login_user", User.class);
        App.loginUser = user;
    }

    public void getUserIsBuy() {

        KJHttp kjh = new KJHttp();
        kjh.cleanCache();
        HttpParams params = new HttpParams();
        params.put("goods_id", id);
        params.put("user_id", App.loginUser.id);

        kjh.post(Server.URL_USER_SOURCE_STATE, params, new HttpCallBack() {
            @Override
            public void onSuccess(Map<String, String> headers, byte[] bt) {
                super.onSuccess(headers, bt);

                if (bt != null && bt.length > 0) {
                    String resultValue = new String(bt);
                    if (resultValue != null) {
                        try {
                            Result result = Contants.gson.fromJson(resultValue, new TypeToken<Result>() {}.getType());
                            if (result != null && result.errCode.equals("0")) {
                                isBuy = true;
                                return;
                            }else{

                                String sourceIdsKey = App.loginUser != null ? App.loginUser.id + "_ids" : App.ANDROID_ID + "_ids";
                                String sourceIds = PreferencesUtils.getString(context, sourceIdsKey);
                                if(!StringUtils.isEmpty(sourceIds)){
                                    String[] sourceArray = sourceIds.split(",");
                                    if(isBuyVip(sourceArray,id)){
                                        isBuy = true;
                                        return;
                                    }
                                }
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } finally {
                        }
                    }
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
            }
        });
    }

    public static boolean isBuyVip(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }

    public void initWithUrl(String url) {
        customWebView.loadUrl("javascript:init('" + id + "');");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

        if(App.loginUser != null){
            getUserIsBuy();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.back_img:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void Horizontal() {

    }

    @Override
    public void networkSet() {

    }

    @Override
    public void reload() {

    }

    @Override
    public void setTitle(final String title) {
        titleNameTv.post(new Runnable() {
            @Override
            public void run() {
                titleNameTv.setText(title);
            }
        });
    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    @Override
    public void startFullActivity(GameInfo gameInfo) {

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


    /**
     * 生成图片
     *
     * @param id
     * @param 'mime'
     * @param data
     * @param 'imgPath'
     */
    @Override
    public void createImage(String id, String data, String isVip, String price) {

        boolean isAuth = false;

        if(isVip.equals("0")){
            isAuth = true;
        }else{
            if (App.loginUser == null) {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                return;
            } else {
                if (App.loginUser.is_vip == 1) {
                    isAuth = true;
                }else{
                    if(isBuy){
                        isAuth = true;
                    }
                }
            }
        }

        if(!isAuth){
            ChargeDialog dialog = new ChargeDialog(CreateNewActivity.this,id);
            dialog.showChargeDialog(dialog);
            return;
        }

        if (dialog == null) {
            dialog = CustomProgress.create(context, "正在生成图片...", true, null);
        }

        dialog.show();

        KJHttp kjh = new KJHttp();
        //清除参数及数据的缓存
        kjh.cleanCache();
        HttpParams params = new HttpParams();
        params.put("id", id);
        params.put("mime", App.ANDROID_ID);

        if (data != null && data.length() > 0) {
            params.put("requestData", data);
        }

        if (HeadImageUtils.imgResultPath != null && HeadImageUtils.imgResultPath.length() > 0) {
            Log.e("upload-path", "upload-path====" + HeadImageUtils.imgResultPath);
            //Toast.makeText(context, "path==" + HeadImageUtils.imgResultPath, Toast.LENGTH_SHORT).show();
            params.put("img", new File(HeadImageUtils.imgResultPath));
        }

        params.putHeaders("Cookie", "cookie_tnzbsq");
        kjh.post(Server.URL_IMAGE_CREATE, params, new HttpCallBack() {
            @Override
            public void onSuccess(Map<String, String> headers, byte[] t) {
                super.onSuccess(t);
                if (isValidContext(context) && dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (t != null && t.length > 0) {
                    String result = new String(t);
                    if (result != null) {
                        try {
                            ImageCreateRet res = Contants.gson.fromJson(result, new TypeToken<ImageCreateRet>() {
                            }.getType());
                            if (Result.checkResult(CreateNewActivity.this, res)) {
                                Bundle bundle = new Bundle();
                                bundle.putString("imagePath", res.data);
                                bundle.putString("createTitle", titleNameTv.getText().toString());
                                showActivity(CreateNewActivity.this, ResultActivity.class, bundle);
                            } else {
                                Toast.makeText(CreateNewActivity.this, "生成失败,请稍后重试!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(CreateNewActivity.this, "生成失败,请稍后重试!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                Log.e("errorNo--" + errorNo, "strMsg---" + strMsg);

                Toast.makeText(CreateNewActivity.this, "生成失败,请稍后重试!", Toast.LENGTH_SHORT).show();
                if (isValidContext(context) && dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }

    private class CreateTask extends AsyncTask<Void, Void, ImageCreateRet> {
        private String id;
        private String mime;
        private String requestData;

        public CreateTask(String id, String mime, String requestData) {
            this.id = id;
            this.mime = mime;
            this.requestData = requestData;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (isValidContext(context) && dialog != null) {
                dialog.show();
            }
        }

        @Override
        protected ImageCreateRet doInBackground(Void... params) {
            return ServiceInterface.ImageCreateService(context, id, mime, requestData);
        }

        @Override
        protected void onPostExecute(ImageCreateRet result) {
            if (isValidContext(context) && dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            try {
                super.onPostExecute(result);

                if (Result.checkResult(CreateNewActivity.this, result)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("imagePath", result.data);
                    bundle.putString("createTitle", titleNameTv.getText().toString());
                    showActivity(CreateNewActivity.this, ResultActivity.class, bundle);
                } else {
                    Toast.makeText(CreateNewActivity.this, "生成失败,请稍后重试!", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveImage(String imageRealPath) {
    }

    @Override
    public void addKeep(String id) {

    }

    @Override
    public void imageShow(String path) {
    }

    @Override
    public void updateVersion() {
    }

    @Override
    public void selectPic(int xvalue, int yvalue) {
        //HeadImageUtils.getFromLocation(CreateNewActivity.this);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            if (xvalue > 0 && yvalue > 0) {
                xcrop = xvalue;
                ycrop = yvalue;
            }
            HeadImageUtils.cutPhoto = null;
            HeadImageUtils.getFromLocation(CreateNewActivity.this);
        } else {
            Toast.makeText(context, "未检测到SD卡，请稍后重试", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void submitMesage(String str, String description) {
    }

    @Override
    public void clearCache() {
    }

    @Override
    public void toSave() {
    }

    @Override
    public void toShare() {
    }

    @Override
    public void updateUserName(String userName) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setShowState(boolean flag) {
        // TODO Auto-generated method stub

    }

    @Override
    public void loadImageList(int currentPage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void search(String keyword) {
        // TODO Auto-generated method stub

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

    /**
     * 结果处理
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case HeadImageUtils.FROM_CRAMA:
                if (HeadImageUtils.photoCamare != null) {
                    HeadImageUtils.cutCorePhoto(CreateNewActivity.this, HeadImageUtils.photoCamare);
                }
                break;
            case HeadImageUtils.FROM_LOCAL:
                if (resultCode == RESULT_OK && data != null) {

                    /*Uri selectedImage = data.getData();
                    //----获取图片的真实路径
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    if (filePathColumn != null && filePathColumn.length > 0) {
                        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                            HeadImageUtils.imgPath = cursor.getString(columnIndex);
                        }
                    }*/

                    if (data != null) {
                        Uri uri = data.getData();
                        if (!TextUtils.isEmpty(uri.getAuthority())) {
                            Cursor cursor = getContentResolver().query(uri,
                                    new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                            if (null == cursor) {
                                Toast.makeText(this, "图片没找到", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            cursor.moveToFirst();
                            HeadImageUtils.imgPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                            cursor.close();
                        } else {
                            HeadImageUtils.imgPath = uri.getPath();
                        }
                    } else {
                        Toast.makeText(this, "图片没找到", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent intent = new Intent(CreateNewActivity.this, ImageCropActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("xcrop", xcrop);
                    bundle.putInt("ycrop", ycrop);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, HeadImageUtils.FREE_CUT);
                }
                break;
            case HeadImageUtils.FREE_CUT:
                if (HeadImageUtils.cutPhoto != null) {
                    //Toast.makeText(this, "1111111", Toast.LENGTH_SHORT).show();
                    //----获取图片的真实路径
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    if (filePathColumn != null && filePathColumn.length > 0) {
                        Cursor cursor = getContentResolver().query(HeadImageUtils.cutPhoto, filePathColumn, null, null, null);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            path = cursor.getString(columnIndex);
                            HeadImageUtils.imgResultPath = path;
                        }
                    }
                } else if (HeadImageUtils.cropBitmap != null) {

                    //String tempType = HeadImageUtils.imgResultPath.substring(HeadImageUtils.imgResultPath.lastIndexOf(".") + 1, HeadImageUtils.imgResultPath.length());
                    String fileName = Contants.BASE_NORMAL_FILE_DIR + File.separator + System.currentTimeMillis()
                            + (int) (Math.random() * 10000) + ".jpg";


                    File fileDir = new File(Contants.BASE_NORMAL_FILE_DIR);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }

                    File tempfile = new File(fileName);

                    try {
                        if (!tempfile.exists()) {
                            tempfile.createNewFile();
                            FileOutputStream fos = new FileOutputStream(tempfile);
                            HeadImageUtils.cropBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.flush();
                            fos.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    HeadImageUtils.imgResultPath = tempfile.getAbsolutePath();
                    path = tempfile.getAbsolutePath();
                } else {
                    Toast.makeText(this, "图片裁剪失败", Toast.LENGTH_SHORT).show();
                    return;
                }

                customWebView.loadUrl("javascript:setImg('" + path + "');");
                break;
            case HeadImageUtils.FROM_CUT:
                // 选择图像之后，修改用户的图像
                if (HeadImageUtils.cutPhoto != null) {
                    if (HeadImageUtils.imgPath != null && HeadImageUtils.imgPath.length() > 0) {
                        customWebView.loadUrl("javascript:setImg('" + HeadImageUtils.imgPath + "');");
                        //updateUserInfo("", "", HeadImageUtils.imgPath);
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void fightMenu() {
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        HeadImageUtils.imgPath = null;
        HeadImageUtils.imgResultPath = null;
    }
}
