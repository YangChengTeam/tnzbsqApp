package com.fy.tnzbsq.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.adapter.OrderInfoAdapter;
import com.fy.tnzbsq.bean.OrderInfoListRet;
import com.fy.tnzbsq.bean.User;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.net.OKHttpRequest;
import com.fy.tnzbsq.net.listener.OnResponseListener;
import com.fy.tnzbsq.util.PreferencesUtils;
import com.jaeger.library.StatusBarUtil;

import org.kymjs.kjframe.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import pl.droidsonroids.gif.GifImageView;

/**
 * 订单信息
 */
public class OrderInfoActivity extends BaseAppActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeLayout;

    @BindView(R.id.loading_layout)
    LinearLayout mLoadingLayout;

    @BindView(R.id.loading_iv)
    GifImageView mLoadingGifImageView;

    @BindView(R.id.iv_no_date)
    ImageView mNoDataImageView;

    @BindView(R.id.toolbarContainer)
    Toolbar mToolbar;

    @BindView(R.id.order_list)
    RecyclerView mCategoryRecyclerView;

    OrderInfoAdapter mOrderInfoAdapter;

    OKHttpRequest okHttpRequest = null;

    private int currentPage = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_info;
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
        StatusBarUtil.setColor(OrderInfoActivity.this, getResources().getColor(R.color.colorAccent), 0);
        mToolbar.setTitle("订单记录");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.back_icon);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mOrderInfoAdapter = new OrderInfoAdapter(OrderInfoActivity.this, null);
        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCategoryRecyclerView.setAdapter(mOrderInfoAdapter);
        mOrderInfoAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
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

    @Override
    protected void onResume() {
        super.onResume();
        App.loginUser = (User) PreferencesUtils.getObject(this, "login_user", User.class);
    }

    public void loadListData() {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", App.loginUser != null ? App.loginUser.id + "" : "");
        params.put("page", currentPage + "");
        params.put("page_size", "20");
        okHttpRequest.aget(Server.ORDER_INFO_URL, params, new OnResponseListener() {
            @Override
            public void onSuccess(String response) {
                swipeLayout.setRefreshing(false);
                if (!StringUtils.isEmpty(response)) {
                    OrderInfoListRet result = Contants.gson.fromJson(response, OrderInfoListRet.class);
                    if (result != null && result.data != null && result.data.list != null && result.data.list.size() > 0) {
                        if (currentPage == 1) {
                            mLoadingLayout.setVisibility(View.GONE);
                            mOrderInfoAdapter.setNewData(result.data.list);
                        } else {
                            mOrderInfoAdapter.addData(result.data.list);
                        }

                        if (result.data.list.size() == 20) {
                            currentPage++;
                            mOrderInfoAdapter.loadMoreComplete();
                        } else {
                            mOrderInfoAdapter.loadMoreEnd();
                        }
                    }else{
                        mLoadingLayout.setVisibility(View.VISIBLE);
                        mLoadingGifImageView.setVisibility(View.GONE);
                        mNoDataImageView.setVisibility(View.VISIBLE);
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
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        loadListData();
    }
}
