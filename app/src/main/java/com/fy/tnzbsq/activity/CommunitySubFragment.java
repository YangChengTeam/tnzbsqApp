package com.fy.tnzbsq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.adapter.CommunityItemClickAdapter;
import com.fy.tnzbsq.bean.CommunityInfoData;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.net.OKHttpRequest;
import com.fy.tnzbsq.net.listener.OnResponseListener;
import com.fy.tnzbsq.util.StringUtils;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by admin on 2017/9/7.
 */

public class CommunitySubFragment extends CustomBaseFragment implements SwipeRefreshLayout.OnRefreshListener, CommunityItemClickAdapter.PraiseListener {

    @BindView(R.id.loading_layout)
    LinearLayout mLoadingLayout;

    @BindView(R.id.loading_iv)
    GifImageView mGifLoadingView;

    @BindView(R.id.iv_no_date)
    ImageView mNoDataView;

    @BindView(R.id.pull_to_refresh)
    SwipeRefreshLayout swipeLayout;

    @BindView(R.id.all_note_list)
    RecyclerView mAllNoteRecyclerView;

    CommunityItemClickAdapter mCommunityItemAdapter;

    private int currentChildPosition;

    LinearLayoutManager mLinearLayoutManager;

    private int currentPage = 1;

    private int currentHotPage = 1;

    private int currentFriendsPage = 1;

    private int currentGamePage = 1;

    private int type = 1;

    private int currentItemPosition;

    OKHttpRequest okHttpRequest = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.community_all_note, null);
        ButterKnife.bind(this, mRootView);
        RxBus.get().register(this);
        init();
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void init() {
        okHttpRequest = new OKHttpRequest();

        Bundle bundle = getArguments();
        if (bundle != null) {
            type = getArguments().getInt("type");
        }

        swipeLayout.setColorSchemeResources(
                R.color.colorAccent,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                R.color.refresh_select_color);
        swipeLayout.setOnRefreshListener(this);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mCommunityItemAdapter = new CommunityItemClickAdapter(getActivity(), null);
        mCommunityItemAdapter.setPraiseListener(this);
        mAllNoteRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAllNoteRecyclerView.setAdapter(mCommunityItemAdapter);

        mCommunityItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                currentItemPosition = position;
                Intent intent = new Intent(getActivity(), CommunityDetailActivity.class);
                intent.putExtra("community_info", mCommunityItemAdapter.getData().get(position));
                startActivity(intent);
            }
        });

        mCommunityItemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.comment_layout) {
                    currentItemPosition = position;
                    Intent intent = new Intent(getActivity(), CommunityDetailActivity.class);
                    intent.putExtra("community_info", mCommunityItemAdapter.getData().get(position));
                    startActivity(intent);
                }
                return false;
            }
        });

        mCommunityItemAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadData();
            }
        }, mAllNoteRecyclerView);

        loadData();
    }

    @Override
    public void praiseItem(int parentPosition) {
        if (mCommunityItemAdapter.getData().get(parentPosition).agreed.equals("0")) {
            currentItemPosition = parentPosition;
            if (App.loginUser == null) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            } else {
                Map<String, String> params = new HashMap<String, String>();
                params.put("note_id", mCommunityItemAdapter.getData().get(currentItemPosition).id);
                params.put("user_id", App.loginUser != null ? App.loginUser.id + "" : "");

                okHttpRequest.aget(Server.AGREE_URL, params, new OnResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        if (!StringUtils.isEmpty(response)) {

                            mCommunityItemAdapter.getData().get(currentItemPosition).agreed = "1";
                            if (!StringUtils.isEmpty(mCommunityItemAdapter.getData().get(currentItemPosition).agree_count)) {
                                mCommunityItemAdapter.getData().get(currentItemPosition).agree_count = ((Integer.parseInt(mCommunityItemAdapter.getData().get(currentItemPosition).agree_count) + 1) + "");
                            }
                            mCommunityItemAdapter.changeView(mLinearLayoutManager.findViewByPosition(currentItemPosition), currentItemPosition);
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
    }

    public void loadData() {
        final Map<String, String> params = new HashMap<String, String>();
        currentPage = getCurrentPageByType(type);
        params.put("user_id", App.loginUser != null ? App.loginUser.id + "" : "");
        params.put("type", type + "");
        params.put("page", currentPage + "");
        params.put("limit", 20 + "");

        okHttpRequest.aget(Server.NOTE_LIST_URL, params, new OnResponseListener() {
            @Override
            public void onSuccess(String response) {
                swipeLayout.setRefreshing(false);
                if (!StringUtils.isEmpty(response)) {
                    CommunityInfoData result = Contants.gson.fromJson(response, CommunityInfoData.class);
                    if (result != null && result.data != null && result.data.size() > 0) {
                        mLoadingLayout.setVisibility(View.GONE);
                        mGifLoadingView.setVisibility(View.GONE);
                        mNoDataView.setVisibility(View.GONE);
                        swipeLayout.setRefreshing(false);
                        if (currentPage == 1) {
                            mCommunityItemAdapter.setNewData(result.data);
                        } else {
                            mCommunityItemAdapter.addData(result.data);
                        }

                        if (result.data.size() == 20) {
                            setCurrentPage();
                            mCommunityItemAdapter.loadMoreComplete();
                        } else {
                            mCommunityItemAdapter.loadMoreEnd();
                        }
                    } else {
                        mLoadingLayout.setVisibility(View.VISIBLE);
                        mGifLoadingView.setVisibility(View.GONE);
                        mNoDataView.setVisibility(View.VISIBLE);
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

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Contants.COMMUNITY_REFRESH)
            }
    )
    public void rxRefresh(String tag) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        currentFriendsPage = 1;
        currentGamePage = 1;
        currentHotPage = 1;
        currentPage = 1;
        loadData();
    }

    public void setCurrentPage() {
        switch (type) {
            case 1:
                currentFriendsPage++;
                break;
            case 2:
                currentGamePage++;
                break;
            case 3:
                currentHotPage++;
                break;
        }
    }

    public int getCurrentPageByType(int type) {
        switch (type) {
            case 1:
                currentPage = currentGamePage;
                break;
            case 2:
                currentPage = currentFriendsPage;
                break;
            case 3:
                currentPage = currentHotPage;
                break;
        }
        return currentPage;
    }
}
