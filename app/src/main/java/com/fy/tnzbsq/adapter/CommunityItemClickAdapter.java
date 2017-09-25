package com.fy.tnzbsq.adapter;

import android.content.ClipboardManager;
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
import com.kk.utils.ToastUtil;

import org.kymjs.kjframe.utils.StringUtils;

import java.io.Serializable;
import java.util.List;

public class CommunityItemClickAdapter extends BaseQuickAdapter<CommunityInfo, BaseViewHolder> {

    private Context mContext;

    public interface PraiseListener{
        void praiseItem(int parentPosition);
    }

    public PraiseListener praiseListener;

    public void setPraiseListener(PraiseListener praiseListener) {
        this.praiseListener = praiseListener;
    }

    public CommunityItemClickAdapter(Context context, List<CommunityInfo> datas) {
        super(R.layout.community_note_list_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CommunityInfo item) {
        helper.setText(R.id.ev_note_title, item.content)
                .setText(R.id.tv_note_user_name, item.user_name)
                .setText(R.id.tv_comment_count, item.follow_count)
                .setText(R.id.tv_praise_count, item.agree_count);

        if (helper.getAdapterPosition() == 0) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(SizeUtils.dp2px(mContext, 6), SizeUtils.dp2px(mContext, 6), SizeUtils.dp2px(mContext, 6), SizeUtils.dp2px(mContext, 6));
            helper.getConvertView().setLayoutParams(layoutParams);
        }

        if (!StringUtils.isEmpty(item.add_time)) {
            long addTime = Long.parseLong(item.add_time) * 1000;
            helper.setText(R.id.tv_note_date, TimeUtils.millis2String(addTime));
        }

        Glide.with(mContext).load(item.face).transform(new GlideCircleTransform(mContext)).into((ImageView) helper.getConvertView().findViewById(R.id.iv_note_user_img));
        helper.addOnClickListener(R.id.tv_comment_count);

        helper.getConvertView().findViewById(R.id.tv_praise_count).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                praiseListener.praiseItem(helper.getAdapterPosition());
            }
        });

        helper.getConvertView().findViewById(R.id.ev_note_title).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager cm =(ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(item.content);
                ToastUtil.toast(mContext,"已复制到粘贴板");
                return false;
            }
        });

        helper.addOnLongClickListener(R.id.ev_note_title);

        CommunityImageAdapter communityImageAdapter = new CommunityImageAdapter(mContext, item.images);
        RecyclerView imagesRecyclerView = (RecyclerView) helper.getConvertView().findViewById(R.id.imgs_list);
        imagesRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        imagesRecyclerView.setAdapter(communityImageAdapter);

        TextView praiseTextView = (TextView) helper.getConvertView().findViewById(R.id.tv_praise_count);

        if (item.agreed.equals("1")) {
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
                intent.putExtra("images", (Serializable) item.images);
                mContext.startActivity(intent);
            }
        });
    }

    public void changeView(View cView, int pos) {
        TextView praiseTextView = (TextView) cView.findViewById(R.id.tv_praise_count);
        String isPraise = this.getData().get(pos).agreed;
        if (isPraise.equals("1")) {
            Drawable isZan = ContextCompat.getDrawable(mContext, R.mipmap.is_zan_icon);
            isZan.setBounds(0, 0, isZan.getMinimumWidth(), isZan.getMinimumHeight());
            praiseTextView.setCompoundDrawables(isZan, null, null, null);
            praiseTextView.setText(this.getData().get(pos).agree_count);
        } else {
            Drawable isZan = ContextCompat.getDrawable(mContext, R.mipmap.no_zan_icon);
            isZan.setBounds(0, 0, isZan.getMinimumWidth(), isZan.getMinimumHeight());
            praiseTextView.setCompoundDrawables(isZan, null, null, null);
        }
    }

}