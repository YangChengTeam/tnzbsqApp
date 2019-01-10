package com.yc.loanbox.view.adpater;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kk.utils.ScreenUtil;
import com.yc.loanbox.R;
import com.yc.loanbox.helper.GlideHelper;
import com.yc.loanbox.model.bean.ProductInfo;

import java.util.List;

public class Product2Adapter extends BaseQuickAdapter<ProductInfo, BaseViewHolder>  {

    private int logoImageWidth = 0;
    private boolean isOnline;

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public Product2Adapter(List<ProductInfo> data) {
        super(R.layout.loanbox_item_product3, data);
        this.isOnline = isOnline;
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductInfo item) {
        if( logoImageWidth == 0) {
            logoImageWidth = ScreenUtil.dip2px(mContext, 50);
        }
        if(isOnline) {
            GlideHelper.circleBorderImageView(mContext, helper.getView(R.id.logo), item.getIco(), R.mipmap.product_default_image,0, 0 , logoImageWidth, logoImageWidth);
            helper.setText(R.id.name, item.getName());
        } else {
            helper.setText(R.id.name, "****");
            GlideHelper.circleBorderImageView(mContext, helper.getView(R.id.logo), "", R.mipmap.product_default_image,0, 0 , logoImageWidth, logoImageWidth);
        }
        helper.setText(R.id.price, "最高"+item.getMoney_limit_max());
    }
}
