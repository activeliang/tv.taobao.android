package com.powyin.scroll.adapter;

import android.view.View;
import java.util.List;

public interface AdapterDelegate<T> {

    public enum LoadedStatus {
        ERROR,
        NO_MORE
    }

    public interface OnItemClickListener<T> {
        void onClick(PowViewHolder<T> powViewHolder, T t, int i, int i2);
    }

    public interface OnItemLongClickListener<T> {
        boolean onLongClick(PowViewHolder<T> powViewHolder, T t, int i, int i2);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    void addData(int i, T t);

    void addData(int i, List<T> list);

    void addDataAtLast(List<T> list);

    void addDataAtLast(List<T> list, LoadedStatus loadedStatus, int i);

    void clearData();

    void completeLoadMore();

    void enableEmptyView(boolean z);

    void enableLoadMore(boolean z);

    int getDataCount();

    List<T> getDataList();

    OnItemClickListener<T> getOnItemClickListener();

    void loadData(List<T> list);

    void loadMore();

    T removeData(int i);

    void removeData(T t);

    void removeFootView();

    void removeHeadView();

    void setEmptyView(View view);

    void setFootView(View view);

    void setHeadView(View view);

    void setLoadMoreStatus(LoadedStatus loadedStatus);

    void setOnItemClickListener(OnItemClickListener<T> onItemClickListener);

    void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener);

    void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener);
}
