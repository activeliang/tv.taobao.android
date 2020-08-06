package com.taobao.orange.candidate;

import android.os.Build;
import android.os.RemoteException;
import com.taobao.orange.GlobalOrange;
import com.taobao.orange.ICandidateCompare;
import com.taobao.orange.OCandidate;
import com.taobao.orange.OConstant;
import com.taobao.orange.util.OLog;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MultiAnalyze {
    private static final String JOINER_CHAR = "&";
    private static final String TAG = "MultiAnalyze";
    public static Map<String, OCandidate> candidateMap = new ConcurrentHashMap();
    private List<UnitAnalyze> unitAnalyzes = new ArrayList();

    public static void initBuildInCandidates() {
        List<OCandidate> candidates = new ArrayList<>();
        candidates.add(new OCandidate(OConstant.CANDIDATE_APPVER, GlobalOrange.appVersion, (Class<? extends ICandidateCompare>) VersionCompare.class));
        candidates.add(new OCandidate(OConstant.CANDIDATE_OSVER, String.valueOf(Build.VERSION.SDK_INT), (Class<? extends ICandidateCompare>) IntCompare.class));
        candidates.add(new OCandidate(OConstant.CANDIDATE_MANUFACTURER, String.valueOf(Build.MANUFACTURER), (Class<? extends ICandidateCompare>) StringCompare.class));
        candidates.add(new OCandidate(OConstant.CANDIDATE_BRAND, String.valueOf(Build.BRAND), (Class<? extends ICandidateCompare>) StringCompare.class));
        candidates.add(new OCandidate(OConstant.CANDIDATE_MODEL, String.valueOf(Build.MODEL), (Class<? extends ICandidateCompare>) StringCompare.class));
        candidates.add(new OCandidate(OConstant.CANDIDATE_HASH_DIS, GlobalOrange.deviceId, (Class<? extends ICandidateCompare>) HashCompare.class));
        OLog.i(TAG, "initBuildInCands", candidates);
        for (OCandidate candidate : candidates) {
            candidateMap.put(candidate.getKey(), candidate);
        }
    }

    public static MultiAnalyze complie(String candidatesExp) {
        return new MultiAnalyze(candidatesExp);
    }

    private MultiAnalyze(String candidatesExp) {
        for (String subCandidate : candidatesExp.split("&")) {
            this.unitAnalyzes.add(UnitAnalyze.complie(subCandidate));
        }
        if (OLog.isPrintLog(0)) {
            OLog.v(TAG, "parse start", "unitAnalyzes", this.unitAnalyzes);
        }
    }

    public boolean match() throws RemoteException {
        for (UnitAnalyze analyze : this.unitAnalyzes) {
            OCandidate candidate = candidateMap.get(analyze.key);
            if (candidate == null) {
                if (!OLog.isPrintLog(3)) {
                    return false;
                }
                OLog.w(TAG, "match fail", "key", analyze.key, "reason", "no found local Candidate");
                return false;
            } else if (!analyze.match(candidate.getClientVal(), candidate.getCompare())) {
                return false;
            }
        }
        return true;
    }
}
