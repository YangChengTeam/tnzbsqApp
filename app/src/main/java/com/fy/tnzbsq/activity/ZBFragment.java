package com.fy.tnzbsq.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.adapter.ZBDataAdapter;
import com.fy.tnzbsq.adapter.ZBTypeAdapter;
import com.fy.tnzbsq.bean.AdDataInfo;
import com.fy.tnzbsq.bean.AdDataListRet;
import com.fy.tnzbsq.bean.SlideInfo;
import com.fy.tnzbsq.bean.ZBDataListRet;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.net.OKHttpRequest;
import com.fy.tnzbsq.net.listener.OnResponseListener;
import com.fy.tnzbsq.util.CommUtils;
import com.fy.tnzbsq.util.NetUtil;
import com.fy.tnzbsq.util.StringUtils;
import com.fy.tnzbsq.view.BannerImageLoader;
import com.fy.tnzbsq.view.NotifyUtil;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.ToastUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.orhanobut.logger.Logger;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * 装逼图
 * Created by admin on 2017/8/24.
 */

public class ZBFragment extends CustomBaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.search_img)
    ImageView mSearchImageView;

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

    private LinearLayout zbsqAdLayout;

    private ImageView mZbsqAdImageView;

    private int downloadId = 0;

    int progresses = 0;

    int total = 0;

    File gameAdFile;

    private View mRootView;

    private AdDataInfo adDataInfo;

    private void showProgress() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*if (progresses > total) {
                    return;
                }*/
                NotifyUtil.buildProgress(10201, R.mipmap.game_ad_logo, "正在下载", progresses, total, "下载进度:%dM/%dM").show();//"下载进度:%d%%"
                showProgress();
            }
        }, 100);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.zb_fragment, null);
            ButterKnife.bind(this, mRootView);
            init();
            loadData();
            loadDataAd();
        }
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
        zbsqAdLayout = ButterKnife.findById(headView, R.id.zbsq_ad_layout);
        mZbsqAdImageView = ButterKnife.findById(headView, R.id.iv_zbsq_ad);
        mZbTypeView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mZbTypeView.setAdapter(zbTypeAdapter);
        return headView;
    }

    public void init() {

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

        zbsqAdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adDataInfo != null) {
                    adDown();
                } else {
                    ToastUtil.toast(getActivity(), "下载数据异常,请稍后重试");
                }
            }
        });

        //搜索
        RxView.clicks(mSearchImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    public void adDown() {
        //已安装，直接启动
        if (CommUtils.appInstalled(getActivity(), adDataInfo.pack_name)) {

            PackageManager packageManager = getActivity().getPackageManager();
            Intent sIntent = new Intent();
            sIntent = packageManager.getLaunchIntentForPackage(adDataInfo.pack_name);
            getActivity().startActivity(sIntent);
        } else {
            final File fileDir = new File(Contants.BASE_NORMAL_FILE_DIR);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            gameAdFile = new File(fileDir + adDataInfo.file_name);

            try {
                if (gameAdFile.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.fromFile(gameAdFile), "application/vnd.android.package-archive");
                    getActivity().startActivity(intent);
                } else {

                    if (NetUtil.isWIFIConnected(getActivity())) {
                        Toast.makeText(getActivity(), adDataInfo.toast_value, Toast.LENGTH_LONG).show();
                        String gameUrl = adDataInfo.down_url;
                        showProgress();
                        FileDownloader.setup(getActivity());
                        downloadId = FileDownloader.getImpl().create(gameUrl).setPath(fileDir + adDataInfo.file_name, false).setListener(new FileDownloadListener() {
                            @Override
                            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                total = totalBytes / 1024 / 1024;
                            }

                            @Override
                            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                total = totalBytes / 1024 / 1024;
                                progresses = soFarBytes / 1024 / 1024;
                                Log.e("progress total", "progress total--->" + total);
                                Log.e("progress", "progress--->" + progresses);
                            }

                            @Override
                            protected void completed(BaseDownloadTask task) {
                                progresses = total;
                                Log.e("completed progress", " completed progress--->" + progresses);
                                NotifyUtil.cancelAll();
                                //FileDownloader.getImpl().clearAllTaskData();
                                if (gameAdFile.exists()) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setDataAndType(Uri.fromFile(gameAdFile), "application/vnd.android.package-archive");
                                    getActivity().startActivity(intent);
                                }
                            }

                            @Override
                            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                            }

                            @Override
                            protected void error(BaseDownloadTask task, Throwable e) {

                            }

                            @Override
                            protected void warn(BaseDownloadTask task) {

                            }
                        }).start();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    public void loadDataAd() {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("version", CommUtils.getVersionName(getActivity()));
        okHttpRequest.aget(Server.AD_URL, params, new OnResponseListener() {
            @Override
            public void onSuccess(String response) {

                zbsqAdLayout.setVisibility(View.VISIBLE);
                if (!StringUtils.isEmpty(response)) {
                    AdDataListRet result = Contants.gson.fromJson(response, AdDataListRet.class);
                    if (result != null && result.data.size() > 0) {
                        if (result.errCode.equals("0")) {
                            for (int i = 0; i < result.data.size(); i++) {
                                adDataInfo = result.data.get(i);
                                if (CommUtils.appInstalled(getActivity(), adDataInfo.pack_name)) {
                                    continue;
                                } else {
                                    break;
                                }
                            }
                            Glide.with(getActivity()).load(adDataInfo.image_url).into(mZbsqAdImageView);
                        }
                        if (result.errCode.equals("1")) {
                            zbsqAdLayout.setVisibility(View.GONE);
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
