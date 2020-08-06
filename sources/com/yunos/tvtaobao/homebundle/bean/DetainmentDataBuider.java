package com.yunos.tvtaobao.homebundle.bean;

import android.content.Context;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import com.yunos.tv.core.common.RequestListener;
import com.yunos.tv.core.common.SharePreferences;
import com.yunos.tv.core.common.User;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.util.ActivityPathRecorder;
import com.yunos.tvtaobao.biz.activity.BaseActivity;
import com.yunos.tvtaobao.biz.listener.BizRequestListener;
import com.yunos.tvtaobao.biz.request.BusinessRequest;
import com.yunos.tvtaobao.biz.request.bo.GuessLikeGoodsData;
import com.yunos.tvtaobao.biz.request.bo.RebateBo;
import com.yunos.tvtaobao.biz.util.FileUtil;
import com.yunos.tvtaobao.homebundle.activity.HomeActivity;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetainmentDataBuider {
    private static final String CONTENT = "content";
    private static final String PATH_FILE = "detainment.txt";
    private static final String TAG = "DetainmentDataBuider";
    private static final long TIME_SPACE = 86400000;
    private static final String USERID = "userId";
    private static final String activityCode = "sytucnxh";
    private static final int position = 5;
    private BusinessRequest mBusinessRequest = BusinessRequest.getBusinessRequest();
    private Context mContext;
    private DetainmentRequestListener mDetainmentRequestListener;
    private String mFilePath;

    public interface DetainmentRequestListener {
        boolean onDetainmentRequestDone(List<GuessLikeGoodsData> list);
    }

    public DetainmentDataBuider(Context context) {
        this.mContext = context;
        this.mFilePath = FileUtil.getApplicationPath(context) + WVNativeCallbackUtil.SEPERATER + PATH_FILE;
    }

    public void checkDetainmentData() {
        String current_userId = User.getUserId();
        if (TextUtils.isEmpty(current_userId) && FileUtil.fileIsExists(this.mFilePath)) {
            current_userId = getUserIdFromFile();
        }
        ZpLogger.v(TAG, "DetainmentDataBuider.requestDetainmentData --> current_userId = " + current_userId);
        this.mBusinessRequest.getGuseeLike("back_home_page", activityCode, 5, new DetainMentDataRequestListener(new WeakReference(this), current_userId));
    }

    public boolean isFileValid() {
        boolean isvalid = false;
        if (FileUtil.fileIsExists(this.mFilePath)) {
            File file = new File(this.mFilePath);
            long modifiedTime = file.lastModified();
            long currentTime = System.currentTimeMillis();
            ZpLogger.v(TAG, "DetainmentDataBuider.isFileValid --> modifiedTime = " + modifiedTime + "; currentTime = " + currentTime);
            if (currentTime - modifiedTime <= 86400000) {
                isvalid = true;
            }
            if (!isvalid) {
                file.delete();
            }
        }
        return isvalid;
    }

    public void onRequestData(String current_userId, DetainmentRequestListener l) {
        if (TextUtils.isEmpty(current_userId) && FileUtil.fileIsExists(this.mFilePath)) {
            current_userId = getUserIdFromFile();
        }
        ZpLogger.v(TAG, "DetainmentDataBuider.requestDetainmentData --> current_userId = " + current_userId);
        this.mDetainmentRequestListener = l;
        DetainMentDataRequestListener listener = new DetainMentDataRequestListener(new WeakReference(this), current_userId);
        listener.setNotify(true);
        this.mBusinessRequest.getGuseeLike("back_home_page", activityCode, 5, listener);
    }

    private String getUserIdFromFile() {
        return SharePreferences.getString("userId", (String) null);
    }

    /* access modifiers changed from: private */
    public void saveDetainmentData(String data) {
        if (!TextUtils.isEmpty(data)) {
            SharePreferences.put("userId", data);
        }
    }

    /* access modifiers changed from: private */
    public void onNotify(List<GuessLikeGoodsData> guessLikeGoodsData) {
        List<GuessLikeGoodsData> recommemdList;
        if (guessLikeGoodsData.size() >= 20) {
            recommemdList = guessLikeGoodsData.subList(0, 20);
        } else {
            recommemdList = guessLikeGoodsData;
        }
        if (recommemdList != null) {
            try {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < recommemdList.size(); i++) {
                    GuessLikeGoodsData goodsData = recommemdList.get(i);
                    if (GuessLikeGoodsData.TYPE_ITEM.equals(goodsData.getType())) {
                        String itemId = goodsData.getGuessLikeGoods().getTid();
                        String price = goodsData.getGuessLikeGoods().getDiscount();
                        ZpLogger.e(TAG, "Rebate itemId = " + itemId + " , price =" + price);
                        JSONObject object = new JSONObject();
                        object.put("itemId", itemId);
                        object.put("price", price);
                        jsonArray.put(object);
                    }
                }
                ZpLogger.e(TAG, "Rebate" + jsonArray.toString());
                JSONObject object2 = new JSONObject();
                object2.put("umToken", Config.getUmtoken(this.mContext));
                object2.put("wua", Config.getWua(this.mContext));
                object2.put("isSimulator", Config.isSimulator(this.mContext));
                object2.put("userAgent", Config.getAndroidSystem(this.mContext));
                String extParams = object2.toString();
                this.mBusinessRequest.requestRebateMoney(jsonArray.toString(), ActivityPathRecorder.getInstance().getCurrentPath((HomeActivity) this.mContext), false, false, true, extParams, new GetRebateBusinessRequestListener(recommemdList, new WeakReference((HomeActivity) this.mContext)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void showFindsameRebateResult(List<GuessLikeGoodsData> goodsData, List<RebateBo> data) {
        if (data != null && data.size() > 0) {
            for (RebateBo rebateBo : data) {
                Iterator<GuessLikeGoodsData> it = goodsData.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    GuessLikeGoodsData recommendGoods = it.next();
                    if (GuessLikeGoodsData.TYPE_ITEM.equals(recommendGoods.getType()) && rebateBo != null && recommendGoods != null && rebateBo.getItemId().equals(recommendGoods.getGuessLikeGoods().getTid())) {
                        recommendGoods.getGuessLikeGoods().setRebateBo(rebateBo);
                        break;
                    }
                }
            }
            if (goodsData != null && this.mDetainmentRequestListener != null) {
                this.mDetainmentRequestListener.onDetainmentRequestDone(goodsData);
            }
        } else if (goodsData != null && this.mDetainmentRequestListener != null) {
            this.mDetainmentRequestListener.onDetainmentRequestDone(goodsData);
        }
    }

    private class GetRebateBusinessRequestListener extends BizRequestListener<List<RebateBo>> {
        List<GuessLikeGoodsData> recommemdList;

        public GetRebateBusinessRequestListener(List<GuessLikeGoodsData> recommemdList2, WeakReference<BaseActivity> baseActivityRef) {
            super(baseActivityRef);
            this.recommemdList = recommemdList2;
        }

        public boolean onError(int resultCode, String msg) {
            return false;
        }

        public void onSuccess(List<RebateBo> data) {
            DetainmentDataBuider.this.showFindsameRebateResult(this.recommemdList, data);
        }

        public boolean ifFinishWhenCloseErrorDialog() {
            return false;
        }
    }

    private static class DetainMentDataRequestListener implements RequestListener<List<GuessLikeGoodsData>> {
        private boolean notify = false;
        private WeakReference<DetainmentDataBuider> reference;
        private String user_id;

        public DetainMentDataRequestListener(WeakReference<DetainmentDataBuider> ref, String user_id2) {
            this.reference = ref;
            this.user_id = user_id2;
        }

        public void setNotify(boolean notify2) {
            this.notify = notify2;
        }

        public void onRequestDone(List<GuessLikeGoodsData> data, int resultCode, String msg) {
            if (this.reference != null && this.reference.get() != null) {
                DetainmentDataBuider detainmentDataBuider = (DetainmentDataBuider) this.reference.get();
                if (resultCode == 200 && detainmentDataBuider != null) {
                    detainmentDataBuider.saveDetainmentData(this.user_id);
                    if (this.notify) {
                        detainmentDataBuider.onNotify(data);
                    }
                }
            }
        }
    }
}
