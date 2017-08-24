package com.fy.tnzbsq.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.ZBTypeInfo;

import java.util.List;

public class ZBTypeAdapter extends BaseQuickAdapter<ZBTypeInfo, BaseViewHolder> {

    public ZBTypeAdapter(List<ZBTypeInfo> list) {
        super(R.layout.zb_type_item, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, ZBTypeInfo item) {
        helper.setText(R.id.tv_zb_type_name, item.ztitle);
        Glide.with(mContext).load(R.mipmap.zb_type_date).into((ImageView) helper.getConvertView().findViewById(R.id.iv_zb_type));
    }
}
