package android.taobao.windvane.util;

public enum MimeTypeEnum {
    JS("js", "application/x-javascript"),
    CSS("css", "text/css"),
    JPG("jpg", "image/jpeg"),
    JPEG("jpep", "image/jpeg"),
    PNG("png", "image/png"),
    WEBP("webp", "image/webp"),
    GIF("gif", "image/gif"),
    HTM("htm", "text/html"),
    HTML("html", "text/html");
    
    private String mimeType;
    private String suffix;

    private MimeTypeEnum(String suffix2, String mimeType2) {
        this.suffix = suffix2;
        this.mimeType = mimeType2;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public void setSuffix(String suffix2) {
        this.suffix = suffix2;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(String mimeType2) {
        this.mimeType = mimeType2;
    }
}
