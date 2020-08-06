package android.taobao.windvane.jsbridge;

import android.content.Context;
import android.content.Intent;
import android.taobao.windvane.webview.IWVWebView;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class WVPluginEntryManager {
    private Map<String, Object> entryMap = new HashMap();
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    private Context mContext = null;
    private IWVWebView mWebView = null;

    public WVPluginEntryManager(Context context, IWVWebView webView) {
        this.mContext = context;
        this.mWebView = webView;
    }

    public void addEntry(String name, Object instance) {
        this.lock.writeLock().lock();
        try {
            this.entryMap.put(name, instance);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public Object getEntry(String name) {
        WVApiPlugin plugin;
        this.lock.readLock().lock();
        try {
            Object jsObject = this.entryMap.get(name);
            if (jsObject == null) {
                this.lock.writeLock().lock();
                try {
                    if (this.entryMap.get(name) == null && (plugin = WVPluginManager.createPlugin(name, this.mContext, this.mWebView)) != null) {
                        this.entryMap.put(name, plugin);
                        jsObject = plugin;
                    }
                } finally {
                    this.lock.writeLock().unlock();
                }
            }
            return jsObject;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    /* JADX INFO: finally extract failed */
    public void onDestroy() {
        this.lock.readLock().lock();
        try {
            for (Object value : this.entryMap.values()) {
                if (value instanceof WVApiPlugin) {
                    ((WVApiPlugin) value).onDestroy();
                }
            }
            this.lock.readLock().unlock();
            this.lock.writeLock().lock();
            try {
                this.entryMap.clear();
            } finally {
                this.lock.writeLock().unlock();
            }
        } catch (Throwable th) {
            this.lock.readLock().unlock();
            throw th;
        }
    }

    public void onPause() {
        this.lock.readLock().lock();
        try {
            for (Object value : this.entryMap.values()) {
                if (value instanceof WVApiPlugin) {
                    ((WVApiPlugin) value).onPause();
                }
            }
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public void onResume() {
        this.lock.readLock().lock();
        try {
            for (Object value : this.entryMap.values()) {
                if (value instanceof WVApiPlugin) {
                    ((WVApiPlugin) value).onResume();
                }
            }
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        this.lock.readLock().lock();
        try {
            for (Object value : this.entryMap.values()) {
                if (value instanceof WVApiPlugin) {
                    ((WVApiPlugin) value).onActivityResult(requestCode, resultCode, intent);
                }
            }
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        this.lock.readLock().lock();
        try {
            for (Object value : this.entryMap.values()) {
                if (value instanceof WVApiPlugin) {
                    ((WVApiPlugin) value).onScrollChanged(l, t, oldl, oldt);
                }
            }
        } finally {
            this.lock.readLock().unlock();
        }
    }
}
