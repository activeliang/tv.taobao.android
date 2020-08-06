package anet.channel;

public interface IAuth {

    public interface AuthCallback {
        void onAuthFail(int i, String str);

        void onAuthSuccess();
    }

    void auth(Session session, AuthCallback authCallback);
}
