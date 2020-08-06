package com.ali.user.open.tbauth;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.config.AuthOption;
import com.ali.user.open.core.config.ConfigManager;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.service.StorageService;
import com.ali.user.open.core.service.UserTrackerService;
import com.ali.user.open.core.trace.SDKLogger;
import com.ali.user.open.core.util.ResourceUtils;
import com.ali.user.open.tbauth.context.TbAuthContext;
import com.ali.user.open.tbauth.ui.TbAuthWebViewActivity;
import com.ut.mini.UTHitBuilders;
import java.util.HashMap;
import java.util.Map;

public class TbAuthComponent {
    public static final TbAuthComponent INSTANCE = new TbAuthComponent();
    private static final String OAUTH_API = "taobao.oauth.code.create";
    private static final String TAG = "TbAuthComponent";

    private TbAuthComponent() {
    }

    public void showLogin(Activity activity) {
        SDKLogger.d(TAG, "showLogin");
        if (KernelContext.sOneTimeAuthOption == AuthOption.H5ONLY) {
            showH5Login(activity);
        } else if (TbAuthContext.h5Only || KernelContext.authOption == AuthOption.H5ONLY) {
            showH5Login(activity);
        } else {
            Intent intent = new Intent();
            intent.setAction("com.taobao.open.intent.action.GETWAY");
            StringBuilder sb = new StringBuilder("tbopen://m.taobao.com/getway/oauth?").append("&appkey=").append(((StorageService) AliMemberSDK.getService(StorageService.class)).getAppKey()).append("&pluginName=").append(OAUTH_API).append("&apkSign=").append("&sign=");
            if (!TextUtils.isEmpty(getIbbCode())) {
                if (!TextUtils.isEmpty(TbAuthContext.sIBB)) {
                    sb.append("&IBB=").append(TbAuthContext.sIBB);
                }
                if (!TextUtils.isEmpty(TbAuthContext.sSceneCode)) {
                    sb.append("&sceneCode=").append(TbAuthContext.sSceneCode);
                } else {
                    sb.append("&BaiChuanIBB4Bind=").append(getIbbCode());
                }
            } else {
                if (!TextUtils.isEmpty(TbAuthContext.sIBB)) {
                    sb.append("&IBB=").append(TbAuthContext.sIBB);
                }
                if (!TextUtils.isEmpty(TbAuthContext.sSceneCode)) {
                    sb.append("&sceneCode=").append(TbAuthContext.sSceneCode);
                } else {
                    sb.append("&sceneCode=10000");
                }
            }
            sb.append("&sdkTraceId=" + TbAuthContext.traceId);
            intent.setData(Uri.parse(sb.toString()));
            if (activity.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                try {
                    Map<String, String> props = new HashMap<>();
                    props.put(UTHitBuilders.UTHitBuilder.FIELD_ARG2, TbAuthContext.traceId);
                    ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send("Page_TaobaoOauth", "Page_TaobaoOauth_Native", props);
                    activity.startActivityForResult(intent, RequestCode.OPEN_TAOBAO);
                } catch (Throwable e) {
                    SDKLogger.d(TAG, "startActivityForResult fail == " + e.getMessage());
                    showH5Login(activity);
                }
            } else {
                showH5Login(activity);
            }
        }
    }

    private String getIbbCode() {
        if (!TbAuthContext.isBind) {
            return "";
        }
        if (TbAuthContext.needSession) {
            return "10024";
        }
        return "10024";
    }

    public void showH5Login(Activity activity) {
        SDKLogger.d(TAG, "open H5 login");
        Map<String, String> props = new HashMap<>();
        props.put(UTHitBuilders.UTHitBuilder.FIELD_ARG2, TbAuthContext.traceId);
        ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send("Page_TaobaoOauth", "Page_TaobaoOauth_H5", props);
        Intent h5Intent = new Intent(activity, TbAuthWebViewActivity.class);
        h5Intent.putExtra("url", ConfigManager.LOGIN_HOST + ((StorageService) AliMemberSDK.getService(StorageService.class)).getAppKey() + "&sdkTraceId=" + TbAuthContext.traceId);
        h5Intent.putExtra("title", ResourceUtils.getString(activity.getApplicationContext(), "member_sdk_authorize_title"));
        activity.startActivityForResult(h5Intent, RequestCode.OPEN_H5_LOGIN);
    }
}
