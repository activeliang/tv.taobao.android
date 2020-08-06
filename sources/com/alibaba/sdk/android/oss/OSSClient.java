package com.alibaba.sdk.android.oss;

import android.content.Context;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
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
import com.alibaba.sdk.android.oss.model.GeneratePresignedUrlRequest;
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
import com.alibaba.sdk.android.oss.model.MultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.alibaba.sdk.android.oss.model.PutSymlinkRequest;
import com.alibaba.sdk.android.oss.model.PutSymlinkResult;
import com.alibaba.sdk.android.oss.model.RestoreObjectRequest;
import com.alibaba.sdk.android.oss.model.RestoreObjectResult;
import com.alibaba.sdk.android.oss.model.ResumableUploadRequest;
import com.alibaba.sdk.android.oss.model.ResumableUploadResult;
import com.alibaba.sdk.android.oss.model.TriggerCallbackRequest;
import com.alibaba.sdk.android.oss.model.TriggerCallbackResult;
import com.alibaba.sdk.android.oss.model.UploadPartRequest;
import com.alibaba.sdk.android.oss.model.UploadPartResult;
import java.io.IOException;

public class OSSClient implements OSS {
    private OSS mOss;

    public OSSClient(Context context, String endpoint, OSSCredentialProvider credentialProvider) {
        this(context, endpoint, credentialProvider, (ClientConfiguration) null);
    }

    public OSSClient(Context context, String endpoint, OSSCredentialProvider credentialProvider, ClientConfiguration conf) {
        this.mOss = new OSSImpl(context, endpoint, credentialProvider, conf);
    }

    public OSSClient(Context context, OSSCredentialProvider credentialProvider, ClientConfiguration conf) {
        this.mOss = new OSSImpl(context, credentialProvider, conf);
    }

    public OSSAsyncTask<ListBucketsResult> asyncListBuckets(ListBucketsRequest request, OSSCompletedCallback<ListBucketsRequest, ListBucketsResult> completedCallback) {
        return this.mOss.asyncListBuckets(request, completedCallback);
    }

    public ListBucketsResult listBuckets(ListBucketsRequest request) throws ClientException, ServiceException {
        return this.mOss.listBuckets(request);
    }

    public OSSAsyncTask<CreateBucketResult> asyncCreateBucket(CreateBucketRequest request, OSSCompletedCallback<CreateBucketRequest, CreateBucketResult> completedCallback) {
        return this.mOss.asyncCreateBucket(request, completedCallback);
    }

    public CreateBucketResult createBucket(CreateBucketRequest request) throws ClientException, ServiceException {
        return this.mOss.createBucket(request);
    }

    public OSSAsyncTask<DeleteBucketResult> asyncDeleteBucket(DeleteBucketRequest request, OSSCompletedCallback<DeleteBucketRequest, DeleteBucketResult> completedCallback) {
        return this.mOss.asyncDeleteBucket(request, completedCallback);
    }

    public DeleteBucketResult deleteBucket(DeleteBucketRequest request) throws ClientException, ServiceException {
        return this.mOss.deleteBucket(request);
    }

    public OSSAsyncTask<GetBucketInfoResult> asyncGetBucketInfo(GetBucketInfoRequest request, OSSCompletedCallback<GetBucketInfoRequest, GetBucketInfoResult> completedCallback) {
        return this.mOss.asyncGetBucketInfo(request, completedCallback);
    }

    public GetBucketInfoResult getBucketInfo(GetBucketInfoRequest request) throws ClientException, ServiceException {
        return this.mOss.getBucketInfo(request);
    }

    public OSSAsyncTask<GetBucketACLResult> asyncGetBucketACL(GetBucketACLRequest request, OSSCompletedCallback<GetBucketACLRequest, GetBucketACLResult> completedCallback) {
        return this.mOss.asyncGetBucketACL(request, completedCallback);
    }

    public GetBucketACLResult getBucketACL(GetBucketACLRequest request) throws ClientException, ServiceException {
        return this.mOss.getBucketACL(request);
    }

    public OSSAsyncTask<PutObjectResult> asyncPutObject(PutObjectRequest request, OSSCompletedCallback<PutObjectRequest, PutObjectResult> completedCallback) {
        return this.mOss.asyncPutObject(request, completedCallback);
    }

    public PutObjectResult putObject(PutObjectRequest request) throws ClientException, ServiceException {
        return this.mOss.putObject(request);
    }

    public OSSAsyncTask<GetObjectResult> asyncGetObject(GetObjectRequest request, OSSCompletedCallback<GetObjectRequest, GetObjectResult> completedCallback) {
        return this.mOss.asyncGetObject(request, completedCallback);
    }

    public GetObjectResult getObject(GetObjectRequest request) throws ClientException, ServiceException {
        return this.mOss.getObject(request);
    }

    public OSSAsyncTask<GetObjectACLResult> asyncGetObjectACL(GetObjectACLRequest request, OSSCompletedCallback<GetObjectACLRequest, GetObjectACLResult> completedCallback) {
        return this.mOss.asyncGetObjectACL(request, completedCallback);
    }

    public GetObjectACLResult getObjectACL(GetObjectACLRequest request) throws ClientException, ServiceException {
        return this.mOss.getObjectACL(request);
    }

    public OSSAsyncTask<DeleteObjectResult> asyncDeleteObject(DeleteObjectRequest request, OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult> completedCallback) {
        return this.mOss.asyncDeleteObject(request, completedCallback);
    }

    public DeleteObjectResult deleteObject(DeleteObjectRequest request) throws ClientException, ServiceException {
        return this.mOss.deleteObject(request);
    }

    public OSSAsyncTask<DeleteMultipleObjectResult> asyncDeleteMultipleObject(DeleteMultipleObjectRequest request, OSSCompletedCallback<DeleteMultipleObjectRequest, DeleteMultipleObjectResult> completedCallback) {
        return this.mOss.asyncDeleteMultipleObject(request, completedCallback);
    }

    public DeleteMultipleObjectResult deleteMultipleObject(DeleteMultipleObjectRequest request) throws ClientException, ServiceException {
        return this.mOss.deleteMultipleObject(request);
    }

    public OSSAsyncTask<AppendObjectResult> asyncAppendObject(AppendObjectRequest request, OSSCompletedCallback<AppendObjectRequest, AppendObjectResult> completedCallback) {
        return this.mOss.asyncAppendObject(request, completedCallback);
    }

    public AppendObjectResult appendObject(AppendObjectRequest request) throws ClientException, ServiceException {
        return this.mOss.appendObject(request);
    }

    public OSSAsyncTask<HeadObjectResult> asyncHeadObject(HeadObjectRequest request, OSSCompletedCallback<HeadObjectRequest, HeadObjectResult> completedCallback) {
        return this.mOss.asyncHeadObject(request, completedCallback);
    }

    public HeadObjectResult headObject(HeadObjectRequest request) throws ClientException, ServiceException {
        return this.mOss.headObject(request);
    }

    public OSSAsyncTask<CopyObjectResult> asyncCopyObject(CopyObjectRequest request, OSSCompletedCallback<CopyObjectRequest, CopyObjectResult> completedCallback) {
        return this.mOss.asyncCopyObject(request, completedCallback);
    }

    public CopyObjectResult copyObject(CopyObjectRequest request) throws ClientException, ServiceException {
        return this.mOss.copyObject(request);
    }

    public OSSAsyncTask<ListObjectsResult> asyncListObjects(ListObjectsRequest request, OSSCompletedCallback<ListObjectsRequest, ListObjectsResult> completedCallback) {
        return this.mOss.asyncListObjects(request, completedCallback);
    }

    public ListObjectsResult listObjects(ListObjectsRequest request) throws ClientException, ServiceException {
        return this.mOss.listObjects(request);
    }

    public OSSAsyncTask<InitiateMultipartUploadResult> asyncInitMultipartUpload(InitiateMultipartUploadRequest request, OSSCompletedCallback<InitiateMultipartUploadRequest, InitiateMultipartUploadResult> completedCallback) {
        return this.mOss.asyncInitMultipartUpload(request, completedCallback);
    }

    public InitiateMultipartUploadResult initMultipartUpload(InitiateMultipartUploadRequest request) throws ClientException, ServiceException {
        return this.mOss.initMultipartUpload(request);
    }

    public OSSAsyncTask<UploadPartResult> asyncUploadPart(UploadPartRequest request, OSSCompletedCallback<UploadPartRequest, UploadPartResult> completedCallback) {
        return this.mOss.asyncUploadPart(request, completedCallback);
    }

    public UploadPartResult uploadPart(UploadPartRequest request) throws ClientException, ServiceException {
        return this.mOss.uploadPart(request);
    }

    public OSSAsyncTask<CompleteMultipartUploadResult> asyncCompleteMultipartUpload(CompleteMultipartUploadRequest request, OSSCompletedCallback<CompleteMultipartUploadRequest, CompleteMultipartUploadResult> completedCallback) {
        return this.mOss.asyncCompleteMultipartUpload(request, completedCallback);
    }

    public CompleteMultipartUploadResult completeMultipartUpload(CompleteMultipartUploadRequest request) throws ClientException, ServiceException {
        return this.mOss.completeMultipartUpload(request);
    }

    public OSSAsyncTask<AbortMultipartUploadResult> asyncAbortMultipartUpload(AbortMultipartUploadRequest request, OSSCompletedCallback<AbortMultipartUploadRequest, AbortMultipartUploadResult> completedCallback) {
        return this.mOss.asyncAbortMultipartUpload(request, completedCallback);
    }

    public AbortMultipartUploadResult abortMultipartUpload(AbortMultipartUploadRequest request) throws ClientException, ServiceException {
        return this.mOss.abortMultipartUpload(request);
    }

    public OSSAsyncTask<ListPartsResult> asyncListParts(ListPartsRequest request, OSSCompletedCallback<ListPartsRequest, ListPartsResult> completedCallback) {
        return this.mOss.asyncListParts(request, completedCallback);
    }

    public ListPartsResult listParts(ListPartsRequest request) throws ClientException, ServiceException {
        return this.mOss.listParts(request);
    }

    public OSSAsyncTask<ListMultipartUploadsResult> asyncListMultipartUploads(ListMultipartUploadsRequest request, OSSCompletedCallback<ListMultipartUploadsRequest, ListMultipartUploadsResult> completedCallback) {
        return this.mOss.asyncListMultipartUploads(request, completedCallback);
    }

    public ListMultipartUploadsResult listMultipartUploads(ListMultipartUploadsRequest request) throws ClientException, ServiceException {
        return this.mOss.listMultipartUploads(request);
    }

    public void updateCredentialProvider(OSSCredentialProvider credentialProvider) {
        this.mOss.updateCredentialProvider(credentialProvider);
    }

    public OSSAsyncTask<CompleteMultipartUploadResult> asyncMultipartUpload(MultipartUploadRequest request, OSSCompletedCallback<MultipartUploadRequest, CompleteMultipartUploadResult> completedCallback) {
        return this.mOss.asyncMultipartUpload(request, completedCallback);
    }

    public CompleteMultipartUploadResult multipartUpload(MultipartUploadRequest request) throws ClientException, ServiceException {
        return this.mOss.multipartUpload(request);
    }

    public OSSAsyncTask<ResumableUploadResult> asyncResumableUpload(ResumableUploadRequest request, OSSCompletedCallback<ResumableUploadRequest, ResumableUploadResult> completedCallback) {
        return this.mOss.asyncResumableUpload(request, completedCallback);
    }

    public ResumableUploadResult resumableUpload(ResumableUploadRequest request) throws ClientException, ServiceException {
        return this.mOss.resumableUpload(request);
    }

    public OSSAsyncTask<ResumableUploadResult> asyncSequenceUpload(ResumableUploadRequest request, OSSCompletedCallback<ResumableUploadRequest, ResumableUploadResult> completedCallback) {
        return this.mOss.asyncSequenceUpload(request, completedCallback);
    }

    public ResumableUploadResult sequenceUpload(ResumableUploadRequest request) throws ClientException, ServiceException {
        return this.mOss.sequenceUpload(request);
    }

    public String presignConstrainedObjectURL(GeneratePresignedUrlRequest request) throws ClientException {
        return this.mOss.presignConstrainedObjectURL(request);
    }

    public String presignConstrainedObjectURL(String bucketName, String objectKey, long expiredTimeInSeconds) throws ClientException {
        return this.mOss.presignConstrainedObjectURL(bucketName, objectKey, expiredTimeInSeconds);
    }

    public String presignPublicObjectURL(String bucketName, String objectKey) {
        return this.mOss.presignPublicObjectURL(bucketName, objectKey);
    }

    public boolean doesObjectExist(String bucketName, String objectKey) throws ClientException, ServiceException {
        return this.mOss.doesObjectExist(bucketName, objectKey);
    }

    public void abortResumableUpload(ResumableUploadRequest request) throws IOException {
        this.mOss.abortResumableUpload(request);
    }

    public OSSAsyncTask<TriggerCallbackResult> asyncTriggerCallback(TriggerCallbackRequest request, OSSCompletedCallback<TriggerCallbackRequest, TriggerCallbackResult> completedCallback) {
        return this.mOss.asyncTriggerCallback(request, completedCallback);
    }

    public TriggerCallbackResult triggerCallback(TriggerCallbackRequest request) throws ClientException, ServiceException {
        return this.mOss.triggerCallback(request);
    }

    public OSSAsyncTask<ImagePersistResult> asyncImagePersist(ImagePersistRequest request, OSSCompletedCallback<ImagePersistRequest, ImagePersistResult> completedCallback) {
        return this.mOss.asyncImagePersist(request, completedCallback);
    }

    public ImagePersistResult imagePersist(ImagePersistRequest request) throws ClientException, ServiceException {
        return this.mOss.imagePersist(request);
    }

    public PutSymlinkResult putSymlink(PutSymlinkRequest request) throws ClientException, ServiceException {
        return this.mOss.putSymlink(request);
    }

    public OSSAsyncTask<PutSymlinkResult> asyncPutSymlink(PutSymlinkRequest request, OSSCompletedCallback<PutSymlinkRequest, PutSymlinkResult> completedCallback) {
        return this.mOss.asyncPutSymlink(request, completedCallback);
    }

    public GetSymlinkResult getSymlink(GetSymlinkRequest request) throws ClientException, ServiceException {
        return this.mOss.getSymlink(request);
    }

    public OSSAsyncTask<GetSymlinkResult> asyncGetSymlink(GetSymlinkRequest request, OSSCompletedCallback<GetSymlinkRequest, GetSymlinkResult> completedCallback) {
        return this.mOss.asyncGetSymlink(request, completedCallback);
    }

    public RestoreObjectResult restoreObject(RestoreObjectRequest request) throws ClientException, ServiceException {
        return this.mOss.restoreObject(request);
    }

    public OSSAsyncTask<RestoreObjectResult> asyncRestoreObject(RestoreObjectRequest request, OSSCompletedCallback<RestoreObjectRequest, RestoreObjectResult> completedCallback) {
        return this.mOss.asyncRestoreObject(request, completedCallback);
    }
}
