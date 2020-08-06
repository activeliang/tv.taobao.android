package com.alibaba.sdk.android.oss.internal;

import android.text.TextUtils;
import android.util.Xml;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.OSSHeaders;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.utils.CRC64;
import com.alibaba.sdk.android.oss.common.utils.DateUtil;
import com.alibaba.sdk.android.oss.common.utils.HttpHeaders;
import com.alibaba.sdk.android.oss.common.utils.OSSUtils;
import com.alibaba.sdk.android.oss.model.AbortMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.AppendObjectResult;
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.CopyObjectResult;
import com.alibaba.sdk.android.oss.model.CreateBucketRequest;
import com.alibaba.sdk.android.oss.model.CreateBucketResult;
import com.alibaba.sdk.android.oss.model.DeleteBucketResult;
import com.alibaba.sdk.android.oss.model.DeleteMultipleObjectResult;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.GetBucketACLResult;
import com.alibaba.sdk.android.oss.model.GetBucketInfoResult;
import com.alibaba.sdk.android.oss.model.GetObjectACLResult;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.GetSymlinkResult;
import com.alibaba.sdk.android.oss.model.HeadObjectResult;
import com.alibaba.sdk.android.oss.model.ImagePersistResult;
import com.alibaba.sdk.android.oss.model.InitiateMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.ListBucketsResult;
import com.alibaba.sdk.android.oss.model.ListMultipartUploadsResult;
import com.alibaba.sdk.android.oss.model.ListObjectsResult;
import com.alibaba.sdk.android.oss.model.ListPartsResult;
import com.alibaba.sdk.android.oss.model.OSSBucketSummary;
import com.alibaba.sdk.android.oss.model.OSSObjectSummary;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.Owner;
import com.alibaba.sdk.android.oss.model.PartSummary;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.alibaba.sdk.android.oss.model.PutSymlinkResult;
import com.alibaba.sdk.android.oss.model.RestoreObjectResult;
import com.alibaba.sdk.android.oss.model.TriggerCallbackResult;
import com.alibaba.sdk.android.oss.model.UploadPartResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class ResponseParsers {
    /* access modifiers changed from: private */
    public static CopyObjectResult parseCopyObjectResponseXML(InputStream in, CopyObjectResult result) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in, "utf-8");
        int eventType = parser.getEventType();
        while (eventType != 1) {
            switch (eventType) {
                case 2:
                    String name = parser.getName();
                    if (!"LastModified".equals(name)) {
                        if (HttpHeaders.ETAG.equals(name)) {
                            result.setEtag(parser.nextText());
                            break;
                        }
                    } else {
                        result.setLastModified(DateUtil.parseIso8601Date(parser.nextText()));
                        break;
                    }
                    break;
            }
            eventType = parser.next();
            if (eventType == 4) {
                eventType = parser.next();
            }
        }
        return result;
    }

    /* access modifiers changed from: private */
    public static ListPartsResult parseListPartsResponseXML(InputStream in, ListPartsResult result) throws Exception {
        List<PartSummary> partEtagList = new ArrayList<>();
        PartSummary partSummary = null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in, "utf-8");
        int eventType = parser.getEventType();
        while (eventType != 1) {
            switch (eventType) {
                case 2:
                    String name = parser.getName();
                    if (!"Bucket".equals(name)) {
                        if (!"Key".equals(name)) {
                            if (!"UploadId".equals(name)) {
                                if (!"PartNumberMarker".equals(name)) {
                                    if (!"NextPartNumberMarker".equals(name)) {
                                        if (!"MaxParts".equals(name)) {
                                            if (!"IsTruncated".equals(name)) {
                                                if (!CreateBucketRequest.TAB_STORAGECLASS.equals(name)) {
                                                    if (!"Part".equals(name)) {
                                                        if (!"PartNumber".equals(name)) {
                                                            if (!"LastModified".equals(name)) {
                                                                if (!HttpHeaders.ETAG.equals(name)) {
                                                                    if ("Size".equals(name)) {
                                                                        String size = parser.nextText();
                                                                        if (!OSSUtils.isEmptyString(size)) {
                                                                            partSummary.setSize(Long.valueOf(size).longValue());
                                                                            break;
                                                                        }
                                                                    }
                                                                } else {
                                                                    partSummary.setETag(parser.nextText());
                                                                    break;
                                                                }
                                                            } else {
                                                                partSummary.setLastModified(DateUtil.parseIso8601Date(parser.nextText()));
                                                                break;
                                                            }
                                                        } else {
                                                            String partNum = parser.nextText();
                                                            if (!OSSUtils.isEmptyString(partNum)) {
                                                                partSummary.setPartNumber(Integer.valueOf(partNum).intValue());
                                                                break;
                                                            }
                                                        }
                                                    } else {
                                                        partSummary = new PartSummary();
                                                        break;
                                                    }
                                                } else {
                                                    result.setStorageClass(parser.nextText());
                                                    break;
                                                }
                                            } else {
                                                String isTruncated = parser.nextText();
                                                if (!OSSUtils.isEmptyString(isTruncated)) {
                                                    result.setTruncated(Boolean.valueOf(isTruncated).booleanValue());
                                                    break;
                                                }
                                            }
                                        } else {
                                            String maxParts = parser.nextText();
                                            if (!OSSUtils.isEmptyString(maxParts)) {
                                                result.setMaxParts(Integer.parseInt(maxParts));
                                                break;
                                            }
                                        }
                                    } else {
                                        String nextPartNumberMarker = parser.nextText();
                                        if (!OSSUtils.isEmptyString(nextPartNumberMarker)) {
                                            result.setNextPartNumberMarker(Integer.parseInt(nextPartNumberMarker));
                                            break;
                                        }
                                    }
                                } else {
                                    String partNumberMarker = parser.nextText();
                                    if (!OSSUtils.isEmptyString(partNumberMarker)) {
                                        result.setPartNumberMarker(Integer.parseInt(partNumberMarker));
                                        break;
                                    }
                                }
                            } else {
                                result.setUploadId(parser.nextText());
                                break;
                            }
                        } else {
                            result.setKey(parser.nextText());
                            break;
                        }
                    } else {
                        result.setBucketName(parser.nextText());
                        break;
                    }
                    break;
                case 3:
                    if ("Part".equals(parser.getName())) {
                        partEtagList.add(partSummary);
                        break;
                    }
                    break;
            }
            eventType = parser.next();
            if (eventType == 4) {
                eventType = parser.next();
            }
        }
        if (partEtagList.size() > 0) {
            result.setParts(partEtagList);
        }
        return result;
    }

    /* access modifiers changed from: private */
    public static CompleteMultipartUploadResult parseCompleteMultipartUploadResponseXML(InputStream in, CompleteMultipartUploadResult result) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in, "utf-8");
        int eventType = parser.getEventType();
        while (eventType != 1) {
            switch (eventType) {
                case 2:
                    String name = parser.getName();
                    if (!"Location".equals(name)) {
                        if (!"Bucket".equals(name)) {
                            if (!"Key".equals(name)) {
                                if (HttpHeaders.ETAG.equals(name)) {
                                    result.setETag(parser.nextText());
                                    break;
                                }
                            } else {
                                result.setObjectKey(parser.nextText());
                                break;
                            }
                        } else {
                            result.setBucketName(parser.nextText());
                            break;
                        }
                    } else {
                        result.setLocation(parser.nextText());
                        break;
                    }
                    break;
            }
            eventType = parser.next();
            if (eventType == 4) {
                eventType = parser.next();
            }
        }
        return result;
    }

    /* access modifiers changed from: private */
    public static InitiateMultipartUploadResult parseInitMultipartResponseXML(InputStream in, InitiateMultipartUploadResult result) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in, "utf-8");
        int eventType = parser.getEventType();
        while (eventType != 1) {
            switch (eventType) {
                case 2:
                    String name = parser.getName();
                    if (!"Bucket".equals(name)) {
                        if (!"Key".equals(name)) {
                            if ("UploadId".equals(name)) {
                                result.setUploadId(parser.nextText());
                                break;
                            }
                        } else {
                            result.setObjectKey(parser.nextText());
                            break;
                        }
                    } else {
                        result.setBucketName(parser.nextText());
                        break;
                    }
                    break;
            }
            eventType = parser.next();
            if (eventType == 4) {
                eventType = parser.next();
            }
        }
        return result;
    }

    /* access modifiers changed from: private */
    public static GetObjectACLResult parseGetObjectACLResponse(InputStream in, GetObjectACLResult result) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in, "utf-8");
        int eventType = parser.getEventType();
        while (eventType != 1) {
            switch (eventType) {
                case 2:
                    String name = parser.getName();
                    if (!"Grant".equals(name)) {
                        if (!"ID".equals(name)) {
                            if ("DisplayName".equals(name)) {
                                result.setObjectOwner(parser.nextText());
                                break;
                            }
                        } else {
                            result.setObjectOwnerID(parser.nextText());
                            break;
                        }
                    } else {
                        result.setObjectACL(parser.nextText());
                        break;
                    }
                    break;
            }
            eventType = parser.next();
            if (eventType == 4) {
                eventType = parser.next();
            }
        }
        return result;
    }

    /* access modifiers changed from: private */
    public static GetBucketInfoResult parseGetBucketInfoResponse(InputStream in, GetBucketInfoResult result) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in, "utf-8");
        int eventType = parser.getEventType();
        OSSBucketSummary bucket = null;
        Owner owner = null;
        while (eventType != 1) {
            switch (eventType) {
                case 2:
                    String name = parser.getName();
                    if (name != null) {
                        if (!"Owner".equals(name)) {
                            if (!"ID".equals(name)) {
                                if (!"DisplayName".equals(name)) {
                                    if (!"Bucket".equals(name)) {
                                        if (!"CreationDate".equals(name)) {
                                            if (!"ExtranetEndpoint".equals(name)) {
                                                if (!"IntranetEndpoint".equals(name)) {
                                                    if (!"Location".equals(name)) {
                                                        if (!"Name".equals(name)) {
                                                            if (!CreateBucketRequest.TAB_STORAGECLASS.equals(name)) {
                                                                if ("Grant".equals(name) && bucket != null) {
                                                                    bucket.setAcl(parser.nextText());
                                                                    break;
                                                                }
                                                            } else if (bucket != null) {
                                                                bucket.storageClass = parser.nextText();
                                                                break;
                                                            }
                                                        } else if (bucket != null) {
                                                            bucket.name = parser.nextText();
                                                            break;
                                                        }
                                                    } else if (bucket != null) {
                                                        bucket.location = parser.nextText();
                                                        break;
                                                    }
                                                } else if (bucket != null) {
                                                    bucket.intranetEndpoint = parser.nextText();
                                                    break;
                                                }
                                            } else if (bucket != null) {
                                                bucket.extranetEndpoint = parser.nextText();
                                                break;
                                            }
                                        } else if (bucket != null) {
                                            bucket.createDate = DateUtil.parseIso8601Date(parser.nextText());
                                            break;
                                        }
                                    } else {
                                        bucket = new OSSBucketSummary();
                                        break;
                                    }
                                } else if (owner != null) {
                                    owner.setDisplayName(parser.nextText());
                                    break;
                                }
                            } else if (owner != null) {
                                owner.setId(parser.nextText());
                                break;
                            }
                        } else {
                            owner = new Owner();
                            break;
                        }
                    }
                    break;
                case 3:
                    String endTagName = parser.getName();
                    if (endTagName != null) {
                        if (!"Bucket".equals(endTagName)) {
                            if ("Owner".equals(endTagName) && bucket != null) {
                                bucket.owner = owner;
                                break;
                            }
                        } else if (bucket != null) {
                            result.setBucket(bucket);
                            break;
                        }
                    }
                    break;
            }
            eventType = parser.next();
            if (eventType == 4) {
                eventType = parser.next();
            }
        }
        return result;
    }

    /* access modifiers changed from: private */
    public static GetBucketACLResult parseGetBucketACLResponse(InputStream in, GetBucketACLResult result) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in, "utf-8");
        int eventType = parser.getEventType();
        while (eventType != 1) {
            switch (eventType) {
                case 2:
                    String name = parser.getName();
                    if (!"Grant".equals(name)) {
                        if (!"ID".equals(name)) {
                            if ("DisplayName".equals(name)) {
                                result.setBucketOwner(parser.nextText());
                                break;
                            }
                        } else {
                            result.setBucketOwnerID(parser.nextText());
                            break;
                        }
                    } else {
                        result.setBucketACL(parser.nextText());
                        break;
                    }
                    break;
            }
            eventType = parser.next();
            if (eventType == 4) {
                eventType = parser.next();
            }
        }
        return result;
    }

    /* access modifiers changed from: private */
    public static DeleteMultipleObjectResult parseDeleteMultipleObjectResponse(InputStream in, DeleteMultipleObjectResult result) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in, "utf-8");
        int eventType = parser.getEventType();
        while (eventType != 1) {
            switch (eventType) {
                case 2:
                    if ("Key".equals(parser.getName())) {
                        result.addDeletedObject(parser.nextText());
                        break;
                    }
                    break;
            }
            eventType = parser.next();
            if (eventType == 4) {
                eventType = parser.next();
            }
        }
        return result;
    }

    /* access modifiers changed from: private */
    public static ListBucketsResult parseBucketListResponse(InputStream in, ListBucketsResult result) throws Exception {
        result.clearBucketList();
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in, "utf-8");
        int eventType = parser.getEventType();
        OSSBucketSummary bucket = null;
        while (eventType != 1) {
            switch (eventType) {
                case 2:
                    String name = parser.getName();
                    if (name != null) {
                        if (!"Prefix".equals(name)) {
                            if (!"Marker".equals(name)) {
                                if (!"MaxKeys".equals(name)) {
                                    if (!"IsTruncated".equals(name)) {
                                        if (!"NextMarker".equals(name)) {
                                            if (!"ID".equals(name)) {
                                                if (!"DisplayName".equals(name)) {
                                                    if (!"Bucket".equals(name)) {
                                                        if (!"CreationDate".equals(name)) {
                                                            if (!"ExtranetEndpoint".equals(name)) {
                                                                if (!"IntranetEndpoint".equals(name)) {
                                                                    if (!"Location".equals(name)) {
                                                                        if (!"Name".equals(name)) {
                                                                            if (CreateBucketRequest.TAB_STORAGECLASS.equals(name) && bucket != null) {
                                                                                bucket.storageClass = parser.nextText();
                                                                                break;
                                                                            }
                                                                        } else if (bucket != null) {
                                                                            bucket.name = parser.nextText();
                                                                            break;
                                                                        }
                                                                    } else if (bucket != null) {
                                                                        bucket.location = parser.nextText();
                                                                        break;
                                                                    }
                                                                } else if (bucket != null) {
                                                                    bucket.intranetEndpoint = parser.nextText();
                                                                    break;
                                                                }
                                                            } else if (bucket != null) {
                                                                bucket.extranetEndpoint = parser.nextText();
                                                                break;
                                                            }
                                                        } else if (bucket != null) {
                                                            bucket.createDate = DateUtil.parseIso8601Date(parser.nextText());
                                                            break;
                                                        }
                                                    } else {
                                                        bucket = new OSSBucketSummary();
                                                        break;
                                                    }
                                                } else {
                                                    result.setOwnerDisplayName(parser.nextText());
                                                    break;
                                                }
                                            } else {
                                                result.setOwnerId(parser.nextText());
                                                break;
                                            }
                                        } else {
                                            result.setNextMarker(parser.nextText());
                                            break;
                                        }
                                    } else {
                                        String isTruncated = parser.nextText();
                                        if (isTruncated != null) {
                                            result.setTruncated(Boolean.valueOf(isTruncated).booleanValue());
                                            break;
                                        }
                                    }
                                } else {
                                    String maxKeys = parser.nextText();
                                    if (maxKeys != null) {
                                        result.setMaxKeys(Integer.valueOf(maxKeys).intValue());
                                        break;
                                    }
                                }
                            } else {
                                result.setMarker(parser.nextText());
                                break;
                            }
                        } else {
                            result.setPrefix(parser.nextText());
                            break;
                        }
                    }
                    break;
                case 3:
                    if ("Bucket".equals(parser.getName()) && bucket != null) {
                        result.addBucket(bucket);
                        break;
                    }
            }
            eventType = parser.next();
            if (eventType == 4) {
                eventType = parser.next();
            }
        }
        return result;
    }

    /* access modifiers changed from: private */
    public static ListObjectsResult parseObjectListResponse(InputStream in, ListObjectsResult result) throws Exception {
        result.clearCommonPrefixes();
        result.clearObjectSummaries();
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in, "utf-8");
        int eventType = parser.getEventType();
        OSSObjectSummary object = null;
        Owner owner = null;
        boolean isCommonPrefixes = false;
        while (eventType != 1) {
            switch (eventType) {
                case 2:
                    String name = parser.getName();
                    if (!"Name".equals(name)) {
                        if (!"Prefix".equals(name)) {
                            if (!"Marker".equals(name)) {
                                if (!"Delimiter".equals(name)) {
                                    if (!"EncodingType".equals(name)) {
                                        if (!"MaxKeys".equals(name)) {
                                            if (!"NextMarker".equals(name)) {
                                                if (!"IsTruncated".equals(name)) {
                                                    if (!"Contents".equals(name)) {
                                                        if (!"Key".equals(name)) {
                                                            if (!"LastModified".equals(name)) {
                                                                if (!"Size".equals(name)) {
                                                                    if (!HttpHeaders.ETAG.equals(name)) {
                                                                        if (!"Type".equals(name)) {
                                                                            if (!CreateBucketRequest.TAB_STORAGECLASS.equals(name)) {
                                                                                if (!"Owner".equals(name)) {
                                                                                    if (!"ID".equals(name)) {
                                                                                        if (!"DisplayName".equals(name)) {
                                                                                            if ("CommonPrefixes".equals(name)) {
                                                                                                isCommonPrefixes = true;
                                                                                                break;
                                                                                            }
                                                                                        } else {
                                                                                            owner.setDisplayName(parser.nextText());
                                                                                            break;
                                                                                        }
                                                                                    } else {
                                                                                        owner.setId(parser.nextText());
                                                                                        break;
                                                                                    }
                                                                                } else {
                                                                                    owner = new Owner();
                                                                                    break;
                                                                                }
                                                                            } else {
                                                                                object.setStorageClass(parser.nextText());
                                                                                break;
                                                                            }
                                                                        } else {
                                                                            object.setType(parser.nextText());
                                                                            break;
                                                                        }
                                                                    } else {
                                                                        object.setETag(parser.nextText());
                                                                        break;
                                                                    }
                                                                } else {
                                                                    String size = parser.nextText();
                                                                    if (!OSSUtils.isEmptyString(size)) {
                                                                        object.setSize(Long.valueOf(size).longValue());
                                                                        break;
                                                                    }
                                                                }
                                                            } else {
                                                                object.setLastModified(DateUtil.parseIso8601Date(parser.nextText()));
                                                                break;
                                                            }
                                                        } else {
                                                            object.setKey(parser.nextText());
                                                            break;
                                                        }
                                                    } else {
                                                        object = new OSSObjectSummary();
                                                        break;
                                                    }
                                                } else {
                                                    String isTruncated = parser.nextText();
                                                    if (!OSSUtils.isEmptyString(isTruncated)) {
                                                        result.setTruncated(Boolean.valueOf(isTruncated).booleanValue());
                                                        break;
                                                    }
                                                }
                                            } else {
                                                result.setNextMarker(parser.nextText());
                                                break;
                                            }
                                        } else {
                                            String maxKeys = parser.nextText();
                                            if (!OSSUtils.isEmptyString(maxKeys)) {
                                                result.setMaxKeys(Integer.valueOf(maxKeys).intValue());
                                                break;
                                            }
                                        }
                                    } else {
                                        result.setEncodingType(parser.nextText());
                                        break;
                                    }
                                } else {
                                    result.setDelimiter(parser.nextText());
                                    break;
                                }
                            } else {
                                result.setMarker(parser.nextText());
                                break;
                            }
                        } else if (!isCommonPrefixes) {
                            result.setPrefix(parser.nextText());
                            break;
                        } else {
                            String commonPrefix = parser.nextText();
                            if (!OSSUtils.isEmptyString(commonPrefix)) {
                                result.addCommonPrefix(commonPrefix);
                                break;
                            }
                        }
                    } else {
                        result.setBucketName(parser.nextText());
                        break;
                    }
                    break;
                case 3:
                    String endTagName = parser.getName();
                    if (!"Owner".equals(parser.getName())) {
                        if (!"Contents".equals(endTagName)) {
                            if ("CommonPrefixes".equals(endTagName)) {
                                isCommonPrefixes = false;
                                break;
                            }
                        } else if (object != null) {
                            object.setBucketName(result.getBucketName());
                            result.addObjectSummary(object);
                            break;
                        }
                    } else if (owner != null) {
                        object.setOwner(owner);
                        break;
                    }
                    break;
            }
            eventType = parser.next();
            if (eventType == 4) {
                eventType = parser.next();
            }
        }
        return result;
    }

    public static String trimQuotes(String s) {
        if (s == null) {
            return null;
        }
        String s2 = s.trim();
        if (s2.startsWith("\"")) {
            s2 = s2.substring(1);
        }
        if (s2.endsWith("\"")) {
            return s2.substring(0, s2.length() - 1);
        }
        return s2;
    }

    public static ObjectMetadata parseObjectMetadata(Map<String, String> headers) throws Exception {
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            for (String key : headers.keySet()) {
                if (key.indexOf(OSSHeaders.OSS_USER_METADATA_PREFIX) >= 0) {
                    objectMetadata.addUserMetadata(key, headers.get(key));
                } else if (key.equalsIgnoreCase(HttpHeaders.LAST_MODIFIED) || key.equalsIgnoreCase("Date")) {
                    objectMetadata.setHeader(key, DateUtil.parseRfc822Date(headers.get(key)));
                } else if (key.equalsIgnoreCase("Content-Length")) {
                    objectMetadata.setHeader(key, Long.valueOf(headers.get(key)));
                } else if (key.equalsIgnoreCase(HttpHeaders.ETAG)) {
                    objectMetadata.setHeader(key, trimQuotes(headers.get(key)));
                } else {
                    objectMetadata.setHeader(key, headers.get(key));
                }
            }
            return objectMetadata;
        } catch (ParseException pe) {
            throw new IOException(pe.getMessage(), pe);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public static ServiceException parseResponseErrorXML(ResponseMessage response, boolean isHeadRequest) throws ClientException {
        int statusCode = response.getStatusCode();
        String requestId = response.getResponse().header(OSSHeaders.OSS_HEADER_REQUEST_ID);
        String code = null;
        String message = null;
        String hostId = null;
        String partNumber = null;
        String partEtag = null;
        String errorMessage = null;
        if (!isHeadRequest) {
            try {
                errorMessage = response.getResponse().body().string();
                OSSLog.logDebug("errorMessage  ：  \n " + errorMessage);
                InputStream inputStream = new ByteArrayInputStream(errorMessage.getBytes());
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(inputStream, "utf-8");
                int eventType = parser.getEventType();
                while (eventType != 1) {
                    switch (eventType) {
                        case 2:
                            if (!"Code".equals(parser.getName())) {
                                if (!"Message".equals(parser.getName())) {
                                    if (!"RequestId".equals(parser.getName())) {
                                        if (!"HostId".equals(parser.getName())) {
                                            if (!"PartNumber".equals(parser.getName())) {
                                                if ("PartEtag".equals(parser.getName())) {
                                                    partEtag = parser.nextText();
                                                    break;
                                                }
                                            } else {
                                                partNumber = parser.nextText();
                                                break;
                                            }
                                        } else {
                                            hostId = parser.nextText();
                                            break;
                                        }
                                    } else {
                                        requestId = parser.nextText();
                                        break;
                                    }
                                } else {
                                    message = parser.nextText();
                                    break;
                                }
                            } else {
                                code = parser.nextText();
                                break;
                            }
                            break;
                    }
                    eventType = parser.next();
                    if (eventType == 4) {
                        eventType = parser.next();
                    }
                }
            } catch (IOException e) {
                throw new ClientException((Throwable) e);
            } catch (XmlPullParserException e2) {
                throw new ClientException((Throwable) e2);
            }
        }
        ServiceException serviceException = new ServiceException(statusCode, message, code, requestId, hostId, errorMessage);
        if (!TextUtils.isEmpty(partEtag)) {
            serviceException.setPartEtag(partEtag);
        }
        if (!TextUtils.isEmpty(partNumber)) {
            serviceException.setPartNumber(partNumber);
        }
        return serviceException;
    }

    public static final class PutObjectResponseParser extends AbstractResponseParser<PutObjectResult> {
        public PutObjectResult parseData(ResponseMessage response, PutObjectResult result) throws IOException {
            result.setETag(ResponseParsers.trimQuotes((String) response.getHeaders().get(HttpHeaders.ETAG)));
            String body = response.getResponse().body().string();
            if (!TextUtils.isEmpty(body)) {
                result.setServerCallbackReturnBody(body);
            }
            return result;
        }
    }

    public static final class AppendObjectResponseParser extends AbstractResponseParser<AppendObjectResult> {
        public AppendObjectResult parseData(ResponseMessage response, AppendObjectResult result) throws IOException {
            String nextPosition = (String) response.getHeaders().get(OSSHeaders.OSS_NEXT_APPEND_POSITION);
            if (nextPosition != null) {
                result.setNextPosition(Long.valueOf(nextPosition));
            }
            result.setObjectCRC64((String) response.getHeaders().get(OSSHeaders.OSS_HASH_CRC64_ECMA));
            return result;
        }
    }

    public static final class HeadObjectResponseParser extends AbstractResponseParser<HeadObjectResult> {
        public HeadObjectResult parseData(ResponseMessage response, HeadObjectResult result) throws Exception {
            result.setMetadata(ResponseParsers.parseObjectMetadata(result.getResponseHeader()));
            return result;
        }
    }

    public static final class GetObjectResponseParser extends AbstractResponseParser<GetObjectResult> {
        public GetObjectResult parseData(ResponseMessage response, GetObjectResult result) throws Exception {
            result.setMetadata(ResponseParsers.parseObjectMetadata(result.getResponseHeader()));
            result.setContentLength(response.getContentLength());
            if (response.getRequest().isCheckCRC64()) {
                result.setObjectContent(new CheckCRC64DownloadInputStream(response.getContent(), new CRC64(), response.getContentLength(), result.getServerCRC().longValue(), result.getRequestId()));
            } else {
                result.setObjectContent(response.getContent());
            }
            return result;
        }

        public boolean needCloseResponse() {
            return false;
        }
    }

    public static final class GetObjectACLResponseParser extends AbstractResponseParser<GetObjectACLResult> {
        /* access modifiers changed from: package-private */
        public GetObjectACLResult parseData(ResponseMessage response, GetObjectACLResult result) throws Exception {
            return ResponseParsers.parseGetObjectACLResponse(response.getContent(), result);
        }
    }

    public static final class CopyObjectResponseParser extends AbstractResponseParser<CopyObjectResult> {
        public CopyObjectResult parseData(ResponseMessage response, CopyObjectResult result) throws Exception {
            return ResponseParsers.parseCopyObjectResponseXML(response.getContent(), result);
        }
    }

    public static final class CreateBucketResponseParser extends AbstractResponseParser<CreateBucketResult> {
        public CreateBucketResult parseData(ResponseMessage response, CreateBucketResult result) throws Exception {
            if (result.getResponseHeader().containsKey("Location")) {
                result.bucketLocation = result.getResponseHeader().get("Location");
            }
            return result;
        }
    }

    public static final class DeleteBucketResponseParser extends AbstractResponseParser<DeleteBucketResult> {
        public DeleteBucketResult parseData(ResponseMessage response, DeleteBucketResult result) throws Exception {
            return result;
        }
    }

    public static final class GetBucketInfoResponseParser extends AbstractResponseParser<GetBucketInfoResult> {
        public GetBucketInfoResult parseData(ResponseMessage response, GetBucketInfoResult result) throws Exception {
            return ResponseParsers.parseGetBucketInfoResponse(response.getContent(), result);
        }
    }

    public static final class GetBucketACLResponseParser extends AbstractResponseParser<GetBucketACLResult> {
        public GetBucketACLResult parseData(ResponseMessage response, GetBucketACLResult result) throws Exception {
            return ResponseParsers.parseGetBucketACLResponse(response.getContent(), result);
        }
    }

    public static final class DeleteObjectResponseParser extends AbstractResponseParser<DeleteObjectResult> {
        public DeleteObjectResult parseData(ResponseMessage response, DeleteObjectResult result) throws Exception {
            return result;
        }
    }

    public static final class DeleteMultipleObjectResponseParser extends AbstractResponseParser<DeleteMultipleObjectResult> {
        /* access modifiers changed from: package-private */
        public DeleteMultipleObjectResult parseData(ResponseMessage response, DeleteMultipleObjectResult result) throws Exception {
            return ResponseParsers.parseDeleteMultipleObjectResponse(response.getContent(), result);
        }
    }

    public static final class ListObjectsResponseParser extends AbstractResponseParser<ListObjectsResult> {
        public ListObjectsResult parseData(ResponseMessage response, ListObjectsResult result) throws Exception {
            return ResponseParsers.parseObjectListResponse(response.getContent(), result);
        }
    }

    public static final class ListBucketResponseParser extends AbstractResponseParser<ListBucketsResult> {
        /* access modifiers changed from: package-private */
        public ListBucketsResult parseData(ResponseMessage response, ListBucketsResult result) throws Exception {
            return ResponseParsers.parseBucketListResponse(response.getContent(), result);
        }
    }

    public static final class InitMultipartResponseParser extends AbstractResponseParser<InitiateMultipartUploadResult> {
        public InitiateMultipartUploadResult parseData(ResponseMessage response, InitiateMultipartUploadResult result) throws Exception {
            return ResponseParsers.parseInitMultipartResponseXML(response.getContent(), result);
        }
    }

    public static final class UploadPartResponseParser extends AbstractResponseParser<UploadPartResult> {
        public UploadPartResult parseData(ResponseMessage response, UploadPartResult result) throws Exception {
            result.setETag(ResponseParsers.trimQuotes((String) response.getHeaders().get(HttpHeaders.ETAG)));
            return result;
        }
    }

    public static final class AbortMultipartUploadResponseParser extends AbstractResponseParser<AbortMultipartUploadResult> {
        public AbortMultipartUploadResult parseData(ResponseMessage response, AbortMultipartUploadResult result) throws Exception {
            return result;
        }
    }

    public static final class CompleteMultipartUploadResponseParser extends AbstractResponseParser<CompleteMultipartUploadResult> {
        public CompleteMultipartUploadResult parseData(ResponseMessage response, CompleteMultipartUploadResult result) throws Exception {
            if (((String) response.getHeaders().get("Content-Type")).equals("application/xml")) {
                return ResponseParsers.parseCompleteMultipartUploadResponseXML(response.getContent(), result);
            }
            String body = response.getResponse().body().string();
            if (TextUtils.isEmpty(body)) {
                return result;
            }
            result.setServerCallbackReturnBody(body);
            return result;
        }
    }

    public static final class ListPartsResponseParser extends AbstractResponseParser<ListPartsResult> {
        public ListPartsResult parseData(ResponseMessage response, ListPartsResult result) throws Exception {
            return ResponseParsers.parseListPartsResponseXML(response.getContent(), result);
        }
    }

    public static final class ListMultipartUploadsResponseParser extends AbstractResponseParser<ListMultipartUploadsResult> {
        public ListMultipartUploadsResult parseData(ResponseMessage response, ListMultipartUploadsResult result) throws Exception {
            return result.parseData(response);
        }
    }

    public static final class TriggerCallbackResponseParser extends AbstractResponseParser<TriggerCallbackResult> {
        public TriggerCallbackResult parseData(ResponseMessage response, TriggerCallbackResult result) throws Exception {
            String body = response.getResponse().body().string();
            if (!TextUtils.isEmpty(body)) {
                result.setServerCallbackReturnBody(body);
            }
            return result;
        }
    }

    public static final class ImagePersistResponseParser extends AbstractResponseParser<ImagePersistResult> {
        public ImagePersistResult parseData(ResponseMessage response, ImagePersistResult result) throws Exception {
            return result;
        }
    }

    public static final class PutSymlinkResponseParser extends AbstractResponseParser<PutSymlinkResult> {
        /* access modifiers changed from: package-private */
        public PutSymlinkResult parseData(ResponseMessage response, PutSymlinkResult result) throws Exception {
            return result;
        }
    }

    public static final class GetSymlinkResponseParser extends AbstractResponseParser<GetSymlinkResult> {
        /* access modifiers changed from: package-private */
        public GetSymlinkResult parseData(ResponseMessage response, GetSymlinkResult result) throws Exception {
            result.setTargetObjectName((String) response.getHeaders().get(OSSHeaders.OSS_HEADER_SYMLINK_TARGET));
            return result;
        }
    }

    public static final class RestoreObjectResponseParser extends AbstractResponseParser<RestoreObjectResult> {
        /* access modifiers changed from: package-private */
        public RestoreObjectResult parseData(ResponseMessage response, RestoreObjectResult result) throws Exception {
            return result;
        }
    }
}
