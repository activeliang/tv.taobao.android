package com.taobao.orange.candidate;

import android.os.RemoteException;
import android.text.TextUtils;
import com.taobao.orange.aidl.ParcelableCandidateCompare;
import com.taobao.orange.util.OLog;
import com.taobao.orange.util.OrangeUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnitAnalyze {
    private static final Pattern BASE_FORMAT;
    private static final String CANDIDATE_HASH_BUCKET = "did_hash";
    private static final Map<String, OPERATOR> OPERATOR_SYMBOL_MAP = new HashMap();
    private static final String TAG = "UnitAnalyze";
    String key;
    private OPERATOR opr;
    private String val;

    enum OPERATOR {
        EQUALS("="),
        GREATER_EQUALS(">="),
        LESS_EQUALS("<="),
        GREATER(">"),
        LESS("<"),
        NOT_EQUALS("!="),
        FUZZY("~="),
        NOT_FUZZY("!~");
        
        private String symbol;

        public String getSymbol() {
            return this.symbol;
        }

        private OPERATOR(String symbol2) {
            this.symbol = symbol2;
        }
    }

    static {
        List<String> symbols = new ArrayList<>();
        for (OPERATOR opr2 : OPERATOR.values()) {
            OPERATOR_SYMBOL_MAP.put(opr2.getSymbol(), opr2);
            symbols.add(opr2.getSymbol());
        }
        BASE_FORMAT = Pattern.compile(String.format("^\\s*(\\w+)\\s*(%s)\\s*(.+)\\s*$", new Object[]{OrangeUtils.formatOperateSymbols(symbols)}));
    }

    static UnitAnalyze complie(String candidatesExp) {
        return new UnitAnalyze(candidatesExp);
    }

    private UnitAnalyze(String candidate) {
        Matcher matcher = BASE_FORMAT.matcher(candidate);
        if (matcher.find()) {
            this.key = matcher.group(1);
            this.opr = OPERATOR_SYMBOL_MAP.get(matcher.group(2));
            this.val = matcher.group(3);
            if (this.key.equals("did_hash") && this.opr != OPERATOR.EQUALS) {
                throw new IllegalArgumentException(String.format("invalid hash candidate:%s", new Object[]{candidate}));
            }
            return;
        }
        throw new IllegalArgumentException(String.format("fail parse candidate:%s", new Object[]{candidate}));
    }

    /* access modifiers changed from: package-private */
    public boolean match(String clientVal, ParcelableCandidateCompare compare) throws RemoteException {
        if (OLog.isPrintLog(0)) {
            OLog.v(TAG, "match start", "key", this.key, "clientVal", clientVal, "opr", this.opr.getSymbol(), "serverVal", this.val);
        }
        if (TextUtils.isEmpty(clientVal)) {
            if (OLog.isPrintLog(1)) {
                OLog.d(TAG, "match no clientVal", "key", this.key);
            }
            return false;
        } else if (compare == null) {
            if (OLog.isPrintLog(1)) {
                OLog.d(TAG, "match no compare", "key", this.key);
            }
            return false;
        } else {
            boolean result = false;
            switch (this.opr) {
                case EQUALS:
                    result = compare.equals(clientVal, this.val);
                    break;
                case NOT_EQUALS:
                    result = compare.equalsNot(clientVal, this.val);
                    break;
                case GREATER:
                    result = compare.greater(clientVal, this.val);
                    break;
                case GREATER_EQUALS:
                    result = compare.greaterEquals(clientVal, this.val);
                    break;
                case LESS:
                    result = compare.less(clientVal, this.val);
                    break;
                case LESS_EQUALS:
                    result = compare.lessEquals(clientVal, this.val);
                    break;
                case FUZZY:
                    result = compare.fuzzy(clientVal, this.val);
                    break;
                case NOT_FUZZY:
                    result = compare.fuzzyNot(clientVal, this.val);
                    break;
            }
            if (result || !OLog.isPrintLog(1)) {
                return result;
            }
            OLog.d(TAG, "match fail", "key", this.key);
            return result;
        }
    }

    public String toString() {
        return String.format("%s%s%s", new Object[]{this.key, this.opr.getSymbol(), this.val});
    }
}
