package com.alibaba.appmonitor.sample;

import com.alibaba.analytics.core.db.annotation.Column;
import com.alibaba.analytics.core.db.annotation.TableName;
import java.util.ArrayList;

@TableName("ap_stat")
public class StatConfig extends AMConifg {
    @Column("detail")
    public int detail;

    public boolean isDetail() {
        if (this.detail == 1) {
            return true;
        }
        return false;
    }

    public boolean isDetail(String page, String monitorPoint) {
        ArrayList<String> keys = new ArrayList<>(2);
        keys.add(page);
        keys.add(monitorPoint);
        return detail(keys);
    }

    private boolean detail(ArrayList<String> keys) {
        if (keys == null || keys.size() == 0) {
            return isDetail();
        }
        String nextkey = keys.remove(0);
        if (isContains(nextkey)) {
            return ((StatConfig) getNext(nextkey)).detail(keys);
        }
        return isDetail();
    }
}
