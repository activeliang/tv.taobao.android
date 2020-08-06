package mtopsdk.network.util;

import anetwork.channel.Header;
import anetwork.channel.Request;
import anetwork.channel.entity.BasicHeader;
import anetwork.channel.entity.RequestImpl;
import anetwork.channel.statist.StatisticData;
import anetwork.channel.util.RequestConstant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.StringUtils;
import mtopsdk.network.domain.NetworkStats;
import mtopsdk.network.domain.ParcelableRequestBodyImpl;
import mtopsdk.network.impl.ParcelableRequestBodyEntry;

public final class ANetworkConverter {
    public static List<Header> createRequestHeaders(Map<String, String> headerMap) {
        if (headerMap == null || headerMap.size() < 1) {
            return null;
        }
        List<Header> headers = new ArrayList<>();
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            if (entry != null && StringUtils.isNotBlank(entry.getKey())) {
                headers.add(new BasicHeader(entry.getKey(), entry.getValue()));
            }
        }
        return headers;
    }

    public static Request convertRequest(mtopsdk.network.domain.Request request) {
        Request req = new RequestImpl(request.url);
        req.setSeqNo(request.seqNo);
        req.setRetryTime(request.retryTimes);
        req.setConnectTimeout(request.connectTimeoutMills);
        req.setReadTimeout(request.readTimeoutMills);
        req.setBizId(request.bizId);
        req.setMethod(request.method);
        req.setHeaders(createRequestHeaders(request.headers));
        req.setExtProperty("APPKEY", request.appKey);
        req.setExtProperty(RequestConstant.AUTH_CODE, request.authCode);
        switch (request.env) {
            case 0:
                req.setExtProperty(RequestConstant.ENVIRONMENT, RequestConstant.ENV_ONLINE);
                break;
            case 1:
                req.setExtProperty(RequestConstant.ENVIRONMENT, RequestConstant.ENV_PRE);
                break;
            case 2:
                req.setExtProperty(RequestConstant.ENVIRONMENT, RequestConstant.ENV_TEST);
                break;
        }
        if ("POST".equalsIgnoreCase(request.method)) {
            ParcelableRequestBodyImpl requestBodyImpl = (ParcelableRequestBodyImpl) request.body;
            req.setBodyEntry(new ParcelableRequestBodyEntry(requestBodyImpl));
            req.addHeader("Content-Type", requestBodyImpl.contentType());
            long length = requestBodyImpl.contentLength();
            if (length > 0) {
                req.addHeader("Content-Length", String.valueOf(length));
            }
        }
        return req;
    }

    public static NetworkStats convertNetworkStats(StatisticData statisticData) {
        if (statisticData == null) {
            return null;
        }
        NetworkStats networkStats = new NetworkStats();
        networkStats.resultCode = statisticData.resultCode;
        networkStats.isRequestSuccess = statisticData.isRequestSuccess;
        networkStats.host = statisticData.host;
        networkStats.ip_port = statisticData.ip_port;
        networkStats.connectionType = statisticData.connectionType;
        networkStats.isSSL = statisticData.isSSL;
        networkStats.oneWayTime_ANet = statisticData.oneWayTime_ANet;
        networkStats.firstDataTime = statisticData.firstDataTime;
        networkStats.sendWaitTime = statisticData.sendBeforeTime;
        networkStats.recDataTime = statisticData.recDataTime;
        networkStats.sendSize = statisticData.sendSize;
        networkStats.recvSize = statisticData.totalSize;
        networkStats.serverRT = statisticData.serverRT;
        networkStats.dataSpeed = statisticData.dataSpeed;
        networkStats.retryTimes = statisticData.retryTime;
        return networkStats;
    }
}
