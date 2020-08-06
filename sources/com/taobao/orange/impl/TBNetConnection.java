package com.taobao.orange.impl;

import android.os.RemoteException;
import anet.channel.request.ByteArrayEntry;
import anetwork.channel.Param;
import anetwork.channel.Request;
import anetwork.channel.aidl.Connection;
import anetwork.channel.degrade.DegradableNetwork;
import anetwork.channel.entity.RequestImpl;
import anetwork.channel.entity.StringParam;
import com.taobao.orange.GlobalOrange;
import com.taobao.orange.inner.INetConnection;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TBNetConnection implements INetConnection {
    private Connection connection;
    private DegradableNetwork network;
    private Map<String, String> params;
    private Request request;

    public void openConnection(String url) throws IOException {
        this.network = new DegradableNetwork(GlobalOrange.context);
        this.request = new RequestImpl(url);
        this.request.setCharset("utf-8");
        this.request.setConnectTimeout(5000);
        this.request.setReadTimeout(5000);
        if (this.params != null && !this.params.isEmpty()) {
            List<Param> paramList = new ArrayList<>();
            for (Map.Entry<String, String> entry : this.params.entrySet()) {
                paramList.add(new StringParam(entry.getKey(), entry.getValue()));
            }
            this.request.setParams(paramList);
        }
    }

    public void setMethod(String method) throws ProtocolException {
        this.request.setMethod(method);
    }

    public void setParams(Map<String, String> params2) {
        this.params = params2;
    }

    public void addHeader(String key, String value) {
        this.request.addHeader(key, value);
    }

    public void setBody(byte[] body) throws IOException {
        this.request.setBodyEntry(new ByteArrayEntry(body));
    }

    public int getResponseCode() throws IOException {
        if (this.connection == null) {
            return 0;
        }
        try {
            return this.connection.getStatusCode();
        } catch (RemoteException e) {
            throw new IOException(e);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x002e A[SYNTHETIC, Splitter:B:19:0x002e] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getResponse() throws java.io.IOException {
        /*
            r9 = this;
            anetwork.channel.aidl.Connection r6 = r9.connection
            if (r6 != 0) goto L_0x0006
            r6 = 0
        L_0x0005:
            return r6
        L_0x0006:
            r4 = 0
            r0 = 0
            anetwork.channel.aidl.Connection r6 = r9.connection     // Catch:{ RemoteException -> 0x0051 }
            anetwork.channel.aidl.ParcelableInputStream r4 = r6.getInputStream()     // Catch:{ RemoteException -> 0x0051 }
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ RemoteException -> 0x0051 }
            r1.<init>()     // Catch:{ RemoteException -> 0x0051 }
            r6 = 2048(0x800, float:2.87E-42)
            byte[] r2 = new byte[r6]     // Catch:{ RemoteException -> 0x0023, all -> 0x004e }
        L_0x0017:
            int r5 = r4.read(r2)     // Catch:{ RemoteException -> 0x0023, all -> 0x004e }
            r6 = -1
            if (r5 == r6) goto L_0x0035
            r6 = 0
            r1.write(r2, r6, r5)     // Catch:{ RemoteException -> 0x0023, all -> 0x004e }
            goto L_0x0017
        L_0x0023:
            r3 = move-exception
            r0 = r1
        L_0x0025:
            java.io.IOException r6 = new java.io.IOException     // Catch:{ all -> 0x002b }
            r6.<init>(r3)     // Catch:{ all -> 0x002b }
            throw r6     // Catch:{ all -> 0x002b }
        L_0x002b:
            r6 = move-exception
        L_0x002c:
            if (r4 == 0) goto L_0x0031
            r4.close()     // Catch:{ RemoteException -> 0x004c }
        L_0x0031:
            com.taobao.orange.util.OrangeUtils.close(r0)
            throw r6
        L_0x0035:
            java.lang.String r6 = new java.lang.String     // Catch:{ RemoteException -> 0x0023, all -> 0x004e }
            byte[] r7 = r1.toByteArray()     // Catch:{ RemoteException -> 0x0023, all -> 0x004e }
            java.lang.String r8 = "utf-8"
            r6.<init>(r7, r8)     // Catch:{ RemoteException -> 0x0023, all -> 0x004e }
            if (r4 == 0) goto L_0x0046
            r4.close()     // Catch:{ RemoteException -> 0x004a }
        L_0x0046:
            com.taobao.orange.util.OrangeUtils.close(r1)
            goto L_0x0005
        L_0x004a:
            r7 = move-exception
            goto L_0x0046
        L_0x004c:
            r7 = move-exception
            goto L_0x0031
        L_0x004e:
            r6 = move-exception
            r0 = r1
            goto L_0x002c
        L_0x0051:
            r3 = move-exception
            goto L_0x0025
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.orange.impl.TBNetConnection.getResponse():java.lang.String");
    }

    public Map<String, List<String>> getHeadFields() {
        if (this.connection == null) {
            return null;
        }
        try {
            return this.connection.getConnHeadFields();
        } catch (RemoteException e) {
            return null;
        }
    }

    public void connect() throws IOException {
        this.connection = this.network.getConnection(this.request, (Object) null);
    }

    public void disconnect() {
        try {
            if (this.connection != null) {
                this.connection.cancel();
            }
        } catch (RemoteException e) {
        }
    }
}
