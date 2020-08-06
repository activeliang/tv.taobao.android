package com.taobao.wireless.trade.mbuy.sdk.engine;

import java.lang.ref.WeakReference;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class BuyProfileModule {
    protected BuyEngine engine;
    private WeakReference<ProfileDelegate> profileDelegateRef;

    public BuyProfileModule(BuyEngine engine2) {
        this.engine = engine2;
    }

    public void commitEvent(ProfilePoint point, String... args) {
        ProfileDelegate profileDelegate;
        if (this.engine != null && (profileDelegate = this.engine.getProfileDelegate()) != null) {
            try {
                switch (point) {
                    case RECURSIVE_PARSE_ERROR:
                        profileDelegate.commitEvent("ConfirmOrder", (int) IjkMediaPlayer.FFP_PROP_OBJ_VIDEO_FPS, (Object) "build_order_parse_failed", (Object) null, (Object) "BIZ_ERR");
                        return;
                    case INVALID_COMPONENT_DATA:
                        profileDelegate.commitEvent("ConfirmOrder", (int) IjkMediaPlayer.FFP_PROP_OBJ_VIDEO_FPS, (Object) "build_order_component_error", (Object) args[0], (Object) "BIZ_ERR");
                        return;
                    default:
                        return;
                }
            } catch (Throwable th) {
            }
        }
    }

    public ProfileDelegate getProfileDelegate() {
        return (ProfileDelegate) this.profileDelegateRef.get();
    }

    public void setProfileDelegate(ProfileDelegate profileDelegate) {
        this.profileDelegateRef = new WeakReference<>(profileDelegate);
    }
}
