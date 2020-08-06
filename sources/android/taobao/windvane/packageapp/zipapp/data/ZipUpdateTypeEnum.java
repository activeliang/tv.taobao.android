package android.taobao.windvane.packageapp.zipapp.data;

public enum ZipUpdateTypeEnum {
    ZIP_UPDATE_TYPE_PASSIVE(0),
    ZIP_APP_TYPE_FORCE(256),
    ZIP_APP_TYPE_ONLINE(512);
    
    private long value;

    private ZipUpdateTypeEnum(long value2) {
        this.value = value2;
    }

    public long getValue() {
        return this.value;
    }

    public void setValue(long value2) {
        this.value = value2;
    }
}
