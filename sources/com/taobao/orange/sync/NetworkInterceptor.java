package com.taobao.orange.sync;

import android.text.TextUtils;
import anet.channel.bytes.ByteArray;
import anet.channel.request.Request;
import anetwork.channel.aidl.DefaultFinishEvent;
import anetwork.channel.interceptor.Callback;
import anetwork.channel.interceptor.Interceptor;
import com.taobao.orange.GlobalOrange;
import com.taobao.orange.OConstant;
import com.taobao.orange.OThreadFactory;
import com.taobao.orange.util.AndroidUtil;
import com.taobao.orange.util.OLog;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public class NetworkInterceptor implements Interceptor {
    static final String ORANGE_REQ_HEADER = "a-orange-q";
    static final String ORANGE_RES_HEADER = "a-orange-p";
    static final String TAG = "NetworkInterceptor";

    public Future intercept(final Interceptor.Chain chain) {
        Request request = chain.request();
        Callback callback = chain.callback();
        boolean result = false;
        if (GlobalOrange.indexUpdMode != OConstant.UPDMODE.O_EVENT && !TextUtils.isEmpty(request.getHost()) && !GlobalOrange.probeHosts.isEmpty()) {
            Iterator<String> it = GlobalOrange.probeHosts.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                if (request.getHost().contains(it.next())) {
                    result = true;
                    break;
                }
            }
        }
        if (result) {
            if (!TextUtils.isEmpty(GlobalOrange.reqOrangeHeader)) {
                request = chain.request().newBuilder().addHeader(ORANGE_REQ_HEADER, GlobalOrange.reqOrangeHeader).build();
            }
            callback = new Callback() {
                public void onResponseCode(int responseCode, final Map<String, List<String>> headers) {
                    if (headers != null && headers.containsKey(NetworkInterceptor.ORANGE_RES_HEADER)) {
                        OThreadFactory.execute(new Runnable() {
                            public void run() {
                                try {
                                    AndroidUtil.setThreadPriority();
                                    IndexUpdateHandler.updateIndex(NetworkInterceptor.getOrangeFromKey(headers, NetworkInterceptor.ORANGE_RES_HEADER), false);
                                } catch (Throwable t) {
                                    OLog.e(NetworkInterceptor.TAG, "intercept", t, new Object[0]);
                                }
                            }
                        });
                    }
                    chain.callback().onResponseCode(responseCode, headers);
                }

                public void onDataReceiveSize(int index, int total, ByteArray byteArray) {
                    chain.callback().onDataReceiveSize(index, total, byteArray);
                }

                public void onFinish(DefaultFinishEvent event) {
                    chain.callback().onFinish(event);
                }
            };
        }
        return chain.proceed(request, callback);
    }

    static String getOrangeFromKey(Map<String, List<String>> header, String headerKey) {
        List<String> values = null;
        Iterator<Map.Entry<String, List<String>>> it = header.entrySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Map.Entry<String, List<String>> entry = it.next();
            if (headerKey.equalsIgnoreCase(entry.getKey())) {
                values = entry.getValue();
                break;
            }
        }
        if (values == null || values.isEmpty()) {
            OLog.w(TAG, "getOrangeFromKey fail", "not exist a-orange-p");
            return null;
        }
        for (String value : values) {
            if (value != null && value.startsWith("resourceId")) {
                if (OLog.isPrintLog(1)) {
                    OLog.d(TAG, "getOrangeFromKey", "value", value);
                }
                try {
                    return URLDecoder.decode(value, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    OLog.w(TAG, "getOrangeFromKey", e, new Object[0]);
                    return null;
                }
            }
        }
        OLog.w(TAG, "getOrangeFromKey fail", "parseValue no resourceId");
        return null;
    }
}
