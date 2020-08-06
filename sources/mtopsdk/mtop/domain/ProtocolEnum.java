package mtopsdk.mtop.domain;

public enum ProtocolEnum {
    HTTP("http://"),
    HTTPSECURE("https://");
    
    private String protocol;

    public String getProtocol() {
        return this.protocol;
    }

    private ProtocolEnum(String protocol2) {
        this.protocol = protocol2;
    }
}
