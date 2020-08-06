package anet.channel.strategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StrategyTemplate {
    Map<String, ConnProtocol> templateMap = new ConcurrentHashMap();

    public static StrategyTemplate getInstance() {
        return holder.instance;
    }

    static class holder {
        static StrategyTemplate instance = new StrategyTemplate();

        holder() {
        }
    }

    public void registerConnProtocol(String host, ConnProtocol protocol) {
        if (protocol != null) {
            this.templateMap.put(host, protocol);
        }
    }

    public ConnProtocol getConnProtocol(String host) {
        return this.templateMap.get(host);
    }
}
