package com.yc.loanbox.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tencent.mmkv.MMKV;
import com.yc.loanbox.model.bean.ProductInfo;
import com.yc.loanbox.view.widget.MyLoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    public MyLoadingDialog dlg = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(getLayoutId());
        ButterKnife.bind(this);
        dlg = new MyLoadingDialog(this);
        setTranslucentStatus();
        initViews();
        initData();
    }

    public void showLoadingDialog(String message) {
        dlg.show(message);

    }

    public void dissmissLoadingDialog(){
        dlg.dismiss();
    }

    protected void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(67108864);
            window.getDecorView().setSystemUiVisibility(1280);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
        } else if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(67108864);
        }
    }

    public void startWebActivity(ProductInfo productInfo){
        if(productInfo == null) return;
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("title", productInfo.getName());
        intent.putExtra("ico", productInfo.getIco());
        intent.putExtra("url", productInfo.getReg_url());
        intent.putExtra("id", productInfo.getId());
        intent.putExtra("ad_id", productInfo.getAd_id());
        intent.putExtra("ptype", productInfo.getPtype());

        startActivity(intent);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<ProductInfo> productInfos;
                String json = MMKV.defaultMMKV().getString("record", "");
                if(!TextUtils.isEmpty(json)){
                    productInfos = JSON.parseObject(json, new TypeReference<List<ProductInfo>>() {
                    }.getType());
                } else {
                    productInfos = new ArrayList<>();
                }

                boolean flag = false;
                for(ProductInfo productInfo1: productInfos){
                    if(productInfo1.getId() != null && productInfo1.getId().equals(productInfo.getId())){
                        flag = true;
                        break;
                    }
                }
                if(!flag && productInfo.getId() != null) {
                    productInfos.add(productInfo);
                }
                MMKV.defaultMMKV().putString("record", JSON.toJSONString
                        (productInfos));
            }
        }).start();
    }

    protected abstract int  getLayoutId();

    protected abstract void initViews();

    protected void initData(){}

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

}
