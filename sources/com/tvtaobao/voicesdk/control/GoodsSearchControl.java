package com.tvtaobao.voicesdk.control;

import android.app.Service;
import android.content.Intent;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.tvtaobao.voicesdk.base.SDKInitConfig;
import com.tvtaobao.voicesdk.bean.CommandReturn;
import com.tvtaobao.voicesdk.bean.ConfigVO;
import com.tvtaobao.voicesdk.bean.DomainResultVo;
import com.tvtaobao.voicesdk.bean.SearchObject;
import com.tvtaobao.voicesdk.control.base.BizBaseControl;
import com.tvtaobao.voicesdk.listener.VoiceListener;
import com.tvtaobao.voicesdk.request.VoiceSearch;
import com.tvtaobao.voicesdk.utils.IntentSearchData;
import com.tvtaobao.voicesdk.utils.JSONUtil;
import com.tvtaobao.voicesdk.utils.LogPrint;
import com.yunos.tv.core.common.RequestListener;
import com.yunos.tv.core.config.AppInfo;
import com.yunos.tv.core.config.TvOptionsChannel;
import com.yunos.tv.core.config.TvOptionsConfig;
import com.yunos.tv.tao.speech.client.domain.result.multisearch.item.SearchItemDO;
import com.yunos.tv.tao.speech.client.domain.result.multisearch.item.SearchItemResultVO;
import com.yunos.tvtaobao.biz.request.BusinessRequest;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoodsSearchControl extends BizBaseControl {
    private final String TAG = "GoodsSearchControl";
    /* access modifiers changed from: private */
    public SearchObject searchConfig = ConfigVO.searchConfig;

    public void execute(DomainResultVo domainResultVO) {
        LogPrint.i("GoodsSearchControl", "GoodsSearchControl.execute");
        SearchItemResultVO searchItemResultVO = (SearchItemResultVO) domainResultVO.getResultVO();
        if (SDKInitConfig.needShowUI()) {
            if (searchItemResultVO == null) {
                searchItemResultVO = new SearchItemResultVO();
            }
            searchItemResultVO.setSpoken(domainResultVO.getSpoken());
            searchItemResultVO.setTips(domainResultVO.getTips());
            Intent intent = new Intent();
            intent.setClassName(AppInfo.getPackageName(), "com.yunos.voice.activity.GoodsSearchActivity");
            IntentSearchData.getIntence().setSearchItemResultVO(searchItemResultVO);
            intent.putExtra("keyword", searchItemResultVO.getKeywords());
            intent.setFlags(402653184);
            ((Service) mWeakService.get()).startActivity(intent);
        } else if (searchItemResultVO == null) {
            onTTS(domainResultVO.getSpoken());
        } else {
            LogPrint.e("GoodsSearchControl", "GoodsSearchControl.SearchResponse searchResult showUI is false");
            if (this.mWeakListener == null || this.mWeakListener.get() == null) {
                notDeal();
                return;
            }
            ((VoiceListener) this.mWeakListener.get()).searchResult(JSON.toJSONString(searchItemResultVO.getSearchItemDO()));
        }
    }

    public void setSearchConfig(SearchObject config) {
        this.searchConfig = config;
    }

    public void requestSearch() {
        LogPrint.i("GoodsSearchControl", "gotoSearchRequest words : " + this.searchConfig.keyword);
        if (this.searchConfig != null || !TextUtils.isEmpty(this.searchConfig.keyword)) {
            TvOptionsConfig.setTvOptionsVoice(true);
            TvOptionsConfig.setTvOptionsSystem(false);
            TvOptionsConfig.setTvOptionsChannel(TvOptionsChannel.VOICE_SEARCH_ORDER);
            BusinessRequest.getBusinessRequest().baseRequest((BaseMtopRequest) new VoiceSearch(this.searchConfig, TvOptionsConfig.getTvOptions()), new SearchListener(), false);
            return;
        }
        onTTS("不好意思，你想买什么呢");
    }

    class SearchListener implements RequestListener<JSONObject> {
        SearchListener() {
        }

        public void onRequestDone(JSONObject data, int resultCode, String msg) {
            LogPrint.e("GoodsSearchControl", "GoodsSearchControl.SearchResponse resultCode : " + resultCode + " ,msg : " + msg);
            if (resultCode == 200) {
                LogPrint.e("GoodsSearchControl", "GoodsSearchControl.SearchResponse showUI :" + GoodsSearchControl.this.searchConfig.showUI);
                if (SDKInitConfig.needShowUI()) {
                    SearchItemDO searchItemDO = (SearchItemDO) JSON.parseObject(data.toString(), SearchItemDO.class);
                    SearchItemResultVO searchItemResultVO = new SearchItemResultVO();
                    searchItemResultVO.setSearchItemDO(searchItemDO);
                    searchItemResultVO.setKeywords(searchItemDO.getKeyword());
                    String spoken = JSONUtil.getString(data, "spoken");
                    JSONArray tipsArray = JSONUtil.getArray(data, "tips");
                    ArrayList<String> tips = new ArrayList<>();
                    if (tipsArray != null) {
                        int i = 0;
                        while (i < tipsArray.length()) {
                            try {
                                tips.add(tipsArray.getString(i));
                                i++;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    searchItemResultVO.setSpoken(spoken);
                    searchItemResultVO.setTips(tips);
                    Intent intent = new Intent();
                    intent.setClassName(AppInfo.getPackageName(), "com.yunos.voice.activity.GoodsSearchActivity");
                    IntentSearchData.getIntence().setSearchItemResultVO(searchItemResultVO);
                    intent.putExtra("keyword", searchItemResultVO.getKeywords());
                    intent.setFlags(402653184);
                    ((Service) GoodsSearchControl.mWeakService.get()).startActivity(intent);
                    return;
                }
                LogPrint.e("GoodsSearchControl", "GoodsSearchControl.SearchResponse searchResult showUI is false");
                if (GoodsSearchControl.this.mWeakListener == null || GoodsSearchControl.this.mWeakListener.get() == null) {
                    GoodsSearchControl.this.notDeal();
                } else {
                    ((VoiceListener) GoodsSearchControl.this.mWeakListener.get()).searchResult(data.toString());
                }
            } else {
                LogPrint.e("GoodsSearchControl", "GoodsSearchControl.SearchResponse FAILURE    msg : " + msg);
                CommandReturn errorReturn = new CommandReturn();
                errorReturn.mIsHandled = true;
                errorReturn.mAction = 1008;
                errorReturn.mASRMessage = ConfigVO.asr_text;
                errorReturn.mMessage = msg;
                errorReturn.mCode = resultCode;
                ((VoiceListener) GoodsSearchControl.this.mWeakListener.get()).callback(errorReturn);
            }
        }
    }
}
