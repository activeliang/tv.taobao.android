package com.bftv.fui.thirdparty;

import android.text.TextUtils;
import com.bftv.fui.thirdparty.bean.AllIntent;
import com.bftv.fui.thirdparty.bean.MiddleData;
import java.util.List;

public class AllIntentManager {
    private static AllIntentManager ourInstance = new AllIntentManager();

    public static AllIntentManager getInstance() {
        return ourInstance;
    }

    private AllIntentManager() {
    }

    public String getUri(MiddleData middleData, String entranceWords) {
        if (middleData == null || middleData.listIntent == null || middleData.listIntent.size() <= 0) {
            return null;
        }
        List<AllIntent> listAllIntent = middleData.listIntent;
        if (TextUtils.isEmpty(entranceWords)) {
            return listAllIntent.get(0).uri;
        }
        for (AllIntent allIntent : listAllIntent) {
            if (allIntent.entranceWords.equals(entranceWords)) {
                return allIntent.uri;
            }
        }
        return listAllIntent.get(0).uri;
    }
}
