package android.support.v7.app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.media.session.MediaSession;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;

@TargetApi(21)
@RequiresApi(21)
class NotificationCompatImpl21 {
    NotificationCompatImpl21() {
    }

    public static void addMediaStyle(NotificationBuilderWithBuilderAccessor b, int[] actionsToShowInCompact, Object token) {
        Notification.MediaStyle style = new Notification.MediaStyle(b.getBuilder());
        if (actionsToShowInCompact != null) {
            style.setShowActionsInCompactView(actionsToShowInCompact);
        }
        if (token != null) {
            style.setMediaSession((MediaSession.Token) token);
        }
    }
}
