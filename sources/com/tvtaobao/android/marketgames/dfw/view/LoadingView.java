package com.tvtaobao.android.marketgames.dfw.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.constraint.ConstraintLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.tvtaobao.android.marketgames.R;
import com.tvtaobao.android.marketgames.dfw.wares.IBoGameData;
import com.tvtaobao.android.marketgames.dfw.wares.IImageLoader;
import com.tvtaobao.android.ui3.widget.SimpleCardView;
import java.util.List;

public class LoadingView extends FrameLayout {
    Runnable applyTask = new Runnable() {
        public void run() {
            if (LoadingView.this.imgLoaderProvider != null && LoadingView.this.dataProvider != null) {
                try {
                    LoadingView.this.constraintLayout.setBackgroundColor(Color.parseColor(LoadingView.this.dataProvider.getBgColor()));
                } catch (Throwable th) {
                }
                if (LoadingView.this.dataProvider.getLoadProgressBarColor() != null) {
                    LoadingView.this.percentBarView.setColors(LoadingView.this.dataProvider.getLoadProgressBarColor());
                }
                LoadingView.this.imgLoaderProvider.load(LoadingView.this.getContext(), LoadingView.this.dataProvider.getBgImgUrl(), new IImageLoader.ImgLoadListener() {
                    public void onSuccess(Bitmap bitmap) {
                        LoadingView.this.bg.setImageBitmap(bitmap);
                        if (LoadingView.this.bgImgLoadListener != null) {
                            LoadingView.this.bgImgLoadListener.onBgLoadSuccess();
                        }
                    }

                    public void onFailed() {
                        if (LoadingView.this.bgImgLoadListener != null) {
                            LoadingView.this.bgImgLoadListener.onBgLoadFailed();
                        }
                    }
                });
            }
        }
    };
    /* access modifiers changed from: private */
    public ImageView bg;
    /* access modifiers changed from: private */
    public BgImgLoadListener bgImgLoadListener;
    /* access modifiers changed from: private */
    public ConstraintLayout constraintLayout;
    private View contentView = LayoutInflater.from(getContext()).inflate(R.layout.marketgames_dialog_loading, this, true);
    /* access modifiers changed from: private */
    public IBoGameData.IBoLoadData dataProvider;
    /* access modifiers changed from: private */
    public IImageLoader imgLoaderProvider;
    private SimpleCardView loadProgressBarArea;
    /* access modifiers changed from: private */
    public TextView loadProgressTxt;
    /* access modifiers changed from: private */
    public PercentBarView percentBarView;

    public interface BgImgLoadListener {
        void onBgLoadFailed();

        void onBgLoadSuccess();
    }

    public LoadingView(Context context, IBoGameData.IBoLoadData dataProvider2, IImageLoader imgLoaderProvider2) {
        super(context);
        findViews();
        setDataProvider(dataProvider2);
        setImgLoaderProvider(imgLoaderProvider2);
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

    private void findViews() {
        this.constraintLayout = (ConstraintLayout) findViewById(R.id.constraint_layout);
        this.bg = (ImageView) findViewById(R.id.bg);
        this.loadProgressBarArea = (SimpleCardView) findViewById(R.id.load_progress_bar_area);
        this.loadProgressTxt = (TextView) findViewById(R.id.load_progress_txt);
        this.percentBarView = new PercentBarView(getContext());
        this.loadProgressBarArea.addView(this.percentBarView, new ViewGroup.LayoutParams(-1, -1));
    }

    public View getContentView() {
        return this.contentView;
    }

    public ConstraintLayout getConstraintLayout() {
        return this.constraintLayout;
    }

    public ImageView getBg() {
        return this.bg;
    }

    public SimpleCardView getLoadProgressBarArea() {
        return this.loadProgressBarArea;
    }

    public TextView getLoadProgressTxt() {
        return this.loadProgressTxt;
    }

    public PercentBarView getPercentBarView() {
        return this.percentBarView;
    }

    public BgImgLoadListener getBgImgLoadListener() {
        return this.bgImgLoadListener;
    }

    public void setBgImgLoadListener(BgImgLoadListener bgImgLoadListener2) {
        this.bgImgLoadListener = bgImgLoadListener2;
    }

    public void setDataProvider(IBoGameData.IBoLoadData dataProvider2) {
        this.dataProvider = dataProvider2;
        apply();
    }

    public void setImgLoaderProvider(IImageLoader imgLoaderProvider2) {
        this.imgLoaderProvider = imgLoaderProvider2;
        apply();
    }

    public void apply() {
        this.bg.removeCallbacks(this.applyTask);
        this.bg.post(this.applyTask);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    public void setPercent(float percent) {
        if (this.percentBarView != null) {
            float unused = this.percentBarView.percent = percent;
            this.loadProgressTxt.setText(((int) percent) + "%");
            this.percentBarView.postInvalidate();
        }
    }

    public float getPercent() {
        if (this.percentBarView != null) {
            return this.percentBarView.percent;
        }
        return 0.0f;
    }

    private class PercentBarView extends View {
        private int[] clrs = new int[2];
        private LinearGradient linearGradient;
        private Paint paint = new Paint();
        /* access modifiers changed from: private */
        public float percent = 0.0f;
        private float[] poss = new float[2];
        private RectF rect = new RectF();

        public PercentBarView(Context context) {
            super(context);
            this.paint.setAntiAlias(true);
            this.paint.setStyle(Paint.Style.FILL);
            this.clrs[0] = Color.parseColor("#00FF5E4D");
            this.clrs[1] = Color.parseColor("#00F62947");
            this.poss[0] = 0.0f;
            this.poss[1] = 1.0f;
        }

        /* access modifiers changed from: package-private */
        public void setColors(List<String> clrStr) {
            if (clrStr != null && clrStr.size() >= 2) {
                try {
                    this.clrs[0] = Color.parseColor(clrStr.get(0));
                } catch (Throwable th) {
                }
                try {
                    this.clrs[1] = Color.parseColor(clrStr.get(1));
                } catch (Throwable th2) {
                }
                this.linearGradient = null;
                LoadingView.this.loadProgressTxt.setVisibility(0);
            }
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.save();
            try {
                if (this.linearGradient == null) {
                    this.linearGradient = new LinearGradient(0.0f, 0.0f, (float) getWidth(), 0.0f, this.clrs, this.poss, Shader.TileMode.CLAMP);
                }
                this.paint.setShader(this.linearGradient);
                this.rect.set(0.0f, 0.0f, (float) ((int) (((float) getWidth()) * (this.percent / 100.0f))), (float) getHeight());
                canvas.drawRoundRect(this.rect, (float) (getHeight() / 2), (float) (getHeight() / 2), this.paint);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            canvas.restore();
        }
    }
}
