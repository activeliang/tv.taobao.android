package com.taobao.wireless.security.sdk.initialize;

import android.content.Context;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.initialize.IInitializeComponent;
import com.taobao.wireless.security.sdk.initialize.IInitializeComponent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class a implements IInitializeComponent {
    private Set<C0008a> a = new HashSet();
    private Object b = new Object();

    /* renamed from: com.taobao.wireless.security.sdk.initialize.a$a  reason: collision with other inner class name */
    class C0008a implements IInitializeComponent.IInitFinishListener {
        IInitializeComponent.IInitFinishListener a;

        public C0008a(IInitializeComponent.IInitFinishListener iInitFinishListener) {
            this.a = iInitFinishListener;
        }

        public void onError() {
            this.a.onError();
        }

        public void onSuccess() {
            this.a.onSuccess();
        }
    }

    public int initialize(Context context) {
        try {
            return SecurityGuardManager.getInitializer().initialize(context);
        } catch (SecException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public synchronized void initializeAsync(Context context) {
        SecurityGuardManager.getInitializer().initializeAsync(context);
    }

    public boolean isSoValid(Context context) {
        return true;
    }

    public synchronized void loadLibraryAsync(Context context) {
        SecurityGuardManager.getInitializer().initializeAsync(context);
    }

    public void loadLibraryAsync(Context context, String str) {
        try {
            SecurityGuardManager.getInitializer().loadLibraryAsync(context, str);
        } catch (SecException e) {
            e.printStackTrace();
        }
    }

    public synchronized int loadLibrarySync(Context context) {
        int i;
        try {
            i = SecurityGuardManager.getInitializer().loadLibrarySync(context);
        } catch (SecException e) {
            e.printStackTrace();
            i = 1;
        }
        return i;
    }

    public int loadLibrarySync(Context context, String str) {
        try {
            return SecurityGuardManager.getInitializer().loadLibrarySync(context, str);
        } catch (SecException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public void registerInitFinishListener(IInitializeComponent.IInitFinishListener iInitFinishListener) {
        if (iInitFinishListener != null) {
            C0008a aVar = new C0008a(iInitFinishListener);
            synchronized (this.b) {
                this.a.add(aVar);
            }
            try {
                SecurityGuardManager.getInitializer().registerInitFinishListener(aVar);
            } catch (SecException e) {
                e.printStackTrace();
            }
        }
    }

    public void unregisterInitFinishListener(IInitializeComponent.IInitFinishListener iInitFinishListener) {
        C0008a aVar;
        if (iInitFinishListener != null) {
            synchronized (this.b) {
                Iterator<C0008a> it = this.a.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        aVar = null;
                        break;
                    }
                    aVar = it.next();
                    if (aVar.a == iInitFinishListener) {
                        break;
                    }
                }
                if (aVar != null) {
                    this.a.remove(aVar);
                }
            }
            if (aVar != null) {
                try {
                    SecurityGuardManager.getInitializer().unregisterInitFinishListener(aVar);
                } catch (SecException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
