package tv.danmaku.ijk.media.player;

import android.content.Context;
import android.util.Log;
import com.taobao.android.alinnkit.alinn.AliNNNetInstance;
import com.taobao.android.alinnkit.core.AliNNForwardType;
import com.taobao.android.alinnkit.help.AliNNMonitor;
import com.taobao.android.alinnkit.help.NetPrepareTask;
import com.taobao.android.alinnkit.intf.AliNNKitNetFactory;
import com.taobao.android.alinnkit.intf.NetPreparedListener;
import com.taobao.android.alinnkit.net.AliNNKitBaseNet;
import java.io.File;

public class ABRFullNet extends AliNNKitBaseNet {
    public static final String BIZ_NAME = "tbLiveABR";
    private static final String ModelFileName = "ab/ab_00042_1";
    private AliNNMonitor.InferenceRecords inferenceRecords;
    private AliNNNetInstance.Session.Tensor mInputTensor;
    private AliNNNetInstance mNetInstance;
    private AliNNNetInstance.Session mSession;
    private float[] results;

    private ABRFullNet(AliNNNetInstance netInstance, AliNNNetInstance.Session session) {
        this.mNetInstance = netInstance;
        this.mSession = session;
        this.inferenceRecords = new AliNNMonitor.InferenceRecords();
    }

    public static void prepareNet(final Context context, NetPreparedListener<ABRFullNet> listener, String authCode) throws IllegalArgumentException {
        final String code = authCode;
        if (context == null || listener == null) {
            throw new IllegalArgumentException("parameter cannot be null");
        } else if (!checkIfNativeUnavailable(listener)) {
            new NetPrepareTask(context.getApplicationContext(), listener, new AliNNKitNetFactory<ABRFullNet>() {
                public ABRFullNet newAliNNKitNet(File modelDir) {
                    String modelFilePath = new File(modelDir, "ab_00042_1").getPath();
                    if (!new File(modelFilePath).exists()) {
                        return null;
                    }
                    AliNNNetInstance netInstance = AliNNNetInstance.createFromFile(context, modelFilePath, code);
                    AliNNNetInstance.Config config = new AliNNNetInstance.Config();
                    config.numThread = 4;
                    config.forwardType = AliNNForwardType.FORWARD_CPU.type;
                    AliNNNetInstance.Session session = netInstance.createSession(config);
                    if (netInstance == null || session == null) {
                        return null;
                    }
                    return new ABRFullNet(netInstance, session);
                }
            }).execute(new String[]{BIZ_NAME});
        }
    }

    public synchronized float[] inference(float[] mInputABRData) {
        String errorCode;
        float[] fArr = null;
        synchronized (this) {
            try {
                if (!(this.mNetInstance == null || this.mSession == null || this.inferenceRecords == null)) {
                    if (this.mInputTensor == null) {
                        this.mInputTensor = this.mSession.getInput((String) null);
                    }
                    this.mInputTensor.setInputFloatData(mInputABRData);
                    this.mSession.run();
                    this.results = this.mSession.getOutput((String) null).getFloatData();
                    float succeededCount = -998.999f;
                    float failedCount = -998.999f;
                    if (this.results == null || this.results.length < 3 || ((double) (((this.results[0] + this.results[1]) + this.results[2]) - 1.0f)) > 1.0E-6d) {
                        failedCount = 1.0f;
                        errorCode = "10001";
                    } else {
                        succeededCount = 1.0f;
                        errorCode = "0";
                    }
                    this.inferenceRecords.commit(BIZ_NAME, this.mModelId, this.mModelFiles, errorCode, succeededCount, failedCount, false);
                    fArr = this.results;
                }
            } catch (Exception e) {
                Log.e("avsdk", "alinn abr run error:" + e.getMessage());
            }
        }
        return fArr;
    }

    public synchronized void release() {
        if (this.mSession != null) {
            this.mSession.release();
            this.mSession = null;
        }
        if (this.mNetInstance != null) {
            this.mNetInstance.release();
        }
    }
}
