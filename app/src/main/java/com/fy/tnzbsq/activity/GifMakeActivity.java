package com.fy.tnzbsq.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.GameInfo;
import com.fy.tnzbsq.common.CustomWebViewDelegate;
import com.fy.tnzbsq.util.Utils;
import com.fy.tnzbsq.view.CustomWebView;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;

import butterknife.BindView;


public class GifMakeActivity extends BaseAppActivity implements CustomWebViewDelegate {

    @BindView(R.id.toolbarContainer)
    Toolbar mToolbar;

    @BindView(R.id.webview)
    CustomWebView customWebView;

    ProgressDialog dialog;

    private String title = "热门Gif图制作";

    private String url = "https://soy.qqtn.com/sorry/";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gif_make;
    }

    @Override
    protected void initVars() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null && bundle.getString("title") != null) {
            title = bundle.getString("title");
        }
        mToolbar.setTitle(title);

        if (bundle != null && bundle.getString("url") != null) {
            url = bundle.getString("url");
        }

        StatusBarUtil.setColor(GifMakeActivity.this, getResources().getColor(R.color.colorAccent), 0);

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.back_icon);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        customWebView.delegate = this;
        loadUrl(url);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Gif图制作");
        dialog.setMessage("加载中···");
        dialog.show();
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
    protected void initViews() {
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
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
    public void setTitle(String title) {

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

    }

    @Override
    public void showWheelView() {

    }

    @Override
    public void createImage(String id, String data, String isVip, String price) {

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

    }

    @Override
    public void photoGraph() {

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
    public void initWithUrl(String url) {
        if(Utils.isValidContext(this) && dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    public void updateUserName(String userName) {

    }

    @Override
    public void setShowState(boolean flag) {

    }

    @Override
    public void loadImageList(int currentPage) {

    }

    @Override
    public void search(String keyword) {

    }

    @Override
    public void fightMenu() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(Utils.isValidContext(this) && dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    public void gifResult(String url) {
        Logger.i("gif --->" + url);
        url = "https://soy.qqtn.com" + url;
        Logger.i("gif result--->" + url);
        Intent intent = new Intent(GifMakeActivity.this, GifResultActivity.class);
        intent.putExtra("imagePath", url);
        intent.putExtra("title",title);
        startActivity(intent);
    }
}
