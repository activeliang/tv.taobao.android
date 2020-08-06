package com.uc.webview.export.extension;

import com.uc.webview.export.annotations.Api;

@Api
/* compiled from: ProGuard */
public class BreakpadConfig {
    public String mCrashDir = "";
    public String mCrashLogFileName = "";
    public String mCrashLogPrefix = null;
    public boolean mEnableEncryptLog = true;
    public boolean mEnableJavaLog = true;
    public String mUploadUrl = "http://119.147.224.154:8012/upload";

    public BreakpadConfig(String str) {
        this.mCrashDir = str;
    }
}
