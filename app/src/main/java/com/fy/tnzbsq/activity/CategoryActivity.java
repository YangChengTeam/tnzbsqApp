package com.fy.tnzbsq.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.adapter.ZBDataAdapter;
import com.fy.tnzbsq.bean.ZBDataListRet;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.net.OKHttpRequest;
import com.fy.tnzbsq.net.listener.OnResponseListener;
import com.fy.tnzbsq.util.CommUtils;
import com.fy.tnzbsq.util.StringUtils;
import com.jaeger.library.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 首页-分类、Banner页
 */
public class CategoryActivity extends BaseAppActivity {

    @BindView(R.id.loading_layout)
    LinearLayout mLoadingLayout;

    @BindView(R.id.toolbarContainer)
    Toolbar mToolbar;

    @BindView(R.id.category_list)
    RecyclerView mCategoryRecyclerView;

    ZBDataAdapter mZbDataAdapter;

    OKHttpRequest okHttpRequest = null;

    private int currentPage = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_categoty;
    }

    @Override
    protected void initVars() {
        okHttpRequest = new OKHttpRequest();

        StatusBarUtil.setColor(CategoryActivity.this, getResources().getColor(R.color.colorAccent), 0);
        Bundle bundle = getIntent().getExtras();
        mToolbar.setTitle(bundle != null ? bundle.getString("title") : "素材列表");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.back_icon);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mZbDataAdapter = new ZBDataAdapter(CategoryActivity.this, null);
        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCategoryRecyclerView.setAdapter(mZbDataAdapter);

        mZbDataAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("version", CommUtils.getVersionName(CategoryActivity.this));
        params.put("page", currentPage + "");
        okHttpRequest.aget(Server.URL_ALL_DATA, params, new OnResponseListener() {
            @Override
            public void onSuccess(String response) {
                if (!StringUtils.isEmpty(response)) {
                    ZBDataListRet result = Contants.gson.fromJson(response, ZBDataListRet.class);
                    if (result != null) {
                        mLoadingLayout.setVisibility(View.GONE);
                        mZbDataAdapter.setNewData(result.data);
                    }
                }
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
