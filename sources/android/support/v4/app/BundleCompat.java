package android.support.v4.app;

import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

public final class BundleCompat {
    private BundleCompat() {
    }

    public static IBinder getBinder(Bundle bundle, String key) {
        if (Build.VERSION.SDK_INT >= 18) {
            return BundleCompatJellybeanMR2.getBinder(bundle, key);
        }
        return BundleCompatGingerbread.getBinder(bundle, key);
    }

    public static void putBinder(Bundle bundle, String key, IBinder binder) {
        if (Build.VERSION.SDK_INT >= 18) {
            BundleCompatJellybeanMR2.putBinder(bundle, key, binder);
        } else {
            BundleCompatGingerbread.putBinder(bundle, key, binder);
        }
    }
}
