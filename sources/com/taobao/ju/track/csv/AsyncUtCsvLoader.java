package com.taobao.ju.track.csv;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import com.taobao.ju.track.util.LogUtil;
import java.util.HashMap;
import java.util.Map;

@TargetApi(3)
public class AsyncUtCsvLoader extends AsyncTask<Object, Object, Map<String, Map<String, String>>> {
    private static final String TAG = AsyncUtCsvLoader.class.getSimpleName();
    private Map<String, Map<String, String>> mParams;

    public AsyncUtCsvLoader(Map<String, Map<String, String>> mapMap) {
        this.mParams = mapMap;
    }

    /* access modifiers changed from: protected */
    public Map<String, Map<String, String>> doInBackground(Object... objects) {
        if (objects == null || objects.length < 2 || !(objects[0] instanceof Context) || !(objects[1] instanceof String)) {
            return new HashMap();
        }
        return new UtCsvLoader().load(objects[0], objects[1]);
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Map<String, Map<String, String>> stringMapMap) {
        int i = 0;
        if (!(this.mParams == null || stringMapMap == null || stringMapMap.size() <= 0)) {
            this.mParams.clear();
            this.mParams.putAll(stringMapMap);
        }
        String str = TAG;
        Object[] objArr = new Object[3];
        objArr[0] = "loadPrams-Success";
        objArr[1] = getClass().getSimpleName();
        if (this.mParams != null) {
            i = this.mParams.size();
        }
        objArr[2] = Integer.valueOf(i);
        LogUtil.d(str, objArr);
    }
}
