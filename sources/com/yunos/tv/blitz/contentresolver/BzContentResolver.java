package com.yunos.tv.blitz.contentresolver;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import com.alibaba.appmonitor.sample.SampleConfigConstant;
import com.yunos.tv.blitz.account.BzDebugLog;
import com.yunos.tv.blitz.callback.BlitzCallbackManager;
import com.yunos.tv.blitz.global.BzAppConfig;
import com.yunos.tv.blitz.service.BlitzServiceUtils;
import com.yunos.tv.tvsdk.media.data.HuasuVideo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BzContentResolver {
    private static final String RESULT_FAIL_KEY = "error";
    private static final String RESULT_SUCC_KEY = "result";
    private static final String TAG = "BzContentResolver";
    private Map<String, BzContentObserver> mContentObserverMap = new TreeMap();
    private ContentResolver mContentResolver = BzAppConfig.context.getContext().getContentResolver();

    public class BzContentObserver extends ContentObserver {
        private String uri;

        public BzContentObserver(Handler handler, String uri2) {
            super(handler);
            this.uri = uri2;
        }

        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            BzDebugLog.d(BzContentResolver.TAG, "onChange, callNative");
            BlitzCallbackManager.callNative(this.uri, selfChange ? "true" : "false", (String) null);
        }
    }

    public String query(String param) {
        BzDebugLog.d(TAG, "query: " + param);
        JSONObject resultJson = new JSONObject();
        String uri = null;
        String selection = null;
        String sortOrder = null;
        String[] projection = null;
        String[] selectionArgs = null;
        try {
            JSONObject data = new JSONObject(param);
            uri = data.optString(HuasuVideo.TAG_URI);
            selection = data.optString("selection");
            sortOrder = data.optString("sortOrder");
            JSONArray projectionJsonArray = data.optJSONArray("projection");
            if (projectionJsonArray != null) {
                projection = new String[projectionJsonArray.length()];
                for (int i = 0; i < projectionJsonArray.length(); i++) {
                    projection[i] = projectionJsonArray.optString(i);
                }
            }
            JSONArray selectionArgsJsonArray = data.optJSONArray("selectionArgs");
            if (selectionArgsJsonArray != null) {
                selectionArgs = new String[selectionArgsJsonArray.length()];
                for (int i2 = 0; i2 < selectionArgsJsonArray.length(); i2++) {
                    selectionArgs[i2] = selectionArgsJsonArray.optString(i2);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BzDebugLog.d(TAG, "query: uri = " + uri + " projection = " + projection + " selection = " + selection + " selectionArgs = " + selectionArgs + " sortOrder = " + sortOrder);
        try {
            Cursor cs = this.mContentResolver.query(Uri.parse(uri), projection, selection, selectionArgs, sortOrder);
            JSONArray resultArray = new JSONArray();
            if (cs.moveToFirst()) {
                do {
                    int len = cs.getColumnCount();
                    JSONObject item = new JSONObject();
                    for (int i3 = 0; i3 < len; i3++) {
                        try {
                            item.put(cs.getColumnName(i3), cs.getString(i3));
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        }
                    }
                    resultArray.put(item);
                } while (cs.moveToNext());
            }
            cs.close();
            BzDebugLog.d(TAG, "result = " + resultArray.toString());
            resultJson.put("result", resultArray);
        } catch (Exception e3) {
            try {
                resultJson.put(RESULT_FAIL_KEY, e3.toString());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return resultJson.toString();
    }

    public String insert(String param) {
        JSONObject resultJson = new JSONObject();
        try {
            JSONObject data = new JSONObject(param);
            if (data != null) {
                String uri = data.optString(HuasuVideo.TAG_URI);
                ContentValues cv = jsonToContentValues(data.optJSONObject(SampleConfigConstant.VALUES));
                BzDebugLog.d(TAG, "insert cv = " + cv.toString());
                resultJson.put("result", this.mContentResolver.insert(Uri.parse(uri), cv).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                resultJson.put(RESULT_FAIL_KEY, e.toString());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return resultJson.toString();
    }

    public String update(String param) {
        JSONObject resultJson = new JSONObject();
        String[] selectionArgs = null;
        try {
            JSONObject data = new JSONObject(param);
            if (data != null) {
                String uri = data.optString(HuasuVideo.TAG_URI);
                ContentValues cv = jsonToContentValues(data.optJSONObject(SampleConfigConstant.VALUES));
                BzDebugLog.d(TAG, "update cv = " + cv.toString());
                String where = data.optString("where");
                JSONArray selectionArgsJsonArray = data.optJSONArray("selectionArgs");
                if (selectionArgsJsonArray != null) {
                    selectionArgs = new String[selectionArgsJsonArray.length()];
                    for (int i = 0; i < selectionArgsJsonArray.length(); i++) {
                        selectionArgs[i] = selectionArgsJsonArray.optString(i);
                    }
                }
                resultJson.put("result", this.mContentResolver.update(Uri.parse(uri), cv, where, selectionArgs));
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                resultJson.put(RESULT_FAIL_KEY, e.toString());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return resultJson.toString();
    }

    public String delete(String param) {
        JSONObject resultJson = new JSONObject();
        String[] selectionArgs = null;
        try {
            JSONObject data = new JSONObject(param);
            if (data != null) {
                String uri = data.optString(HuasuVideo.TAG_URI);
                String where = data.optString("where");
                JSONArray selectionArgsJsonArray = data.optJSONArray("selectionArgs");
                if (selectionArgsJsonArray != null) {
                    selectionArgs = new String[selectionArgsJsonArray.length()];
                    for (int i = 0; i < selectionArgsJsonArray.length(); i++) {
                        selectionArgs[i] = selectionArgsJsonArray.optString(i);
                    }
                }
                resultJson.put("result", this.mContentResolver.delete(Uri.parse(uri), where, selectionArgs));
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                resultJson.put(RESULT_FAIL_KEY, e.toString());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return resultJson.toString();
    }

    public String call(String param) {
        JSONObject resultJson = new JSONObject();
        try {
            JSONObject data = new JSONObject(param);
            String uri = data.getString(HuasuVideo.TAG_URI);
            String method = data.getString("method");
            String arg = data.optString("arg");
            String extras = data.optString(BlitzServiceUtils.CCLIENT_EXTRAS);
            Bundle extrasBundle = null;
            if (!extras.isEmpty()) {
                extrasBundle = new Bundle();
                JSONObject extrasJson = new JSONObject(extras);
                Iterator<String> keys = extrasJson.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    Object obj = extrasJson.opt(key);
                    if (obj instanceof JSONArray) {
                        JSONArray array = (JSONArray) obj;
                        ArrayList<String> extraList = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            extraList.add(array.optString(i));
                        }
                        extrasBundle.putStringArrayList(key, extraList);
                    } else {
                        extrasBundle.putString(key, obj == null ? "" : obj.toString());
                    }
                }
            }
            BzDebugLog.i(TAG, "uri = " + uri + " method = " + method + " arg = " + arg + " bundle = " + extrasBundle);
            Bundle resultBundle = this.mContentResolver.call(Uri.parse(uri), method, arg, extrasBundle);
            String result = "";
            if (resultBundle != null) {
                JSONObject json = new JSONObject();
                for (String key2 : resultBundle.keySet()) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        json.put(key2, JSONObject.wrap(resultBundle.get(key2)));
                    } else {
                        json.put(key2, resultBundle.get(key2));
                    }
                }
                result = json.toString();
                BzDebugLog.i(TAG, "result = " + result);
            }
            resultJson.put("result", result);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                resultJson.put(RESULT_FAIL_KEY, e.toString());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return resultJson.toString();
    }

    public String registerObserver(String param) {
        BzDebugLog.d(TAG, "registerObserver: " + param);
        JSONObject resultJson = new JSONObject();
        try {
            JSONObject paramJson = new JSONObject(param);
            String uri = paramJson.optString(HuasuVideo.TAG_URI);
            boolean notifyForDescendents = paramJson.optBoolean("notifyForDescendents", true);
            if (uri != null) {
                if (this.mContentObserverMap.containsKey(uri)) {
                    BzDebugLog.d(TAG, "already has key: " + uri);
                    this.mContentResolver.unregisterContentObserver(this.mContentObserverMap.get(uri));
                }
                BzContentObserver observer = new BzContentObserver((Handler) null, uri);
                this.mContentObserverMap.put(uri, observer);
                this.mContentResolver.registerContentObserver(Uri.parse(uri), notifyForDescendents, observer);
            } else {
                resultJson.put(RESULT_FAIL_KEY, "uri is empty");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                resultJson.put(RESULT_FAIL_KEY, e.toString());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return resultJson.toString();
    }

    public String unregisterObserver(String param) {
        BzDebugLog.d(TAG, "unregisterObserver: " + param);
        JSONObject resultJson = new JSONObject();
        try {
            String uri = new JSONObject(param).optString(HuasuVideo.TAG_URI);
            if (uri == null || !this.mContentObserverMap.containsKey(uri)) {
                resultJson.put(RESULT_FAIL_KEY, "invalid uri");
                return resultJson.toString();
            }
            ContentObserver observer = this.mContentObserverMap.get(uri);
            BzDebugLog.d(TAG, "unregisterObserver: " + observer);
            this.mContentResolver.unregisterContentObserver(observer);
            this.mContentObserverMap.remove(uri);
            resultJson.put("result", uri);
            return resultJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                resultJson.put(RESULT_FAIL_KEY, e.toString());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static ContentValues jsonToContentValues(JSONObject jsonObj) {
        ContentValues cv = null;
        if (jsonObj != null) {
            cv = new ContentValues();
            Iterator<String> keys = jsonObj.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Object obj = jsonObj.opt(key);
                if (obj != null) {
                    cv.put(key, obj.toString());
                } else {
                    cv.putNull(key);
                }
            }
        }
        if (cv != null) {
            BzDebugLog.d(TAG, "jsonToContentValues: " + cv.toString());
        }
        return cv;
    }
}
