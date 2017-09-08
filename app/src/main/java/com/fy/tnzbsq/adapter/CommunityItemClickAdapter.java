package com.fy.tnzbsq.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.activity.CommunityImageShowActivity;
import com.fy.tnzbsq.bean.CommunityInfo;
import com.fy.tnzbsq.util.SizeUtils;
import com.fy.tnzbsq.util.TimeUtils;
import com.fy.tnzbsq.view.GlideCircleTransform;

import org.kymjs.kjframe.utils.StringUtils;

import java.io.Serializable;
import java.util.List;

public class CommunityItemClickAdapter extends BaseQuickAdapter<CommunityInfo, BaseViewHolder> {

    private Context mContext;

    public CommunityItemClickAdapter(Context context, List<CommunityInfo> datas) {
        super(R.layout.community_note_list_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CommunityInfo item) {
        helper.setText(R.id.tv_note_title, item.getContent())
                .setText(R.id.tv_note_user_name, item.getUser_name())
                .setText(R.id.tv_comment_count, item.getFollow_count())
                .setText(R.id.tv_praise_count, item.getAgree_count());

        if (helper.getAdapterPosition() == 0) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(SizeUtils.dp2px(mContext, 6), SizeUtils.dp2px(mContext, 6), SizeUtils.dp2px(mContext, 6), SizeUtils.dp2px(mContext, 6));
            helper.getConvertView().setLayoutParams(layoutParams);
        }

        if (!StringUtils.isEmpty(item.getAdd_time())) {
            long addTime = Long.parseLong(item.getAdd_time()) * 1000;
            helper.setText(R.id.tv_note_date, TimeUtils.millis2String(addTime));
        }

        Glide.with(mContext).load(item.getFace()).transform(new GlideCircleTransform(mContext)).into((ImageView) helper.getConvertView().findViewById(R.id.iv_note_user_img));

        helper.addOnClickListener(R.id.tv_comment_count).addOnClickListener(R.id.tv_praise_count);

        CommunityImageAdapter communityImageAdapter = new CommunityImageAdapter(mContext, item.getImages());
        RecyclerView imagesRecyclerView = (RecyclerView) helper.getConvertView().findViewById(R.id.imgs_list);
        imagesRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        imagesRecyclerView.setAdapter(communityImageAdapter);

        TextView praiseTextView = (TextView) helper.getConvertView().findViewById(R.id.tv_praise_count);

        if (item.getAgreed().equals("1")) {
            Drawable isZan = mContext.getResources().getDrawable(R.mipmap.is_zan_icon);
            isZan.setBounds(0, 0, isZan.getMinimumWidth(), isZan.getMinimumHeight());
            praiseTextView.setCompoundDrawables(isZan, null, null, null);
        } else {
            Drawable isZan = mContext.getResources().getDrawable(R.mipmap.no_zan_icon);
            isZan.setBounds(0, 0, isZan.getMinimumWidth(), isZan.getMinimumHeight());
            praiseTextView.setCompoundDrawables(isZan, null, null, null);
        }

        communityImageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, CommunityImageShowActivity.class);
                intent.putExtra("current_position",position);
                intent.putExtra("images", (Serializable) item.getImages());
                mContext.startActivity(intent);
            }
        });
    }

    public void changeView(View cView, int pos) {
        TextView praiseTextView = (TextView) cView.findViewById(R.id.tv_praise_count);
        String isPraise = this.getData().get(pos).getAgreed();
        if (isPraise.equals("1")) {
            Drawable isZan = ContextCompat.getDrawable(mContext, R.mipmap.is_zan_icon);
            isZan.setBounds(0, 0, isZan.getMinimumWidth(), isZan.getMinimumHeight());
            praiseTextView.setCompoundDrawables(isZan, null, null, null);
            praiseTextView.setText((Integer.parseInt(this.getData().get(pos).getAgree_count()) + 1) + "");
        } else {
            Drawable isZan = ContextCompat.getDrawable(mContext, R.mipmap.no_zan_icon);
            isZan.setBounds(0, 0, isZan.getMinimumWidth(), isZan.getMinimumHeight());
            praiseTextView.setCompoundDrawables(isZan, null, null, null);
        }
    }

}