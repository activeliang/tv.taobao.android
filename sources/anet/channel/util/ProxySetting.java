package anet.channel.util;

import java.net.Proxy;

public class ProxySetting {
    public final String authAccount;
    public final String authPassword;
    public final Proxy proxy;

    private ProxySetting(Proxy proxy2, String account, String password) {
        this.proxy = proxy2;
        this.authAccount = account;
        this.authPassword = password;
    }
}
