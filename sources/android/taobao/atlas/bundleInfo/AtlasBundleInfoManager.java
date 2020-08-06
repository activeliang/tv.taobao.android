package android.taobao.atlas.bundleInfo;

import android.taobao.atlas.bundleInfo.BundleListing;
import android.taobao.atlas.runtime.RuntimeVariables;
import android.taobao.atlas.util.WrapperUtil;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import org.json.JSONArray;
import org.json.JSONObject;

public class AtlasBundleInfoManager {
    public static final String TAG = "AtlasBundleInfoManager";
    private static AtlasBundleInfoManager sManager;
    private BundleListing mCurrentBundleListing;

    public static synchronized AtlasBundleInfoManager instance() {
        AtlasBundleInfoManager atlasBundleInfoManager;
        synchronized (AtlasBundleInfoManager.class) {
            if (sManager == null) {
                sManager = new AtlasBundleInfoManager();
            }
            atlasBundleInfoManager = sManager;
        }
        return atlasBundleInfoManager;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v4, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: java.lang.String} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private AtlasBundleInfoManager() {
        /*
            r14 = this;
            r14.<init>()
            android.taobao.atlas.bundleInfo.BundleListing r10 = r14.mCurrentBundleListing
            if (r10 != 0) goto L_0x00cf
            r1 = 0
            r9 = 2
            r4 = 0
        L_0x000a:
            android.taobao.atlas.bundleInfo.BundleListing r10 = android.taobao.atlas.bundleInfo.AtlasBundleInfoGenerator.generateBundleInfo()     // Catch:{ Throwable -> 0x003c }
            r14.mCurrentBundleListing = r10     // Catch:{ Throwable -> 0x003c }
            java.lang.String r10 = "AtlasBundleInfoManager"
            java.lang.String r11 = "generate info from generator"
            android.util.Log.e(r10, r11)     // Catch:{ Throwable -> 0x003c }
        L_0x0019:
            r14.updateBundleListingWithExtraInfo()     // Catch:{ Throwable -> 0x005c }
        L_0x001c:
            if (r4 == 0) goto L_0x00cf
            java.util.HashMap r3 = new java.util.HashMap
            r3.<init>()
            java.lang.String r10 = "InitBundleInfoByVersionIfNeed"
            r3.put(r10, r1)
            android.taobao.atlas.util.log.impl.AtlasMonitor r10 = android.taobao.atlas.util.log.impl.AtlasMonitor.getInstance()
            java.lang.String r11 = "container_bundleinfo_parse_fail"
            r10.report(r11, r3, r4)
            java.lang.RuntimeException r10 = new java.lang.RuntimeException
            java.lang.String r11 = "parse bundleinfo failed"
            r10.<init>(r11)
            throw r10
        L_0x003c:
            r6 = move-exception
            r6.printStackTrace()     // Catch:{ Throwable -> 0x005c }
            if (r1 != 0) goto L_0x00a0
            java.lang.String r10 = "bundleInfo"
            java.lang.Object r10 = android.taobao.atlas.runtime.RuntimeVariables.getFrameworkProperty(r10)     // Catch:{ Throwable -> 0x005c }
            r0 = r10
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Throwable -> 0x005c }
            r1 = r0
            boolean r10 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x005c }
            if (r10 == 0) goto L_0x0066
            java.lang.RuntimeException r10 = new java.lang.RuntimeException     // Catch:{ Throwable -> 0x005c }
            java.lang.String r11 = "read bundleInfo failed"
            r10.<init>(r11)     // Catch:{ Throwable -> 0x005c }
            throw r10     // Catch:{ Throwable -> 0x005c }
        L_0x005c:
            r5 = move-exception
            r4 = r5
            r4.printStackTrace()
            int r9 = r9 + -1
            if (r9 > 0) goto L_0x000a
            goto L_0x001c
        L_0x0066:
            java.lang.String r10 = "compressInfo"
            java.lang.Object r2 = android.taobao.atlas.runtime.RuntimeVariables.getFrameworkProperty(r10)     // Catch:{ Throwable -> 0x005c }
            if (r2 == 0) goto L_0x0095
            java.lang.Boolean r2 = (java.lang.Boolean) r2     // Catch:{ Throwable -> 0x005c }
            boolean r10 = r2.booleanValue()     // Catch:{ Throwable -> 0x005c }
            if (r10 == 0) goto L_0x0095
            java.lang.String r1 = uncompress(r1)     // Catch:{ Throwable -> 0x005c }
            java.lang.String r10 = "AtlasBundleInfoManager"
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x005c }
            r11.<init>()     // Catch:{ Throwable -> 0x005c }
            java.lang.String r12 = "the result of decoded info "
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ Throwable -> 0x005c }
            java.lang.StringBuilder r11 = r11.append(r1)     // Catch:{ Throwable -> 0x005c }
            java.lang.String r11 = r11.toString()     // Catch:{ Throwable -> 0x005c }
            android.util.Log.e(r10, r11)     // Catch:{ Throwable -> 0x005c }
        L_0x0095:
            if (r1 != 0) goto L_0x00a0
            java.lang.RuntimeException r10 = new java.lang.RuntimeException     // Catch:{ Throwable -> 0x005c }
            java.lang.String r11 = "bundleinfo is invalid"
            r10.<init>(r11)     // Catch:{ Throwable -> 0x005c }
            throw r10     // Catch:{ Throwable -> 0x005c }
        L_0x00a0:
            java.util.LinkedHashMap r7 = android.taobao.atlas.bundleInfo.BundleListingUtil.parseArray(r1)     // Catch:{ Throwable -> 0x005c }
            if (r7 != 0) goto L_0x00c3
            java.util.HashMap r3 = new java.util.HashMap     // Catch:{ Throwable -> 0x005c }
            r3.<init>()     // Catch:{ Throwable -> 0x005c }
            java.lang.String r10 = "InitBundleInfoByVersionIfNeed"
            r3.put(r10, r1)     // Catch:{ Throwable -> 0x005c }
            android.taobao.atlas.util.log.impl.AtlasMonitor r10 = android.taobao.atlas.util.log.impl.AtlasMonitor.getInstance()     // Catch:{ Throwable -> 0x005c }
            java.lang.String r11 = "container_bundleinfo_parse_fail"
            java.lang.RuntimeException r12 = new java.lang.RuntimeException     // Catch:{ Throwable -> 0x005c }
            java.lang.String r13 = "the infos is null!"
            r12.<init>(r13)     // Catch:{ Throwable -> 0x005c }
            r10.report(r11, r3, r12)     // Catch:{ Throwable -> 0x005c }
        L_0x00c3:
            android.taobao.atlas.bundleInfo.BundleListing r8 = new android.taobao.atlas.bundleInfo.BundleListing     // Catch:{ Throwable -> 0x005c }
            r8.<init>()     // Catch:{ Throwable -> 0x005c }
            r8.setBundles(r7)     // Catch:{ Throwable -> 0x005c }
            r14.mCurrentBundleListing = r8     // Catch:{ Throwable -> 0x005c }
            goto L_0x0019
        L_0x00cf:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.bundleInfo.AtlasBundleInfoManager.<init>():void");
    }

    public BundleListing getBundleInfo() {
        return this.mCurrentBundleListing;
    }

    public List<String> getDependencyForBundle(String bundleName) {
        BundleListing.BundleInfo bundleInfo;
        List<String> dependencies = null;
        if (!(this.mCurrentBundleListing == null || this.mCurrentBundleListing.getBundles() == null || (bundleInfo = this.mCurrentBundleListing.getBundles().get(bundleName)) == null || bundleInfo.getDependency() == null)) {
            dependencies = new ArrayList<>();
            for (int x = 0; x < bundleInfo.getDependency().size(); x++) {
                if (!TextUtils.isEmpty(bundleInfo.getDependency().get(x))) {
                    dependencies.add(bundleInfo.getDependency().get(x));
                }
            }
        }
        return dependencies;
    }

    public boolean isInternalBundle(String bundleName) {
        BundleListing.BundleInfo info;
        if (this.mCurrentBundleListing == null || this.mCurrentBundleListing.getBundles() == null || (info = this.mCurrentBundleListing.getBundles().get(bundleName)) == null) {
            return true;
        }
        return info.isInternal();
    }

    public String getBundleForRemoteFragment(String rFname) {
        if (this.mCurrentBundleListing == null || this.mCurrentBundleListing.getBundles() == null) {
            return null;
        }
        for (Map.Entry<String, BundleListing.BundleInfo> entry : this.mCurrentBundleListing.getBundles().entrySet()) {
            BundleListing.BundleInfo bundleInfo = entry.getValue();
            if (bundleInfo != null && bundleInfo.remoteFragments != null && bundleInfo.remoteFragments.containsKey(rFname)) {
                return bundleInfo.getPkgName();
            }
        }
        return null;
    }

    public String getBundleForRemoteTransactor(String tName) {
        if (this.mCurrentBundleListing == null || this.mCurrentBundleListing.getBundles() == null) {
            return null;
        }
        for (Map.Entry<String, BundleListing.BundleInfo> entry : this.mCurrentBundleListing.getBundles().entrySet()) {
            BundleListing.BundleInfo bundleInfo = entry.getValue();
            if (bundleInfo != null && bundleInfo.remoteTransactors != null && bundleInfo.remoteTransactors.containsKey(tName)) {
                return bundleInfo.getPkgName();
            }
        }
        return null;
    }

    public String getBundleForRemoteView(String rVname) {
        if (this.mCurrentBundleListing == null || this.mCurrentBundleListing.getBundles() == null) {
            return null;
        }
        for (Map.Entry<String, BundleListing.BundleInfo> entry : this.mCurrentBundleListing.getBundles().entrySet()) {
            BundleListing.BundleInfo bundleInfo = entry.getValue();
            if (bundleInfo != null && bundleInfo.remoteViews != null && bundleInfo.remoteViews.containsKey(rVname)) {
                return bundleInfo.getPkgName();
            }
        }
        return null;
    }

    public String getBundleForComponet(String componentName) {
        if (this.mCurrentBundleListing == null || this.mCurrentBundleListing.getBundles() == null) {
            return null;
        }
        for (Map.Entry<String, BundleListing.BundleInfo> entry : this.mCurrentBundleListing.getBundles().entrySet()) {
            BundleListing.BundleInfo bundleInfo = entry.getValue();
            if (bundleInfo != null && bundleInfo.getActivities() != null && bundleInfo.getActivities().containsKey(componentName)) {
                return bundleInfo.getPkgName();
            }
            if (bundleInfo != null && bundleInfo.getServices() != null && bundleInfo.getServices().containsKey(componentName)) {
                return bundleInfo.getPkgName();
            }
            if (bundleInfo != null && bundleInfo.getReceivers() != null && bundleInfo.getReceivers().containsKey(componentName)) {
                return bundleInfo.getPkgName();
            }
            if (bundleInfo != null && bundleInfo.getContentProviders() != null && bundleInfo.getContentProviders().containsKey(componentName)) {
                return bundleInfo.getPkgName();
            }
        }
        return null;
    }

    public boolean isMbundle(String location) {
        BundleListing.BundleInfo bundleInfo = getBundleInfo(location);
        if (bundleInfo != null) {
            return bundleInfo.isMBundle();
        }
        return false;
    }

    public BundleListing.BundleInfo getBundleInfo(String name) {
        if (this.mCurrentBundleListing == null || this.mCurrentBundleListing.getBundles() == null) {
            return null;
        }
        BundleListing.BundleInfo info = this.mCurrentBundleListing.getBundles().get(name);
        if (info != null) {
            return info;
        }
        Log.w(TAG, "Could not find info for: " + name);
        return null;
    }

    private void updateBundleListingWithExtraInfo() {
        if (this.mCurrentBundleListing != null && this.mCurrentBundleListing.getBundles() != null) {
            String extraInfo = getFromAssets(String.format("%s%s.json", new Object[]{"bundleInfo-", WrapperUtil.getPackageInfo(RuntimeVariables.androidApplication).versionName}), RuntimeVariables.androidApplication);
            if (!TextUtils.isEmpty(extraInfo)) {
                try {
                    JSONArray array = new JSONArray(extraInfo);
                    if (array != null) {
                        for (int x = 0; x < array.length(); x++) {
                            JSONObject jb = array.getJSONObject(x);
                            BundleListing.BundleInfo info = getBundleInfo(jb.optString("name"));
                            info.size = (long) jb.optInt("size");
                            info.md5 = jb.optString("md5");
                            info.url = jb.optString("url");
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String uncompress(String base64EncodeStr) {
        byte[] gzipArray = Base64.decode(base64EncodeStr, 0);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(gzipArray);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[1024];
            while (true) {
                int n = ungzip.read(buffer);
                if (n >= 0) {
                    out.write(buffer, 0, n);
                } else {
                    String str = new String(out.toByteArray(), "UTF-8");
                    try {
                        in.close();
                        out.close();
                        return str;
                    } catch (Throwable th) {
                        return str;
                    }
                }
            }
            throw th;
            return null;
        } catch (IOException e) {
            in.close();
            out.close();
        } catch (Throwable th2) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x004b A[SYNTHETIC, Splitter:B:20:0x004b] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0057 A[SYNTHETIC, Splitter:B:26:0x0057] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String getFromAssets(java.lang.String r9, android.content.Context r10) {
        /*
            r8 = this;
            r0 = 0
            java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x0044 }
            android.content.res.Resources r6 = r10.getResources()     // Catch:{ Exception -> 0x0044 }
            android.content.res.AssetManager r6 = r6.getAssets()     // Catch:{ Exception -> 0x0044 }
            java.io.InputStream r6 = r6.open(r9)     // Catch:{ Exception -> 0x0044 }
            java.lang.String r7 = "UTF-8"
            r3.<init>(r6, r7)     // Catch:{ Exception -> 0x0044 }
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0044 }
            r1.<init>(r3)     // Catch:{ Exception -> 0x0044 }
            java.lang.String r4 = ""
            java.lang.String r5 = ""
        L_0x0020:
            java.lang.String r4 = r1.readLine()     // Catch:{ Exception -> 0x0063, all -> 0x0060 }
            if (r4 == 0) goto L_0x0038
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0063, all -> 0x0060 }
            r6.<init>()     // Catch:{ Exception -> 0x0063, all -> 0x0060 }
            java.lang.StringBuilder r6 = r6.append(r5)     // Catch:{ Exception -> 0x0063, all -> 0x0060 }
            java.lang.StringBuilder r6 = r6.append(r4)     // Catch:{ Exception -> 0x0063, all -> 0x0060 }
            java.lang.String r5 = r6.toString()     // Catch:{ Exception -> 0x0063, all -> 0x0060 }
            goto L_0x0020
        L_0x0038:
            if (r1 == 0) goto L_0x003d
            r1.close()     // Catch:{ IOException -> 0x003f }
        L_0x003d:
            r0 = r1
        L_0x003e:
            return r5
        L_0x003f:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x003d
        L_0x0044:
            r2 = move-exception
        L_0x0045:
            r2.printStackTrace()     // Catch:{ all -> 0x0054 }
            r5 = 0
            if (r0 == 0) goto L_0x003e
            r0.close()     // Catch:{ IOException -> 0x004f }
            goto L_0x003e
        L_0x004f:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x003e
        L_0x0054:
            r6 = move-exception
        L_0x0055:
            if (r0 == 0) goto L_0x005a
            r0.close()     // Catch:{ IOException -> 0x005b }
        L_0x005a:
            throw r6
        L_0x005b:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x005a
        L_0x0060:
            r6 = move-exception
            r0 = r1
            goto L_0x0055
        L_0x0063:
            r2 = move-exception
            r0 = r1
            goto L_0x0045
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.bundleInfo.AtlasBundleInfoManager.getFromAssets(java.lang.String, android.content.Context):java.lang.String");
    }
}
