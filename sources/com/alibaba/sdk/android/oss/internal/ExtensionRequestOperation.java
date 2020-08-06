package com.alibaba.sdk.android.oss.internal;

import android.os.Environment;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.OSSConstants;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.utils.BinaryUtil;
import com.alibaba.sdk.android.oss.common.utils.OSSUtils;
import com.alibaba.sdk.android.oss.model.AbortMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.AbortMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.HeadObjectRequest;
import com.alibaba.sdk.android.oss.model.HeadObjectResult;
import com.alibaba.sdk.android.oss.model.MultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.ResumableUploadRequest;
import com.alibaba.sdk.android.oss.model.ResumableUploadResult;
import com.alibaba.sdk.android.oss.network.ExecutionContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ExtensionRequestOperation {
    private static ExecutorService executorService = Executors.newFixedThreadPool(5, new ThreadFactory() {
        public Thread newThread(Runnable r) {
            return new Thread(r, "oss-android-extensionapi-thread");
        }
    });
    private InternalRequestOperation apiOperation;

    public ExtensionRequestOperation(InternalRequestOperation apiOperation2) {
        this.apiOperation = apiOperation2;
    }

    public boolean doesObjectExist(String bucketName, String objectKey) throws ClientException, ServiceException {
        try {
            this.apiOperation.headObject(new HeadObjectRequest(bucketName, objectKey), (OSSCompletedCallback<HeadObjectRequest, HeadObjectResult>) null).getResult();
            return true;
        } catch (ServiceException e) {
            if (e.getStatusCode() == 404) {
                return false;
            }
            throw e;
        }
    }

    public void abortResumableUpload(ResumableUploadRequest request) throws IOException {
        setCRC64(request);
        String uploadFilePath = request.getUploadFilePath();
        if (!OSSUtils.isEmptyString(request.getRecordDirectory())) {
            File recordFile = new File(request.getRecordDirectory() + WVNativeCallbackUtil.SEPERATER + BinaryUtil.calculateMd5Str((BinaryUtil.calculateMd5Str(uploadFilePath) + request.getBucketName() + request.getObjectKey() + String.valueOf(request.getPartSize())).getBytes()));
            if (recordFile.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(recordFile));
                String uploadId = br.readLine();
                br.close();
                OSSLog.logDebug("[initUploadId] - Found record file, uploadid: " + uploadId);
                if (request.getCRC64() == OSSRequest.CRC64Config.YES) {
                    File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + OSSConstants.RESOURCE_NAME_OSS + File.separator + uploadId);
                    if (file.exists()) {
                        file.delete();
                    }
                }
                this.apiOperation.abortMultipartUpload(new AbortMultipartUploadRequest(request.getBucketName(), request.getObjectKey(), uploadId), (OSSCompletedCallback<AbortMultipartUploadRequest, AbortMultipartUploadResult>) null);
            }
            if (recordFile != null) {
                recordFile.delete();
            }
        }
    }

    public OSSAsyncTask<ResumableUploadResult> resumableUpload(ResumableUploadRequest request, OSSCompletedCallback<ResumableUploadRequest, ResumableUploadResult> completedCallback) {
        setCRC64(request);
        ExecutionContext<ResumableUploadRequest, ResumableUploadResult> executionContext = new ExecutionContext<>(this.apiOperation.getInnerClient(), request, this.apiOperation.getApplicationContext());
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new ResumableUploadTask(request, completedCallback, executionContext, this.apiOperation)), executionContext);
    }

    public OSSAsyncTask<ResumableUploadResult> sequenceUpload(ResumableUploadRequest request, OSSCompletedCallback<ResumableUploadRequest, ResumableUploadResult> completedCallback) {
        setCRC64(request);
        ExecutionContext<ResumableUploadRequest, ResumableUploadResult> executionContext = new ExecutionContext<>(this.apiOperation.getInnerClient(), request, this.apiOperation.getApplicationContext());
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new SequenceUploadTask(request, completedCallback, executionContext, this.apiOperation)), executionContext);
    }

    public OSSAsyncTask<CompleteMultipartUploadResult> multipartUpload(MultipartUploadRequest request, OSSCompletedCallback<MultipartUploadRequest, CompleteMultipartUploadResult> completedCallback) {
        setCRC64(request);
        ExecutionContext<MultipartUploadRequest, CompleteMultipartUploadResult> executionContext = new ExecutionContext<>(this.apiOperation.getInnerClient(), request, this.apiOperation.getApplicationContext());
        return OSSAsyncTask.wrapRequestTask(executorService.submit(new MultipartUploadTask(this.apiOperation, request, completedCallback, executionContext)), executionContext);
    }

    private void setCRC64(OSSRequest request) {
        Enum crc64;
        if (request.getCRC64() != OSSRequest.CRC64Config.NULL) {
            crc64 = request.getCRC64();
        } else {
            crc64 = this.apiOperation.getConf().isCheckCRC64() ? OSSRequest.CRC64Config.YES : OSSRequest.CRC64Config.NO;
        }
        request.setCRC64(crc64);
    }
}
