package com.yunos.tv.blitz.video;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.intertrust.wasabi.media.PlaylistProxy;
import com.yunos.tv.blitz.BlitzContextWrapper;
import com.yunos.tv.blitz.activity.BzBaseActivity;
import com.yunos.tv.blitz.data.BzResult;
import java.lang.ref.WeakReference;
import yunos.media.drm.DrmManagerCreator;
import yunos.media.drm.interfc.DrmManager;

public class BzDrmHelper {
    private static final String RESULT_FAIL = "error";
    private static final String RESULT_SUCC = "result";
    private static final String RESULT_URI = "uri";
    private static final String TAG = "BzDrmHelper";
    int mAddrCallback;
    DrmManager mDrmManager = null;
    private WeakReference<BzBaseActivity> mzActivityRef = null;

    public BzDrmHelper(WeakReference<BzBaseActivity> baseActivityRef, int addr_callback) {
        this.mAddrCallback = addr_callback;
        this.mzActivityRef = baseActivityRef;
    }

    public void drmVideoPlay(String url, String httpDns, String drmToken, Context context) {
        Log.d(TAG, "test drmVideoPlay drmToken = " + drmToken);
        try {
            this.mDrmManager = new DrmManagerCreator(url, context).createDrmManager();
            this.mDrmManager.setOnDrmErrorListener(new DrmManager.DrmErrorListener() {
                public void onErrorListener(DrmManager drmManager, int errorCode, int extra, Object msg) {
                }
            });
            Log.d(TAG, "makeUrl start");
            this.mDrmManager.makeUrl(drmToken, PlaylistProxy.MediaSourceType.HLS, new PlaylistProxy.MediaSourceParams(), new DrmManager.ICallBack() {
                public void onComplete(Uri uri, int errorCode) {
                    Log.d(BzDrmHelper.TAG, "makeUrl complete");
                    BzResult result = new BzResult();
                    if (uri == null || uri.toString().length() <= 0) {
                        result.addData(BzDrmHelper.RESULT_FAIL, errorCode);
                        BzDrmHelper.this.replyCallBack(result, false);
                        Log.w(BzDrmHelper.TAG, "drmManager Exception:errorCode=" + errorCode);
                        return;
                    }
                    Log.d(BzDrmHelper.TAG, "uri  = " + uri.toString());
                    result.addData("uri", uri.toString());
                    result.setSuccess();
                    BzDrmHelper.this.replyCallBack(result, true);
                }
            });
        } catch (Exception e) {
            Log.w(TAG, "drmManager Exception = " + Log.getStackTraceString(e));
        }
    }

    /* access modifiers changed from: package-private */
    public boolean replyCallBack(BzResult bzResult, boolean success) {
        BzBaseActivity blitzActivity;
        BlitzContextWrapper blitzContextWrapper;
        if (this.mzActivityRef == null || this.mzActivityRef.get() == null || (blitzActivity = (BzBaseActivity) this.mzActivityRef.get()) == null || (blitzContextWrapper = blitzActivity.getBlitzContext()) == null) {
            return false;
        }
        blitzContextWrapper.replyCallBack(this.mAddrCallback, success, bzResult.toJsonString());
        return true;
    }
}
