package com.taobao.detail.domain.template.android;

import java.util.ArrayList;
import java.util.HashMap;

public class LayoutVO extends ComponentVO {
    public ComponentVO bottom;
    public ArrayList<ComponentVO> components;
    public ComponentVO header;
    public HashMap<String, String> replaceDataset;
    public String version;

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0023  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.taobao.detail.domain.template.android.ComponentVO getChild(java.lang.String r5) {
        /*
            r4 = this;
            r2 = 0
            if (r5 == 0) goto L_0x0015
            int r3 = r5.length()
            if (r3 == 0) goto L_0x0015
            java.util.ArrayList r3 = r4.children
            if (r3 == 0) goto L_0x0015
            java.util.ArrayList r3 = r4.children
            boolean r3 = r3.isEmpty()
            if (r3 == 0) goto L_0x0017
        L_0x0015:
            r0 = r2
        L_0x0016:
            return r0
        L_0x0017:
            java.util.ArrayList r3 = r4.children
            java.util.Iterator r1 = r3.iterator()
        L_0x001d:
            boolean r3 = r1.hasNext()
            if (r3 == 0) goto L_0x003a
            java.lang.Object r0 = r1.next()
            com.taobao.detail.domain.template.android.ComponentVO r0 = (com.taobao.detail.domain.template.android.ComponentVO) r0
            java.lang.String r3 = r0.key
            boolean r3 = r5.equals(r3)
            if (r3 != 0) goto L_0x0016
            java.lang.String r3 = r0.ID
            boolean r3 = r5.equals(r3)
            if (r3 == 0) goto L_0x001d
            goto L_0x0016
        L_0x003a:
            r0 = r2
            goto L_0x0016
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.detail.domain.template.android.LayoutVO.getChild(java.lang.String):com.taobao.detail.domain.template.android.ComponentVO");
    }
}
