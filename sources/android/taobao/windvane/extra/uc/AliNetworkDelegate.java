package android.taobao.windvane.extra.uc;

import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.util.CommonUtils;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;
import com.taobao.tao.image.ImageStrategyConfig;
import com.taobao.tao.util.ImageStrategyDecider;
import com.uc.webview.export.internal.interfaces.INetworkDelegate;
import com.uc.webview.export.internal.interfaces.IRequestData;
import com.uc.webview.export.internal.interfaces.IResponseData;

public class AliNetworkDelegate implements INetworkDelegate {
    private boolean isUseWebpImg = false;

    public AliNetworkDelegate() {
        if (WVCommonConfig.commonConfig.ucsdk_image_strategy_rate > Math.random()) {
            this.isUseWebpImg = true;
        }
    }

    public IRequestData onSendRequest(IRequestData requestIn) {
        if (requestIn == null) {
            return null;
        }
        String url = requestIn.getUrl();
        TaoLog.d("AliNetworkDelegate onSendRequest", url);
        if (!this.isUseWebpImg || !CommonUtils.isPicture(url)) {
            return requestIn;
        }
        try {
            String decideUrl = ImageStrategyDecider.decideUrl(url, -1, -1, ImageStrategyConfig.newBuilderWithName("windvane").enableQuality(false).forceWebPOn(true).build());
            if (TextUtils.isEmpty(decideUrl)) {
                return requestIn;
            }
            requestIn.setUrl(decideUrl);
            if (TextUtils.isEmpty(decideUrl) || decideUrl.equals(url)) {
                return requestIn;
            }
            TaoLog.i("AliNetworkDelegate", url + " decideUrl to : " + decideUrl);
            return requestIn;
        } catch (Throwable th) {
            return requestIn;
        }
    }

    public IResponseData onReceiveResponse(IResponseData responseIn) {
        if (responseIn == null) {
            return null;
        }
        TaoLog.d("AliNetworkDelegate onReceiveResponse ", responseIn.getUrl() + " status : " + responseIn.getStatus());
        return responseIn;
    }
}
