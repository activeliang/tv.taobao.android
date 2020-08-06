package com.alibaba.sdk.android.oss.internal;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.AbortMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.AbortMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.InitiateMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.InitiateMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.MultipartUploadRequest;
import com.alibaba.sdk.android.oss.network.ExecutionContext;
import java.io.IOException;
import java.util.concurrent.Callable;

public class MultipartUploadTask extends BaseMultipartUploadTask<MultipartUploadRequest, CompleteMultipartUploadResult> implements Callable<CompleteMultipartUploadResult> {
    public MultipartUploadTask(InternalRequestOperation operation, MultipartUploadRequest request, OSSCompletedCallback<MultipartUploadRequest, CompleteMultipartUploadResult> completedCallback, ExecutionContext context) {
        super(operation, request, completedCallback, context);
    }

    /* access modifiers changed from: protected */
    public void initMultipartUploadId() throws ClientException, ServiceException {
        this.mUploadId = this.mApiOperation.initMultipartUpload(new InitiateMultipartUploadRequest(this.mRequest.getBucketName(), this.mRequest.getObjectKey(), this.mRequest.getMetadata()), (OSSCompletedCallback<InitiateMultipartUploadRequest, InitiateMultipartUploadResult>) null).getResult().getUploadId();
        this.mRequest.setUploadId(this.mUploadId);
    }

    /* access modifiers changed from: protected */
    public CompleteMultipartUploadResult doMultipartUpload() throws IOException, ServiceException, ClientException, InterruptedException {
        checkCancel();
        int readByte = this.mPartAttr[0];
        final int partNumber = this.mPartAttr[1];
        int currentLength = 0;
        for (int i = 0; i < partNumber; i++) {
            checkException();
            if (this.mPoolExecutor != null) {
                if (i == partNumber - 1) {
                    readByte = (int) (this.mFileLength - ((long) currentLength));
                }
                final int byteCount = readByte;
                final int readIndex = i;
                currentLength += byteCount;
                this.mPoolExecutor.execute(new Runnable() {
                    public void run() {
                        MultipartUploadTask.this.uploadPart(readIndex, byteCount, partNumber);
                    }
                });
            }
        }
        if (checkWaitCondition(partNumber)) {
            synchronized (this.mLock) {
                this.mLock.wait();
            }
        }
        if (this.mUploadException != null) {
            abortThisUpload();
        }
        checkException();
        CompleteMultipartUploadResult completeResult = completeMultipartUploadResult();
        releasePool();
        return completeResult;
    }

    /* access modifiers changed from: protected */
    public void abortThisUpload() {
        if (this.mUploadId != null) {
            this.mApiOperation.abortMultipartUpload(new AbortMultipartUploadRequest(this.mRequest.getBucketName(), this.mRequest.getObjectKey(), this.mUploadId), (OSSCompletedCallback<AbortMultipartUploadRequest, AbortMultipartUploadResult>) null).waitUntilFinished();
        }
    }

    /* access modifiers changed from: protected */
    public void processException(Exception e) {
        synchronized (this.mLock) {
            this.mPartExceptionCount++;
            if (this.mUploadException == null) {
                this.mUploadException = e;
                this.mLock.notify();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void preUploadPart(int readIndex, int byteCount, int partNumber) throws Exception {
        checkException();
    }
}
