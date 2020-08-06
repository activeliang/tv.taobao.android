package com.ali.user.sso.internal;

import android.util.SparseArray;

public class TokenInfo {
    public int mPid;
    public SparseArray<Long> mTokens = new SparseArray<>();
    public int mUid;
}
