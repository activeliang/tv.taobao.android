package com.tvtaobao.voicesdk.control;

import android.app.Service;
import android.content.Intent;
import com.alibaba.fastjson.JSON;
import com.tvtaobao.voicesdk.ASRNotify;
import com.tvtaobao.voicesdk.base.SDKInitConfig;
import com.tvtaobao.voicesdk.bean.CommandReturn;
import com.tvtaobao.voicesdk.bean.ConfigVO;
import com.tvtaobao.voicesdk.bean.DomainResultVo;
import com.tvtaobao.voicesdk.control.base.BizBaseControl;
import com.tvtaobao.voicesdk.listener.VoiceListener;
import com.tvtaobao.voicesdk.request.TakeOutSearchRequest;
import com.tvtaobao.voicesdk.utils.IntentSearchData;
import com.tvtaobao.voicesdk.utils.JSONUtil;
import com.tvtaobao.voicesdk.utils.LogPrint;
import com.yunos.tv.core.common.RequestListener;
import com.yunos.tv.core.config.AppInfo;
import com.yunos.tv.tao.speech.client.domain.result.multisearch.takeout.TakeoutSearchItemDO;
import com.yunos.tv.tao.speech.client.domain.result.multisearch.takeout.TakeoutSearchResultVO;
import com.yunos.tvtaobao.biz.request.BusinessRequest;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TakeOutSearchControl extends BizBaseControl {
    private String TAG = "TakeOutSearchControl";

    public void execute(DomainResultVo domainResultVO) {
        LogPrint.i(this.TAG, this.TAG + ".execute");
        Service service = (Service) mWeakService.get();
        if (service == null) {
            LogPrint.i(this.TAG, this.TAG + ".execute service is null");
            return;
        }
        TakeoutSearchResultVO takeoutSearchResultVO = (TakeoutSearchResultVO) domainResultVO.getResultVO();
        takeoutSearchResultVO.setSpoken(domainResultVO.getSpoken());
        takeoutSearchResultVO.setTips(domainResultVO.getTips());
        if (!ASRNotify.getInstance().isActionSearch(takeoutSearchResultVO.getKeywords(), domainResultVO.getSpoken(), domainResultVO.getTips())) {
            LogPrint.i(this.TAG, this.TAG + ".execute showUI : " + SDKInitConfig.needShowUI());
            if (SDKInitConfig.needShowUI()) {
                Intent intent = new Intent();
                intent.setClassName(AppInfo.getPackageName(), "com.yunos.voice.activity.TakeOutSearchActivity");
                IntentSearchData.getIntence().setTakeoutSearchResultVO(takeoutSearchResultVO);
                intent.putExtra("keywords", takeoutSearchResultVO.getShopName());
                intent.setFlags(402653184);
                service.getApplicationContext().startActivity(intent);
                return;
            }
            String resultVO = JSON.toJSONString(takeoutSearchResultVO.getTakeOutSearchItemDO());
            CommandReturn commandReturn = new CommandReturn();
            commandReturn.mIsHandled = true;
            commandReturn.mAction = 1006;
            commandReturn.mASRMessage = ConfigVO.asr_text;
            commandReturn.mData = resultVO;
            ((VoiceListener) this.mWeakListener.get()).callback(commandReturn);
        }
    }

    public void takeoutSearch(String keyword, String offset, int pageSize, String orderType) {
        BusinessRequest.getBusinessRequest().baseRequest((BaseMtopRequest) new TakeOutSearchRequest(keyword, offset, pageSize, SDKInitConfig.getLocation(), orderType), new TakeOutSearchListener(), false);
    }

    private class TakeOutSearchListener implements RequestListener<JSONObject> {
        private TakeOutSearchListener() {
        }

        public void onRequestDone(JSONObject data, int resultCode, String msg) {
            if (resultCode != 200) {
                CommandReturn errorReturn = new CommandReturn();
                errorReturn.mIsHandled = true;
                errorReturn.mAction = 1008;
                errorReturn.mASRMessage = ConfigVO.asr_text;
                errorReturn.mMessage = msg;
                errorReturn.mCode = resultCode;
                ((VoiceListener) TakeOutSearchControl.this.mWeakListener.get()).callback(errorReturn);
            } else if (SDKInitConfig.needShowUI()) {
                TakeoutSearchResultVO takeoutSearchResultVO = new TakeoutSearchResultVO();
                TakeoutSearchItemDO takeoutSearchItemDO = (TakeoutSearchItemDO) JSON.parseObject(data.toString(), TakeoutSearchItemDO.class);
                takeoutSearchResultVO.setTakeOutSearchItemDO(takeoutSearchItemDO);
                takeoutSearchItemDO.setKeyword(takeoutSearchItemDO.getKeyword());
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
                takeoutSearchResultVO.setSpoken(spoken);
                takeoutSearchResultVO.setTips(tips);
                Intent intent = new Intent();
                intent.setClassName(AppInfo.getPackageName(), "com.yunos.voice.activity.TakeOutSearchActivity");
                intent.setFlags(402653184);
                intent.putExtra("keywords", takeoutSearchResultVO.getShopName());
                IntentSearchData.getIntence().setTakeoutSearchResultVO(takeoutSearchResultVO);
                ((Service) TakeOutSearchControl.mWeakService.get()).startActivity(intent);
            } else {
                CommandReturn commandReturn = new CommandReturn();
                commandReturn.mIsHandled = true;
                commandReturn.mAction = 1006;
                commandReturn.mASRMessage = ConfigVO.asr_text;
                commandReturn.mData = data.toString();
                ((VoiceListener) TakeOutSearchControl.this.mWeakListener.get()).callback(commandReturn);
            }
        }
    }
}
