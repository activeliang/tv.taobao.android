package com.taobao.orange.candidate;

import com.taobao.orange.util.OLog;
import com.taobao.orange.util.OrangeUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HashCompare extends DefCandidateCompare {
    private static final String TAG = "HashCompare";
    private static Pattern p = Pattern.compile("\\d+");

    public boolean equals(String base, String val) {
        Matcher m = p.matcher(val);
        List<Integer> hash = new ArrayList<>();
        while (m.find()) {
            hash.add(Integer.valueOf(Integer.parseInt(m.group())));
        }
        if (hash.size() != 3) {
            throw new IllegalArgumentException("did_hash candidate format is illegal");
        }
        long mod = OrangeUtils.hash(base) % ((long) hash.get(0).intValue());
        if (OLog.isPrintLog(0)) {
            OLog.v(TAG, "equals", "mod", Long.valueOf(mod));
        }
        return mod >= ((long) hash.get(1).intValue()) && mod <= ((long) hash.get(2).intValue());
    }
}
