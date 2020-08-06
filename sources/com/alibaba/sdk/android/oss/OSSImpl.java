package com.alibaba.sdk.android.oss;

import android.content.Context;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.OSSLogToFileUtils;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.utils.OSSUtils;
import com.alibaba.sdk.android.oss.internal.ExtensionRequestOperation;
import com.alibaba.sdk.android.oss.internal.InternalRequestOperation;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.internal.ObjectURLPresigner;
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
import java.net.URI;
import java.net.URISyntaxException;

class OSSImpl implements OSS {
    private ClientConfiguration conf;
    private OSSCredentialProvider credentialProvider;
    private URI endpointURI;
    private ExtensionRequestOperation extensionRequestOperation;
    private InternalRequestOperation internalRequestOperation;

    public OSSImpl(Context context, String endpoint, OSSCredentialProvider credentialProvider2, ClientConfiguration conf2) {
        OSSLogToFileUtils.init(context.getApplicationContext(), conf2);
        try {
            String endpoint2 = endpoint.trim();
            this.endpointURI = new URI(!endpoint2.startsWith("http") ? "http://" + endpoint2 : endpoint2);
            if (credentialProvider2 == null) {
                throw new IllegalArgumentException("CredentialProvider can't be null.");
            }
            Boolean hostIsIP = false;
            try {
                hostIsIP = Boolean.valueOf(OSSUtils.isValidateIP(this.endpointURI.getHost()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!this.endpointURI.getScheme().equals("https") || !hostIsIP.booleanValue()) {
                this.credentialProvider = credentialProvider2;
                this.conf = conf2 == null ? ClientConfiguration.getDefaultConf() : conf2;
                this.internalRequestOperation = new InternalRequestOperation(context.getApplicationContext(), this.endpointURI, credentialProvider2, this.conf);
                this.extensionRequestOperation = new ExtensionRequestOperation(this.internalRequestOperation);
                return;
            }
            throw new IllegalArgumentException("endpoint should not be format with https://ip.");
        } catch (URISyntaxException e2) {
            throw new IllegalArgumentException("Endpoint must be a string like 'http://oss-cn-****.aliyuncs.com',or your cname like 'http://image.cnamedomain.com'!");
        }
    }

    public OSSImpl(Context context, OSSCredentialProvider credentialProvider2, ClientConfiguration conf2) {
        this.credentialProvider = credentialProvider2;
        this.conf = conf2 == null ? ClientConfiguration.getDefaultConf() : conf2;
        this.internalRequestOperation = new InternalRequestOperation(context.getApplicationContext(), credentialProvider2, this.conf);
        this.extensionRequestOperation = new ExtensionRequestOperation(this.internalRequestOperation);
    }

    public OSSAsyncTask<ListBucketsResult> asyncListBuckets(ListBucketsRequest request, OSSCompletedCallback<ListBucketsRequest, ListBucketsResult> completedCallback) {
        return this.internalRequestOperation.listBuckets(request, completedCallback);
    }

    public ListBucketsResult listBuckets(ListBucketsRequest request) throws ClientException, ServiceException {
        return this.internalRequestOperation.listBuckets(request, (OSSCompletedCallback<ListBucketsRequest, ListBucketsResult>) null).getResult();
    }

    public OSSAsyncTask<CreateBucketResult> asyncCreateBucket(CreateBucketRequest request, OSSCompletedCallback<CreateBucketRequest, CreateBucketResult> completedCallback) {
        return this.internalRequestOperation.createBucket(request, completedCallback);
    }

    public CreateBucketResult createBucket(CreateBucketRequest request) throws ClientException, ServiceException {
        return this.internalRequestOperation.createBucket(request, (OSSCompletedCallback<CreateBucketRequest, CreateBucketResult>) null).getResult();
    }

    public OSSAsyncTask<DeleteBucketResult> asyncDeleteBucket(DeleteBucketRequest request, OSSCompletedCallback<DeleteBucketRequest, DeleteBucketResult> completedCallback) {
        return this.internalRequestOperation.deleteBucket(request, completedCallback);
    }

    public DeleteBucketResult deleteBucket(DeleteBucketRequest request) throws ClientException, ServiceException {
        return this.internalRequestOperation.deleteBucket(request, (OSSCompletedCallback<DeleteBucketRequest, DeleteBucketResult>) null).getResult();
    }

    public OSSAsyncTask<GetBucketInfoResult> asyncGetBucketInfo(GetBucketInfoRequest request, OSSCompletedCallback<GetBucketInfoRequest, GetBucketInfoResult> completedCallback) {
        return this.internalRequestOperation.getBucketInfo(request, completedCallback);
    }

    public GetBucketInfoResult getBucketInfo(GetBucketInfoRequest request) throws ClientException, ServiceException {
        return this.internalRequestOperation.getBucketInfo(request, (OSSCompletedCallback<GetBucketInfoRequest, GetBucketInfoResult>) null).getResult();
    }

    public OSSAsyncTask<GetBucketACLResult> asyncGetBucketACL(GetBucketACLRequest request, OSSCompletedCallback<GetBucketACLRequest, GetBucketACLResult> completedCallback) {
        return this.internalRequestOperation.getBucketACL(request, completedCallback);
    }

    public GetBucketACLResult getBucketACL(GetBucketACLRequest request) throws ClientException, ServiceException {
        return this.internalRequestOperation.getBucketACL(request, (OSSCompletedCallback<GetBucketACLRequest, GetBucketACLResult>) null).getResult();
    }

    public OSSAsyncTask<PutObjectResult> asyncPutObject(PutObjectRequest request, OSSCompletedCallback<PutObjectRequest, PutObjectResult> completedCallback) {
        return this.internalRequestOperation.putObject(request, completedCallback);
    }

    public PutObjectResult putObject(PutObjectRequest request) throws ClientException, ServiceException {
        return this.internalRequestOperation.syncPutObject(request);
    }

    public OSSAsyncTask<GetObjectResult> asyncGetObject(GetObjectRequest request, OSSCompletedCallback<GetObjectRequest, GetObjectResult> completedCallback) {
        return this.internalRequestOperation.getObject(request, completedCallback);
    }

    public GetObjectResult getObject(GetObjectRequest request) throws ClientException, ServiceException {
        return this.internalRequestOperation.getObject(request, (OSSCompletedCallback<GetObjectRequest, GetObjectResult>) null).getResult();
    }

    public OSSAsyncTask<GetObjectACLResult> asyncGetObjectACL(GetObjectACLRequest request, OSSCompletedCallback<GetObjectACLRequest, GetObjectACLResult> completedCallback) {
        return this.internalRequestOperation.getObjectACL(request, completedCallback);
    }

    public GetObjectACLResult getObjectACL(GetObjectACLRequest request) throws ClientException, ServiceException {
        return this.internalRequestOperation.getObjectACL(request, (OSSCompletedCallback<GetObjectACLRequest, GetObjectACLResult>) null).getResult();
    }

    public OSSAsyncTask<DeleteObjectResult> asyncDeleteObject(DeleteObjectRequest request, OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult> completedCallback) {
        return this.internalRequestOperation.deleteObject(request, completedCallback);
    }

    public DeleteObjectResult deleteObject(DeleteObjectRequest request) throws ClientException, ServiceException {
        return this.internalRequestOperation.deleteObject(request, (OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult>) null).getResult();
    }

    public OSSAsyncTask<DeleteMultipleObjectResult> asyncDeleteMultipleObject(DeleteMultipleObjectRequest request, OSSCompletedCallback<DeleteMultipleObjectRequest, DeleteMultipleObjectResult> completedCallback) {
        return this.internalRequestOperation.deleteMultipleObject(request, completedCallback);
    }

    public DeleteMultipleObjectResult deleteMultipleObject(DeleteMultipleObjectRequest request) throws ClientException, ServiceException {
        return this.internalRequestOperation.deleteMultipleObject(request, (OSSCompletedCallback<DeleteMultipleObjectRequest, DeleteMultipleObjectResult>) null).getResult();
    }

    public OSSAsyncTask<AppendObjectResult> asyncAppendObject(AppendObjectRequest request, OSSCompletedCallback<AppendObjectRequest, AppendObjectResult> completedCallback) {
        return this.internalRequestOperation.appendObject(request, completedCallback);
    }

    public AppendObjectResult appendObject(AppendObjectRequest request) throws ClientException, ServiceException {
        return this.internalRequestOperation.syncAppendObject(request);
    }

    public OSSAsyncTask<HeadObjectResult> asyncHeadObject(HeadObjectRequest request, OSSCompletedCallback<HeadObjectRequest, HeadObjectResult> completedCallback) {
        return this.internalRequestOperation.headObject(request, completedCallback);
    }

    public HeadObjectResult headObject(HeadObjectRequest request) throws ClientException, ServiceException {
        return this.internalRequestOperation.headObject(request, (OSSCompletedCallback<HeadObjectRequest, HeadObjectResult>) null).getResult();
    }

    public OSSAsyncTask<CopyObjectResult> asyncCopyObject(CopyObjectRequest request, OSSCompletedCallback<CopyObjectRequest, CopyObjectResult> completedCallback) {
        return this.internalRequestOperation.copyObject(request, completedCallback);
    }

    public CopyObjectResult copyObject(CopyObjectRequest request) throws ClientException, ServiceException {
        return this.internalRequestOperation.copyObject(request, (OSSCompletedCallback<CopyObjectRequest, CopyObjectResult>) null).getResult();
    }

    public OSSAsyncTask<ListObjectsResult> asyncListObjects(ListObjectsRequest request, OSSCompletedCallback<ListObjectsRequest, ListObjectsResult> completedCallback) {
        return this.internalRequestOperation.listObjects(request, completedCallback);
    }

    public ListObjectsResult listObjects(ListObjectsRequest request) throws ClientException, ServiceException {
        return this.internalRequestOperation.listObjects(request, (OSSCompletedCallback<ListObjectsRequest, ListObjectsResult>) null).getResult();
    }

    public OSSAsyncTask<InitiateMultipartUploadResult> asyncInitMultipartUpload(InitiateMultipartUploadRequest request, OSSCompletedCallback<InitiateMultipartUploadRequest, InitiateMultipartUploadResult> completedCallback) {
        return this.internalRequestOperation.initMultipartUpload(request, completedCallback);
    }

    public InitiateMultipartUploadResult initMultipartUpload(InitiateMultipartUploadRequest request) throws ClientException, ServiceException {
        return this.internalRequestOperation.initMultipartUpload(request, (OSSCompletedCallback<InitiateMultipartUploadRequest, InitiateMultipartUploadResult>) null).getResult();
    }

    public OSSAsyncTask<UploadPartResult> asyncUploadPart(UploadPartRequest request, OSSCompletedCallback<UploadPartRequest, UploadPartResult> completedCallback) {
        return this.internalRequestOperation.uploadPart(request, completedCallback);
    }

    public UploadPartResult uploadPart(UploadPartRequest request) throws ClientException, ServiceException {
        return this.internalRequestOperation.syncUploadPart(request);
    }

    public OSSAsyncTask<CompleteMultipartUploadResult> asyncCompleteMultipartUpload(CompleteMultipartUploadRequest request, OSSCompletedCallback<CompleteMultipartUploadRequest, CompleteMultipartUploadResult> completedCallback) {
        return this.internalRequestOperation.completeMultipartUpload(request, completedCallback);
    }

    public CompleteMultipartUploadResult completeMultipartUpload(CompleteMultipartUploadRequest request) throws ClientException, ServiceException {
        return this.internalRequestOperation.syncCompleteMultipartUpload(request);
    }

    public OSSAsyncTask<AbortMultipartUploadResult> asyncAbortMultipartUpload(AbortMultipartUploadRequest request, OSSCompletedCallback<AbortMultipartUploadRequest, AbortMultipartUploadResult> completedCallback) {
        return this.internalRequestOperation.abortMultipartUpload(request, completedCallback);
    }

    public AbortMultipartUploadResult abortMultipartUpload(AbortMultipartUploadRequest request) throws ClientException, ServiceException {
        return this.internalRequestOperation.abortMultipartUpload(request, (OSSCompletedCallback<AbortMultipartUploadRequest, AbortMultipartUploadResult>) null).getResult();
    }

    public OSSAsyncTask<ListPartsResult> asyncListParts(ListPartsRequest request, OSSCompletedCallback<ListPartsRequest, ListPartsResult> completedCallback) {
        return this.internalRequestOperation.listParts(request, completedCallback);
    }

    public ListPartsResult listParts(ListPartsRequest request) throws ClientException, ServiceException {
        return this.internalRequestOperation.listParts(request, (OSSCompletedCallback<ListPartsRequest, ListPartsResult>) null).getResult();
    }

    public OSSAsyncTask<ListMultipartUploadsResult> asyncListMultipartUploads(ListMultipartUploadsRequest request, OSSCompletedCallback<ListMultipartUploadsRequest, ListMultipartUploadsResult> completedCallback) {
        return this.internalRequestOperation.listMultipartUploads(request, completedCallback);
    }

    public ListMultipartUploadsResult listMultipartUploads(ListMultipartUploadsRequest request) throws ClientException, ServiceException {
        return this.internalRequestOperation.listMultipartUploads(request, (OSSCompletedCallback<ListMultipartUploadsRequest, ListMultipartUploadsResult>) null).getResult();
    }

    public void updateCredentialProvider(OSSCredentialProvider credentialProvider2) {
        this.credentialProvider = credentialProvider2;
        this.internalRequestOperation.setCredentialProvider(credentialProvider2);
    }

    public OSSAsyncTask<CompleteMultipartUploadResult> asyncMultipartUpload(MultipartUploadRequest request, OSSCompletedCallback<MultipartUploadRequest, CompleteMultipartUploadResult> completedCallback) {
        return this.extensionRequestOperation.multipartUpload(request, completedCallback);
    }

    public CompleteMultipartUploadResult multipartUpload(MultipartUploadRequest request) throws ClientException, ServiceException {
        return this.extensionRequestOperation.multipartUpload(request, (OSSCompletedCallback<MultipartUploadRequest, CompleteMultipartUploadResult>) null).getResult();
    }

    public OSSAsyncTask<ResumableUploadResult> asyncResumableUpload(ResumableUploadRequest request, OSSCompletedCallback<ResumableUploadRequest, ResumableUploadResult> completedCallback) {
        return this.extensionRequestOperation.resumableUpload(request, completedCallback);
    }

    public ResumableUploadResult resumableUpload(ResumableUploadRequest request) throws ClientException, ServiceException {
        return this.extensionRequestOperation.resumableUpload(request, (OSSCompletedCallback<ResumableUploadRequest, ResumableUploadResult>) null).getResult();
    }

    public OSSAsyncTask<ResumableUploadResult> asyncSequenceUpload(ResumableUploadRequest request, OSSCompletedCallback<ResumableUploadRequest, ResumableUploadResult> completedCallback) {
        return this.extensionRequestOperation.sequenceUpload(request, completedCallback);
    }

    public ResumableUploadResult sequenceUpload(ResumableUploadRequest request) throws ClientException, ServiceException {
        return this.extensionRequestOperation.sequenceUpload(request, (OSSCompletedCallback<ResumableUploadRequest, ResumableUploadResult>) null).getResult();
    }

    public String presignConstrainedObjectURL(GeneratePresignedUrlRequest request) throws ClientException {
        return new ObjectURLPresigner(this.endpointURI, this.credentialProvider, this.conf).presignConstrainedURL(request);
    }

    public String presignConstrainedObjectURL(String bucketName, String objectKey, long expiredTimeInSeconds) throws ClientException {
        return new ObjectURLPresigner(this.endpointURI, this.credentialProvider, this.conf).presignConstrainedURL(bucketName, objectKey, expiredTimeInSeconds);
    }

    public String presignPublicObjectURL(String bucketName, String objectKey) {
        return new ObjectURLPresigner(this.endpointURI, this.credentialProvider, this.conf).presignPublicURL(bucketName, objectKey);
    }

    public boolean doesObjectExist(String bucketName, String objectKey) throws ClientException, ServiceException {
        return this.extensionRequestOperation.doesObjectExist(bucketName, objectKey);
    }

    public void abortResumableUpload(ResumableUploadRequest request) throws IOException {
        this.extensionRequestOperation.abortResumableUpload(request);
    }

    public OSSAsyncTask<TriggerCallbackResult> asyncTriggerCallback(TriggerCallbackRequest request, OSSCompletedCallback<TriggerCallbackRequest, TriggerCallbackResult> completedCallback) {
        return this.internalRequestOperation.triggerCallback(request, completedCallback);
    }

    public TriggerCallbackResult triggerCallback(TriggerCallbackRequest request) throws ClientException, ServiceException {
        return this.internalRequestOperation.asyncTriggerCallback(request);
    }

    public OSSAsyncTask<ImagePersistResult> asyncImagePersist(ImagePersistRequest request, OSSCompletedCallback<ImagePersistRequest, ImagePersistResult> completedCallback) {
        return this.internalRequestOperation.imageActionPersist(request, completedCallback);
    }

    public ImagePersistResult imagePersist(ImagePersistRequest request) throws ClientException, ServiceException {
        return this.internalRequestOperation.imageActionPersist(request, (OSSCompletedCallback<ImagePersistRequest, ImagePersistResult>) null).getResult();
    }

    public PutSymlinkResult putSymlink(PutSymlinkRequest request) throws ClientException, ServiceException {
        return this.internalRequestOperation.syncPutSymlink(request);
    }

    public OSSAsyncTask<PutSymlinkResult> asyncPutSymlink(PutSymlinkRequest request, OSSCompletedCallback<PutSymlinkRequest, PutSymlinkResult> completedCallback) {
        return this.internalRequestOperation.putSymlink(request, completedCallback);
    }

    public GetSymlinkResult getSymlink(GetSymlinkRequest request) throws ClientException, ServiceException {
        return this.internalRequestOperation.syncGetSymlink(request);
    }

    public OSSAsyncTask<GetSymlinkResult> asyncGetSymlink(GetSymlinkRequest request, OSSCompletedCallback<GetSymlinkRequest, GetSymlinkResult> completedCallback) {
        return this.internalRequestOperation.getSymlink(request, completedCallback);
    }

    public RestoreObjectResult restoreObject(RestoreObjectRequest request) throws ClientException, ServiceException {
        return this.internalRequestOperation.syncRestoreObject(request);
    }

    public OSSAsyncTask<RestoreObjectResult> asyncRestoreObject(RestoreObjectRequest request, OSSCompletedCallback<RestoreObjectRequest, RestoreObjectResult> completedCallback) {
        return this.internalRequestOperation.restoreObject(request, completedCallback);
    }
}
