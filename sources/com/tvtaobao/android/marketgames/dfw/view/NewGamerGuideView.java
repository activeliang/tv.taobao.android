package com.tvtaobao.android.marketgames.dfw.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.tvtaobao.android.marketgames.R;
import com.tvtaobao.android.marketgames.dfw.wares.IBoGameData;
import com.tvtaobao.android.marketgames.dfw.wares.IImageLoader;

public class NewGamerGuideView extends FrameLayout {
    Runnable applyTask = new Runnable() {
        public void run() {
            if (NewGamerGuideView.this.dataProvider != null) {
                try {
                    NewGamerGuideView.this.constraintLayout.setBackgroundColor(Color.parseColor(NewGamerGuideView.this.dataProvider.getBgColor()));
                } catch (Throwable th) {
                }
            }
            if (NewGamerGuideView.this.imgLoaderProvider != null && NewGamerGuideView.this.dataProvider != null) {
                NewGamerGuideView.this.imgLoaderProvider.loadInto(NewGamerGuideView.this.getContext(), NewGamerGuideView.this.dataProvider.getBgImgUrl(), NewGamerGuideView.this.bg);
            }
        }
    };
    /* access modifiers changed from: private */
    public ImageView bg;
    /* access modifiers changed from: private */
    public ConstraintLayout constraintLayout;
    private View contentView = LayoutInflater.from(getContext()).inflate(R.layout.marketgames_dialog_guide, this, true);
    /* access modifiers changed from: private */
    public IBoGameData.IBoGuideData dataProvider;
    /* access modifiers changed from: private */
    public IImageLoader imgLoaderProvider;

    public NewGamerGuideView(Context context, IBoGameData.IBoGuideData dataProvider2, IImageLoader imgLoaderProvider2) {
        super(context);
        findViews();
        setDataProvider(dataProvider2);
        setImgLoaderProvider(imgLoaderProvider2);
    }

    private void findViews() {
        this.constraintLayout = (ConstraintLayout) findViewById(R.id.constraint_layout);
        this.bg = (ImageView) findViewById(R.id.bg);
    }

    public View getContentView() {
        return this.contentView;
    }

    public void show(Runnable task) {
        if ((getContext() instanceof Activity) && (((Activity) getContext()).getWindow().getDecorView() instanceof ViewGroup)) {
            ((ViewGroup) ((Activity) getContext()).getWindow().getDecorView()).addView(this, new ViewGroup.LayoutParams(-1, -1));
            this.bg.requestFocus();
            if (task != null) {
                try {
                    task.run();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void dismiss(Runnable task) {
        if ((getContext() instanceof Activity) && (((Activity) getContext()).getWindow().getDecorView() instanceof ViewGroup)) {
            ((ViewGroup) ((Activity) getContext()).getWindow().getDecorView()).removeView(this);
            if (task != null) {
                try {
                    task.run();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setDataProvider(IBoGameData.IBoGuideData dataProvider2) {
        this.dataProvider = dataProvider2;
        apply();
    }

    public void setImgLoaderProvider(IImageLoader imgLoaderProvider2) {
        this.imgLoaderProvider = imgLoaderProvider2;
        apply();
    }

    public ImageView getBg() {
        return this.bg;
    }

    public void apply() {
        this.bg.removeCallbacks(this.applyTask);
        this.bg.post(this.applyTask);
    }
}
