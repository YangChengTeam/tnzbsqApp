package com.fy.tnzbsq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.jaeger.library.StatusBarUtil;
import com.kk.pay.other.ToastUtil;

import org.kymjs.kjframe.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 首页-分类、Banner页
 */
public class CategoryActivity extends BaseAppActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeLayout;

    @BindView(R.id.loading_layout)
    LinearLayout mLoadingLayout;

    @BindView(R.id.toolbarContainer)
    Toolbar mToolbar;

    @BindView(R.id.category_list)
    RecyclerView mCategoryRecyclerView;

    ZBDataAdapter mZbDataAdapter;

    OKHttpRequest okHttpRequest = null;

    private int currentPage = 1;

    private String typeId;

    private String bannerId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_categoty;
    }

    @Override
    protected void initVars() {

        swipeLayout.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_light);
        swipeLayout.setOnRefreshListener(this);

        okHttpRequest = new OKHttpRequest();
        StatusBarUtil.setColor(CategoryActivity.this, getResources().getColor(R.color.colorAccent), 0);
        Bundle bundle = getIntent().getExtras();
        typeId = bundle != null ? bundle.getString("type_id") : "";
        bannerId = bundle != null ? bundle.getString("banner_id") : "";

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
                Intent intent = new Intent(CategoryActivity.this, CreateBeforeActivity.class);
                intent.putExtra("zb_data_info", mZbDataAdapter.getData().get(position));
                startActivity(intent);
            }
        });

        mZbDataAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadListData();
            }
        }, mCategoryRecyclerView);
    }


    @Override
    protected void initViews() {
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        loadListData();
    }

    public void loadListData() {
        final Map<String, String> params = new HashMap<String, String>();

        String dataUrl = "";
        if (typeId != null && bannerId == null) {
            dataUrl = Server.CATEGORY_LIST_URL;
            params.put("id", typeId);
        }
        if (typeId == null && bannerId != null) {
            params.put("id", bannerId);
            dataUrl = Server.BANNER_LIST_URL;
        }
        params.put("page", currentPage + "");
        if (!StringUtils.isEmpty(dataUrl)) {
            okHttpRequest.aget(dataUrl, params, new OnResponseListener() {
                @Override
                public void onSuccess(String response) {
                    swipeLayout.setRefreshing(false);
                    if (!StringUtils.isEmpty(response)) {
                        ZBDataListRet result = Contants.gson.fromJson(response, ZBDataListRet.class);
                        if (result != null) {
                            if (currentPage == 1) {
                                mLoadingLayout.setVisibility(View.GONE);
                                mZbDataAdapter.setNewData(result.data);
                            } else {
                                mZbDataAdapter.addData(result.data);
                            }

                            if (result.data.size() == 20) {
                                currentPage++;
                                mZbDataAdapter.loadMoreComplete();
                            } else {
                                mZbDataAdapter.loadMoreEnd();
                            }
                        }
                    }
                }

                @Override
                public void onError(Exception e) {
                    swipeLayout.setRefreshing(false);
                }

                @Override
                public void onBefore() {
                }
            });
        } else {
            ToastUtil.toast(this, "数据异常");
        }
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        loadListData();
    }
}
