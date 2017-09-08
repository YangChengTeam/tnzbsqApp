package com.fy.tnzbsq.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.adapter.CommunityItemClickAdapter;
import com.fy.tnzbsq.bean.CommunityInfoData;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.net.OKHttpRequest;
import com.fy.tnzbsq.net.listener.OnResponseListener;
import com.fy.tnzbsq.util.StringUtils;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/9/7.
 */

public class CommunitySubFragment extends CustomBaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.loading_layout)
    LinearLayout mLoadingLayout;

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

    private int currentEnglishPage = 1;

    private int type = 1;

    private int currentItemPosition;

    OKHttpRequest okHttpRequest = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.community_all_note, null);
        ButterKnife.bind(this, mRootView);
        init();
        //loadData();
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
        mAllNoteRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAllNoteRecyclerView.setAdapter(mCommunityItemAdapter);

        mCommunityItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                currentItemPosition = position;
                /*Intent intent = new Intent(getActivity(), CommunityDetailActivity.class);
                intent.putExtra("community_info", mCommunityItemAdapter.getData().get(position));
                startActivity(intent);*/
            }
        });

        mCommunityItemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                /*if (view.getId() == R.id.praise_count_layout && mCommunityItemAdapter.getData().get(position).getAgreed().equals("0")) {
                    currentChildPosition = position;
                    mPresenter.addAgreeInfo(UserInfoHelper.getUserInfo() != null ? UserInfoHelper.getUserInfo().getUid() : "", ((CommunityInfo) adapter.getData().get(position)).getId());
                }
                if (view.getId() == R.id.comment_layout) {
                    currentItemPosition = position;
                    Intent intent = new Intent(getActivity(), CommunityDetailActivity.class);
                    intent.putExtra("community_info", mCommunityItemAdapter.getData().get(position));
                    startActivity(intent);
                }*/
                return false;
            }
        });

        mAllNoteRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isSlideToBottom(recyclerView) && mCommunityItemAdapter.getData().size() >= 10) {
                    setCurrentPage();
                    //mPresenter.communityInfoList(UserInfoHelper.getUserInfo() != null ? UserInfoHelper.getUserInfo().getUid() : "", type, getCurrentPageByType(type), 10);
                }
            }
        });

        //mPresenter.communityInfoList(UserInfoHelper.getUserInfo() != null ? UserInfoHelper.getUserInfo().getUid() : "", type, getCurrentPageByType(type), 10);
        loadData();
    }

    public void loadData() {
        final Map<String, String> params = new HashMap<String, String>();
        Logger.e("current page---" + currentPage);

        params.put("user_id", "3");
        params.put("type", type + "");
        params.put("page", currentPage + "");
        params.put("limit", 10 + "");

        okHttpRequest.aget(Server.NOTE_LIST_URL, params, new OnResponseListener() {
            @Override
            public void onSuccess(String response) {
                swipeLayout.setRefreshing(false);
                if (!StringUtils.isEmpty(response)) {
                    CommunityInfoData result = Contants.gson.fromJson(response, CommunityInfoData.class);
                    if (result != null && result.data.list != null) {
                        mLoadingLayout.setVisibility(View.GONE);

                        if (currentPage == 1) {
                            mCommunityItemAdapter.setNewData(result.data.list);
                        } else {
                            mCommunityItemAdapter.addData(result.data.list);
                        }

                        if (result.data.list.size() == 20) {
                            currentPage++;
                            mCommunityItemAdapter.loadMoreComplete();
                        } else {
                            mCommunityItemAdapter.loadMoreEnd();
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

    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    @Override
    public void onRefresh() {

    }

    public void setCurrentPage() {
        switch (type) {
            case 1:
                currentEnglishPage++;
                break;
            case 2:
                currentFriendsPage++;
                break;
            case 3:
                currentHotPage++;
                break;
        }
    }

    public int getCurrentPageByType(int type) {
        switch (type) {
            case 1:
                currentPage = currentFriendsPage;
                break;
            case 2:
                currentPage = currentEnglishPage;
                break;
            case 3:
                currentPage = currentHotPage;
                break;
        }
        return currentPage;
    }
}
