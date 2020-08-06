package com.zhiping.dev.android.oss;

import com.zhiping.dev.android.oss.IClient;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

public abstract class BaseIClient implements IClient {
    public void put(File file, String bucketName, String objectKey, IClient.CallBack callBack) {
    }

    public void put(byte[] bytes, String bucketName, String objectKey, IClient.CallBack callBack) {
    }

    public void get(FileOutputStream fos, String bucketName, String objectKey, IClient.CallBack callBack) {
    }

    public void delete(String bucketName, String objectKey, IClient.CallBack callBack) {
    }

    public void listObjects(String bucketName, Map addOnParams, IClient.CallBack callBack) {
    }

    public void createBucket(String bucketName, Map addOnParams, IClient.CallBack callBack) {
    }

    public void listBuckets(Map addOnParams, IClient.CallBack callBack) {
    }

    public void isObjExist(String bucketName, String objectKey, IClient.CallBack callBack) {
    }

    public void applyConfig(IClient.IConfig iConfig) {
    }

    public static abstract class BaseIClientCallBack implements IClient.CallBack {
        public void onSuccess(Object... objects) {
        }

        public void onProgress(Object... objects) {
        }

        public void onFailure(Object... objects) {
        }

        public boolean onSuccessInMainThread() {
            return true;
        }

        public boolean onFailureInMainThread() {
            return true;
        }

        public boolean onProgressInMainThread() {
            return true;
        }
    }
}
