package android.taobao.windvane.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

public class PopupWindowController {
    private Animation mAnimation;
    private Context mContext;
    private PopupWindow mPopupWindow;
    private View mView;
    /* access modifiers changed from: private */
    public LinearLayout popupLayout;
    private FrameLayout popupViewGroup;

    public PopupWindowController(Context context, View view, String[] tags, View.OnClickListener listener) {
        this(context, view, (String) null, tags, listener);
    }

    public PopupWindowController(Context context, View view, String title, String[] tags, View.OnClickListener listener) {
        this.mContext = context;
        this.mView = view;
        initButtons(title, tags, listener);
        initPopupWindow();
        this.mAnimation = new TranslateAnimation(2, 0.0f, 2, 0.0f, 1, 1.0f, 1, 0.0f);
        this.mAnimation.setDuration(250);
    }

    public void show() {
        try {
            this.mPopupWindow.showAtLocation(this.mView, 81, 0, 0);
            this.popupLayout.startAnimation(this.mAnimation);
            this.mPopupWindow.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hide() {
        this.mPopupWindow.dismiss();
    }

    private void initPopupWindow() {
        this.mPopupWindow = new PopupWindow(this.mContext);
        this.mPopupWindow.setContentView(this.popupViewGroup);
        this.mPopupWindow.setWidth(-1);
        this.mPopupWindow.setHeight(-1);
        this.mPopupWindow.setFocusable(true);
        this.mPopupWindow.setOutsideTouchable(true);
        this.mPopupWindow.setBackgroundDrawable(new ColorDrawable(2130706432));
        fixPopupWindow(this.mPopupWindow);
    }

    private void initButtons(String title, String[] tags, View.OnClickListener listener) {
        this.popupViewGroup = new FrameLayout(this.mContext);
        this.popupViewGroup.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        this.popupLayout = new LinearLayout(this.mContext);
        this.popupLayout.setOrientation(1);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -2);
        params.gravity = 80;
        this.popupLayout.setLayoutParams(params);
        this.popupLayout.setBackgroundColor(-7829368);
        LinearLayout.LayoutParams listParams = new LinearLayout.LayoutParams(-1, -2);
        if (!TextUtils.isEmpty(title)) {
            Button _titleView = new Button(this.mContext);
            _titleView.setText(title);
            _titleView.setBackgroundColor(-657931);
            _titleView.setTextColor(-7829368);
            _titleView.setLayoutParams(listParams);
            this.popupLayout.addView(_titleView);
            TextView line = new TextView(this.mContext);
            line.setHeight(4);
            this.popupLayout.addView(line);
        }
        if (tags != null) {
            for (String tag : tags) {
                Button _btn = new Button(this.mContext);
                _btn.setText(tag);
                _btn.setTag(tag);
                _btn.setBackgroundColor(-657931);
                _btn.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                _btn.setLayoutParams(listParams);
                _btn.setOnClickListener(listener);
                this.popupLayout.addView(_btn);
                TextView line2 = new TextView(this.mContext);
                line2.setHeight(2);
                this.popupLayout.addView(line2);
            }
        }
        final Button cancelButton = new Button(this.mContext);
        cancelButton.setText("取消");
        cancelButton.setBackgroundColor(-657931);
        cancelButton.setTextColor(-7829368);
        LinearLayout.LayoutParams cancelParams = new LinearLayout.LayoutParams(-1, -2);
        cancelParams.topMargin = 4;
        cancelButton.setLayoutParams(cancelParams);
        this.popupLayout.addView(cancelButton);
        this.popupViewGroup.addView(this.popupLayout);
        cancelButton.setOnClickListener(listener);
        this.popupViewGroup.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = PopupWindowController.this.popupLayout.getTop();
                int y = (int) event.getY();
                if (event.getAction() == 1 && y < height) {
                    cancelButton.performClick();
                }
                return true;
            }
        });
    }

    private void fixPopupWindow(final PopupWindow window) {
        if (Build.VERSION.SDK_INT < 14) {
            try {
                final Field fAnchor = PopupWindow.class.getDeclaredField("mAnchor");
                fAnchor.setAccessible(true);
                Field listener = PopupWindow.class.getDeclaredField("mOnScrollChangedListener");
                listener.setAccessible(true);
                final ViewTreeObserver.OnScrollChangedListener originalListener = (ViewTreeObserver.OnScrollChangedListener) listener.get(window);
                listener.set(window, new ViewTreeObserver.OnScrollChangedListener() {
                    public void onScrollChanged() {
                        try {
                            WeakReference<View> mAnchor = (WeakReference) fAnchor.get(window);
                            if (mAnchor != null && mAnchor.get() != null) {
                                originalListener.onScrollChanged();
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
