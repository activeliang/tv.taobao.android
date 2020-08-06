package com.taobao.securityjni.bcast;

import android.content.Intent;
import com.bftv.fui.constantplugin.Constant;
import com.yunos.tvtaobao.biz.listener.OnReminderListener;
import java.lang.reflect.Array;
import java.util.ArrayList;

@Deprecated
public class AppStateManager {
    public static final String DNS_ACTION = "setaobao.bbox.DNS";
    public static final String EXTRA_DNS_IP = "IPAddress";
    public static final String EXTRA_DNS_LOCAL = "DNSinfolocal";
    public static final String EXTRA_DNS_NET = "DNSinfonet";
    public static final String EXTRA_RT = "RTinfo";
    public static final String EXTRA_SPITEP = "SPITEPinfo";
    private static final int IPV4_SIZE = 4;
    public static int Init = 0;
    public static final String RT_ACTION = "setaobao.bbox.RT";
    public static final int RT_VALUE_100_PERMISSION = 10;
    public static final int RT_VALUE_INVALID = -1;
    public static final int RT_VALUE_LIKELY_1 = 1;
    public static final int RT_VALUE_LIKELY_2 = 2;
    public static final int RT_VALUE_LIKELY_3 = 3;
    public static final int RT_VALUE_LIKELY_4 = 4;
    public static final int RT_VALUE_LIKELY_5 = 5;
    public static final int RT_VALUE_LIKELY_6 = 6;
    public static final int RT_VALUE_LIKELY_7 = 7;
    public static final int RT_VALUE_LIKELY_8 = 8;
    public static final int RT_VALUE_LIKELY_9 = 9;
    public static final int RT_VALUE_UNDETECTABLE = 0;
    public static final String SPITEP_ACTION = "setaobao.bbox.SPITEP";
    public static final int SPITEP_VALUE_NS_0 = 0;
    public static final int SPITEP_VALUE_NS_1 = 1;
    public static final int SPITEP_VALUE_NS_2 = 2;
    public static final int SPITEP_VALUE_NS_3 = 3;

    public static class DoaminIP {
        public byte[][] ip;
        public String name;

        private String IpToString() {
            StringBuilder build = new StringBuilder();
            if (this.ip == null) {
                return Constant.NULL;
            }
            for (int i = 0; i < this.ip.length; i++) {
                byte[] entry = this.ip[i];
                build.append("ip[").append(i).append("]=");
                if (entry != null) {
                    for (int j = 0; j < entry.length; j++) {
                        build.append(entry[j] & OnReminderListener.RET_FULL);
                        if (j != entry.length - 1) {
                            build.append(':');
                        }
                    }
                } else {
                    build.append(Constant.NULL);
                }
                build.append("  ");
            }
            return build.toString();
        }

        public String toString() {
            return "DoaminIP [name=" + this.name + ", ip=" + IpToString() + "]";
        }
    }

    public static final ArrayList<DoaminIP> parserDomainIP(Intent intent) {
        byte[] ip = intent.getByteArrayExtra(EXTRA_DNS_IP);
        if (ip == null) {
            return null;
        }
        ArrayList<DoaminIP> dip = new ArrayList<>();
        byte[][] ipAddr = null;
        int i = 0;
        while (i < ip.length) {
            int namelen = ip[i] & 255;
            String name = new String(ip, i + 1, namelen);
            int i2 = i + namelen + 1;
            int iplen = ip[i2] & 255;
            if (iplen + i2 > ip.length - 1) {
                break;
            }
            int itemNum = iplen / 4;
            if (itemNum > 0) {
                ipAddr = (byte[][]) Array.newInstance(Byte.TYPE, new int[]{itemNum, 4});
                for (int j = 0; j < itemNum; j++) {
                    System.arraycopy(ip, i2 + 1 + (j * 4), ipAddr[j], 0, 4);
                }
            }
            i = i2 + iplen + 1;
            DoaminIP domainName = new DoaminIP();
            domainName.name = name;
            domainName.ip = ipAddr;
            dip.add(domainName);
        }
        return dip;
    }
}
