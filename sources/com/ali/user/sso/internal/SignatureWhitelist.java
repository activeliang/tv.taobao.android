package com.ali.user.sso.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.Signature;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import com.ali.user.sso.utils.FileUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SignatureWhitelist {
    private static final String PREFS_KEY_TIMESTAMP = "timestamp";
    private static final String PREFS_KEY_WHITELIST = "whitelist";
    private static final String SHARED_PREFS_SSO = ".sso.whitelist";
    private static final String TAG = "Signature";
    Context mContext;
    private List<String> mSignatures;

    public boolean match(Signature[] signatures) {
        int i;
        try {
            if (this.mSignatures == null || this.mSignatures.size() == 0) {
                this.mSignatures = new ArrayList();
                i = 0;
                while (i < Whitelist.mWhitelist.length) {
                    this.mSignatures.add(Whitelist.mWhitelist[i]);
                    i++;
                }
            }
            if (this.mSignatures == null || this.mSignatures.size() == 0 || signatures == null || signatures.length <= 0) {
                return false;
            }
            int length = signatures.length;
            int i2 = 0;
            while (i2 < length) {
                Signature signature = signatures[i2];
                String signatureStr = signature != null ? signature.toCharsString() : "";
                Log.v("ssologin", "SignatureWhitelist match: input signature = " + signatureStr);
                if (TextUtils.isEmpty(signatureStr) || !this.mSignatures.contains(signatureStr)) {
                    i2++;
                } else {
                    Log.v("ssologin", "SignatureWhitelist matched");
                    return true;
                }
            }
            return false;
        } catch (RuntimeException e) {
            Log.w("Signature", "Malformed signature: " + i + " - (hidden for security)");
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    public synchronized boolean update(List<Signature> signatures, long new_timestamp) {
        boolean z;
        if (signatures != null) {
            if (signatures.size() != 0) {
                SharedPreferences storePreferences = getSharedPreferences();
                if (new_timestamp <= storePreferences.getLong("timestamp", 0)) {
                    z = false;
                } else {
                    Set<String> tempSet = new HashSet<>();
                    StringBuilder joiner = new StringBuilder();
                    if (this.mSignatures != null) {
                        for (String signatureStr : this.mSignatures) {
                            tempSet.add(signatureStr);
                            joiner.append(',').append(signatureStr);
                        }
                    }
                    for (Signature signature : signatures) {
                        tempSet.add(signature.toCharsString());
                        joiner.append(',').append(signature.toCharsString());
                    }
                    this.mSignatures = new ArrayList();
                    for (String add : tempSet) {
                        this.mSignatures.add(add);
                    }
                    storePreferences.edit().putLong("timestamp", new_timestamp).putString(PREFS_KEY_WHITELIST, "").apply();
                    FileUtils.writeFileData(this.mContext, PREFS_KEY_WHITELIST, joiner.substring(1));
                    z = true;
                }
            }
        }
        z = false;
        return z;
    }

    /* access modifiers changed from: package-private */
    public long getTimestamp() {
        return getSharedPreferences().getLong("timestamp", 0);
    }

    public SignatureWhitelist(Context context) {
        this.mContext = context.getApplicationContext();
        String whitelist = FileUtils.readFileData(this.mContext, PREFS_KEY_WHITELIST);
        if (TextUtils.isEmpty(whitelist)) {
            whitelist = getSharedPreferences().getString(PREFS_KEY_WHITELIST, (String) null);
            if (!TextUtils.isEmpty(whitelist)) {
                getSharedPreferences().edit().putString(PREFS_KEY_WHITELIST, "").apply();
                FileUtils.writeFileData(this.mContext, PREFS_KEY_WHITELIST, whitelist);
            }
        }
        this.mSignatures = new ArrayList();
        if (!TextUtils.isEmpty(whitelist)) {
            String[] entries = whitelist.split(",");
            for (int i = 0; i < entries.length; i++) {
                try {
                    this.mSignatures.add(entries[i]);
                } catch (RuntimeException e) {
                    Log.w("Signature", "Malformed signature: " + i + " - (hidden for security)");
                }
            }
        }
    }

    private SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(this.mContext);
    }
}
