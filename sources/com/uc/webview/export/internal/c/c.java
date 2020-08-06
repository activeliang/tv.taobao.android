package com.uc.webview.export.internal.c;

import com.uc.webview.export.internal.interfaces.INetworkDelegate;
import com.uc.webview.export.internal.interfaces.IRequestData;
import com.uc.webview.export.internal.interfaces.IResponseData;
import com.uc.webview.export.internal.interfaces.InvokeObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* compiled from: ProGuard */
public final class c implements InvokeObject {
    private INetworkDelegate a;

    public c(INetworkDelegate iNetworkDelegate) {
        this.a = iNetworkDelegate;
    }

    public final Object invoke(int i, Object[] objArr) {
        switch (i) {
            case 1:
                return ((b) this.a.onSendRequest(new b(objArr[0]))).a;
            case 2:
            case 3:
                return ((a) this.a.onReceiveResponse(new a(objArr[0]))).a;
            default:
                return null;
        }
    }

    /* compiled from: ProGuard */
    private static class b implements IRequestData {
        /* access modifiers changed from: private */
        public Map<String, Object> a = new HashMap();

        public b(Map<String, Object> map) {
            this.a = map;
        }

        public final String getUrl() {
            return (String) this.a.get("1");
        }

        public final void setUrl(String str) {
            this.a.put("1", str);
        }

        public final String getMethod() {
            return (String) this.a.get("2");
        }

        public final void setMethod(String str) {
            this.a.put("2", str);
        }

        public final Map<String, String> getHeaders() {
            return (Map) this.a.get("3");
        }

        public final void setHeaders(Map<String, String> map) {
            this.a.put("3", map);
        }
    }

    /* compiled from: ProGuard */
    private static class a implements IResponseData {
        /* access modifiers changed from: private */
        public Map<String, Object> a = new HashMap();

        public a(Map<String, Object> map) {
            this.a = map;
        }

        public final String getUrl() {
            return (String) this.a.get("1");
        }

        public final void setUrl(String str) {
            this.a.put("1", str);
        }

        public final int getStatus() {
            return ((Integer) this.a.get("4")).intValue();
        }

        public final void setStatus(int i) {
            this.a.put("4", new Integer(i));
        }

        public final Map<String, String> getHeaders() {
            return (Map) this.a.get("3");
        }

        public final void setHeaders(Map<String, String> map) {
            this.a.put("3", map);
        }

        public final Map<String, List<String>> getHeadersV2() {
            return (Map) this.a.get("3");
        }

        public final void setHeadersV2(Map<String, List<String>> map) {
            this.a.put("3", map);
        }
    }
}
