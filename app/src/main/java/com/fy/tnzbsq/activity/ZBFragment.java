package com.fy.tnzbsq.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fy.tnzbsq.R;
import com.fy.tnzbsq.adapter.ZBDataAdapter;
import com.fy.tnzbsq.bean.ZBDataInfo;
import com.fy.tnzbsq.bean.ZBDataListRet;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.net.OKHttpRequest;
import com.fy.tnzbsq.net.listener.OnResponseListener;
import com.fy.tnzbsq.util.StringUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/8/24.
 */

public class ZBFragment extends CustomBaseFragment {

    @BindView(R.id.loading_layout)
    LinearLayout mLoadingLayout;

    //@BindView(R.id.banner)
    //Banner mBanner;

   /* @BindView(R.id.zb_type_list)
    RecyclerView mZbTypeView;*/

    @BindView(R.id.main_data_list)
    RecyclerView mDataListView;

   // ZBTypeAdapter zbTypeAdapter;

    ZBDataAdapter zbDataAdapter;

    OKHttpRequest okHttpRequest = null;

    private int currentPage = 1;

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
        //mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //mBanner.stopAutoPlay();
    }

    public void init() {
        okHttpRequest = new OKHttpRequest();

        //zbTypeAdapter = new ZBTypeAdapter(new ArrayList<ZBTypeInfo>());
        zbDataAdapter = new ZBDataAdapter(new ArrayList<ZBDataInfo>());

        //mZbTypeView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDataListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //mZbTypeView.setAdapter(zbTypeAdapter);
        mDataListView.setAdapter(zbDataAdapter);

        /*mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                *//*SlideInfo slideInfo = mPresenter.getSlideInfo(position);
                Intent intent = new Intent(getActivity() ,WebActivity.class);
                intent.putExtra("title", slideInfo.getTitle());
                intent.putExtra("url", slideInfo.getTypeValue());
                startActivity(intent);*//*
            }
        });*/


        /*mBanner.isAutoPlay(true)
                .setDelayTime(1500)
                .setImageLoader(new BannerImageLoader())
                .setImages(null)
                .start();*/
    }

    public void loadData() {
        final Map<String, String> params = new HashMap<String, String>();
        Logger.e("current page---" + currentPage);
        params.put("p", String.valueOf(currentPage));
        okHttpRequest.aget(Server.URL_ALL_DATA, params, new OnResponseListener() {
            @Override
            public void onSuccess(String response) {
                if (!StringUtils.isEmpty(response)) {
                    ZBDataListRet result = Contants.gson.fromJson(response, ZBDataListRet.class);
                    if (result != null) {
                        mLoadingLayout.setVisibility(View.GONE);
                        zbDataAdapter.setNewData(result.data);
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
