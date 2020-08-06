package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.taobao.windvane.service.WVEventId;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.util.Pair;
import com.bftv.fui.constantplugin.Constant;
import com.taobao.atlas.dexmerge.MergeConstants;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.uc.webview.export.Build;
import com.uc.webview.export.annotations.Api;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.c;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.codec.language.Soundex;
import org.json.JSONArray;
import org.json.JSONObject;

@Api
/* compiled from: ProGuard */
public class UCMPackageInfo {
    public static final String CORE_FACTORY_IMPL_CLASS = "com.uc.webkit.sdk.CoreFactoryImpl";
    private static final String[] a = {"WebCore_UC", "webviewuc", "webviewuc.cr"};
    private static final String[] b = {"armeabi-v7a", "armeabi", "x86"};
    public static final int compareVersion = 10027;
    public static final int deleteTempDecFiles = 10039;
    public static final int expectCreateDirFile2P = 10035;
    public static final int expectDirFile1F = 10032;
    public static final int expectDirFile1S = 10033;
    public static final int getDataDirHash = 10012;
    public static final int getDecompressRoot = 10003;
    public static final int getDir = 10001;
    public static final int getFlagRoot = 10005;
    public static final int getKernalJarCpyRoot = 10009;
    public static final int getKernalJarLnkRoot = 10007;
    public static final int getKernalResCpyRoot = 10010;
    public static final int getKernalResLnkRoot = 10008;
    public static final int getKernelFileIfMultiCoreFromDir = 10028;
    public static final int getKernelFiles = 10022;
    public static final int getKernelResFiles = 10024;
    public static final int getLibFilter = 10023;
    public static final int getOdexRoot = 10004;
    public static final int getRepairRoot = 10006;
    public static final int getUnExistsFilePath = 10021;
    public static final int getUpdateRoot = 10002;
    public static final int getVersion = 10040;
    public static final int hadInstallUCMobile = 10026;
    public static final int initUCMBuildInfo = 10041;
    public static final int isThickSDK = 10011;
    public static final int sortByLastModified = 10025;
    public final Pair<String, String> browserIFModule;
    public final String coreCode;
    public final Pair<String, String> coreImplModule;
    public final String dataDir;
    public final String disabledFilePath;
    public final boolean isSpecified;
    public final String mainLibrary;
    public final String pkgName;
    public final String resDirPath;
    public final Pair<String, String> sdkShellModule;
    public final String soDirPath;

    public UCMPackageInfo(Context context, String str, String str2, String str3, String str4, boolean z, String str5, String str6, String str7, boolean z2) {
        String str8;
        this.pkgName = str;
        this.soDirPath = str2;
        String a2 = z2 ? a(context, str4, str3) : str3;
        if (a2 == null) {
            a2 = null;
        } else if (!a2.endsWith(WVNativeCallbackUtil.SEPERATER)) {
            a2 = a2 + WVNativeCallbackUtil.SEPERATER;
        }
        this.resDirPath = a2;
        this.isSpecified = z;
        if (str4 != null) {
            File file = (File) invoke(expectCreateDirFile2P, (File) invoke(10004, context), (String) invoke(10012, str4));
            this.dataDir = str4;
            this.disabledFilePath = str4 + "/e1df430e25e9dacb26ead508abb3413f";
            if (z2) {
                if (c.a(str5)) {
                    str5 = null;
                } else {
                    File file2 = new File(str5);
                    String name = file2.getName();
                    if (name.startsWith("lib") && name.endsWith("_jar_kj_uc.so")) {
                        String str9 = name.substring(3, name.length() - 13) + ".jar";
                        String str10 = (String) invoke(10012, str4);
                        str5 = c.a(file2, new File((File) invoke(expectCreateDirFile2P, (File) invoke(10007, context), str10), str9), new File((File) invoke(expectCreateDirFile2P, (File) invoke(10009, context), str10), str9), false).getAbsolutePath();
                    }
                }
            }
            this.sdkShellModule = new Pair<>(str5, file.getAbsolutePath());
            if (z2) {
                if (c.a(str6)) {
                    str6 = null;
                } else {
                    File file3 = new File(str6);
                    String name2 = file3.getName();
                    if (name2.startsWith("lib") && name2.endsWith("_jar_kj_uc.so")) {
                        String str11 = name2.substring(3, name2.length() - 13) + ".jar";
                        String str12 = (String) invoke(10012, str4);
                        str6 = c.a(file3, new File((File) invoke(expectCreateDirFile2P, (File) invoke(10007, context), str12), str11), new File((File) invoke(expectCreateDirFile2P, (File) invoke(10009, context), str12), str11), false).getAbsolutePath();
                    }
                }
            }
            this.browserIFModule = new Pair<>(str6, file.getAbsolutePath());
            if (z2) {
                if (c.a(str7)) {
                    str7 = null;
                } else {
                    File file4 = new File(str7);
                    String name3 = file4.getName();
                    if (name3.startsWith("lib") && name3.endsWith("_jar_kj_uc.so")) {
                        String str13 = name3.substring(3, name3.length() - 13) + ".jar";
                        String str14 = (String) invoke(10012, str4);
                        str7 = c.a(file4, new File((File) invoke(expectCreateDirFile2P, (File) invoke(10007, context), str14), str13), new File((File) invoke(expectCreateDirFile2P, (File) invoke(10009, context), str14), str13), false).getAbsolutePath();
                    }
                }
            }
            this.coreImplModule = new Pair<>(str7, file.getAbsolutePath());
        } else {
            this.dataDir = null;
            this.disabledFilePath = (String) invoke(10021, new Object[0]);
            this.sdkShellModule = null;
            this.browserIFModule = null;
            this.coreImplModule = null;
        }
        if (c.a(str2)) {
            str8 = context.getApplicationInfo().nativeLibraryDir;
        } else {
            str8 = str2;
        }
        String str15 = null;
        long j = 0;
        String[] strArr = a;
        int length = strArr.length;
        int i = 0;
        while (i < length) {
            String str16 = strArr[i];
            File file5 = new File(str8, "lib" + str16 + MergeConstants.SO_SUFFIX);
            if (!file5.exists() || file5.lastModified() <= j) {
                str16 = str15;
            } else {
                j = file5.lastModified();
            }
            i++;
            str15 = str16;
        }
        if (str15 != null || c.a(str2)) {
            this.mainLibrary = str15;
            this.coreCode = "WebCore_UC".equals(this.mainLibrary) ? "u3" : "webviewuc".equals(this.mainLibrary) ? "u4" : "webviewuc.cr".equals(this.mainLibrary) ? "u4" : this.mainLibrary == null ? Constant.NULL : "error";
            return;
        }
        throw new UCSetupException((int) WVEventId.PAGE_onPause, String.format("Main so file U3 [%s] or U4 [%s|%s] not exists.", new Object[]{"WebCore_UC", "webviewuc", "webviewuc.cr"}));
    }

    public String getDirAlias(Context context) {
        String str;
        if (this.coreImplModule == null || this.coreImplModule.first == null || (str = (String) this.coreImplModule.first) == null) {
            return "nul";
        }
        if (str.startsWith(((File) invoke(10003, context)).getAbsolutePath())) {
            return "dec";
        }
        if (str.startsWith(((File) invoke(10002, context)).getAbsolutePath())) {
            return "upd";
        }
        if (str.startsWith(((File) invoke(10007, context)).getAbsolutePath())) {
            return "kjl";
        }
        if (str.startsWith(((File) invoke(10009, context)).getAbsolutePath())) {
            return "kjc";
        }
        if (str.startsWith(((File) invoke(10006, context)).getAbsolutePath())) {
            return "rep";
        }
        return "oth";
    }

    static synchronized List<UCMPackageInfo> a(Context context, HashMap<String, Object> hashMap) {
        List<UCMPackageInfo> list;
        synchronized (UCMPackageInfo.class) {
            List<UCMPackageInfo> arrayList = new ArrayList<>();
            String str = (String) hashMap.get(UCCore.OPTION_DEX_FILE_PATH);
            if (!c.a(str)) {
                arrayList = a(context, new File(str), arrayList);
            }
            if (arrayList.size() == 0) {
                UCMPackageInfo a2 = a(context, (String) null, (String) hashMap.get(UCCore.OPTION_DEX_FILE_PATH), (String) hashMap.get(UCCore.OPTION_SO_FILE_PATH), (String) hashMap.get(UCCore.OPTION_RES_FILE_PATH));
                if (a2 != null) {
                    arrayList.add(a2);
                }
                if (((Boolean) invoke(10011, new Object[0])).booleanValue()) {
                    list = arrayList;
                }
            }
            String str2 = (String) hashMap.get(UCCore.OPTION_UCM_KRL_DIR);
            if (!c.a(str2)) {
                arrayList = a(context, new File(str2), arrayList);
            }
            String str3 = (String) hashMap.get(UCCore.OPTION_UCM_LIB_DIR);
            if (!c.a(str3)) {
                arrayList.add(a(context, str3));
            }
            list = (List) invoke(sortByLastModified, arrayList);
        }
        return list;
    }

    public static Object invoke(int i, Object... objArr) {
        FileReader fileReader;
        BufferedReader bufferedReader;
        boolean z;
        while (true) {
            switch (i) {
                case 10001:
                    File file = new File(((Context) objArr[0]).getDir("ucmsdk", 0), (String) objArr[1]);
                    if (file.exists() || file.mkdirs()) {
                        return file;
                    }
                    throw new UCSetupException(1003, String.format("Dir file [%s] can't be created.", new Object[]{file.getAbsolutePath()}));
                case 10002:
                    objArr = new Object[]{(Context) objArr[0], "updates"};
                    i = 10001;
                    continue;
                case 10003:
                    objArr = new Object[]{(Context) objArr[0], "decompresses"};
                    i = 10001;
                    continue;
                case 10004:
                    i = expectCreateDirFile2P;
                    objArr = new Object[]{((Context) objArr[0]).getCacheDir(), "odexs"};
                    continue;
                case 10005:
                    objArr = new Object[]{(Context) objArr[0], "flags"};
                    i = 10001;
                    continue;
                case 10006:
                    objArr = new Object[]{(Context) objArr[0], "repairs"};
                    i = 10001;
                    continue;
                case 10007:
                    objArr = new Object[]{(Context) objArr[0], "kjlinks"};
                    i = 10001;
                    continue;
                case 10008:
                    objArr = new Object[]{(Context) objArr[0], "krlinks"};
                    i = 10001;
                    continue;
                case 10009:
                    objArr = new Object[]{(Context) objArr[0], "kjcopies"};
                    i = 10001;
                    continue;
                case 10010:
                    objArr = new Object[]{(Context) objArr[0], "krcopies"};
                    i = 10001;
                    continue;
                case 10011:
                    try {
                        Class.forName(CORE_FACTORY_IMPL_CLASS);
                        return true;
                    } catch (Throwable th) {
                        return false;
                    }
                case 10012:
                    return String.valueOf(((String) objArr[0]).hashCode()).replace(Soundex.SILENT_MARKER, '_');
                case 10021:
                    return "/unexists/" + System.currentTimeMillis();
                case 10022:
                    return ((File) objArr[0]).listFiles(new as());
                case getLibFilter /*10023*/:
                    return new at();
                case getKernelResFiles /*10024*/:
                    return ((File) objArr[0]).list(new au());
                case sortByLastModified /*10025*/:
                    List list = (List) objArr[0];
                    if (list == null || list.size() <= 1) {
                        return list;
                    }
                    Collections.sort(list, new av());
                    return list;
                case hadInstallUCMobile /*10026*/:
                    Iterator<PackageInfo> it = ((Context) objArr[0]).getPackageManager().getInstalledPackages(64).iterator();
                    while (true) {
                        if (it.hasNext()) {
                            PackageInfo next = it.next();
                            if (next.packageName.startsWith("com.UCMobile") && next.applicationInfo.enabled) {
                                z = true;
                            }
                        } else {
                            z = false;
                        }
                    }
                    return Boolean.valueOf(z);
                case compareVersion /*10027*/:
                    String str = (String) objArr[2];
                    String[] split = ((String) objArr[0]).split("\\.");
                    String[] split2 = ((String) objArr[1]).split("\\.");
                    if (c.b(split[0]) >= c.b(split2[0]) && c.b(split[1]) >= c.b(split2[1]) && c.b(split[2]) >= c.b(split2[2])) {
                        return true;
                    }
                    Log.d("UCMPackageInfo", str);
                    return false;
                case getKernelFileIfMultiCoreFromDir /*10028*/:
                    String str2 = (String) objArr[0];
                    if (Build.PACK_TYPE != 34 && Build.PACK_TYPE != 43) {
                        return null;
                    }
                    File[] fileArr = (File[]) invoke(10022, (File) invoke(expectDirFile1S, str2));
                    if (fileArr == null || fileArr.length <= 0) {
                        return null;
                    }
                    return fileArr[0];
                case expectDirFile1F /*10032*/:
                    File file2 = (File) objArr[0];
                    if (file2.exists()) {
                        return file2;
                    }
                    throw new UCSetupException(1002, String.format("Directory [%s] not exists.", new Object[]{file2.getAbsolutePath()}));
                case expectDirFile1S /*10033*/:
                    return (File) invoke(expectDirFile1F, new File((String) objArr[0]));
                case expectCreateDirFile2P /*10035*/:
                    return UCCyclone.expectCreateDirFile(new File((File) objArr[0], (String) objArr[1]));
                case deleteTempDecFiles /*10039*/:
                    File[] listFiles = ((Context) objArr[0]).getCacheDir().listFiles(new aw());
                    if (listFiles != null && listFiles.length > 0) {
                        for (File recursiveDelete : listFiles) {
                            UCCyclone.recursiveDelete(recursiveDelete, false, (Object) null);
                        }
                        break;
                    }
                case getVersion /*10040*/:
                    File file3 = new File((String) objArr[0], "curver");
                    if (file3.exists()) {
                        try {
                            fileReader = new FileReader(file3);
                            try {
                                bufferedReader = new BufferedReader(fileReader);
                                try {
                                    String readLine = bufferedReader.readLine();
                                    if (readLine != null) {
                                        String trim = readLine.trim();
                                        if (!trim.startsWith(WVNativeCallbackUtil.SEPERATER)) {
                                            trim = WVNativeCallbackUtil.SEPERATER + trim;
                                        }
                                        if (!trim.endsWith(WVNativeCallbackUtil.SEPERATER)) {
                                            trim = trim + WVNativeCallbackUtil.SEPERATER;
                                        }
                                        try {
                                            bufferedReader.close();
                                        } catch (Exception e) {
                                        }
                                        try {
                                            fileReader.close();
                                            return trim;
                                        } catch (Exception e2) {
                                            return trim;
                                        }
                                    } else {
                                        try {
                                            bufferedReader.close();
                                        } catch (Exception e3) {
                                        }
                                        try {
                                            fileReader.close();
                                        } catch (Exception e4) {
                                        }
                                    }
                                } catch (Exception e5) {
                                    e = e5;
                                    try {
                                        Log.i("tag_test_log", "getVersion", e);
                                        try {
                                            bufferedReader.close();
                                        } catch (Exception e6) {
                                        }
                                        try {
                                            fileReader.close();
                                        } catch (Exception e7) {
                                        }
                                        return null;
                                    } catch (Throwable th2) {
                                        th = th2;
                                        try {
                                            bufferedReader.close();
                                        } catch (Exception e8) {
                                        }
                                        try {
                                            fileReader.close();
                                        } catch (Exception e9) {
                                        }
                                        throw th;
                                    }
                                }
                            } catch (Exception e10) {
                                e = e10;
                                bufferedReader = null;
                                Log.i("tag_test_log", "getVersion", e);
                                bufferedReader.close();
                                fileReader.close();
                                return null;
                            } catch (Throwable th3) {
                                th = th3;
                                bufferedReader = null;
                                bufferedReader.close();
                                fileReader.close();
                                throw th;
                            }
                        } catch (Exception e11) {
                            e = e11;
                            bufferedReader = null;
                            fileReader = null;
                            Log.i("tag_test_log", "getVersion", e);
                            bufferedReader.close();
                            fileReader.close();
                            return null;
                        } catch (Throwable th4) {
                            th = th4;
                            bufferedReader = null;
                            fileReader = null;
                            bufferedReader.close();
                            fileReader.close();
                            throw th;
                        }
                    }
                    return null;
                case initUCMBuildInfo /*10041*/:
                    ClassLoader classLoader = (ClassLoader) objArr[0];
                    try {
                        Class<?> cls = Class.forName("com.uc.webview.browser.shell.Build$Version", false, classLoader);
                        String obj = cls.getField("NAME").get((Object) null).toString();
                        String obj2 = cls.getField("SUPPORT_SDK_MIN").get((Object) null).toString();
                        Build.UCM_VERSION = obj;
                        Build.UCM_SUPPORT_SDK_MIN = obj2;
                        Build.Version.API_LEVEL = cls.getField("API_LEVEL").getInt((Object) null);
                    } catch (Exception e12) {
                        Build.Version.API_LEVEL = 1;
                    }
                    try {
                        Class<?> cls2 = Class.forName("com.uc.webview.browser.shell.Build", false, classLoader);
                        Build.CORE_VERSION = cls2.getField("CORE_VERSION").get((Object) null).toString();
                        Build.CORE_TIME = cls2.getField("TIME").get((Object) null).toString();
                        break;
                    } catch (Exception e13) {
                        break;
                    }
            }
        }
        return null;
    }

    private static String a(Context context, String str, String str2) {
        File file;
        File file2;
        if (c.a(str2)) {
            return null;
        }
        File file3 = (File) invoke(expectDirFile1S, str2);
        String[] strArr = (String[]) invoke(getKernelResFiles, file3);
        if (strArr == null || strArr.length == 0) {
            return null;
        }
        String str3 = (String) invoke(10012, str);
        File file4 = (File) invoke(expectCreateDirFile2P, (File) invoke(10008, context), str3);
        File file5 = (File) invoke(expectCreateDirFile2P, (File) invoke(10010, context), str3);
        File file6 = (File) invoke(expectCreateDirFile2P, file4, "paks");
        File file7 = (File) invoke(expectCreateDirFile2P, file5, "paks");
        boolean[] zArr = new boolean[strArr.length];
        File[] fileArr = new File[strArr.length];
        File[] fileArr2 = new File[strArr.length];
        File[] fileArr3 = new File[strArr.length];
        boolean z = true;
        for (int i = 0; i < strArr.length; i++) {
            String str4 = strArr[i];
            boolean endsWith = str4.endsWith("_pak_kr_uc.so");
            String substring = str4.substring(3, str4.length() - 9);
            int lastIndexOf = substring.lastIndexOf(95);
            String str5 = substring.substring(0, lastIndexOf) + '.' + substring.substring(lastIndexOf + 1);
            File file8 = new File(file3, str4);
            if (endsWith) {
                file = file6;
            } else {
                file = file4;
            }
            File file9 = new File(file, str5);
            if (endsWith) {
                file2 = file7;
            } else {
                file2 = file5;
            }
            File file10 = new File(file2, str5);
            File a2 = c.a(file8, file9, file10, false);
            fileArr[i] = file8;
            fileArr2[i] = file9;
            fileArr3[i] = file10;
            boolean z2 = a2 == file9;
            zArr[i] = z2;
            if (!z2) {
                z = false;
            }
        }
        if (z) {
            return file4.getAbsolutePath();
        }
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (zArr[i2]) {
                c.a(fileArr[i2], fileArr2[i2], fileArr3[i2], true);
            }
        }
        return file5.getAbsolutePath();
    }

    private static UCMPackageInfo a(Context context, String str) {
        String str2;
        String str3 = null;
        Context context2 = context;
        loop0:
        while (true) {
            File file = (File) invoke(expectDirFile1S, str);
            try {
                String absolutePath = UCCyclone.expectFile(file, "libcore_jar_kj_uc.so").getAbsolutePath();
                try {
                    str2 = UCCyclone.expectFile(file, "libsdk_shell_jar_kj_uc.so").getAbsolutePath();
                } catch (Throwable th) {
                    str2 = null;
                }
                try {
                    str3 = UCCyclone.expectFile(file, "libbrowser_if_jar_kj_uc.so").getAbsolutePath();
                } catch (Throwable th2) {
                }
                return new com.uc.webview.export.internal.utility.UCMPackageInfo(context2, "specified", file.getAbsolutePath(), file.getAbsolutePath(), file.getAbsolutePath(), true, str2, str3, absolutePath, true);
            } catch (UCSetupException e) {
                File[] listFiles = file.listFiles();
                if (listFiles == null) {
                    break;
                }
                for (String str4 : b) {
                    int length = listFiles.length;
                    int i = 0;
                    while (i < length) {
                        File file2 = listFiles[i];
                        if (!str4.equals(file2.getName()) || !file2.isDirectory()) {
                            i++;
                        } else {
                            str = file2.getAbsolutePath();
                        }
                    }
                }
                break loop0;
                throw e;
            }
        }
        throw e;
    }

    private static UCMPackageInfo a(Context context, String str, String str2, String str3, String str4) {
        String str5;
        String str6;
        String str7;
        String str8;
        boolean a2 = c.a(str2);
        boolean a3 = c.a(str3);
        boolean a4 = c.a(str4);
        if (!a2 || ((Boolean) invoke(10011, new Object[0])).booleanValue()) {
            if (!a2) {
                File file = (File) invoke(expectDirFile1S, str2);
                str5 = UCCyclone.expectFile(file, "core.jar").getAbsolutePath();
                try {
                    str7 = UCCyclone.expectFile(file, "sdk_shell.jar").getAbsolutePath();
                } catch (Throwable th) {
                    str7 = null;
                }
                try {
                    str6 = UCCyclone.expectFile(file, "browser_if.jar").getAbsolutePath();
                } catch (Throwable th2) {
                    str6 = null;
                }
            } else {
                str5 = null;
                str6 = null;
                str7 = null;
            }
            if (str == null) {
                str8 = "specified";
            } else {
                str8 = str;
            }
            return new com.uc.webview.export.internal.utility.UCMPackageInfo(context, str8, str3, str4, str2, true, str7, str6, str5, false);
        } else if (a3 && a4) {
            return null;
        } else {
            throw new UCSetupException((int) WVEventId.PAGE_onResume, "No ucm dex file specified.");
        }
    }

    private static List<UCMPackageInfo> a(Context context, File file, List<UCMPackageInfo> list) {
        boolean z;
        if (list == null) {
            list = new ArrayList<>();
        }
        if (file.exists() && file.isDirectory()) {
            File file2 = new File(file, "sdk_shell.jar");
            File file3 = new File(file, "browser_if.jar");
            File file4 = new File(file, "core.jar");
            File file5 = new File(file, "lib");
            if (!file4.exists() || ((d.k && !file2.exists()) || ((d.k && !file3.exists()) || !file5.isDirectory()))) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                String[] strArr = b;
                int length = strArr.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    File file6 = new File(file5, strArr[i]);
                    if (file6.isDirectory()) {
                        file5 = file6;
                        break;
                    }
                    i++;
                }
                File file7 = new File(file, "assets");
                if (z) {
                    list.add(new com.uc.webview.export.internal.utility.UCMPackageInfo(context, "specified", file5.getAbsolutePath(), file7.getAbsolutePath(), file.getAbsolutePath(), true, file2.getAbsolutePath(), file3.getAbsolutePath(), file4.getAbsolutePath(), false));
                }
            }
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (File file8 : listFiles) {
                    if (file8.isDirectory()) {
                        a(context, file8, list);
                    }
                }
            }
        }
        return list;
    }

    static synchronized List<UCMPackageInfo> a(Context context, String str, boolean z) {
        List<UCMPackageInfo> list;
        String str2;
        String str3;
        String str4;
        synchronized (UCMPackageInfo.class) {
            List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(64);
            List<UCMPackageInfo> a2 = a(context);
            for (PackageInfo next : installedPackages) {
                if (!next.packageName.equals(context.getPackageName()) && next.packageName.startsWith(str) && next.applicationInfo.enabled) {
                    if (z || (next.signatures.length > 0 && next.signatures[0].toCharsString().equals("308202153082017ea00302010202044d92c9ac300d06092a864886f70d0101050500304e310b300906035504061302636e310b3009060355040813026764310b300906035504071302677a310b3009060355040a13027563310b3009060355040b13027563310b30090603550403130275633020170d3131303333303036313135365a180f32303635313233313036313135365a304e310b300906035504061302636e310b3009060355040813026764310b300906035504071302677a310b3009060355040a13027563310b3009060355040b13027563310b300906035504031302756330819f300d06092a864886f70d010101050003818d0030818902818100aac959f5439f1595907c7fa43a6d628fa6c6e0006470d122ee5edac296e51d24450acf16e3a4aa8b75735e23a8a7cd4925825a9e3311d6c6d4024b4e837d613bb037a25e898380625b042c1cb7eb017f86772b4ae10256f840d75a9b4f646f2fd7a178e58035182358c1eb2b940307107af050384f3b2763b186679e371ea5c90203010001300d06092a864886f70d010105050003818100a635800a51087088b481823651d212253fcc0fa4b97dc872f9f1c47b723e1a31e4c53ddaf8d7f9e0b754f579c01c887fd6f1de1368594f8262a77e95582ad818bcffd02b9e36a1afe462b220acc6ca00bb98a2d07a484257ab43759231c7134e89168048f05bb3ed20438b045b3e5259488a80fe64888cc89202d33c57824fc1"))) {
                        try {
                            String str5 = next.packageName;
                            String str6 = next.applicationInfo.dataDir;
                            String.valueOf(str6.hashCode()).replace(Soundex.SILENT_MARKER, '_');
                            String str7 = str6 + "/com/sdk_shell";
                            String str8 = (String) invoke(getVersion, str7);
                            if (str8 != null) {
                                str2 = str7 + str8;
                            } else {
                                str2 = str7;
                            }
                            File file = new File(str2);
                            invoke(expectDirFile1F, file);
                            File file2 = new File(file, "dex/sdk_shell.jar");
                            UCCyclone.expectFile(file2);
                            String str9 = str6 + "/com/browser_if";
                            String str10 = (String) invoke(getVersion, str9);
                            if (str10 != null) {
                                str3 = str9 + str10;
                            } else {
                                str3 = str9;
                            }
                            File file3 = new File(str3);
                            invoke(expectDirFile1F, file3);
                            File file4 = new File(file3, "dex/browser_if.jar");
                            UCCyclone.expectFile(file4);
                            String str11 = str6 + "/com/core";
                            String str12 = (String) invoke(getVersion, str11);
                            if (str12 != null) {
                                str4 = str11 + str12;
                            } else {
                                str4 = str11;
                            }
                            File file5 = new File(str4);
                            invoke(expectDirFile1F, file5);
                            File file6 = new File(file5, "dex/core.jar");
                            UCCyclone.expectFile(file6);
                            String str13 = file5.getAbsolutePath() + "/lib";
                            if (!new File(str13, "/libWebCore_UC.so").exists()) {
                                str13 = str6 + "/native";
                                if (!new File(str13, "/libWebCore_UC.so").exists()) {
                                    str13 = str6 + "/lib";
                                    if (!new File(str13, "/libWebCore_UC.so").exists()) {
                                    }
                                }
                            }
                            a2.add(new com.uc.webview.export.internal.utility.UCMPackageInfo(context, str5, str13, (String) null, str6, false, file2.getAbsolutePath(), file4.getAbsolutePath(), file6.getAbsolutePath(), false));
                        } catch (Throwable th) {
                        }
                    }
                }
            }
            list = (List) invoke(sortByLastModified, a2);
        }
        return list;
    }

    private static List<UCMPackageInfo> a(Context context) {
        String[] strArr = {"com.UCMobile", "com.taobao.taobao"};
        new ArrayList();
        if (((Boolean) d.a(10007, "shareapps")).booleanValue()) {
            String str = (String) d.a(10005, "shareapps");
            if ("NAN".equals(str)) {
                strArr = new String[0];
            } else {
                strArr = str.split(",");
            }
        }
        ArrayList arrayList = new ArrayList();
        for (String str2 : strArr) {
            try {
                if (!str2.equals(context.getPackageName())) {
                    for (a next : a.a(new File("/data/data/" + str2 + "/app_ucmsdk", "config.json"))) {
                        if (!c.a(next.c)) {
                            File file = new File(next.c);
                            File file2 = new File(next.d);
                            String str3 = next.a;
                            String str4 = next.b;
                            if (((Boolean) invoke(compareVersion, str3, Build.Version.SUPPORT_UCM_MIN, "最小内核版本不通过")).booleanValue()) {
                                if (((Boolean) invoke(compareVersion, Build.Version.NAME, str4, "最小SDK版本不通过")).booleanValue()) {
                                    if (!file.exists() || !file2.exists()) {
                                        Log.i("UCMPackageInfo", file.getAbsolutePath() + " or " + file2.getAbsolutePath() + " not exist");
                                    } else {
                                        arrayList.add(a(context, str2, file.getAbsolutePath(), file2.getAbsolutePath(), (String) null));
                                    }
                                }
                            }
                            Log.d("UCMPackageInfo", "版本校验不通过>>config ucmVersion:" + str3 + ",SUPPORT_UCM_MIN=" + Build.Version.SUPPORT_UCM_MIN);
                            Log.d("UCMPackageInfo", "版本校验不通过>>config ucmSuportSDKMin:" + str4 + ",NAME=" + Build.Version.NAME);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("UCMPackageInfo", "getAppUCMPackageInfo", e);
            }
        }
        return arrayList;
    }

    /* compiled from: ProGuard */
    public static final class a {
        public String a;
        public String b;
        public String c;
        public String d;
        public String e;
        public String f;

        public static List<a> a(File file) {
            ByteArrayOutputStream byteArrayOutputStream;
            FileInputStream fileInputStream;
            FileInputStream fileInputStream2 = null;
            ArrayList arrayList = new ArrayList();
            if (file == null || !file.exists()) {
                Log.i("Config", "configFile:" + file + " not exist");
                return arrayList;
            }
            try {
                fileInputStream = new FileInputStream(file);
                try {
                    byteArrayOutputStream = new ByteArrayOutputStream();
                } catch (Exception e2) {
                    e = e2;
                    byteArrayOutputStream = null;
                    fileInputStream2 = fileInputStream;
                    try {
                        Log.e("Config", "getConfig", e);
                        UCCyclone.close(fileInputStream2);
                        UCCyclone.close(byteArrayOutputStream);
                        return new ArrayList();
                    } catch (Throwable th) {
                        th = th;
                        fileInputStream = fileInputStream2;
                        UCCyclone.close(fileInputStream);
                        UCCyclone.close(byteArrayOutputStream);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    byteArrayOutputStream = null;
                    UCCyclone.close(fileInputStream);
                    UCCyclone.close(byteArrayOutputStream);
                    throw th;
                }
                try {
                    byte[] bArr = new byte[1024];
                    while (true) {
                        int read = fileInputStream.read(bArr);
                        if (read <= 0) {
                            break;
                        }
                        byteArrayOutputStream.write(bArr, 0, read);
                    }
                    JSONArray jSONArray = new JSONArray(new String(byteArrayOutputStream.toByteArray()));
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jSONObject = jSONArray.getJSONObject(i);
                        a aVar = new a();
                        aVar.a = jSONObject.getString("ucmver");
                        aVar.b = jSONObject.getString("sdkMin");
                        if (jSONObject.has("dex")) {
                            aVar.c = jSONObject.getJSONObject("dex").getString(TuwenConstants.PARAMS.SKU_PATH);
                        }
                        if (jSONObject.has("so")) {
                            aVar.d = jSONObject.getJSONObject("so").getString(TuwenConstants.PARAMS.SKU_PATH);
                        }
                        if (jSONObject.has("archive")) {
                            JSONObject jSONObject2 = jSONObject.getJSONObject("archive");
                            aVar.e = jSONObject2.getString(TuwenConstants.PARAMS.SKU_PATH);
                            aVar.f = jSONObject2.getString("algorithm");
                        }
                        arrayList.add(aVar);
                    }
                    UCCyclone.close(fileInputStream);
                    UCCyclone.close(byteArrayOutputStream);
                    return arrayList;
                } catch (Exception e3) {
                    e = e3;
                    fileInputStream2 = fileInputStream;
                    Log.e("Config", "getConfig", e);
                    UCCyclone.close(fileInputStream2);
                    UCCyclone.close(byteArrayOutputStream);
                    return new ArrayList();
                } catch (Throwable th3) {
                    th = th3;
                    UCCyclone.close(fileInputStream);
                    UCCyclone.close(byteArrayOutputStream);
                    throw th;
                }
            } catch (Exception e4) {
                e = e4;
                byteArrayOutputStream = null;
                Log.e("Config", "getConfig", e);
                UCCyclone.close(fileInputStream2);
                UCCyclone.close(byteArrayOutputStream);
                return new ArrayList();
            } catch (Throwable th4) {
                th = th4;
                byteArrayOutputStream = null;
                fileInputStream = null;
                UCCyclone.close(fileInputStream);
                UCCyclone.close(byteArrayOutputStream);
                throw th;
            }
        }

        public static boolean a(List<a> list, File file) {
            if (list.size() == 0) {
                Log.i("Config", "configs:" + list + " is empty or configFile:" + file);
                return false;
            }
            FileOutputStream fileOutputStream = null;
            try {
                JSONArray jSONArray = new JSONArray();
                for (a next : list) {
                    JSONObject jSONObject = new JSONObject();
                    jSONArray.put(jSONObject);
                    jSONObject.put("ucmver", next.a);
                    jSONObject.put("sdkMin", next.b);
                    if (!c.a(next.c)) {
                        JSONObject jSONObject2 = new JSONObject();
                        jSONObject.put("dex", jSONObject2);
                        jSONObject2.put(TuwenConstants.PARAMS.SKU_PATH, next.c);
                    }
                    if (!c.a(next.d)) {
                        JSONObject jSONObject3 = new JSONObject();
                        jSONObject.put("so", jSONObject3);
                        jSONObject3.put(TuwenConstants.PARAMS.SKU_PATH, next.d);
                    }
                    if (!c.a(next.e)) {
                        JSONObject jSONObject4 = new JSONObject();
                        jSONObject.put("archive", jSONObject4);
                        jSONObject4.put(TuwenConstants.PARAMS.SKU_PATH, next.e);
                        jSONObject4.put("algorithm", c.a(next.f) ? "zip" : next.f);
                    }
                }
                FileOutputStream fileOutputStream2 = new FileOutputStream(file);
                try {
                    fileOutputStream2.write(jSONArray.toString().getBytes());
                    UCCyclone.close(fileOutputStream2);
                    return true;
                } catch (Exception e2) {
                    e = e2;
                    fileOutputStream = fileOutputStream2;
                    try {
                        Log.e("Config", "saveConfig", e);
                        UCCyclone.close(fileOutputStream);
                        return false;
                    } catch (Throwable th) {
                        th = th;
                        UCCyclone.close(fileOutputStream);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    fileOutputStream = fileOutputStream2;
                    UCCyclone.close(fileOutputStream);
                    throw th;
                }
            } catch (Exception e3) {
                e = e3;
                Log.e("Config", "saveConfig", e);
                UCCyclone.close(fileOutputStream);
                return false;
            }
        }
    }
}
