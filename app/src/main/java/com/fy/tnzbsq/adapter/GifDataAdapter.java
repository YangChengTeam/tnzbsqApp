package com.fy.tnzbsq.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.GifDataInfo;
import com.fy.tnzbsq.bean.ZBDataInfo;
import com.fy.tnzbsq.view.GlideRoundTransform;

import java.util.List;

public class GifDataAdapter extends BaseQuickAdapter<GifDataInfo, BaseViewHolder> {

    private Context mContext;

    public GifDataAdapter(Context context, List<GifDataInfo> list) {
        super(R.layout.gif_data_item, list);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, GifDataInfo item) {
        helper.setText(R.id.tv_zb_title, item.title);
        Glide.with(mContext).load(item.imgUrl)
                .transform(new GlideRoundTransform(mContext, 1))
                .into((ImageView) helper.getConvertView().findViewById(R.id.iv_zb_thumb));
    }
}
