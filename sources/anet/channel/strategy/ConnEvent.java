package anet.channel.strategy;

public class ConnEvent {
    public long connTime = Long.MAX_VALUE;
    public boolean isSuccess = false;

    public String toString() {
        return this.isSuccess ? "ConnEvent#Success" : "ConnEvent#Fail";
    }
}
