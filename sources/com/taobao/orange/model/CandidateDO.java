package com.taobao.orange.model;

import android.text.TextUtils;
import com.taobao.orange.candidate.MultiAnalyze;
import com.taobao.orange.util.OLog;
import java.io.Serializable;

public class CandidateDO implements Serializable {
    private static final String TAG = "CandidateDO";
    public String match;
    public String md5;
    public String resourceId;
    public String version;

    /* access modifiers changed from: package-private */
    public boolean checkValid() {
        if (!TextUtils.isEmpty(this.resourceId) && !TextUtils.isEmpty(this.match) && !TextUtils.isEmpty(this.version)) {
            return true;
        }
        OLog.w(TAG, "lack param", new Object[0]);
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean checkMatch() {
        try {
            return MultiAnalyze.complie(this.match).match();
        } catch (Exception e) {
            OLog.e(TAG, "checkMatch", e, new Object[0]);
            return false;
        }
    }

    public String toString() {
        return String.format("CandidateDO{match:'%s', verison:'%s'}", new Object[]{this.match, this.version});
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CandidateDO that = (CandidateDO) o;
        if (this.resourceId != null) {
            if (!this.resourceId.equals(that.resourceId)) {
                return false;
            }
        } else if (that.resourceId != null) {
            return false;
        }
        if (this.match != null) {
            if (!this.match.equals(that.match)) {
                return false;
            }
        } else if (that.match != null) {
            return false;
        }
        if (this.version != null) {
            z = this.version.equals(that.version);
        } else if (that.version != null) {
            z = false;
        }
        return z;
    }
}
