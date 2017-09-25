package com.fy.tnzbsq.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.CommentInfo;
import com.fy.tnzbsq.util.StringUtils;
import com.fy.tnzbsq.util.TimeUtils;
import com.fy.tnzbsq.view.GlideRoundTransform;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CommentItemAdapter extends BaseQuickAdapter<CommentInfo, BaseViewHolder> {

    private Context mContext;

    public CommentItemAdapter(Context context, List<CommentInfo> datas) {
        super(R.layout.comment_list_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CommentInfo item) {
        try {
            helper.setText(R.id.tv_comment_user_name, item.user_name)
                    .setText(R.id.tv_comment_content, URLDecoder.decode(item.content, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!StringUtils.isEmpty(item.add_time)) {
            long addTime = Long.parseLong(item.add_time) * 1000;
            helper.setText(R.id.tv_comment_date, TimeUtils.millis2String(addTime, new SimpleDateFormat("MM-dd HH:mm",
                    Locale.getDefault())));
        }
        Glide.with(mContext).load(item.face).transform(new GlideRoundTransform(mContext, 25)).into((ImageView) helper.getConvertView().findViewById(R.id.iv_comment_user_img));
    }
}