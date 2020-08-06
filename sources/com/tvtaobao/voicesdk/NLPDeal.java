package com.tvtaobao.voicesdk;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.text.TextUtils;
import com.tvtaobao.voicesdk.bean.ConfigVO;
import com.tvtaobao.voicesdk.bean.DomainResultVo;
import com.tvtaobao.voicesdk.bean.PageReturn;
import com.tvtaobao.voicesdk.control.base.BizBaseBuilder;
import com.tvtaobao.voicesdk.control.base.BizBaseControl;
import com.tvtaobao.voicesdk.request.NlpNewRequest;
import com.tvtaobao.voicesdk.type.DomainType;
import com.tvtaobao.voicesdk.utils.LogPrint;
import com.tvtaobao.voicesdk.utils.TTSUtils;
import com.yunos.tv.core.common.RequestListener;
import com.yunos.tv.core.util.Utils;
import com.yunos.tvtaobao.biz.request.BusinessRequest;
import com.yunos.tvtaobao.biz.request.bo.SearchResult;
import com.yunos.tvtaobao.biz.request.info.GlobalConfigInfo;
import com.yunos.tvtaobao.payment.utils.TvTaoSharedPerference;
import java.util.ArrayList;

public class NLPDeal extends BizBaseControl {
    protected static final String TAG = "NLPDeal";
    /* access modifiers changed from: private */
    public addressReceived addressReceiver;
    /* access modifiers changed from: private */
    public String asr_text;
    /* access modifiers changed from: private */
    public IntentFilter intentFilter;
    /* access modifiers changed from: private */
    public DomainResultVo mDomainResultVo;
    /* access modifiers changed from: private */
    public boolean mReceiverTag = false;

    public void nlpRequest(String asr_text2) {
        if (!TextUtils.isEmpty(asr_text2)) {
            this.asr_text = asr_text2;
            LogPrint.i(TAG, "NLPDeal.nlpRequest asr : " + asr_text2);
            BusinessRequest.getBusinessRequest().baseRequest(new NlpNewRequest(asr_text2), new NlpListener(), false, true, 5000);
        }
    }

    public void addCart(String itemId) {
        BusinessRequest.getBusinessRequest().addBag(itemId, 1, (String) null, "", new AddCartListener());
    }

    public void manageFav(String itemId) {
        BusinessRequest.getBusinessRequest().manageFav(itemId, "addAuction", new ManageFavListener());
    }

    public void execute(DomainResultVo domainResultVO) {
    }

    class NlpListener implements RequestListener<DomainResultVo> {
        NlpListener() {
        }

        public void onRequestDone(DomainResultVo domainResultVo, int resultCode, String msg) {
            boolean blockWhParams = true;
            if (domainResultVo == null) {
                NLPDeal.this.notDeal();
                return;
            }
            DomainResultVo unused = NLPDeal.this.mDomainResultVo = domainResultVo;
            TTSUtils.getInstance().setDomainResult(domainResultVo);
            if (domainResultVo.getTaobaoLogin() == null || domainResultVo.getTaobaoLogin().intValue() != 1) {
                if (domainResultVo.getElemBind() != null && domainResultVo.getElemBind().intValue() == 1) {
                    NLPDeal.this.setElemBindCallBack();
                }
                if (domainResultVo.getAddressSwitch() == null || domainResultVo.getAddressSwitch().intValue() != 1) {
                    LogPrint.e(NLPDeal.TAG, "NlpListener domain : " + domainResultVo.getDomain() + " ,intent : " + domainResultVo.getIntent());
                    LogPrint.i(NLPDeal.TAG, "NlpListener semantic : " + domainResultVo.getSemantic());
                    ConfigVO.asr_text = NLPDeal.this.asr_text;
                    if (DomainType.TYPE_ONLINE_SHOPPING.equals(domainResultVo.getDomain())) {
                        PageReturn pageReturn = ASRNotify.getInstance().isAction(domainResultVo);
                        if (pageReturn == null || !pageReturn.isHandler) {
                            LogPrint.i("TVTao_NLPDeal", "NLPDeal BizBaseBuilder.builder");
                            BizBaseControl bizBaseControl = BizBaseBuilder.builder(domainResultVo.getIntent());
                            if (bizBaseControl != null) {
                                if (!TextUtils.isEmpty(domainResultVo.getLoadingTxt())) {
                                    NLPDeal.this.alreadyDeal(domainResultVo.getLoadingTxt());
                                }
                                bizBaseControl.init(NLPDeal.mWeakService, NLPDeal.this.mWeakListener);
                                bizBaseControl.execute(domainResultVo);
                            } else {
                                NLPDeal.this.notDeal();
                            }
                        } else {
                            NLPDeal.this.alreadyDeal(pageReturn.feedback);
                        }
                    } else {
                        NLPDeal.this.notDeal();
                        Utils.utCustomHit("Voice_asr_unknown", NLPDeal.this.getProperties(NLPDeal.this.asr_text));
                    }
                    String unused2 = NLPDeal.this.asr_text = null;
                    if (NLPDeal.this.mReceiverTag && NLPDeal.this.addressReceiver != null) {
                        boolean unused3 = NLPDeal.this.mReceiverTag = false;
                        ((Service) NLPDeal.mWeakService.get()).unregisterReceiver(NLPDeal.this.addressReceiver);
                        return;
                    }
                    return;
                }
                LogPrint.i(NLPDeal.TAG, "NlpListener startAddressActivity");
                if (!NLPDeal.this.mReceiverTag) {
                    boolean unused4 = NLPDeal.this.mReceiverTag = true;
                    addressReceived unused5 = NLPDeal.this.addressReceiver = new addressReceived();
                    IntentFilter unused6 = NLPDeal.this.intentFilter = new IntentFilter();
                    NLPDeal.this.intentFilter.addAction("com.yunos.tvtaobao.address");
                    ((Service) NLPDeal.mWeakService.get()).registerReceiver(NLPDeal.this.addressReceiver, NLPDeal.this.intentFilter);
                }
                LogPrint.e(NLPDeal.TAG, "需要地址");
                if (GlobalConfigInfo.getInstance().getGlobalConfig() != null) {
                    blockWhParams = GlobalConfigInfo.getInstance().getGlobalConfig().isBlockWhParams();
                } else if (NLPDeal.mWeakService.get() != null) {
                    Object localBlock = TvTaoSharedPerference.getSp((Context) NLPDeal.mWeakService.get(), "blockWhParams", Boolean.FALSE);
                    if (!(localBlock instanceof Boolean) || !((Boolean) localBlock).booleanValue()) {
                        blockWhParams = false;
                    }
                } else {
                    blockWhParams = false;
                }
                NLPDeal.this.gotoActivity("tvtaobao://home?module=common&page=https://tvos.taobao.com/wow/yunos/act/yuyinwaimaixuanzhe?" + (blockWhParams ? "isJump=false" : "wh_isJump=false"));
                return;
            }
            LogPrint.i(NLPDeal.TAG, "NlpListener startLoginActivity");
            NLPDeal.this.startLoginActivity();
            NLPDeal.this.onTTS("请扫码登录");
        }
    }

    public class addressReceived extends BroadcastReceiver {
        public addressReceived() {
        }

        public void onReceive(Context context, Intent intent) {
            LogPrint.e(NLPDeal.TAG, "addressReceived " + intent.getAction());
            if ("com.yunos.tvtaobao.address".equals(intent.getAction())) {
                NLPDeal.this.nlpRequest(NLPDeal.this.asr_text);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onLoginHandle() {
        super.onLoginHandle();
        if (!TextUtils.isEmpty(this.asr_text)) {
            LogPrint.e(TAG, this.asr_text);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    NLPDeal.this.nlpRequest(NLPDeal.this.asr_text);
                }
            }, 1000);
        }
    }

    /* access modifiers changed from: protected */
    public void onLogin() {
        super.onLogin();
        LogPrint.e(TAG, "NLPDeal登录成功");
        if (!TextUtils.isEmpty(this.asr_text)) {
            LogPrint.e(TAG, this.asr_text);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    NLPDeal.this.nlpRequest(NLPDeal.this.asr_text);
                    String unused = NLPDeal.this.asr_text = null;
                }
            }, 1000);
        }
    }

    class AddCartListener implements RequestListener<ArrayList<SearchResult>> {
        AddCartListener() {
        }

        public void onRequestDone(ArrayList<SearchResult> arrayList, int resultCode, String msg) {
            LogPrint.e(NLPDeal.TAG, "NLPDeal.AddCartListener Code : " + resultCode + " ,msg : " + msg);
            if (resultCode == 200) {
                NLPDeal.this.onTTS("加入购物车成功，您还想买什么？");
            } else {
                NLPDeal.this.onTTS(msg);
            }
        }
    }

    class ManageFavListener implements RequestListener<String> {
        ManageFavListener() {
        }

        public void onRequestDone(String data, int resultCode, String msg) {
            LogPrint.e(NLPDeal.TAG, "NLPDeal.ManageFavListener Code : " + resultCode + " ,msg : " + msg);
            if (resultCode == 200) {
                NLPDeal.this.onTTS("加入收藏成功，您还想买什么？");
            } else {
                NLPDeal.this.onTTS(msg);
            }
        }
    }
}
