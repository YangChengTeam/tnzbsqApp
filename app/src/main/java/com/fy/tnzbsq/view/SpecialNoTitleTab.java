package com.fy.tnzbsq.view;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fy.tnzbsq.R;

import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;

/**
 * Created by mjj on 2017/6/3
 */
public class SpecialNoTitleTab extends BaseTabItem {

    private LinearLayout addLayout;

    private ImageView mIcon;

    private int mDefaultDrawable;
    private int mCheckedDrawable;

    private int mDefaultTextColor = 0x56000000;
    private int mCheckedTextColor = 0x56000000;

    private Context mContext;

    public interface MainAddListener {
        void addListener();
    }

    public MainAddListener listener;

    public void setListener(MainAddListener listener) {
        this.listener = listener;
    }

    public SpecialNoTitleTab(Context context) {
        this(context, null);
        this.mContext = context;
    }

    public SpecialNoTitleTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpecialNoTitleTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.special_notitle_tab, this, true);

        addLayout = (LinearLayout) findViewById(R.id.add_layout);
        mIcon = (ImageView) findViewById(R.id.icon);

        addLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.addListener();
            }
        });
    }

   /* @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        View view = getChildAt(0);
        if (view != null) {
            view.setOnClickListener(l);
        }
    }*/

    /**
     * 方便初始化的方法
     *
     * @param drawableRes        默认状态的图标
     * @param checkedDrawableRes 选中状态的图标
     * @param title              标题
     */
    public void initialize(@DrawableRes int drawableRes, @DrawableRes int checkedDrawableRes, String title) {
        mDefaultDrawable = drawableRes;
        mCheckedDrawable = checkedDrawableRes;
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked) {
            mIcon.setImageResource(mCheckedDrawable);
        } else {
            mIcon.setImageResource(mDefaultDrawable);
        }
    }

    @Override
    public void setMessageNumber(int number) {
    }

    @Override
    public void setHasMessage(boolean hasMessage) {
    }

    @Override
    public String getTitle() {
        return "";
    }

    public void setTextDefaultColor(@ColorInt int color) {
        mDefaultTextColor = color;
    }

    public void setTextCheckedColor(@ColorInt int color) {
        mCheckedTextColor = color;
    }
}
