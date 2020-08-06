package com.yunos.tvtaobao.biz.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.bftv.fui.constantplugin.Constant;
import com.tvtaobao.voicesdk.ASRNotify;
import com.tvtaobao.voicesdk.bean.DomainResultVo;
import com.tvtaobao.voicesdk.bean.PageReturn;
import com.tvtaobao.voicesdk.listener.ASRHandler;
import com.tvtaobao.voicesdk.register.LPR;
import com.tvtaobao.voicesdk.utils.ActivityUtil;
import com.yunos.RunMode;
import com.yunos.ott.sdk.core.Environment;
import com.yunos.tv.blitz.activity.BzBaseActivity;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.common.ImageHandleManager;
import com.yunos.tv.core.common.User;
import com.yunos.tv.core.location.LocationAssist;
import com.yunos.tv.core.util.NetWorkUtil;
import com.yunos.tv.core.util.SharedPreferencesUtils;
import com.yunos.tv.core.util.TaokeConst;
import com.yunos.tvtaobao.biz.common.NoPutToStack;
import com.yunos.tvtaobao.biz.dialog.util.DialogUtil;
import com.yunos.tvtaobao.biz.listener.BizRequestListener;
import com.yunos.tvtaobao.biz.listener.NetworkOkDoListener;
import com.yunos.tvtaobao.biz.request.BusinessRequest;
import com.yunos.tvtaobao.biz.request.core.AsyncDataLoader;
import com.yunos.tvtaobao.biz.request.info.GlobalConfigInfo;
import com.yunos.tvtaobao.blitz.account.AccountActivityHelper;
import com.yunos.tvtaobao.businessview.R;
import com.yunos.tvtaobao.tvsdk.widget.focus.FocusPositionManager;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.json.JSONObject;

public abstract class BaseActivity extends CoreActivity {
    /* access modifiers changed from: private */
    public static String TAG = BaseActivity.class.getSimpleName();
    private final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    private final String MENU_URI = "tvtaobao://home?module=menu";
    private AccountActivityHelper mAccountActivityHelper;
    private BusinessRequest mBusinessRequest;
    protected DialogUtil mDialogUtil;
    private SdkBroadcastReceiver mNetworkChangeBroadcastReceiver;
    /* access modifiers changed from: private */
    public NetworkOkDoListener mNetworkOkDoListener;
    private AccountActivityHelper.OnAccountStateChangedListener mOnAccountStateChangedListener;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (RunMode.isYunos() == Environment.getInstance().isYunos()) {
            LocationAssist.getInstance().requestLocationPermission(this);
            ZpLogger.d(TAG, "TAG  ---> " + TAG + ";   ---- >  onCreate;  this =  " + this);
            if (this.mNetworkChangeBroadcastReceiver == null) {
                this.mNetworkChangeBroadcastReceiver = new SdkBroadcastReceiver();
                IntentFilter filter = new IntentFilter();
                filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                filter.setPriority(1000);
                registerReceiver(this.mNetworkChangeBroadcastReceiver, filter);
            }
            this.mAccountActivityHelper = new AccountActivityHelper();
            this.mDialogUtil = new DialogUtil(this);
        }
    }

    /* access modifiers changed from: protected */
    public void startNeedLoginActivity(Intent intent) {
        ZpLogger.d(TAG, "TAG  ---> " + TAG + ";   ---- >  startNeedLoginActivity;  this =  " + this);
        try {
            if (!CoreApplication.getLoginHelper(CoreApplication.getApplication()).isLogin()) {
                setLoginActivityStartShowing();
                CoreApplication.getLoginHelper(CoreApplication.getApplication()).startYunosAccountActivity(this, false);
                return;
            }
        } catch (Exception e) {
            ZpLogger.e(TAG, "get login state exception:" + e);
            setLoginActivityStartShowing();
            CoreApplication.getLoginHelper(CoreApplication.getApplication()).startYunosAccountActivity(this, false);
        }
        startActivity(intent);
    }

    /* access modifiers changed from: protected */
    public void startNeedLoginActivityForResult(Intent intent, int requestCode) {
        ZpLogger.d(TAG, "TAG  ---> " + TAG + ";   ---- >  startNeedLoginActivityForResult;  this =  " + this);
        try {
            if (!CoreApplication.getLoginHelper(CoreApplication.getApplication()).isLogin()) {
                CoreApplication.getLoginHelper(CoreApplication.getApplication()).startYunosAccountActivity(this, false);
                return;
            }
        } catch (Exception e) {
            ZpLogger.e(TAG, "get login state exception:" + e);
            CoreApplication.getLoginHelper(CoreApplication.getApplication()).startYunosAccountActivity(this, false);
        }
        startActivityForResult(intent, requestCode);
    }

    /* access modifiers changed from: protected */
    public void onStartActivityNetWorkError() {
        showNetworkErrorDialog(false);
    }

    public void showNetworkErrorDialog(boolean isfinishActivity) {
        this.mDialogUtil.showNetworkErrorDialog(isfinishActivity);
    }

    public void showErrorDialog(String msg, final boolean isFinishActivity) {
        ZpLogger.d(TAG, "TAG  ---> " + TAG + ";   ---- >   showErrorDialog;  this =  " + this);
        if (this.mDialogUtil != null) {
            this.mDialogUtil.showErrorDialog(msg, getString(R.string.ytbv_confirm), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (isFinishActivity) {
                        BaseActivity.this.finish();
                    }
                }
            }, new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode != 4) {
                        return false;
                    }
                    dialog.dismiss();
                    if (isFinishActivity) {
                        BaseActivity.this.finish();
                    }
                    return true;
                }
            });
        }
    }

    public boolean isHasDestroyActivity() {
        return isFinishing();
    }

    public void showErrorDialog(String msg, DialogInterface.OnClickListener listener, DialogInterface.OnKeyListener onKeyListener) {
        this.mDialogUtil.showErrorDialog(msg, getString(R.string.ytbv_confirm), listener, onKeyListener);
    }

    public void showErrorDialog(String msg, String button, DialogInterface.OnClickListener listener, DialogInterface.OnKeyListener onKeyListener) {
        this.mDialogUtil.showErrorDialog(msg, button, listener, onKeyListener);
    }

    public void showYunosHostPage(Bundle bundle, String pageUrl) throws Exception {
        ZpLogger.d(TAG, TAG + ".showYunosHostPage. bundle = " + bundle);
        Method methodMeta = getClass().getMethod("startHostPage", new Class[]{String.class, String.class, Bundle.class, Boolean.TYPE});
        ZpLogger.d(TAG, TAG + ".showYunosHostPage.methodMeta = " + methodMeta);
        if (methodMeta != null) {
            methodMeta.invoke(this, new Object[]{pageUrl, null, bundle, true});
            ZpLogger.d(TAG, TAG + ".showYunosHostPage ok");
        }
    }

    public void OnWaitProgressDialog(boolean show) {
        if (this.mDialogUtil != null) {
            this.mDialogUtil.OnWaitProgressDialog(show);
        }
    }

    public void onTextProgressDialog(CharSequence text, boolean show) {
        if (this.mDialogUtil != null) {
            this.mDialogUtil.onTextProgressDialog(text, show);
        }
    }

    public void setProgressCancelable(boolean flag) {
        if (this.mDialogUtil != null) {
            this.mDialogUtil.setProgressCancelable(flag);
        }
    }

    /* access modifiers changed from: protected */
    public void changedNetworkStatus(boolean available) {
        ZpLogger.i(TAG, "changedNetworkStatus available=" + available);
    }

    public class SdkBroadcastReceiver extends BroadcastReceiver {
        public SdkBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), "android.net.conn.CONNECTIVITY_CHANGE")) {
                if (NetWorkUtil.isNetWorkAvailable() && BaseActivity.this.mDialogUtil != null) {
                    BaseActivity.this.mDialogUtil.networkDialogDismiss();
                    if (BaseActivity.this.mNetworkOkDoListener != null) {
                        BaseActivity.this.mNetworkOkDoListener.todo();
                    }
                }
                BaseActivity.this.changedNetworkStatus(NetWorkUtil.isNetWorkAvailable());
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        if (RunMode.isYunos() == Environment.getInstance().isYunos()) {
            this.mAccountActivityHelper.setAccountActivityHide();
        }
        super.onResume();
        ActivityUtil.addTopActivity(this);
        ZpLogger.d("TVTao_" + TAG, "LPR   ---> " + getClass().getName());
        if (!NoPutToStack.getVoiceMap().containsKey(getClass().getName())) {
            ZpLogger.d("TVTao_" + TAG, "LPR   ---> true");
            LPR.getInstance().registed((Context) this);
            ASRNotify.getInstance().setHandler(new VoiceAction());
            return;
        }
        ZpLogger.d("TVTao_" + TAG, "LPR   ---> false");
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        if (RunMode.isYunos() == Environment.getInstance().isYunos()) {
            if (this.mAccountActivityHelper != null) {
                this.mAccountActivityHelper.setAccountActivityShowed();
            }
            super.onPause();
            AsyncDataLoader.purge();
            ImageHandleManager.getImageHandleManager(getApplicationContext()).purge();
            if (!NoPutToStack.getVoiceMap().containsKey(getClass().getName())) {
                ASRNotify.getInstance().setHandler((ASRHandler) null);
                LPR.getInstance().unregistered();
                return;
            }
            return;
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (RunMode.isYunos() == Environment.getInstance().isYunos()) {
            ZpLogger.d(TAG, "TAG   ---> " + TAG + ";   ---- >   onDestroy;  this =  " + this);
            unRegisterLoginListener();
            if (this.mDialogUtil != null) {
                this.mDialogUtil.onDestroy();
            }
            if (this.mNetworkChangeBroadcastReceiver != null) {
                unregisterReceiver(this.mNetworkChangeBroadcastReceiver);
                this.mNetworkChangeBroadcastReceiver = null;
            }
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        FocusPositionManager focusPositonManager;
        if ((event.getKeyCode() == 21 || event.getKeyCode() == 22 || event.getKeyCode() == 19 || event.getKeyCode() == 20) && (focusPositonManager = getFocusPositionManager()) != null && !focusPositonManager.IsFocusStarted()) {
            focusPositonManager.focusStart();
        }
        return super.dispatchKeyEvent(event);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        ZpLogger.i(TAG, TAG + ".dispatchTouchEvent.ev = " + ev);
        if (ev.getAction() == 0) {
            FocusPositionManager focusPositonManager = getFocusPositionManager();
            ZpLogger.v(TAG, TAG + ".dispatchTouchEvent.focusPositonManager = " + focusPositonManager);
            if (focusPositonManager != null && focusPositonManager.IsFocusStarted()) {
                focusPositonManager.focusStop();
                focusPositonManager.invalidate();
                ZpLogger.v(TAG, TAG + ".dispatchTouchEvent.focused = " + focusPositonManager.findFocus());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /* access modifiers changed from: protected */
    public PageReturn onVoiceAction(DomainResultVo action) {
        return null;
    }

    private class VoiceAction implements ASRHandler {
        private VoiceAction() {
        }

        public PageReturn onASRNotify(DomainResultVo object) {
            return BaseActivity.this.onVoiceAction(object);
        }
    }

    /* access modifiers changed from: protected */
    public boolean loginIsCanceled() {
        if (this.mAccountActivityHelper == null) {
            return false;
        }
        return this.mAccountActivityHelper.loginIsCanceled();
    }

    public void setLoginActivityStartShowing() {
        if (this.mAccountActivityHelper != null) {
            this.mAccountActivityHelper.setAccountActivityStartShowing();
        }
    }

    public void setCurrLoginInvalid() {
        if (this.mAccountActivityHelper != null) {
            this.mAccountActivityHelper.setAccountInvalid();
        }
    }

    public void startLoginActivity(String from, boolean forceLogin) {
        if (this.mAccountActivityHelper != null) {
            this.mAccountActivityHelper.startAccountActivity(this, from, forceLogin);
        }
    }

    /* access modifiers changed from: protected */
    public void registerLoginListener() {
        if (this.mOnAccountStateChangedListener == null) {
            this.mOnAccountStateChangedListener = new AccountActivityHelper.OnAccountStateChangedListener() {
                public void onAccountStateChanged(AccountActivityHelper.AccountLoginState state) {
                    switch (AnonymousClass4.$SwitchMap$com$yunos$tvtaobao$blitz$account$AccountActivityHelper$AccountLoginState[state.ordinal()]) {
                        case 1:
                            BaseActivity.this.onLogin();
                            return;
                        case 2:
                            BaseActivity.this.onLogout();
                            return;
                        case 3:
                            BaseActivity.this.onLoginCancel();
                            return;
                        default:
                            return;
                    }
                }
            };
            if (this.mAccountActivityHelper != null) {
                this.mAccountActivityHelper.registerAccountActivity(this.mOnAccountStateChangedListener);
            }
        }
    }

    /* renamed from: com.yunos.tvtaobao.biz.activity.BaseActivity$4  reason: invalid class name */
    static /* synthetic */ class AnonymousClass4 {
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
        if (this.mAccountActivityHelper != null && this.mOnAccountStateChangedListener != null) {
            this.mAccountActivityHelper.unRegisterAccountActivity(this.mOnAccountStateChangedListener);
        }
    }

    /* access modifiers changed from: protected */
    public void onLogin() {
        refreshData();
        if (CoreApplication.getLoginHelper(CoreApplication.getApplication()).isLogin()) {
            this.mBusinessRequest = BusinessRequest.getBusinessRequest();
            this.mBusinessRequest.requestTaokeLoginAnalysis(User.getNick(), "tvtaobao", TaokeConst.REFERER_BASEACTIVITY, new TaokeBussinessRequestListener(new WeakReference(this)));
        }
    }

    /* access modifiers changed from: protected */
    public void onLogout() {
        finish();
    }

    /* access modifiers changed from: protected */
    public void onLoginCancel() {
        ZpLogger.i(TAG, "onLoginCancel");
    }

    /* access modifiers changed from: protected */
    public void refreshData() {
    }

    public void removeNetworkOkDoListener() {
        this.mNetworkOkDoListener = null;
    }

    public void setNetworkOkDoListener(NetworkOkDoListener mNetworkOkDoListener2) {
        this.mNetworkOkDoListener = mNetworkOkDoListener2;
    }

    public void onWebviewPageDone(String url) {
    }

    /* access modifiers changed from: protected */
    public String getAppTag() {
        return "Tt";
    }

    /* access modifiers changed from: protected */
    public String getAppName() {
        return "tvtaobao";
    }

    /* access modifiers changed from: protected */
    public void addSystemBar() {
        getWindow().addFlags(Integer.MIN_VALUE);
    }

    /* access modifiers changed from: protected */
    public void setH5BackGroud() {
        setWebViewBackgroundColor(44, 47, 53, 255);
    }

    /* access modifiers changed from: protected */
    public void initBlitzContext(String initStr, int type) {
        super.initBlitzContext(initStr, type);
        try {
            Field field = BzBaseActivity.class.getDeclaredField("ActivityList");
            field.setAccessible(true);
            ArrayList arrayList = (ArrayList) field.get((Object) null);
            if (GlobalConfigInfo.getInstance().getGlobalConfig() != null && GlobalConfigInfo.getInstance().getGlobalConfig().isBeta() && arrayList.size() > 2) {
                arrayList.remove(0);
                ((BzBaseActivity) arrayList.get(0)).finish();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public FocusPositionManager getFocusPositionManager() {
        return null;
    }

    private class TaokeBussinessRequestListener extends BizRequestListener<JSONObject> {
        public TaokeBussinessRequestListener(WeakReference<BaseActivity> baseActivityRef) {
            super(baseActivityRef);
        }

        public boolean onError(int resultCode, String msg) {
            return true;
        }

        public void onSuccess(JSONObject data) {
            String access$200 = BaseActivity.TAG;
            StringBuilder append = new StringBuilder().append("");
            Object obj = data;
            if (data == null) {
                obj = Constant.NULL;
            }
            ZpLogger.d(access$200, append.append(obj).toString());
            SharedPreferencesUtils.saveTvBuyTaoKe(BaseActivity.this, System.currentTimeMillis() + 604800000);
        }

        public boolean ifFinishWhenCloseErrorDialog() {
            return false;
        }
    }
}
