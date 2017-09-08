package com.fy.tnzbsq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.adapter.ZBDataAdapter;
import com.fy.tnzbsq.adapter.ZBTypeAdapter;
import com.fy.tnzbsq.bean.SlideInfo;
import com.fy.tnzbsq.bean.ZBDataListRet;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.net.OKHttpRequest;
import com.fy.tnzbsq.net.listener.OnResponseListener;
import com.fy.tnzbsq.util.CommUtils;
import com.fy.tnzbsq.util.StringUtils;
import com.fy.tnzbsq.view.BannerImageLoader;
import com.orhanobut.logger.Logger;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 装逼图
 * Created by admin on 2017/8/24.
 */

public class ZBFragment extends CustomBaseFragment implements SwipeRefreshLayout.OnRefreshListener,MainActivity.CurrentTabIndex {

    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeLayout;

    @BindView(R.id.loading_layout)
    LinearLayout mLoadingLayout;

    private Banner mBanner;

    private RecyclerView mZbTypeView;

    @BindView(R.id.main_data_list)
    RecyclerView mDataListView;

    ZBTypeAdapter zbTypeAdapter;

    ZBDataAdapter zbDataAdapter;

    OKHttpRequest okHttpRequest = null;

    private int currentPage = 1;

    List<SlideInfo> mSlideInfoList;

    private MainActivity.CurrentTabIndex currentTabIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.zb_fragment, null);
        ButterKnife.bind(this, mRootView);
        init();
        loadData();
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
    }

    private View getHeaderView() {
        zbTypeAdapter = new ZBTypeAdapter(null);
        View headView = getActivity().getLayoutInflater().inflate(R.layout.zb_fragment_head_view, (ViewGroup) mDataListView.getParent(), false);
        mZbTypeView = ButterKnife.findById(headView, R.id.zb_type_list);
        mBanner = ButterKnife.findById(headView, R.id.banner);
        mZbTypeView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mZbTypeView.setAdapter(zbTypeAdapter);
        return headView;
    }
    @Override
    public void currentSelect(int index) {

    }
    public void init() {
        currentTabIndex = this;
        ((MainActivity)getActivity()).setCurrentTabIndex(currentTabIndex);
        swipeLayout.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_light);
        swipeLayout.setOnRefreshListener(this);

        okHttpRequest = new OKHttpRequest();

        zbDataAdapter = new ZBDataAdapter(getActivity(), null);
        mDataListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        zbDataAdapter.addHeaderView(getHeaderView());
        mDataListView.setAdapter(zbDataAdapter);

        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                if (mSlideInfoList != null && mSlideInfoList.get(position) != null) {
                    intent.putExtra("banner_id", mSlideInfoList.get(position).id);
                }
                startActivity(intent);
            }
        });

        zbTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                if (zbTypeAdapter.getData() != null) {
                    intent.putExtra("type_id", zbTypeAdapter.getData().get(position).id);
                }
                startActivity(intent);
            }
        });

        zbDataAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Logger.e("position --->" + position);
                Intent intent = new Intent(getActivity(), CreateBeforeActivity.class);
                intent.putExtra("zb_data_info", zbDataAdapter.getData().get(position));
                startActivity(intent);
            }
        });

        zbDataAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadData();
            }
        }, mDataListView);
    }

    public void loadData() {
        final Map<String, String> params = new HashMap<String, String>();
        Logger.e("current page---" + currentPage);
        params.put("version", CommUtils.getVersionName(getActivity()));
        params.put("page", currentPage + "");
        okHttpRequest.aget(Server.URL_ALL_DATA, params, new OnResponseListener() {
            @Override
            public void onSuccess(String response) {
                swipeLayout.setRefreshing(false);
                if (!StringUtils.isEmpty(response)) {
                    ZBDataListRet result = Contants.gson.fromJson(response, ZBDataListRet.class);
                    if (result != null) {
                        Logger.e("data size--->" + result.data.size());
                        mLoadingLayout.setVisibility(View.GONE);
                        zbTypeAdapter.setNewData(result.channel);

                        if (currentPage == 1) {
                            zbDataAdapter.setNewData(result.data);
                        } else {
                            zbDataAdapter.addData(result.data);
                        }

                        //设置banner图
                        if (result.banner != null && currentPage == 1) {
                            mSlideInfoList = result.banner;
                            List<String> imgUrls = new ArrayList<String>();
                            for (int i = 0; i < result.banner.size(); i++) {
                                imgUrls.add(result.banner.get(i).c_img);
                            }

                            mBanner.isAutoPlay(true)
                                    .setDelayTime(3000)
                                    .setImageLoader(new BannerImageLoader())
                                    .setImages(imgUrls)
                                    .start();
                        }

                        if (result.data.size() == 20) {
                            currentPage++;
                            zbDataAdapter.loadMoreComplete();
                        } else {
                            zbDataAdapter.loadMoreEnd();
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
                swipeLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        mSlideInfoList.clear();
        loadData();
    }

}
