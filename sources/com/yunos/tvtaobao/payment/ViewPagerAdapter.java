package com.yunos.tvtaobao.payment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {
    ArrayList<View> viewLists;

    public ViewPagerAdapter(ArrayList<View> lists) {
        this.viewLists = lists;
    }

    public int getCount() {
        if (this.viewLists == null) {
            return 0;
        }
        return this.viewLists.size();
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(this.viewLists.get(position));
    }

    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(this.viewLists.get(position));
        return this.viewLists.get(position);
    }
}
