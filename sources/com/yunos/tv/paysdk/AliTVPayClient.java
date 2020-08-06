package com.yunos.tv.paysdk;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;

public class AliTVPayClient {

    public interface IPayCallback {
        void onPayProcessEnd(AliTVPayResult aliTVPayResult);
    }

    public void aliTVPay(Context context, String orderInfo, String signInfo, Bundle parameters, IPayCallback payCallback) throws RemoteException {
    }
}
