package com.fy.tnzbsq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.adapter.ZBDataAdapter;
import com.fy.tnzbsq.bean.ZBDataListRet;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.net.OKHttpRequest;
import com.fy.tnzbsq.net.listener.OnResponseListener;
import com.fy.tnzbsq.util.StringUtils;
import com.jaeger.library.StatusBarUtil;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.pay.other.ToastUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import pl.droidsonroids.gif.GifImageView;
import rx.functions.Action1;

/**
 * 首页-分类、Banner页
 */
public class SearchResult1Activity extends BaseAppActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.title_left_iv)
    ImageView backImg;

    @BindView(R.id.search_key)
    EditText searchKeyEv;

    @BindView(R.id.search_btn)
    Button mSearchBtn;

    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeLayout;

    @BindView(R.id.loading_layout)
    LinearLayout mLoadingLayout;

    @BindView(R.id.loading_iv)
    GifImageView gifLoading;

    @BindView(R.id.iv_no_date)
    ImageView noDataImageView;

    @BindView(R.id.category_list)
    RecyclerView mCategoryRecyclerView;

    ZBDataAdapter mZbDataAdapter;

    OKHttpRequest okHttpRequest = null;

    private int currentPage = 1;

    private String searchKey = "";

    @Override
    protected int getLayoutId() {
        return R.layout.search_result1;
    }

    @Override
    protected void initVars() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null && !StringUtils.isEmpty(bundle.getString("searchKey"))) {
            searchKey = bundle.getString("searchKey");
        }

        if (!StringUtils.isEmpty(searchKey)) {
            searchKeyEv.setText(searchKey);
            searchKeyEv.setSelection(searchKey.length());
        }

        swipeLayout.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_light);
        swipeLayout.setOnRefreshListener(this);

        okHttpRequest = new OKHttpRequest();
        StatusBarUtil.setColor(SearchResult1Activity.this, getResources().getColor(R.color.colorAccent), 0);

        mZbDataAdapter = new ZBDataAdapter(SearchResult1Activity.this, null);
        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCategoryRecyclerView.setAdapter(mZbDataAdapter);

        mZbDataAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(SearchResult1Activity.this, CreateBeforeActivity.class);
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

        RxView.clicks(backImg).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                SearchResult1Activity.this.finish();
            }
        });

        //搜索
        RxView.clicks(mSearchBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (StringUtils.isEmpty(searchKeyEv.getText())) {
                    ToastUtil.toast(SearchResult1Activity.this, "请输入关键词搜索");
                    return;
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchKeyEv.getWindowToken(), 0);

                searchKey = searchKeyEv.getText().toString();
                currentPage = 1;
                loadListData();
            }
        });
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

        if (!StringUtils.isEmpty(searchKey)) {
            params.put("keyword", searchKey);
        }
        params.put("page", currentPage + "");

        okHttpRequest.aget(Server.URL_SEARCH_DATA, params, new OnResponseListener() {
            @Override
            public void onSuccess(String response) {
                swipeLayout.setRefreshing(false);
                if (!StringUtils.isEmpty(response)) {
                    ZBDataListRet result = Contants.gson.fromJson(response, ZBDataListRet.class);
                    if (result != null && result.data != null && result.data.size() > 0) {
                        mCategoryRecyclerView.setVisibility(View.VISIBLE);
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
                    } else {
                        mCategoryRecyclerView.setVisibility(View.GONE);
                        mLoadingLayout.setVisibility(View.VISIBLE);
                        gifLoading.setVisibility(View.GONE);
                        noDataImageView.setVisibility(View.VISIBLE);
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
