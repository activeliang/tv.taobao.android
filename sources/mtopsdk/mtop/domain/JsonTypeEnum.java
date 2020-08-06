package mtopsdk.mtop.domain;

public enum JsonTypeEnum {
    JSON("json"),
    ORIGINALJSON("originaljson");
    
    private String jsonType;

    public String getJsonType() {
        return this.jsonType;
    }

    private JsonTypeEnum(String jsonType2) {
        this.jsonType = jsonType2;
    }
}
