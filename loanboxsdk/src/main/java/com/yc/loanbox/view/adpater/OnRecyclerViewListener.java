package com.yc.loanbox.view.adpater;

import android.view.View;

public interface OnRecyclerViewListener {
    void onItemClick(View view, int i);

    boolean onItemLongClick(View view, int i);

}
