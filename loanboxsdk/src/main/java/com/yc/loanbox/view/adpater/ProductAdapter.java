package com.yc.loanbox.view.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kk.utils.ScreenUtil;
import com.yc.loanbox.R;
import com.yc.loanbox.helper.GlideHelper;
import com.yc.loanbox.model.bean.ProductInfo;

import java.util.List;

public class ProductAdapter extends BaseQuickAdapter<ProductInfo, BaseViewHolder> {

    private int layout = 0;
    private int logoImageWidth = 0;

    public ProductAdapter(int layout, List<ProductInfo> data) {
        super(layout, data);
        this.layout = layout;

    }



    @Override
    protected void convert(BaseViewHolder helper, ProductInfo item) {

        if(logoImageWidth == 0) {
            logoImageWidth = ScreenUtil.dip2px(mContext, 50);
        }

        if(layout == R.layout.loanbox_item_product){
            helper.setText(R.id.name, item.getName());
            helper.setText(R.id.des1, "日利率:"+item.getDay_rate()+"%|期限" + item.getTime_limit_min()+"-"+item.getTime_limit_max()+"天");
            helper.setText(R.id.price, "¥"+item.getMoney_limit_min()+"-"+item.getMoney_limit_max());
            helper.setText(R.id.des2, item.getDesp());
            if(item.getSpecial_flag() != null && item.getSpecial_flag().length() > 8){
                helper.getView(R.id.tag_image).setVisibility(View.VISIBLE);
                GlideHelper.imageView(mContext, helper.getView(R.id.tag_image), item.getSpecial_flag(), 0);
            } else {
                helper.getView(R.id.tag_image).setVisibility(View.VISIBLE);
            }
            GlideHelper.circleBorderImageView(mContext, helper.getView(R.id.logo), item.getIco(), R.mipmap.product_default_image,0, 0 , logoImageWidth, logoImageWidth);
        } else if(layout == R.layout.loanbox_item_cnxh){
            RecyclerView.LayoutParams params =  (RecyclerView.LayoutParams)helper.itemView.getLayoutParams();
            if(helper.getAdapterPosition() == 0){
                params.setMargins(10, 0, 0, 0);
            } else if(helper.getAdapterPosition() == getData().size() - 1){
                params.setMargins(8, 0, 10, 0);
            } else  {
                params.setMargins(8, 0, 0, 0);
            }
            helper.itemView.setLayoutParams(params);
            helper.setText(R.id.title, item.getName());
            helper.setText(R.id.price, "¥"+item.getMoney_limit_max());
            GlideHelper.circleBorderImageView(mContext, helper.getView(R.id.image), item.getIco(), R.mipmap.product_default_image,0, 0, logoImageWidth, logoImageWidth);
        }
    }
}
