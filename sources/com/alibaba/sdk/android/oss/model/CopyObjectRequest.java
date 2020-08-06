package com.alibaba.sdk.android.oss.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CopyObjectRequest extends OSSRequest {
    private String destinationBucketName;
    private String destinationKey;
    private List<String> matchingETagConstraints = new ArrayList();
    private Date modifiedSinceConstraint;
    private ObjectMetadata newObjectMetadata;
    private List<String> nonmatchingEtagConstraints = new ArrayList();
    private String serverSideEncryption;
    private String sourceBucketName;
    private String sourceKey;
    private Date unmodifiedSinceConstraint;

    public CopyObjectRequest(String sourceBucketName2, String sourceKey2, String destinationBucketName2, String destinationKey2) {
        setSourceBucketName(sourceBucketName2);
        setSourceKey(sourceKey2);
        setDestinationBucketName(destinationBucketName2);
        setDestinationKey(destinationKey2);
    }

    public String getSourceBucketName() {
        return this.sourceBucketName;
    }

    public void setSourceBucketName(String sourceBucketName2) {
        this.sourceBucketName = sourceBucketName2;
    }

    public String getSourceKey() {
        return this.sourceKey;
    }

    public void setSourceKey(String sourceKey2) {
        this.sourceKey = sourceKey2;
    }

    public String getDestinationBucketName() {
        return this.destinationBucketName;
    }

    public void setDestinationBucketName(String destinationBucketName2) {
        this.destinationBucketName = destinationBucketName2;
    }

    public String getDestinationKey() {
        return this.destinationKey;
    }

    public void setDestinationKey(String destinationKey2) {
        this.destinationKey = destinationKey2;
    }

    public ObjectMetadata getNewObjectMetadata() {
        return this.newObjectMetadata;
    }

    public void setNewObjectMetadata(ObjectMetadata newObjectMetadata2) {
        this.newObjectMetadata = newObjectMetadata2;
    }

    public List<String> getMatchingETagConstraints() {
        return this.matchingETagConstraints;
    }

    public void setMatchingETagConstraints(List<String> matchingETagConstraints2) {
        this.matchingETagConstraints.clear();
        if (matchingETagConstraints2 != null && !matchingETagConstraints2.isEmpty()) {
            this.matchingETagConstraints.addAll(matchingETagConstraints2);
        }
    }

    public void clearMatchingETagConstraints() {
        this.matchingETagConstraints.clear();
    }

    public List<String> getNonmatchingEtagConstraints() {
        return this.nonmatchingEtagConstraints;
    }

    public void setNonmatchingETagConstraints(List<String> nonmatchingEtagConstraints2) {
        this.nonmatchingEtagConstraints.clear();
        if (nonmatchingEtagConstraints2 != null && !nonmatchingEtagConstraints2.isEmpty()) {
            this.nonmatchingEtagConstraints.addAll(nonmatchingEtagConstraints2);
        }
    }

    public void clearNonmatchingETagConstraints() {
        this.nonmatchingEtagConstraints.clear();
    }

    public Date getUnmodifiedSinceConstraint() {
        return this.unmodifiedSinceConstraint;
    }

    public void setUnmodifiedSinceConstraint(Date unmodifiedSinceConstraint2) {
        this.unmodifiedSinceConstraint = unmodifiedSinceConstraint2;
    }

    public Date getModifiedSinceConstraint() {
        return this.modifiedSinceConstraint;
    }

    public void setModifiedSinceConstraint(Date modifiedSinceConstraint2) {
        this.modifiedSinceConstraint = modifiedSinceConstraint2;
    }

    public String getServerSideEncryption() {
        return this.serverSideEncryption;
    }

    public void setServerSideEncryption(String serverSideEncryption2) {
        this.serverSideEncryption = serverSideEncryption2;
    }
}
