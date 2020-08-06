package com.taobao.orange.candidate;

public class IntCompare extends DefCandidateCompare {
    public boolean equals(String clientVal, String serverVal) {
        return Integer.parseInt(clientVal) == Integer.parseInt(serverVal);
    }

    public boolean equalsNot(String clientVal, String serverVal) {
        return Integer.parseInt(clientVal) != Integer.parseInt(serverVal);
    }

    public boolean greater(String clientVal, String serverVal) {
        return Integer.parseInt(clientVal) > Integer.parseInt(serverVal);
    }

    public boolean greaterEquals(String clientVal, String serverVal) {
        return Integer.parseInt(clientVal) >= Integer.parseInt(serverVal);
    }

    public boolean less(String clientVal, String serverVal) {
        return Integer.parseInt(clientVal) < Integer.parseInt(serverVal);
    }

    public boolean lessEquals(String clientVal, String serverVal) {
        return Integer.parseInt(clientVal) <= Integer.parseInt(serverVal);
    }
}
