package com.taobao.media.connectionclass;

import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class ConnectionClassManager {
    static final long BANDWIDTH_LOWER_BOUND = 10;
    private static final int BYTES_TO_BITS = 8;
    public static double DEFAULT_DECAY_CONSTANT = 0.05d;
    private static final int DEFAULT_GOOD_BANDWIDTH = 2000;
    private static final int DEFAULT_MODERATE_BANDWIDTH = 550;
    private static final int DEFAULT_POOR_BANDWIDTH = 150;
    static final double DEFAULT_SAMPLES_TO_QUALITY_CHANGE = 5.0d;
    private AtomicReference<ConnectionQuality> mCurrentBandwidthConnectionQuality;
    private ExponentialGeometricAverage mDownloadBandwidth;
    private volatile boolean mInitiateStateChange;
    private ArrayList<ConnectionClassStateChangeListener> mListenerList;
    private AtomicReference<ConnectionQuality> mNextBandwidthConnectionQuality;
    private int mSampleCounter;

    public interface ConnectionClassStateChangeListener {
        void onBandwidthStateChange(ConnectionQuality connectionQuality);
    }

    private static class ConnectionClassManagerHolder {
        public static final ConnectionClassManager instance = new ConnectionClassManager();

        private ConnectionClassManagerHolder() {
        }
    }

    public static ConnectionClassManager getInstance() {
        return ConnectionClassManagerHolder.instance;
    }

    private ConnectionClassManager() {
        this.mDownloadBandwidth = new ExponentialGeometricAverage(DEFAULT_DECAY_CONSTANT);
        this.mInitiateStateChange = false;
        this.mCurrentBandwidthConnectionQuality = new AtomicReference<>(ConnectionQuality.UNKNOWN);
        this.mListenerList = new ArrayList<>();
    }

    public synchronized void addBandwidth(long bytes, long timeInMs) {
        if (timeInMs != 0 && ((((double) bytes) * 1.0d) / ((double) timeInMs)) * 8.0d >= 10.0d) {
            this.mDownloadBandwidth.addMeasurement(((((double) bytes) * 1.0d) / ((double) timeInMs)) * 8.0d);
            if (this.mInitiateStateChange) {
                this.mSampleCounter++;
                if (getCurrentBandwidthQuality() != this.mNextBandwidthConnectionQuality.get()) {
                    this.mInitiateStateChange = false;
                    this.mSampleCounter = 1;
                }
                if (((double) this.mSampleCounter) >= DEFAULT_SAMPLES_TO_QUALITY_CHANGE) {
                    this.mInitiateStateChange = false;
                    this.mSampleCounter = 1;
                    this.mCurrentBandwidthConnectionQuality.set(this.mNextBandwidthConnectionQuality.get());
                    notifyListeners();
                }
            } else if (this.mCurrentBandwidthConnectionQuality.get() != getCurrentBandwidthQuality()) {
                this.mInitiateStateChange = true;
                this.mNextBandwidthConnectionQuality = new AtomicReference<>(getCurrentBandwidthQuality());
            }
        }
    }

    public void reset() {
        if (this.mDownloadBandwidth != null) {
            this.mDownloadBandwidth.reset();
        }
        this.mCurrentBandwidthConnectionQuality.set(ConnectionQuality.UNKNOWN);
    }

    public synchronized ConnectionQuality getCurrentBandwidthQuality() {
        ConnectionQuality mapBandwidthQuality;
        if (this.mDownloadBandwidth == null) {
            mapBandwidthQuality = ConnectionQuality.UNKNOWN;
        } else {
            mapBandwidthQuality = mapBandwidthQuality(this.mDownloadBandwidth.getAverage());
        }
        return mapBandwidthQuality;
    }

    private ConnectionQuality mapBandwidthQuality(double average) {
        if (average < ClientTraceData.b.f47a) {
            return ConnectionQuality.UNKNOWN;
        }
        if (average < 150.0d) {
            return ConnectionQuality.POOR;
        }
        if (average < 550.0d) {
            return ConnectionQuality.MODERATE;
        }
        if (average < 2000.0d) {
            return ConnectionQuality.GOOD;
        }
        return ConnectionQuality.EXCELLENT;
    }

    public synchronized double getDownloadKBitsPerSecond() {
        double average;
        if (this.mDownloadBandwidth == null) {
            average = -1.0d;
        } else {
            average = this.mDownloadBandwidth.getAverage();
        }
        return average;
    }

    public ConnectionQuality register(ConnectionClassStateChangeListener listener) {
        if (listener != null) {
            this.mListenerList.add(listener);
        }
        return this.mCurrentBandwidthConnectionQuality.get();
    }

    public void remove(ConnectionClassStateChangeListener listener) {
        if (listener != null) {
            this.mListenerList.remove(listener);
        }
    }

    private void notifyListeners() {
        int size = this.mListenerList.size();
        for (int i = 0; i < size; i++) {
            this.mListenerList.get(i).onBandwidthStateChange(this.mCurrentBandwidthConnectionQuality.get());
        }
    }
}
