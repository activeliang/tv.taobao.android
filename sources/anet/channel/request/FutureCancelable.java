package anet.channel.request;

import anet.channel.util.ALog;
import java.util.concurrent.Future;

public class FutureCancelable implements Cancelable {
    public static final FutureCancelable NULL = new FutureCancelable((Future<?>) null, (String) null);
    private final Future<?> future;
    private final String seq;

    public FutureCancelable(Future<?> future2, String seq2) {
        this.future = future2;
        this.seq = seq2;
    }

    public void cancel() {
        if (this.future != null) {
            ALog.i("awcn.FutureCancelable", "cancel request", this.seq, new Object[0]);
            this.future.cancel(true);
        }
    }
}
