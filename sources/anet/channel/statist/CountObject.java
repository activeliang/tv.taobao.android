package anet.channel.statist;

public class CountObject {
    public String arg;
    public String module;
    public String modulePoint;
    public double value;

    public String toString() {
        return new StringBuilder(64).append("[module:").append(this.module).append(" modulePoint:").append(this.modulePoint).append(" arg:").append(this.arg).append(" value:").append(this.value).append("]").toString();
    }
}
