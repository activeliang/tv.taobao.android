package com.taobao.orange.candidate;

public class StringCompare extends DefCandidateCompare {
    public boolean equals(String clientVal, String serverVal) {
        return clientVal.equals(serverVal);
    }

    public boolean fuzzy(String clientVal, String serverVal) {
        return clientVal.startsWith(serverVal);
    }

    public boolean fuzzyNot(String clientVal, String serverVal) {
        return !clientVal.startsWith(serverVal);
    }
}
