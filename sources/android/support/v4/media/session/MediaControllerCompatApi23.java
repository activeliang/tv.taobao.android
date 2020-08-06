package android.support.v4.media.session;

import android.annotation.TargetApi;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.media.session.MediaControllerCompatApi21;

@TargetApi(23)
@RequiresApi(23)
class MediaControllerCompatApi23 {
    MediaControllerCompatApi23() {
    }

    public static class TransportControls extends MediaControllerCompatApi21.TransportControls {
        public static void playFromUri(Object controlsObj, Uri uri, Bundle extras) {
            ((MediaController.TransportControls) controlsObj).playFromUri(uri, extras);
        }
    }
}
