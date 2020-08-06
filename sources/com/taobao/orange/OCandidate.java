package com.taobao.orange;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.taobao.orange.aidl.OrangeCandidateCompareStub;
import com.taobao.orange.aidl.ParcelableCandidateCompare;
import com.taobao.orange.candidate.DefCandidateCompare;

public class OCandidate {
    private String clientVal;
    private ParcelableCandidateCompare compare;
    private String key;

    public OCandidate(@NonNull String key2, @Nullable String clientVal2, @NonNull ICandidateCompare compare2) {
        if (TextUtils.isEmpty(key2) || compare2 == null) {
            throw new IllegalArgumentException("key or compare is null");
        }
        this.key = key2;
        this.clientVal = clientVal2;
        this.compare = new OrangeCandidateCompareStub(compare2);
    }

    public OCandidate(@NonNull String key2, String clientVal2, @NonNull Class<? extends ICandidateCompare> compareClz) {
        if (TextUtils.isEmpty(key2) || compareClz == null) {
            throw new IllegalArgumentException("key or compare is null");
        }
        this.key = key2;
        this.clientVal = clientVal2;
        try {
            this.compare = new OrangeCandidateCompareStub((ICandidateCompare) compareClz.newInstance());
        } catch (Exception e) {
            this.compare = new OrangeCandidateCompareStub(new DefCandidateCompare());
        }
    }

    public OCandidate(@NonNull String key2, String clientVal2, ParcelableCandidateCompare compare2) {
        if (TextUtils.isEmpty(key2) || compare2 == null) {
            throw new IllegalArgumentException("key or compare is null");
        }
        this.key = key2;
        this.clientVal = clientVal2;
        this.compare = compare2;
    }

    public String getKey() {
        return this.key;
    }

    public String getClientVal() {
        return this.clientVal;
    }

    public ParcelableCandidateCompare getCompare() {
        return this.compare;
    }

    public String toString() {
        return String.format("%s=%s", new Object[]{this.key, this.clientVal});
    }
}
