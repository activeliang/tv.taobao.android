package com.alibaba.sdk.android.oss.internal;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.HttpMethod;
import com.alibaba.sdk.android.oss.common.OSSHeaders;
import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.utils.BinaryUtil;
import com.alibaba.sdk.android.oss.common.utils.CRC64;
import com.alibaba.sdk.android.oss.common.utils.DateUtil;
import com.alibaba.sdk.android.oss.common.utils.HttpHeaders;
import com.alibaba.sdk.android.oss.common.utils.HttpUtil;
import com.alibaba.sdk.android.oss.common.utils.OSSUtils;
import com.alibaba.sdk.android.oss.common.utils.VersionInfoUtils;
import com.alibaba.sdk.android.oss.exception.InconsistentException;
import com.alibaba.sdk.android.oss.internal.ResponseParsers;
import com.alibaba.sdk.android.oss.model.AbortMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.AbortMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.AppendObjectRequest;
import com.alibaba.sdk.android.oss.model.AppendObjectResult;
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.CopyObjectRequest;
import com.alibaba.sdk.android.oss.model.CopyObjectResult;
import com.alibaba.sdk.android.oss.model.CreateBucketRequest;
import com.alibaba.sdk.android.oss.model.CreateBucketResult;
import com.alibaba.sdk.android.oss.model.DeleteBucketRequest;
import com.alibaba.sdk.android.oss.model.DeleteBucketResult;
import com.alibaba.sdk.android.oss.model.DeleteMultipleObjectRequest;
import com.alibaba.sdk.android.oss.model.DeleteMultipleObjectResult;
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.GetBucketACLRequest;
import com.alibaba.sdk.android.oss.model.GetBucketACLResult;
import com.alibaba.sdk.android.oss.model.GetBucketInfoRequest;
import com.alibaba.sdk.android.oss.model.GetBucketInfoResult;
import com.alibaba.sdk.android.oss.model.GetObjectACLRequest;
import com.alibaba.sdk.android.oss.model.GetObjectACLResult;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.GetSymlinkRequest;
import com.alibaba.sdk.android.oss.model.GetSymlinkResult;
import com.alibaba.sdk.android.oss.model.HeadObjectRequest;
import com.alibaba.sdk.android.oss.model.HeadObjectResult;
import com.alibaba.sdk.android.oss.model.ImagePersistRequest;
import com.alibaba.sdk.android.oss.model.ImagePersistResult;
import com.alibaba.sdk.android.oss.model.InitiateMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.InitiateMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.ListBucketsRequest;
import com.alibaba.sdk.android.oss.model.ListBucketsResult;
import com.alibaba.sdk.android.oss.model.ListMultipartUploadsRequest;
import com.alibaba.sdk.android.oss.model.ListMultipartUploadsResult;
import com.alibaba.sdk.android.oss.model.ListObjectsRequest;
import com.alibaba.sdk.android.oss.model.ListObjectsResult;
import com.alibaba.sdk.android.oss.model.ListPartsRequest;
import com.alibaba.sdk.android.oss.model.ListPartsResult;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.OSSResult;
import com.alibaba.sdk.android.oss.model.PartETag;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.alibaba.sdk.android.oss.model.PutSymlinkRequest;
import com.alibaba.sdk.android.oss.model.PutSymlinkResult;
import com.alibaba.sdk.android.oss.model.RestoreObjectRequest;
import com.alibaba.sdk.android.oss.model.RestoreObjectResult;
import com.alibaba.sdk.android.oss.model.TriggerCallbackRequest;
import com.alibaba.sdk.android.oss.model.TriggerCallbackResult;
import com.alibaba.sdk.android.oss.model.UploadPartRequest;
import com.alibaba.sdk.android.oss.model.UploadPartResult;
import com.alibaba.sdk.android.oss.network.ExecutionContext;
import com.alibaba.sdk.android.oss.network.OSSRequestTask;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import okhttp3.Cache;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

public class InternalRequestOperation {
    private static final int LIST_PART_MAX_RETURNS = 1000;
    private static final int MAX_PART_NUMBER = 10000;
    private static ExecutorService executorService = Executors.newFixedThreadPool(5, new ThreadFactory() {
        public Thread newThread(Runnable r) {
            return new Thread(r, "oss-android-api-thread");
        }
    });
    private Context applicationContext;
    private ClientConfiguration conf;
    private OSSCredentialProvider credentialProvider;
    private volatile URI endpoint;
    private OkHttpClient innerClient;
    private int maxRetryCount = 2;
    /* access modifiers changed from: private */
    public URI service;

    public InternalRequestOperation(Context context, final URI endpoint2, OSSCredentialProvider credentialProvider2, ClientConfiguration conf2) {
        this.applicationContext = context;
        this.endpoint = endpoint2;
        this.credentialProvider = credentialProvider2;
        this.conf = conf2;
        OkHttpClient.Builder builder = new OkHttpClient.Builder().followRedirects(false).followSslRedirects(false).retryOnConnectionFailure(false).cache((Cache) null).hostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return HttpsURLConnection.getDefaultHostnameVerifier().verify(endpoint2.getHost(), session);
            }
        });
        if (conf2 != null) {
            Dispatcher dispatcher = new Dispatcher();
            dispatcher.setMaxRequests(conf2.getMaxConcurrentRequest());
            builder.connectTimeout((long) conf2.getConnectionTimeout(), TimeUnit.MILLISECONDS).readTimeout((long) conf2.getSocketTimeout(), TimeUnit.MILLISECONDS).writeTimeout((long) conf2.getSocketTimeout(), TimeUnit.MILLISECONDS).dispatcher(dispatcher);
            if (!(conf2.getProxyHost() == null || conf2.getProxyPort() == 0)) {
                builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(conf2.getProxyHost(), conf2.getProxyPort())));
            }
            this.maxRetryCount = conf2.getMaxErrorRetry();
        }
        this.innerClient = builder.build();
    }

    public InternalRequestOperation(Context context, OSSCredentialProvider credentialProvider2, ClientConfiguration conf2) {
        try {
            this.service = new URI("http://oss.aliyuncs.com");
            this.endpoint = new URI("http://127.0.0.1");
            this.applicationContext = context;
            this.credentialProvider = credentialProvider2;
            this.conf = conf2;
            OkHttpClient.Builder builder = new OkHttpClient.Builder().followRedirects(false).followSslRedirects(false).retryOnConnectionFailure(false).cache((Cache) null).hostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return HttpsURLConnection.getDefaultHostnameVerifier().verify(InternalRequestOperation.this.service.getHost(), session);
                }
            });
            if (conf2 != null) {
                Dispatcher dispatcher = new Dispatcher();
                dispatcher.setMaxRequests(conf2.getMaxConcurrentRequest());
                builder.connectTimeout((long) conf2.getConnectionTimeout(), TimeUnit.MILLISECONDS).readTimeout((long) conf2.getSocketTimeout(), TimeUnit.MILLISECONDS).writeTimeout((long) conf2.getSocketTimeout(), TimeUnit.MILLISECONDS).dispatcher(dispatcher);
                if (!(conf2.getProxyHost() == null || conf2.getProxyPort() == 0)) {
                    builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(conf2.getProxyHost(), conf2.getProxyPort())));
                }
                this.maxRetryCount = conf2.getMaxErrorRetry();
            }
            this.innerClient = builder.build();
        } catch (Exception e) {
            throw new IllegalArgumentException("Endpoint must be a string like 'http://oss-cn-****.aliyuncs.com',or your cname like 'http://image.cnamedomain.com'!");
        }
    }

    public PutObjectResult syncPutObject(PutObjectRequest request) throws ClientException, ServiceException {
        PutObjectResult result = putObject(request, (OSSCompletedCallback<PutObjectRequest, PutObjectResult>) null).getResult();
        checkCRC64(request, result);
        return result;
    }

    public OSSAsyncTask<PutObjectResult> putObject(PutObjectRequest request, final OSSCompletedCallback<PutObjectRequest, PutObjectResult> completedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(request.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.PUT);
        requestMessage.setBucketName(request.getBucketName());
        requestMessage.setObjectKey(request.getObjectKey());
        if (request.getUploadData() != null) {
            requestMessage.setUploadData(request.getUploadData());
        }
        if (request.getUploadFilePath() != null) {
            requestMessage.setUploadFilePath(request.getUploadFilePath());
        }
        if (request.getCallbackParam() != null) {
            requestMessage.getHeaders().put("x-oss-callback", OSSUtils.populateMapToBase64JsonString(request.getCallbackParam()));
        }
        if (request.getCallbackVars() != null) {
            requestMessage.getHeaders().put("x-oss-callback-var", OSSUtils.populateMapToBase64JsonString(request.getCallbackVars()));
        }
        OSSUtils.populateRequestMetadata(requestMessage.getHeaders(), request.getMetadata());
        canonicalizeRequestMessage(requestMessage, request);
        ExecutionContext<PutObjectRequest, PutObjectResult> executionContext = new ExecutionContext<>(getInnerClient(), request, this.applicationContext);
        if (completedCallback != null) {
            executionContext.setCompletedCallback(new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                    InternalRequestOperation.this.checkCRC64(request, result, completedCallback);
                }

                public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                    completedCallback.onFailure(request, clientException, serviceException);
                }
            });
        }
        if (request.getRetryCallback() != null) {
            executionContext.setRetryCallback(request.getRetryCallback());
        }
        executionContext.setProgressCallback(request.getProgressCallback());
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask<>(requestMessage, new ResponseParsers.PutObjectResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public OSSAsyncTask<CreateBucketResult> createBucket(CreateBucketRequest request, OSSCompletedCallback<CreateBucketRequest, CreateBucketResult> completedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(request.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.PUT);
        requestMessage.setBucketName(request.getBucketName());
        if (request.getBucketACL() != null) {
            requestMessage.getHeaders().put(OSSHeaders.OSS_CANNED_ACL, request.getBucketACL().toString());
        }
        try {
            Map<String, String> configures = new HashMap<>();
            if (request.getLocationConstraint() != null) {
                configures.put(CreateBucketRequest.TAB_LOCATIONCONSTRAINT, request.getLocationConstraint());
            }
            configures.put(CreateBucketRequest.TAB_STORAGECLASS, request.getBucketStorageClass().toString());
            requestMessage.createBucketRequestBodyMarshall(configures);
            canonicalizeRequestMessage(requestMessage, request);
            ExecutionContext<CreateBucketRequest, CreateBucketResult> executionContext = new ExecutionContext<>(getInnerClient(), request, this.applicationContext);
            if (completedCallback != null) {
                executionContext.setCompletedCallback(completedCallback);
            }
            return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask<>(requestMessage, new ResponseParsers.CreateBucketResponseParser(), executionContext, this.maxRetryCount)), executionContext);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public OSSAsyncTask<DeleteBucketResult> deleteBucket(DeleteBucketRequest request, OSSCompletedCallback<DeleteBucketRequest, DeleteBucketResult> completedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(request.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.DELETE);
        requestMessage.setBucketName(request.getBucketName());
        canonicalizeRequestMessage(requestMessage, request);
        ExecutionContext<DeleteBucketRequest, DeleteBucketResult> executionContext = new ExecutionContext<>(getInnerClient(), request, this.applicationContext);
        if (completedCallback != null) {
            executionContext.setCompletedCallback(completedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask<>(requestMessage, new ResponseParsers.DeleteBucketResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public OSSAsyncTask<GetBucketInfoResult> getBucketInfo(GetBucketInfoRequest request, OSSCompletedCallback<GetBucketInfoRequest, GetBucketInfoResult> completedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        Map<String, String> query = new LinkedHashMap<>();
        query.put(RequestParameters.SUBRESOURCE_BUCKETINFO, "");
        requestMessage.setIsAuthorizationRequired(request.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.GET);
        requestMessage.setBucketName(request.getBucketName());
        requestMessage.setParameters(query);
        canonicalizeRequestMessage(requestMessage, request);
        ExecutionContext<GetBucketInfoRequest, GetBucketInfoResult> executionContext = new ExecutionContext<>(getInnerClient(), request, this.applicationContext);
        if (completedCallback != null) {
            executionContext.setCompletedCallback(completedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask<>(requestMessage, new ResponseParsers.GetBucketInfoResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public OSSAsyncTask<GetBucketACLResult> getBucketACL(GetBucketACLRequest request, OSSCompletedCallback<GetBucketACLRequest, GetBucketACLResult> completedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        Map<String, String> query = new LinkedHashMap<>();
        query.put(RequestParameters.SUBRESOURCE_ACL, "");
        requestMessage.setIsAuthorizationRequired(request.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.GET);
        requestMessage.setBucketName(request.getBucketName());
        requestMessage.setParameters(query);
        canonicalizeRequestMessage(requestMessage, request);
        ExecutionContext<GetBucketACLRequest, GetBucketACLResult> executionContext = new ExecutionContext<>(getInnerClient(), request, this.applicationContext);
        if (completedCallback != null) {
            executionContext.setCompletedCallback(completedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask<>(requestMessage, new ResponseParsers.GetBucketACLResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public AppendObjectResult syncAppendObject(AppendObjectRequest request) throws ClientException, ServiceException {
        AppendObjectResult result = appendObject(request, (OSSCompletedCallback<AppendObjectRequest, AppendObjectResult>) null).getResult();
        boolean checkCRC = request.getCRC64() == OSSRequest.CRC64Config.YES;
        if (request.getInitCRC64() != null && checkCRC) {
            result.setClientCRC(Long.valueOf(CRC64.combine(request.getInitCRC64().longValue(), result.getClientCRC().longValue(), result.getNextPosition() - request.getPosition())));
        }
        checkCRC64(request, result);
        return result;
    }

    public OSSAsyncTask<AppendObjectResult> appendObject(AppendObjectRequest request, final OSSCompletedCallback<AppendObjectRequest, AppendObjectResult> completedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(request.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.POST);
        requestMessage.setBucketName(request.getBucketName());
        requestMessage.setObjectKey(request.getObjectKey());
        if (request.getUploadData() != null) {
            requestMessage.setUploadData(request.getUploadData());
        }
        if (request.getUploadFilePath() != null) {
            requestMessage.setUploadFilePath(request.getUploadFilePath());
        }
        requestMessage.getParameters().put(RequestParameters.SUBRESOURCE_APPEND, "");
        requestMessage.getParameters().put("position", String.valueOf(request.getPosition()));
        OSSUtils.populateRequestMetadata(requestMessage.getHeaders(), request.getMetadata());
        canonicalizeRequestMessage(requestMessage, request);
        ExecutionContext<AppendObjectRequest, AppendObjectResult> executionContext = new ExecutionContext<>(getInnerClient(), request, this.applicationContext);
        if (completedCallback != null) {
            executionContext.setCompletedCallback(new OSSCompletedCallback<AppendObjectRequest, AppendObjectResult>() {
                public void onSuccess(AppendObjectRequest request, AppendObjectResult result) {
                    boolean checkCRC = request.getCRC64() == OSSRequest.CRC64Config.YES;
                    if (request.getInitCRC64() != null && checkCRC) {
                        result.setClientCRC(Long.valueOf(CRC64.combine(request.getInitCRC64().longValue(), result.getClientCRC().longValue(), result.getNextPosition() - request.getPosition())));
                    }
                    InternalRequestOperation.this.checkCRC64(request, result, completedCallback);
                }

                public void onFailure(AppendObjectRequest request, ClientException clientException, ServiceException serviceException) {
                    completedCallback.onFailure(request, clientException, serviceException);
                }
            });
        }
        executionContext.setProgressCallback(request.getProgressCallback());
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask<>(requestMessage, new ResponseParsers.AppendObjectResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public OSSAsyncTask<HeadObjectResult> headObject(HeadObjectRequest request, OSSCompletedCallback<HeadObjectRequest, HeadObjectResult> completedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(request.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.HEAD);
        requestMessage.setBucketName(request.getBucketName());
        requestMessage.setObjectKey(request.getObjectKey());
        canonicalizeRequestMessage(requestMessage, request);
        ExecutionContext<HeadObjectRequest, HeadObjectResult> executionContext = new ExecutionContext<>(getInnerClient(), request, this.applicationContext);
        if (completedCallback != null) {
            executionContext.setCompletedCallback(completedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask<>(requestMessage, new ResponseParsers.HeadObjectResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public OSSAsyncTask<GetObjectResult> getObject(GetObjectRequest request, OSSCompletedCallback<GetObjectRequest, GetObjectResult> completedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(request.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.GET);
        requestMessage.setBucketName(request.getBucketName());
        requestMessage.setObjectKey(request.getObjectKey());
        if (request.getRange() != null) {
            requestMessage.getHeaders().put(HttpHeaders.RANGE, request.getRange().toString());
        }
        if (request.getxOssProcess() != null) {
            requestMessage.getParameters().put(RequestParameters.X_OSS_PROCESS, request.getxOssProcess());
        }
        canonicalizeRequestMessage(requestMessage, request);
        if (request.getRequestHeaders() != null) {
            for (Map.Entry<String, String> entry : request.getRequestHeaders().entrySet()) {
                requestMessage.getHeaders().put(entry.getKey(), entry.getValue());
            }
        }
        ExecutionContext<GetObjectRequest, GetObjectResult> executionContext = new ExecutionContext<>(getInnerClient(), request, this.applicationContext);
        if (completedCallback != null) {
            executionContext.setCompletedCallback(completedCallback);
        }
        executionContext.setProgressCallback(request.getProgressListener());
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask<>(requestMessage, new ResponseParsers.GetObjectResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public OSSAsyncTask<GetObjectACLResult> getObjectACL(GetObjectACLRequest request, OSSCompletedCallback<GetObjectACLRequest, GetObjectACLResult> completedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        Map<String, String> query = new LinkedHashMap<>();
        query.put(RequestParameters.SUBRESOURCE_ACL, "");
        requestMessage.setIsAuthorizationRequired(request.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.GET);
        requestMessage.setParameters(query);
        requestMessage.setBucketName(request.getBucketName());
        requestMessage.setObjectKey(request.getObjectKey());
        canonicalizeRequestMessage(requestMessage, request);
        ExecutionContext<GetObjectACLRequest, GetObjectACLResult> executionContext = new ExecutionContext<>(getInnerClient(), request, this.applicationContext);
        if (completedCallback != null) {
            executionContext.setCompletedCallback(completedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask<>(requestMessage, new ResponseParsers.GetObjectACLResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public OSSAsyncTask<CopyObjectResult> copyObject(CopyObjectRequest request, OSSCompletedCallback<CopyObjectRequest, CopyObjectResult> completedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(request.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.PUT);
        requestMessage.setBucketName(request.getDestinationBucketName());
        requestMessage.setObjectKey(request.getDestinationKey());
        OSSUtils.populateCopyObjectHeaders(request, requestMessage.getHeaders());
        canonicalizeRequestMessage(requestMessage, request);
        ExecutionContext<CopyObjectRequest, CopyObjectResult> executionContext = new ExecutionContext<>(getInnerClient(), request, this.applicationContext);
        if (completedCallback != null) {
            executionContext.setCompletedCallback(completedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask<>(requestMessage, new ResponseParsers.CopyObjectResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public OSSAsyncTask<DeleteObjectResult> deleteObject(DeleteObjectRequest request, OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult> completedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(request.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.DELETE);
        requestMessage.setBucketName(request.getBucketName());
        requestMessage.setObjectKey(request.getObjectKey());
        canonicalizeRequestMessage(requestMessage, request);
        ExecutionContext<DeleteObjectRequest, DeleteObjectResult> executionContext = new ExecutionContext<>(getInnerClient(), request, this.applicationContext);
        if (completedCallback != null) {
            executionContext.setCompletedCallback(completedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask<>(requestMessage, new ResponseParsers.DeleteObjectResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public OSSAsyncTask<DeleteMultipleObjectResult> deleteMultipleObject(DeleteMultipleObjectRequest request, OSSCompletedCallback<DeleteMultipleObjectRequest, DeleteMultipleObjectResult> completedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        Map<String, String> query = new LinkedHashMap<>();
        query.put(RequestParameters.SUBRESOURCE_DELETE, "");
        requestMessage.setIsAuthorizationRequired(request.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.POST);
        requestMessage.setBucketName(request.getBucketName());
        requestMessage.setParameters(query);
        try {
            byte[] bodyBytes = requestMessage.deleteMultipleObjectRequestBodyMarshall(request.getObjectKeys(), request.getQuiet().booleanValue());
            if (bodyBytes != null && bodyBytes.length > 0) {
                requestMessage.getHeaders().put(HttpHeaders.CONTENT_MD5, BinaryUtil.calculateBase64Md5(bodyBytes));
                requestMessage.getHeaders().put("Content-Length", String.valueOf(bodyBytes.length));
            }
            canonicalizeRequestMessage(requestMessage, request);
            ExecutionContext<DeleteMultipleObjectRequest, DeleteMultipleObjectResult> executionContext = new ExecutionContext<>(getInnerClient(), request, this.applicationContext);
            if (completedCallback != null) {
                executionContext.setCompletedCallback(completedCallback);
            }
            return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask<>(requestMessage, new ResponseParsers.DeleteMultipleObjectResponseParser(), executionContext, this.maxRetryCount)), executionContext);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public OSSAsyncTask<ListBucketsResult> listBuckets(ListBucketsRequest request, OSSCompletedCallback<ListBucketsRequest, ListBucketsResult> completedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(request.isAuthorizationRequired());
        requestMessage.setMethod(HttpMethod.GET);
        requestMessage.setService(this.service);
        requestMessage.setEndpoint(this.endpoint);
        canonicalizeRequestMessage(requestMessage, request);
        OSSUtils.populateListBucketRequestParameters(request, requestMessage.getParameters());
        ExecutionContext<ListBucketsRequest, ListBucketsResult> executionContext = new ExecutionContext<>(getInnerClient(), request, this.applicationContext);
        if (completedCallback != null) {
            executionContext.setCompletedCallback(completedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask<>(requestMessage, new ResponseParsers.ListBucketResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public OSSAsyncTask<ListObjectsResult> listObjects(ListObjectsRequest request, OSSCompletedCallback<ListObjectsRequest, ListObjectsResult> completedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(request.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.GET);
        requestMessage.setBucketName(request.getBucketName());
        canonicalizeRequestMessage(requestMessage, request);
        OSSUtils.populateListObjectsRequestParameters(request, requestMessage.getParameters());
        ExecutionContext<ListObjectsRequest, ListObjectsResult> executionContext = new ExecutionContext<>(getInnerClient(), request, this.applicationContext);
        if (completedCallback != null) {
            executionContext.setCompletedCallback(completedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask<>(requestMessage, new ResponseParsers.ListObjectsResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public OSSAsyncTask<InitiateMultipartUploadResult> initMultipartUpload(InitiateMultipartUploadRequest request, OSSCompletedCallback<InitiateMultipartUploadRequest, InitiateMultipartUploadResult> completedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(request.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.POST);
        requestMessage.setBucketName(request.getBucketName());
        requestMessage.setObjectKey(request.getObjectKey());
        requestMessage.getParameters().put(RequestParameters.SUBRESOURCE_UPLOADS, "");
        if (request.isSequential) {
            requestMessage.getParameters().put(RequestParameters.SUBRESOURCE_SEQUENTIAL, "");
        }
        OSSUtils.populateRequestMetadata(requestMessage.getHeaders(), request.getMetadata());
        canonicalizeRequestMessage(requestMessage, request);
        ExecutionContext<InitiateMultipartUploadRequest, InitiateMultipartUploadResult> executionContext = new ExecutionContext<>(getInnerClient(), request, this.applicationContext);
        if (completedCallback != null) {
            executionContext.setCompletedCallback(completedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask<>(requestMessage, new ResponseParsers.InitMultipartResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public UploadPartResult syncUploadPart(UploadPartRequest request) throws ClientException, ServiceException {
        UploadPartResult result = uploadPart(request, (OSSCompletedCallback<UploadPartRequest, UploadPartResult>) null).getResult();
        checkCRC64(request, result);
        return result;
    }

    public OSSAsyncTask<UploadPartResult> uploadPart(UploadPartRequest request, final OSSCompletedCallback<UploadPartRequest, UploadPartResult> completedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(request.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.PUT);
        requestMessage.setBucketName(request.getBucketName());
        requestMessage.setObjectKey(request.getObjectKey());
        requestMessage.getParameters().put(RequestParameters.UPLOAD_ID, request.getUploadId());
        requestMessage.getParameters().put(RequestParameters.PART_NUMBER, String.valueOf(request.getPartNumber()));
        requestMessage.setUploadData(request.getPartContent());
        if (request.getMd5Digest() != null) {
            requestMessage.getHeaders().put(HttpHeaders.CONTENT_MD5, request.getMd5Digest());
        }
        canonicalizeRequestMessage(requestMessage, request);
        ExecutionContext<UploadPartRequest, UploadPartResult> executionContext = new ExecutionContext<>(getInnerClient(), request, this.applicationContext);
        if (completedCallback != null) {
            executionContext.setCompletedCallback(new OSSCompletedCallback<UploadPartRequest, UploadPartResult>() {
                public void onSuccess(UploadPartRequest request, UploadPartResult result) {
                    InternalRequestOperation.this.checkCRC64(request, result, completedCallback);
                }

                public void onFailure(UploadPartRequest request, ClientException clientException, ServiceException serviceException) {
                    completedCallback.onFailure(request, clientException, serviceException);
                }
            });
        }
        executionContext.setProgressCallback(request.getProgressCallback());
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask<>(requestMessage, new ResponseParsers.UploadPartResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public CompleteMultipartUploadResult syncCompleteMultipartUpload(CompleteMultipartUploadRequest request) throws ClientException, ServiceException {
        CompleteMultipartUploadResult result = completeMultipartUpload(request, (OSSCompletedCallback<CompleteMultipartUploadRequest, CompleteMultipartUploadResult>) null).getResult();
        if (result.getServerCRC() != null) {
            result.setClientCRC(Long.valueOf(calcObjectCRCFromParts(request.getPartETags())));
        }
        checkCRC64(request, result);
        return result;
    }

    public OSSAsyncTask<CompleteMultipartUploadResult> completeMultipartUpload(CompleteMultipartUploadRequest request, final OSSCompletedCallback<CompleteMultipartUploadRequest, CompleteMultipartUploadResult> completedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(request.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.POST);
        requestMessage.setBucketName(request.getBucketName());
        requestMessage.setObjectKey(request.getObjectKey());
        requestMessage.setStringBody(OSSUtils.buildXMLFromPartEtagList(request.getPartETags()));
        requestMessage.getParameters().put(RequestParameters.UPLOAD_ID, request.getUploadId());
        if (request.getCallbackParam() != null) {
            requestMessage.getHeaders().put("x-oss-callback", OSSUtils.populateMapToBase64JsonString(request.getCallbackParam()));
        }
        if (request.getCallbackVars() != null) {
            requestMessage.getHeaders().put("x-oss-callback-var", OSSUtils.populateMapToBase64JsonString(request.getCallbackVars()));
        }
        OSSUtils.populateRequestMetadata(requestMessage.getHeaders(), request.getMetadata());
        canonicalizeRequestMessage(requestMessage, request);
        ExecutionContext<CompleteMultipartUploadRequest, CompleteMultipartUploadResult> executionContext = new ExecutionContext<>(getInnerClient(), request, this.applicationContext);
        if (completedCallback != null) {
            executionContext.setCompletedCallback(new OSSCompletedCallback<CompleteMultipartUploadRequest, CompleteMultipartUploadResult>() {
                public void onSuccess(CompleteMultipartUploadRequest request, CompleteMultipartUploadResult result) {
                    if (result.getServerCRC() != null) {
                        result.setClientCRC(Long.valueOf(InternalRequestOperation.this.calcObjectCRCFromParts(request.getPartETags())));
                    }
                    InternalRequestOperation.this.checkCRC64(request, result, completedCallback);
                }

                public void onFailure(CompleteMultipartUploadRequest request, ClientException clientException, ServiceException serviceException) {
                    completedCallback.onFailure(request, clientException, serviceException);
                }
            });
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask<>(requestMessage, new ResponseParsers.CompleteMultipartUploadResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public OSSAsyncTask<AbortMultipartUploadResult> abortMultipartUpload(AbortMultipartUploadRequest request, OSSCompletedCallback<AbortMultipartUploadRequest, AbortMultipartUploadResult> completedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(request.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.DELETE);
        requestMessage.setBucketName(request.getBucketName());
        requestMessage.setObjectKey(request.getObjectKey());
        requestMessage.getParameters().put(RequestParameters.UPLOAD_ID, request.getUploadId());
        canonicalizeRequestMessage(requestMessage, request);
        ExecutionContext<AbortMultipartUploadRequest, AbortMultipartUploadResult> executionContext = new ExecutionContext<>(getInnerClient(), request, this.applicationContext);
        if (completedCallback != null) {
            executionContext.setCompletedCallback(completedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask<>(requestMessage, new ResponseParsers.AbortMultipartUploadResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public OSSAsyncTask<ListPartsResult> listParts(ListPartsRequest request, OSSCompletedCallback<ListPartsRequest, ListPartsResult> completedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(request.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.GET);
        requestMessage.setBucketName(request.getBucketName());
        requestMessage.setObjectKey(request.getObjectKey());
        requestMessage.getParameters().put(RequestParameters.UPLOAD_ID, request.getUploadId());
        Integer maxParts = request.getMaxParts();
        if (maxParts != null) {
            if (!OSSUtils.checkParamRange((long) maxParts.intValue(), 0, true, 1000, true)) {
                throw new IllegalArgumentException("MaxPartsOutOfRange: 1000");
            }
            requestMessage.getParameters().put(RequestParameters.MAX_PARTS, maxParts.toString());
        }
        Integer partNumberMarker = request.getPartNumberMarker();
        if (partNumberMarker != null) {
            if (!OSSUtils.checkParamRange((long) partNumberMarker.intValue(), 0, false, 10000, true)) {
                throw new IllegalArgumentException("PartNumberMarkerOutOfRange: 10000");
            }
            requestMessage.getParameters().put(RequestParameters.PART_NUMBER_MARKER, partNumberMarker.toString());
        }
        canonicalizeRequestMessage(requestMessage, request);
        ExecutionContext<ListPartsRequest, ListPartsResult> executionContext = new ExecutionContext<>(getInnerClient(), request, this.applicationContext);
        if (completedCallback != null) {
            executionContext.setCompletedCallback(completedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask<>(requestMessage, new ResponseParsers.ListPartsResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public OSSAsyncTask<ListMultipartUploadsResult> listMultipartUploads(ListMultipartUploadsRequest request, OSSCompletedCallback<ListMultipartUploadsRequest, ListMultipartUploadsResult> completedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setIsAuthorizationRequired(request.isAuthorizationRequired());
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.GET);
        requestMessage.setBucketName(request.getBucketName());
        requestMessage.getParameters().put(RequestParameters.SUBRESOURCE_UPLOADS, "");
        OSSUtils.populateListMultipartUploadsRequestParameters(request, requestMessage.getParameters());
        canonicalizeRequestMessage(requestMessage, request);
        ExecutionContext<ListMultipartUploadsRequest, ListMultipartUploadsResult> executionContext = new ExecutionContext<>(getInnerClient(), request, this.applicationContext);
        if (completedCallback != null) {
            executionContext.setCompletedCallback(completedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask<>(requestMessage, new ResponseParsers.ListMultipartUploadsResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    private boolean checkIfHttpDnsAvailable(boolean httpDnsEnable) {
        boolean IS_ICS_OR_LATER;
        String proxyHost;
        if (!httpDnsEnable || this.applicationContext == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 14) {
            IS_ICS_OR_LATER = true;
        } else {
            IS_ICS_OR_LATER = false;
        }
        if (IS_ICS_OR_LATER) {
            proxyHost = System.getProperty("http.proxyHost");
        } else {
            proxyHost = android.net.Proxy.getHost(this.applicationContext);
        }
        String confProxyHost = this.conf.getProxyHost();
        if (!TextUtils.isEmpty(confProxyHost)) {
            proxyHost = confProxyHost;
        }
        return TextUtils.isEmpty(proxyHost);
    }

    public OkHttpClient getInnerClient() {
        return this.innerClient;
    }

    private void canonicalizeRequestMessage(RequestMessage message, OSSRequest request) {
        boolean checkCRC64 = false;
        Map<String, String> header = message.getHeaders();
        if (header.get("Date") == null) {
            header.put("Date", DateUtil.currentFixedSkewedTimeInRFC822Format());
        }
        if ((message.getMethod() == HttpMethod.POST || message.getMethod() == HttpMethod.PUT) && OSSUtils.isEmptyString(header.get("Content-Type"))) {
            header.put("Content-Type", OSSUtils.determineContentType((String) null, message.getUploadFilePath(), message.getObjectKey()));
        }
        message.setHttpDnsEnable(checkIfHttpDnsAvailable(this.conf.isHttpDnsEnable()));
        message.setCredentialProvider(this.credentialProvider);
        message.getHeaders().put(HttpHeaders.USER_AGENT, VersionInfoUtils.getUserAgent(this.conf.getCustomUserMark()));
        if (message.getHeaders().containsKey(HttpHeaders.RANGE) || message.getParameters().containsKey(RequestParameters.X_OSS_PROCESS)) {
            message.setCheckCRC64(false);
        }
        message.setIsInCustomCnameExcludeList(OSSUtils.isInCustomCnameExcludeList(this.endpoint.getHost(), this.conf.getCustomCnameExcludeList()));
        if (request.getCRC64() == OSSRequest.CRC64Config.NULL) {
            checkCRC64 = this.conf.isCheckCRC64();
        } else if (request.getCRC64() == OSSRequest.CRC64Config.YES) {
            checkCRC64 = true;
        }
        message.setCheckCRC64(checkCRC64);
        request.setCRC64(checkCRC64 ? OSSRequest.CRC64Config.YES : OSSRequest.CRC64Config.NO);
    }

    public void setCredentialProvider(OSSCredentialProvider credentialProvider2) {
        this.credentialProvider = credentialProvider2;
    }

    private <Request extends OSSRequest, Result extends OSSResult> void checkCRC64(Request request, Result result) throws ClientException {
        if (request.getCRC64() == OSSRequest.CRC64Config.YES) {
            try {
                OSSUtils.checkChecksum(result.getClientCRC(), result.getServerCRC(), result.getRequestId());
            } catch (InconsistentException e) {
                throw new ClientException(e.getMessage(), e);
            }
        }
    }

    /* access modifiers changed from: private */
    public <Request extends OSSRequest, Result extends OSSResult> void checkCRC64(Request request, Result result, OSSCompletedCallback<Request, Result> completedCallback) {
        try {
            checkCRC64(request, result);
            if (completedCallback != null) {
                completedCallback.onSuccess(request, result);
            }
        } catch (ClientException e) {
            if (completedCallback != null) {
                completedCallback.onFailure(request, e, (ServiceException) null);
            }
        }
    }

    /* access modifiers changed from: private */
    public long calcObjectCRCFromParts(List<PartETag> partETags) {
        long crc = 0;
        for (PartETag partETag : partETags) {
            if (partETag.getCRC64() == 0 || partETag.getPartSize() <= 0) {
                return 0;
            }
            crc = CRC64.combine(crc, partETag.getCRC64(), partETag.getPartSize());
        }
        return crc;
    }

    public Context getApplicationContext() {
        return this.applicationContext;
    }

    public ClientConfiguration getConf() {
        return this.conf;
    }

    public OSSAsyncTask<TriggerCallbackResult> triggerCallback(TriggerCallbackRequest request, OSSCompletedCallback<TriggerCallbackRequest, TriggerCallbackResult> completedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        Map<String, String> query = new LinkedHashMap<>();
        query.put(RequestParameters.X_OSS_PROCESS, "");
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.POST);
        requestMessage.setBucketName(request.getBucketName());
        requestMessage.setObjectKey(request.getObjectKey());
        requestMessage.setParameters(query);
        String bodyString = OSSUtils.buildTriggerCallbackBody(request.getCallbackParam(), request.getCallbackVars());
        requestMessage.setStringBody(bodyString);
        requestMessage.getHeaders().put(HttpHeaders.CONTENT_MD5, BinaryUtil.calculateBase64Md5(bodyString.getBytes()));
        canonicalizeRequestMessage(requestMessage, request);
        ExecutionContext<TriggerCallbackRequest, TriggerCallbackResult> executionContext = new ExecutionContext<>(getInnerClient(), request, this.applicationContext);
        if (completedCallback != null) {
            executionContext.setCompletedCallback(completedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask<>(requestMessage, new ResponseParsers.TriggerCallbackResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public TriggerCallbackResult asyncTriggerCallback(TriggerCallbackRequest request) throws ClientException, ServiceException {
        return triggerCallback(request, (OSSCompletedCallback<TriggerCallbackRequest, TriggerCallbackResult>) null).getResult();
    }

    public OSSAsyncTask<ImagePersistResult> imageActionPersist(ImagePersistRequest request, OSSCompletedCallback<ImagePersistRequest, ImagePersistResult> completedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        Map<String, String> query = new LinkedHashMap<>();
        query.put(RequestParameters.X_OSS_PROCESS, "");
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.POST);
        requestMessage.setBucketName(request.mFromBucket);
        requestMessage.setObjectKey(request.mFromObjectkey);
        requestMessage.setParameters(query);
        requestMessage.setStringBody(OSSUtils.buildImagePersistentBody(request.mToBucketName, request.mToObjectKey, request.mAction));
        canonicalizeRequestMessage(requestMessage, request);
        ExecutionContext<ImagePersistRequest, ImagePersistResult> executionContext = new ExecutionContext<>(getInnerClient(), request, this.applicationContext);
        if (completedCallback != null) {
            executionContext.setCompletedCallback(completedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask<>(requestMessage, new ResponseParsers.ImagePersistResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public PutSymlinkResult syncPutSymlink(PutSymlinkRequest request) throws ClientException, ServiceException {
        return putSymlink(request, (OSSCompletedCallback<PutSymlinkRequest, PutSymlinkResult>) null).getResult();
    }

    public OSSAsyncTask<PutSymlinkResult> putSymlink(PutSymlinkRequest request, OSSCompletedCallback<PutSymlinkRequest, PutSymlinkResult> completedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        Map<String, String> query = new LinkedHashMap<>();
        query.put(RequestParameters.X_OSS_SYMLINK, "");
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.PUT);
        requestMessage.setBucketName(request.getBucketName());
        requestMessage.setObjectKey(request.getObjectKey());
        requestMessage.setParameters(query);
        if (!OSSUtils.isEmptyString(request.getTargetObjectName())) {
            requestMessage.getHeaders().put(OSSHeaders.OSS_HEADER_SYMLINK_TARGET, HttpUtil.urlEncode(request.getTargetObjectName(), "utf-8"));
        }
        OSSUtils.populateRequestMetadata(requestMessage.getHeaders(), request.getMetadata());
        canonicalizeRequestMessage(requestMessage, request);
        ExecutionContext<PutSymlinkRequest, PutSymlinkResult> executionContext = new ExecutionContext<>(getInnerClient(), request, this.applicationContext);
        if (completedCallback != null) {
            executionContext.setCompletedCallback(completedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask<>(requestMessage, new ResponseParsers.PutSymlinkResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public GetSymlinkResult syncGetSymlink(GetSymlinkRequest request) throws ClientException, ServiceException {
        return getSymlink(request, (OSSCompletedCallback<GetSymlinkRequest, GetSymlinkResult>) null).getResult();
    }

    public OSSAsyncTask<GetSymlinkResult> getSymlink(GetSymlinkRequest request, OSSCompletedCallback<GetSymlinkRequest, GetSymlinkResult> completedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        Map<String, String> query = new LinkedHashMap<>();
        query.put(RequestParameters.X_OSS_SYMLINK, "");
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.GET);
        requestMessage.setBucketName(request.getBucketName());
        requestMessage.setObjectKey(request.getObjectKey());
        requestMessage.setParameters(query);
        canonicalizeRequestMessage(requestMessage, request);
        ExecutionContext<GetSymlinkRequest, GetSymlinkResult> executionContext = new ExecutionContext<>(getInnerClient(), request, this.applicationContext);
        if (completedCallback != null) {
            executionContext.setCompletedCallback(completedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask<>(requestMessage, new ResponseParsers.GetSymlinkResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }

    public RestoreObjectResult syncRestoreObject(RestoreObjectRequest request) throws ClientException, ServiceException {
        return restoreObject(request, (OSSCompletedCallback<RestoreObjectRequest, RestoreObjectResult>) null).getResult();
    }

    public OSSAsyncTask<RestoreObjectResult> restoreObject(RestoreObjectRequest request, OSSCompletedCallback<RestoreObjectRequest, RestoreObjectResult> completedCallback) {
        RequestMessage requestMessage = new RequestMessage();
        Map<String, String> query = new LinkedHashMap<>();
        query.put(RequestParameters.X_OSS_RESTORE, "");
        requestMessage.setEndpoint(this.endpoint);
        requestMessage.setMethod(HttpMethod.POST);
        requestMessage.setBucketName(request.getBucketName());
        requestMessage.setObjectKey(request.getObjectKey());
        requestMessage.setParameters(query);
        canonicalizeRequestMessage(requestMessage, request);
        ExecutionContext<RestoreObjectRequest, RestoreObjectResult> executionContext = new ExecutionContext<>(getInnerClient(), request, this.applicationContext);
        if (completedCallback != null) {
            executionContext.setCompletedCallback(completedCallback);
        }
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new OSSRequestTask<>(requestMessage, new ResponseParsers.RestoreObjectResponseParser(), executionContext, this.maxRetryCount)), executionContext);
    }
}
