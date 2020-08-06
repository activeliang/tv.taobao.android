package android.taobao.windvane.packageapp.zipapp.data;

public enum ZipAppTypeEnum {
    ZIP_APP_TYPE_PACKAGEAPP(0),
    ZIP_APP_TYPE_ZCACHE(16),
    ZIP_APP_TYPE_REACT(32),
    ZIP_APP_TYPE_ZCACHE2(48),
    ZIP_APP_TYPE_UNKNOWN(240);
    
    private long value;

    private ZipAppTypeEnum(long value2) {
        this.value = value2;
    }

    public long getValue() {
        return this.value;
    }

    public void setValue(long value2) {
        this.value = value2;
    }
}
