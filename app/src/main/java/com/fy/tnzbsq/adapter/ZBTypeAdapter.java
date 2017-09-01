package com.fy.tnzbsq.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.ChannelInfo;

import java.util.List;

public class ZBTypeAdapter extends BaseQuickAdapter<ChannelInfo, BaseViewHolder> {

    public ZBTypeAdapter(List<ChannelInfo> list) {
        super(R.layout.zb_type_item, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChannelInfo item) {
        helper.setText(R.id.tv_zb_type_name, item.name);
        Glide.with(mContext).load(item.ico).into((ImageView) helper.getConvertView().findViewById(R.id.iv_zb_type));
    }
}
