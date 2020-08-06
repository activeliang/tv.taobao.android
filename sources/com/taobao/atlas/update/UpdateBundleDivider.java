package com.taobao.atlas.update;

import com.taobao.atlas.update.model.UpdateInfo;
import java.util.ArrayList;
import java.util.List;

public class UpdateBundleDivider {
    public static List<UpdateInfo.Item> dividePatchInfo(List<UpdateInfo.Item> allItem, int patchType) {
        List<UpdateInfo.Item> list = new ArrayList<>();
        if (allItem != null) {
            for (UpdateInfo.Item item : allItem) {
                if (item.patchType == patchType) {
                    UpdateInfo.Item newItem = UpdateInfo.Item.makeCopy(item);
                    newItem.patchType = patchType;
                    list.add(newItem);
                } else if (item.patchType == 3 && (patchType == 1 || patchType == 2)) {
                    UpdateInfo.Item newItem2 = UpdateInfo.Item.makeCopy(item);
                    newItem2.patchType = patchType;
                    list.add(newItem2);
                }
            }
        }
        return list;
    }
}
