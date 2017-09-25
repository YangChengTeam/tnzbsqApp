package com.fy.tnzbsq.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.adapter.CommentItemAdapter;
import com.fy.tnzbsq.bean.CommentInfo;
import com.fy.tnzbsq.bean.CommentInfoList;
import com.fy.tnzbsq.bean.CommentInfoRet;
import com.fy.tnzbsq.bean.CommunityInfo;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.common.Server;
import com.fy.tnzbsq.net.OKHttpRequest;
import com.fy.tnzbsq.net.listener.OnResponseListener;
import com.fy.tnzbsq.util.StringUtils;
import com.fy.tnzbsq.view.CommunityHeadView;
import com.fy.tnzbsq.view.CustomProgress;
import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.ToastUtil;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

import static com.fy.tnzbsq.App.loginUser;


/**
 * Created by admin on 2017/9/1.
 */

public class CommunityDetailActivity extends BaseAppActivity implements CommunityHeadView.CommunityDetailListener {

    @BindView(R.id.toolbarContainer)
    Toolbar mToolbar;

    @BindView(R.id.comment_list)
    RecyclerView mCommentRecyclerView;

    @BindView(R.id.et_comment_content)
    EditText mCommentContentEditText;

    @BindView(R.id.tv_send_comment)
    TextView mSendCommentTextView;

    private CommentItemAdapter mCommentItemAdapter;

    private CommunityInfo communityInfo;

    private int currentPage = 1;

    private int pageSize = 20;

    private CommunityHeadView headView;

    OKHttpRequest okHttpRequest = null;

    private CustomProgress dialog;

    @Override
    public int getLayoutId() {
        return R.layout.community_note_detail;
    }

    @Override
    protected void initVars() {

    }

    @Override
    protected void initViews() {
        okHttpRequest = new OKHttpRequest();

        Intent intent = getIntent();
        communityInfo = (CommunityInfo) intent.getSerializableExtra("community_info");

        String titleName = "";
        if (communityInfo.type.equals("1")) {
            titleName = "自拍交友";
        } else if (communityInfo.type.equals("2")) {
            titleName = "游戏互动";
        } else {
            titleName = "热门";
        }
        mToolbar.setTitle(titleName);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.back_icon);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (dialog == null) {
            dialog = CustomProgress.create(context, "回复中...", true, null);
        }

        if (communityInfo != null) {

            mCommentItemAdapter = new CommentItemAdapter(this, null);

            headView = new CommunityHeadView(this);

            mCommentItemAdapter.setHeaderView(headView);
            headView.setListener(this);

            headView.showHeadInfo(communityInfo);

            mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mCommentRecyclerView.setAdapter(mCommentItemAdapter);

            setPraiseStatus(communityInfo.agreed);

            loadCommentData(communityInfo.id);
        }

        mCommentItemAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadCommentData(communityInfo.id);
            }
        }, mCommentRecyclerView);


        RxView.clicks(mSendCommentTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                if (StringUtils.isEmpty(mCommentContentEditText.getText())) {
                    ToastUtil.toast(CommunityDetailActivity.this, "请输入回复内容");
                    return;
                }

                if (loginUser == null) {
                    Intent intent = new Intent(CommunityDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }

                if (communityInfo != null) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("note_id", communityInfo.id);
                    params.put("user_id", loginUser != null ? loginUser.id + "" : "");
                    try {
                        params.put("content", URLEncoder.encode(mCommentContentEditText.getText().toString(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    dialog.show();

                    okHttpRequest.aget(Server.FOLLOW_URL, params, new OnResponseListener() {
                        @Override
                        public void onSuccess(String response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            if (!StringUtils.isEmpty(response)) {
                                mCommentContentEditText.setText("");

                                CommentInfoRet result = Contants.gson.fromJson(response, CommentInfoRet.class);

                                if (result != null && result.data != null) {
                                    CommentInfo commentInfo = result.data;
                                    if (loginUser != null) {
                                        commentInfo.user_name = App.loginUser.nickname;
                                        commentInfo.face = App.loginUser.logo;
                                    }
                                    if (communityInfo != null && !StringUtils.isEmpty(communityInfo.follow_count)) {
                                        try {
                                            int resCount = Integer.parseInt(communityInfo.follow_count) + 1;
                                            headView.updateCommentCount(resCount);
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    mCommentItemAdapter.addData(0, commentInfo);
                                    RxBus.get().post(Contants.COMMUNITY_REFRESH, "from add communityInfo");
                                }
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            ToastUtil.toast(CommunityDetailActivity.this, "回复失败,请重试");
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onBefore() {

                        }
                    });

                }
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    public void loadCommentData(String nid) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("note_id", nid);
        params.put("page", currentPage + "");

        okHttpRequest.aget(Server.FOLLOW_LIST_URL, params, new OnResponseListener() {
            @Override
            public void onSuccess(String response) {

                if (!StringUtils.isEmpty(response)) {
                    CommentInfoList result = Contants.gson.fromJson(response, CommentInfoList.class);

                    if (currentPage > 1) {
                        mCommentItemAdapter.addData(result.data);
                    } else {
                        mCommentItemAdapter.setNewData(result.data);
                    }

                    if (result.data.size() == pageSize) {
                        currentPage++;
                        mCommentItemAdapter.loadMoreComplete();
                    } else {
                        mCommentItemAdapter.loadMoreEnd();
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

    @Override
    public void praiseClick() {
        if (loginUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        if (communityInfo != null) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("note_id", communityInfo.id);
            params.put("user_id", loginUser != null ? loginUser.id + "" : "");

            okHttpRequest.aget(Server.AGREE_URL, params, new OnResponseListener() {
                @Override
                public void onSuccess(String response) {
                    if (!StringUtils.isEmpty(response)) {

                        setPraiseStatus("1");
                        if (communityInfo != null && !StringUtils.isEmpty(communityInfo.agree_count)) {
                            try {
                                int resCount = Integer.parseInt(communityInfo.agree_count) + 1;

                                headView.updatePraiseCount(resCount);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                        RxBus.get().post(Contants.COMMUNITY_REFRESH, "from add communityInfo");
                    }
                }

                @Override
                public void onError(Exception e) {

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
    public void imageShow(int position) {
        if (communityInfo != null) {
            Intent intent = new Intent(CommunityDetailActivity.this, CommunityImageShowActivity.class);
            intent.putExtra("current_position", position);
            intent.putExtra("images", (Serializable) communityInfo.images);
            startActivity(intent);
        } else {
            ToastUtil.toast(this, "数据异常");
        }
    }

    public void setPraiseStatus(String type) {
        if (type != null && type.equals("1")) {
            Drawable isZan = ContextCompat.getDrawable(CommunityDetailActivity.this, R.mipmap.is_zan_icon);
            isZan.setBounds(0, 0, isZan.getMinimumWidth(), isZan.getMinimumHeight());
            headView.updatePraiseState(isZan);
        } else {
            Drawable noZan = ContextCompat.getDrawable(CommunityDetailActivity.this, R.mipmap.no_zan_icon);
            noZan.setBounds(0, 0, noZan.getMinimumWidth(), noZan.getMinimumHeight());
            headView.updatePraiseState(noZan);
        }
    }

}