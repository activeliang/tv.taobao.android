package com.taobao.wireless.trade.mbuy.sdk.engine;

import android.util.Pair;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.co.ComponentType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class BuySubmitModule {
    protected BuyEngine engine;

    public BuySubmitModule(BuyEngine engine2) {
        this.engine = engine2;
    }

    public JSONObject generateAsyncRequestData(Component trigger) {
        Map<String, Component> index;
        if (trigger == null) {
            return null;
        }
        try {
            BuyEngineContext context = this.engine.getContext();
            if (context == null || (index = context.getIndex()) == null) {
                return null;
            }
            JSONArray input = context.getInput();
            Set<Component> result = new HashSet<>();
            result.add(trigger);
            if (input == null || input.isEmpty()) {
                return format(result, trigger);
            }
            Iterator<Object> it = input.iterator();
            while (it.hasNext()) {
                String componentKey = (String) it.next();
                if (index.get(componentKey) != null) {
                    result.add(index.get(componentKey));
                }
            }
            return format(result, trigger);
        } catch (Throwable th) {
            return null;
        }
    }

    public String generateAsyncRequestDataWithZip(Component trigger) {
        try {
            return compress(JSONObject.toJSONString(generateAsyncRequestData(trigger)));
        } catch (Throwable th) {
            return "";
        }
    }

    public JSONObject generateFinalSubmitData() {
        Map<String, Component> index;
        BuyEngineContext context = this.engine.getContext();
        if (context == null || (index = context.getIndex()) == null) {
            return null;
        }
        List<Component> result = new ArrayList<>(index.size());
        for (Component component : index.values()) {
            if (component.getType() != ComponentType.SYNTHETIC && component.isSubmit()) {
                result.add(component);
            }
        }
        return format(result, (Component) null);
    }

    public String generateFinalSubmitDataWithZip() {
        try {
            String submitString = JSONObject.toJSONString(generateFinalSubmitData());
            executeRecovery();
            return compress(submitString);
        } catch (Throwable th) {
            return "";
        }
    }

    public String generateCurrentBuyDataWithZip() {
        try {
            JSONObject currentBuyData = new JSONObject((Map<String, Object>) this.engine.getContext().getOrigin());
            currentBuyData.put("data", (Object) this.engine.getContext().getData());
            currentBuyData.put("hierarchy", (Object) this.engine.getContext().getHierarchy());
            currentBuyData.put("linkage", (Object) this.engine.getContext().getLinkage());
            return compress(currentBuyData.toJSONString());
        } catch (Throwable th) {
            return "";
        }
    }

    public boolean executeRollback() {
        BuyEngineContext context = this.engine.getContext();
        RollbackProtocol rollbackProtocol = context.getRollbackProtocol();
        if (rollbackProtocol == null) {
            return false;
        }
        rollbackProtocol.rollback();
        context.setRollbackProtocol((RollbackProtocol) null);
        return true;
    }

    public void executeRecovery() {
        Queue<Pair<JSONObject, Pair<String, Object>>> recovery = this.engine.getContext().getRecovery();
        for (Pair<JSONObject, Pair<String, Object>> pair : recovery) {
            ((JSONObject) pair.first).put((String) ((Pair) pair.second).first, ((Pair) pair.second).second);
        }
        recovery.clear();
    }

    private JSONObject format(Collection<?> collection, Component trigger) {
        JSONObject d;
        JSONObject data = new JSONObject();
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            Component component = (Component) it.next();
            if (trigger == null) {
                d = component.convertToFinalSubmitData();
            } else {
                d = component.convertToAsyncSubmitData();
            }
            if (d != null) {
                data.put(component.getKey(), (Object) d);
            }
        }
        JSONObject linkage = new JSONObject();
        BuyEngineContext context = this.engine.getContext();
        JSONObject originalCommon = context.getCommon();
        if (originalCommon != null) {
            JSONObject common = new JSONObject();
            String validateParams = originalCommon.getString("validateParams");
            boolean compress = originalCommon.getBooleanValue("compress");
            if (trigger == null) {
                String submitParams = originalCommon.getString("submitParams");
                if (submitParams == null && validateParams == null) {
                    common = originalCommon;
                } else {
                    if (submitParams != null && !submitParams.isEmpty()) {
                        common.put("submitParams", (Object) submitParams);
                    }
                    if (validateParams != null && !validateParams.isEmpty()) {
                        common.put("validateParams", (Object) validateParams);
                    }
                    common.put("compress", (Object) Boolean.valueOf(compress));
                }
            } else {
                String queryParams = originalCommon.getString("queryParams");
                if (queryParams == null && validateParams == null) {
                    common = originalCommon;
                } else {
                    if (queryParams != null && !queryParams.isEmpty()) {
                        common.put("queryParams", (Object) queryParams);
                    }
                    if (validateParams != null && !validateParams.isEmpty()) {
                        common.put("validateParams", (Object) validateParams);
                    }
                    common.put("compress", (Object) Boolean.valueOf(compress));
                }
            }
            linkage.put("common", (Object) common);
        }
        String signature = context.getLinkage().getString("signature");
        if (signature != null && !signature.isEmpty()) {
            linkage.put("signature", (Object) signature);
        }
        JSONObject hierarchy = new JSONObject();
        hierarchy.put("structure", (Object) context.getStructure());
        JSONObject submitData = new JSONObject();
        submitData.put("data", (Object) data);
        submitData.put("linkage", (Object) linkage);
        submitData.put("hierarchy", (Object) hierarchy);
        if (trigger != null) {
            submitData.put("operator", (Object) trigger.getKey());
        }
        return submitData;
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x002f  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0040 A[SYNTHETIC, Splitter:B:24:0x0040] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x004a A[SYNTHETIC, Splitter:B:30:0x004a] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0056 A[SYNTHETIC, Splitter:B:40:0x0056] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String compress(java.lang.String r8) {
        /*
            if (r8 == 0) goto L_0x0008
            boolean r6 = r8.isEmpty()
            if (r6 == 0) goto L_0x000c
        L_0x0008:
            java.lang.String r6 = ""
        L_0x000b:
            return r6
        L_0x000c:
            r0 = 0
            java.io.ByteArrayOutputStream r4 = new java.io.ByteArrayOutputStream
            int r6 = r8.length()
            r4.<init>(r6)
            r5 = 1
            r2 = 0
            java.util.zip.GZIPOutputStream r3 = new java.util.zip.GZIPOutputStream     // Catch:{ IOException -> 0x003c, all -> 0x0047 }
            r3.<init>(r4)     // Catch:{ IOException -> 0x003c, all -> 0x0047 }
            java.lang.String r6 = "utf-8"
            byte[] r6 = r8.getBytes(r6)     // Catch:{ IOException -> 0x006d, all -> 0x006a }
            r3.write(r6)     // Catch:{ IOException -> 0x006d, all -> 0x006a }
            if (r3 == 0) goto L_0x002c
            r3.close()     // Catch:{ IOException -> 0x0038, all -> 0x0067 }
        L_0x002c:
            r2 = r3
        L_0x002d:
            if (r5 != 0) goto L_0x0056
            java.lang.String r6 = ""
            r4.close()     // Catch:{ IOException -> 0x0036 }
            goto L_0x000b
        L_0x0036:
            r7 = move-exception
            goto L_0x000b
        L_0x0038:
            r1 = move-exception
            r5 = 0
            r2 = r3
            goto L_0x002d
        L_0x003c:
            r1 = move-exception
        L_0x003d:
            r5 = 0
            if (r2 == 0) goto L_0x002d
            r2.close()     // Catch:{ IOException -> 0x0044 }
            goto L_0x002d
        L_0x0044:
            r1 = move-exception
            r5 = 0
            goto L_0x002d
        L_0x0047:
            r6 = move-exception
        L_0x0048:
            if (r2 == 0) goto L_0x004d
            r2.close()     // Catch:{ IOException -> 0x0053 }
        L_0x004d:
            throw r6     // Catch:{ all -> 0x004e }
        L_0x004e:
            r6 = move-exception
        L_0x004f:
            r4.close()     // Catch:{ IOException -> 0x0065 }
        L_0x0052:
            throw r6
        L_0x0053:
            r1 = move-exception
            r5 = 0
            goto L_0x004d
        L_0x0056:
            byte[] r0 = r4.toByteArray()     // Catch:{ all -> 0x004e }
            r4.close()     // Catch:{ IOException -> 0x0063 }
        L_0x005d:
            r6 = 0
            java.lang.String r6 = android.util.Base64.encodeToString(r0, r6)
            goto L_0x000b
        L_0x0063:
            r6 = move-exception
            goto L_0x005d
        L_0x0065:
            r7 = move-exception
            goto L_0x0052
        L_0x0067:
            r6 = move-exception
            r2 = r3
            goto L_0x004f
        L_0x006a:
            r6 = move-exception
            r2 = r3
            goto L_0x0048
        L_0x006d:
            r1 = move-exception
            r2 = r3
            goto L_0x003d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.wireless.trade.mbuy.sdk.engine.BuySubmitModule.compress(java.lang.String):java.lang.String");
    }
}
