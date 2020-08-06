package android.taobao.windvane.packageapp.zipapp.data;

import android.support.v4.media.session.PlaybackStateCompat;

public enum ZipUpdateInfoEnum {
    ZIP_UPDATE_INFO_DELETE(PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM),
    ZIP_APP_TYPE_NORMAL(0);
    
    private long value;

    private ZipUpdateInfoEnum(long value2) {
        this.value = value2;
    }

    public long getValue() {
        return this.value;
    }

    public void setValue(long value2) {
        this.value = value2;
    }
}
