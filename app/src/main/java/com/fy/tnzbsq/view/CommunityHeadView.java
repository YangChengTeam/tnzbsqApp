package com.fy.tnzbsq.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.adapter.ImageDetailSelectedAdapter;
import com.fy.tnzbsq.bean.CommunityInfo;
import com.fy.tnzbsq.util.StringUtils;
import com.fy.tnzbsq.util.TimeUtils;
import com.jakewharton.rxbinding.view.RxView;

import java.net.URLDecoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by admin on 2017/9/8.
 */

public class CommunityHeadView extends BaseView {

    private Context mContext;

    @BindView(R.id.iv_note_user_img)
    ImageView noteUserImageView;

    @BindView(R.id.tv_note_user_name)
    TextView mUserNameTextView;

    @BindView(R.id.tv_note_date)
    TextView mNoteDateTextView;

    @BindView(R.id.tv_note_title)
    TextView mNoteTitleTextView;

    @BindView(R.id.tv_comment_count)
    TextView mCommentCountTextView;

    @BindView(R.id.tv_praise_count)
    TextView mPraiseCountTextView;

    @BindView(R.id.note_detail_image_list)
    RecyclerView mNoteDetailImagesRecyclerView;

    ImageDetailSelectedAdapter mImageDetailSelectedAdapter;

    List<String> imageList;

    public CommunityDetailListener listener;

    public void setListener(CommunityDetailListener listener) {
        this.listener = listener;
    }

    public interface CommunityDetailListener {
        void praiseClick();

        void imageShow(int position);
    }

    public CommunityHeadView(Context context) {
        super(context);
        this.mContext = context;
    }

    public CommunityHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public int getLayoutId() {
        return R.layout.community_note_detail_head;
    }

    public void showHeadInfo(CommunityInfo communityInfo) {
        imageList = communityInfo.images;
        Glide.with(mContext).load(communityInfo.face).transform(new GlideRoundTransform(mContext, 30)).into(noteUserImageView);

        mNoteDetailImagesRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mImageDetailSelectedAdapter = new ImageDetailSelectedAdapter(mContext, imageList);
        mNoteDetailImagesRecyclerView.setAdapter(mImageDetailSelectedAdapter);

        mUserNameTextView.setText(communityInfo.user_name);

        if (!StringUtils.isEmpty(communityInfo.add_time)) {
            long addTime = Long.parseLong(communityInfo.add_time) * 1000;
            mNoteDateTextView.setText(TimeUtils.millis2String(addTime));
        }
        try {
            mNoteTitleTextView.setText(URLDecoder.decode(communityInfo.content, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mCommentCountTextView.setText(communityInfo.follow_count);
        mPraiseCountTextView.setText(communityInfo.agree_count);

        RxView.clicks(mPraiseCountTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                listener.praiseClick();
            }
        });

        mImageDetailSelectedAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                listener.imageShow(position);
            }
        });
    }

    public void updateCommentCount(int count) {
        mCommentCountTextView.setText(count + "");
    }

    public void updatePraiseCount(int count) {
        mPraiseCountTextView.setText(count + "");
    }

    public void updatePraiseState(Drawable state) {
        mPraiseCountTextView.setCompoundDrawables(state, null, null, null);
    }

}