package anetwork.channel;

public interface IBodyHandler {
    boolean isCompleted();

    int read(byte[] bArr);
}
