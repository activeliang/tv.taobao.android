package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Pair;
import android.webkit.CookieSyncManager;
import com.ali.auth.third.core.model.KernelMessageConstants;
import com.uc.webview.export.cyclone.UCElapseTime;
import com.uc.webview.export.extension.ILocationManager;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.c.b;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.interfaces.IGlobalSettings;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.interfaces.UCMobileWebKit;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.c;
import com.uc.webview.export.utility.Utils;

/* compiled from: ProGuard */
public final class q extends UCSubSetupTask<q, q> {
    private static final boolean a = (Build.VERSION.SDK_INT >= 14);
    private boolean b = true;

    /* compiled from: ProGuard */
    class a implements ILocationManager {
        private LocationManager b;

        public a(Context context) {
            this.b = (LocationManager) context.getSystemService("location");
        }

        public final void requestLocationUpdates(String str, long j, float f, LocationListener locationListener) {
            if (this.b != null) {
                this.b.requestLocationUpdates(str, j, f, locationListener);
            }
        }

        public final void requestLocationUpdatesWithUrl(String str, long j, float f, LocationListener locationListener, String str2) {
            if (this.b != null) {
                this.b.requestLocationUpdates(str, j, f, locationListener);
            }
        }

        public final void removeUpdates(LocationListener locationListener) {
            if (this.b != null) {
                this.b.removeUpdates(locationListener);
            }
        }
    }

    /* access modifiers changed from: protected */
    public final void a() {
        int i;
        String str;
        IGlobalSettings iGlobalSettings;
        boolean z = false;
        UCElapseTime uCElapseTime = new UCElapseTime();
        Context context = (Context) this.mOptions.get("CONTEXT");
        boolean a2 = c.a((Boolean) this.mOptions.get(UCCore.OPTION_USE_SDK_SETUP));
        if (!a2) {
            b.a(context);
            CookieSyncManager.createInstance(context);
        }
        boolean z2 = d.h == -1 ? a && Utils.checkSupportSamplerExternalOES() : d.h == 1;
        UCMobileWebKit a3 = b.a(context, z2, false);
        if (!a2) {
            boolean z3 = ((Integer) d.a(10042, new Object[0])).intValue() == -1 ? a : ((Integer) d.a(10042, new Object[0])).intValue() == 1;
            if (!z2 && z3) {
                Log.e("InitTask", "UC Core not support Hardware accelerated.");
                z3 = false;
            }
            if (Build.VERSION.SDK_INT < 14) {
                if (z3) {
                    Log.e("InitTask", "Video Hardware accelerated is supported start at api level 14 and now is " + Build.VERSION.SDK_INT);
                }
                b.k().setBoolValue("video_hardward_accelerate", false);
            } else {
                b.k().setBoolValue("video_hardward_accelerate", z3);
                if (z3) {
                    d.a(10001, 1048576L);
                }
            }
            a3.setLocationManagerUC(new a(context));
            if (((Boolean) d.a(10010, new Object[0])).booleanValue() && !d.j && ((Integer) d.a((int) KernelMessageConstants.PARAM_ERROR, new Object[0])).intValue() == 2) {
                throw new UCSetupException(3016, String.format("Init success but disallow switch from android to [%d].", new Object[]{b.r()}));
            }
        }
        if (((Boolean) UCMPackageInfo.invoke(10011, new Object[0])).booleanValue()) {
            i = 1;
        } else {
            i = this.mUCM.isSpecified ? 2 : 3;
        }
        d.l = i;
        d.d = a3;
        int intValue = b.r().intValue();
        if (intValue == 3) {
            d.a((int) UCMPackageInfo.compareVersion, new r(this));
        }
        d.a(10021, Integer.valueOf(intValue));
        if (!a2) {
            d.a((int) UCMPackageInfo.expectDirFile1S, new Object[0]);
            if (!this.mUCM.isSpecified) {
                if (((Boolean) d.a((int) UCMPackageInfo.expectCreateDirFile2P, context, this.mUCM.pkgName)).booleanValue()) {
                    d.a(10001, Long.valueOf(PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE_ENABLED));
                }
            }
            d.a(10034, context);
            IGlobalSettings iGlobalSettings2 = (IGlobalSettings) d.a(10022, new Object[0]);
            if (iGlobalSettings2 != null) {
                if (this.mOptions.containsKey(UCCore.OPTION_WAP_DENY)) {
                    Object obj = this.mOptions.get(UCCore.OPTION_WAP_DENY);
                    iGlobalSettings2.setStringValue("SDKWapDeny", obj == null ? "" : obj.toString());
                    Log.d("InitTask", "initProxySettings: setStringValue: SDKWapDeny = " + obj.toString());
                }
                if (this.mOptions.containsKey(UCCore.OPTION_UC_PROXY_ADBLOCK)) {
                    Object obj2 = this.mOptions.get(UCCore.OPTION_UC_PROXY_ADBLOCK);
                    if (obj2 instanceof String) {
                        z = Boolean.parseBoolean((String) obj2);
                        str = "SDKAdBlock";
                        iGlobalSettings = iGlobalSettings2;
                    } else if (obj2 == null) {
                        str = "SDKAdBlock";
                        iGlobalSettings = iGlobalSettings2;
                    } else {
                        z = ((Boolean) obj2).booleanValue();
                        str = "SDKAdBlock";
                        iGlobalSettings = iGlobalSettings2;
                    }
                    iGlobalSettings.setBoolValue(str, z);
                }
                iGlobalSettings2.setStringValue("UBISiProfileId", com.uc.webview.export.Build.SDK_PROFILE_ID);
                iGlobalSettings2.setStringValue("UBISiPrd", com.uc.webview.export.Build.SDK_PRD);
            }
        }
        callbackStat(new Pair(IWaStat.SETUP_TASK_INIT, new s(this, a2, uCElapseTime)));
        callbackStat(new Pair(IWaStat.SETUP_SUCCESS_INITED, (Object) null));
    }

    public final void run() {
        Pair<Integer, Object> a2;
        int i;
        String str;
        IGlobalSettings iGlobalSettings;
        String obj;
        boolean z = false;
        if (this.b) {
            this.b = c.a((Boolean) getOption(UCCore.OPTION_INIT_IN_SETUP_THREAD));
        }
        if (!this.b) {
            UCElapseTime uCElapseTime = new UCElapseTime();
            Context context = (Context) this.mOptions.get("CONTEXT");
            boolean a3 = c.a((Boolean) this.mOptions.get(UCCore.OPTION_USE_SDK_SETUP));
            if (!a3) {
                b.a(context);
                CookieSyncManager.createInstance(context);
            }
            boolean z2 = d.h == -1 ? a && Utils.checkSupportSamplerExternalOES() : d.h == 1;
            UCMobileWebKit a4 = b.a(context, z2, false);
            if (!a3) {
                boolean z3 = ((Integer) d.a(10042, new Object[0])).intValue() == -1 ? a : ((Integer) d.a(10042, new Object[0])).intValue() == 1;
                if (!z2 && z3) {
                    Log.e("InitTask", "UC Core not support Hardware accelerated.");
                    z3 = false;
                }
                if (Build.VERSION.SDK_INT < 14) {
                    if (z3) {
                        Log.e("InitTask", "Video Hardware accelerated is supported start at api level 14 and now is " + Build.VERSION.SDK_INT);
                    }
                    b.k().setBoolValue("video_hardward_accelerate", false);
                } else {
                    b.k().setBoolValue("video_hardward_accelerate", z3);
                    if (z3) {
                        d.a(10001, 1048576L);
                    }
                }
                a4.setLocationManagerUC(new a(context));
                if (((Boolean) d.a(10010, new Object[0])).booleanValue() && !d.j && ((Integer) d.a((int) KernelMessageConstants.PARAM_ERROR, new Object[0])).intValue() == 2) {
                    throw new UCSetupException(3016, String.format("Init success but disallow switch from android to [%d].", new Object[]{b.r()}));
                }
            }
            if (((Boolean) UCMPackageInfo.invoke(10011, new Object[0])).booleanValue()) {
                i = 1;
            } else {
                i = this.mUCM.isSpecified ? 2 : 3;
            }
            d.l = i;
            d.d = a4;
            int intValue = b.r().intValue();
            if (intValue == 3) {
                d.a((int) UCMPackageInfo.compareVersion, new r(this));
            }
            d.a(10021, Integer.valueOf(intValue));
            if (!a3) {
                d.a((int) UCMPackageInfo.expectDirFile1S, new Object[0]);
                if (!this.mUCM.isSpecified) {
                    if (((Boolean) d.a((int) UCMPackageInfo.expectCreateDirFile2P, context, this.mUCM.pkgName)).booleanValue()) {
                        d.a(10001, Long.valueOf(PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE_ENABLED));
                    }
                }
                d.a(10034, context);
                IGlobalSettings iGlobalSettings2 = (IGlobalSettings) d.a(10022, new Object[0]);
                if (iGlobalSettings2 != null) {
                    if (this.mOptions.containsKey(UCCore.OPTION_WAP_DENY)) {
                        Object obj2 = this.mOptions.get(UCCore.OPTION_WAP_DENY);
                        if (obj2 == null) {
                            obj = "";
                        } else {
                            obj = obj2.toString();
                        }
                        iGlobalSettings2.setStringValue("SDKWapDeny", obj);
                        Log.d("InitTask", "initProxySettings: setStringValue: SDKWapDeny = " + obj2.toString());
                    }
                    if (this.mOptions.containsKey(UCCore.OPTION_UC_PROXY_ADBLOCK)) {
                        Object obj3 = this.mOptions.get(UCCore.OPTION_UC_PROXY_ADBLOCK);
                        if (obj3 instanceof String) {
                            z = Boolean.parseBoolean((String) obj3);
                            str = "SDKAdBlock";
                            iGlobalSettings = iGlobalSettings2;
                        } else if (obj3 == null) {
                            str = "SDKAdBlock";
                            iGlobalSettings = iGlobalSettings2;
                        } else {
                            z = ((Boolean) obj3).booleanValue();
                            str = "SDKAdBlock";
                            iGlobalSettings = iGlobalSettings2;
                        }
                        iGlobalSettings.setBoolValue(str, z);
                    }
                    iGlobalSettings2.setStringValue("UBISiProfileId", com.uc.webview.export.Build.SDK_PROFILE_ID);
                    iGlobalSettings2.setStringValue("UBISiPrd", com.uc.webview.export.Build.SDK_PRD);
                }
            }
            callbackStat(new Pair(IWaStat.SETUP_TASK_INIT, new s(this, a3, uCElapseTime)));
            callbackStat(new Pair(IWaStat.SETUP_SUCCESS_INITED, (Object) null));
            return;
        }
        ap apVar = new ap();
        synchronized (apVar) {
            d.a((int) UCMPackageInfo.compareVersion, new t(this, apVar));
            a2 = apVar.a(60000);
        }
        if (((Integer) a2.first).intValue() == 3) {
            setException(a2.second instanceof UCSetupException ? (UCSetupException) a2.second : new UCSetupException(4003, (Throwable) a2.second));
        }
    }
}
