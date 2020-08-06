package android.support.v7.app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;

@TargetApi(24)
@RequiresApi(24)
class NotificationCompatImpl24 {
    NotificationCompatImpl24() {
    }

    public static void addDecoratedCustomViewStyle(NotificationBuilderWithBuilderAccessor b) {
        b.getBuilder().setStyle(new Notification.DecoratedCustomViewStyle());
    }

    public static void addDecoratedMediaCustomViewStyle(NotificationBuilderWithBuilderAccessor b) {
        b.getBuilder().setStyle(new Notification.DecoratedMediaCustomViewStyle());
    }
}
