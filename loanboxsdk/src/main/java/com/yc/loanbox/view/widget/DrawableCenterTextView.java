package com.yc.loanbox.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class DrawableCenterTextView extends AppCompatTextView {
    public DrawableCenterTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DrawableCenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableCenterTextView(Context context) {
        super(context);
    }

    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            Drawable drawableLeft = drawables[2];
            if (drawableLeft != null) {
                float bodyWidth = (((float) drawableLeft.getIntrinsicWidth()) + getPaint().measureText(getText().toString())) + ((float) getCompoundDrawablePadding());
                setPadding(0, 0, (int) (((float) getWidth()) - bodyWidth), 0);
                canvas.translate((((float) getWidth()) - bodyWidth) / 2.0f, 0.0f);
            }
        }
        super.onDraw(canvas);
    }
}
