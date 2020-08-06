package com.tvtaobao.android.ui3.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tvtaobao.android.ui3.R;

public class NetWorkErrorView extends ConstraintLayout {
    private ImageView ivIcon;
    /* access modifiers changed from: private */
    public OnRefreshListener onRefreshListener;
    private LinearLayout rlView;
    private TextView tvInfoNetwork;
    private TextView tvRefresh;

    public interface OnRefreshListener {
        void onRefresh();
    }

    public NetWorkErrorView(Context context) {
        this(context, (AttributeSet) null);
    }

    public NetWorkErrorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetWorkErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(getContext()).inflate(R.layout.ui3wares_layout_network_error_view, this, true);
        this.rlView = (LinearLayout) findViewById(R.id.rl_view);
        this.ivIcon = (ImageView) findViewById(R.id.iv_icon);
        this.tvInfoNetwork = (TextView) findViewById(R.id.tv_info_network);
        this.tvRefresh = (TextView) findViewById(R.id.tv_refresh);
        this.tvRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (NetWorkErrorView.this.onRefreshListener != null) {
                    NetWorkErrorView.this.onRefreshListener.onRefresh();
                }
            }
        });
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ui3wares_ErrorView_Network);
        if (ta != null && context != null) {
            int networkErrorSrc = ta.getResourceId(R.styleable.ui3wares_ErrorView_Network_ui3wares_networkErrorSrc, R.drawable.ui3wares_error_newwork);
            String networkInfo = ta.getString(R.styleable.ui3wares_ErrorView_Network_ui3wares_networkInfo);
            String networkButtonText = ta.getString(R.styleable.ui3wares_ErrorView_Network_ui3wares_networkButtonText);
            setIcon(networkErrorSrc);
            if (!TextUtils.isEmpty(networkInfo)) {
                setTvInfoNetwork(networkInfo);
            }
            if (!TextUtils.isEmpty(networkInfo)) {
                setTvRefresh(networkButtonText);
            }
            if (ta != null) {
                ta.recycle();
            }
        }
    }

    public void setIcon(int iconResId) {
        if (this.ivIcon != null) {
            this.ivIcon.setImageResource(iconResId);
        }
    }

    public void setTvInfoNetwork(String tvInfoNewworkText) {
        if (this.tvInfoNetwork != null) {
            this.tvInfoNetwork.setText(tvInfoNewworkText);
        }
    }

    public void setTvRefresh(String tvRefreshText) {
        if (this.tvRefresh != null) {
            this.tvRefresh.setText(tvRefreshText);
        }
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener2) {
        this.onRefreshListener = onRefreshListener2;
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        this.tvRefresh.requestFocus();
    }
}
