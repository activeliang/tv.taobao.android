package com.yunos.tv.core.config;

import com.yunos.RunMode;
import com.yunos.alitvcompliance.TVCompliance;
import com.yunos.alitvcompliance.types.RetCode;
import com.yunos.alitvcompliance.types.RetData;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.HashMap;
import java.util.Map;

public class PageMapConfig {
    private static final String TAG = PageMapConfig.class.getSimpleName();
    private static Map<String, String> mPageUrlMap;

    static {
        initPageUrlMap();
    }

    private static void initPageUrlMap() {
        if (mPageUrlMap == null) {
            mPageUrlMap = new HashMap();
        }
        String domainName = "tvos.taobao.com";
        if (RunMode.needDomainCompliance()) {
            try {
                RetData retData = TVCompliance.getComplianceDomain(domainName);
                ZpLogger.d(TAG, "Converted code is " + retData.toString());
                if (retData.getCode() == RetCode.Success || retData.getCode() == RetCode.Default) {
                    ZpLogger.d(TAG, "Converted domain is " + retData.getResult());
                } else {
                    ZpLogger.d(TAG, "Original domain is " + retData.getResult());
                }
                domainName = retData.getResult();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        mPageUrlMap.put("home", "https://" + domainName + "/wow/yunos/act/home");
        mPageUrlMap.put("chaoshi", "https://" + domainName + "/wow/yunos/act/tvchaoshi");
        mPageUrlMap.put("chaoshi_searchresult", "https://" + domainName + "/wow/chaoshi/act/cssr");
        mPageUrlMap.put("orderlist", "https://" + domainName + "/wow/yunos/act/order");
        if (Config.getRunMode() == RunMode.PREDEPLOY) {
            mPageUrlMap.put("shopbliz", "https://pre-wormhole.tmall.com/wow/yunos/act/shop-home");
        } else {
            mPageUrlMap.put("shopbliz", "https://" + domainName + "/wow/yunos/act/shop-home");
        }
        mPageUrlMap.put(BaseConfig.INTENT_KEY_MODULE_POING, "https://" + domainName + "/wow/yunos/act/myjifen");
        mPageUrlMap.put("todaygoods", "https://" + domainName + "/wow/yunos/act/tvtb-ntoday");
        mPageUrlMap.put("coupon", "https://" + domainName + "/wow/yunos/act/ka-quan-bao");
        mPageUrlMap.put(BaseConfig.INTENT_KEY_MODULE_TAKEOUTSEARCHTEST, "https://" + domainName + "/wow/yunos/24965/tvtaobaoele");
        mPageUrlMap.put("elemeshops", "https://" + domainName + "/wow/yunos/24969/testeleindex");
        String domainName1 = "h5.m.taobao.com";
        if (RunMode.needDomainCompliance()) {
            try {
                RetData retData1 = TVCompliance.getComplianceDomain("h5.m.taobao.com");
                ZpLogger.d(TAG, "Converted code is " + retData1.toString());
                if (retData1.getCode() == RetCode.Success || retData1.getCode() == RetCode.Default) {
                    ZpLogger.d(TAG, "Converted domain is " + retData1.getResult());
                } else {
                    ZpLogger.d(TAG, "Original domain is " + retData1.getResult());
                }
                domainName1 = retData1.getResult();
            } catch (Throwable e2) {
                e2.printStackTrace();
            }
        }
        mPageUrlMap.put("chongzhi", "https://" + domainName1 + "/yuntv/tvphonepay.html");
        mPageUrlMap.put("recommend", "https://" + domainName1 + "/yuntv/recommend.html");
        mPageUrlMap.put("relativerecommend", "https://" + domainName1 + "/yuntv/detailrecommend.html");
        mPageUrlMap.put("tiantian", "https://" + domainName1 + "/yuntv/saledetail.html");
        mPageUrlMap.put("fenlei", "https://" + domainName1 + "/yuntv/subsite.html");
        mPageUrlMap.put("zhuhuichang", "https://" + domainName1 + "/yuntv/mainsite.html");
    }

    public static Map<String, String> getPageUrlMap() {
        return mPageUrlMap;
    }
}
