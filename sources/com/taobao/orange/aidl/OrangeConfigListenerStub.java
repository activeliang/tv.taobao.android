package com.taobao.orange.aidl;

import android.os.RemoteException;
import com.taobao.orange.OBaseListener;
import com.taobao.orange.OConfigListener;
import com.taobao.orange.OConstant;
import com.taobao.orange.OrangeConfigListener;
import com.taobao.orange.OrangeConfigListenerV1;
import com.taobao.orange.aidl.ParcelableConfigListener;
import java.util.HashMap;
import java.util.Map;

public class OrangeConfigListenerStub extends ParcelableConfigListener.Stub {
    private boolean append = true;
    private OBaseListener mListener;

    public OrangeConfigListenerStub(OBaseListener listener) {
        this.mListener = listener;
    }

    public OrangeConfigListenerStub(OBaseListener listener, boolean append2) {
        this.append = append2;
        this.mListener = listener;
    }

    public boolean isAppend() {
        return this.append;
    }

    public void onConfigUpdate(String groupName, Map args) throws RemoteException {
        if (this.mListener instanceof OrangeConfigListener) {
            ((OrangeConfigListener) this.mListener).onConfigUpdate(groupName);
        } else if (this.mListener instanceof OrangeConfigListenerV1) {
            ((OrangeConfigListenerV1) this.mListener).onConfigUpdate(groupName, Boolean.parseBoolean((String) ((HashMap) args).get(OConstant.LISTENERKEY_FROM_CACHE)));
        } else if (this.mListener instanceof OConfigListener) {
            ((OConfigListener) this.mListener).onConfigUpdate(groupName, (HashMap) args);
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return this.mListener.equals(((OrangeConfigListenerStub) o).mListener);
    }

    public int hashCode() {
        return this.mListener.hashCode();
    }
}
