package com.alibaba.analytics.core.config;

import android.net.Uri;
import com.alibaba.analytics.AnalyticsMgr;
import com.alibaba.analytics.core.ClientVariables;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.StringUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class UTTPKBiz {
    private static final String TAG = "UTMCTPKBiz";
    private static UTTPKBiz s_instance = null;
    private String[] TPK_ONLINECONF_KEY = {"B01N16"};
    private Map<String, String> mTPKCache = new HashMap();
    private List<UTTPKItem> mTPKItems = new LinkedList();
    private String mTPKMD5 = null;

    private UTTPKBiz() {
    }

    public synchronized void addTPKItem(UTTPKItem aTPKItem) {
        if (aTPKItem != null) {
            this.mTPKItems.add(aTPKItem);
        }
    }

    public synchronized void addTPKCache(String tpkKey, String tpkValue) {
        if (!StringUtils.isEmpty(tpkKey)) {
            if (tpkValue == null) {
                this.mTPKCache.remove(tpkKey);
            } else {
                this.mTPKCache.put(tpkKey, tpkValue);
            }
        }
    }

    public static synchronized UTTPKBiz getInstance() {
        UTTPKBiz uTTPKBiz;
        synchronized (UTTPKBiz.class) {
            if (s_instance == null) {
                s_instance = new UTTPKBiz();
            }
            uTTPKBiz = s_instance;
        }
        return uTTPKBiz;
    }

    private void _onTPKConfArrive(String pConfName, String pConfContent) {
        Logger.d(TAG, "", "pConfName", pConfName, "pConfContent", pConfContent);
        if (!StringUtils.isEmpty(pConfContent)) {
            try {
                JSONArray lTpks = new JSONArray(pConfContent);
                for (int i = 0; i < lTpks.length(); i++) {
                    JSONObject lTpk = lTpks.optJSONObject(i);
                    if (lTpk != null && lTpk.has("kn") && !lTpk.isNull("kn")) {
                        String lKn = lTpk.getString("kn");
                        if (!"a".equals(lKn)) {
                            UTTPKItem lTPKItem = new UTTPKItem();
                            String lV = lTpk.optString("v");
                            if (StringUtils.isEmpty(lV)) {
                                lV = "${" + lKn + "}";
                            }
                            String lTy = lTpk.optString("ty", UTTPKItem.TYPE_FAR);
                            lTPKItem.setKname(lKn);
                            lTPKItem.setKvalue(lV);
                            lTPKItem.setType(lTy);
                            this.mTPKItems.add(lTPKItem);
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    private String _getTPKValue(String aFetchRule, Uri aPageUri, Map<String, String> aPageProperties) {
        String lValue;
        if (StringUtils.isEmpty(aFetchRule)) {
            return null;
        }
        if (aFetchRule.startsWith("${url|") && aFetchRule.length() > 7) {
            String lRuleKey = aFetchRule.substring("${url|".length(), aFetchRule.length() - 1);
            if (StringUtils.isEmpty(lRuleKey) || aPageUri == null) {
                return null;
            }
            return aPageUri.getQueryParameter(lRuleKey);
        } else if (aFetchRule.startsWith("${ut|") && aFetchRule.length() > 6) {
            String lRuleKey2 = aFetchRule.substring("${ut|".length(), aFetchRule.length() - 1);
            if (StringUtils.isEmpty(lRuleKey2) || aPageProperties == null) {
                return null;
            }
            return aPageProperties.get(lRuleKey2);
        } else if (!aFetchRule.startsWith("${") || aFetchRule.length() <= 3) {
            return aFetchRule;
        } else {
            String lRuleKey3 = aFetchRule.substring("${".length(), aFetchRule.length() - 1);
            if (StringUtils.isEmpty(lRuleKey3)) {
                return null;
            }
            if (aPageProperties != null && (lValue = aPageProperties.get(lRuleKey3)) != null) {
                return lValue;
            }
            if (aPageUri != null) {
                return aPageUri.getQueryParameter(lRuleKey3);
            }
            return null;
        }
    }

    public synchronized String getTpkString(Uri aPageUri, Map<String, String> aPageProperties) {
        String str;
        String lTPKString;
        String lRemoteTPKMD5 = UTClientConfigMgr.getInstance().get("tpk_md5");
        Logger.d("UTTPKBiz", "tpk_md5", lRemoteTPKMD5);
        if (!(lRemoteTPKMD5 == null || lRemoteTPKMD5.equals(this.mTPKMD5) || (lTPKString = AnalyticsMgr.getValue("tpk_string")) == null)) {
            _onTPKConfArrive((String) null, lTPKString);
            this.mTPKMD5 = "" + lTPKString.hashCode();
        }
        Iterator i$ = this.mTPKItems.iterator();
        while (true) {
            if (i$.hasNext()) {
                UTTPKItem lTPKItem = i$.next();
                String lKey = lTPKItem.getKname();
                String lType = lTPKItem.getType();
                String lFetchRule = lTPKItem.getKvalue();
                if (StringUtils.isEmpty(lKey)) {
                    str = null;
                    break;
                } else if (StringUtils.isEmpty(this.mTPKCache.get(lKey))) {
                    String lNewValue = _getTPKValue(lFetchRule, aPageUri, aPageProperties);
                    if (!StringUtils.isEmpty(lNewValue)) {
                        this.mTPKCache.put(lKey, lNewValue);
                    }
                } else if (!UTTPKItem.TYPE_FAR.equals(lType)) {
                    String lNewValue2 = _getTPKValue(lFetchRule, aPageUri, aPageProperties);
                    if (!StringUtils.isEmpty(lNewValue2)) {
                        this.mTPKCache.put(lKey, lNewValue2);
                    }
                }
            } else {
                if (!this.mTPKCache.containsKey("ttid") && !StringUtils.isEmpty(ClientVariables.getInstance().getOutsideTTID())) {
                    this.mTPKCache.put("ttid", ClientVariables.getInstance().getOutsideTTID());
                }
                if (this.mTPKCache.size() > 0) {
                    str = "{" + StringUtils.convertMapToString(this.mTPKCache) + "}";
                } else {
                    str = null;
                }
            }
        }
        return str;
    }

    public synchronized void sessionTimeout() {
        this.mTPKCache.clear();
    }
}
