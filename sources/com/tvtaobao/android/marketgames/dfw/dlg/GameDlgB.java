package com.tvtaobao.android.marketgames.dfw.dlg;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.tvtaobao.android.marketgames.R;
import com.tvtaobao.android.marketgames.dfw.act.GameDaFuWengActivity;
import com.tvtaobao.android.ui3.widget.FullScreenDialog;

public class GameDlgB extends FullScreenDialog {
    private ImageView aura;
    private ConstraintLayout constraintLayout;
    private FrameLayout contentContainer;
    private IDataProvider dataProvider;
    private FrameLayout emptyState;
    private IEventListener eventListener;
    private TextView mainTxt;
    private Style style;
    private TextView subTxt;

    public interface IDataProvider {
        String getAuraImgUrl();

        String getBgColor();

        String getEmptyStateMainTxt();

        String getEmptyStateSubTxt();
    }

    public interface IEventListener {
        void onDlgDismiss();

        void onDlgShow();
    }

    public enum Style {
        empty,
        list
    }

    private void findViews() {
        this.constraintLayout = (ConstraintLayout) findViewById(R.id.constraint_layout);
        this.aura = (ImageView) findViewById(R.id.aura);
        this.contentContainer = (FrameLayout) findViewById(R.id.content_container);
        this.emptyState = (FrameLayout) findViewById(R.id.empty_state);
        this.mainTxt = (TextView) findViewById(R.id.main_txt);
        this.subTxt = (TextView) findViewById(R.id.sub_txt);
    }

    public GameDlgB(Context context) {
        super(context);
        findViews();
        setCancelable(true);
        if (context instanceof Activity) {
            setOwnerActivity((Activity) context);
        }
    }

    public View onCreateView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.marketgames_dialog_b, (ViewGroup) null);
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

    public GameDlgB setDataProvider(IDataProvider dataProvider2) {
        this.dataProvider = dataProvider2;
        return this;
    }

    public IEventListener getEventListener() {
        return this.eventListener;
    }

    public GameDlgB setEventListener(IEventListener eventListener2) {
        this.eventListener = eventListener2;
        return this;
    }

    public ImageView getAura() {
        return this.aura;
    }

    public FrameLayout getContentContainer() {
        return this.contentContainer;
    }

    public FrameLayout getEmptyState() {
        return this.emptyState;
    }

    public TextView getMainTxt() {
        return this.mainTxt;
    }

    public TextView getSubTxt() {
        return this.subTxt;
    }

    public Style getStyle() {
        return this.style;
    }

    public GameDlgB setStyle(Style style2) {
        this.style = style2;
        return this;
    }

    public GameDlgB apply() {
        this.aura.setVisibility(4);
        this.contentContainer.setVisibility(4);
        this.emptyState.setVisibility(4);
        if (this.dataProvider != null) {
            try {
                this.constraintLayout.setBackgroundColor(Color.parseColor(this.dataProvider.getBgColor()));
            } catch (Throwable th) {
            }
            if (!(this.dataProvider.getAuraImgUrl() == null || !(getOwnerActivity() instanceof GameDaFuWengActivity) || ((GameDaFuWengActivity) getOwnerActivity()).getDfwLauncher() == null || ((GameDaFuWengActivity) getOwnerActivity()).getDfwLauncher().imageLoader == null)) {
                ((GameDaFuWengActivity) getOwnerActivity()).getDfwLauncher().imageLoader.loadInto(getOwnerActivity(), this.dataProvider.getAuraImgUrl(), this.aura);
                this.aura.setVisibility(0);
            }
            if (!TextUtils.isEmpty(this.dataProvider.getEmptyStateMainTxt())) {
                this.mainTxt.setText(this.dataProvider.getEmptyStateMainTxt());
            }
            if (!TextUtils.isEmpty(this.dataProvider.getEmptyStateSubTxt())) {
                this.subTxt.setText(this.dataProvider.getEmptyStateSubTxt());
            }
        }
        if (this.style != null) {
            if (this.style == Style.empty) {
                this.contentContainer.setVisibility(4);
                this.emptyState.setVisibility(0);
            } else if (this.style == Style.list) {
                this.contentContainer.setVisibility(0);
                this.emptyState.setVisibility(4);
            }
        }
        return this;
    }
}
