package com.yunos.tvtaobao.biz.request.core;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.yunos.tv.blitz.account.LoginHelper;
import com.yunos.tv.core.AppInitializer;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.account.LoginHelperImpl;
import com.yunos.tv.core.util.NetWorkUtil;
import com.yunos.tvtaobao.biz.request.core.ServiceResponse;
import com.yunos.tvtaobao.payment.MemberSDKLoginStatus;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsyncDataLoader {
    private static final String TAG = "AsyncDataLoader";
    protected static ThreadPoolExecutor threadPool;

    public interface DataLoadCallback<T> {
        T load();

        void onStartLogin();

        void postExecute(T t);

        void preExecute();
    }

    protected static synchronized void initialize() {
        synchronized (AsyncDataLoader.class) {
            if (threadPool == null) {
                threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
            }
        }
    }

    public static void shutdown() {
        if (threadPool != null) {
            threadPool.shutdownNow();
            threadPool = null;
        }
    }

    public static void purge() {
        if (threadPool != null) {
            threadPool.purge();
        }
    }

    public static void waitTerminationAndExit() throws InterruptedException {
        if (threadPool != null) {
            if (!threadPool.isShutdown()) {
                threadPool.shutdownNow();
            }
            threadPool.awaitTermination(3, TimeUnit.SECONDS);
            threadPool = null;
        }
    }

    public static <T> void execute(DataLoadCallback<ServiceResponse<T>> dataLoadCallback) {
        execute((Context) null, dataLoadCallback, (ServiceResponse.RequestErrorListener) null, (ServiceResponse.RequestErrorListener) null);
    }

    protected static <T> void execute(Context context, DataLoadCallback<ServiceResponse<T>> dataLoadCallback, ServiceResponse.RequestErrorListener networkErrorListener, ServiceResponse.RequestErrorListener loginErrorListener) {
        ZpLogger.d(TAG, " execute ;  threadPool = " + threadPool);
        try {
            dataLoadCallback.preExecute();
            if (!NetWorkUtil.isNetWorkAvailable()) {
                ZpLogger.d(TAG, " execute ;  network not available ");
                ServiceResponse<T> response = new ServiceResponse<>();
                if (networkErrorListener != null) {
                    response.addErrorListener(networkErrorListener);
                }
                response.update(ServiceCode.NET_WORK_ERROR);
                dataLoadCallback.postExecute(response);
                return;
            }
            if (threadPool == null) {
                initialize();
            }
            Handler handler = new MyHandler(dataLoadCallback, loginErrorListener);
            ZpLogger.v(TAG, handler.toString());
            if (!threadPool.isShutdown()) {
                threadPool.execute(new MyRunnable(handler, dataLoadCallback));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            ZpLogger.e(TAG, e1.getMessage());
        }
    }

    public static <T> void executeWithNoAutoLogin(DataLoadCallback<ServiceResponse<T>> dataLoadCallback) {
        ZpLogger.d(TAG, " executeWithNoAutoLogin ;  threadPool = " + threadPool);
        try {
            dataLoadCallback.preExecute();
            if (!NetWorkUtil.isNetWorkAvailable()) {
                ServiceResponse<T> response = new ServiceResponse<>();
                response.update(ServiceCode.NET_WORK_ERROR);
                dataLoadCallback.postExecute(response);
                return;
            }
            if (threadPool == null) {
                initialize();
            }
            Handler handler = new MyHandler(dataLoadCallback);
            if (threadPool != null && !threadPool.isShutdown()) {
                threadPool.execute(new MyRunnableNoLogin(handler, dataLoadCallback));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            ZpLogger.e(TAG, e1.getMessage());
        }
    }

    public static void executeProcess(Runnable runnable) {
        if (threadPool == null) {
            initialize();
        }
        if (threadPool != null && !threadPool.isShutdown()) {
            threadPool.execute(runnable);
        }
    }

    private static class MyHandler<T> extends Handler {
        /* access modifiers changed from: private */
        public DataLoadCallback<ServiceResponse<T>> callback;
        /* access modifiers changed from: private */
        public final ServiceResponse.RequestErrorListener requestErrorListener;

        public MyHandler(DataLoadCallback<ServiceResponse<T>> callback2, ServiceResponse.RequestErrorListener loginErrorRef) {
            super(Looper.getMainLooper());
            this.callback = callback2;
            this.requestErrorListener = loginErrorRef;
        }

        public MyHandler(DataLoadCallback<ServiceResponse<T>> callbackRef) {
            super(Looper.getMainLooper());
            this.callback = callbackRef;
            this.requestErrorListener = null;
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    try {
                        this.callback.postExecute((ServiceResponse) message.obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.callback = null;
                    return;
                case 1:
                    ZpLogger.i(AsyncDataLoader.TAG, "AsyncDataLoader.relogin");
                    final LoginHelper helper = CoreApplication.getLoginHelper(CoreApplication.getApplication());
                    this.callback.onStartLogin();
                    CoreApplication.getLoginHelper(CoreApplication.getApplication()).addSyncLoginListener(new LoginHelper.SyncLoginListener() {
                        public void onLogin(boolean isSuccess) {
                            CoreApplication.getLoginHelper(CoreApplication.getApplication()).removeSyncLoginListener(this);
                            ZpLogger.i(AsyncDataLoader.TAG, "onLogin isSuccess=" + isSuccess + ", getSessionId = " + helper.getSessionId());
                            if (!isSuccess) {
                                ServiceResponse<T> sr = new ServiceResponse<>();
                                sr.update(ServiceCode.CLIENT_LOGIN_ERROR);
                                if (MyHandler.this.requestErrorListener != null) {
                                    sr.addErrorListener(MyHandler.this.requestErrorListener);
                                }
                                MyHandler.this.callback.postExecute(sr);
                            } else if (!AsyncDataLoader.threadPool.isShutdown()) {
                                AsyncDataLoader.threadPool.execute(new Runnable() {
                                    public void run() {
                                        ZpLogger.v(AsyncDataLoader.TAG, "AsyncDataLoader.registerSessionInfo, getSessionId = " + helper.getSessionId());
                                        AppInitializer.getMtopInstance().registerSessionInfo(helper.getSessionId(), helper.getUserId());
                                        this.sendMessage(this.obtainMessage(0, (ServiceResponse) MyHandler.this.callback.load()));
                                    }
                                });
                            }
                        }
                    });
                    if (helper instanceof LoginHelperImpl) {
                        helper.startYunosAccountActivity(CoreApplication.getApplication(), true);
                        return;
                    } else {
                        helper.login(CoreApplication.getApplication());
                        return;
                    }
                default:
                    return;
            }
        }
    }

    private static class MyRunnable<T> implements Runnable {
        private final DataLoadCallback<ServiceResponse<T>> callback;
        private final Handler handler;

        public MyRunnable(Handler handler2, DataLoadCallback<ServiceResponse<T>> callback2) {
            this.handler = handler2;
            this.callback = callback2;
        }

        public void run() {
            while (MemberSDKLoginStatus.isLoggingOut()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                if (!CoreApplication.getLoginHelper(CoreApplication.getApplication()).isLogin() && !MemberSDKLoginStatus.isLoggingOut()) {
                    this.handler.sendEmptyMessage(1);
                    return;
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            if (this.callback != null) {
                ServiceResponse<T> sr = new ServiceResponse<>();
                try {
                    sr = this.callback.load();
                } catch (Exception e3) {
                    e3.printStackTrace();
                    ZpLogger.e(AsyncDataLoader.TAG, "asyncDataLoader callback.load exception:" + e3);
                    sr.update(ServiceCode.DATA_PARSE_ERROR);
                }
                if (sr != null && !sr.isSucess()) {
                    ZpLogger.i(AsyncDataLoader.TAG, "AsyncDataLoader.isNotLogin = " + sr.isNotLogin() + ".isSessionTimeout = " + sr.isSessionTimeout());
                    if (sr.isNotLogin() || sr.isSessionTimeout()) {
                        this.handler.sendEmptyMessage(1);
                        return;
                    }
                }
                this.handler.sendMessage(this.handler.obtainMessage(0, sr));
            }
        }
    }

    private static class MyHandlerNoLogin<T> extends Handler {
        private final DataLoadCallback<ServiceResponse<T>> callback;

        public MyHandlerNoLogin(DataLoadCallback<ServiceResponse<T>> callback2) {
            this.callback = callback2;
        }

        public void handleMessage(Message message) {
            try {
                this.callback.postExecute((ServiceResponse) message.obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class MyRunnableNoLogin<T> implements Runnable {
        private DataLoadCallback<ServiceResponse<T>> callback;
        private final Handler handler;

        public MyRunnableNoLogin(Handler handler2, DataLoadCallback<ServiceResponse<T>> callbackRef) {
            this.handler = handler2;
            this.callback = callbackRef;
        }

        public void run() {
            this.handler.sendMessage(this.handler.obtainMessage(0, this.callback.load()));
            this.callback = null;
        }
    }
}
