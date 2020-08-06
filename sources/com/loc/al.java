package com.loc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;

/* compiled from: SPUtil */
public final class al {
    private String a;

    public al(String str) {
        this.a = r.a(TextUtils.isDigitsOnly(str) ? "SPUtil" : str);
    }

    public final void a(Context context, String str, String str2) {
        if (context != null && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            try {
                SharedPreferences.Editor edit = context.getSharedPreferences(this.a, 0).edit();
                edit.putString(str, u.g(v.a(context, u.a(str2))));
                if (edit == null) {
                    return;
                }
                if (Build.VERSION.SDK_INT >= 9) {
                    edit.apply();
                } else {
                    edit.commit();
                }
            } catch (Throwable th) {
            }
        }
    }

    public final void a(Context context, String str, boolean z) {
        a(context, str, Boolean.toString(z));
    }

    public final boolean a(Context context, String str) {
        try {
            return Boolean.parseBoolean(b(context, str));
        } catch (Throwable th) {
            return true;
        }
    }

    public final String b(Context context, String str) {
        if (context == null) {
            return "";
        }
        try {
            return u.a(v.b(context, u.d(context.getSharedPreferences(this.a, 0).getString(str, ""))));
        } catch (Throwable th) {
            return "";
        }
    }
}
