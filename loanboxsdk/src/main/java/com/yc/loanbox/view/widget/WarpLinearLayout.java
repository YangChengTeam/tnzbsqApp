package com.yc.loanbox.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;

import com.yc.loanbox.R;

import java.util.ArrayList;
import java.util.List;

public class WarpLinearLayout extends ViewGroup {
    private Type mType;
    private List<WarpLine> mWarpLineGroup;

    public static final class Gravite {
        public static final int CENTER = 2;
        public static final int LEFT = 1;
        public static final int RIGHT = 0;
    }

    private static final class Type {
        private int grivate;
        private float horizontal_Space;
        private boolean isFull;
        private float vertical_Space;
        public static final int[] WarpLinearLayout = new int[]{R.attr.grivate, R.attr.horizontal_Space, R.attr.isFull, R.attr.vertical_Space};

        Type(Context context, AttributeSet attrs) {
            if (attrs != null) {
                TypedArray typedArray = context.obtainStyledAttributes(attrs, WarpLinearLayout);
                this.grivate = typedArray.getInt(0, this.grivate);
                this.horizontal_Space = typedArray.getDimension(1, this.horizontal_Space);
                this.vertical_Space = typedArray.getDimension(3, this.vertical_Space);
                this.isFull = typedArray.getBoolean(2, this.isFull);
            }
        }
    }

    private final class WarpLine {
        private int height;
        private List<View> lineView;
        private int lineWidth;

        private WarpLine() {
            this.lineView = new ArrayList();
            this.lineWidth = WarpLinearLayout.this.getPaddingLeft() + WarpLinearLayout.this.getPaddingRight();
            this.height = 0;
        }

        private void addView(View view) {
            if (this.lineView.size() != 0) {
                this.lineWidth = (int) (((float) this.lineWidth) + WarpLinearLayout.this.mType.horizontal_Space);
            }
            this.height = this.height > view.getMeasuredHeight() ? this.height : view.getMeasuredHeight();
            this.lineWidth += view.getMeasuredWidth();
            this.lineView.add(view);
        }
    }

    public WarpLinearLayout(Context context) {
        this(context, null);
    }

    public WarpLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.style.WarpLinearLayoutDefault);
    }

    public WarpLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mType = new Type(context, attrs);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        int withMode = MeasureSpec.getMode(widthMeasureSpec);
        int withSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int with = 0;
        int childCount = getChildCount();
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        switch (withMode) {
            case Integer.MIN_VALUE:
                for (i = 0; i < childCount; i++) {
                    if (i != 0) {
                        with = (int) (((float) with) + this.mType.horizontal_Space);
                    }
                    with += getChildAt(i).getMeasuredWidth();
                }
                with += getPaddingLeft() + getPaddingRight();
                if (with > withSize) {
                    with = withSize;
                    break;
                }
                break;
            case 0:
                for (i = 0; i < childCount; i++) {
                    if (i != 0) {
                        with = (int) (((float) with) + this.mType.horizontal_Space);
                    }
                    with += getChildAt(i).getMeasuredWidth();
                }
                with += getPaddingLeft() + getPaddingRight();
                break;
            case 1073741824:
                with = withSize;
                break;
            default:
                with = withSize;
                break;
        }
        WarpLine warpLine = new WarpLine();
        this.mWarpLineGroup = new ArrayList();
        for (i = 0; i < childCount; i++) {
            if (((float) (warpLine.lineWidth + getChildAt(i).getMeasuredWidth())) + this.mType.horizontal_Space <= ((float) with)) {
                warpLine.addView(getChildAt(i));
            } else if (warpLine.lineView.size() == 0) {
                warpLine.addView(getChildAt(i));
                this.mWarpLineGroup.add(warpLine);
                warpLine = new WarpLine();
            } else {
                this.mWarpLineGroup.add(warpLine);
                warpLine = new WarpLine();
                warpLine.addView(getChildAt(i));
            }
        }
        if (warpLine.lineView.size() > 0 && !this.mWarpLineGroup.contains(warpLine)) {
            this.mWarpLineGroup.add(warpLine);
        }
        int height = getPaddingTop() + getPaddingBottom();
        for (i = 0; i < this.mWarpLineGroup.size(); i++) {
            if (i != 0) {
                height = (int) (((float) height) + this.mType.vertical_Space);
            }
            height += ((WarpLine) this.mWarpLineGroup.get(i)).height;
        }
        switch (heightMode) {
            case Integer.MIN_VALUE:
                if (height > heightSize) {
                    height = heightSize;
                    break;
                }
                break;
            case 1073741824:
                height = heightSize;
                break;
        }
        setMeasuredDimension(with, height);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        t = getPaddingTop();
        for (int i = 0; i < this.mWarpLineGroup.size(); i++) {
            int left = getPaddingLeft();
            WarpLine warpLine = (WarpLine) this.mWarpLineGroup.get(i);
            int lastWidth = getMeasuredWidth() - warpLine.lineWidth;
            for (int j = 0; j < warpLine.lineView.size(); j++) {
                float f;
                float measuredWidth;
                float size;
                View view = (View) warpLine.lineView.get(j);
                if (isFull()) {
                    view.layout(left, t, (view.getMeasuredWidth() + left) + (lastWidth / warpLine.lineView.size()), view.getMeasuredHeight() + t);
                    f = (float) left;
                    measuredWidth = ((float) view.getMeasuredWidth()) + this.mType.horizontal_Space;
                    size = (float) (lastWidth / warpLine.lineView.size());
                } else {
                    switch (getGrivate()) {
                        case 0:
                            view.layout(left + lastWidth, t, (left + lastWidth) + view.getMeasuredWidth(), view.getMeasuredHeight() + t);
                            break;
                        case 2:
                            view.layout((lastWidth / 2) + left, t, ((lastWidth / 2) + left) + view.getMeasuredWidth(), view.getMeasuredHeight() + t);
                            break;
                        default:
                            view.layout(left, t, view.getMeasuredWidth() + left, view.getMeasuredHeight() + t);
                            break;
                    }
                    f = (float) left;
                    measuredWidth = (float) view.getMeasuredWidth();
                    size = this.mType.horizontal_Space;
                }
                left = (int) (f + (measuredWidth + size));
            }
            t = (int) (((float) t) + (((float) warpLine.height) + this.mType.vertical_Space));
        }
    }

    public int getGrivate() {
        return this.mType.grivate;
    }

    public float getHorizontal_Space() {
        return this.mType.horizontal_Space;
    }

    public float getVertical_Space() {
        return this.mType.vertical_Space;
    }

    public boolean isFull() {
        return this.mType.isFull;
    }

    public void setGrivate(int grivate) {
        this.mType.grivate = grivate;
    }

    public void setHorizontal_Space(float horizontal_Space) {
        this.mType.horizontal_Space = horizontal_Space;
    }

    public void setVertical_Space(float vertical_Space) {
        this.mType.vertical_Space = vertical_Space;
    }

    public void setIsFull(boolean isFull) {
        this.mType.isFull = isFull;
    }
}
