package android.taobao.windvane.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WebWaitingView extends RelativeLayout {
    private static final int LOADING_BG_ID = 101;
    private static final int LOADING_PGBAR_ID = 102;

    public WebWaitingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public WebWaitingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WebWaitingView(Context context) {
        super(context);
        init(context);
    }

    @TargetApi(16)
    private void init(Context context) {
        float scale = context.getResources().getDisplayMetrics().density;
        View bgView = new View(context);
        bgView.setId(101);
        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(scale * 20.0f);
        gd.setColor(-12303292);
        gd.setAlpha(150);
        if (Build.VERSION.SDK_INT >= 16) {
            bgView.setBackground(gd);
        } else {
            bgView.setBackgroundDrawable(gd);
        }
        int size = (int) (120.0f * scale);
        RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams(size, size);
        param1.addRule(13);
        addView(bgView, param1);
        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setId(102);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -2);
        params.addRule(14);
        params.addRule(6, 101);
        params.topMargin = ((int) (20.0f * scale)) + 10;
        addView(progressBar, params);
        TextView txt = new TextView(context);
        txt.setText("正在加载");
        txt.setTextColor(-1);
        RelativeLayout.LayoutParams paramTxt = new RelativeLayout.LayoutParams(-2, -2);
        paramTxt.addRule(14);
        paramTxt.addRule(3, 102);
        addView(txt, paramTxt);
    }
}
