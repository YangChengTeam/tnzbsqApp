package com.yc.loanbox.view.adpater;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kk.utils.ScreenUtil;
import com.yc.loanbox.R;
import com.yc.loanbox.helper.GlideHelper;
import com.yc.loanbox.model.bean.ProductInfo;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends PagerAdapter {
    private int logo_w = 80;
    private Context mContext;
    private LayoutInflater mInflater;
    private OnRecyclerViewListener onRecyclerViewListener;
    private BitmapFactory.Options options;
    List<ProductInfo> productModels = new ArrayList();

    public GalleryAdapter(Context context, List<ProductInfo> list) {
        this.mContext = context;
        this.productModels = list;
        this.mInflater = LayoutInflater.from(context);
        this.logo_w = ScreenUtil.dip2px(mContext, 50);
    }

    public void update(List<ProductInfo> list) {
        this.productModels.clear();
        this.productModels.addAll(list);
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.productModels.size();
    }

    public ProductInfo getItem(int postion) {
        return (ProductInfo) this.productModels.get(postion);
    }

    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.loanbox_item_gallery, container, false);
        container.addView(view);
        ProductInfo model = (ProductInfo) this.productModels.get(position);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        imageView.getLayoutParams().width = this.logo_w;
        imageView.getLayoutParams().height = this.logo_w;
        GlideHelper.circleBorderImageView(mContext, imageView, model.getIco(), R.mipmap.product_default_image,0, 0 , logo_w, logo_w);
        ((RatingBar) view.findViewById(R.id.ratingBar)).setRating(5.0f);
        ((TextView) view.findViewById(R.id.title)).setText(model.getName());
        ((TextView) view.findViewById(R.id.intro)).setText(model.getDesp());
        ((TextView) view.findViewById(R.id.intro2)).setText(model.getDesp2());
        ((TextView) view.findViewById(R.id.price)).setText(model.getMoney_limit_min() + "-" + model.getMoney_limit_max());
        ((TextView) view.findViewById(R.id.rll)).setText(model.getDay_rate() + "%");
        ((TextView) view.findViewById(R.id.qx)).setText(model.getTime_limit_min() + "-" + model.getTime_limit_max() + "天");
        TextView ok_num = (TextView) view.findViewById(R.id.ok_num);
        String num = model.getSucc_num() + "";
        SpannableString ss = new SpannableString(num + "人已成功放款");
        ss.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.red)), 0, num.length(), 17);
        ok_num.setText(ss);
        final int i = position;
        view.findViewById(R.id.layout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GalleryAdapter.this.onRecyclerViewListener.onItemClick(v, i);
            }
        });
        return view;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }
}