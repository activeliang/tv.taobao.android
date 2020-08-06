package anet.channel;

public class NoAvailStrategyException extends Exception {
    private static final long serialVersionUID = 1;
    private SessionRequest request;

    public NoAvailStrategyException(SessionRequest sessionRequest) {
        this.request = sessionRequest;
    }

    public String toString() {
        return "No Available Strategy" + super.toString();
    }
}
