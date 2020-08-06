package anet.channel.statist;

public class AlarmObject {
    public String arg;
    public String errorCode;
    public String errorMsg;
    public boolean isSuccess;
    public String module;
    public String modulePoint;

    public String toString() {
        return new StringBuilder(64).append("[module:").append(this.module).append(" modulePoint:").append(this.modulePoint).append(" arg:").append(this.arg).append(" isSuccess:").append(this.isSuccess).append(" errorCode:").append(this.errorCode).append("]").toString();
    }
}
