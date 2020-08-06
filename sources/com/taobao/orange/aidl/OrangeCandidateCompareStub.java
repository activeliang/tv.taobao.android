package com.taobao.orange.aidl;

import android.os.RemoteException;
import com.taobao.orange.ICandidateCompare;
import com.taobao.orange.aidl.ParcelableCandidateCompare;

public class OrangeCandidateCompareStub extends ParcelableCandidateCompare.Stub {
    private ICandidateCompare mCompare;

    public OrangeCandidateCompareStub(ICandidateCompare compare) {
        this.mCompare = compare;
    }

    public String getName() {
        return this.mCompare.getClass().getSimpleName();
    }

    public boolean equals(String clientVal, String serverVal) throws RemoteException {
        return this.mCompare.equals(clientVal, serverVal);
    }

    public boolean equalsNot(String clientVal, String serverVal) throws RemoteException {
        return this.mCompare.equalsNot(clientVal, serverVal);
    }

    public boolean greater(String clientVal, String serverVal) throws RemoteException {
        return this.mCompare.greater(clientVal, serverVal);
    }

    public boolean greaterEquals(String clientVal, String serverVal) throws RemoteException {
        return this.mCompare.greaterEquals(clientVal, serverVal);
    }

    public boolean less(String clientVal, String serverVal) throws RemoteException {
        return this.mCompare.less(clientVal, serverVal);
    }

    public boolean lessEquals(String clientVal, String serverVal) throws RemoteException {
        return this.mCompare.lessEquals(clientVal, serverVal);
    }

    public boolean fuzzy(String clientVal, String serverVal) throws RemoteException {
        return this.mCompare.fuzzy(clientVal, serverVal);
    }

    public boolean fuzzyNot(String clientVal, String serverVal) throws RemoteException {
        return this.mCompare.fuzzyNot(clientVal, serverVal);
    }
}
