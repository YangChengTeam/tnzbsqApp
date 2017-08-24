package com.fy.tnzbsq.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.ZBDataInfo;

import java.util.List;

public class ZBDataAdapter extends BaseQuickAdapter<ZBDataInfo, BaseViewHolder> {

    public ZBDataAdapter(List<ZBDataInfo> list) {
        super(R.layout.zb_data_item, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, ZBDataInfo item) {
        helper.setText(R.id.tv_zb_title, item.title)
        .setText(R.id.tv_zb_type, item.name)
        .setText(R.id.tv_zb_des, item.desp)
        .setText(R.id.tv_use_count, item.build_num);
        Glide.with(mContext).load(item.front_img).into((ImageView) helper.getConvertView().findViewById(R.id.iv_zb_thumb));
    }
}
