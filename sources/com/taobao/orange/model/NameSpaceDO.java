package com.taobao.orange.model;

import com.ali.auth.third.offline.login.LoginConstants;
import com.taobao.orange.util.OLog;
import com.taobao.orange.util.OrangeUtils;
import java.io.Serializable;
import java.util.List;

public class NameSpaceDO implements Serializable {
    public static final String LEVEL_DEFAULT = "DEFAULT";
    public static final String LEVEL_HIGH = "HIGH";
    private static final String TAG = "NameSpaceDO";
    public static final String TYPE_CUSTOM = "CUSTOM";
    public static final String TYPE_STANDARD = "STANDARD";
    public List<CandidateDO> candidates;
    public transient CandidateDO curCandidateDO;
    public transient boolean hasChanged;
    public String loadLevel;
    public String md5;
    public String name;
    public String resourceId;
    public String type;
    public String version;

    public boolean checkValid(ConfigDO localConfigDO) {
        long baseConfigVersion;
        long localConfigVersion = localConfigDO == null ? 0 : OrangeUtils.parseLong(localConfigDO.getCurVersion());
        boolean localConfigIsAb = (localConfigDO == null || localConfigDO.candidate == null) ? false : true;
        if (localConfigDO == null || localConfigIsAb) {
            baseConfigVersion = 0;
        } else {
            baseConfigVersion = OrangeUtils.parseLong(localConfigDO.version);
        }
        long namespaceVersion = OrangeUtils.parseLong(this.version);
        if (this.candidates != null && !this.candidates.isEmpty()) {
            if (OLog.isPrintLog(0)) {
                OLog.v(TAG, "checkCandidates start", LoginConstants.CONFIG, this.name, "candidates.size", Integer.valueOf(this.candidates.size()));
            }
            int i = 0;
            while (i < this.candidates.size()) {
                CandidateDO candidateDO = this.candidates.get(i);
                if (OLog.isPrintLog(0)) {
                    OLog.v(TAG, "checkCandidate start", "index", Integer.valueOf(i), candidateDO);
                }
                if (!candidateDO.checkValid() || !candidateDO.checkMatch()) {
                    i++;
                } else if (!localConfigIsAb || OrangeUtils.parseLong(candidateDO.version) != localConfigVersion) {
                    if (OLog.isPrintLog(1)) {
                        OLog.d(TAG, "checkCandidate match", "localV", Long.valueOf(localConfigVersion), "remoteV", candidateDO.version);
                    }
                    this.curCandidateDO = candidateDO;
                    return true;
                } else {
                    if (OLog.isPrintLog(1)) {
                        OLog.d(TAG, "checkCandidate match but no version update", new Object[0]);
                    }
                    return false;
                }
            }
            if (OLog.isPrintLog(1)) {
                OLog.d(TAG, "checkCandidates finish", "not any match");
            }
        }
        boolean result = namespaceVersion > baseConfigVersion;
        if (result || !OLog.isPrintLog(1)) {
            return result;
        }
        OLog.d(TAG, "checkValid", "no version update");
        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("NameSpaceDO{");
        sb.append("loadLevel='").append(this.loadLevel).append('\'');
        sb.append(", name='").append(this.name).append('\'');
        sb.append(", version='").append(this.version).append('\'');
        sb.append('}');
        return String.format("NameSpaceDO{level:'%s', name:'%s', verison:'%s'}", new Object[]{this.loadLevel, this.name, this.version});
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NameSpaceDO that = (NameSpaceDO) o;
        if (this.loadLevel != null) {
            if (!this.loadLevel.equals(that.loadLevel)) {
                return false;
            }
        } else if (that.loadLevel != null) {
            return false;
        }
        if (this.md5 != null) {
            if (!this.md5.equals(that.md5)) {
                return false;
            }
        } else if (that.md5 != null) {
            return false;
        }
        if (this.name != null) {
            if (!this.name.equals(that.name)) {
                return false;
            }
        } else if (that.name != null) {
            return false;
        }
        if (this.resourceId != null) {
            if (!this.resourceId.equals(that.resourceId)) {
                return false;
            }
        } else if (that.resourceId != null) {
            return false;
        }
        if (this.version != null) {
            if (!this.version.equals(that.version)) {
                return false;
            }
        } else if (that.version != null) {
            return false;
        }
        if (this.candidates != null) {
            z = this.candidates.equals(that.candidates);
        } else if (that.candidates != null) {
            z = false;
        }
        return z;
    }
}
