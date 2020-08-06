package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Collections;
import java.util.List;

public class ParceledListSlice<T extends Parcelable> {
    private static boolean DEBUG = false;
    private static String TAG = "ParceledListSlice";

    public static <T extends Parcelable> ParceledListSlice<T> emptyList() {
        return new ParceledListSlice<>(Collections.emptyList());
    }

    public ParceledListSlice(List<T> list) {
    }

    private ParceledListSlice(Parcel p, ClassLoader loader) {
    }

    private static void verifySameType(Class<?> expected, Class<?> actual) {
        if (!actual.equals(expected)) {
            throw new IllegalArgumentException("Can't unparcel type " + actual.getName() + " in list of type " + expected.getName());
        }
    }

    public List<T> getList() {
        return null;
    }
}
