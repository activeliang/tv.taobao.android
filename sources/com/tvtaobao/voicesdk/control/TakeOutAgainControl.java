package com.tvtaobao.voicesdk.control;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.tvtaobao.voicesdk.bean.ConfigVO;
import com.tvtaobao.voicesdk.bean.DetailListVO;
import com.tvtaobao.voicesdk.bean.DomainResultVo;
import com.tvtaobao.voicesdk.control.base.BizBaseControl;
import com.tvtaobao.voicesdk.utils.ActivityUtil;
import com.tvtaobao.voicesdk.utils.LogPrint;
import com.tvtaobao.voicesdk.utils.TTSUtils;
import com.tvtaobao.voicesdk.view.PromptDialog;
import com.yunos.tv.alitvasrsdk.CommonData;
import com.yunos.tv.core.common.RequestListener;
import com.yunos.tv.core.util.Utils;
import com.yunos.tv.tao.speech.client.domain.result.multisearch.takeout.TakeoutAgainResultVO;
import com.yunos.tvtaobao.biz.request.BusinessRequest;
import com.yunos.tvtaobao.biz.request.bo.TakeOutBagAgain;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class TakeOutAgainControl extends BizBaseControl {
    public void execute(DomainResultVo domainResultVO) {
        TakeoutAgainResultVO takeoutAgainResultVO = (TakeoutAgainResultVO) domainResultVO.getResultVO();
        if (takeoutAgainResultVO != null && takeoutAgainResultVO.getDetailList() != null) {
            buyAgain(takeoutAgainResultVO);
        } else if (!TextUtils.isEmpty(domainResultVO.getToUri())) {
            gotoActivity(domainResultVO.getToUri());
        } else {
            TTSUtils.getInstance().showDialog(ActivityUtil.getTopActivity());
        }
        Map<String, String> params = Utils.getProperties();
        params.put(CommonData.TYPE_ASR, ConfigVO.asr_text);
        params.put("query", "");
        params.put("tts", domainResultVO.getSpoken());
        Utils.utCustomHit("Voice_Foodrecur", params);
    }

    private void buyAgain(TakeoutAgainResultVO resultVO) {
        LogPrint.e(this.TAG, "Buy again = " + resultVO.getTbMainOrderId());
        List<DetailListVO> productInfoBases = resultVO.getDetailList();
        JSONArray array = new JSONArray();
        try {
            for (DetailListVO detailListVO : productInfoBases) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("itemId", detailListVO.getId());
                jsonObject.put("skuId", detailListVO.getSkuId());
                jsonObject.put("quantity", detailListVO.getQuantity());
                jsonObject.put("itemTitle", detailListVO.getName());
                array.put(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String shopId = resultVO.getStoreId();
        String par = array.toString();
        if (!TextUtils.isEmpty(par) && !TextUtils.isEmpty(shopId)) {
            BusinessRequest.getBusinessRequest().requestTakeOutAgain(resultVO.getStoreId(), par, new GetBagAgainListener(shopId));
        }
    }

    class GetBagAgainListener implements RequestListener<TakeOutBagAgain> {
        String mShopId;

        public GetBagAgainListener(String shopId) {
            this.mShopId = shopId;
        }

        public void onRequestDone(TakeOutBagAgain data, int resultCode, String msg) {
            if (data != null && data.success) {
                Intent intent = new Intent();
                intent.setFlags(268435456);
                intent.setData(Uri.parse("tvtaobao://home?app=takeout&module=takeouthome&shopId=" + this.mShopId + "&from=voice_application"));
                ((Service) TakeOutAgainControl.mWeakService.get()).startActivity(intent);
            } else if (data != null && !TextUtils.isEmpty(data.errorDesc)) {
                Intent intent2 = new Intent();
                intent2.setClass((Context) TakeOutAgainControl.mWeakService.get(), PromptDialog.class);
                intent2.setFlags(268435456);
                intent2.putExtra("errorDesc", data.errorDesc);
                ((Service) TakeOutAgainControl.mWeakService.get()).startActivity(intent2);
            }
        }
    }
}
