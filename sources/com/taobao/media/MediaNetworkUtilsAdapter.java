package com.taobao.media;

import anet.channel.status.NetworkStatusHelper;
import com.taobao.adapter.INetworkUtilsAdapter;

public class MediaNetworkUtilsAdapter implements INetworkUtilsAdapter {
    public String getNetworkStutas() {
        try {
            String status = NetworkStatusHelper.getStatus().getType();
            char c = 65535;
            switch (status.hashCode()) {
                case 1621:
                    if (status.equals("2G")) {
                        c = 3;
                        break;
                    }
                    break;
                case 1652:
                    if (status.equals("3G")) {
                        c = 2;
                        break;
                    }
                    break;
                case 1683:
                    if (status.equals("4G")) {
                        c = 1;
                        break;
                    }
                    break;
                case 2664213:
                    if (status.equals("WIFI")) {
                        c = 0;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 1:
                case 2:
                case 3:
                    return status;
                default:
                    return "2G";
            }
        } catch (Throwable th) {
            return "4G";
        }
    }

    public boolean isConnected() {
        try {
            return NetworkStatusHelper.isConnected();
        } catch (Throwable th) {
            return true;
        }
    }
}
