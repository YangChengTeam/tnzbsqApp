package com.fy.tnzbsq.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.bean.ZBDataInfo;
import com.fy.tnzbsq.view.GlideRoundTransform;

import java.util.List;

public class ZBDataAdapter extends BaseQuickAdapter<ZBDataInfo, BaseViewHolder> {

    private Context mContext;

    public ZBDataAdapter(Context context, List<ZBDataInfo> list) {
        super(R.layout.zb_data_item, list);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ZBDataInfo item) {
        helper.setText(R.id.tv_zb_title, item.title)
                .setText(R.id.tv_zb_type, item.name)
                .setText(R.id.tv_zb_des, item.desp)
                .setText(R.id.tv_use_count, item.build_num);
        Glide.with(mContext).load(item.small_img)
                .transform(new GlideRoundTransform(mContext, 1))
                .into((ImageView) helper.getConvertView().findViewById(R.id.iv_zb_thumb));
        if (item.is_vip == 0) {
            helper.setVisible(R.id.iv_is_lock, false);
        } else {
            helper.setVisible(R.id.iv_is_lock, true);
        }
    }
}
