package android.taobao.windvane.packageapp.zipapp.utils;

import android.taobao.windvane.util.DigestUtils;
import android.taobao.windvane.util.RsaUtil;
import android.taobao.windvane.util.TaoLog;
import java.security.Key;

public class ZipAppSecurityUtils {
    private static final String TAG = "PackageApp-ZipAppSecurityUtils";
    private static final String key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr4QTVnTHJ/W1hfBkEfTdWMMAxsQHW22gK0JProk3hmdwwal+Up7Ty/8NUXs+8SKufik2ASXQLFkqeoZu60sXmtlQGZJ+kAezC8pS9MboHZWywO9VJwxRUQuXI/Hn0jjZsA8tZPpN6Ty9wkz80GrQJrRuhjEjT0JAjElhpZUxTXMKIIPqM+ndgcfF55f9wWYFKW+o/Z0Nil0yP1crvLryq3sbSbDTnz7+j4zUE7aCGb0ECyS/ii1o53C08YKyhzpSTICSzILvHMdHFHGeuH1LfrinuLYdyORlC0f6qoSODBSaXO7UI+uHxhb6K3e1YzUYsMRuEjyDUTETeT/b07LIgwIDAQAB";

    public static boolean validConfigFile(String config, String token) {
        try {
            return RsaUtil.decryptData(token, (Key) RsaUtil.getPublicKey(key)).equals(DigestUtils.md5ToHex(config));
        } catch (Exception e) {
            TaoLog.e(TAG, "decrypt fail: " + e.getMessage());
            return false;
        }
    }
}
