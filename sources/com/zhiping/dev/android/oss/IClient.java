package com.zhiping.dev.android.oss;

import android.app.Application;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

public interface IClient {

    public interface CallBack {
        void onFailure(Object... objArr);

        boolean onFailureInMainThread();

        void onProgress(Object... objArr);

        boolean onProgressInMainThread();

        void onSuccess(Object... objArr);

        boolean onSuccessInMainThread();
    }

    public interface IConfig {
        String getAccessKey();

        String getAccessSecret();

        Application getApplication();

        boolean getEnableOssLog();

        String getEndPoint();

        Map getExtCfg();

        boolean isNeedListBucket();
    }

    void applyConfig(IConfig iConfig);

    void createBucket(String str, Map map, CallBack callBack);

    void delete(String str, String str2, CallBack callBack);

    void get(FileOutputStream fileOutputStream, String str, String str2, CallBack callBack);

    void isObjExist(String str, String str2, CallBack callBack);

    void listBuckets(Map map, CallBack callBack);

    void listObjects(String str, Map map, CallBack callBack);

    void put(File file, String str, String str2, CallBack callBack);

    void put(byte[] bArr, String str, String str2, CallBack callBack);
}
