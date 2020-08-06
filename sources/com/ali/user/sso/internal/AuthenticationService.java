package com.ali.user.sso.internal;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AuthenticationService extends Service {
    private static final String TAG = "SSO";
    private Authenticator mAuthenticator;

    public void onCreate() {
        Log.d("SSO", "SSO service started.");
        this.mAuthenticator = new Authenticator(this);
    }

    public void onDestroy() {
        Log.d("SSO", "SSO service stopped.");
        this.mAuthenticator = null;
    }

    public IBinder onBind(Intent intent) {
        Log.d("SSO", "SSO service is binded.");
        return this.mAuthenticator.getIBinder();
    }
}
