package com.tvtaobao.android.marketgames.dfw.dlg;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.tvtaobao.android.marketgames.R;
import com.tvtaobao.android.ui3.widget.FullScreenDialog;

public class GameDlgRule extends FullScreenDialog {
    private ConstraintLayout constraintLayout;
    private TextView content;
    private IDataProvider dataProvider;
    private EventListener eventListener;
    private LinearLayout linearLayout;
    private ScrollView scrollView;
    private TextView title;

    public interface EventListener {
        void onDlgDismiss();

        void onDlgShow();
    }

    public interface IDataProvider {
        String getBgColor();

        String getRuleContent();

        String getRuleContentColor();

        String getTitle();

        String getTitleColor();
    }

    private void findViews() {
        this.constraintLayout = (ConstraintLayout) findViewById(R.id.constraint_layout);
        this.scrollView = (ScrollView) findViewById(R.id.scroll_view);
        this.linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        this.title = (TextView) findViewById(R.id.title);
        this.content = (TextView) findViewById(R.id.content);
    }

    public GameDlgRule(Context context) {
        super(context);
        findViews();
        setCancelable(true);
    }

    public View onCreateView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.marketgames_dialog_rule, (ViewGroup) null);
    }

    public void show() {
        super.show(true);
        if (this.eventListener != null) {
            this.eventListener.onDlgShow();
        }
    }

    public void dismiss() {
        super.dismiss();
        if (this.eventListener != null) {
            this.eventListener.onDlgDismiss();
        }
    }

    public IDataProvider getDataProvider() {
        return this.dataProvider;
    }

    public GameDlgRule setDataProvider(IDataProvider IDataProvider2) {
        this.dataProvider = IDataProvider2;
        return this;
    }

    public EventListener getEventListener() {
        return this.eventListener;
    }

    public GameDlgRule setEventListener(EventListener eventListener2) {
        this.eventListener = eventListener2;
        return this;
    }

    public ScrollView getScrollView() {
        return this.scrollView;
    }

    public LinearLayout getLinearLayout() {
        return this.linearLayout;
    }

    public TextView getTitle() {
        return this.title;
    }

    public TextView getContent() {
        return this.content;
    }

    public GameDlgRule apply() {
        this.title.setVisibility(4);
        this.content.setVisibility(4);
        if (this.dataProvider != null) {
            try {
                this.constraintLayout.setBackgroundColor(Color.parseColor(this.dataProvider.getBgColor()));
            } catch (Throwable th) {
            }
            if (!TextUtils.isEmpty(this.dataProvider.getTitle())) {
                this.title.setText(this.dataProvider.getTitle());
                this.title.setVisibility(0);
                try {
                    this.title.setTextColor(Color.parseColor(this.dataProvider.getTitleColor()));
                } catch (Throwable th2) {
                }
            }
            if (!TextUtils.isEmpty(this.dataProvider.getRuleContent())) {
                this.content.setText(this.dataProvider.getRuleContent());
                this.content.setVisibility(0);
                try {
                    this.content.setTextColor(Color.parseColor(this.dataProvider.getRuleContentColor()));
                } catch (Throwable th3) {
                }
            }
        }
        return this;
    }
}
