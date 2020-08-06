package com.ali.auth.third.offline.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.ali.auth.third.core.model.HistoryAccount;
import com.ali.auth.third.offline.R;
import com.ali.auth.third.offline.login.util.AccountUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginHistoryAdapter<T> extends BaseAdapter implements Filterable {
    private ArrayFilter mFilter;
    private FilterListListener mFilterListener;
    protected LayoutInflater mInflater;
    private List<T> mObjects;
    private View.OnLongClickListener mOnDeleteListener;
    private View.OnClickListener mOnItemClickListener;

    public LoginHistoryAdapter(Context context, T[] objects) {
        init(context, Arrays.asList(objects));
    }

    public LoginHistoryAdapter(Context context, View.OnLongClickListener deleteListener, View.OnClickListener onItemClickListener, List<T> objects, FilterListListener filterListener) {
        this.mOnDeleteListener = deleteListener;
        this.mOnItemClickListener = onItemClickListener;
        this.mFilterListener = filterListener;
        init(context, objects);
    }

    public void afterDeleteHistory(List<T> object) {
        resetList(object);
        ((ArrayFilter) getFilter()).updateOriginData();
    }

    public void resetList(List<T> object) {
        this.mObjects = object;
    }

    public List<T> getHistoryList() {
        return this.mObjects;
    }

    private void init(Context context, List<T> objects) {
        this.mObjects = objects;
        if (context != null) {
            this.mInflater = LayoutInflater.from(context);
        } else {
            Log.e("LoginHistoryAdapter", "null context");
        }
    }

    public int getCount() {
        if (this.mObjects != null) {
            return this.mObjects.size();
        }
        return 0;
    }

    public T getItem(int arg0) {
        if (this.mObjects != null) {
            return this.mObjects.get(arg0);
        }
        return null;
    }

    public long getItemId(int arg0) {
        return (long) arg0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LoginHistoryAdapter<T>.HistoryViews historyViews;
        if (convertView == null) {
            convertView = this.mInflater.inflate(R.layout.security_recent_filter_item, parent, false);
            historyViews = new HistoryViews();
            historyViews.nameView = (TextView) convertView.findViewById(R.id.ali_user_drop_name);
            historyViews.clearButton = (TextView) convertView.findViewById(R.id.clearButton);
            convertView.setTag(historyViews);
        } else {
            historyViews = (HistoryViews) convertView.getTag();
        }
        HistoryAccount loginHistory = (HistoryAccount) getItem(position);
        historyViews.nameView.setText(AccountUtil.hideAccount(loginHistory.nick));
        historyViews.nameView.setTag(loginHistory);
        historyViews.nameView.setOnClickListener(this.mOnItemClickListener);
        historyViews.nameView.setOnLongClickListener(this.mOnDeleteListener);
        return convertView;
    }

    final class HistoryViews {
        public TextView clearButton;
        public TextView nameView;

        HistoryViews() {
        }
    }

    public Filter getFilter() {
        if (this.mFilter == null) {
            this.mFilter = new ArrayFilter((ArrayList) null, this, this.mFilterListener);
        }
        return this.mFilter;
    }
}
