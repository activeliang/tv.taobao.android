package com.taobao.alimama.utils;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.alimama.global.Constants;
import com.taobao.alimama.io.SharedPreferencesUtils;
import com.taobao.alimama.login.LoginManager;
import com.taobao.muniontaobaosdk.util.TaoLog;
import com.taobao.orange.OrangeConfig;
import com.taobao.orange.OrangeConfigListenerV1;
import com.taobao.utils.ILoginInfoGetter;
import com.taobao.utils.LoginInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class BucketTools {
    private static final int a = 1000;

    /* renamed from: a  reason: collision with other field name */
    private static final String f8a = "bucket";

    /* renamed from: a  reason: collision with other field name */
    private static volatile List<String> f9a = null;
    private static final String b = "&";

    public static List<String> a() {
        if (f9a == null) {
            f9a = d();
            OrangeConfig.getInstance().registerListener(new String[]{Constants.ORANGE_GROUP_NAME}, (OrangeConfigListenerV1) new OrangeConfigListenerV1() {
                public void onConfigUpdate(String str, boolean z) {
                    TaoLog.Logd(Constants.TAG, String.format("onConfigUpdate in getBucketOnFixedStatus, fromCache=%s", new Object[]{String.valueOf(z)}));
                    List<String> b = BucketTools.b();
                    if (!b.equals(BucketTools.c())) {
                        BucketTools.b(b);
                    }
                }
            });
        }
        return f9a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m1a() {
        return isInBucketOnFixedStatus("adv_forward_with_fetch");
    }

    public static boolean a(String str) {
        return b().contains(str);
    }

    public static List<String> b() {
        return e();
    }

    /* access modifiers changed from: private */
    public static void b(List<String> list) {
        if (list.isEmpty()) {
            SharedPreferencesUtils.removeKey(f8a);
        } else {
            SharedPreferencesUtils.putString(f8a, TextUtils.join("&", list));
        }
    }

    /* renamed from: b  reason: collision with other method in class */
    public static boolean m2b() {
        return isInBucketOnFixedStatus("adv_view_opt");
    }

    public static boolean c() {
        return isInBucketOnFixedStatus("adv_new_ifs");
    }

    private static List<String> d() {
        String string = SharedPreferencesUtils.getString(f8a, "");
        return TextUtils.isEmpty(string) ? Collections.emptyList() : Arrays.asList(string.split("&"));
    }

    private static List<String> e() throws IllegalStateException {
        ILoginInfoGetter loginInfoGetter = LoginManager.getLoginInfoGetter();
        if (loginInfoGetter == null) {
            return Collections.emptyList();
        }
        LoginInfo lastLoginUserInfo = loginInfoGetter.getLastLoginUserInfo();
        if (lastLoginUserInfo == null || TextUtils.isEmpty(lastLoginUserInfo.userId)) {
            return Collections.emptyList();
        }
        long j = -1;
        try {
            j = Long.parseLong(lastLoginUserInfo.userId);
        } catch (NumberFormatException e) {
        }
        int i = j <= 0 ? 1000 : (int) (j % 1000);
        String config = OrangeConfig.getInstance().getConfig(Constants.ORANGE_GROUP_NAME, f8a, "");
        ArrayList arrayList = new ArrayList();
        try {
            JSONObject parseObject = JSONObject.parseObject(config);
            if (parseObject == null) {
                return Collections.emptyList();
            }
            for (String next : parseObject.keySet()) {
                JSONArray jSONArray = parseObject.getJSONArray(next);
                for (String split : (String[]) jSONArray.toArray(new String[jSONArray.size()])) {
                    String[] split2 = split.split("-");
                    if (split2.length == 2) {
                        try {
                            int parseInt = Integer.parseInt(split2[0]);
                            int parseInt2 = Integer.parseInt(split2[1]);
                            if (i >= parseInt && i <= parseInt2) {
                                arrayList.add(next);
                            }
                        } catch (NumberFormatException e2) {
                        }
                    }
                }
            }
            if (!arrayList.isEmpty()) {
                Collections.sort(arrayList);
            }
            return Collections.unmodifiableList(arrayList);
        } catch (Exception e3) {
        }
    }

    public static boolean isInBucketOnFixedStatus(String str) {
        return a().contains(str);
    }
}
