package com.alibaba.sdk.android.oss.internal;

import android.text.TextUtils;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.utils.OSSSharedPreferences;
import com.alibaba.sdk.android.oss.model.AbortMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.AbortMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.PartETag;
import com.alibaba.sdk.android.oss.model.ResumableUploadRequest;
import com.alibaba.sdk.android.oss.model.ResumableUploadResult;
import com.alibaba.sdk.android.oss.network.ExecutionContext;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SequenceUploadTask extends BaseMultipartUploadTask<ResumableUploadRequest, ResumableUploadResult> implements Callable<ResumableUploadResult> {
    private List<Integer> mAlreadyUploadIndex = new ArrayList();
    private File mCRC64RecordFile;
    private long mFirstPartSize;
    private File mRecordFile;
    private OSSSharedPreferences mSp = OSSSharedPreferences.instance(this.mContext.getApplicationContext());

    public SequenceUploadTask(ResumableUploadRequest request, OSSCompletedCallback<ResumableUploadRequest, ResumableUploadResult> completedCallback, ExecutionContext context, InternalRequestOperation apiOperation) {
        super(apiOperation, request, completedCallback, context);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r26v91, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v96, resolved type: java.util.Map} */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void initMultipartUploadId() throws java.io.IOException, com.alibaba.sdk.android.oss.ClientException, com.alibaba.sdk.android.oss.ServiceException {
        /*
            r30 = this;
            r21 = 0
            r0 = r30
            com.alibaba.sdk.android.oss.model.MultipartUploadRequest r0 = r0.mRequest
            r26 = r0
            com.alibaba.sdk.android.oss.model.ResumableUploadRequest r26 = (com.alibaba.sdk.android.oss.model.ResumableUploadRequest) r26
            java.lang.String r26 = r26.getRecordDirectory()
            boolean r26 = com.alibaba.sdk.android.oss.common.utils.OSSUtils.isEmptyString(r26)
            if (r26 != 0) goto L_0x02fc
            r0 = r30
            java.lang.String r0 = r0.mUploadFilePath
            r26 = r0
            java.lang.String r8 = com.alibaba.sdk.android.oss.common.utils.BinaryUtil.calculateMd5Str((java.lang.String) r26)
            java.lang.StringBuilder r26 = new java.lang.StringBuilder
            r26.<init>()
            r0 = r26
            java.lang.StringBuilder r27 = r0.append(r8)
            r0 = r30
            com.alibaba.sdk.android.oss.model.MultipartUploadRequest r0 = r0.mRequest
            r26 = r0
            com.alibaba.sdk.android.oss.model.ResumableUploadRequest r26 = (com.alibaba.sdk.android.oss.model.ResumableUploadRequest) r26
            java.lang.String r26 = r26.getBucketName()
            r0 = r27
            r1 = r26
            java.lang.StringBuilder r27 = r0.append(r1)
            r0 = r30
            com.alibaba.sdk.android.oss.model.MultipartUploadRequest r0 = r0.mRequest
            r26 = r0
            com.alibaba.sdk.android.oss.model.ResumableUploadRequest r26 = (com.alibaba.sdk.android.oss.model.ResumableUploadRequest) r26
            java.lang.String r26 = r26.getObjectKey()
            r0 = r27
            r1 = r26
            java.lang.StringBuilder r27 = r0.append(r1)
            r0 = r30
            com.alibaba.sdk.android.oss.model.MultipartUploadRequest r0 = r0.mRequest
            r26 = r0
            com.alibaba.sdk.android.oss.model.ResumableUploadRequest r26 = (com.alibaba.sdk.android.oss.model.ResumableUploadRequest) r26
            long r28 = r26.getPartSize()
            java.lang.String r26 = java.lang.String.valueOf(r28)
            r0 = r27
            r1 = r26
            java.lang.StringBuilder r27 = r0.append(r1)
            r0 = r30
            boolean r0 = r0.mCheckCRC64
            r26 = r0
            if (r26 == 0) goto L_0x027d
            java.lang.String r26 = "-crc64"
        L_0x0074:
            r0 = r27
            r1 = r26
            java.lang.StringBuilder r26 = r0.append(r1)
            java.lang.String r27 = "-sequence"
            java.lang.StringBuilder r26 = r26.append(r27)
            java.lang.String r26 = r26.toString()
            byte[] r26 = r26.getBytes()
            java.lang.String r22 = com.alibaba.sdk.android.oss.common.utils.BinaryUtil.calculateMd5Str((byte[]) r26)
            java.lang.StringBuilder r27 = new java.lang.StringBuilder
            r27.<init>()
            r0 = r30
            com.alibaba.sdk.android.oss.model.MultipartUploadRequest r0 = r0.mRequest
            r26 = r0
            com.alibaba.sdk.android.oss.model.ResumableUploadRequest r26 = (com.alibaba.sdk.android.oss.model.ResumableUploadRequest) r26
            java.lang.String r26 = r26.getRecordDirectory()
            r0 = r27
            r1 = r26
            java.lang.StringBuilder r26 = r0.append(r1)
            java.lang.String r27 = java.io.File.separator
            java.lang.StringBuilder r26 = r26.append(r27)
            r0 = r26
            r1 = r22
            java.lang.StringBuilder r26 = r0.append(r1)
            java.lang.String r23 = r26.toString()
            java.io.File r26 = new java.io.File
            r0 = r26
            r1 = r23
            r0.<init>(r1)
            r0 = r26
            r1 = r30
            r1.mRecordFile = r0
            r0 = r30
            java.io.File r0 = r0.mRecordFile
            r26 = r0
            boolean r26 = r26.exists()
            if (r26 == 0) goto L_0x0111
            java.io.BufferedReader r4 = new java.io.BufferedReader
            java.io.FileReader r26 = new java.io.FileReader
            r0 = r30
            java.io.File r0 = r0.mRecordFile
            r27 = r0
            r26.<init>(r27)
            r0 = r26
            r4.<init>(r0)
            java.lang.String r26 = r4.readLine()
            r0 = r26
            r1 = r30
            r1.mUploadId = r0
            r4.close()
            java.lang.StringBuilder r26 = new java.lang.StringBuilder
            r26.<init>()
            java.lang.String r27 = "sequence [initUploadId] - Found record file, uploadid: "
            java.lang.StringBuilder r26 = r26.append(r27)
            r0 = r30
            java.lang.String r0 = r0.mUploadId
            r27 = r0
            java.lang.StringBuilder r26 = r26.append(r27)
            java.lang.String r26 = r26.toString()
            com.alibaba.sdk.android.oss.common.OSSLog.logDebug(r26)
        L_0x0111:
            r0 = r30
            java.lang.String r0 = r0.mUploadId
            r26 = r0
            boolean r26 = com.alibaba.sdk.android.oss.common.utils.OSSUtils.isEmptyString(r26)
            if (r26 != 0) goto L_0x02b5
            r0 = r30
            boolean r0 = r0.mCheckCRC64
            r26 = r0
            if (r26 == 0) goto L_0x017e
            java.lang.StringBuilder r27 = new java.lang.StringBuilder
            r27.<init>()
            r0 = r30
            com.alibaba.sdk.android.oss.model.MultipartUploadRequest r0 = r0.mRequest
            r26 = r0
            com.alibaba.sdk.android.oss.model.ResumableUploadRequest r26 = (com.alibaba.sdk.android.oss.model.ResumableUploadRequest) r26
            java.lang.String r26 = r26.getRecordDirectory()
            r0 = r27
            r1 = r26
            java.lang.StringBuilder r26 = r0.append(r1)
            java.lang.String r27 = java.io.File.separator
            java.lang.StringBuilder r26 = r26.append(r27)
            r0 = r30
            java.lang.String r0 = r0.mUploadId
            r27 = r0
            java.lang.StringBuilder r26 = r26.append(r27)
            java.lang.String r9 = r26.toString()
            java.io.File r6 = new java.io.File
            r6.<init>(r9)
            boolean r26 = r6.exists()
            if (r26 == 0) goto L_0x017e
            java.io.FileInputStream r10 = new java.io.FileInputStream
            r10.<init>(r6)
            java.io.ObjectInputStream r17 = new java.io.ObjectInputStream
            r0 = r17
            r0.<init>(r10)
            java.lang.Object r26 = r17.readObject()     // Catch:{ ClassNotFoundException -> 0x0282 }
            r0 = r26
            java.util.Map r0 = (java.util.Map) r0     // Catch:{ ClassNotFoundException -> 0x0282 }
            r21 = r0
            r6.delete()     // Catch:{ ClassNotFoundException -> 0x0282 }
            if (r17 == 0) goto L_0x017b
            r17.close()
        L_0x017b:
            r6.delete()
        L_0x017e:
            r14 = 0
            r16 = 0
        L_0x0181:
            com.alibaba.sdk.android.oss.model.ListPartsRequest r15 = new com.alibaba.sdk.android.oss.model.ListPartsRequest
            r0 = r30
            com.alibaba.sdk.android.oss.model.MultipartUploadRequest r0 = r0.mRequest
            r26 = r0
            com.alibaba.sdk.android.oss.model.ResumableUploadRequest r26 = (com.alibaba.sdk.android.oss.model.ResumableUploadRequest) r26
            java.lang.String r27 = r26.getBucketName()
            r0 = r30
            com.alibaba.sdk.android.oss.model.MultipartUploadRequest r0 = r0.mRequest
            r26 = r0
            com.alibaba.sdk.android.oss.model.ResumableUploadRequest r26 = (com.alibaba.sdk.android.oss.model.ResumableUploadRequest) r26
            java.lang.String r26 = r26.getObjectKey()
            r0 = r30
            java.lang.String r0 = r0.mUploadId
            r28 = r0
            r0 = r27
            r1 = r26
            r2 = r28
            r15.<init>(r0, r1, r2)
            if (r16 <= 0) goto L_0x01b5
            java.lang.Integer r26 = java.lang.Integer.valueOf(r16)
            r0 = r26
            r15.setPartNumberMarker(r0)
        L_0x01b5:
            r0 = r30
            com.alibaba.sdk.android.oss.internal.InternalRequestOperation r0 = r0.mApiOperation
            r26 = r0
            r27 = 0
            r0 = r26
            r1 = r27
            com.alibaba.sdk.android.oss.internal.OSSAsyncTask r25 = r0.listParts(r15, r1)
            com.alibaba.sdk.android.oss.model.OSSResult r24 = r25.getResult()     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            com.alibaba.sdk.android.oss.model.ListPartsResult r24 = (com.alibaba.sdk.android.oss.model.ListPartsResult) r24     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            boolean r14 = r24.isTruncated()     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            int r16 = r24.getNextPartNumberMarker()     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            java.util.List r20 = r24.getParts()     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            r11 = 0
        L_0x01d8:
            int r26 = r20.size()     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            r0 = r26
            if (r11 >= r0) goto L_0x02b0
            r0 = r20
            java.lang.Object r18 = r0.get(r11)     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            com.alibaba.sdk.android.oss.model.PartSummary r18 = (com.alibaba.sdk.android.oss.model.PartSummary) r18     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            com.alibaba.sdk.android.oss.model.PartETag r19 = new com.alibaba.sdk.android.oss.model.PartETag     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            int r26 = r18.getPartNumber()     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            java.lang.String r27 = r18.getETag()     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            r0 = r19
            r1 = r26
            r2 = r27
            r0.<init>(r1, r2)     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            long r26 = r18.getSize()     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            r0 = r19
            r1 = r26
            r0.setPartSize(r1)     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            if (r21 == 0) goto L_0x023d
            int r26 = r21.size()     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            if (r26 <= 0) goto L_0x023d
            int r26 = r19.getPartNumber()     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            java.lang.Integer r26 = java.lang.Integer.valueOf(r26)     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            r0 = r21
            r1 = r26
            boolean r26 = r0.containsKey(r1)     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            if (r26 == 0) goto L_0x023d
            int r26 = r19.getPartNumber()     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            java.lang.Integer r26 = java.lang.Integer.valueOf(r26)     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            r0 = r21
            r1 = r26
            java.lang.Object r26 = r0.get(r1)     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            java.lang.Long r26 = (java.lang.Long) r26     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            long r26 = r26.longValue()     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            r0 = r19
            r1 = r26
            r0.setCRC64(r1)     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
        L_0x023d:
            r0 = r30
            java.util.List r0 = r0.mPartETags     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            r26 = r0
            r0 = r26
            r1 = r19
            r0.add(r1)     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            r0 = r30
            long r0 = r0.mUploadedLength     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            r26 = r0
            long r28 = r18.getSize()     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            long r26 = r26 + r28
            r0 = r26
            r2 = r30
            r2.mUploadedLength = r0     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            r0 = r30
            java.util.List<java.lang.Integer> r0 = r0.mAlreadyUploadIndex     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            r26 = r0
            int r27 = r18.getPartNumber()     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            java.lang.Integer r27 = java.lang.Integer.valueOf(r27)     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            r26.add(r27)     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            if (r11 != 0) goto L_0x0279
            long r26 = r18.getSize()     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
            r0 = r26
            r2 = r30
            r2.mFirstPartSize = r0     // Catch:{ ServiceException -> 0x029a, ClientException -> 0x02f9 }
        L_0x0279:
            int r11 = r11 + 1
            goto L_0x01d8
        L_0x027d:
            java.lang.String r26 = ""
            goto L_0x0074
        L_0x0282:
            r7 = move-exception
            com.alibaba.sdk.android.oss.common.OSSLog.logThrowable2Local(r7)     // Catch:{ all -> 0x0290 }
            if (r17 == 0) goto L_0x028b
            r17.close()
        L_0x028b:
            r6.delete()
            goto L_0x017e
        L_0x0290:
            r26 = move-exception
            if (r17 == 0) goto L_0x0296
            r17.close()
        L_0x0296:
            r6.delete()
            throw r26
        L_0x029a:
            r7 = move-exception
            r14 = 0
            int r26 = r7.getStatusCode()
            r27 = 404(0x194, float:5.66E-43)
            r0 = r26
            r1 = r27
            if (r0 != r1) goto L_0x02f8
            r26 = 0
            r0 = r26
            r1 = r30
            r1.mUploadId = r0
        L_0x02b0:
            r25.waitUntilFinished()
            if (r14 != 0) goto L_0x0181
        L_0x02b5:
            r0 = r30
            java.io.File r0 = r0.mRecordFile
            r26 = r0
            boolean r26 = r26.exists()
            if (r26 != 0) goto L_0x02fc
            r0 = r30
            java.io.File r0 = r0.mRecordFile
            r26 = r0
            boolean r26 = r26.createNewFile()
            if (r26 != 0) goto L_0x02fc
            com.alibaba.sdk.android.oss.ClientException r26 = new com.alibaba.sdk.android.oss.ClientException
            java.lang.StringBuilder r27 = new java.lang.StringBuilder
            r27.<init>()
            java.lang.String r28 = "Can't create file at path: "
            java.lang.StringBuilder r27 = r27.append(r28)
            r0 = r30
            java.io.File r0 = r0.mRecordFile
            r28 = r0
            java.lang.String r28 = r28.getAbsolutePath()
            java.lang.StringBuilder r27 = r27.append(r28)
            java.lang.String r28 = "\nPlease make sure the directory exist!"
            java.lang.StringBuilder r27 = r27.append(r28)
            java.lang.String r27 = r27.toString()
            r26.<init>((java.lang.String) r27)
            throw r26
        L_0x02f8:
            throw r7
        L_0x02f9:
            r7 = move-exception
            r14 = 0
            throw r7
        L_0x02fc:
            r0 = r30
            java.lang.String r0 = r0.mUploadId
            r26 = r0
            boolean r26 = com.alibaba.sdk.android.oss.common.utils.OSSUtils.isEmptyString(r26)
            if (r26 == 0) goto L_0x0385
            com.alibaba.sdk.android.oss.model.InitiateMultipartUploadRequest r12 = new com.alibaba.sdk.android.oss.model.InitiateMultipartUploadRequest
            r0 = r30
            com.alibaba.sdk.android.oss.model.MultipartUploadRequest r0 = r0.mRequest
            r26 = r0
            com.alibaba.sdk.android.oss.model.ResumableUploadRequest r26 = (com.alibaba.sdk.android.oss.model.ResumableUploadRequest) r26
            java.lang.String r27 = r26.getBucketName()
            r0 = r30
            com.alibaba.sdk.android.oss.model.MultipartUploadRequest r0 = r0.mRequest
            r26 = r0
            com.alibaba.sdk.android.oss.model.ResumableUploadRequest r26 = (com.alibaba.sdk.android.oss.model.ResumableUploadRequest) r26
            java.lang.String r28 = r26.getObjectKey()
            r0 = r30
            com.alibaba.sdk.android.oss.model.MultipartUploadRequest r0 = r0.mRequest
            r26 = r0
            com.alibaba.sdk.android.oss.model.ResumableUploadRequest r26 = (com.alibaba.sdk.android.oss.model.ResumableUploadRequest) r26
            com.alibaba.sdk.android.oss.model.ObjectMetadata r26 = r26.getMetadata()
            r0 = r27
            r1 = r28
            r2 = r26
            r12.<init>(r0, r1, r2)
            r26 = 1
            r0 = r26
            r12.isSequential = r0
            r0 = r30
            com.alibaba.sdk.android.oss.internal.InternalRequestOperation r0 = r0.mApiOperation
            r26 = r0
            r27 = 0
            r0 = r26
            r1 = r27
            com.alibaba.sdk.android.oss.internal.OSSAsyncTask r26 = r0.initMultipartUpload(r12, r1)
            com.alibaba.sdk.android.oss.model.OSSResult r13 = r26.getResult()
            com.alibaba.sdk.android.oss.model.InitiateMultipartUploadResult r13 = (com.alibaba.sdk.android.oss.model.InitiateMultipartUploadResult) r13
            java.lang.String r26 = r13.getUploadId()
            r0 = r26
            r1 = r30
            r1.mUploadId = r0
            r0 = r30
            java.io.File r0 = r0.mRecordFile
            r26 = r0
            if (r26 == 0) goto L_0x0385
            java.io.BufferedWriter r5 = new java.io.BufferedWriter
            java.io.FileWriter r26 = new java.io.FileWriter
            r0 = r30
            java.io.File r0 = r0.mRecordFile
            r27 = r0
            r26.<init>(r27)
            r0 = r26
            r5.<init>(r0)
            r0 = r30
            java.lang.String r0 = r0.mUploadId
            r26 = r0
            r0 = r26
            r5.write(r0)
            r5.close()
        L_0x0385:
            r0 = r30
            com.alibaba.sdk.android.oss.model.MultipartUploadRequest r0 = r0.mRequest
            r26 = r0
            com.alibaba.sdk.android.oss.model.ResumableUploadRequest r26 = (com.alibaba.sdk.android.oss.model.ResumableUploadRequest) r26
            r0 = r30
            java.lang.String r0 = r0.mUploadId
            r27 = r0
            r26.setUploadId(r27)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.sdk.android.oss.internal.SequenceUploadTask.initMultipartUploadId():void");
    }

    /* access modifiers changed from: protected */
    public ResumableUploadResult doMultipartUpload() throws IOException, ClientException, ServiceException, InterruptedException {
        long tempUploadedLength = this.mUploadedLength;
        checkCancel();
        int readByte = this.mPartAttr[0];
        int partNumber = this.mPartAttr[1];
        if (this.mPartETags.size() > 0 && this.mAlreadyUploadIndex.size() > 0) {
            if (this.mUploadedLength > this.mFileLength) {
                throw new ClientException("The uploading file is inconsistent with before");
            } else if (this.mFirstPartSize != ((long) readByte)) {
                throw new ClientException("The part size setting is inconsistent with before");
            } else {
                long revertUploadedLength = this.mUploadedLength;
                if (!TextUtils.isEmpty(this.mSp.getStringValue(this.mUploadId))) {
                    revertUploadedLength = Long.valueOf(this.mSp.getStringValue(this.mUploadId)).longValue();
                }
                if (this.mProgressCallback != null) {
                    this.mProgressCallback.onProgress(this.mRequest, revertUploadedLength, this.mFileLength);
                }
                this.mSp.removeKey(this.mUploadId);
            }
        }
        for (int i = 0; i < partNumber; i++) {
            if (this.mAlreadyUploadIndex.size() == 0 || !this.mAlreadyUploadIndex.contains(Integer.valueOf(i + 1))) {
                if (i == partNumber - 1) {
                    readByte = (int) (this.mFileLength - tempUploadedLength);
                }
                OSSLog.logDebug("upload part readByte : " + readByte);
                int byteCount = readByte;
                tempUploadedLength += (long) byteCount;
                uploadPart(i, byteCount, partNumber);
                if (this.mUploadException != null) {
                    break;
                }
            }
        }
        checkException();
        CompleteMultipartUploadResult completeResult = completeMultipartUploadResult();
        ResumableUploadResult result = null;
        if (completeResult != null) {
            result = new ResumableUploadResult(completeResult);
        }
        if (this.mRecordFile != null) {
            this.mRecordFile.delete();
        }
        if (this.mCRC64RecordFile != null) {
            this.mCRC64RecordFile.delete();
        }
        return result;
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x0107 A[Catch:{ all -> 0x018b }] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x010e A[SYNTHETIC, Splitter:B:30:0x010e] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x013d A[SYNTHETIC, Splitter:B:43:0x013d] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x018e A[SYNTHETIC, Splitter:B:50:0x018e] */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x019a A[SYNTHETIC, Splitter:B:58:0x019a] */
    /* JADX WARNING: Removed duplicated region for block: B:78:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:82:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void uploadPart(int r21, int r22, int r23) {
        /*
            r20 = this;
            r13 = 0
            r15 = 0
            r0 = r20
            com.alibaba.sdk.android.oss.network.ExecutionContext r2 = r0.mContext     // Catch:{ ServiceException -> 0x01ba, Exception -> 0x0192 }
            com.alibaba.sdk.android.oss.network.CancellationHandler r2 = r2.getCancellationHandler()     // Catch:{ ServiceException -> 0x01ba, Exception -> 0x0192 }
            boolean r2 = r2.isCancelled()     // Catch:{ ServiceException -> 0x01ba, Exception -> 0x0192 }
            if (r2 == 0) goto L_0x001b
            if (r13 == 0) goto L_0x0015
            r13.close()     // Catch:{ IOException -> 0x0016 }
        L_0x0015:
            return
        L_0x0016:
            r10 = move-exception
            com.alibaba.sdk.android.oss.common.OSSLog.logThrowable2Local(r10)
            goto L_0x0015
        L_0x001b:
            r0 = r20
            int r2 = r0.mRunPartTaskCount     // Catch:{ ServiceException -> 0x01ba, Exception -> 0x0192 }
            int r2 = r2 + 1
            r0 = r20
            r0.mRunPartTaskCount = r2     // Catch:{ ServiceException -> 0x01ba, Exception -> 0x0192 }
            r20.preUploadPart(r21, r22, r23)     // Catch:{ ServiceException -> 0x01ba, Exception -> 0x0192 }
            java.io.RandomAccessFile r14 = new java.io.RandomAccessFile     // Catch:{ ServiceException -> 0x01ba, Exception -> 0x0192 }
            r0 = r20
            java.io.File r2 = r0.mUploadFile     // Catch:{ ServiceException -> 0x01ba, Exception -> 0x0192 }
            java.lang.String r3 = "r"
            r14.<init>(r2, r3)     // Catch:{ ServiceException -> 0x01ba, Exception -> 0x0192 }
            com.alibaba.sdk.android.oss.model.UploadPartRequest r18 = new com.alibaba.sdk.android.oss.model.UploadPartRequest     // Catch:{ ServiceException -> 0x01bd, Exception -> 0x01b2, all -> 0x01aa }
            r0 = r20
            com.alibaba.sdk.android.oss.model.MultipartUploadRequest r2 = r0.mRequest     // Catch:{ ServiceException -> 0x01bd, Exception -> 0x01b2, all -> 0x01aa }
            com.alibaba.sdk.android.oss.model.ResumableUploadRequest r2 = (com.alibaba.sdk.android.oss.model.ResumableUploadRequest) r2     // Catch:{ ServiceException -> 0x01bd, Exception -> 0x01b2, all -> 0x01aa }
            java.lang.String r3 = r2.getBucketName()     // Catch:{ ServiceException -> 0x01bd, Exception -> 0x01b2, all -> 0x01aa }
            r0 = r20
            com.alibaba.sdk.android.oss.model.MultipartUploadRequest r2 = r0.mRequest     // Catch:{ ServiceException -> 0x01bd, Exception -> 0x01b2, all -> 0x01aa }
            com.alibaba.sdk.android.oss.model.ResumableUploadRequest r2 = (com.alibaba.sdk.android.oss.model.ResumableUploadRequest) r2     // Catch:{ ServiceException -> 0x01bd, Exception -> 0x01b2, all -> 0x01aa }
            java.lang.String r2 = r2.getObjectKey()     // Catch:{ ServiceException -> 0x01bd, Exception -> 0x01b2, all -> 0x01aa }
            r0 = r20
            java.lang.String r4 = r0.mUploadId     // Catch:{ ServiceException -> 0x01bd, Exception -> 0x01b2, all -> 0x01aa }
            int r5 = r21 + 1
            r0 = r18
            r0.<init>(r3, r2, r4, r5)     // Catch:{ ServiceException -> 0x01bd, Exception -> 0x01b2, all -> 0x01aa }
            r0 = r21
            long r4 = (long) r0
            r0 = r20
            com.alibaba.sdk.android.oss.model.MultipartUploadRequest r2 = r0.mRequest     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            com.alibaba.sdk.android.oss.model.ResumableUploadRequest r2 = (com.alibaba.sdk.android.oss.model.ResumableUploadRequest) r2     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            long r2 = r2.getPartSize()     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            long r16 = r4 * r2
            r0 = r22
            byte[] r11 = new byte[r0]     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            r0 = r16
            r14.seek(r0)     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            r2 = 0
            r0 = r22
            r14.readFully(r11, r2, r0)     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            r0 = r18
            r0.setPartContent(r11)     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            java.lang.String r2 = com.alibaba.sdk.android.oss.common.utils.BinaryUtil.calculateBase64Md5((byte[]) r11)     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            r0 = r18
            r0.setMd5Digest(r2)     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            r0 = r20
            com.alibaba.sdk.android.oss.model.MultipartUploadRequest r2 = r0.mRequest     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            com.alibaba.sdk.android.oss.model.ResumableUploadRequest r2 = (com.alibaba.sdk.android.oss.model.ResumableUploadRequest) r2     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            java.lang.Enum r2 = r2.getCRC64()     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            r0 = r18
            r0.setCRC64(r2)     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            r0 = r20
            com.alibaba.sdk.android.oss.internal.InternalRequestOperation r2 = r0.mApiOperation     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            r0 = r18
            com.alibaba.sdk.android.oss.model.UploadPartResult r19 = r2.syncUploadPart(r0)     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            com.alibaba.sdk.android.oss.model.PartETag r12 = new com.alibaba.sdk.android.oss.model.PartETag     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            int r2 = r18.getPartNumber()     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            java.lang.String r3 = r19.getETag()     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            r12.<init>(r2, r3)     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            r0 = r22
            long r2 = (long) r0     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            r12.setPartSize(r2)     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            r0 = r20
            boolean r2 = r0.mCheckCRC64     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            if (r2 == 0) goto L_0x00be
            java.lang.Long r2 = r19.getClientCRC()     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            long r2 = r2.longValue()     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            r12.setCRC64(r2)     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
        L_0x00be:
            r0 = r20
            java.util.List r2 = r0.mPartETags     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            r2.add(r12)     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            r0 = r20
            long r2 = r0.mUploadedLength     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            r0 = r22
            long r4 = (long) r0     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            long r2 = r2 + r4
            r0 = r20
            r0.mUploadedLength = r2     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            r0 = r20
            r0.uploadPartFinish(r12)     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            r0 = r20
            com.alibaba.sdk.android.oss.network.ExecutionContext r2 = r0.mContext     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            com.alibaba.sdk.android.oss.network.CancellationHandler r2 = r2.getCancellationHandler()     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            boolean r2 = r2.isCancelled()     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            if (r2 == 0) goto L_0x0119
            com.alibaba.sdk.android.oss.TaskCancelException r10 = new com.alibaba.sdk.android.oss.TaskCancelException     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            java.lang.String r2 = "sequence upload task cancel"
            r10.<init>((java.lang.String) r2)     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            com.alibaba.sdk.android.oss.ClientException r2 = new com.alibaba.sdk.android.oss.ClientException     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            java.lang.String r3 = r10.getMessage()     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            r4 = 1
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r4)     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            r2.<init>(r3, r10, r4)     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            throw r2     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
        L_0x00fb:
            r10 = move-exception
            r15 = r18
            r13 = r14
        L_0x00ff:
            int r2 = r10.getStatusCode()     // Catch:{ all -> 0x018b }
            r3 = 409(0x199, float:5.73E-43)
            if (r2 == r3) goto L_0x013d
            r0 = r20
            r0.processException(r10)     // Catch:{ all -> 0x018b }
        L_0x010c:
            if (r13 == 0) goto L_0x0015
            r13.close()     // Catch:{ IOException -> 0x0113 }
            goto L_0x0015
        L_0x0113:
            r10 = move-exception
            com.alibaba.sdk.android.oss.common.OSSLog.logThrowable2Local(r10)
            goto L_0x0015
        L_0x0119:
            r0 = r20
            com.alibaba.sdk.android.oss.model.MultipartUploadRequest r3 = r0.mRequest     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            r0 = r20
            long r4 = r0.mUploadedLength     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            r0 = r20
            long r6 = r0.mFileLength     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            r2 = r20
            r2.onProgressCallback(r3, r4, r6)     // Catch:{ ServiceException -> 0x00fb, Exception -> 0x01b5, all -> 0x01ad }
            if (r14 == 0) goto L_0x012f
            r14.close()     // Catch:{ IOException -> 0x0134 }
        L_0x012f:
            r15 = r18
            r13 = r14
            goto L_0x0015
        L_0x0134:
            r10 = move-exception
            com.alibaba.sdk.android.oss.common.OSSLog.logThrowable2Local(r10)
            r15 = r18
            r13 = r14
            goto L_0x0015
        L_0x013d:
            com.alibaba.sdk.android.oss.model.PartETag r12 = new com.alibaba.sdk.android.oss.model.PartETag     // Catch:{ all -> 0x018b }
            int r2 = r15.getPartNumber()     // Catch:{ all -> 0x018b }
            java.lang.String r3 = r10.getPartEtag()     // Catch:{ all -> 0x018b }
            r12.<init>(r2, r3)     // Catch:{ all -> 0x018b }
            byte[] r2 = r15.getPartContent()     // Catch:{ all -> 0x018b }
            int r2 = r2.length     // Catch:{ all -> 0x018b }
            long r2 = (long) r2     // Catch:{ all -> 0x018b }
            r12.setPartSize(r2)     // Catch:{ all -> 0x018b }
            r0 = r20
            boolean r2 = r0.mCheckCRC64     // Catch:{ all -> 0x018b }
            if (r2 == 0) goto L_0x0177
            byte[] r11 = r15.getPartContent()     // Catch:{ all -> 0x018b }
            java.io.ByteArrayInputStream r8 = new java.io.ByteArrayInputStream     // Catch:{ all -> 0x018b }
            r8.<init>(r11)     // Catch:{ all -> 0x018b }
            java.util.zip.CheckedInputStream r9 = new java.util.zip.CheckedInputStream     // Catch:{ all -> 0x018b }
            com.alibaba.sdk.android.oss.common.utils.CRC64 r2 = new com.alibaba.sdk.android.oss.common.utils.CRC64     // Catch:{ all -> 0x018b }
            r2.<init>()     // Catch:{ all -> 0x018b }
            r9.<init>(r8, r2)     // Catch:{ all -> 0x018b }
            java.util.zip.Checksum r2 = r9.getChecksum()     // Catch:{ all -> 0x018b }
            long r2 = r2.getValue()     // Catch:{ all -> 0x018b }
            r12.setCRC64(r2)     // Catch:{ all -> 0x018b }
        L_0x0177:
            r0 = r20
            java.util.List r2 = r0.mPartETags     // Catch:{ all -> 0x018b }
            r2.add(r12)     // Catch:{ all -> 0x018b }
            r0 = r20
            long r2 = r0.mUploadedLength     // Catch:{ all -> 0x018b }
            r0 = r22
            long r4 = (long) r0     // Catch:{ all -> 0x018b }
            long r2 = r2 + r4
            r0 = r20
            r0.mUploadedLength = r2     // Catch:{ all -> 0x018b }
            goto L_0x010c
        L_0x018b:
            r2 = move-exception
        L_0x018c:
            if (r13 == 0) goto L_0x0191
            r13.close()     // Catch:{ IOException -> 0x01a5 }
        L_0x0191:
            throw r2
        L_0x0192:
            r10 = move-exception
        L_0x0193:
            r0 = r20
            r0.processException(r10)     // Catch:{ all -> 0x018b }
            if (r13 == 0) goto L_0x0015
            r13.close()     // Catch:{ IOException -> 0x019f }
            goto L_0x0015
        L_0x019f:
            r10 = move-exception
            com.alibaba.sdk.android.oss.common.OSSLog.logThrowable2Local(r10)
            goto L_0x0015
        L_0x01a5:
            r10 = move-exception
            com.alibaba.sdk.android.oss.common.OSSLog.logThrowable2Local(r10)
            goto L_0x0191
        L_0x01aa:
            r2 = move-exception
            r13 = r14
            goto L_0x018c
        L_0x01ad:
            r2 = move-exception
            r15 = r18
            r13 = r14
            goto L_0x018c
        L_0x01b2:
            r10 = move-exception
            r13 = r14
            goto L_0x0193
        L_0x01b5:
            r10 = move-exception
            r15 = r18
            r13 = r14
            goto L_0x0193
        L_0x01ba:
            r10 = move-exception
            goto L_0x00ff
        L_0x01bd:
            r10 = move-exception
            r13 = r14
            goto L_0x00ff
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.sdk.android.oss.internal.SequenceUploadTask.uploadPart(int, int, int):void");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00c1  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00c9  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void checkException() throws java.io.IOException, com.alibaba.sdk.android.oss.ServiceException, com.alibaba.sdk.android.oss.ClientException {
        /*
            r10 = this;
            com.alibaba.sdk.android.oss.network.ExecutionContext r6 = r10.mContext
            com.alibaba.sdk.android.oss.network.CancellationHandler r6 = r6.getCancellationHandler()
            boolean r6 = r6.isCancelled()
            if (r6 == 0) goto L_0x0026
            com.alibaba.sdk.android.oss.model.MultipartUploadRequest r6 = r10.mRequest
            com.alibaba.sdk.android.oss.model.ResumableUploadRequest r6 = (com.alibaba.sdk.android.oss.model.ResumableUploadRequest) r6
            java.lang.Boolean r6 = r6.deleteUploadOnCancelling()
            boolean r6 = r6.booleanValue()
            if (r6 == 0) goto L_0x002a
            r10.abortThisUpload()
            java.io.File r6 = r10.mRecordFile
            if (r6 == 0) goto L_0x0026
            java.io.File r6 = r10.mRecordFile
            r6.delete()
        L_0x0026:
            super.checkException()
            return
        L_0x002a:
            java.util.List r6 = r10.mPartETags
            if (r6 == 0) goto L_0x0026
            java.util.List r6 = r10.mPartETags
            int r6 = r6.size()
            if (r6 <= 0) goto L_0x0026
            boolean r6 = r10.mCheckCRC64
            if (r6 == 0) goto L_0x0026
            com.alibaba.sdk.android.oss.model.MultipartUploadRequest r6 = r10.mRequest
            com.alibaba.sdk.android.oss.model.ResumableUploadRequest r6 = (com.alibaba.sdk.android.oss.model.ResumableUploadRequest) r6
            java.lang.String r6 = r6.getRecordDirectory()
            if (r6 == 0) goto L_0x0026
            java.util.HashMap r3 = new java.util.HashMap
            r3.<init>()
            java.util.List r6 = r10.mPartETags
            java.util.Iterator r6 = r6.iterator()
        L_0x004f:
            boolean r7 = r6.hasNext()
            if (r7 == 0) goto L_0x006f
            java.lang.Object r1 = r6.next()
            com.alibaba.sdk.android.oss.model.PartETag r1 = (com.alibaba.sdk.android.oss.model.PartETag) r1
            int r7 = r1.getPartNumber()
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)
            long r8 = r1.getCRC64()
            java.lang.Long r8 = java.lang.Long.valueOf(r8)
            r3.put(r7, r8)
            goto L_0x004f
        L_0x006f:
            r4 = 0
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00bb }
            r7.<init>()     // Catch:{ IOException -> 0x00bb }
            com.alibaba.sdk.android.oss.model.MultipartUploadRequest r6 = r10.mRequest     // Catch:{ IOException -> 0x00bb }
            com.alibaba.sdk.android.oss.model.ResumableUploadRequest r6 = (com.alibaba.sdk.android.oss.model.ResumableUploadRequest) r6     // Catch:{ IOException -> 0x00bb }
            java.lang.String r6 = r6.getRecordDirectory()     // Catch:{ IOException -> 0x00bb }
            java.lang.StringBuilder r6 = r7.append(r6)     // Catch:{ IOException -> 0x00bb }
            java.lang.String r7 = java.io.File.separator     // Catch:{ IOException -> 0x00bb }
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ IOException -> 0x00bb }
            java.lang.String r7 = r10.mUploadId     // Catch:{ IOException -> 0x00bb }
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ IOException -> 0x00bb }
            java.lang.String r2 = r6.toString()     // Catch:{ IOException -> 0x00bb }
            java.io.File r6 = new java.io.File     // Catch:{ IOException -> 0x00bb }
            r6.<init>(r2)     // Catch:{ IOException -> 0x00bb }
            r10.mCRC64RecordFile = r6     // Catch:{ IOException -> 0x00bb }
            java.io.File r6 = r10.mCRC64RecordFile     // Catch:{ IOException -> 0x00bb }
            boolean r6 = r6.exists()     // Catch:{ IOException -> 0x00bb }
            if (r6 != 0) goto L_0x00a5
            java.io.File r6 = r10.mCRC64RecordFile     // Catch:{ IOException -> 0x00bb }
            r6.createNewFile()     // Catch:{ IOException -> 0x00bb }
        L_0x00a5:
            java.io.ObjectOutputStream r5 = new java.io.ObjectOutputStream     // Catch:{ IOException -> 0x00bb }
            java.io.FileOutputStream r6 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x00bb }
            java.io.File r7 = r10.mCRC64RecordFile     // Catch:{ IOException -> 0x00bb }
            r6.<init>(r7)     // Catch:{ IOException -> 0x00bb }
            r5.<init>(r6)     // Catch:{ IOException -> 0x00bb }
            r5.writeObject(r3)     // Catch:{ IOException -> 0x00d0, all -> 0x00cd }
            if (r5 == 0) goto L_0x0026
            r5.close()
            goto L_0x0026
        L_0x00bb:
            r0 = move-exception
        L_0x00bc:
            com.alibaba.sdk.android.oss.common.OSSLog.logThrowable2Local(r0)     // Catch:{ all -> 0x00c6 }
            if (r4 == 0) goto L_0x0026
            r4.close()
            goto L_0x0026
        L_0x00c6:
            r6 = move-exception
        L_0x00c7:
            if (r4 == 0) goto L_0x00cc
            r4.close()
        L_0x00cc:
            throw r6
        L_0x00cd:
            r6 = move-exception
            r4 = r5
            goto L_0x00c7
        L_0x00d0:
            r0 = move-exception
            r4 = r5
            goto L_0x00bc
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.sdk.android.oss.internal.SequenceUploadTask.checkException():void");
    }

    /* access modifiers changed from: protected */
    public void abortThisUpload() {
        if (this.mUploadId != null) {
            this.mApiOperation.abortMultipartUpload(new AbortMultipartUploadRequest(((ResumableUploadRequest) this.mRequest).getBucketName(), ((ResumableUploadRequest) this.mRequest).getObjectKey(), this.mUploadId), (OSSCompletedCallback<AbortMultipartUploadRequest, AbortMultipartUploadResult>) null).waitUntilFinished();
        }
    }

    /* access modifiers changed from: protected */
    public void processException(Exception e) {
        if (this.mUploadException == null || !e.getMessage().equals(this.mUploadException.getMessage())) {
            this.mUploadException = e;
        }
        OSSLog.logThrowable2Local(e);
        if (this.mContext.getCancellationHandler().isCancelled() && !this.mIsCancel) {
            this.mIsCancel = true;
        }
    }

    /* access modifiers changed from: protected */
    public void uploadPartFinish(PartETag partETag) throws Exception {
        if (this.mContext.getCancellationHandler().isCancelled() && !this.mSp.contains(this.mUploadId)) {
            this.mSp.setStringValue(this.mUploadId, String.valueOf(this.mUploadedLength));
            onProgressCallback(this.mRequest, this.mUploadedLength, this.mFileLength);
        }
    }
}
