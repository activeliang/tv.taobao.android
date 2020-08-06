package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import com.yunos.tvtaobao.businessview.R;

public class TvTaoBaoProgressBar extends ProgressBar {
    public TvTaoBaoProgressBar(Context context) {
        super(context);
        initTvTaoBaoProgressBar(context);
    }

    public TvTaoBaoProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTvTaoBaoProgressBar(context);
    }

    public TvTaoBaoProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initTvTaoBaoProgressBar(context);
    }

    private void initTvTaoBaoProgressBar(Context context) {
        setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.ytbv_load_animation));
        setIndeterminate(false);
    }
}
