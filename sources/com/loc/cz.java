package com.loc;

/* compiled from: AmapContext */
public final class cz {
    public static boolean a = false;
    private static volatile Cdo b;

    public static Cdo a() {
        return b;
    }

    public static void a(Cdo doVar) {
        if (doVar != null) {
            b = doVar;
        }
    }
}
