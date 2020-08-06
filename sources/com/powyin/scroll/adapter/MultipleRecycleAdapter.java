package com.powyin.scroll.adapter;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.taobao.windvane.extra.uc.UCNetworkDelegate;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.powyin.scroll.R;
import com.powyin.scroll.adapter.AdapterDelegate;
import com.uc.webview.export.extension.UCCore;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MultipleRecycleAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AdapterDelegate<T> {
    private final int ITYPE_ERROR = 273;
    private final int ITYPE_Empty = 272;
    private final int ITYPE_FOOT = UCNetworkDelegate.CHANGE_WEBVIEW_URL;
    private final int ITYPE_HEAD = 275;
    private final int ITYPE_LOAD = 274;
    private boolean isMovingEnable = false;
    private ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            PowViewHolder powViewHolder = ((RecycleViewHolder) viewHolder).mPowViewHolder;
            if (powViewHolder == null || !powViewHolder.isEnableDragAndDrop()) {
                return 0;
            }
            return makeMovementFlags(15, 0);
        }

        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            PowViewHolder oriViewHolder = ((RecycleViewHolder) viewHolder).mPowViewHolder;
            PowViewHolder tarViewHolder = ((RecycleViewHolder) target).mPowViewHolder;
            if (oriViewHolder == null || tarViewHolder == null || !oriViewHolder.isEnableDragAndDrop() || !tarViewHolder.isEnableDragAndDrop()) {
                return false;
            }
            int ori = viewHolder.getAdapterPosition();
            int tar = target.getAdapterPosition();
            MultipleRecycleAdapter.this.mDataList.set(ori, MultipleRecycleAdapter.this.mDataList.set(tar, MultipleRecycleAdapter.this.mDataList.get(ori)));
            MultipleRecycleAdapter.this.notifyItemMoved(ori, tar);
            return true;
        }

        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int index = viewHolder.getAdapterPosition();
            MultipleRecycleAdapter.this.mDataList.remove(index);
            MultipleRecycleAdapter.this.notifyItemRemoved(index);
        }
    });
    /* access modifiers changed from: private */
    public Activity mActivity;
    List<T> mDataList = new ArrayList();
    /* access modifiers changed from: private */
    public View mFootView;
    private boolean mHasFoot = false;
    boolean mHasHead = false;
    private boolean mHasLoad = false;
    /* access modifiers changed from: private */
    public View mHeadView;
    private Constructor<? extends PowViewHolder>[] mHolderConstructor;
    private Class[] mHolderGenericDataClass;
    private PowViewHolder[] mHolderInstances;
    /* access modifiers changed from: private */
    public boolean mIsProgressLoadMore = false;
    private MultipleRecycleAdapter<T>.IncludeTypeLoad mLoad;
    /* access modifiers changed from: private */
    public String mLoadCompleteInfo = "load completed";
    /* access modifiers changed from: private */
    public AdapterDelegate.LoadedStatus mLoadStatus = null;
    /* access modifiers changed from: private */
    public long mLoadViewBeginShowTime = -1;
    AdapterDelegate.OnItemClickListener<T> mOnItemClickListener;
    AdapterDelegate.OnItemLongClickListener<T> mOnItemLongClickListener;
    /* access modifiers changed from: private */
    public AdapterDelegate.OnLoadMoreListener mOnLoadMoreListener;
    private RecyclerView mRecyclerView;
    private boolean mSpaceEnable = false;
    /* access modifiers changed from: private */
    public View mSpaceView;

    @SafeVarargs
    public static <T> MultipleRecycleAdapter<T> getByViewHolder(Activity activity, Class<? extends PowViewHolder<? extends T>>... arrClass) {
        return new MultipleRecycleAdapter<>(activity, arrClass);
    }

    @SafeVarargs
    public MultipleRecycleAdapter(Activity activity, Class<? extends PowViewHolder<? extends T>>... viewHolderClass) {
        Type genericType;
        Class[] clsArr = new Class[viewHolderClass.length];
        System.arraycopy(viewHolderClass, 0, clsArr, 0, viewHolderClass.length);
        this.mActivity = activity;
        this.mHolderInstances = new PowViewHolder[clsArr.length];
        this.mHolderGenericDataClass = new Class[clsArr.length];
        this.mHolderConstructor = new Constructor[clsArr.length];
        int i = 0;
        while (i < clsArr.length) {
            try {
                this.mHolderConstructor[i] = clsArr[i].getConstructor(new Class[]{Activity.class, ViewGroup.class});
                this.mHolderConstructor[i].setAccessible(true);
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        int i2 = 0;
        while (i2 < clsArr.length) {
            Class typeClass = clsArr[i2];
            do {
                genericType = typeClass.getGenericSuperclass();
                typeClass = typeClass.getSuperclass();
                if (typeClass == PowViewHolder.class || typeClass == Object.class) {
                }
                genericType = typeClass.getGenericSuperclass();
                typeClass = typeClass.getSuperclass();
                break;
            } while (typeClass == Object.class);
            if (typeClass != PowViewHolder.class || genericType == PowViewHolder.class) {
                throw new RuntimeException("参数类必须继承泛型ViewHolder");
            }
            Type genericClass = ((ParameterizedType) genericType).getActualTypeArguments()[0];
            if (genericClass instanceof Class) {
                this.mHolderGenericDataClass[i2] = (Class) genericClass;
            } else if (genericClass instanceof ParameterizedType) {
                this.mHolderGenericDataClass[i2] = (Class) ((ParameterizedType) genericClass).getRawType();
            } else {
                throw new RuntimeException("get genericClass error");
            }
            try {
                this.mHolderInstances[i2] = (PowViewHolder) this.mHolderConstructor[i2].newInstance(new Object[]{this.mActivity, null});
                i2++;
            } catch (Exception e2) {
                e2.printStackTrace();
                throw new RuntimeException(e2.getMessage());
            }
        }
        PowViewHolder<T>[] powViewHolderArr = this.mHolderInstances;
        int length = powViewHolderArr.length;
        int i3 = 0;
        while (i3 < length) {
            this.isMovingEnable |= powViewHolderArr[i3].isEnableDragAndDrop();
            if (!this.isMovingEnable) {
                i3++;
            } else {
                return;
            }
        }
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 272:
                return new IncludeTypeEmpty(getSpaceContain(parent));
            case 273:
                return new IncludeTypeError(parent);
            case 274:
                return new IncludeTypeLoad(parent);
            case 275:
                return new IncludeTypeHead(parent);
            case UCNetworkDelegate.CHANGE_WEBVIEW_URL:
                return new IncludeTypeFoot(parent);
            default:
                try {
                    return ((PowViewHolder) this.mHolderConstructor[viewType].newInstance(new Object[]{this.mActivity, parent})).mViewHolder;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e.getMessage());
                }
        }
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int oriPosition) {
        int position;
        int position2;
        switch (holder.getItemViewType()) {
            case 272:
                ((IncludeTypeEmpty) holder).loadView();
                return;
            case 273:
                if (this.mHasHead) {
                    position = oriPosition - 1;
                } else {
                    position = oriPosition;
                }
                ((IncludeTypeError) holder).loadData(this.mDataList.get(position));
                return;
            case 274:
                MultipleRecycleAdapter<T>.IncludeTypeLoad load = (IncludeTypeLoad) holder;
                this.mLoad = load;
                load.progressBar.ensureLoading();
                return;
            case 275:
                ((IncludeTypeHead) holder).loadView();
                return;
            case UCNetworkDelegate.CHANGE_WEBVIEW_URL:
                ((IncludeTypeFoot) holder).loadView();
                return;
            default:
                if (this.mHasHead) {
                    position2 = oriPosition - 1;
                } else {
                    position2 = oriPosition;
                }
                T itemData = position2 < this.mDataList.size() ? this.mDataList.get(position2) : null;
                PowViewHolder<T> powViewHolder = ((RecycleViewHolder) holder).mPowViewHolder;
                powViewHolder.mData = itemData;
                powViewHolder.mMultipleAdapter = this;
                if (this.mOnItemClickListener != null) {
                    powViewHolder.registerAutoItemClick();
                }
                if (this.mOnItemLongClickListener != null) {
                    powViewHolder.registerAutoItemLongClick();
                }
                powViewHolder.loadData(this, itemData, position2);
                return;
        }
    }

    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        switch (holder.getItemViewType()) {
            case 274:
                ((IncludeTypeLoad) holder).progressBar.ensureStopAnimation();
                return;
            default:
                return;
        }
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
        if (this.isMovingEnable) {
            this.itemTouchHelper.attachToRecyclerView(this.mRecyclerView);
        }
    }

    public int getItemCount() {
        int i = 1;
        if (this.mSpaceEnable) {
            return 1;
        }
        int size = (this.mHasFoot ? 1 : 0) + this.mDataList.size() + (this.mHasHead ? 1 : 0);
        if (!this.mHasLoad) {
            i = 0;
        }
        return i + size;
    }

    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ((RecycleViewHolder) holder).onViewAttachedToWindow();
    }

    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ((RecycleViewHolder) holder).onViewDetachedFromWindow();
    }

    public long getItemId(int position) {
        if (this.mSpaceEnable) {
            return 272;
        }
        if (this.mHasHead) {
            if (position == 0) {
                return 275;
            }
            position--;
        }
        if (position < this.mDataList.size()) {
            T data = this.mDataList.get(position);
            if (data == null) {
                return 0;
            }
            return (long) data.hashCode();
        }
        int position2 = position - this.mDataList.size();
        if (!this.mHasFoot || position2 != 0) {
            return 274;
        }
        return 276;
    }

    public int getItemViewType(int position) {
        if (this.mSpaceEnable) {
            return 272;
        }
        if (this.mHasHead) {
            if (position == 0) {
                return 275;
            }
            position--;
        }
        if (position < this.mDataList.size()) {
            for (int i = 0; i < this.mHolderInstances.length; i++) {
                T itemData = this.mDataList.get(position);
                if (itemData != null && this.mHolderGenericDataClass[i].isAssignableFrom(itemData.getClass()) && this.mHolderInstances[i].acceptData(itemData)) {
                    return i;
                }
            }
            return 273;
        }
        int position2 = position - this.mDataList.size();
        if (this.mHasFoot) {
            if (position2 == 0) {
                return UCNetworkDelegate.CHANGE_WEBVIEW_URL;
            }
            position2--;
        }
        if (position2 == 0 && this.mHasLoad) {
            return 274;
        }
        throw new RuntimeException(" what happen ");
    }

    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.mRecyclerView = null;
    }

    public List<T> getDataList() {
        ArrayList<T> arrayList = new ArrayList<>();
        arrayList.addAll(this.mDataList);
        return arrayList;
    }

    public int getDataCount() {
        return this.mDataList.size();
    }

    public void loadData(List<T> dataList) {
        this.mDataList.clear();
        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addData(int position, T data) {
        if (data != null) {
            this.mDataList.add(position, data);
            if (this.mHasHead) {
                position++;
            }
            notifyItemInserted(position);
        }
    }

    public void addData(int position, List<T> dataList) {
        if (dataList != null) {
            this.mDataList.addAll(position, dataList);
            notifyDataSetChanged();
        }
    }

    public void addDataAtLast(List<T> dataList) {
        addDataAtLast(dataList, (AdapterDelegate.LoadedStatus) null, 0);
    }

    public void addDataAtLast(final List<T> dataList, final AdapterDelegate.LoadedStatus status, int delayTime) {
        if (delayTime <= 0) {
            if (dataList != null && dataList.size() > 0) {
                this.mDataList.addAll(this.mDataList.size(), dataList);
                notifyItemRangeInserted(this.mDataList.size(), dataList.size());
            }
            setLoadMoreStatus(status);
            return;
        }
        this.mActivity.getWindow().getDecorView().postDelayed(new Runnable() {
            public void run() {
                if (dataList != null && dataList.size() > 0) {
                    MultipleRecycleAdapter.this.mDataList.addAll(MultipleRecycleAdapter.this.mDataList.size(), dataList);
                    MultipleRecycleAdapter.this.notifyDataSetChanged();
                }
                MultipleRecycleAdapter.this.setLoadMoreStatus(status);
            }
        }, (long) delayTime);
    }

    public T removeData(int position) {
        T ret = this.mDataList.remove(position);
        if (this.mHasHead) {
            position++;
        }
        notifyItemRemoved(position);
        return ret;
    }

    public void removeData(T data) {
        int index = this.mDataList.indexOf(data);
        if (index >= 0) {
            removeData(index);
        }
    }

    public void clearData() {
        if (this.mDataList.size() != 0) {
            this.mDataList.clear();
            notifyDataSetChanged();
        }
    }

    public void enableLoadMore(boolean show) {
        if (this.mHasLoad != show) {
            this.mHasLoad = show;
            notifyDataSetChanged();
        }
    }

    public void setLoadMoreStatus(AdapterDelegate.LoadedStatus status) {
        if (status != null) {
            this.mLoadStatus = status;
            this.mIsProgressLoadMore = false;
            if (this.mLoad != null) {
                this.mLoad.progressBar.invalidate();
            }
        }
    }

    public void loadMore() {
        if (this.mOnLoadMoreListener != null) {
            this.mIsProgressLoadMore = true;
            this.mOnLoadMoreListener.onLoadMore();
        }
    }

    public void completeLoadMore() {
        this.mIsProgressLoadMore = false;
        if (this.mLoad != null) {
            this.mLoad.progressBar.ensureLoading();
        }
    }

    public void setOnLoadMoreListener(AdapterDelegate.OnLoadMoreListener loadMoreListener) {
        this.mOnLoadMoreListener = loadMoreListener;
    }

    public void setOnItemClickListener(AdapterDelegate.OnItemClickListener<T> clickListener) {
        this.mOnItemClickListener = clickListener;
    }

    public AdapterDelegate.OnItemClickListener<T> getOnItemClickListener() {
        return this.mOnItemClickListener;
    }

    public void setOnItemLongClickListener(AdapterDelegate.OnItemLongClickListener<T> longClickListener) {
        this.mOnItemLongClickListener = longClickListener;
    }

    public void setHeadView(View view) {
        if (view != null) {
            this.mHasHead = true;
            this.mHeadView = view;
            notifyDataSetChanged();
        }
    }

    public void removeHeadView() {
        if (this.mHasHead) {
            this.mHasHead = false;
            this.mHeadView = null;
            notifyDataSetChanged();
        }
    }

    public void setFootView(View view) {
        if (view != null) {
            this.mHasFoot = true;
            this.mFootView = view;
            notifyDataSetChanged();
        }
    }

    public void removeFootView() {
        if (this.mHasFoot) {
            this.mHasFoot = false;
            this.mFootView = null;
            notifyDataSetChanged();
        }
    }

    public void enableEmptyView(boolean show) {
        if (this.mSpaceEnable != show) {
            this.mSpaceEnable = show;
            notifyDataSetChanged();
        }
    }

    public void setEmptyView(View view) {
        if (view != null && this.mSpaceView != view) {
            this.mSpaceView = view;
            notifyDataSetChanged();
        }
    }

    private class IncludeTypeEmpty extends RecycleViewHolder<Object> {
        FrameLayout mainView;

        IncludeTypeEmpty(FrameLayout viewGroup) {
            super(viewGroup, (PowViewHolder) null);
            this.mainView = viewGroup;
        }

        /* access modifiers changed from: package-private */
        public void loadView() {
            if (MultipleRecycleAdapter.this.mSpaceView != null) {
                ViewParent parent = MultipleRecycleAdapter.this.mSpaceView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(MultipleRecycleAdapter.this.mSpaceView);
                }
                this.mainView.removeAllViews();
                this.mainView.addView(MultipleRecycleAdapter.this.mSpaceView, new FrameLayout.LayoutParams(-1, -1));
            }
        }
    }

    private FrameLayout getSpaceContain(ViewGroup viewGroup) {
        FrameLayout frameLayout = new FrameLayout(viewGroup.getContext()) {
            /* access modifiers changed from: protected */
            public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(heightMeasureSpec), UCCore.VERIFY_POLICY_QUICK));
            }
        };
        TextView textView = new TextView(viewGroup.getContext());
        textView.setText("space");
        textView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 17;
        frameLayout.addView(textView, layoutParams);
        frameLayout.setLayoutParams(new RecyclerView.LayoutParams(-1, -1));
        return frameLayout;
    }

    private class IncludeTypeError extends RecycleViewHolder<Object> {
        TextView errorInfo = ((TextView) this.itemView.findViewById(R.id.powyin_scroll_err_text));

        IncludeTypeError(ViewGroup viewGroup) {
            super(MultipleRecycleAdapter.this.mActivity.getLayoutInflater().inflate(R.layout.powyin_scroll_multiple_adapter_err, viewGroup, false), (PowViewHolder) null);
        }

        /* access modifiers changed from: private */
        public void loadData(T data) {
            this.errorInfo.setText(data == null ? " you has one empty data inside " : data.toString());
        }
    }

    private class IncludeTypeLoad extends RecycleViewHolder<Object> {
        MultipleRecycleAdapter<T>.LoadProgressBar progressBar = ((LoadProgressBar) this.itemView);

        IncludeTypeLoad(ViewGroup viewGroup) {
            super(new LoadProgressBar(MultipleRecycleAdapter.this.mActivity), (PowViewHolder) null);
        }
    }

    private class IncludeTypeHead extends RecycleViewHolder<Object> {
        FrameLayout frameLayout = ((FrameLayout) this.itemView);

        IncludeTypeHead(ViewGroup viewGroup) {
            super(MultipleRecycleAdapter.this.mActivity.getLayoutInflater().inflate(R.layout.powyin_scroll_multiple_adapter_head, viewGroup, false), (PowViewHolder) null);
        }

        /* access modifiers changed from: package-private */
        public void loadView() {
            if (MultipleRecycleAdapter.this.mHeadView != null) {
                ViewParent parent = MultipleRecycleAdapter.this.mHeadView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(MultipleRecycleAdapter.this.mHeadView);
                }
                this.frameLayout.removeAllViews();
                this.frameLayout.addView(MultipleRecycleAdapter.this.mHeadView, new FrameLayout.LayoutParams(-1, -2));
            }
        }
    }

    private class IncludeTypeFoot extends RecycleViewHolder<Object> {
        FrameLayout frameLayout = ((FrameLayout) this.itemView);

        IncludeTypeFoot(ViewGroup viewGroup) {
            super(MultipleRecycleAdapter.this.mActivity.getLayoutInflater().inflate(R.layout.powyin_scroll_multiple_adapter_foot, viewGroup, false), (PowViewHolder) null);
        }

        /* access modifiers changed from: package-private */
        public void loadView() {
            if (MultipleRecycleAdapter.this.mFootView != null) {
                ViewParent parent = MultipleRecycleAdapter.this.mFootView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(MultipleRecycleAdapter.this.mFootView);
                }
                this.frameLayout.removeAllViews();
                this.frameLayout.addView(MultipleRecycleAdapter.this.mFootView, new FrameLayout.LayoutParams(-1, -2));
            }
        }
    }

    class LoadProgressBar extends View {
        ValueAnimator animator;
        int ballCount = 13;
        int canvasHei;
        float canvasTextX;
        float canvasTextY;
        int canvasWei;
        Paint circlePaint = new Paint();
        float divide;
        boolean mAttached = false;
        TextPaint textPaint;

        public LoadProgressBar(Context context) {
            super(context);
            this.circlePaint.setColor(1996488704);
            this.circlePaint.setStrokeWidth(4.0f);
            this.textPaint = new TextPaint();
            this.textPaint.setColor(-1711276033);
            this.textPaint.setTextSize((float) ((int) ((13.0f * context.getResources().getDisplayMetrics().scaledDensity) + 0.5f)));
            this.textPaint.setAntiAlias(true);
            this.textPaint.setStrokeWidth(1.0f);
        }

        public void addOnLayoutChangeListener(View.OnLayoutChangeListener listener) {
            super.addOnLayoutChangeListener(listener);
        }

        private void ensureAnimation() {
            if (!this.mAttached || MultipleRecycleAdapter.this.mLoadViewBeginShowTime == -1 || MultipleRecycleAdapter.this.mLoadStatus == AdapterDelegate.LoadedStatus.NO_MORE || MultipleRecycleAdapter.this.mLoadStatus == AdapterDelegate.LoadedStatus.ERROR) {
                if (this.animator != null) {
                    this.animator.cancel();
                    this.animator = null;
                }
                invalidate();
            } else if (this.animator == null || !this.animator.isStarted()) {
                this.animator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
                this.animator.setDuration(2500);
                this.animator.setRepeatCount(-1);
                this.animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        if (valueAnimator != LoadProgressBar.this.animator) {
                            valueAnimator.cancel();
                        } else if (!LoadProgressBar.this.mAttached || MultipleRecycleAdapter.this.mLoadViewBeginShowTime == -1 || MultipleRecycleAdapter.this.mLoadStatus == AdapterDelegate.LoadedStatus.NO_MORE || MultipleRecycleAdapter.this.mLoadStatus == AdapterDelegate.LoadedStatus.ERROR) {
                            if (LoadProgressBar.this.animator != null) {
                                LoadProgressBar.this.animator.cancel();
                                LoadProgressBar.this.animator = null;
                            }
                            LoadProgressBar.this.invalidate();
                        } else {
                            LoadProgressBar.this.divide = (10.0f * ((float) ((System.currentTimeMillis() % 3200) - 1600))) / 3200.0f;
                            LoadProgressBar.this.invalidate();
                        }
                    }
                });
                this.animator.start();
            }
        }

        /* access modifiers changed from: private */
        public void ensureStopAnimation() {
            if (this.animator != null) {
                this.animator.cancel();
                this.animator = null;
            }
        }

        private float getSplit(float value) {
            int positive = value >= 0.0f ? 1 : -1;
            float value2 = Math.abs(value);
            if (value2 <= 1.0f) {
                return ((float) positive) * value2;
            }
            return ((float) Math.pow((double) value2, 2.0d)) * ((float) positive);
        }

        /* access modifiers changed from: protected */
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.mAttached = true;
        }

        /* access modifiers changed from: protected */
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.mAttached = false;
            ensureStopAnimation();
        }

        /* access modifiers changed from: protected */
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(getMeasuredWidth(), (int) ((40.0f * getContext().getResources().getDisplayMetrics().density) + 0.5f));
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (MultipleRecycleAdapter.this.mLoadViewBeginShowTime != -1) {
                float alpha = ((float) ((int) (System.currentTimeMillis() - MultipleRecycleAdapter.this.mLoadViewBeginShowTime))) / 2000.0f;
                if (alpha > 1.0f) {
                    alpha = 1.0f;
                }
                if (alpha < 0.0f) {
                    alpha = 1.0f;
                }
                this.circlePaint.setAlpha((int) (200.0f * alpha));
                if (MultipleRecycleAdapter.this.mLoadStatus == AdapterDelegate.LoadedStatus.NO_MORE) {
                    canvas.drawText(MultipleRecycleAdapter.this.mLoadCompleteInfo, this.canvasTextX, this.canvasTextY, this.textPaint);
                    canvas.drawLine(20.0f, (float) (this.canvasHei / 2), this.canvasTextX - 20.0f, (float) (this.canvasHei / 2), this.textPaint);
                    canvas.drawLine((((float) this.canvasWei) - this.canvasTextX) + 20.0f, (float) (this.canvasHei / 2), (float) (this.canvasWei - 20), (float) (this.canvasHei / 2), this.textPaint);
                }
                if (MultipleRecycleAdapter.this.mLoadStatus == AdapterDelegate.LoadedStatus.ERROR) {
                    canvas.drawText("error", this.canvasTextX, this.canvasTextY, this.textPaint);
                    canvas.drawLine(20.0f, (float) (this.canvasHei / 2), this.canvasTextX - 20.0f, (float) (this.canvasHei / 2), this.textPaint);
                    canvas.drawLine(20.0f + (((float) this.canvasWei) - this.canvasTextX), (float) (this.canvasHei / 2), (float) (this.canvasWei - 20), (float) (this.canvasHei / 2), this.textPaint);
                }
                if (MultipleRecycleAdapter.this.mLoadStatus == null) {
                    for (int i = 0; i < this.ballCount; i++) {
                        canvas.drawCircle(((float) (this.canvasWei / 2)) + (getSplit((5.0f * (((((float) i) * 1.0f) / ((float) this.ballCount)) - 0.5f)) + this.divide) * ((float) this.canvasWei) * 0.08f), (float) ((this.canvasHei / 2) + 6), 8.0f, this.circlePaint);
                    }
                }
            }
        }

        /* access modifiers changed from: protected */
        public void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            this.canvasHei = getHeight();
            this.canvasWei = getWidth();
            this.canvasTextX = ((float) (this.canvasWei / 2)) - (this.textPaint.measureText(MultipleRecycleAdapter.this.mLoadCompleteInfo) / 2.0f);
            this.canvasTextY = ((float) (this.canvasHei / 2)) + (this.textPaint.getTextSize() / 2.55f);
            if (bottom < ((ViewGroup) getParent()).getHeight()) {
                long unused = MultipleRecycleAdapter.this.mLoadViewBeginShowTime = -1;
            } else {
                long unused2 = MultipleRecycleAdapter.this.mLoadViewBeginShowTime = System.currentTimeMillis();
                ensureAnimation();
            }
            ensureLoading();
        }

        /* access modifiers changed from: package-private */
        public void ensureLoading() {
            if (MultipleRecycleAdapter.this.mOnLoadMoreListener != null && MultipleRecycleAdapter.this.mLoadStatus == null && !MultipleRecycleAdapter.this.mIsProgressLoadMore && this.mAttached && MultipleRecycleAdapter.this.mLoadViewBeginShowTime != -1) {
                boolean unused = MultipleRecycleAdapter.this.mIsProgressLoadMore = true;
                MultipleRecycleAdapter.this.mOnLoadMoreListener.onLoadMore();
            }
        }
    }
}
