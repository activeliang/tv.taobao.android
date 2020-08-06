package com.tvtaobao.android.runtime;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import com.bftv.fui.constantplugin.Constant;
import java.lang.ref.WeakReference;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class RtBaseEnv {
    private static Map<String, String> RT_configs = new HashMap();
    private static EarsManager RT_ears_manager = new EarsManager();
    private static Map<String, Object> RT_values = new HashMap();

    public interface BgTask<T> {
        T doInBackground();

        void done(T t, Throwable th);
    }

    public interface MsgEar {
        void onMsg(Msg msg);
    }

    public static String mkKey(Object obj) {
        return (obj == null ? Constant.NULL : "" + obj.hashCode()) + Constant.INTENT_JSON_MARK + System.currentTimeMillis();
    }

    public static void destroy() {
        clr();
        cfgClr();
        earsClr();
    }

    public static Object set(String key, Object object) {
        Object rtn;
        synchronized (RT_values) {
            rtn = RT_values.put(key, object);
        }
        return rtn;
    }

    public static Object get(String key) {
        return get(key, (Object) null);
    }

    public static Object get(String key, Object defaultV) {
        Object rtn;
        synchronized (RT_values) {
            rtn = RT_values.get(key);
        }
        if (rtn != null) {
            return rtn;
        }
        return defaultV;
    }

    public static Object rmv(String key) {
        Object rtn;
        synchronized (RT_values) {
            rtn = RT_values.remove(key);
        }
        return rtn;
    }

    public static void clr() {
        synchronized (RT_values) {
            RT_values.clear();
        }
    }

    public static Map.Entry<String, String> cfgSet(String cfgKey, String cfgValue) {
        Map.Entry<String, String> rtn = null;
        synchronized (RT_configs) {
            String oldVal = RT_configs.put(cfgKey, cfgValue);
            if (oldVal != null) {
                rtn = new AbstractMap.SimpleEntry<>(cfgKey, oldVal);
            }
        }
        return rtn;
    }

    public static Map.Entry<String, String> cfgGet(String cfgKey) {
        Map.Entry rtn = null;
        synchronized (RT_configs) {
            String val = RT_configs.get(cfgKey);
            if (val != null) {
                rtn = new AbstractMap.SimpleEntry(cfgKey, val);
            }
        }
        return rtn;
    }

    public static Map.Entry<String, String> cfgRmv(String cfgKey) {
        Map.Entry rtn = null;
        synchronized (RT_configs) {
            String oldVal = RT_configs.remove(cfgKey);
            if (oldVal != null) {
                rtn = new AbstractMap.SimpleEntry(cfgKey, oldVal);
            }
        }
        return rtn;
    }

    public static void cfgClr() {
        synchronized (RT_configs) {
            RT_configs.clear();
        }
    }

    public static Map.Entry<String, String>[] cfgList() {
        Map.Entry<String, String>[] rtn;
        synchronized (RT_configs) {
            rtn = (Map.Entry[]) ((Map.Entry[]) RT_configs.entrySet().toArray());
        }
        return rtn;
    }

    public static void doInBackground(final BgTask task) {
        if (task != null) {
            new AsyncTask<Object, Object, Object>() {
                /* access modifiers changed from: protected */
                public Object doInBackground(Object... objects) {
                    Object rlt = null;
                    Throwable err = null;
                    try {
                        if (task != null) {
                            rlt = task.doInBackground();
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                        err = e;
                    }
                    return Pair.create(rlt, err);
                }

                /* access modifiers changed from: protected */
                public void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    if (task != null && (o instanceof Pair)) {
                        task.done(((Pair) o).first, (Throwable) ((Pair) o).second);
                    }
                }
            }.execute(new Object[0]);
        }
    }

    public static void listen(MsgEar ear) {
        RT_ears_manager.listen(ear);
    }

    public static void unlisten(MsgEar ear) {
        RT_ears_manager.unlisten(ear);
    }

    public static void broadcast(Msg msg) {
        RT_ears_manager.broadcast(msg);
    }

    public static void earsClr() {
        RT_ears_manager.clear();
    }

    private static class EarsManager {
        /* access modifiers changed from: private */
        public List<MsgEarClient> RT_ears;
        /* access modifiers changed from: private */
        public AtomicLong round;
        /* access modifiers changed from: private */
        public AtomicBoolean sizeChanged;

        private EarsManager() {
            this.RT_ears = new LinkedList();
            this.round = new AtomicLong(0);
            this.sizeChanged = new AtomicBoolean(false);
        }

        public void listen(MsgEar ear) {
            synchronized (this.RT_ears) {
                for (MsgEarClient mec : this.RT_ears) {
                    if (mec.msgEarWR != null && mec.msgEarWR.get() == ear) {
                        return;
                    }
                }
                this.RT_ears.add(new MsgEarClient(ear));
                this.sizeChanged.set(true);
            }
        }

        public void unlisten(MsgEar ear) {
            synchronized (this.RT_ears) {
                Iterator<MsgEarClient> iterator = this.RT_ears.iterator();
                while (iterator.hasNext()) {
                    MsgEarClient mec = iterator.next();
                    if (mec.msgEarWR != null && mec.msgEarWR.get() == ear) {
                        iterator.remove();
                        this.sizeChanged.set(true);
                        return;
                    }
                }
            }
        }

        public void broadcast(final Msg msg) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    MsgEar me;
                    synchronized (EarsManager.this.RT_ears) {
                        if (EarsManager.this.sizeChanged.get()) {
                            EarsManager.this.sizeChanged.set(false);
                        }
                        long currRound = EarsManager.this.round.incrementAndGet();
                        Iterator<MsgEarClient> iterator = EarsManager.this.RT_ears.iterator();
                        while (iterator.hasNext()) {
                            MsgEarClient mec = iterator.next();
                            if (!(mec.round == currRound || mec.msgEarWR == null || (me = (MsgEar) mec.msgEarWR.get()) == null)) {
                                me.onMsg(msg);
                                mec.round = currRound;
                                if (EarsManager.this.sizeChanged.get()) {
                                    EarsManager.this.sizeChanged.set(false);
                                    iterator = EarsManager.this.RT_ears.iterator();
                                }
                            }
                        }
                    }
                }
            });
        }

        public void clear() {
            synchronized (this.RT_ears) {
                this.RT_ears.clear();
                this.sizeChanged.set(true);
            }
        }
    }

    private static class MsgEarClient {
        WeakReference<MsgEar> msgEarWR;
        long round = 0;

        public MsgEarClient(MsgEar msgEar) {
            this.msgEarWR = new WeakReference<>(msgEar);
        }
    }

    public static class Msg<T> {
        public static AtomicLong count = new AtomicLong(0);
        public T data;
        public long id = count.incrementAndGet();
        public String name;

        public Msg(String name2, T data2) {
            this.name = name2;
            this.data = data2;
        }

        public static Msg obtain(String name2, Object data2) {
            return new Msg(name2, data2);
        }
    }

    public static class Params extends HashMap {
        public static Params mk(Object key, Object value) {
            return new Params().put(key, value);
        }

        public Params put(Object key, Object val) {
            super.put(key, val);
            return this;
        }
    }
}
