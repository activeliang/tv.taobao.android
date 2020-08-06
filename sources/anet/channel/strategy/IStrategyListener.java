package anet.channel.strategy;

import anet.channel.strategy.StrategyResultParser;

public interface IStrategyListener {
    void onStrategyUpdated(StrategyResultParser.HttpDnsResponse httpDnsResponse);
}
