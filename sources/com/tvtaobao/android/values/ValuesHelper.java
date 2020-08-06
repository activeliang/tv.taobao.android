package com.tvtaobao.android.values;

import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValuesHelper {
    private Map<String, IVal> kvMap = new HashMap();
    private Handler mainUiHandler = new Handler(Looper.getMainLooper());

    public interface IVal {
        Object get();

        void onRmv();
    }

    public void set(String key, Object val) {
        setVal(key, new Val(val));
    }

    public Object get(String key) {
        return getVal(key);
    }

    public Object rmv(String name) {
        return rmvVal(name);
    }

    public void clr() {
        clrVals();
    }

    public boolean has(String name) {
        return hasVal(name);
    }

    public void setVal(String key, IVal val) {
        synchronized (this.kvMap) {
            this.kvMap.put(key, val);
        }
    }

    public Object getVal(String key) {
        Object obj;
        synchronized (this.kvMap) {
            IVal rtn = this.kvMap.get(key);
            if (rtn == null) {
                obj = null;
            } else {
                obj = rtn.get();
            }
        }
        return obj;
    }

    public Object rmvVal(String name) {
        IVal iVal;
        synchronized (this.kvMap) {
            iVal = this.kvMap.remove(name);
        }
        if (iVal != null) {
            final IVal finalIVal = iVal;
            this.mainUiHandler.post(new Runnable() {
                public void run() {
                    finalIVal.onRmv();
                }
            });
        }
        if (iVal == null) {
            return null;
        }
        return iVal.get();
    }

    public void clrVals() {
        IVal iVal;
        List<Pair<String, IVal>> allVals = new ArrayList<>();
        synchronized (this.kvMap) {
            for (Map.Entry<String, IVal> entry : this.kvMap.entrySet()) {
                allVals.add(Pair.create(entry.getKey(), entry.getValue()));
            }
            this.kvMap.clear();
        }
        for (int i = 0; i < allVals.size(); i++) {
            Pair<String, IVal> pair = allVals.get(i);
            if (!(pair == null || (iVal = (IVal) pair.second) == null)) {
                final IVal finalIVal = iVal;
                this.mainUiHandler.post(new Runnable() {
                    public void run() {
                        finalIVal.onRmv();
                    }
                });
            }
        }
    }

    public boolean hasVal(String name) {
        boolean containsKey;
        synchronized (this.kvMap) {
            containsKey = this.kvMap.containsKey(name);
        }
        return containsKey;
    }

    public static class Val implements IVal {
        private Object val;

        public Val(Object val2) {
            this.val = val2;
        }

        public Object get() {
            return this.val;
        }

        public void onRmv() {
        }
    }
}
