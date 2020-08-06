package com.uc.webview.export.internal.interfaces;

import com.uc.webview.export.annotations.Interface;
import com.uc.webview.export.extension.ILocationManager;

@Interface
/* compiled from: ProGuard */
public interface UCMobileWebKit {
    void fontDownloadFinished();

    String getCoreBuildSeq();

    String getCoreVersion();

    IWebResourceInternal getWebResources();

    void onDestroy();

    void onLowMemory();

    void onOrientationChanged();

    void onPause();

    void onResume();

    void onScreenLock();

    void onScreenUnLock();

    void onTrimMemory(int i);

    void onWindowSizeChanged();

    void setLocationManagerUC(ILocationManager iLocationManager);

    void setThirdNetwork(INetwork iNetwork, INetworkDecider iNetworkDecider);

    void updateBussinessInfo(int i, int i2, String str, Object obj);
}
