package com.ut.mini.crashhandler;

import android.text.TextUtils;
import com.alibaba.analytics.core.device.Constants;
import com.alibaba.analytics.utils.MD5Utils;
import java.io.PrintWriter;
import java.io.StringWriter;
import mtopsdk.common.util.SymbolExpUtil;

public class UTExceptionParser {
    public static UTExceptionItem parse(Throwable pException) {
        if (pException == null) {
            return null;
        }
        UTExceptionItem lExceptionItem = new UTExceptionItem();
        Throwable lCause = pException.getCause();
        if (lCause == null) {
            lCause = pException;
        }
        if (lCause == null) {
            return lExceptionItem;
        }
        StackTraceElement[] stes = lCause.getStackTrace();
        if (stes.length <= 0 || stes[0] == null) {
            return lExceptionItem;
        }
        String sException = lCause.toString();
        String message = "";
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            pException.printStackTrace(pw);
            message = sw.toString();
            try {
                pw.close();
                sw.close();
            } catch (Exception e) {
            }
        } catch (Exception e2) {
            try {
                pw.close();
                sw.close();
            } catch (Exception e3) {
            }
        } catch (Throwable th) {
            try {
                pw.close();
                sw.close();
            } catch (Exception e4) {
            }
            throw th;
        }
        int kPos = sException.indexOf("}:");
        if (kPos > 0) {
            sException = sException.substring(kPos + 2).trim();
        } else {
            int kPos2 = sException.indexOf(SymbolExpUtil.SYMBOL_COLON);
            if (kPos2 > 0) {
                sException = sException.substring(0, kPos2);
            }
        }
        lExceptionItem.setExpName(sException);
        if (!TextUtils.isEmpty(message)) {
            message = message.replaceAll("\n", "++");
        }
        lExceptionItem.setCrashDetail(message);
        lExceptionItem.setMd5(MD5Utils.getMd5Hex(message.getBytes()));
        if (message.contains(Constants.PACKAGE_NAME) || message.contains("com.ut") || message.contains(Constants.PACKAGE_NAME2)) {
            lExceptionItem.setmCrashedByUT(true);
            return lExceptionItem;
        }
        lExceptionItem.setmCrashedByUT(false);
        return lExceptionItem;
    }

    public static class UTExceptionItem {
        String mCrashDetail = null;
        boolean mCrashedByUT = false;
        String mExpName = null;
        String mMd5 = null;

        public String getExpName() {
            return this.mExpName;
        }

        public void setExpName(String aExpName) {
            this.mExpName = aExpName;
        }

        public String getMd5() {
            return this.mMd5;
        }

        public void setMd5(String aMd5) {
            this.mMd5 = aMd5;
        }

        public String getCrashDetail() {
            return this.mCrashDetail;
        }

        public void setCrashDetail(String aCrashDetail) {
            this.mCrashDetail = aCrashDetail;
        }

        public boolean isCrashedByUT() {
            return this.mCrashedByUT;
        }

        public void setmCrashedByUT(boolean mCrashedByUT2) {
            this.mCrashedByUT = mCrashedByUT2;
        }
    }
}
