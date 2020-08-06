package com.yunos.tvtaobao.biz.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.tvlife.imageloader.core.DisplayImageOptions;
import com.tvlife.imageloader.core.assist.FailReason;
import com.tvlife.imageloader.core.listener.ImageLoadingListener;
import com.yunos.tv.core.common.ImageHandleManager;
import com.yunos.tv.core.common.ImageLoaderManager;
import com.yunos.tvtaobao.biz.listener.VerticalItemHandleListener;
import com.yunos.tvtaobao.biz.request.info.GlobalConfigInfo;
import com.yunos.tvtaobao.biz.widget.TabFlipGridViewHeaderView;
import com.yunos.tvtaobao.biz.widget.TabGoodsItemView;
import com.yunos.tvtaobao.businessview.R;
import com.yunos.tvtaobao.tvsdk.widget.focus.FocusFlipGridView;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;

public abstract class TabGoodsBaseAdapter extends BaseAdapter {
    public static final String TAG = "TabGoodsBaseAdapter";
    private Context mContext;
    protected int mCornerRadius = this.mContext.getResources().getDimensionPixelSize(R.dimen.dp_0);
    /* access modifiers changed from: private */
    public FocusFlipGridView mFocusFlipGridView;
    private boolean mHaveCheckBitmapLoading = false;
    private boolean mHaveHeaderView;
    private ImageLoaderManager mImageLoaderManager = ImageLoaderManager.get();
    private DisplayImageOptions mImageOptions;
    private boolean mIsNotifyDataSetChanged = false;
    /* access modifiers changed from: private */
    public Handler mMainHandler;
    private int mOldRow = 0;
    protected String mTabKey;
    private VerticalItemHandleListener mVerticalItemHandleListener;

    public abstract int getColumnsCounts();

    public abstract Drawable getDefaultDisplayPicture();

    public abstract View getFillView(int i, View view, ViewGroup viewGroup);

    public abstract String getGoodsTitle(String str, int i);

    public abstract Drawable getInfoDiaplayDrawable(String str, int i);

    public abstract String getNetPicUrl(String str, int i);

    public TabGoodsBaseAdapter(Context context, boolean haveHeaderView) {
        boolean beta;
        boolean z = true;
        this.mContext = context;
        this.mHaveHeaderView = haveHeaderView;
        if (GlobalConfigInfo.getInstance().getGlobalConfig() == null || !GlobalConfigInfo.getInstance().getGlobalConfig().isBeta()) {
            beta = false;
        } else {
            beta = true;
        }
        this.mImageOptions = new DisplayImageOptions.Builder().cacheOnDisc(beta).cacheInMemory(beta ? false : z).build();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View resultView = getFillView(position, convertView, parent);
        ZpLogger.i(TAG, "getView  -->  position = " + position + "; convertView = " + convertView + "; resultView = " + resultView);
        if (resultView instanceof TabGoodsItemView) {
            commonfillView(position, (TabGoodsItemView) resultView, parent);
        }
        return resultView;
    }

    public void setOnVerticalItemHandleListener(VerticalItemHandleListener l) {
        this.mVerticalItemHandleListener = l;
    }

    public void setIsNotifyDataSetChanged(boolean isNotifyDataSetChanged) {
        this.mIsNotifyDataSetChanged = isNotifyDataSetChanged;
    }

    public void onSetMainHandler(Handler handler) {
        ZpLogger.i(TAG, "onSetMainHandler   handler = " + handler);
        this.mMainHandler = handler;
    }

    public void onSetTabKey(String tabKey) {
        this.mTabKey = tabKey;
    }

    public String onGetTabKey() {
        return this.mTabKey;
    }

    public void onSetFocusFlipGridView(FocusFlipGridView focusFlipGridView) {
        this.mFocusFlipGridView = focusFlipGridView;
    }

    public int onGetCornerRadius() {
        return this.mCornerRadius;
    }

    public int getOldRow() {
        return this.mOldRow;
    }

    public boolean isHaveHeaderView() {
        return this.mHaveHeaderView;
    }

    public void onNotifyDataSetChanged() {
        notifyDataSetChanged();
    }

    private void commonfillView(int position, TabGoodsItemView convertView, ViewGroup parent) {
        TabGoodsItemView goodListItemFrameLayout = convertView;
        ZpLogger.i(TAG, "commonfillView   position = " + position + ";  convertView = " + convertView);
        if (goodListItemFrameLayout != null) {
            goodListItemFrameLayout.onSetTabKey(this.mTabKey);
            goodListItemFrameLayout.onSetPosition(position);
            if (!this.mIsNotifyDataSetChanged) {
                goodListItemFrameLayout.onHideInfoImageView();
                goodListItemFrameLayout.onSetGoodsListDefaultDrawable(getDefaultDisplayPicture(), position);
            }
            if ((!this.mHaveCheckBitmapLoading && position < getColumnsCounts() * 2) || this.mIsNotifyDataSetChanged) {
                onRequestInfoOfItem(goodListItemFrameLayout, this.mTabKey, position);
                onRequestImageOfItem(goodListItemFrameLayout, this.mTabKey, position);
            }
            ZpLogger.i(TAG, "commonfillView   position = " + position + ";  goodListItemFrameLayout = " + goodListItemFrameLayout + "; mIsNotifyDataSetChanged = " + this.mIsNotifyDataSetChanged + "; mHaveCheckBitmapLoading = " + this.mHaveCheckBitmapLoading + "; mTabKey = " + this.mTabKey);
        }
    }

    public void onSetCheckVisibleItem(boolean check) {
        this.mHaveCheckBitmapLoading = check;
    }

    public void onCheckVisibleItemAndLoadBitmap() {
        ImageHandleManager.getImageHandleManager(this.mContext).executeTask(new LoadBitmapRunnable(new WeakReference(this)));
    }

    public void onDisplayHandleInfo(TabGoodsItemView fenLeiItemView, int position) {
        ImageHandleManager.getImageHandleManager(this.mContext).executeTask(new DisplayInfoRunnable(new WeakReference(this), new WeakReference(fenLeiItemView), position));
    }

    public void onDisplayHandleGoods(TabGoodsItemView fenLeiItemView, int position, Bitmap bm) {
        ImageHandleManager.getImageHandleManager(this.mContext).executeTask(new DisplayGoodsBitmapRunnable(new WeakReference(this), new WeakReference(fenLeiItemView), position, new WeakReference(bm)));
    }

    public void onItemSelected(int position, boolean isSelected, View fatherView) {
        ZpLogger.i(TAG, "onGetview  ---->  position = " + position + ";  isSelected = " + isSelected + ", fatherView = " + fatherView);
        if (isSelected) {
            if (isHaveHeaderView()) {
                position--;
            }
            if (position >= 0) {
                int currentRow = position / getColumnsCounts();
                if (!(this.mOldRow == currentRow || this.mVerticalItemHandleListener == null)) {
                    this.mVerticalItemHandleListener.onGetview(this.mFocusFlipGridView, this.mTabKey, position);
                }
                this.mOldRow = currentRow;
            }
        }
    }

    public void onClearAndDestroy() {
    }

    public void onRequestInfoOfItem(TabGoodsItemView fenLeiItemView, String ordey, int position) {
        ZpLogger.i(TAG, "onRequestInfoOfItem   position = " + position);
        fenLeiItemView.setHideInfoDrawable(false);
        onDisplayHandleInfo(fenLeiItemView, position);
    }

    public void onRequestImageOfItem(TabGoodsItemView fenLeiItemView, String tabkey, int position) {
        String mPicUrl = getNetPicUrl(tabkey, position);
        ZpLogger.i(TAG, "onRequestImageOfItem   position =  " + position + "; mPicUrl = " + mPicUrl + "; mImageLoaderManager = " + this.mImageLoaderManager);
        if (this.mImageLoaderManager != null && fenLeiItemView != null) {
            fenLeiItemView.setDefaultDrawable(false);
            this.mImageLoaderManager.loadImage(mPicUrl, this.mImageOptions, new GoodsImageLoadingListener(new WeakReference(this), new WeakReference(fenLeiItemView), position));
        }
    }

    private static class GoodsImageLoadingListener implements ImageLoadingListener {
        private final WeakReference<TabGoodsItemView> mFenLeiItemViewRef;
        private final int mPosition;
        protected final WeakReference<TabGoodsBaseAdapter> weakReference;

        public GoodsImageLoadingListener(WeakReference<TabGoodsBaseAdapter> weakReference2, WeakReference<TabGoodsItemView> fenLeiItemViewRef, int position) {
            this.weakReference = weakReference2;
            this.mFenLeiItemViewRef = fenLeiItemViewRef;
            this.mPosition = position;
        }

        public void onLoadingCancelled(String arg0, View arg1) {
            ZpLogger.i(TabGoodsBaseAdapter.TAG, "GoodsImageLoadingListener ---> onLoadingCancelled -->  mPosition =  " + this.mPosition + ";    mPicUrl = " + arg0);
        }

        public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
            TabGoodsBaseAdapter fenLeiGoodsAdapter;
            ZpLogger.i(TabGoodsBaseAdapter.TAG, "GoodsImageLoadingListener ---> onLoadingComplete -->  mPosition =  " + this.mPosition + ";    mPicUrl = " + arg0);
            TabGoodsItemView fenLeiItemView = null;
            if (this.mFenLeiItemViewRef != null) {
                fenLeiItemView = (TabGoodsItemView) this.mFenLeiItemViewRef.get();
            }
            if (this.weakReference != null && (fenLeiGoodsAdapter = (TabGoodsBaseAdapter) this.weakReference.get()) != null) {
                fenLeiGoodsAdapter.onDisplayHandleGoods(fenLeiItemView, this.mPosition, arg2);
            }
        }

        public void onLoadingStarted(String arg0, View arg1) {
            ZpLogger.i(TabGoodsBaseAdapter.TAG, "GoodsImageLoadingListener ---> onLoadingStarted -->  mPosition =  " + this.mPosition + ";    mPicUrl = " + arg0);
        }

        public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
            ZpLogger.i(TabGoodsBaseAdapter.TAG, "GoodsImageLoadingListener ---> onLoadingFailed -->  mPosition =  " + this.mPosition + ";    mPicUrl = " + arg0);
        }
    }

    private static class DisplayGoodsBitmapRunnable implements Runnable {
        /* access modifiers changed from: private */
        public final WeakReference<TabGoodsItemView> mFenLeiItemViewRef;
        private final WeakReference<Bitmap> mNetBmRef;
        /* access modifiers changed from: private */
        public final int mPosition;
        protected final WeakReference<TabGoodsBaseAdapter> weakReference;

        public DisplayGoodsBitmapRunnable(WeakReference<TabGoodsBaseAdapter> weakReference2, WeakReference<TabGoodsItemView> fenLeiItemViewRef, int position, WeakReference<Bitmap> netBmRef) {
            this.mPosition = position;
            this.mNetBmRef = netBmRef;
            this.mFenLeiItemViewRef = fenLeiItemViewRef;
            this.weakReference = weakReference2;
        }

        public void run() {
            Bitmap mNetBm;
            TabGoodsBaseAdapter tabGoodsBaseAdapter = (TabGoodsBaseAdapter) this.weakReference.get();
            if (tabGoodsBaseAdapter != null && this.mNetBmRef != null && (mNetBm = (Bitmap) this.mNetBmRef.get()) != null) {
                final BitmapDrawable drawable = new BitmapDrawable(mNetBm);
                Handler mtabHandler = tabGoodsBaseAdapter.mMainHandler;
                if (mtabHandler != null) {
                    mtabHandler.post(new Runnable() {
                        public void run() {
                            TabGoodsItemView fenLeiItemView;
                            if (DisplayGoodsBitmapRunnable.this.mFenLeiItemViewRef != null && (fenLeiItemView = (TabGoodsItemView) DisplayGoodsBitmapRunnable.this.mFenLeiItemViewRef.get()) != null) {
                                fenLeiItemView.onSetGoodsListDrawable(drawable, DisplayGoodsBitmapRunnable.this.mPosition);
                            }
                        }
                    });
                }
            }
        }
    }

    private static class DisplayInfoRunnable implements Runnable {
        /* access modifiers changed from: private */
        public final WeakReference<TabGoodsItemView> mFenLeiItemViewRef;
        /* access modifiers changed from: private */
        public final int mPosition;
        protected final WeakReference<TabGoodsBaseAdapter> weakReference;

        public DisplayInfoRunnable(WeakReference<TabGoodsBaseAdapter> weakReference2, WeakReference<TabGoodsItemView> fenLeiItemViewRef, int position) {
            this.mPosition = position;
            this.mFenLeiItemViewRef = fenLeiItemViewRef;
            this.weakReference = weakReference2;
        }

        public void run() {
            TabGoodsBaseAdapter tabGoodsBaseAdapter = (TabGoodsBaseAdapter) this.weakReference.get();
            if (tabGoodsBaseAdapter != null) {
                final Drawable drawable = tabGoodsBaseAdapter.getInfoDiaplayDrawable(tabGoodsBaseAdapter.mTabKey, this.mPosition);
                Handler mtabHandler = tabGoodsBaseAdapter.mMainHandler;
                if (mtabHandler != null && drawable != null) {
                    mtabHandler.post(new Runnable() {
                        public void run() {
                            TabGoodsItemView fenLeiItemView;
                            if (DisplayInfoRunnable.this.mFenLeiItemViewRef != null && (fenLeiItemView = (TabGoodsItemView) DisplayInfoRunnable.this.mFenLeiItemViewRef.get()) != null) {
                                fenLeiItemView.onSetInfoListDrawable(drawable, DisplayInfoRunnable.this.mPosition);
                            }
                        }
                    });
                }
            }
        }
    }

    private static class LoadBitmapRunnable implements Runnable {
        protected final WeakReference<TabGoodsBaseAdapter> weakReference;

        public LoadBitmapRunnable(WeakReference<TabGoodsBaseAdapter> weakReference2) {
            this.weakReference = weakReference2;
        }

        public void run() {
            Handler mtabHandler;
            final TabGoodsBaseAdapter tabGoodsBaseAdapter = (TabGoodsBaseAdapter) this.weakReference.get();
            if (tabGoodsBaseAdapter != null && (mtabHandler = tabGoodsBaseAdapter.mMainHandler) != null) {
                mtabHandler.post(new Runnable() {
                    public void run() {
                        FocusFlipGridView focusFlipGridView = tabGoodsBaseAdapter.mFocusFlipGridView;
                        if (focusFlipGridView != null) {
                            int firstVisibleItem = focusFlipGridView.getFirstVisiblePosition();
                            int endVisibleItem = focusFlipGridView.getLastVisiblePosition() + 1;
                            ZpLogger.i(TabGoodsBaseAdapter.TAG, "LoadBitmapRunnable ---> " + this + "; firstVisibleItem = " + firstVisibleItem + "; endVisibleItem = " + endVisibleItem);
                            if (firstVisibleItem >= 0) {
                                for (int itempos = firstVisibleItem; itempos < endVisibleItem; itempos++) {
                                    int index = itempos - firstVisibleItem;
                                    View childView = focusFlipGridView.getChildAt(index);
                                    ZpLogger.i(TabGoodsBaseAdapter.TAG, "LoadBitmapRunnable --->  index = " + index + "; childView = " + childView);
                                    if (childView instanceof TabGoodsItemView) {
                                        TabGoodsItemView fenLeiItemView = (TabGoodsItemView) childView;
                                        int position = fenLeiItemView.onGetPosition();
                                        tabGoodsBaseAdapter.onRequestImageOfItem(fenLeiItemView, tabGoodsBaseAdapter.mTabKey, position);
                                        tabGoodsBaseAdapter.onRequestInfoOfItem(fenLeiItemView, tabGoodsBaseAdapter.mTabKey, position);
                                        ZpLogger.i(TabGoodsBaseAdapter.TAG, "LoadBitmapRunnable --->  fenLeiItemView = " + fenLeiItemView + "; position = " + position);
                                    } else if (childView instanceof TabFlipGridViewHeaderView) {
                                        int cornerRadius = tabGoodsBaseAdapter.onGetCornerRadius();
                                        TabFlipGridViewHeaderView tabFlipGridViewHeaderView = (TabFlipGridViewHeaderView) childView;
                                        if (tabFlipGridViewHeaderView != null) {
                                            tabFlipGridViewHeaderView.onHandleHeaderContent(cornerRadius);
                                        }
                                        ZpLogger.i(TabGoodsBaseAdapter.TAG, "LoadBitmapRunnable --->  tabFlipGridViewHeaderView = " + tabFlipGridViewHeaderView + "; cornerRadius = " + cornerRadius);
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
    }
}
