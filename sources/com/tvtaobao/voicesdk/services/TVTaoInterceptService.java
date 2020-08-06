package com.tvtaobao.voicesdk.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import com.tvtaobao.voicesdk.ASRNotify;
import com.tvtaobao.voicesdk.ITVTaoCallBack;
import com.tvtaobao.voicesdk.ITVTaoIntercept;
import com.tvtaobao.voicesdk.bean.DomainResultVo;
import com.tvtaobao.voicesdk.bean.PageReturn;
import com.tvtaobao.voicesdk.type.DomainType;
import com.tvtaobao.voicesdk.utils.LogPrint;
import com.yunos.tv.tao.speech.client.domain.result.multisearch.takeout.TakeoutSearchResultVO;

public class TVTaoInterceptService extends Service {
    private final String TAG = "TVTaoInterceptService";
    private IBinder iBinder = new ITVTaoIntercept.Stub() {
        public void onVoiceAction(final DomainResultVo domainResultVo, final ITVTaoCallBack callback) throws RemoteException {
            LogPrint.i("TVTaoInterceptService", "TVTaoInterceptService.onVoiceAction intent : " + domainResultVo.getIntent());
            TVTaoInterceptService.this.mainHandler.post(new Runnable() {
                public void run() {
                    PageReturn pageReturn = null;
                    try {
                        if (DomainType.TAKEOUT_SEARCH.equals(domainResultVo.getIntent()) && ASRNotify.getInstance().isActionSearch(((TakeoutSearchResultVO) domainResultVo.getResultVO()).getKeywords(), domainResultVo.getSpoken(), domainResultVo.getTips())) {
                            PageReturn pageReturn2 = new PageReturn();
                            try {
                                pageReturn2.isHandler = true;
                                pageReturn2.feedback = "正在搜索";
                                pageReturn = pageReturn2;
                            } catch (RemoteException e) {
                                e = e;
                                PageReturn pageReturn3 = pageReturn2;
                                e.printStackTrace();
                                try {
                                    callback.onTVTaoIntercept((PageReturn) null);
                                } catch (RemoteException e1) {
                                    e1.printStackTrace();
                                    return;
                                }
                            }
                        }
                        if (pageReturn == null) {
                            pageReturn = ASRNotify.getInstance().isAction(domainResultVo);
                        }
                        callback.onTVTaoIntercept(pageReturn);
                    } catch (RemoteException e2) {
                        e = e2;
                        e.printStackTrace();
                        callback.onTVTaoIntercept((PageReturn) null);
                    }
                }
            });
        }
    };
    /* access modifiers changed from: private */
    public Handler mainHandler = new Handler(Looper.getMainLooper());

    public void onCreate() {
        super.onCreate();
        LogPrint.i("TVTaoInterceptService", "TVTaoInterceptService.onCreate");
        if (Build.VERSION.SDK_INT >= 26) {
            LogPrint.i("TVTaoInterceptService", "Android Version -> " + Build.VERSION.SDK_INT);
            ((NotificationManager) getSystemService("notification")).createNotificationChannel(new NotificationChannel("tvtaobao", "TVTAOBAO", 4));
            startForeground(1, new Notification.Builder(getApplicationContext(), "tvtaobao").build());
        }
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        LogPrint.i("TVTaoInterceptService", "TVTaoInterceptService.IBinder");
        return this.iBinder;
    }
}
