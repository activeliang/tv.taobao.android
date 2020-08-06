package com.taobao.orange;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.text.TextUtils;
import java.util.Arrays;

public final class OConfig implements Parcelable {
    public static final Parcelable.Creator<OConfig> CREATOR = new Parcelable.Creator<OConfig>() {
        public OConfig createFromParcel(Parcel in) {
            return new OConfig(in);
        }

        public OConfig[] newArray(int size) {
            return new OConfig[size];
        }
    };
    String ackHost;
    String[] ackVips;
    String appKey;
    String appSecret;
    String appVersion;
    String authCode;
    String dcHost;
    String[] dcVips;
    int env;
    int indexUpdateMode;
    String[] probeHosts;
    int serverType;
    String userId;

    private OConfig() {
    }

    protected OConfig(Parcel in) {
        this.env = in.readInt();
        this.appKey = in.readString();
        this.appVersion = in.readString();
        this.appSecret = in.readString();
        this.authCode = in.readString();
        this.userId = in.readString();
        this.serverType = in.readInt();
        this.indexUpdateMode = in.readInt();
        this.probeHosts = in.createStringArray();
        this.dcHost = in.readString();
        this.dcVips = in.createStringArray();
        this.ackHost = in.readString();
        this.ackVips = in.createStringArray();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.env);
        parcel.writeString(this.appKey);
        parcel.writeString(this.appVersion);
        parcel.writeString(this.appSecret);
        parcel.writeString(this.authCode);
        parcel.writeString(this.userId);
        parcel.writeInt(this.serverType);
        parcel.writeInt(this.indexUpdateMode);
        parcel.writeStringArray(this.probeHosts);
        parcel.writeString(this.dcHost);
        parcel.writeStringArray(this.dcVips);
        parcel.writeString(this.ackHost);
        parcel.writeStringArray(this.ackVips);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        sb.append("env=").append(this.env);
        sb.append(", appKey='").append(this.appKey).append('\'');
        sb.append(", appVersion='").append(this.appVersion).append('\'');
        sb.append(", appSecret='").append(TextUtils.isEmpty(this.appSecret) ? this.appSecret : "****").append('\'');
        sb.append(", authCode='").append(this.authCode).append('\'');
        sb.append(", userId='").append(this.userId).append('\'');
        sb.append(", serverType=").append(this.serverType);
        sb.append(", indexUpdateMode=").append(this.indexUpdateMode);
        sb.append(", probeHosts=").append(Arrays.toString(this.probeHosts));
        sb.append(", dcHost='").append(this.dcHost).append('\'');
        sb.append(", dcVips='").append(Arrays.toString(this.dcVips)).append('\'');
        sb.append(", ackHost='").append(this.ackHost).append('\'');
        sb.append(", ackVips='").append(Arrays.toString(this.ackVips)).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static class Builder {
        String ackHost;
        String[] ackVips;
        private String appKey;
        private String appSecret;
        private String appVersion;
        private String authCode;
        String dcHost;
        String[] dcVips;
        private int env;
        private int indexUpdateMode;
        private String[] probeHosts;
        private int serverType;
        private String userId;

        public Builder setEnv(@IntRange(from = 0, to = 2) int env2) {
            this.env = env2;
            return this;
        }

        public Builder setAppKey(@NonNull String appKey2) {
            this.appKey = appKey2;
            return this;
        }

        public Builder setAppVersion(@NonNull String appVersion2) {
            this.appVersion = appVersion2;
            return this;
        }

        public Builder setAppSecret(@NonNull String appSecret2) {
            this.appSecret = appSecret2;
            return this;
        }

        public Builder setAuthCode(@NonNull String authCode2) {
            this.authCode = authCode2;
            return this;
        }

        public Builder setUserId(@Nullable String userId2) {
            this.userId = userId2;
            return this;
        }

        public Builder setServerType(@IntRange(from = 0, to = 1) int serverType2) {
            this.serverType = serverType2;
            return this;
        }

        public Builder setIndexUpdateMode(@IntRange(from = 0, to = 2) int indexUpdateMode2) {
            this.indexUpdateMode = indexUpdateMode2;
            return this;
        }

        public Builder setProbeHosts(@Size(min = 1) String[] probeHosts2) {
            this.probeHosts = probeHosts2;
            return this;
        }

        @Deprecated
        public Builder setOnlineHost(String onlineHost) {
            this.dcHost = onlineHost;
            return this;
        }

        @Deprecated
        public Builder setOnlineAckHost(String onlineAckHost) {
            this.ackHost = onlineAckHost;
            return this;
        }

        public Builder setDcHost(@NonNull String dcHost2) {
            this.dcHost = dcHost2;
            return this;
        }

        public Builder setDcVips(@Size(min = 1) String[] dcVips2) {
            this.dcVips = dcVips2;
            return this;
        }

        public Builder setAckHost(@NonNull String ackHost2) {
            this.ackHost = ackHost2;
            return this;
        }

        public Builder setAckVips(@Size(min = 1) String[] ackVips2) {
            this.ackVips = ackVips2;
            return this;
        }

        public OConfig build() {
            OConfig config = new OConfig();
            config.env = this.env;
            config.appKey = this.appKey;
            config.appSecret = this.appSecret;
            config.authCode = this.authCode;
            config.userId = this.userId;
            config.appVersion = this.appVersion;
            config.serverType = this.serverType;
            config.indexUpdateMode = this.indexUpdateMode;
            config.probeHosts = this.probeHosts;
            config.dcHost = this.dcHost;
            config.dcVips = this.dcVips;
            config.ackHost = this.ackHost;
            config.ackVips = this.ackVips;
            return config;
        }
    }
}
