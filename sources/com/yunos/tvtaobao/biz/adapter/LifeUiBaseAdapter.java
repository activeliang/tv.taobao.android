package com.yunos.tvtaobao.biz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.zhiping.dev.android.logger.ZpLogger;

public abstract class LifeUiBaseAdapter extends BaseAdapter {
    private String TAG = "LifeUiBaseAdapter";
    protected Context mContext = null;
    private boolean mFirstNewconvertView = false;
    private LayoutInflater mLayoutInflater = null;
    private View mMakeFirstView = null;

    public abstract void fillView(int i, View view, boolean z, ViewGroup viewGroup);

    public abstract int getLayoutID();

    public LifeUiBaseAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        boolean newconvertView = false;
        if (convertView == null) {
            convertView = this.mLayoutInflater.inflate(getLayoutID(), (ViewGroup) null);
            newconvertView = true;
        }
        ZpLogger.i(this.TAG, "getView  -->  position = " + position + "; convertView = " + convertView + "; LifeUiBaseAdapter.this = " + this);
        if (getCount() > 2) {
            if (position == 0) {
                this.mFirstNewconvertView = newconvertView;
                this.mMakeFirstView = convertView;
                return convertView;
            } else if (position == 1) {
                fillView(0, this.mMakeFirstView, this.mFirstNewconvertView, parent);
            }
        }
        fillView(position, convertView, newconvertView, parent);
        return convertView;
    }
}
