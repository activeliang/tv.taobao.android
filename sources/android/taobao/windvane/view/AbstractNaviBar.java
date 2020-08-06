package android.taobao.windvane.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public abstract class AbstractNaviBar extends RelativeLayout {
    public static final int NAVI_BAR_ID = 110;

    public abstract void resetState();

    public abstract void startLoading();

    public abstract void stopLoading();

    public AbstractNaviBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AbstractNaviBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbstractNaviBar(Context context) {
        super(context);
    }
}
