package com.ali.user.sso.internal;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Process;
import android.text.TextUtils;
import java.io.IOException;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class Authenticator extends AbstractAccountAuthenticator {
    public static final int ERROR_CODE_UNAUTHORIZED = 101;
    public static final String KEY_ACCOUNT_NAMES = "accounts";
    public static final String KEY_ACTUAL_NAME = "accounts";
    public static final String KEY_CALLER_PID = "callerPid";
    public static final String KEY_CALLER_UID = "callerUid";
    public static final String KEY_PEEK_ONLY = "peek";
    public static final String KEY_PHOTO_URL = "photo-url";
    public static final String KEY_PID = "agent_pid";
    public static final String KEY_REQUEST = "request";
    public static final String KEY_SHARE_APP = "share-app";
    public static final String KEY_SIGNATURES = "signatures";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_TOKEN_TIMESTAMP = "token-timestamp";
    public static final String REQ_ABDICATE = "abdicate";
    public static final String REQ_PEEK_TOKEN = "peek-token";
    public static final String REQ_PEEK_USERINFO = "peek-userinfo";
    public static final String REQ_REVEAL_ACTUAL_NAME = "reveal-actual-name";
    public static final String REQ_UPDATE_TOKEN = "update-token";
    public static final String REQ_UPDATE_WHITELIST = "update-whitelist";
    public static final String REQ_WHITELIST_TIMESTAMP = "whitelist-timestamp";
    private static final String USER_DATA_KEY_ACTUAL_NAME = "actual-account-name";
    private static final String USER_DATA_KEY_PHOTO_URL = "photo-url";
    private static final String USER_DATA_KEY_SHARE_APP = "share-app";
    private static final String USER_DATA_KEY_TOKEN_TIMESTAMP = "token-timestamp";
    private static String sTaobaoAccountType;
    private final Context mContext;
    private final SignatureWhitelist mWhitelist;

    public Authenticator(Context context) {
        super(context);
        this.mContext = context;
        this.mWhitelist = new SignatureWhitelist(context);
    }

    public Bundle addAccount(AccountAuthenticatorResponse response, String account_type, String authTokenType, String[] requiredFeatures, Bundle options) {
        String request;
        String token = null;
        if (options != null) {
            if (options.getString("authtoken") != null) {
                token = options.getString("authtoken");
            }
            Bundle result = null;
            if (TextUtils.equals(account_type, getTaobaoAccountType(this.mContext))) {
                result = verifyCallerIdentity(options);
            }
            if (result != null) {
                return result;
            }
        }
        if (options == null || (request = options.getString("request")) == null) {
            if (options == null || !options.containsKey("authAccount")) {
                Bundle result2 = new Bundle();
                result2.putInt("errorCode", 4);
                result2.putString("errorMessage", "Unsupported");
                return result2;
            }
            AccountManager am = AccountManager.get(this.mContext);
            String account_name = options.getString("authAccount");
            if (account_name.length() == 0) {
                return errorResult(7, "Empty account name");
            }
            Account account = new Account(account_name, account_type);
            String authToken = am.peekAuthToken(account, authTokenType);
            if (!TextUtils.isEmpty(authToken)) {
                am.invalidateAuthToken(authTokenType, authToken);
            }
            Bundle userdata = null;
            String actual_name = options.getString("accounts");
            String photo_url = options.getString("photo-url");
            String share_app = options.getString("share-app");
            String token_timestamp = String.valueOf(System.currentTimeMillis());
            if (actual_name != null) {
                userdata = new Bundle();
                userdata.putString(USER_DATA_KEY_ACTUAL_NAME, actual_name);
                userdata.putString("photo-url", photo_url);
                userdata.putString("share-app", share_app);
                userdata.putString("token-timestamp", token_timestamp);
            }
            am.addAccountExplicitly(account, (String) null, userdata);
            if (token != null) {
                am.setAuthToken(account, authTokenType, token);
            }
            Bundle result3 = new Bundle();
            result3.putString("authAccount", account_name);
            result3.putString("accountType", account_type);
            return result3;
        } else if (REQ_REVEAL_ACTUAL_NAME.equals(request)) {
            Bundle result4 = new Bundle();
            String[] account_names = options.getStringArray("accounts");
            AccountManager am2 = AccountManager.get(this.mContext);
            int length = account_names.length;
            for (int i = 0; i < length; i++) {
                String account_name2 = account_names[i];
                String actual_name2 = am2.getUserData(new Account(account_name2, account_type), USER_DATA_KEY_ACTUAL_NAME);
                if (actual_name2 != null) {
                    result4.putString(account_name2, actual_name2);
                }
            }
            return result4;
        } else if (REQ_UPDATE_TOKEN.equals(request)) {
            String token2 = options.getString("authtoken");
            if (token2 == null) {
                return Bundle.EMPTY;
            }
            String account_name3 = options.getString("authAccount");
            if (account_name3.length() == 0) {
                return errorResult(7, "Empty account name");
            }
            AccountManager.get(this.mContext).setAuthToken(new Account(account_name3, account_type), authTokenType, token2);
            return Bundle.EMPTY;
        } else if (REQ_WHITELIST_TIMESTAMP.equals(request)) {
            Bundle result5 = new Bundle();
            result5.putLong("timestamp", this.mWhitelist.getTimestamp());
            return result5;
        } else if (REQ_UPDATE_WHITELIST.equals(request)) {
            List<Signature> signatures = options.getParcelableArrayList(KEY_SIGNATURES);
            if (signatures == null) {
                throw new IllegalArgumentException("signatures unspecified");
            } else if (signatures.isEmpty()) {
                return Bundle.EMPTY;
            } else {
                long new_timestamp = options.getLong("timestamp");
                if (new_timestamp == 0) {
                    return Bundle.EMPTY;
                }
                this.mWhitelist.update(signatures, new_timestamp);
                return Bundle.EMPTY;
            }
        } else if (REQ_ABDICATE.equals(request)) {
            ComponentName comp = new ComponentName(this.mContext, AuthenticationService.class);
            response.onResult(Bundle.EMPTY);
            this.mContext.getPackageManager().setComponentEnabledSetting(comp, 2, 0);
            return null;
        } else if (REQ_PEEK_TOKEN.equals(request)) {
            AccountManager am3 = AccountManager.get(this.mContext);
            String account_name4 = options.getString("authAccount");
            String authToken2 = am3.peekAuthToken(new Account(account_name4, account_type), authTokenType);
            if (authToken2 == null) {
                return Bundle.EMPTY;
            }
            Bundle result6 = new Bundle();
            result6.putString("authAccount", account_name4);
            result6.putString("accountType", account_type);
            result6.putString("token", authToken2);
            return result6;
        } else if (REQ_PEEK_USERINFO.equals(request)) {
            String[] account_names2 = options.getStringArray("accounts");
            if (account_names2 == null || account_names2.length == 0) {
                return Bundle.EMPTY;
            }
            Bundle result7 = new Bundle();
            AccountManager am4 = AccountManager.get(this.mContext);
            int length2 = account_names2.length;
            for (int i2 = 0; i2 < length2; i2++) {
                String account_name5 = account_names2[i2];
                Bundle userinfo = new Bundle();
                Account account2 = new Account(account_name5, account_type);
                String actual_name3 = am4.getUserData(account2, USER_DATA_KEY_ACTUAL_NAME);
                String photo_url2 = am4.getUserData(account2, "photo-url");
                String share_app2 = am4.getUserData(account2, "share-app");
                String token_timestamp2 = am4.getUserData(account2, "token-timestamp");
                String authToken3 = am4.peekAuthToken(account2, authTokenType);
                try {
                    if (!TextUtils.isEmpty(token_timestamp2)) {
                        userinfo.putLong("token-timestamp", Long.parseLong(token_timestamp2));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                userinfo.putString("accounts", actual_name3);
                userinfo.putString("photo-url", photo_url2);
                userinfo.putString("share-app", share_app2);
                userinfo.putString("token", authToken3);
                result7.putParcelable(account_name5, userinfo);
            }
            return result7;
        } else {
            throw new UnsupportedOperationException("Unknown request: " + request);
        }
    }

    private Bundle verifyCallerIdentity(Bundle options) {
        int caller_pid = options.getInt(KEY_CALLER_PID, -1);
        int caller_uid = options.getInt(KEY_CALLER_UID, -1);
        if (caller_uid == -1 || caller_pid == -1) {
            Bundle result = new Bundle();
            result.putInt("errorCode", 6);
            result.putString("errorMessage", "sso service fetch pid failed");
            return result;
        } else if (caller_pid == Process.myPid() && caller_uid == Process.myUid()) {
            return null;
        } else {
            try {
                verifySignatures(caller_pid, caller_uid);
                return null;
            } catch (SecurityException e) {
                Bundle result2 = new Bundle();
                result2.putInt("errorCode", 101);
                result2.putString("errorMessage", "Unsupported");
                return result2;
            }
        }
    }

    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) {
        if (options.getInt(KEY_CALLER_PID, -1) == -1 || options.getInt(KEY_CALLER_UID, -1) == -1) {
            return Bundle.EMPTY;
        }
        Bundle result = null;
        if (TextUtils.equals(account.type, getTaobaoAccountType(this.mContext))) {
            result = verifyCallerIdentity(options);
        }
        if (result != null) {
            return result;
        }
        String token = AccountManager.get(this.mContext).peekAuthToken(account, authTokenType);
        if (token == null) {
            return Bundle.EMPTY;
        }
        Bundle result2 = new Bundle();
        result2.putString("authAccount", account.name);
        result2.putString("accountType", account.type);
        result2.putString("authtoken", token);
        return result2;
    }

    private void verifySignatures(int pid, int uid) {
        try {
            String[] packageNames = this.mContext.getPackageManager().getPackagesForUid(uid);
            if (packageNames != null && packageNames.length > 0) {
                int length = packageNames.length;
                int i = 0;
                while (i < length) {
                    if (!this.mWhitelist.match(this.mContext.getPackageManager().getPackageInfo(packageNames[i], 64).signatures)) {
                        i++;
                    } else {
                        return;
                    }
                }
            }
        } catch (Exception e) {
        }
        throw new SecurityException("Identify declined");
    }

    private Bundle errorResult(int code, String message) {
        Bundle result = new Bundle();
        result.putInt("errorCode", code);
        result.putString("errorMessage", message);
        return result;
    }

    public static String getTaobaoAccountType(Context context) {
        if (sTaobaoAccountType != null) {
            return sTaobaoAccountType;
        }
        try {
            String accountTypeFromXml = getAccountTypeFromXml(context.getResources().getXml(context.getPackageManager().getServiceInfo(new ComponentName(context, AuthenticationService.class), 640).metaData.getInt("android.accounts.AccountAuthenticator")));
            sTaobaoAccountType = accountTypeFromXml;
            return accountTypeFromXml;
        } catch (Exception e) {
            throw new IllegalArgumentException("TaobaoAuthenticationService service not found: " + e.getMessage());
        }
    }

    private static String getAccountTypeFromXml(XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        while (eventType != 2) {
            eventType = parser.next();
        }
        if ("account-authenticator".equals(parser.getName())) {
            return parser.getAttributeValue("http://schemas.android.com/apk/res/android", "accountType");
        }
        throw new IllegalStateException("Invalid xml");
    }

    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) {
        unsupport(response);
        return null;
    }

    private void unsupport(AccountAuthenticatorResponse response) {
        response.onError(6, "Unsupported");
    }

    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        throw new UnsupportedOperationException();
    }

    public String getAuthTokenLabel(String authTokenType) {
        return "Full access";
    }

    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) {
        Bundle result = new Bundle();
        result.putBoolean("booleanResult", false);
        return result;
    }

    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle loginOptions) {
        return null;
    }
}
