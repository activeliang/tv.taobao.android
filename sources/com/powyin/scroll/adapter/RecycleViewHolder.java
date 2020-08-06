package com.powyin.scroll.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

class RecycleViewHolder<T> extends RecyclerView.ViewHolder {
    PowViewHolder<T> mPowViewHolder;

    RecycleViewHolder(View itemView, PowViewHolder<T> powViewHolder) {
        super(itemView);
        this.mPowViewHolder = powViewHolder;
    }

    /* access modifiers changed from: package-private */
    public void onViewAttachedToWindow() {
        if (this.mPowViewHolder != null) {
            this.mPowViewHolder.onViewAttachedToWindow();
        }
    }

    /* access modifiers changed from: package-private */
    public void onViewDetachedFromWindow() {
        if (this.mPowViewHolder != null) {
            this.mPowViewHolder.onViewDetachedFromWindow();
        }
    }
}
