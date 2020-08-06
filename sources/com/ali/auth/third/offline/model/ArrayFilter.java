package com.ali.auth.third.offline.model;

import android.widget.Filter;
import java.util.ArrayList;
import java.util.List;

public class ArrayFilter<T> extends Filter {
    private FilterListListener mFilterListener;
    private LoginHistoryAdapter<T> mHistoryAdapter;
    private final Object mLock = new Object();
    ArrayList<T> mOriginalValues;

    public ArrayFilter(ArrayList<T> originalValues, LoginHistoryAdapter<T> adapter, FilterListListener listener) {
        this.mOriginalValues = originalValues;
        this.mHistoryAdapter = adapter;
        this.mFilterListener = listener;
    }

    public void updateOriginData() {
        synchronized (this.mLock) {
            if (this.mHistoryAdapter.getHistoryList() != null) {
                this.mOriginalValues = new ArrayList<>(this.mHistoryAdapter.getHistoryList());
            } else {
                this.mOriginalValues = new ArrayList<>();
            }
        }
    }

    /* access modifiers changed from: protected */
    public Filter.FilterResults performFiltering(CharSequence prefix) {
        ArrayList<T> list;
        ArrayList<T> values;
        Filter.FilterResults results = new Filter.FilterResults();
        if (this.mOriginalValues == null) {
            synchronized (this.mLock) {
                if (this.mHistoryAdapter.getHistoryList() != null) {
                    this.mOriginalValues = new ArrayList<>(this.mHistoryAdapter.getHistoryList());
                } else {
                    this.mOriginalValues = new ArrayList<>();
                }
            }
        }
        if (prefix == null || prefix.length() == 0) {
            synchronized (this.mLock) {
                list = new ArrayList<>(this.mOriginalValues);
            }
            results.values = list;
            results.count = list.size();
        } else {
            String prefixString = prefix.toString().toLowerCase();
            synchronized (this.mLock) {
                values = new ArrayList<>(this.mOriginalValues);
            }
            int count = values.size();
            ArrayList<T> newValues = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                T value = values.get(i);
                if (value.toString().toLowerCase().startsWith(prefixString)) {
                    newValues.add(value);
                }
            }
            results.values = newValues;
            results.count = newValues.size();
        }
        return results;
    }

    /* access modifiers changed from: protected */
    public void publishResults(CharSequence constraint, Filter.FilterResults results) {
        this.mHistoryAdapter.resetList((List) results.values);
        if (results.count > 0) {
            this.mHistoryAdapter.notifyDataSetChanged();
        } else {
            this.mHistoryAdapter.notifyDataSetInvalidated();
        }
        if (this.mFilterListener != null) {
            this.mFilterListener.getFilterCount(results.count);
        }
    }
}
