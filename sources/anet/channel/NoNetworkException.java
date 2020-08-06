package anet.channel;

public class NoNetworkException extends Exception {
    private static final long serialVersionUID = 1;
    private SessionRequest request;

    public NoNetworkException(SessionRequest sessionRequest) {
        this.request = sessionRequest;
    }

    public String toString() {
        return "NoNetwork " + super.toString();
    }
}
