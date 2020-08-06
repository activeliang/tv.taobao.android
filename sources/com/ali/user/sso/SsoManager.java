package com.ali.user.sso;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorDescription;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import com.ali.user.sso.internal.AuthenticationService;
import com.ali.user.sso.internal.Authenticator;
import com.ali.user.sso.internal.SignatureWhitelist;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import mtopsdk.common.util.SymbolExpUtil;

public class SsoManager {
    private static final long KResultWaitingTimeout = 15;
    private static final String LOG_TIP_FOR_INIT_FAILURE = "\nHave you set \"manifestmerger.enabled=true\" in your \"project.properties\"?";
    private static final int RETRY_TIME = 1;
    private static final String TAG = "sso.SsoManager";
    private static final String VERSION_CODE_KEY = "com.taobao.android.sso.Version";
    private static SignatureWhitelist mWhitelist;
    private static boolean sLocalEnabled;
    static int sLocalVersion;
    private static String[] sMainAccountType;

    public static class UnauthorizedAccessException extends GeneralSecurityException {
        private static final long serialVersionUID = 246686276926960445L;

        public UnauthorizedAccessException(String message) {
            super(message);
        }
    }

    public static Bundle peekUserInfos(Context context, String[] accountNames, String accountType, String tokenType) throws UnauthorizedAccessException, AuthenticatorException, OperationCanceledException, IOException {
        if (!checkPrerequisite(context, accountType, false)) {
            return null;
        }
        Bundle options = new Bundle();
        options.putStringArray("accounts", accountNames);
        return requestSpecial(context, Authenticator.REQ_PEEK_USERINFO, tokenType, options, accountType);
    }

    public static String peekToken(Context context, Account account, String tokenType) throws UnauthorizedAccessException, AuthenticatorException, OperationCanceledException, IOException {
        if (!checkPrerequisite(context, account.type, false)) {
            return null;
        }
        AccountManager am = AccountManager.get(context);
        Bundle options = new Bundle();
        options.putBoolean(Authenticator.KEY_PEEK_ONLY, true);
        int randomToken = new Random(System.currentTimeMillis()).nextInt();
        options.putInt(Authenticator.KEY_PID, Process.myPid());
        options.putInt("token", randomToken);
        options.putInt(Authenticator.KEY_CALLER_PID, Process.myPid());
        options.putInt(Authenticator.KEY_CALLER_UID, Process.myUid());
        Bundle result = null;
        for (int retry = 0; retry <= 1; retry++) {
            result = (Bundle) waitResult(am.getAuthToken(account, tokenType, options, (Activity) null, (AccountManagerCallback) null, (Handler) null));
            if (!(result == null || result.getParcelable("intent") == null)) {
                options.putString("authAccount", account.name);
                result = requestSpecial(context, Authenticator.REQ_PEEK_TOKEN, tokenType, options, account.type);
                result.putString("authtoken", result.getString("token"));
            }
            if (result.getInt("errorCode") != 1) {
                break;
            }
        }
        convertErrorToException(result);
        return result.getString("authtoken");
    }

    public static void updateToken(Context context, Account account, String tokenType, String token) throws UnauthorizedAccessException, AuthenticatorException, OperationCanceledException, IOException {
        if (checkPrerequisite(context, account.type, true)) {
            Bundle options = new Bundle();
            options.putString("authtoken", token);
            options.putString("authAccount", account.name);
            requestSpecial(context, Authenticator.REQ_UPDATE_TOKEN, tokenType, options, account.type);
        }
    }

    public static void invalidateToken(Context context, String authToken, String accountType) {
        AccountManager.get(context).invalidateAuthToken(accountType, authToken);
    }

    public static boolean updateWhitelist(Context context, ArrayList<Signature> signatures, long timestamp, String accountType) throws UnauthorizedAccessException, AuthenticatorException, OperationCanceledException, IOException {
        if (mWhitelist == null) {
            mWhitelist = new SignatureWhitelist(context.getApplicationContext());
        }
        boolean updated = mWhitelist.update(signatures, timestamp);
        if (sLocalEnabled) {
            return updated;
        }
        if (timestamp <= requestSpecial(context, Authenticator.REQ_WHITELIST_TIMESTAMP, (String) null, (Bundle) null, accountType).getLong("timestamp")) {
            return false;
        }
        Bundle options = new Bundle();
        options.putParcelableArrayList(Authenticator.KEY_SIGNATURES, signatures);
        options.putLong("timestamp", timestamp);
        requestSpecial(context, Authenticator.REQ_UPDATE_WHITELIST, (String) null, options, accountType);
        return true;
    }

    public static Account addAccountWithToken(Context context, String accountName, String maskName, String avatarUrl, String tokenType, String token, String appName, String accountType) throws UnauthorizedAccessException, AuthenticatorException, OperationCanceledException, IOException {
        Log.d(TAG, "addAccountWithToken  accountName=" + accountName + "  token=" + token);
        if (token != null) {
            checkPrerequisite(context, accountType, true);
        }
        Bundle options = new Bundle();
        if (token != null) {
            options.putString("authtoken", token);
        }
        if (maskName != null) {
            options.putString("authAccount", maskName);
            options.putString("accounts", accountName);
        } else {
            options.putString("authAccount", accountName);
        }
        options.putString(Authenticator.KEY_PHOTO_URL, avatarUrl);
        options.putString(Authenticator.KEY_SHARE_APP, appName);
        AccountManager am = AccountManager.get(context);
        int randomToken = new Random(System.currentTimeMillis()).nextInt();
        options.putInt(Authenticator.KEY_PID, Process.myPid());
        options.putInt("token", randomToken);
        options.putInt(Authenticator.KEY_CALLER_PID, Process.myPid());
        options.putInt(Authenticator.KEY_CALLER_UID, Process.myUid());
        Bundle result = null;
        for (int retry = 0; retry <= 1; retry++) {
            result = (Bundle) waitResult(am.addAccount(accountType, tokenType, (String[]) null, options, (Activity) null, (AccountManagerCallback) null, (Handler) null));
            if (result.getInt("errorCode") != 1) {
                break;
            }
        }
        convertErrorToException(result);
        String name = result.getString("authAccount");
        String type = result.getString("accountType");
        if (name != null && type != null) {
            return new Account(name, type);
        }
        throw new AuthenticatorException("Unknown result: " + result);
    }

    public static final boolean removeAccount(Context context, Account account) throws UnauthorizedAccessException, AuthenticatorException, OperationCanceledException, IOException {
        return ((Boolean) waitResult(AccountManager.get(context).removeAccount(account, (AccountManagerCallback) null, (Handler) null))).booleanValue();
    }

    private static <T> T waitResult(AccountManagerFuture<T> future) throws UnauthorizedAccessException, AuthenticatorException, OperationCanceledException, IOException {
        T result = future.getResult(KResultWaitingTimeout, TimeUnit.SECONDS);
        if ((result instanceof Bundle) && ((Bundle) result).getInt("errorCode") != 1) {
            convertErrorToException((Bundle) result);
        }
        return result;
    }

    private static Bundle convertErrorToException(Bundle result) throws AuthenticatorException, UnauthorizedAccessException {
        if (!result.containsKey("errorCode") && !result.containsKey("errorMessage")) {
            return result;
        }
        if (result.getInt("errorCode") == 101) {
            throw new UnauthorizedAccessException(result.getString("errorMessage"));
        }
        throw new AuthenticatorException(result.getString("errorMessage"));
    }

    private static boolean isMainAccountType(String accountType, Context context) {
        if (TextUtils.isEmpty(accountType)) {
            return false;
        }
        if (sMainAccountType == null) {
            Resources res = context.getResources();
            String mainAccountTypes = res.getString(res.getIdentifier("main_account_type", "string", context.getPackageName()));
            if (mainAccountTypes == null) {
                mainAccountTypes = Authenticator.getTaobaoAccountType(context);
            }
            sMainAccountType = mainAccountTypes.split(SymbolExpUtil.SYMBOL_COLON);
            if (sMainAccountType == null || sMainAccountType.length == 0) {
                return false;
            }
        }
        for (String mainType : sMainAccountType) {
            if (accountType.equals(mainType)) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkPrerequisite(Context context, String accountType, boolean wait) throws UnauthorizedAccessException, AuthenticatorException, OperationCanceledException, IOException {
        boolean z;
        if (sLocalVersion == 0) {
            ServiceInfo service_info = getLocalAuthenticationServiceInfo(context, accountType);
            sLocalVersion = service_info.metaData.getInt(VERSION_CODE_KEY);
            if (sLocalVersion == 0) {
                throw new IllegalArgumentException("com.taobao.android.sso.Version is not defined in meta-data of authentication service.\nHave you set \"manifestmerger.enabled=true\" in your \"project.properties\"?");
            }
            if (!service_info.enabled || !service_info.applicationInfo.enabled) {
                z = false;
            } else {
                z = true;
            }
            sLocalEnabled = z;
        }
        AuthenticatorDescription activeAuthenticator = getActiveAuthenticator(context, accountType);
        if (activeAuthenticator == null) {
            if (!isMainAccountType(accountType, context)) {
                return false;
            }
            Log.d("ssomanager", "accountType:" + accountType + " | no exist authenticator service\nset own service enabled, " + context.getPackageName());
            setLocalAuthenticatorState(context, true, accountType);
            if (!wait) {
                return false;
            }
            waitAuthenticator(context, accountType);
            sLocalEnabled = true;
            return true;
        } else if (activeAuthenticator.packageName.equals(context.getPackageName())) {
            sLocalEnabled = true;
            Log.d("ssomanager", "active_authenticator:" + activeAuthenticator.packageName + "\naccountType:" + accountType);
            return true;
        } else {
            Log.d("ssomanager", "active_authenticator:" + activeAuthenticator.packageName + "\naccountType:" + accountType);
            PackageManager pm = context.getPackageManager();
            try {
                if (mWhitelist == null) {
                    throw new IllegalStateException("Whitelist not set yet for account type: " + accountType);
                }
                Signature[] signatures = pm.getPackageInfo(activeAuthenticator.packageName, 64).signatures;
                Log.v("ssolog", "checkPrerequisite Whitelist");
                if (signatures == null || !mWhitelist.match(signatures)) {
                    throw new UnauthorizedAccessException("sso service verify whitelist failed. sso service is provide by " + activeAuthenticator.packageName);
                }
                ResolveInfo current = null;
                try {
                    current = pm.resolveService(new Intent("android.accounts.AccountAuthenticator").setPackage(activeAuthenticator.packageName), 128);
                } catch (Throwable th) {
                }
                int current_version = 0;
                if (!isMainAccountType(accountType, context)) {
                    return true;
                }
                if (current != null && (current.serviceInfo.metaData == null || (current_version = current.serviceInfo.metaData.getInt(VERSION_CODE_KEY)) == 0)) {
                    throw new AuthenticatorException("The current account authenticator installed by " + current.serviceInfo.packageName + " is incompatible with SSO authenticator.");
                } else if (current_version < sLocalVersion) {
                    setLocalAuthenticatorState(context, true, accountType);
                    Bundle ret = null;
                    try {
                        ret = requestSpecial(context, Authenticator.REQ_ABDICATE, (String) null, (Bundle) null, accountType);
                        waitAuthenticator(context, accountType);
                        Log.d("ssomanager", activeAuthenticator.packageName + " replaced by active_authenticator:" + context.getPackageName() + "\naccountType:" + accountType);
                        return true;
                    } finally {
                        if (ret == null) {
                            setLocalAuthenticatorState(context, false, accountType);
                        }
                    }
                } else if (!sLocalEnabled) {
                    return true;
                } else {
                    setLocalAuthenticatorState(context, false, accountType);
                    sLocalEnabled = false;
                    return true;
                }
            } catch (PackageManager.NameNotFoundException e) {
            }
        }
    }

    private static ServiceInfo getLocalAuthenticationServiceInfo(Context context, String accountType) {
        if (TextUtils.isEmpty(accountType)) {
            throw new IllegalArgumentException("Account Type is null.");
        }
        ComponentName comp = null;
        if (accountType.equals(Authenticator.getTaobaoAccountType(context))) {
            comp = new ComponentName(context, AuthenticationService.class);
        }
        if (comp == null) {
            throw new IllegalArgumentException("Not Supported Account Type");
        }
        try {
            return context.getPackageManager().getServiceInfo(comp, 640);
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalArgumentException("Authentication service not found.\nHave you set \"manifestmerger.enabled=true\" in your \"project.properties\"?");
        }
    }

    static AuthenticatorDescription getActiveAuthenticator(Context context, String accountType) {
        if (TextUtils.isEmpty(accountType)) {
            return null;
        }
        try {
            for (AuthenticatorDescription authenticator : AccountManager.get(context).getAuthenticatorTypes()) {
                if (accountType.equals(authenticator.type)) {
                    return authenticator;
                }
            }
        } catch (Throwable th) {
        }
        return null;
    }

    private static Bundle requestSpecial(Context context, String request, String tokenType, Bundle options, String accountType) throws UnauthorizedAccessException, AuthenticatorException, OperationCanceledException, IOException {
        if (options == null) {
            options = new Bundle();
        }
        options.putString("request", request);
        int randomToken = new Random(System.currentTimeMillis()).nextInt();
        options.putInt(Authenticator.KEY_PID, Process.myPid());
        options.putInt("token", randomToken);
        options.putInt(Authenticator.KEY_CALLER_PID, Process.myPid());
        options.putInt(Authenticator.KEY_CALLER_UID, Process.myUid());
        Bundle result = null;
        for (int retry = 0; retry <= 1; retry++) {
            result = (Bundle) waitResult(AccountManager.get(context).addAccount(accountType, tokenType != null ? tokenType : "", (String[]) null, options, (Activity) null, (AccountManagerCallback) null, (Handler) null));
            if (result == null) {
                result = new Bundle();
            }
            if (result.getInt("errorCode") != 1) {
                break;
            }
        }
        convertErrorToException(result);
        return result;
    }

    private static void setLocalAuthenticatorState(Context context, boolean enable, String accountType) {
        ComponentName comp = null;
        if (accountType.equals(Authenticator.getTaobaoAccountType(context))) {
            comp = new ComponentName(context, AuthenticationService.class);
        }
        context.getPackageManager().setComponentEnabledSetting(comp, enable ? 1 : 2, 1);
    }

    private static void waitAuthenticator(Context context, String accountType) {
        long spin_interval = 64;
        long timeout = 15000;
        String pkg = context.getPackageName();
        while (timeout > 0) {
            AuthenticatorDescription active = getActiveAuthenticator(context, accountType);
            if (active == null || !active.packageName.equals(pkg)) {
                Log.v(TAG, "Waiting for authenticator...");
                if (spin_interval < 1000) {
                    spin_interval *= 2;
                }
                try {
                    Thread.sleep(spin_interval);
                } catch (InterruptedException e) {
                }
                timeout -= spin_interval;
            } else {
                return;
            }
        }
    }
}
