package com.alibaba.analytics.core.model;

import android.text.TextUtils;
import com.alibaba.analytics.core.db.Entity;
import com.alibaba.analytics.core.db.annotation.Column;
import com.alibaba.analytics.core.db.annotation.Ingore;
import com.alibaba.analytics.core.db.annotation.TableName;
import com.alibaba.analytics.core.logbuilder.LogAssemble;
import com.alibaba.analytics.core.logbuilder.LogPriorityMgr;
import com.alibaba.analytics.core.logbuilder.SessionTimeAndIndexMgr;
import com.alibaba.analytics.utils.Base64;
import com.alibaba.analytics.utils.RC4;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@TableName("log")
public class Log extends Entity {
    @Ingore
    public static final String DEFAULT_PRIORITY = "3";
    @Ingore
    private static final int EVENTID_INTERVAL = 800000;
    @Ingore
    public static final String FIELD_NAME_PRIORITY = "priority";
    @Ingore
    public static final String FIELD_NAME_TIME = "time";
    @Ingore
    private static final String TAG = "UTLog";
    @Column("_index")
    public String _index = "";
    @Ingore
    private String arg1;
    @Ingore
    private String arg2;
    @Ingore
    private String arg3;
    @Ingore
    private Map<String, String> args;
    @Column("content")
    private String content;
    @Column("eventId")
    public String eventId;
    @Ingore
    private String page;
    @Column("priority")
    public String priority = "3";
    @Column("streamId")
    public String streamId;
    @Column("time")
    public String time = null;
    @Ingore
    private int topicId = 0;

    public Log() {
    }

    public Log(String priority2, List<String> streamId2, String eventId2, Map<String, String> contentMap) {
        this.priority = priority2;
        this.streamId = buildStreamId(streamId2);
        this.eventId = eventId2;
        this.time = String.valueOf(System.currentTimeMillis());
        this._index = createIndex();
        contentMap.put(LogField.RESERVE3.toString(), this._index);
        setContent(LogAssemble.assemble(contentMap));
    }

    public Log(String page2, String eventId2, String arg12, String arg22, String arg32, Map<String, String> args2) {
        this.eventId = eventId2;
        this.page = page2;
        this.arg1 = arg12;
        this.arg2 = arg22;
        this.arg3 = arg32;
        this.args = args2;
        this.time = String.valueOf(System.currentTimeMillis());
        this._index = createIndex();
        this.priority = LogPriorityMgr.getInstance().getLogLevel(eventId2);
        buildLogContent();
    }

    public String getContent() {
        try {
            byte[] lBContent = Base64.decode(this.content.getBytes("UTF-8"), 2);
            if (lBContent != null) {
                return new String(RC4.rc4(lBContent));
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Deprecated
    public String getPlus80WLogContent() {
        return addEventIdInterval(getContent());
    }

    @Deprecated
    private String addEventIdInterval(String rawLogContent) {
        Map<String, String> logMap = LogAssemble.disassemble(rawLogContent);
        logMap.put(LogField.EVENTID.toString(), (LogAssemble.getEventId(logMap) + EVENTID_INTERVAL) + "");
        return LogAssemble.assembleWithFullFields(logMap);
    }

    @Deprecated
    public String getCipherContent() {
        return this.content;
    }

    public void setContent(String content2) {
        if (content2 != null) {
            try {
                this.content = new String(Base64.encode(RC4.rc4(content2.getBytes()), 2), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @Deprecated
    public void setCipherContent(String cipherContent) {
        this.content = cipherContent;
    }

    public String toString() {
        return "Log [eventId=" + this.eventId + ", index=" + this._index + "]";
    }

    public void buildLogContent() {
        if (TextUtils.isEmpty(this.time)) {
            this.time = String.valueOf(System.currentTimeMillis());
        }
        setContent(LogAssemble.assemble(this.page, this.eventId, this.arg1, this.arg2, this.arg3, this.args, this._index, this.time));
    }

    private String buildStreamId(List<String> streamId2) {
        if (streamId2 == null) {
            return "";
        }
        if (streamId2.size() == 1) {
            return streamId2.get(0);
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < streamId2.size(); i++) {
            if (i >= 0) {
                builder.append(",");
            }
            builder.append(streamId2.get(i));
        }
        return builder.toString();
    }

    private String createIndex() {
        String startTime = "";
        String sesstionTime = SessionTimeAndIndexMgr.getInstance().getSessionTimestamp() + "";
        if (!TextUtils.isEmpty(sesstionTime)) {
            if (sesstionTime.length() >= 2) {
                startTime = sesstionTime.substring(sesstionTime.length() - 2, sesstionTime.length());
            } else {
                startTime = sesstionTime;
            }
        }
        if ("2202".equalsIgnoreCase(this.eventId)) {
            return String.format("%s%06d,2202_%06d", new Object[]{startTime, Long.valueOf(SessionTimeAndIndexMgr.getInstance().logIndexIncrementAndGet()), Long.valueOf(SessionTimeAndIndexMgr.getInstance().m2202LogIndexIncrementAndGet())});
        }
        return String.format("%s%06d", new Object[]{startTime, Long.valueOf(SessionTimeAndIndexMgr.getInstance().logIndexIncrementAndGet())});
    }

    public int getTopicId() {
        return this.topicId;
    }

    public void setTopicId(int topicid) {
        this.topicId = topicid;
    }
}
