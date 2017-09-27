package com.fy.tnzbsq.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.OrderInfo;
import com.fy.tnzbsq.util.TimeUtils;

import java.util.List;

public class OrderInfoAdapter extends BaseQuickAdapter<OrderInfo, BaseViewHolder> {

    private Context mContext;

    public OrderInfoAdapter(Context context, List<OrderInfo> list) {
        super(R.layout.order_info_item, list);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderInfo item) {
        helper.setText(R.id.tv_order_sn, item.order_sn)
                .setText(R.id.tv_user_id,item.user_id)
                .setText(R.id.tv_user_nickname,item.nickname)
                .setText(R.id.tv_order_date, TimeUtils.millis2String(Long.parseLong(item.order_time)))
                .setText(R.id.tv_order_material, item.goods_title);
    }
}
