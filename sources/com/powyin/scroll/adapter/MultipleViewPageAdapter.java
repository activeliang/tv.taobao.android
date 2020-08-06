package com.powyin.scroll.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.powyin.scroll.adapter.AdapterDelegate;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MultipleViewPageAdapter<T> extends PagerAdapter implements AdapterDelegate<T> {
    private Activity mActivity;
    private PowViewHolder[] mCache = new PowViewHolder[100];
    private List<T> mDataList = new ArrayList();
    private boolean mEnnableChach = true;
    private Constructor<? extends PowViewHolder>[] mHolderConstructor;
    private Class[] mHolderGenericDataClass;
    private PowViewHolder[] mHolderInstances;

    @SafeVarargs
    public static <T> MultipleViewPageAdapter<T> getByViewHolder(Activity activity, Class<? extends PowViewHolder<? extends T>>... arrClass) {
        return new MultipleViewPageAdapter<>(activity, arrClass);
    }

    @SafeVarargs
    public MultipleViewPageAdapter(Activity activity, Class<? extends PowViewHolder<? extends T>>... viewHolderClass) {
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
        for (int i2 = 0; i2 < clsArr.length; i2++) {
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
        }
    }

    public Object instantiateItem(ViewGroup container, int position) {
        PowViewHolder<T> target = null;
        T itemData = this.mDataList.get(position);
        if (this.mCache[position] != null) {
            PowViewHolder<T> target2 = this.mCache[position];
            container.addView(target2.mItemView);
            target2.mData = itemData;
            target2.onViewAttachedToWindow();
            return target2;
        }
        int i = 0;
        while (true) {
            if (i >= this.mHolderInstances.length) {
                break;
            } else if (itemData == null) {
                throw new RuntimeException("data must not be Null");
            } else {
                if (this.mHolderGenericDataClass[i].isAssignableFrom(itemData.getClass())) {
                    if (this.mHolderInstances[i] == null) {
                        try {
                            this.mHolderInstances[i] = (PowViewHolder) this.mHolderConstructor[i].newInstance(new Object[]{this.mActivity, null});
                            this.mHolderInstances[i].mViewHolder = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                    this.mHolderInstances[i].mPosition = position;
                    if (this.mHolderInstances[i].acceptData(itemData)) {
                        target = this.mHolderInstances[i];
                        this.mHolderInstances[i] = null;
                        break;
                    }
                }
                i++;
            }
        }
        if (target == null) {
            throw new RuntimeException("can not find holder to load the data");
        }
        if (this.mEnnableChach) {
            this.mCache[position] = target;
        }
        container.addView(target.mItemView);
        target.mData = itemData;
        target.loadData((AdapterDelegate<? super T>) null, itemData, position);
        target.onViewAttachedToWindow();
        return target;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        PowViewHolder target = (PowViewHolder) object;
        container.removeView(target.mItemView);
        target.onViewDetachedFromWindow();
    }

    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    public float getPageWidth(int position) {
        return super.getPageWidth(position);
    }

    public int getCount() {
        int size = this.mDataList.size();
        if (this.mCache.length < size) {
            PowViewHolder[] rep = new PowViewHolder[size];
            System.arraycopy(this.mCache, 0, rep, 0, this.mCache.length);
            this.mCache = rep;
        }
        return size;
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == ((PowViewHolder) object).mItemView;
    }

    public List<T> getDataList() {
        ArrayList<T> ret = new ArrayList<>();
        ret.addAll(this.mDataList);
        return ret;
    }

    public int getDataCount() {
        return this.mDataList.size();
    }

    public void loadData(List<T> dataList) {
        this.mDataList.clear();
        if (dataList != null && dataList.size() > 0) {
            this.mDataList.addAll(dataList);
        }
        notifyDataSetChanged();
    }

    public void addData(int position, T t) {
    }

    public void addData(int position, List<T> list) {
    }

    public void addDataAtLast(List<T> list) {
    }

    public void addDataAtLast(List<T> list, AdapterDelegate.LoadedStatus status, int delayTime) {
    }

    public T removeData(int position) {
        return null;
    }

    public void removeData(T t) {
    }

    public void clearData() {
    }

    public void enableLoadMore(boolean enable) {
    }

    public void setLoadMoreStatus(AdapterDelegate.LoadedStatus status) {
    }

    public void loadMore() {
    }

    public void completeLoadMore() {
    }

    public void setOnLoadMoreListener(AdapterDelegate.OnLoadMoreListener loadMoreListener) {
    }

    public void setHeadView(View view) {
        throw new RuntimeException("not Support");
    }

    public void setFootView(View view) {
        throw new RuntimeException("not Support");
    }

    public void removeHeadView() {
        throw new RuntimeException("not Support");
    }

    public void removeFootView() {
        throw new RuntimeException("not Support");
    }

    public void enableEmptyView(boolean show) {
        throw new RuntimeException("not Support");
    }

    public void setEmptyView(View view) {
        throw new RuntimeException("not Support");
    }

    public void setOnItemClickListener(AdapterDelegate.OnItemClickListener<T> onItemClickListener) {
        throw new RuntimeException("not Support");
    }

    public AdapterDelegate.OnItemClickListener<T> getOnItemClickListener() {
        throw new RuntimeException("not Support");
    }

    public void setOnItemLongClickListener(AdapterDelegate.OnItemLongClickListener<T> onItemLongClickListener) {
        throw new RuntimeException("not Support");
    }

    public void ennableChach(boolean ennable) {
        this.mEnnableChach = ennable;
    }

    public PowViewHolder<T> getPage(int index) {
        return this.mCache[index];
    }
}
