package com.fy.tnzbsq.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.StringUtils;
import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.net.OKHttpRequest;
import com.fy.tnzbsq.net.listener.OnResponseListener;
import com.just.agentweb.AgentWeb;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by admin on 2017/12/28.
 */

public class AdActivity extends BaseAppActivity {

    @BindView(R.id.toolbarContainer)
    Toolbar mToolbar;

    @BindView(R.id.layout_ad)
    LinearLayout mAdLayout;

    AgentWeb mAgentWeb;

    OKHttpRequest okHttpRequest = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ad;
    }

    @Override
    protected void initVars() {
        mToolbar.setTitle("幸运大抽奖");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.back_icon);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initViews() {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mAdLayout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(StringUtils.isEmpty(App.adUrl)?"http://zs.qqtn.com/":App.adUrl);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        okHttpRequest = new OKHttpRequest();

        final Map<String, String> params = new HashMap<String, String>();
        params.put("aid", StringUtils.isEmpty(App.aid)?"":App.aid);
        okHttpRequest.aget(Server.TOTAL_AD_URL, params, new OnResponseListener() {
            @Override
            public void onSuccess(String response) {
                Logger.i("点击了广告");
            }

            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onBefore() {
            }
        });

    }
}
