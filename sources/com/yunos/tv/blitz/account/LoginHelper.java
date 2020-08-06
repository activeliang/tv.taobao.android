package com.yunos.tv.blitz.account;

import android.content.Context;

public interface LoginHelper {

    public interface SyncLoginListener {
        void onLogin(boolean z);
    }

    void addReceiveLoginListener(SyncLoginListener syncLoginListener);

    void addSyncLoginListener(SyncLoginListener syncLoginListener);

    void destroy();

    String getNick();

    Context getRegistedContext();

    String getSessionId();

    String getUserId();

    boolean isLogin();

    void login(Context context);

    void logout(Context context);

    void registerLoginListener(Context context);

    void removeReceiveLoginListener(SyncLoginListener syncLoginListener);

    void removeSyncLoginListener(SyncLoginListener syncLoginListener);

    void startYunosAccountActivity(Context context, boolean z);

    void unregisterLoginListener();
}
