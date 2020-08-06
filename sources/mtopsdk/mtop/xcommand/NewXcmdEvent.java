package mtopsdk.mtop.xcommand;

public class NewXcmdEvent {
    String value;

    public NewXcmdEvent(String value2) {
        this.value = value2;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value2) {
        this.value = value2;
    }
}
