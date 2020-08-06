package com.tvtaobao.voicesdk.control.base;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.Site;
import com.ali.user.open.ucc.BindComponentProxy;
import com.ali.user.open.ucc.UccCallback;
import com.ali.user.open.ucc.UccService;
import com.tvtaobao.voicesdk.bean.CommandReturn;
import com.tvtaobao.voicesdk.bean.ConfigVO;
import com.tvtaobao.voicesdk.bean.DomainResultVo;
import com.tvtaobao.voicesdk.listener.VoiceListener;
import com.tvtaobao.voicesdk.utils.DialogManager;
import com.tvtaobao.voicesdk.utils.LogPrint;
import com.yunos.CloudUUIDWrapper;
import com.yunos.tv.alitvasrsdk.CommonData;
import com.yunos.tv.blitz.account.LoginHelper;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.aqm.ActivityQueueManager;
import com.yunos.tv.core.common.RequestListener;
import com.yunos.tv.core.common.User;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.listener.TvBaoELemeBindGuideListener;
import com.yunos.tv.core.util.SharedPreferencesUtils;
import com.yunos.tv.core.util.TaokeConst;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.request.BusinessRequest;
import com.yunos.tvtaobao.blitz.account.AccountActivityHelper;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public abstract class BizBaseControl {
    /* access modifiers changed from: protected */
    public static WeakReference<Service> mWeakService;
    protected final String TAG = BizBaseControl.class.getSimpleName();
    private AccountActivityHelper mAccountActivityHelper;
    private AccountActivityHelper.OnAccountStateChangedListener mOnAccountStateChangedListener;
    /* access modifiers changed from: protected */
    public WeakReference<VoiceListener> mWeakListener;

    public abstract void execute(DomainResultVo domainResultVo);

    protected BizBaseControl() {
        LogPrint.e(this.TAG, TaokeConst.REFERER_BIZBASE_CONTROL);
        this.mAccountActivityHelper = new AccountActivityHelper();
        CoreApplication.getLoginHelper(CoreApplication.getApplication()).addSyncLoginListener(new LoginHelper.SyncLoginListener() {
            public void onLogin(boolean isSuccess) {
                if (isSuccess) {
                    LogPrint.e(BizBaseControl.this.TAG, BizBaseControl.this.TAG + "login success");
                    BizBaseControl.this.onLoginHandle();
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onLoginHandle() {
    }

    public void init(WeakReference<Service> service, WeakReference<VoiceListener> listener) {
        mWeakService = service;
        this.mWeakListener = listener;
    }

    public void startLoginActivity() {
        DialogManager.getManager().dismissAllDialog();
        setLoginActivityStartShowing();
        CoreApplication.getLoginHelper(CoreApplication.getApplication()).startYunosAccountActivity(CoreApplication.getApplication(), false);
    }

    /* access modifiers changed from: protected */
    public boolean loginIsCanceled() {
        return this.mAccountActivityHelper.loginIsCanceled();
    }

    public void setLoginActivityStartShowing() {
        this.mAccountActivityHelper.setAccountActivityStartShowing();
    }

    public void setCurrLoginInvalid() {
        this.mAccountActivityHelper.setAccountInvalid();
    }

    /* access modifiers changed from: protected */
    public void registerLoginListener() {
        if (this.mOnAccountStateChangedListener == null) {
            this.mOnAccountStateChangedListener = new AccountActivityHelper.OnAccountStateChangedListener() {
                public void onAccountStateChanged(AccountActivityHelper.AccountLoginState state) {
                    switch (AnonymousClass6.$SwitchMap$com$yunos$tvtaobao$blitz$account$AccountActivityHelper$AccountLoginState[state.ordinal()]) {
                        case 1:
                            BizBaseControl.this.onLogin();
                            return;
                        case 2:
                            BizBaseControl.this.onLogout();
                            return;
                        case 3:
                            BizBaseControl.this.onLoginCancel();
                            return;
                        default:
                            return;
                    }
                }
            };
            this.mAccountActivityHelper.registerAccountActivity(this.mOnAccountStateChangedListener);
        }
    }

    /* renamed from: com.tvtaobao.voicesdk.control.base.BizBaseControl$6  reason: invalid class name */
    static /* synthetic */ class AnonymousClass6 {
        static final /* synthetic */ int[] $SwitchMap$com$yunos$tvtaobao$blitz$account$AccountActivityHelper$AccountLoginState = new int[AccountActivityHelper.AccountLoginState.values().length];

        static {
            try {
                $SwitchMap$com$yunos$tvtaobao$blitz$account$AccountActivityHelper$AccountLoginState[AccountActivityHelper.AccountLoginState.LOGIN.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$yunos$tvtaobao$blitz$account$AccountActivityHelper$AccountLoginState[AccountActivityHelper.AccountLoginState.LOGOUT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$yunos$tvtaobao$blitz$account$AccountActivityHelper$AccountLoginState[AccountActivityHelper.AccountLoginState.CANCEL.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    /* access modifiers changed from: protected */
    public void unRegisterLoginListener() {
        if (this.mOnAccountStateChangedListener != null) {
            this.mAccountActivityHelper.unRegisterAccountActivity(this.mOnAccountStateChangedListener);
        }
    }

    /* access modifiers changed from: protected */
    public void onLogin() {
        if (CoreApplication.getLoginHelper(CoreApplication.getApplication()).isLogin()) {
            BusinessRequest.getBusinessRequest().requestTaokeLoginAnalysis(User.getNick(), "tvtaobao", TaokeConst.REFERER_BIZBASE_CONTROL, new TaokeBussinessRequestListener());
        }
    }

    /* access modifiers changed from: protected */
    public void onLogout() {
    }

    /* access modifiers changed from: protected */
    public void onLoginCancel() {
        LogPrint.i(this.TAG, "onLoginCancel");
    }

    /* access modifiers changed from: protected */
    public void onTTS(String s) {
        if (this.mWeakListener == null || this.mWeakListener.get() == null) {
            notDeal();
            return;
        }
        CommandReturn commandReturn = new CommandReturn();
        commandReturn.mIsHandled = true;
        commandReturn.mAction = 1001;
        commandReturn.mMessage = s;
        ((VoiceListener) this.mWeakListener.get()).callback(commandReturn);
    }

    /* access modifiers changed from: protected */
    public void notDeal() {
        LogPrint.d(this.TAG, this.TAG + ".notDeal mWeakListener : " + this.mWeakListener);
        if (this.mWeakListener == null || this.mWeakListener.get() == null) {
            LogPrint.d(this.TAG, this.TAG + ".notDeal");
            return;
        }
        CommandReturn commandReturn = new CommandReturn();
        commandReturn.mIsHandled = false;
        ((VoiceListener) this.mWeakListener.get()).callback(commandReturn);
        DialogManager.getManager().dismissAllDialog();
    }

    /* access modifiers changed from: protected */
    public void alreadyDeal(String feedback) {
        if (this.mWeakListener != null && this.mWeakListener.get() != null) {
            CommandReturn commandReturn = new CommandReturn();
            commandReturn.mIsHandled = true;
            if (!TextUtils.isEmpty(feedback)) {
                commandReturn.mAction = 1005;
                commandReturn.mMessage = feedback;
            }
            ((VoiceListener) this.mWeakListener.get()).callback(commandReturn);
        }
    }

    /* access modifiers changed from: protected */
    public void gotoActivity(String uri) {
        LogPrint.i("TVTao_" + this.TAG, "gotoActivity uri === " + uri);
        if (mWeakService != null && mWeakService.get() != null) {
            LogPrint.i("TVTao_" + this.TAG, this.TAG + " mWeakService.get().startActivity(intent)");
            Intent intent = new Intent();
            intent.setData(Uri.parse(uri));
            intent.setFlags(268435456);
            ((Service) mWeakService.get()).startActivity(intent);
            DialogManager.getManager().dismissAllDialog();
            alreadyDeal("正在为您跳转页面");
        } else if (CoreApplication.getApplication() == null || CoreApplication.getApplication().getApplicationContext() == null) {
            LogPrint.i("TVTao_" + this.TAG, this.TAG + " gotoActivity notDeal ");
            notDeal();
        } else {
            LogPrint.i("TVTao_" + this.TAG, this.TAG + " getApplicationContext().startActivity");
            Intent intent2 = new Intent();
            intent2.setData(Uri.parse(uri));
            intent2.setFlags(268435456);
            CoreApplication.getApplication().getApplicationContext().startActivity(intent2);
            DialogManager.getManager().dismissAllDialog();
            alreadyDeal("正在为您跳转页面");
        }
    }

    public void setElemBindCallBack() {
        TvBaoELemeBindGuideListener.seteLemeBindCallBack(new TvBaoELemeBindGuideListener.IElemBindCallBack() {
            public void onSuccess() {
                LogPrint.e(BizBaseControl.this.TAG, BizBaseControl.this.TAG + "bind success");
                BizBaseControl.this.onLoginHandle();
            }

            public void onFailure() {
            }
        });
        ((UccService) AliMemberSDK.getService(UccService.class)).setBindComponentProxy(new BindComponentProxy() {
            public void openPage(Context context, Bundle bundle, UccCallback uccCallback) {
                BizBaseControl.this.gotoBindActivity("com.yunos.tvtaobao.elem.activity.bind.ElemAuthActivity", bundle);
            }
        });
        ((UccService) AliMemberSDK.getService(UccService.class)).trustLogin(ActivityQueueManager.getTop(), Site.ELEME, (UccCallback) new UccCallback() {
            public void onSuccess(String s, Map map) {
                TvBaoELemeBindGuideListener.onSuccess();
            }

            public void onFail(String s, int i, String s1) {
                if (i == 1114) {
                    BizBaseControl.this.gotoBindActivity(BaseConfig.SWITCH_TO_ELEM_BIND_GUILD, (Bundle) null);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void gotoBindActivity(String className, Bundle bundle) {
        LogPrint.i("TVTao_" + this.TAG, "gotoBindActivity className === " + className);
        if (mWeakService != null && mWeakService.get() != null) {
            LogPrint.i("TVTao_" + this.TAG, this.TAG + " mWeakService.get().startActivity(intent)");
            Intent intent = new Intent();
            intent.setFlags(537001984);
            intent.setClassName((Context) mWeakService.get(), className);
            intent.putExtra("isVoice", true);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            intent.addFlags(268435456);
            ((Service) mWeakService.get()).startActivity(intent);
            DialogManager.getManager().dismissAllDialog();
            alreadyDeal("正在为您跳转页面");
        } else if (CoreApplication.getApplication() == null || CoreApplication.getApplication().getApplicationContext() == null) {
            LogPrint.i("TVTao_" + this.TAG, this.TAG + " gotoActivity notDeal ");
            notDeal();
        } else {
            LogPrint.i("TVTao_" + this.TAG, this.TAG + " getApplicationContext().startActivity");
            Intent intent2 = new Intent();
            intent2.setFlags(537001984);
            intent2.putExtra("isVoice", true);
            if (bundle != null) {
                intent2.putExtras(bundle);
            }
            intent2.setClassName((Context) mWeakService.get(), className);
            CoreApplication.getApplication().getApplicationContext().startActivity(intent2);
            DialogManager.getManager().dismissAllDialog();
            alreadyDeal("正在为您跳转页面");
        }
    }

    /* access modifiers changed from: protected */
    public void callbackErrorInfo(int resultCode, String msg) {
        CommandReturn errorReturn = new CommandReturn();
        errorReturn.mIsHandled = true;
        errorReturn.mAction = 1008;
        errorReturn.mASRMessage = ConfigVO.asr_text;
        errorReturn.mMessage = msg;
        errorReturn.mCode = resultCode;
        ((VoiceListener) this.mWeakListener.get()).callback(errorReturn);
    }

    private class TaokeBussinessRequestListener implements RequestListener<JSONObject> {
        private TaokeBussinessRequestListener() {
        }

        public void onRequestDone(JSONObject data, int resultCode, String msg) {
            if (resultCode == 200) {
                SharedPreferencesUtils.saveTvBuyTaoKe((Context) BizBaseControl.mWeakService.get(), System.currentTimeMillis() + 604800000);
            }
        }
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getProperties() {
        return getProperties(ConfigVO.asr_text);
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getProperties(String asr) {
        Map<String, String> p = new HashMap<>();
        String uuid = CloudUUIDWrapper.getCloudUUID();
        if (!TextUtils.isEmpty(uuid)) {
            p.put("uuid", uuid);
        }
        p.put("channel", Config.getChannelName());
        if (!TextUtils.isEmpty(asr)) {
            p.put(CommonData.TYPE_ASR, asr);
        }
        return p;
    }
}
