package com.zhiping.dev.android.oss;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCustomSignerCredentialProvider;
import com.alibaba.sdk.android.oss.internal.InternalRequestOperation;
import com.alibaba.sdk.android.oss.model.CannedAccessControlList;
import com.alibaba.sdk.android.oss.model.CreateBucketRequest;
import com.alibaba.sdk.android.oss.model.CreateBucketResult;
import com.alibaba.sdk.android.oss.model.DeleteBucketRequest;
import com.alibaba.sdk.android.oss.model.DeleteBucketResult;
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.ListBucketsRequest;
import com.alibaba.sdk.android.oss.model.ListBucketsResult;
import com.alibaba.sdk.android.oss.model.ListObjectsRequest;
import com.alibaba.sdk.android.oss.model.ListObjectsResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.alibaba.sdk.android.oss.model.StorageClass;
import com.yunos.tvtaobao.biz.listener.OnReminderListener;
import com.zhiping.dev.android.oss.IClient;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import mtopsdk.common.util.SymbolExpUtil;

class AliyunOssClient extends BaseIClient {
    /* access modifiers changed from: private */
    public static final String TAG = AliyunOssClient.class.getSimpleName();
    private static HandlerThread sharedHandlerThread = new HandlerThread(AliyunOssClient.class.getSimpleName());
    /* access modifiers changed from: private */
    public String accessKey;
    /* access modifiers changed from: private */
    public String accessSecret;
    /* access modifiers changed from: private */
    public OSSClient client;
    private String endPoint;
    /* access modifiers changed from: private */
    public Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private OSSCredentialProvider ossCredentialProvider;

    static {
        sharedHandlerThread.start();
    }

    public AliyunOssClient(IClient.IConfig IConfig) {
        applyConfig(IConfig);
    }

    private void doCfgClear() {
        this.client = null;
        this.ossCredentialProvider = null;
        this.accessKey = null;
        this.accessSecret = null;
        this.endPoint = null;
    }

    public void applyConfig(IClient.IConfig cfg) {
        doCfgClear();
        this.accessKey = cfg.getAccessKey();
        this.accessSecret = cfg.getAccessSecret();
        this.endPoint = cfg.getEndPoint();
        if (cfg.getExtCfg() != null && (cfg.getExtCfg().get("ossCredentialProvider") instanceof OSSCustomSignerCredentialProvider)) {
            this.ossCredentialProvider = (OSSCredentialProvider) cfg.getExtCfg().get("ossCredentialProvider");
        }
        if (TextUtils.isEmpty(this.accessKey)) {
            throw new RuntimeException("accessKey is empty");
        } else if (TextUtils.isEmpty(this.accessSecret)) {
            throw new RuntimeException("accessSecret is empty");
        } else if (TextUtils.isEmpty(this.endPoint)) {
            throw new RuntimeException("endPoint is empty");
        } else if (cfg.getApplication() == null) {
            throw new RuntimeException("application is null");
        } else {
            if (this.ossCredentialProvider == null) {
                this.ossCredentialProvider = new DefaultOSSCredentialProvider();
            }
            ClientConfiguration conf = new ClientConfiguration();
            conf.setConnectionTimeout(15000);
            conf.setSocketTimeout(15000);
            conf.setMaxConcurrentRequest(5);
            conf.setMaxErrorRetry(2);
            this.client = new OSSClient(cfg.getApplication(), this.endPoint, this.ossCredentialProvider, conf);
            if (cfg.isNeedListBucket()) {
                try {
                    Field f_mOss = this.client.getClass().getDeclaredField("mOss");
                    f_mOss.setAccessible(true);
                    Object o_mOss = f_mOss.get(this.client);
                    if ("com.alibaba.sdk.android.oss.OSSImpl".equals(o_mOss.getClass().getName())) {
                        Field f_internalRequestOperation = o_mOss.getClass().getDeclaredField("internalRequestOperation");
                        f_internalRequestOperation.setAccessible(true);
                        Object o_internalRequestOperation = f_internalRequestOperation.get(o_mOss);
                        if (o_internalRequestOperation instanceof InternalRequestOperation) {
                            Field f_service = o_internalRequestOperation.getClass().getDeclaredField("service");
                            f_service.setAccessible(true);
                            if (f_service.get(o_internalRequestOperation) == null) {
                                f_service.set(o_internalRequestOperation, new URI("http://oss.aliyuncs.com"));
                                Log.d(TAG, "doCfg reflect success  ");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (cfg.getEnableOssLog()) {
                OSSLog.enableLog();
            } else {
                OSSLog.disableLog();
            }
        }
    }

    /* access modifiers changed from: private */
    public String getFitSizeStr(long l) {
        double gb = (double) (((float) l) / 1.07374182E9f);
        double mb = (double) (((float) l) / 1048576.0f);
        double kb = (double) (((float) l) / 1024.0f);
        if (gb > 1.0d) {
            return String.format("%.2fGB", new Object[]{Double.valueOf(gb)});
        } else if (mb > 1.0d) {
            return String.format("%.2fMB", new Object[]{Double.valueOf(mb)});
        } else if (kb <= 1.0d) {
            return null;
        } else {
            return String.format("%.2fkB", new Object[]{Double.valueOf(kb)});
        }
    }

    public void put(File file, String bucketName, String objectKey, final IClient.CallBack callBack) {
        if (TextUtils.isEmpty(bucketName)) {
            throw new RuntimeException("bucketName has no value .");
        } else if (TextUtils.isEmpty(objectKey)) {
            throw new RuntimeException("objectKey has no value .");
        } else if (file != null && file.exists() && file.canRead()) {
            PutObjectRequest request = new PutObjectRequest(bucketName, objectKey, file.getPath());
            request.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                long lastProgressTime = System.currentTimeMillis();
                long nowProgressTime = this.lastProgressTime;

                public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                    Log.d(AliyunOssClient.TAG, "file upload progress: " + currentSize + WVNativeCallbackUtil.SEPERATER + totalSize);
                    this.nowProgressTime = System.currentTimeMillis();
                    if (this.nowProgressTime - this.lastProgressTime > 100) {
                        this.lastProgressTime = this.nowProgressTime;
                        if (callBack != null) {
                            if (callBack.onProgressInMainThread()) {
                                final PutObjectRequest putObjectRequest = request;
                                final long j = currentSize;
                                final long j2 = totalSize;
                                AliyunOssClient.this.mainThreadHandler.post(new Runnable() {
                                    public void run() {
                                        callBack.onProgress(putObjectRequest, Long.valueOf(j), Long.valueOf(j2));
                                    }
                                });
                            } else {
                                callBack.onProgress(request, Long.valueOf(currentSize), Long.valueOf(totalSize));
                            }
                        }
                    }
                    if (currentSize == totalSize && callBack != null) {
                        if (callBack.onProgressInMainThread()) {
                            final PutObjectRequest putObjectRequest2 = request;
                            final long j3 = currentSize;
                            final long j4 = totalSize;
                            AliyunOssClient.this.mainThreadHandler.post(new Runnable() {
                                public void run() {
                                    callBack.onProgress(putObjectRequest2, Long.valueOf(j3), Long.valueOf(j4));
                                }
                            });
                            return;
                        }
                        callBack.onProgress(request, Long.valueOf(currentSize), Long.valueOf(totalSize));
                    }
                }
            });
            this.client.asyncPutObject(request, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                public void onSuccess(final PutObjectRequest request, final PutObjectResult result) {
                    Log.d(AliyunOssClient.TAG, "file upload success!");
                    if (callBack == null) {
                        return;
                    }
                    if (callBack.onSuccessInMainThread()) {
                        AliyunOssClient.this.mainThreadHandler.post(new Runnable() {
                            public void run() {
                                callBack.onSuccess(request, result);
                            }
                        });
                        return;
                    }
                    callBack.onSuccess(request, result);
                }

                public void onFailure(final PutObjectRequest request, final ClientException clientException, final ServiceException serviceException) {
                    Log.d(AliyunOssClient.TAG, "file upload failure:" + clientException + "," + serviceException);
                    if (callBack == null) {
                        return;
                    }
                    if (callBack.onFailureInMainThread()) {
                        AliyunOssClient.this.mainThreadHandler.post(new Runnable() {
                            public void run() {
                                callBack.onFailure(request, clientException, serviceException);
                            }
                        });
                        return;
                    }
                    callBack.onFailure(request, clientException, serviceException);
                }
            });
        } else if (callBack == null) {
        } else {
            if (callBack.onFailureInMainThread()) {
                this.mainThreadHandler.post(new Runnable() {
                    public void run() {
                        callBack.onFailure(new RuntimeException("false == (file != null && file.exists() && file.canRead())"));
                    }
                });
                return;
            }
            callBack.onFailure(new RuntimeException("false == (file != null && file.exists() && file.canRead())"));
        }
    }

    public void put(byte[] bytes, String bucketName, String objectKey, final IClient.CallBack callBack) {
        if (TextUtils.isEmpty(bucketName)) {
            throw new RuntimeException("bucketName has no value .");
        } else if (TextUtils.isEmpty(objectKey)) {
            throw new RuntimeException("objectKey has no value .");
        } else if (bytes != null && bytes.length > 0) {
            PutObjectRequest request = new PutObjectRequest(bucketName, objectKey, bytes);
            request.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                long lastProgressTime = System.currentTimeMillis();
                long nowProgressTime = this.lastProgressTime;

                public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                    Log.d(AliyunOssClient.TAG, "bytes upload progress: " + currentSize + WVNativeCallbackUtil.SEPERATER + totalSize);
                    this.nowProgressTime = System.currentTimeMillis();
                    if (this.nowProgressTime - this.lastProgressTime > 100) {
                        this.lastProgressTime = this.nowProgressTime;
                        if (callBack != null) {
                            if (callBack.onProgressInMainThread()) {
                                final PutObjectRequest putObjectRequest = request;
                                final long j = currentSize;
                                final long j2 = totalSize;
                                AliyunOssClient.this.mainThreadHandler.post(new Runnable() {
                                    public void run() {
                                        callBack.onProgress(putObjectRequest, Long.valueOf(j), Long.valueOf(j2));
                                    }
                                });
                            } else {
                                callBack.onProgress(request, Long.valueOf(currentSize), Long.valueOf(totalSize));
                            }
                        }
                    }
                    if (currentSize == totalSize && callBack != null) {
                        if (callBack.onProgressInMainThread()) {
                            final PutObjectRequest putObjectRequest2 = request;
                            final long j3 = currentSize;
                            final long j4 = totalSize;
                            AliyunOssClient.this.mainThreadHandler.post(new Runnable() {
                                public void run() {
                                    callBack.onProgress(putObjectRequest2, Long.valueOf(j3), Long.valueOf(j4));
                                }
                            });
                            return;
                        }
                        callBack.onProgress(request, Long.valueOf(currentSize), Long.valueOf(totalSize));
                    }
                }
            });
            this.client.asyncPutObject(request, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                public void onSuccess(final PutObjectRequest request, final PutObjectResult result) {
                    Log.d(AliyunOssClient.TAG, "bytes upload success!");
                    if (callBack == null) {
                        return;
                    }
                    if (callBack.onSuccessInMainThread()) {
                        AliyunOssClient.this.mainThreadHandler.post(new Runnable() {
                            public void run() {
                                callBack.onSuccess(request, result);
                            }
                        });
                        return;
                    }
                    callBack.onSuccess(request, result);
                }

                public void onFailure(final PutObjectRequest request, final ClientException clientException, final ServiceException serviceException) {
                    Log.d(AliyunOssClient.TAG, "bytes upload failure:" + clientException + "," + serviceException);
                    if (callBack == null) {
                        return;
                    }
                    if (callBack.onFailureInMainThread()) {
                        AliyunOssClient.this.mainThreadHandler.post(new Runnable() {
                            public void run() {
                                callBack.onFailure(request, clientException, serviceException);
                            }
                        });
                        return;
                    }
                    callBack.onFailure(request, clientException, serviceException);
                }
            });
        } else if (callBack == null) {
        } else {
            if (callBack.onFailureInMainThread()) {
                this.mainThreadHandler.post(new Runnable() {
                    public void run() {
                        callBack.onFailure(new RuntimeException("false == (bytes != null && bytes.length>0)"));
                    }
                });
                return;
            }
            callBack.onFailure(new RuntimeException("false == (bytes != null && bytes.length>0)"));
        }
    }

    public void get(final FileOutputStream fos, String bucketName, String objectKey, final IClient.CallBack callBack) {
        if (TextUtils.isEmpty(bucketName)) {
            throw new RuntimeException("bucketName has no value .");
        } else if (TextUtils.isEmpty(objectKey)) {
            throw new RuntimeException("objectKey has no value .");
        } else {
            this.client.asyncGetObject(new GetObjectRequest(bucketName, objectKey), new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
                public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                    Log.d(AliyunOssClient.TAG, "get success:" + request.getObjectKey() + "," + AliyunOssClient.this.getFitSizeStr(result.getContentLength()));
                    try {
                        long contentLength = result.getContentLength();
                        long progress = 0;
                        InputStream is = result.getObjectContent();
                        byte[] buffer = new byte[1024];
                        long lastProgressTime = System.currentTimeMillis();
                        long j = lastProgressTime;
                        do {
                            int length = is.read(buffer);
                            fos.write(buffer, 0, length);
                            progress += (long) length;
                            long nowProgressTime = System.currentTimeMillis();
                            if (nowProgressTime - lastProgressTime > 100) {
                                lastProgressTime = nowProgressTime;
                                Log.d(AliyunOssClient.TAG, "get progress: " + progress + WVNativeCallbackUtil.SEPERATER + contentLength);
                                if (callBack != null) {
                                    final long pro = progress;
                                    final long len = contentLength;
                                    if (callBack.onProgressInMainThread()) {
                                        final GetObjectRequest getObjectRequest = request;
                                        AliyunOssClient.this.mainThreadHandler.post(new Runnable() {
                                            public void run() {
                                                callBack.onProgress(getObjectRequest, Long.valueOf(pro), Long.valueOf(len));
                                            }
                                        });
                                    } else {
                                        callBack.onProgress(request, Long.valueOf(pro), Long.valueOf(len));
                                    }
                                }
                            }
                        } while (progress != contentLength);
                        Log.d(AliyunOssClient.TAG, "get progress: " + progress + WVNativeCallbackUtil.SEPERATER + contentLength);
                        Log.d(AliyunOssClient.TAG, "get progress: done");
                        fos.flush();
                        if (callBack != null) {
                            final long pro2 = progress;
                            final long len2 = contentLength;
                            if (callBack.onProgressInMainThread()) {
                                final GetObjectRequest getObjectRequest2 = request;
                                AliyunOssClient.this.mainThreadHandler.post(new Runnable() {
                                    public void run() {
                                        callBack.onProgress(getObjectRequest2, Long.valueOf(pro2), Long.valueOf(len2));
                                    }
                                });
                            } else {
                                callBack.onProgress(request, Long.valueOf(pro2), Long.valueOf(len2));
                            }
                        }
                        if (callBack == null) {
                            return;
                        }
                        if (callBack.onSuccessInMainThread()) {
                            final GetObjectRequest getObjectRequest3 = request;
                            final GetObjectResult getObjectResult = result;
                            AliyunOssClient.this.mainThreadHandler.post(new Runnable() {
                                public void run() {
                                    callBack.onSuccess(getObjectRequest3, getObjectResult, fos);
                                }
                            });
                            return;
                        }
                        callBack.onSuccess(request, result, fos);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (callBack == null) {
                            return;
                        }
                        if (callBack.onFailureInMainThread()) {
                            final GetObjectRequest getObjectRequest4 = request;
                            final GetObjectResult getObjectResult2 = result;
                            AliyunOssClient.this.mainThreadHandler.post(new Runnable() {
                                public void run() {
                                    callBack.onFailure(getObjectRequest4, getObjectResult2, e);
                                }
                            });
                            return;
                        }
                        callBack.onFailure(request, result, e);
                    }
                }

                public void onFailure(final GetObjectRequest request, final ClientException clientException, final ServiceException serviceException) {
                    Log.d(AliyunOssClient.TAG, "get failure:" + clientException + "," + serviceException);
                    if (callBack == null) {
                        return;
                    }
                    if (callBack.onFailureInMainThread()) {
                        AliyunOssClient.this.mainThreadHandler.post(new Runnable() {
                            public void run() {
                                callBack.onFailure(request, clientException, serviceException);
                            }
                        });
                        return;
                    }
                    callBack.onFailure(request, clientException, serviceException);
                }
            });
        }
    }

    public void delete(String bucketName, String objectKey, final IClient.CallBack callBack) {
        if (TextUtils.isEmpty(bucketName)) {
            throw new RuntimeException("bucketName has no value .");
        } else if (TextUtils.isEmpty(objectKey)) {
            this.client.asyncDeleteBucket(new DeleteBucketRequest(bucketName), new OSSCompletedCallback<DeleteBucketRequest, DeleteBucketResult>() {
                public void onSuccess(final DeleteBucketRequest request, final DeleteBucketResult result) {
                    Log.d(AliyunOssClient.TAG, "delete bucketName success.");
                    if (callBack == null) {
                        return;
                    }
                    if (callBack.onSuccessInMainThread()) {
                        AliyunOssClient.this.mainThreadHandler.post(new Runnable() {
                            public void run() {
                                callBack.onSuccess(request, result);
                            }
                        });
                        return;
                    }
                    callBack.onSuccess(request, result);
                }

                public void onFailure(final DeleteBucketRequest request, final ClientException clientException, final ServiceException serviceException) {
                    Log.d(AliyunOssClient.TAG, "delete bucketName failure:" + clientException + "," + serviceException);
                    if (callBack == null) {
                        return;
                    }
                    if (callBack.onFailureInMainThread()) {
                        AliyunOssClient.this.mainThreadHandler.post(new Runnable() {
                            public void run() {
                                callBack.onFailure(request, clientException, serviceException);
                            }
                        });
                        return;
                    }
                    callBack.onFailure(request, clientException, serviceException);
                }
            });
        } else {
            this.client.asyncDeleteObject(new DeleteObjectRequest(bucketName, objectKey), new OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult>() {
                public void onSuccess(final DeleteObjectRequest request, final DeleteObjectResult result) {
                    Log.d(AliyunOssClient.TAG, "delete object success.");
                    if (callBack == null) {
                        return;
                    }
                    if (callBack.onSuccessInMainThread()) {
                        AliyunOssClient.this.mainThreadHandler.post(new Runnable() {
                            public void run() {
                                callBack.onSuccess(request, result);
                            }
                        });
                        return;
                    }
                    callBack.onSuccess(request, result);
                }

                public void onFailure(final DeleteObjectRequest request, final ClientException clientException, final ServiceException serviceException) {
                    Log.d(AliyunOssClient.TAG, "delete object failure:" + clientException + "," + serviceException);
                    if (callBack == null) {
                        return;
                    }
                    if (callBack.onFailureInMainThread()) {
                        AliyunOssClient.this.mainThreadHandler.post(new Runnable() {
                            public void run() {
                                callBack.onFailure(request, clientException, serviceException);
                            }
                        });
                        return;
                    }
                    callBack.onFailure(request, clientException, serviceException);
                }
            });
        }
    }

    public void listObjects(String bucketName, Map addOnParams, final IClient.CallBack callBack) {
        if (TextUtils.isEmpty(bucketName)) {
            throw new RuntimeException("bucketName has no value .");
        }
        ListObjectsRequest request = new ListObjectsRequest(bucketName);
        if (addOnParams != null && !addOnParams.isEmpty()) {
            Object prefix = addOnParams.get(RequestParameters.PREFIX);
            if (prefix instanceof String) {
                request.setPrefix((String) prefix);
            }
            Object marker = addOnParams.get(RequestParameters.MARKER);
            if (marker instanceof String) {
                request.setMarker((String) marker);
            }
            Object maxKeys = addOnParams.get("maxKeys");
            if (maxKeys instanceof Integer) {
                request.setMaxKeys((Integer) maxKeys);
            }
            Object delimiter = addOnParams.get(RequestParameters.DELIMITER);
            if (delimiter instanceof Integer) {
                request.setDelimiter((String) delimiter);
            }
        }
        this.client.asyncListObjects(request, new OSSCompletedCallback<ListObjectsRequest, ListObjectsResult>() {
            public void onSuccess(final ListObjectsRequest request, final ListObjectsResult result) {
                Log.d(AliyunOssClient.TAG, "listObjects success :" + (result.getObjectSummaries() != null ? Integer.valueOf(result.getObjectSummaries().size()) : "no result"));
                if (callBack == null) {
                    return;
                }
                if (callBack.onSuccessInMainThread()) {
                    AliyunOssClient.this.mainThreadHandler.post(new Runnable() {
                        public void run() {
                            callBack.onSuccess(request, result);
                        }
                    });
                } else {
                    callBack.onSuccess(request, result);
                }
            }

            public void onFailure(final ListObjectsRequest request, final ClientException clientException, final ServiceException serviceException) {
                Log.d(AliyunOssClient.TAG, "listObjects failure:" + clientException + "," + serviceException);
                if (callBack == null) {
                    return;
                }
                if (callBack.onFailureInMainThread()) {
                    AliyunOssClient.this.mainThreadHandler.post(new Runnable() {
                        public void run() {
                            callBack.onFailure(request, clientException, serviceException);
                        }
                    });
                    return;
                }
                callBack.onFailure(request, clientException, serviceException);
            }
        });
    }

    public void createBucket(String bucketName, Map addOnParams, final IClient.CallBack callBack) {
        if (TextUtils.isEmpty(bucketName)) {
            throw new RuntimeException("bucketName has no value .");
        }
        CreateBucketRequest request = new CreateBucketRequest(bucketName);
        if (addOnParams != null && !addOnParams.isEmpty()) {
            Object bucketACL = addOnParams.get("bucketACL");
            if (bucketACL instanceof CannedAccessControlList) {
                request.setBucketACL((CannedAccessControlList) bucketACL);
            }
            Object bucketStorageClass = addOnParams.get("bucketStorageClass");
            if (bucketStorageClass instanceof StorageClass) {
                request.setBucketStorageClass((StorageClass) bucketStorageClass);
            }
        }
        this.client.asyncCreateBucket(request, new OSSCompletedCallback<CreateBucketRequest, CreateBucketResult>() {
            public void onSuccess(final CreateBucketRequest request, final CreateBucketResult result) {
                Log.d(AliyunOssClient.TAG, "createBucket success.");
                if (callBack == null) {
                    return;
                }
                if (callBack.onSuccessInMainThread()) {
                    AliyunOssClient.this.mainThreadHandler.post(new Runnable() {
                        public void run() {
                            callBack.onSuccess(request, result);
                        }
                    });
                    return;
                }
                callBack.onSuccess(request, result);
            }

            public void onFailure(final CreateBucketRequest request, final ClientException clientException, final ServiceException serviceException) {
                Log.d(AliyunOssClient.TAG, "createBucket failure:" + clientException + "," + serviceException);
                if (callBack == null) {
                    return;
                }
                if (callBack.onFailureInMainThread()) {
                    AliyunOssClient.this.mainThreadHandler.post(new Runnable() {
                        public void run() {
                            callBack.onFailure(request, clientException, serviceException);
                        }
                    });
                    return;
                }
                callBack.onFailure(request, clientException, serviceException);
            }
        });
    }

    public void listBuckets(Map addOnParams, final IClient.CallBack callBack) {
        ListBucketsRequest request = new ListBucketsRequest();
        if (addOnParams != null && !addOnParams.isEmpty()) {
            Object prefix = addOnParams.get(RequestParameters.PREFIX);
            if (prefix instanceof String) {
                request.setPrefix((String) prefix);
            }
            Object marker = addOnParams.get(RequestParameters.MARKER);
            if (marker instanceof String) {
                request.setMarker((String) marker);
            }
            Object maxKeys = addOnParams.get("maxKeys");
            if (maxKeys instanceof Integer) {
                request.setMaxKeys((Integer) maxKeys);
            }
        }
        this.client.asyncListBuckets(request, new OSSCompletedCallback<ListBucketsRequest, ListBucketsResult>() {
            public void onSuccess(final ListBucketsRequest request, final ListBucketsResult result) {
                Log.d(AliyunOssClient.TAG, "listBuckets success : " + (result.getBuckets() != null ? Integer.valueOf(result.getBuckets().size()) : "no result"));
                if (callBack == null) {
                    return;
                }
                if (callBack.onSuccessInMainThread()) {
                    AliyunOssClient.this.mainThreadHandler.post(new Runnable() {
                        public void run() {
                            callBack.onSuccess(request, result);
                        }
                    });
                } else {
                    callBack.onSuccess(request, result);
                }
            }

            public void onFailure(final ListBucketsRequest request, final ClientException clientException, final ServiceException serviceException) {
                Log.d(AliyunOssClient.TAG, "listBuckets failure:" + clientException + "," + serviceException);
                if (callBack == null) {
                    return;
                }
                if (callBack.onFailureInMainThread()) {
                    AliyunOssClient.this.mainThreadHandler.post(new Runnable() {
                        public void run() {
                            callBack.onFailure(request, clientException, serviceException);
                        }
                    });
                    return;
                }
                callBack.onFailure(request, clientException, serviceException);
            }
        });
    }

    public void isObjExist(final String bucketName, final String objectKey, final IClient.CallBack callBack) {
        if (TextUtils.isEmpty(bucketName)) {
            throw new RuntimeException("bucketName has no value .");
        } else if (TextUtils.isEmpty(objectKey)) {
            throw new RuntimeException("objectKey has no value .");
        } else {
            new Handler(sharedHandlerThread.getLooper()).post(new Runnable() {
                public void run() {
                    try {
                        final boolean rlt = AliyunOssClient.this.client.doesObjectExist(bucketName, objectKey);
                        Log.d(AliyunOssClient.TAG, "isObjExist success : " + bucketName + " - " + objectKey + " - " + rlt);
                        if (callBack == null) {
                            return;
                        }
                        if (callBack.onSuccessInMainThread()) {
                            AliyunOssClient.this.mainThreadHandler.post(new Runnable() {
                                public void run() {
                                    callBack.onSuccess(Boolean.valueOf(rlt));
                                }
                            });
                            return;
                        }
                        callBack.onSuccess(Boolean.valueOf(rlt));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(AliyunOssClient.TAG, "isObjExist failure : " + bucketName + " - " + objectKey + " - " + e.getMessage());
                        if (callBack == null) {
                            return;
                        }
                        if (callBack.onFailureInMainThread()) {
                            AliyunOssClient.this.mainThreadHandler.post(new Runnable() {
                                public void run() {
                                    callBack.onFailure(e);
                                }
                            });
                            return;
                        }
                        callBack.onFailure(e);
                    }
                }
            });
        }
    }

    private class DefaultOSSCredentialProvider extends OSSCustomSignerCredentialProvider {
        private DefaultOSSCredentialProvider() {
        }

        public byte[] getHmacMd5Str(String s, String keyString) {
            try {
                SecretKeySpec key = new SecretKeySpec(keyString.getBytes("UTF-8"), "HmacSHA1");
                Mac mac = Mac.getInstance("HmacSHA1");
                mac.init(key);
                byte[] bytes = mac.doFinal(s.getBytes("ASCII"));
                StringBuffer hash = new StringBuffer();
                for (byte b : bytes) {
                    String hex = Integer.toHexString(b & OnReminderListener.RET_FULL);
                    if (hex.length() == 1) {
                        hash.append('0');
                    }
                    hash.append(hex);
                }
                return bytes;
            } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException e) {
                return null;
            }
        }

        public String signContent(String content) {
            try {
                return "OSS " + AliyunOssClient.this.accessKey + SymbolExpUtil.SYMBOL_COLON + Base64.encodeToString(getHmacMd5Str(content, AliyunOssClient.this.accessSecret), 2);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
