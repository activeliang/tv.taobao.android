package com.taobao.orange.candidate;

import com.taobao.orange.ICandidateCompare;

public class DefCandidateCompare implements ICandidateCompare {
    public boolean equals(String clientVal, String serverVal) {
        return false;
    }

    public boolean equalsNot(String clientVal, String serverVal) {
        return false;
    }

    public boolean greater(String clientVal, String serverVal) {
        return false;
    }

    public boolean greaterEquals(String clientVal, String serverVal) {
        return false;
    }

    public boolean less(String clientVal, String serverVal) {
        return false;
    }

    public boolean lessEquals(String clientVal, String serverVal) {
        return false;
    }

    public boolean fuzzy(String clientVal, String serverVal) {
        return false;
    }

    public boolean fuzzyNot(String clientVal, String serverVal) {
        return false;
    }
}
