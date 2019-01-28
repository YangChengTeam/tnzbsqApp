package com.fy.tnzbsq.view.builder;

import android.app.PendingIntent;


/**
 * Created by Administrator on 2017/2/13 0013.
 */

public class MediaBuilder extends BaseBuilder {

    @Override
    public void build() {
        super.build();
        cBuilder.setShowWhen(false);
    }

    @Override
    public BaseBuilder addBtn(int icon, CharSequence text, PendingIntent pendingIntent) {
        return super.addBtn(icon, text, pendingIntent);
    }
}
