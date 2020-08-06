package com.ali.auth.third.offline.login.util;

import android.text.TextUtils;
import com.ali.auth.third.core.config.ConfigManager;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.model.AccountContract;
import com.ali.auth.third.core.model.InternalSession;
import com.ali.auth.third.core.model.Session;
import com.ali.auth.third.core.model.User;
import com.ali.auth.third.core.service.impl.CredentialManager;
import com.ali.auth.third.core.storage.aes.MD5;
import com.ali.auth.third.core.util.StringUtil;
import com.ali.auth.third.offline.login.context.LoginContext;
import mtopsdk.common.util.SymbolExpUtil;

public class SqliteHelper {
    private static SqliteHelper ourInstance = new SqliteHelper();

    public static SqliteHelper getInstance() {
        return ourInstance;
    }

    private SqliteHelper() {
    }

    public void saveToSqlite(String username, String password) {
        String[] main_sub;
        Session session = LoginContext.credentialService.getSession();
        AccountContract accountContract = new AccountContract(MD5.getSHA256(username + "&" + password), session.openId, KernelContext.storageService.encode(session.userid), KernelContext.storageService.encode(session.nick));
        accountContract.setHash_key(username);
        if (!TextUtils.isEmpty(accountContract.getUserid())) {
            LoginContext.databaseHandler.deleteAccountByUserId(accountContract.getUserid());
        }
        if (!TextUtils.isEmpty(accountContract.getOpenid())) {
            LoginContext.databaseHandler.deleteAccountByOpenId(accountContract.getOpenid());
        }
        LoginContext.databaseHandler.addAccount(accountContract);
        InternalSession internalSession = CredentialManager.INSTANCE.getInternalSession();
        if (internalSession != null) {
            User user = internalSession.user;
            String nick = user.nick;
            String mobile = internalSession.mobile;
            String email = user.email;
            if (!TextUtils.isEmpty(mobile) && !StringUtil.equals(username, mobile)) {
                accountContract.setHash(MD5.getSHA256(mobile + "&" + password));
                if (ConfigManager.DEBUG) {
                    accountContract.setHash_key(mobile);
                }
                LoginContext.databaseHandler.addAccount(accountContract);
            }
            if (!TextUtils.isEmpty(email) && !StringUtil.equals(username, email)) {
                accountContract.setHash(MD5.getSHA256(email + "&" + password));
                if (ConfigManager.DEBUG) {
                    accountContract.setHash_key(email);
                }
                LoginContext.databaseHandler.addAccount(accountContract);
            }
            if (!TextUtils.isEmpty(nick)) {
                if (!StringUtil.equals(username, nick)) {
                    accountContract.setHash(MD5.getSHA256(nick + "&" + password));
                    if (ConfigManager.DEBUG) {
                        accountContract.setHash_key(nick);
                    }
                    LoginContext.databaseHandler.addAccount(accountContract);
                }
                if (nick.contains(SymbolExpUtil.SYMBOL_COLON) && (main_sub = nick.split(SymbolExpUtil.SYMBOL_COLON)) != null && main_sub.length == 2) {
                    String sub = main_sub[1];
                    if (!TextUtils.isEmpty(sub) && !StringUtil.equals(sub, username)) {
                        accountContract.setHash(MD5.getSHA256(sub + "&" + password));
                        if (ConfigManager.DEBUG) {
                            accountContract.setHash_key(sub);
                        }
                        LoginContext.databaseHandler.addAccount(accountContract);
                    }
                }
            }
        }
    }

    public void saveToSqlite(String nick, String userid, String openId, String hash) {
        AccountContract accountContract = new AccountContract(hash, openId, KernelContext.storageService.encode(userid), KernelContext.storageService.encode(nick));
        if (!TextUtils.isEmpty(accountContract.getUserid())) {
            LoginContext.databaseHandler.deleteAccountByUserId(accountContract.getUserid());
        }
        if (!TextUtils.isEmpty(accountContract.getOpenid())) {
            LoginContext.databaseHandler.deleteAccountByOpenId(accountContract.getOpenid());
        }
        LoginContext.databaseHandler.addAccount(accountContract);
    }
}
