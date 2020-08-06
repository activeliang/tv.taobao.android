package com.alibaba.sdk.android.oss.model;

import android.util.Xml;
import com.alibaba.sdk.android.oss.common.utils.DateUtil;
import com.alibaba.sdk.android.oss.common.utils.OSSUtils;
import com.alibaba.sdk.android.oss.internal.ResponseMessage;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public class ListMultipartUploadsResult extends OSSResult {
    private String bucketName;
    private List<String> commonPrefixes = new ArrayList();
    private String delimiter;
    private boolean isTruncated;
    private String keyMarker;
    private int maxUploads;
    private List<MultipartUpload> multipartUploads = new ArrayList();
    private String nextKeyMarker;
    private String nextUploadIdMarker;
    private String prefix;
    private String uploadIdMarker;

    public String getBucketName() {
        return this.bucketName;
    }

    public void setBucketName(String bucketName2) {
        this.bucketName = bucketName2;
    }

    public String getKeyMarker() {
        return this.keyMarker;
    }

    public void setKeyMarker(String keyMarker2) {
        this.keyMarker = keyMarker2;
    }

    public String getUploadIdMarker() {
        return this.uploadIdMarker;
    }

    public void setUploadIdMarker(String uploadIdMarker2) {
        this.uploadIdMarker = uploadIdMarker2;
    }

    public String getNextKeyMarker() {
        return this.nextKeyMarker;
    }

    public void setNextKeyMarker(String nextKeyMarker2) {
        this.nextKeyMarker = nextKeyMarker2;
    }

    public String getNextUploadIdMarker() {
        return this.nextUploadIdMarker;
    }

    public void setNextUploadIdMarker(String nextUploadIdMarker2) {
        this.nextUploadIdMarker = nextUploadIdMarker2;
    }

    public int getMaxUploads() {
        return this.maxUploads;
    }

    public void setMaxUploads(int maxUploads2) {
        this.maxUploads = maxUploads2;
    }

    public boolean isTruncated() {
        return this.isTruncated;
    }

    public void setTruncated(boolean isTruncated2) {
        this.isTruncated = isTruncated2;
    }

    public List<MultipartUpload> getMultipartUploads() {
        return this.multipartUploads;
    }

    public void setMultipartUploads(List<MultipartUpload> multipartUploads2) {
        this.multipartUploads.clear();
        if (multipartUploads2 != null && !multipartUploads2.isEmpty()) {
            this.multipartUploads.addAll(multipartUploads2);
        }
    }

    public void addMultipartUpload(MultipartUpload multipartUpload) {
        this.multipartUploads.add(multipartUpload);
    }

    public String getDelimiter() {
        return this.delimiter;
    }

    public void setDelimiter(String delimiter2) {
        this.delimiter = delimiter2;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix2) {
        this.prefix = prefix2;
    }

    public List<String> getCommonPrefixes() {
        return this.commonPrefixes;
    }

    public void setCommonPrefixes(List<String> commonPrefixes2) {
        this.commonPrefixes.clear();
        if (commonPrefixes2 != null && !commonPrefixes2.isEmpty()) {
            this.commonPrefixes.addAll(commonPrefixes2);
        }
    }

    public void addCommonPrefix(String commonPrefix) {
        this.commonPrefixes.add(commonPrefix);
    }

    public ListMultipartUploadsResult parseData(ResponseMessage responseMessage) throws Exception {
        List<MultipartUpload> uploadList = new ArrayList<>();
        MultipartUpload upload = null;
        boolean isCommonPrefixes = false;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(responseMessage.getContent(), "utf-8");
        int eventType = parser.getEventType();
        while (eventType != 1) {
            switch (eventType) {
                case 2:
                    String name = parser.getName();
                    if (!"Bucket".equals(name)) {
                        if (!"Delimiter".equals(name)) {
                            if (!"Prefix".equals(name)) {
                                if (!"MaxUploads".equals(name)) {
                                    if (!"IsTruncated".equals(name)) {
                                        if (!"KeyMarker".equals(name)) {
                                            if (!"UploadIdMarker".equals(name)) {
                                                if (!"NextKeyMarker".equals(name)) {
                                                    if (!"NextUploadIdMarker".equals(name)) {
                                                        if (!"Upload".equals(name)) {
                                                            if (!"Key".equals(name)) {
                                                                if (!"UploadId".equals(name)) {
                                                                    if (!"Initiated".equals(name)) {
                                                                        if (!CreateBucketRequest.TAB_STORAGECLASS.equals(name)) {
                                                                            if ("CommonPrefixes".equals(name)) {
                                                                                isCommonPrefixes = true;
                                                                                break;
                                                                            }
                                                                        } else {
                                                                            upload.setStorageClass(parser.nextText());
                                                                            break;
                                                                        }
                                                                    } else {
                                                                        upload.setInitiated(DateUtil.parseIso8601Date(parser.nextText()));
                                                                        break;
                                                                    }
                                                                } else {
                                                                    upload.setUploadId(parser.nextText());
                                                                    break;
                                                                }
                                                            } else {
                                                                upload.setKey(parser.nextText());
                                                                break;
                                                            }
                                                        } else {
                                                            upload = new MultipartUpload();
                                                            break;
                                                        }
                                                    } else {
                                                        setNextUploadIdMarker(parser.nextText());
                                                        break;
                                                    }
                                                } else {
                                                    setNextKeyMarker(parser.nextText());
                                                    break;
                                                }
                                            } else {
                                                setUploadIdMarker(parser.nextText());
                                                break;
                                            }
                                        } else {
                                            setKeyMarker(parser.nextText());
                                            break;
                                        }
                                    } else {
                                        String isTruncated2 = parser.nextText();
                                        if (!OSSUtils.isEmptyString(isTruncated2)) {
                                            setTruncated(Boolean.valueOf(isTruncated2).booleanValue());
                                            break;
                                        }
                                    }
                                } else {
                                    String maxUploads2 = parser.nextText();
                                    if (!OSSUtils.isEmptyString(maxUploads2)) {
                                        setMaxUploads(Integer.valueOf(maxUploads2).intValue());
                                        break;
                                    }
                                }
                            } else if (!isCommonPrefixes) {
                                setPrefix(parser.nextText());
                                break;
                            } else {
                                String commonPrefix = parser.nextText();
                                if (!OSSUtils.isEmptyString(commonPrefix)) {
                                    addCommonPrefix(commonPrefix);
                                    break;
                                }
                            }
                        } else {
                            setDelimiter(parser.nextText());
                            break;
                        }
                    } else {
                        setBucketName(parser.nextText());
                        break;
                    }
                    break;
                case 3:
                    if (!"Upload".equals(parser.getName())) {
                        if ("CommonPrefixes".equals(parser.getName())) {
                            isCommonPrefixes = false;
                            break;
                        }
                    } else {
                        uploadList.add(upload);
                        break;
                    }
                    break;
            }
            eventType = parser.next();
            if (eventType == 4) {
                eventType = parser.next();
            }
        }
        if (uploadList.size() > 0) {
            setMultipartUploads(uploadList);
        }
        return this;
    }
}
