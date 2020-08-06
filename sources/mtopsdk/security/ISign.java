package mtopsdk.security;

import android.support.annotation.NonNull;
import java.util.HashMap;
import mtopsdk.mtop.global.MtopConfig;

public interface ISign {
    String getAppKey(SignCtx signCtx);

    String getAvmpSign(String str, String str2, int i);

    String getCommonHmacSha1Sign(String str, String str2);

    String getMtopApiSign(HashMap<String, String> hashMap, String str, String str2);

    String getSecBodyDataEx(String str, String str2, String str3, int i);

    void init(@NonNull MtopConfig mtopConfig);

    public static class SignCtx {
        public String authCode;
        public int index;

        public SignCtx(int index2, String authCode2) {
            this.index = index2;
            this.authCode = authCode2;
        }
    }
}
