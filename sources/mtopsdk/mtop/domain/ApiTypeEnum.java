package mtopsdk.mtop.domain;

public enum ApiTypeEnum {
    ISV_OPEN_API("isv_open_api");
    
    private String apiType;

    public String getApiType() {
        return this.apiType;
    }

    private ApiTypeEnum(String apiType2) {
        this.apiType = apiType2;
    }
}
