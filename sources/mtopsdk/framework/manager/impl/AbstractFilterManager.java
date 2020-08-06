package mtopsdk.framework.manager.impl;

import java.util.LinkedList;
import java.util.List;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.domain.FilterResult;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.filter.IAfterFilter;
import mtopsdk.framework.filter.IBeforeFilter;
import mtopsdk.framework.manager.FilterManager;

public abstract class AbstractFilterManager implements FilterManager {
    private static final String TAG = "mtopsdk.AbstractFilterManager";
    protected final List<IAfterFilter> afterFilters = new LinkedList();
    protected final List<IBeforeFilter> beforeFilters = new LinkedList();

    public void addBefore(IBeforeFilter filter) {
        this.beforeFilters.add(filter);
    }

    public void addAfter(IAfterFilter filter) {
        this.afterFilters.add(filter);
    }

    public void start(String filterName, MtopContext mtopContext) {
        boolean find = StringUtils.isBlank(filterName);
        for (IBeforeFilter filter : this.beforeFilters) {
            if (!find) {
                if (!filterName.equals(filter.getName())) {
                    continue;
                } else {
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                        TBSdkLog.i(TAG, mtopContext.seqNo, "[start]jump to beforeFilter:" + filterName);
                    }
                    find = true;
                }
            }
            long startTime = System.currentTimeMillis();
            String result = filter.doBefore(mtopContext);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                TBSdkLog.d(TAG, mtopContext.seqNo, "[start]execute BeforeFilter: " + filter.getName() + ",time(ms)= " + (System.currentTimeMillis() - startTime));
            }
            if (result == null || FilterResult.STOP == result) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, mtopContext.seqNo, "[start]execute BeforeFilter: " + filter.getName() + ",result=" + result);
                    return;
                }
                return;
            }
        }
    }

    public void callback(String filterName, MtopContext mtopContext) {
        boolean find = StringUtils.isBlank(filterName);
        for (IAfterFilter filter : this.afterFilters) {
            if (!find) {
                if (!filterName.equals(filter.getName())) {
                    continue;
                } else {
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                        TBSdkLog.i(TAG, mtopContext.seqNo, "[callback]jump to afterFilter:" + filterName);
                    }
                    find = true;
                }
            }
            long startTime = System.currentTimeMillis();
            String result = filter.doAfter(mtopContext);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                TBSdkLog.d(TAG, mtopContext.seqNo, "[callback]execute AfterFilter: " + filter.getName() + ",time(ms)= " + (System.currentTimeMillis() - startTime));
            }
            if (result == null || FilterResult.STOP == result) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, mtopContext.seqNo, "[callback]execute AfterFilter: " + filter.getName() + ",result=" + result);
                    return;
                }
                return;
            }
        }
    }
}
