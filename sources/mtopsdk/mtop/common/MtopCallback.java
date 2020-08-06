package mtopsdk.mtop.common;

public class MtopCallback {

    @Deprecated
    public interface MtopProgressListener extends MtopListener {
        @Deprecated
        void onDataReceived(MtopProgressEvent mtopProgressEvent, Object obj);
    }

    public interface MtopCacheListener extends MtopListener {
        void onCached(MtopCacheEvent mtopCacheEvent, Object obj);
    }

    public interface MtopFinishListener extends MtopListener {
        void onFinished(MtopFinishEvent mtopFinishEvent, Object obj);
    }

    public interface MtopHeaderListener extends MtopListener {
        void onHeader(MtopHeaderEvent mtopHeaderEvent, Object obj);
    }
}
