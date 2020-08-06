package com.taobao.ju.track;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import com.taobao.ju.track.constants.Constants;
import com.taobao.ju.track.impl.CtrlTrackImpl;
import com.taobao.ju.track.impl.ExtTrackImpl;
import com.taobao.ju.track.impl.PageTrackImpl;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class JTrack {
    private static Map<String, CtrlTrackImpl> mCtrlTracks;
    private static Map<String, ExtTrackImpl> mExtTracks;
    private static Map<String, PageTrackImpl> mPageTracks;
    /* access modifiers changed from: private */
    public static Context sSystemContext;

    public static void init(Context context) {
        sSystemContext = context;
    }

    public static void setPosStartFromOne(boolean startFromOne) {
        Constants.setPosStartFromOne(startFromOne);
    }

    public static PageTrackImpl getPage(String trackCsvFileName) {
        synchronized (PageTrackImpl.class) {
            if (mPageTracks != null) {
                mPageTracks = new HashMap();
            }
            if (mPageTracks.containsKey(trackCsvFileName)) {
                PageTrackImpl pageTrackImpl = mPageTracks.get(trackCsvFileName);
                return pageTrackImpl;
            }
            PageTrackImpl impl = new PageTrackImpl(sSystemContext, trackCsvFileName);
            mPageTracks.put(trackCsvFileName, impl);
            return impl;
        }
    }

    public static synchronized CtrlTrackImpl getCtrl(String trackCsvFileName) {
        CtrlTrackImpl ctrlTrackImpl;
        synchronized (JTrack.class) {
            synchronized (CtrlTrackImpl.class) {
                if (mCtrlTracks != null) {
                    mCtrlTracks = new HashMap();
                }
                if (mCtrlTracks.containsKey(trackCsvFileName)) {
                    ctrlTrackImpl = mCtrlTracks.get(trackCsvFileName);
                } else {
                    CtrlTrackImpl impl = new CtrlTrackImpl(sSystemContext, trackCsvFileName);
                    mCtrlTracks.put(trackCsvFileName, impl);
                    ctrlTrackImpl = impl;
                }
            }
        }
        return ctrlTrackImpl;
    }

    public static synchronized ExtTrackImpl getExt(String trackCsvFileName) {
        ExtTrackImpl extTrackImpl;
        synchronized (JTrack.class) {
            synchronized (ExtTrackImpl.class) {
                if (mExtTracks != null) {
                    mExtTracks = new HashMap();
                }
                if (mExtTracks.containsKey(trackCsvFileName)) {
                    extTrackImpl = mExtTracks.get(trackCsvFileName);
                } else {
                    ExtTrackImpl impl = new ExtTrackImpl(sSystemContext, trackCsvFileName);
                    mExtTracks.put(trackCsvFileName, impl);
                    extTrackImpl = impl;
                }
            }
        }
        return extTrackImpl;
    }

    public static final class Page {
        private static PageTrackImpl sTrack;
        private static String sTrackCsvFileName;

        private Page() {
        }

        public static synchronized PageTrackImpl getTrack() {
            PageTrackImpl pageTrackImpl;
            synchronized (Page.class) {
                if (sTrack == null) {
                    sTrack = new PageTrackImpl(JTrack.sSystemContext, sTrackCsvFileName);
                }
                pageTrackImpl = sTrack;
            }
            return pageTrackImpl;
        }

        public static void setCsvFileName(String fileName) {
            sTrack = null;
            sTrackCsvFileName = fileName;
        }

        public static boolean hasPoint(String rowKey) {
            return getTrack().hasPoint(rowKey);
        }

        public static boolean hasParam(String rowKey, String colKey) {
            return getTrack().hasParam(rowKey, colKey);
        }

        public static String[] getParamKvs(String rowKey) {
            return getTrack().getParamKvs(rowKey);
        }

        public static Properties getParamProp(String rowKey) {
            return getTrack().getParamProp(rowKey);
        }

        public static Map<String, String> getParamMap(String rowKey) {
            return getTrack().getParamMap(rowKey);
        }

        public static String getParamValue(String rowKey, String colKey) {
            return getTrack().getParamValue(rowKey, colKey);
        }

        public static String getParamValue(String rowKey, String colKey, String defaultValue) {
            return getTrack().getParamValue(rowKey, colKey, defaultValue);
        }

        public static Map<String, String> getStatic(String rowKey) {
            return getTrack().getStatic(rowKey);
        }

        public static Map<String, String> getRefer(String rowKey) {
            return getTrack().getRefer(rowKey);
        }

        public static Map<String, String> getDynamic(String rowKey) {
            return getTrack().getDynamic(rowKey);
        }

        public static boolean isInternal(String rowKey, String colKey) {
            return getTrack().isInternal(rowKey, colKey);
        }

        public static boolean isStatic(String rowKey, String colKey) {
            return getTrack().isStatic(rowKey, colKey);
        }

        public static boolean isRefer(String rowKey, String colKey) {
            return getTrack().isRefer(rowKey, colKey);
        }

        public static boolean isDynamic(String rowKey, String colKey) {
            return getTrack().isDynamic(rowKey, colKey);
        }

        public static boolean isValidateToUt(String paramValue) {
            return getTrack().isValidateToUT(paramValue);
        }

        public static String getPageName(String activityName) {
            return getTrack().getPageName(activityName);
        }

        public static String getPageName(Activity activity) {
            return getTrack().getPageName(activity);
        }

        public static String getSpm(String pageNameOrActivityName) {
            return getTrack().getSpm(pageNameOrActivityName);
        }

        public static String getSpm(Activity activity) {
            return getTrack().getSpm(activity);
        }

        public static Properties getSpmAsProp(Activity activity) {
            return getTrack().getSpmAsProp(activity);
        }

        public static Map<String, String> getSpmAsMap(Activity activity) {
            return getTrack().getSpmAsMap(activity);
        }

        public static String getSpmAB(String pageNameOrActivityName) {
            return getTrack().getSpmAB(pageNameOrActivityName);
        }

        public static String getSpmAB(Activity activity) {
            return getTrack().getSpmAB(activity);
        }

        public static String getArgs(Activity activity) {
            return getTrack().getArgs(activity);
        }

        public static String getArgs(String pageNameOrActivityName) {
            return getTrack().getArgs(pageNameOrActivityName);
        }

        public static Map<String, String> getArgsMap(Activity activity, Bundle bundle) {
            return getTrack().getArgsMap(activity, bundle);
        }

        public static Map<String, String> getArgsMap(String pageNameOrActivityName, Bundle bundle) {
            return getTrack().getArgsMap(pageNameOrActivityName, bundle);
        }

        public static Map<String, String> getArgsMap(Activity activity, Uri uri) {
            return getTrack().getArgsMap(activity, uri);
        }

        public static Map<String, String> getArgsMap(String pageNameOrActivityName, Uri uri) {
            return getTrack().getArgsMap(pageNameOrActivityName, uri);
        }
    }

    public static class Ctrl {
        private static CtrlTrackImpl sTrack;
        private static String sTrackCsvFileName;

        private Ctrl() {
        }

        public static synchronized CtrlTrackImpl getTrack() {
            CtrlTrackImpl ctrlTrackImpl;
            synchronized (Ctrl.class) {
                if (sTrack == null) {
                    sTrack = new CtrlTrackImpl(JTrack.sSystemContext, sTrackCsvFileName);
                }
                ctrlTrackImpl = sTrack;
            }
            return ctrlTrackImpl;
        }

        public static void setCsvFileName(String fileName) {
            sTrack = null;
            sTrackCsvFileName = fileName;
        }

        public static boolean hasPoint(String rowKey) {
            return getTrack().hasPoint(rowKey);
        }

        public static boolean hasParam(String rowKey, String colKey) {
            return getTrack().hasParam(rowKey, colKey);
        }

        public static String[] getParamKvs(String rowKey) {
            return getTrack().getParamKvs(rowKey);
        }

        public static Properties getParamProp(String rowKey) {
            return getTrack().getParamProp(rowKey);
        }

        public static Map<String, String> getParamMap(String rowKey) {
            return getTrack().getParamMap(rowKey);
        }

        public static String getParamValue(String rowKey, String colKey) {
            return getTrack().getParamValue(rowKey, colKey);
        }

        public static String getParamValue(String rowKey, String colKey, String defaultValue) {
            return getTrack().getParamValue(rowKey, colKey, defaultValue);
        }

        public static Map<String, String> getStatic(String rowKey) {
            return getTrack().getStatic(rowKey);
        }

        public static Map<String, String> getRefer(String rowKey) {
            return getTrack().getRefer(rowKey);
        }

        public static Map<String, String> getDynamic(String rowKey) {
            return getTrack().getDynamic(rowKey);
        }

        public static boolean isInternal(String rowKey, String colKey) {
            return getTrack().isInternal(rowKey, colKey);
        }

        public static boolean isStatic(String rowKey, String colKey) {
            return getTrack().isStatic(rowKey, colKey);
        }

        public static boolean isRefer(String rowKey, String colKey) {
            return getTrack().isRefer(rowKey, colKey);
        }

        public static boolean isDynamic(String rowKey, String colKey) {
            return getTrack().isDynamic(rowKey, colKey);
        }

        public static boolean isValidateToUt(String paramValue) {
            return getTrack().isValidateToUT(paramValue);
        }

        public static String getSpm(String pageNameOrActivityName, String rowKey) {
            return getTrack().getSpm(pageNameOrActivityName, rowKey);
        }

        public static String getSpm(Activity activity, String rowKey) {
            return getTrack().getSpm(activity, rowKey);
        }
    }

    public static class Ext {
        private static ExtTrackImpl sTrack;
        private static String sTrackCsvFileName;

        private Ext() {
        }

        public static synchronized ExtTrackImpl getTrack() {
            ExtTrackImpl extTrackImpl;
            synchronized (Ext.class) {
                if (sTrack == null) {
                    sTrack = new ExtTrackImpl(JTrack.sSystemContext, sTrackCsvFileName);
                }
                extTrackImpl = sTrack;
            }
            return extTrackImpl;
        }

        public static void setCsvFileName(String fileName) {
            sTrack = null;
            sTrackCsvFileName = fileName;
        }

        public static boolean hasPoint(String rowKey) {
            return getTrack().hasPoint(rowKey);
        }

        public static boolean hasParam(String rowKey, String colKey) {
            return getTrack().hasParam(rowKey, colKey);
        }

        public static String[] getParamKvs(String rowKey) {
            return getTrack().getParamKvs(rowKey);
        }

        public static Properties getParamProp(String rowKey) {
            return getTrack().getParamProp(rowKey);
        }

        public static Map<String, String> getParamMap(String rowKey) {
            return getTrack().getParamMap(rowKey);
        }

        public static String getParamValue(String rowKey, String colKey) {
            return getTrack().getParamValue(rowKey, colKey);
        }

        public static String getParamValue(String rowKey, String colKey, String defaultValue) {
            return getTrack().getParamValue(rowKey, colKey, defaultValue);
        }

        public static Map<String, String> getStatic(String rowKey) {
            return getTrack().getStatic(rowKey);
        }

        public static Map<String, String> getRefer(String rowKey) {
            return getTrack().getRefer(rowKey);
        }

        public static Map<String, String> getDynamic(String rowKey) {
            return getTrack().getDynamic(rowKey);
        }

        public static boolean isInternal(String rowKey, String colKey) {
            return getTrack().isInternal(rowKey, colKey);
        }

        public static boolean isStatic(String rowKey, String colKey) {
            return getTrack().isStatic(rowKey, colKey);
        }

        public static boolean isRefer(String rowKey, String colKey) {
            return getTrack().isRefer(rowKey, colKey);
        }

        public static boolean isDynamic(String rowKey, String colKey) {
            return getTrack().isDynamic(rowKey, colKey);
        }

        public static boolean isValidateToUt(String paramValue) {
            return getTrack().isValidateToUT(paramValue);
        }
    }
}
