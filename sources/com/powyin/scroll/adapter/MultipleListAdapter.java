package com.powyin.scroll.adapter;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.powyin.scroll.R;
import com.powyin.scroll.adapter.AdapterDelegate;
import com.uc.webview.export.extension.UCCore;
import com.yunos.tv.alitvasr.sdk.AbstractClientManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MultipleListAdapter<T> implements ListAdapter, AdapterDelegate<T> {
    private final int ITYPE_ERROR = 1;
    private final int ITYPE_Empty = 0;
    private final int ITYPE_FOOT = 4;
    private final int ITYPE_HEAD = 3;
    private final int ITYPE_LOAD = 2;
    /* access modifiers changed from: private */
    public Activity mActivity;
    List<T> mDataList = new ArrayList();
    private final DataSetObservable mDataSetObservable = new DataSetObservable();
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
    private MultipleListAdapter<T>.IncludeTypeLoad mLoad;
    /* access modifiers changed from: private */
    public String mLoadCompleteInfo = "我是有底线的";
    /* access modifiers changed from: private */
    public String mLoadErrorInfo = "加载失败";
    /* access modifiers changed from: private */
    public AdapterDelegate.LoadedStatus mLoadStatus;
    AdapterDelegate.OnItemClickListener<T> mOnItemClickListener;
    AdapterDelegate.OnItemLongClickListener<T> mOnItemLongClickListener;
    /* access modifiers changed from: private */
    public AdapterDelegate.OnLoadMoreListener mOnLoadMoreListener;
    private boolean mSpaceEnable = false;
    /* access modifiers changed from: private */
    public View mSpaceView;

    @SafeVarargs
    public static <T> MultipleListAdapter<T> getByViewHolder(Activity activity, Class<? extends PowViewHolder<? extends T>>... arrClass) {
        return new MultipleListAdapter<>(activity, arrClass);
    }

    @SafeVarargs
    public MultipleListAdapter(Activity activity, Class<? extends PowViewHolder<? extends T>>... viewHolderClass) {
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
                throw new RuntimeException("参数类必须实现（Activity）单一参数的构造方法  或者  " + e2.getMessage());
            }
        }
    }

    public boolean areAllItemsEnabled() {
        return true;
    }

    public boolean isEnabled(int position) {
        return true;
    }

    public int getCount() {
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

    public Object getItem(int position) {
        if (this.mSpaceEnable) {
            return 0;
        }
        if (this.mHasHead) {
            if (position == 0) {
                return 3;
            }
            position--;
        }
        if (position < this.mDataList.size()) {
            return this.mDataList.get(position);
        }
        int position2 = position - this.mDataList.size();
        if (!this.mHasFoot || position2 != 0) {
            return 2;
        }
        return 4;
    }

    public boolean hasStableIds() {
        return false;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case 0:
                    RecyclerView.ViewHolder empty = new IncludeTypeEmpty(getSpaceContain(parent));
                    empty.itemView.setTag(empty);
                    convertView = empty.itemView;
                    break;
                case 1:
                    RecyclerView.ViewHolder error = new IncludeTypeError(parent);
                    error.itemView.setTag(error);
                    convertView = error.itemView;
                    break;
                case 2:
                    RecyclerView.ViewHolder load = new IncludeTypeLoad(parent);
                    load.itemView.setTag(load);
                    convertView = load.itemView;
                    break;
                case 3:
                    RecyclerView.ViewHolder head = new IncludeTypeHead(parent);
                    head.itemView.setTag(head);
                    convertView = head.itemView;
                    break;
                case 4:
                    RecyclerView.ViewHolder foot = new IncludeTypeFoot(parent);
                    foot.itemView.setTag(foot);
                    convertView = foot.itemView;
                    break;
                default:
                    try {
                        PowViewHolder holder = (PowViewHolder) this.mHolderConstructor[type - 5].newInstance(new Object[]{this.mActivity, parent});
                        holder.mItemView.setTag(holder);
                        convertView = holder.mItemView;
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e.getMessage());
                    }
            }
        }
        switch (type) {
            case 0:
                ((IncludeTypeEmpty) convertView.getTag()).loadView();
                break;
            case 1:
                if (this.mHasHead) {
                    position--;
                }
                ((IncludeTypeError) convertView.getTag()).loadData(this.mDataList.get(position));
                break;
            case 2:
                MultipleListAdapter<T>.IncludeTypeLoad load2 = (IncludeTypeLoad) convertView.getTag();
                this.mLoad = load2;
                load2.progressBar.ensureAnimation(false);
                load2.ensureLoading();
                break;
            case 3:
                ((IncludeTypeHead) convertView.getTag()).loadView();
                break;
            case 4:
                ((IncludeTypeFoot) convertView.getTag()).loadView();
                break;
            default:
                if (this.mHasHead) {
                    position--;
                }
                T itemData = position < this.mDataList.size() ? this.mDataList.get(position) : null;
                PowViewHolder<T> powViewHolder = (PowViewHolder) convertView.getTag();
                powViewHolder.mData = itemData;
                powViewHolder.mMultipleListAdapter = this;
                if (this.mOnItemClickListener != null) {
                    powViewHolder.registerAutoItemClick();
                }
                if (this.mOnItemLongClickListener != null) {
                    powViewHolder.registerAutoItemLongClick();
                }
                powViewHolder.loadData(this, itemData, position);
                break;
        }
        return convertView;
    }

    public long getItemId(int position) {
        if (this.mSpaceEnable) {
            return 0;
        }
        if (this.mHasHead) {
            if (position == 0) {
                return 3;
            }
            position--;
        }
        if (position < this.mDataList.size()) {
            T data = this.mDataList.get(position);
            if (data != null) {
                return (long) data.hashCode();
            }
            return 0;
        }
        int position2 = position - this.mDataList.size();
        if (!this.mHasFoot || position2 != 0) {
            return 2;
        }
        return 4;
    }

    public int getItemViewType(int position) {
        if (this.mSpaceEnable) {
            return 0;
        }
        if (this.mHasHead) {
            if (position == 0) {
                return 3;
            }
            position--;
        }
        if (position < this.mDataList.size()) {
            for (int i = 0; i < this.mHolderInstances.length; i++) {
                T itemData = this.mDataList.get(position);
                if (itemData != null && this.mHolderGenericDataClass[i].isAssignableFrom(itemData.getClass()) && this.mHolderInstances[i].acceptData(itemData)) {
                    return i + 5;
                }
            }
            return 1;
        }
        int position2 = position - this.mDataList.size();
        if (this.mHasFoot) {
            if (position2 == 0) {
                return 4;
            }
            position2--;
        }
        if (position2 == 0 && this.mHasLoad) {
            return 2;
        }
        throw new RuntimeException(" what happen ");
    }

    public int getViewTypeCount() {
        return this.mHolderConstructor.length + 5;
    }

    public boolean isEmpty() {
        return false;
    }

    public void registerDataSetObserver(DataSetObserver observer) {
        this.mDataSetObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        this.mDataSetObservable.unregisterObserver(observer);
    }

    public void notifyDataSetChanged() {
        this.mDataSetObservable.notifyChanged();
    }

    public void notifyDataSetInvalidated() {
        this.mDataSetObservable.notifyInvalidated();
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
            notifyDataSetChanged();
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
            this.mDataList.addAll(this.mDataList.size(), dataList);
            notifyDataSetChanged();
            setLoadMoreStatus(status);
            return;
        }
        this.mActivity.getWindow().getDecorView().postDelayed(new Runnable() {
            public void run() {
                MultipleListAdapter.this.mDataList.addAll(MultipleListAdapter.this.mDataList.size(), dataList);
                MultipleListAdapter.this.notifyDataSetChanged();
                MultipleListAdapter.this.setLoadMoreStatus(status);
            }
        }, (long) delayTime);
    }

    public T removeData(int position) {
        T ret = this.mDataList.remove(position);
        notifyDataSetChanged();
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
            switch (status) {
                case NO_MORE:
                    this.mLoadStatus = AdapterDelegate.LoadedStatus.NO_MORE;
                    break;
            }
            if (this.mLoad != null) {
                this.mLoad.progressBar.invalidate();
            }
        }
    }

    public void loadMore() {
    }

    public void completeLoadMore() {
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
            if (MultipleListAdapter.this.mSpaceView != null) {
                ViewParent parent = MultipleListAdapter.this.mSpaceView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(MultipleListAdapter.this.mSpaceView);
                }
                this.mainView.removeAllViews();
                this.mainView.addView(MultipleListAdapter.this.mSpaceView, new FrameLayout.LayoutParams(-1, -1));
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
        frameLayout.setLayoutParams(new AbsListView.LayoutParams(-1, -1));
        return frameLayout;
    }

    private class IncludeTypeError extends RecycleViewHolder<Object> {
        TextView errorInfo = ((TextView) this.itemView.findViewById(R.id.powyin_scroll_err_text));

        IncludeTypeError(ViewGroup viewGroup) {
            super(MultipleListAdapter.this.mActivity.getLayoutInflater().inflate(R.layout.powyin_scroll_multiple_adapter_err, viewGroup, false), (PowViewHolder) null);
        }

        /* access modifiers changed from: private */
        public void loadData(T data) {
            this.errorInfo.setText(data == null ? " you has one empty data inside " : data.toString());
        }
    }

    private class IncludeTypeLoad extends RecycleViewHolder<Object> {
        MultipleListAdapter<T>.LoadProgressBar progressBar = ((LoadProgressBar) this.itemView);

        IncludeTypeLoad(ViewGroup viewGroup) {
            super(new LoadProgressBar(MultipleListAdapter.this.mActivity), (PowViewHolder) null);
        }

        /* access modifiers changed from: package-private */
        public void ensureLoading() {
            if (MultipleListAdapter.this.mLoadStatus == null && MultipleListAdapter.this.mOnLoadMoreListener != null) {
                AdapterDelegate.LoadedStatus unused = MultipleListAdapter.this.mLoadStatus = null;
                MultipleListAdapter.this.mOnLoadMoreListener.onLoadMore();
            }
        }
    }

    private class IncludeTypeHead extends RecycleViewHolder<Object> {
        FrameLayout frameLayout = ((FrameLayout) this.itemView);

        IncludeTypeHead(ViewGroup viewGroup) {
            super(MultipleListAdapter.this.mActivity.getLayoutInflater().inflate(R.layout.powyin_scroll_multiple_adapter_head, viewGroup, false), (PowViewHolder) null);
        }

        /* access modifiers changed from: package-private */
        public void loadView() {
            if (MultipleListAdapter.this.mHeadView != null) {
                ViewParent parent = MultipleListAdapter.this.mHeadView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(MultipleListAdapter.this.mHeadView);
                }
                this.frameLayout.removeAllViews();
                this.frameLayout.addView(MultipleListAdapter.this.mHeadView, new FrameLayout.LayoutParams(-1, -2));
            }
        }
    }

    private class IncludeTypeFoot extends RecycleViewHolder<Object> {
        FrameLayout frameLayout = ((FrameLayout) this.itemView);

        IncludeTypeFoot(ViewGroup viewGroup) {
            super(MultipleListAdapter.this.mActivity.getLayoutInflater().inflate(R.layout.powyin_scroll_multiple_adapter_foot, viewGroup, false), (PowViewHolder) null);
        }

        /* access modifiers changed from: package-private */
        public void loadView() {
            if (MultipleListAdapter.this.mFootView != null) {
                ViewParent parent = MultipleListAdapter.this.mFootView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(MultipleListAdapter.this.mFootView);
                }
                this.frameLayout.removeAllViews();
                this.frameLayout.addView(MultipleListAdapter.this.mFootView, new FrameLayout.LayoutParams(-1, -2));
            }
        }
    }

    class LoadProgressBar extends View {
        ValueAnimator animator;
        int ballCount = 10;
        private long beginShowTime = System.currentTimeMillis();
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
            this.circlePaint.setColor(-1728053248);
            this.circlePaint.setStrokeWidth(4.0f);
            this.textPaint = new TextPaint();
            this.textPaint.setColor(-1728053248);
            this.textPaint.setTextSize((float) ((int) ((13.0f * context.getResources().getDisplayMetrics().scaledDensity) + 0.5f)));
            this.textPaint.setAntiAlias(true);
            this.textPaint.setStrokeWidth(1.0f);
        }

        /* access modifiers changed from: private */
        public void ensureAnimation(boolean forceReStart) {
            if (this.mAttached && MultipleListAdapter.this.mLoadStatus != AdapterDelegate.LoadedStatus.NO_MORE) {
                if (forceReStart) {
                    if (this.animator != null) {
                        this.animator.cancel();
                        this.animator = null;
                    }
                } else if (this.animator != null && this.animator.isRunning()) {
                    return;
                }
                this.animator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
                this.animator.setDuration(AbstractClientManager.BIND_SERVICE_TIMEOUT);
                this.animator.setRepeatCount(-1);
                this.animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        LoadProgressBar.this.divide = ((float) (8 * ((System.currentTimeMillis() % 3000) - 1500))) / 3000.0f;
                        LoadProgressBar.this.invalidate();
                    }
                });
                this.animator.start();
            } else if (this.animator != null) {
                this.animator.cancel();
                this.animator = null;
            }
        }

        private void ensureStopAnimation() {
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
            ensureAnimation(false);
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
            if (this.beginShowTime != -1) {
                float alpha = ((float) ((int) (System.currentTimeMillis() - this.beginShowTime))) / 2000.0f;
                if (alpha > 1.0f) {
                    alpha = 1.0f;
                }
                if (alpha < 0.0f) {
                    alpha = 1.0f;
                }
                int colorAlpha = (int) (200.0f * alpha);
                this.textPaint.setAlpha(colorAlpha);
                this.circlePaint.setAlpha(colorAlpha);
                if (MultipleListAdapter.this.mLoadStatus == AdapterDelegate.LoadedStatus.NO_MORE) {
                    canvas.drawText(MultipleListAdapter.this.mLoadCompleteInfo, this.canvasTextX, this.canvasTextY, this.textPaint);
                    canvas.drawLine(20.0f, (float) (this.canvasHei / 2), this.canvasTextX - 20.0f, (float) (this.canvasHei / 2), this.textPaint);
                    canvas.drawLine((((float) this.canvasWei) - this.canvasTextX) + 20.0f, (float) (this.canvasHei / 2), (float) (this.canvasWei - 20), (float) (this.canvasHei / 2), this.textPaint);
                }
                if (MultipleListAdapter.this.mLoadStatus == AdapterDelegate.LoadedStatus.ERROR) {
                    canvas.drawText(MultipleListAdapter.this.mLoadErrorInfo, this.canvasTextX, this.canvasTextY, this.textPaint);
                    canvas.drawLine(20.0f, (float) (this.canvasHei / 2), this.canvasTextX - 20.0f, (float) (this.canvasHei / 2), this.textPaint);
                    canvas.drawLine(20.0f + (((float) this.canvasWei) - this.canvasTextX), (float) (this.canvasHei / 2), (float) (this.canvasWei - 20), (float) (this.canvasHei / 2), this.textPaint);
                    return;
                }
                for (int i = 0; i < this.ballCount; i++) {
                    canvas.drawCircle(((float) (this.canvasWei / 2)) + (getSplit((4.0f * (((((float) i) * 1.0f) / ((float) this.ballCount)) - 0.5f)) + this.divide) * ((float) this.canvasWei) * 0.08f), (float) ((this.canvasHei / 2) + 6), 8.0f, this.circlePaint);
                }
            }
        }

        /* access modifiers changed from: protected */
        public void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            this.canvasHei = getHeight();
            this.canvasWei = getWidth();
            this.canvasTextX = ((float) (this.canvasWei / 2)) - (this.textPaint.measureText(MultipleListAdapter.this.mLoadCompleteInfo) / 2.0f);
            this.canvasTextY = ((float) (this.canvasHei / 2)) + (this.textPaint.getTextSize() / 2.55f);
            if (bottom < ((ViewGroup) getParent()).getHeight()) {
                this.beginShowTime = -1;
            } else {
                this.beginShowTime = System.currentTimeMillis();
            }
        }
    }
}
