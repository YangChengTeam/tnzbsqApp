package com.fy.tnzbsq.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.util.ImageUtil;

import java.util.List;

public class ImageSelectedAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private Context mContext;

    public ImageSelectedAdapter(Context context, List<String> datas) {
        super(R.layout.community_image_seelected_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String imgUrl) {
        Glide.with(mContext).load(imgUrl).into((ImageView) helper.getConvertView().findViewById(R.id.iv_community_add_note));
        if (helper.getAdapterPosition() == 0 && ImageUtil.ADD_PATH.equals(imgUrl)) {
            helper.setVisible(R.id.iv_delete_image, false);
        }else{
            helper.setVisible(R.id.iv_delete_image, true);
        }
        helper.addOnClickListener(R.id.iv_delete_image);
    }
}