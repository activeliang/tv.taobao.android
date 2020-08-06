package anet.channel.strategy;

import java.util.concurrent.ConcurrentHashMap;

public class SchemeGuesser {
    private boolean enabled = true;
    private final ConcurrentHashMap<String, String> guessSchemeMap = new ConcurrentHashMap<>();

    public static SchemeGuesser getInstance() {
        return holder.instance;
    }

    private static class holder {
        static SchemeGuesser instance = new SchemeGuesser();

        private holder() {
        }
    }

    public void setEnabled(boolean enabled2) {
        this.enabled = enabled2;
    }

    public String guessScheme(String host) {
        if (!this.enabled) {
            return null;
        }
        String scheme = this.guessSchemeMap.get(host);
        if (scheme != null) {
            return scheme;
        }
        this.guessSchemeMap.put(host, "https");
        return "https";
    }

    public void onSslFail(String host) {
        this.guessSchemeMap.put(host, "http");
    }
}
