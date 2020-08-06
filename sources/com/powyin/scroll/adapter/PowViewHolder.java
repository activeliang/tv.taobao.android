package com.powyin.scroll.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import com.powyin.scroll.adapter.AdapterDelegate;
import java.lang.reflect.Field;

public abstract class PowViewHolder<T> {
    protected final Activity mActivity;
    public T mData;
    public final View mItemView;
    MultipleRecycleAdapter<T> mMultipleAdapter;
    MultipleListAdapter<T> mMultipleListAdapter;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (!(PowViewHolder.this.mMultipleAdapter == null || PowViewHolder.this.mMultipleAdapter.mOnItemClickListener == null)) {
                int index = PowViewHolder.this.mViewHolder != null ? PowViewHolder.this.mViewHolder.getAdapterPosition() : PowViewHolder.this.mPosition;
                if (PowViewHolder.this.mMultipleAdapter.mHasHead) {
                    index--;
                }
                AdapterDelegate.OnItemClickListener<T> onItemClickListener = PowViewHolder.this.mMultipleAdapter.mOnItemClickListener;
                if (index >= 0 && index < PowViewHolder.this.mMultipleAdapter.mDataList.size()) {
                    onItemClickListener.onClick(PowViewHolder.this, PowViewHolder.this.mData, index, v.getId());
                }
            }
            if (PowViewHolder.this.mMultipleListAdapter != null && PowViewHolder.this.mMultipleListAdapter.mOnItemClickListener != null) {
                int index2 = PowViewHolder.this.mViewHolder != null ? PowViewHolder.this.mViewHolder.getAdapterPosition() : PowViewHolder.this.mPosition;
                if (PowViewHolder.this.mMultipleListAdapter.mHasHead) {
                    index2--;
                }
                AdapterDelegate.OnItemClickListener<T> onItemClickListener2 = PowViewHolder.this.mMultipleListAdapter.mOnItemClickListener;
                if (index2 >= 0 && index2 < PowViewHolder.this.mMultipleListAdapter.mDataList.size()) {
                    onItemClickListener2.onClick(PowViewHolder.this, PowViewHolder.this.mData, index2, v.getId());
                }
            }
        }
    };
    private final View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        public boolean onLongClick(View v) {
            if (!(PowViewHolder.this.mMultipleAdapter == null || PowViewHolder.this.mMultipleAdapter.mOnItemLongClickListener == null)) {
                int index = PowViewHolder.this.mViewHolder != null ? PowViewHolder.this.mViewHolder.getAdapterPosition() : PowViewHolder.this.mPosition;
                if (PowViewHolder.this.mMultipleAdapter.mHasHead) {
                    index--;
                }
                AdapterDelegate.OnItemLongClickListener<T> onItemLongClickListener = PowViewHolder.this.mMultipleAdapter.mOnItemLongClickListener;
                if (index >= 0 && index < PowViewHolder.this.mMultipleAdapter.mDataList.size()) {
                    return onItemLongClickListener.onLongClick(PowViewHolder.this, PowViewHolder.this.mData, index, v.getId());
                }
            }
            if (!(PowViewHolder.this.mMultipleListAdapter == null || PowViewHolder.this.mMultipleListAdapter.mOnItemLongClickListener == null)) {
                int index2 = PowViewHolder.this.mViewHolder != null ? PowViewHolder.this.mViewHolder.getAdapterPosition() : PowViewHolder.this.mPosition;
                if (PowViewHolder.this.mMultipleListAdapter.mHasHead) {
                    index2--;
                }
                AdapterDelegate.OnItemLongClickListener<T> onItemLongClickListener2 = PowViewHolder.this.mMultipleListAdapter.mOnItemLongClickListener;
                if (index2 >= 0 && index2 < PowViewHolder.this.mMultipleListAdapter.mDataList.size()) {
                    return onItemLongClickListener2.onLongClick(PowViewHolder.this, PowViewHolder.this.mData, index2, v.getId());
                }
            }
            return false;
        }
    };
    int mPosition = -1;
    private int mRegisterMainItemClickStatus = 0;
    private int mRegisterMainItemLongClickStatus = 0;
    RecycleViewHolder<T> mViewHolder;

    /* access modifiers changed from: protected */
    public abstract int getItemViewRes();

    public abstract void loadData(AdapterDelegate<? super T> adapterDelegate, T t, int i);

    public PowViewHolder(Activity activity, ViewGroup viewGroup) {
        this.mActivity = activity;
        if (getItemViewRes() == 0) {
            throw new RuntimeException("must provide View by getItemView() or gitItemViewRes()");
        }
        this.mItemView = activity.getLayoutInflater().inflate(getItemViewRes(), viewGroup, false);
        this.mViewHolder = new RecycleViewHolder<>(this.mItemView, this);
    }

    /* access modifiers changed from: package-private */
    public final void registerAutoItemClick() {
        if (this.mRegisterMainItemClickStatus == 0) {
            if (getItemViewOnClickListener() == null) {
                this.mItemView.setOnClickListener(this.mOnClickListener);
                this.mRegisterMainItemClickStatus = 1;
                return;
            }
            this.mRegisterMainItemClickStatus = -1;
        }
    }

    /* access modifiers changed from: package-private */
    public final void registerAutoItemLongClick() {
        if (this.mRegisterMainItemLongClickStatus == 0) {
            if (getItemViewOnLongClickListener() == null) {
                this.mItemView.setOnLongClickListener(this.mOnLongClickListener);
                this.mRegisterMainItemLongClickStatus = 1;
                return;
            }
            this.mRegisterMainItemLongClickStatus = -1;
        }
    }

    private View.OnClickListener getItemViewOnClickListener() {
        if (this.mItemView.isClickable()) {
            try {
                Field mListenerInfo = View.class.getDeclaredField("mListenerInfo");
                mListenerInfo.setAccessible(true);
                Object infoObject = mListenerInfo.get(this.mItemView);
                if (infoObject == null) {
                    return null;
                }
                Object onClickObject = infoObject.getClass().getDeclaredField("mOnClickListener").get(infoObject);
                if (onClickObject == null) {
                    return null;
                }
                if (onClickObject instanceof View.OnClickListener) {
                    return (View.OnClickListener) onClickObject;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private View.OnLongClickListener getItemViewOnLongClickListener() {
        if (this.mItemView.isLongClickable()) {
            try {
                Field mListenerInfo = View.class.getDeclaredField("mListenerInfo");
                mListenerInfo.setAccessible(true);
                Object infoObject = mListenerInfo.get(this.mItemView);
                if (infoObject == null) {
                    return null;
                }
                Object onClickObject = infoObject.getClass().getDeclaredField("mOnLongClickListener").get(infoObject);
                if (onClickObject == null) {
                    return null;
                }
                if (onClickObject instanceof View.OnLongClickListener) {
                    return (View.OnLongClickListener) onClickObject;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public final void registerItemClick(int... viewIds) {
        if (this.mRegisterMainItemClickStatus == 1 && this.mItemView.isClickable() && getItemViewOnClickListener() == this.mOnClickListener) {
            this.mItemView.setOnClickListener((View.OnClickListener) null);
        }
        this.mRegisterMainItemClickStatus = -1;
        int i = 0;
        while (viewIds != null && i < viewIds.length) {
            View item = this.mItemView.findViewById(viewIds[i]);
            if (item != null) {
                item.setOnClickListener(this.mOnClickListener);
            }
            i++;
        }
    }

    /* access modifiers changed from: protected */
    public final void registerItemLongClick(int... viewIds) {
        if (this.mRegisterMainItemLongClickStatus == 1 && this.mItemView.isLongClickable() && getItemViewOnLongClickListener() == this.mOnLongClickListener) {
            this.mItemView.setOnLongClickListener((View.OnLongClickListener) null);
        }
        this.mRegisterMainItemLongClickStatus = -1;
        int i = 0;
        while (viewIds != null && i < viewIds.length) {
            View item = this.mItemView.findViewById(viewIds[i]);
            if (item != null) {
                item.setOnLongClickListener(this.mOnLongClickListener);
            }
            i++;
        }
    }

    /* access modifiers changed from: protected */
    public boolean acceptData(T t) {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isEnableDragAndDrop() {
        return false;
    }

    public final int getItemPostion() {
        if (this.mViewHolder == null) {
            return this.mPosition;
        }
        int position = this.mViewHolder.getAdapterPosition();
        if (this.mMultipleAdapter.mHasHead) {
            return position - 1;
        }
        return position;
    }

    public <K extends View> K findViewById(int resId) {
        return this.mItemView.findViewById(resId);
    }

    /* access modifiers changed from: protected */
    public void onViewAttachedToWindow() {
    }

    /* access modifiers changed from: protected */
    public void onViewDetachedFromWindow() {
    }
}
