package com.yunos.tv.tvsdk.media.mediacodec;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.util.Log;

public class MediaDecoderThread extends Thread {
    public static final int CACHED_DATA_DONE = 7;
    public static final int NOT_ENOUGH_CACHED_DATA = 6;
    public static final int OTHER_ERROR = 1;
    public static final int READ_DATA_END = 2;
    public static final int SEEK_COMPLETE = 5;
    private static final String TAG = "VideoDecoderThread";
    public static final int THREAD_ALREADY = 4;
    public static final int TRY_AGAIN_LATER = 3;
    private MediaCodec decoder;
    private MediaExtractor extractor;
    private Object mLocker = new Object();
    private OnDecoderListener mOnDecoderListener;
    private boolean mSeekAfter;
    private Object mSeekLocker = new Object();
    private long mSeekMsec;
    private long mStartMsec;
    private THREAD_STATUS mThreadStatus = THREAD_STATUS.INIT;

    public interface OnDecoderListener {
        void onDecoder(int i);
    }

    private enum THREAD_STATUS {
        INIT,
        RUNNING,
        PAUSE,
        STOP
    }

    public MediaDecoderThread(MediaCodec codec, MediaExtractor tractor) {
        this.decoder = codec;
        this.extractor = tractor;
    }

    public void setOnDecoderListener(OnDecoderListener l) {
        this.mOnDecoderListener = l;
    }

    public void setSeekTime(long mSec, boolean after) {
        Log.i(TAG, "setSeekTime mSec=" + mSec + " after=" + after);
        synchronized (this.mSeekLocker) {
            this.mSeekMsec = mSec;
            this.mSeekAfter = after;
        }
    }

    public boolean isRuuning() {
        boolean isRunning;
        synchronized (this.mLocker) {
            isRunning = this.mThreadStatus == THREAD_STATUS.RUNNING;
            this.mLocker.notifyAll();
        }
        return isRunning;
    }

    public boolean isReady() {
        boolean isReady;
        synchronized (this.mLocker) {
            isReady = this.mThreadStatus != THREAD_STATUS.INIT;
            this.mLocker.notifyAll();
        }
        return isReady;
    }

    public void setStop() {
        synchronized (this.mLocker) {
            Log.i(TAG, "setStop");
            this.mThreadStatus = THREAD_STATUS.STOP;
            this.mLocker.notifyAll();
        }
    }

    public void setStart() {
        synchronized (this.mLocker) {
            Log.i(TAG, "setStart");
            this.mThreadStatus = THREAD_STATUS.RUNNING;
            this.mLocker.notifyAll();
        }
    }

    public void setPause() {
        synchronized (this.mLocker) {
            Log.i(TAG, "setPause");
            this.mThreadStatus = THREAD_STATUS.PAUSE;
            this.mLocker.notifyAll();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:100:0x0222, code lost:
        r12.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x0221, code lost:
        r12 = move-exception;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r26 = this;
            r0 = r26
            android.media.MediaCodec r2 = r0.decoder
            if (r2 != 0) goto L_0x0010
            java.lang.String r2 = "VideoDecoderThread"
            java.lang.String r4 = "didnot find video track in the media data"
            android.util.Log.e(r2, r4)
        L_0x000f:
            return
        L_0x0010:
            r13 = 0
            r0 = r26
            android.media.MediaCodec r2 = r0.decoder
            r2.start()
            r0 = r26
            android.media.MediaCodec r2 = r0.decoder
            java.nio.ByteBuffer[] r15 = r2.getInputBuffers()
            r0 = r26
            android.media.MediaCodec r2 = r0.decoder
            java.nio.ByteBuffer[] r21 = r2.getOutputBuffers()
            android.media.MediaCodec$BufferInfo r14 = new android.media.MediaCodec$BufferInfo
            r14.<init>()
            r16 = 0
            long r6 = java.lang.System.currentTimeMillis()
            r0 = r26
            r0.mStartMsec = r6
            r0 = r26
            com.yunos.tv.tvsdk.media.mediacodec.MediaDecoderThread$OnDecoderListener r2 = r0.mOnDecoderListener
            if (r2 == 0) goto L_0x0045
            r0 = r26
            com.yunos.tv.tvsdk.media.mediacodec.MediaDecoderThread$OnDecoderListener r2 = r0.mOnDecoderListener
            r4 = 4
            r2.onDecoder(r4)
        L_0x0045:
            boolean r2 = java.lang.Thread.interrupted()
            if (r2 != 0) goto L_0x0062
            r0 = r26
            java.lang.Object r4 = r0.mLocker
            monitor-enter(r4)
            r0 = r26
            com.yunos.tv.tvsdk.media.mediacodec.MediaDecoderThread$THREAD_STATUS r2 = r0.mThreadStatus     // Catch:{ all -> 0x00c5 }
            com.yunos.tv.tvsdk.media.mediacodec.MediaDecoderThread$THREAD_STATUS r6 = com.yunos.tv.tvsdk.media.mediacodec.MediaDecoderThread.THREAD_STATUS.STOP     // Catch:{ all -> 0x00c5 }
            if (r2 != r6) goto L_0x008b
            java.lang.String r2 = "VideoDecoderThread"
            java.lang.String r6 = "stop"
            android.util.Log.i(r2, r6)     // Catch:{ all -> 0x00c5 }
            monitor-exit(r4)     // Catch:{ all -> 0x00c5 }
        L_0x0062:
            java.lang.String r2 = "VideoDecoderThread"
            java.lang.String r4 = "thread finished"
            android.util.Log.i(r2, r4)
            r0 = r26
            android.media.MediaCodec r2 = r0.decoder
            r2.stop()
            r0 = r26
            android.media.MediaCodec r2 = r0.decoder
            r2.release()
            r2 = 0
            r0 = r26
            r0.decoder = r2
            r0 = r26
            android.media.MediaExtractor r2 = r0.extractor
            r2.release()
            r2 = 0
            r0 = r26
            r0.extractor = r2
            goto L_0x000f
        L_0x008b:
            r0 = r26
            com.yunos.tv.tvsdk.media.mediacodec.MediaDecoderThread$THREAD_STATUS r2 = r0.mThreadStatus     // Catch:{ all -> 0x00c5 }
            com.yunos.tv.tvsdk.media.mediacodec.MediaDecoderThread$THREAD_STATUS r6 = com.yunos.tv.tvsdk.media.mediacodec.MediaDecoderThread.THREAD_STATUS.INIT     // Catch:{ all -> 0x00c5 }
            if (r2 == r6) goto L_0x009b
            r0 = r26
            com.yunos.tv.tvsdk.media.mediacodec.MediaDecoderThread$THREAD_STATUS r2 = r0.mThreadStatus     // Catch:{ all -> 0x00c5 }
            com.yunos.tv.tvsdk.media.mediacodec.MediaDecoderThread$THREAD_STATUS r6 = com.yunos.tv.tvsdk.media.mediacodec.MediaDecoderThread.THREAD_STATUS.PAUSE     // Catch:{ all -> 0x00c5 }
            if (r2 != r6) goto L_0x00e1
        L_0x009b:
            long r18 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x00c5 }
        L_0x009f:
            r0 = r26
            com.yunos.tv.tvsdk.media.mediacodec.MediaDecoderThread$THREAD_STATUS r2 = r0.mThreadStatus     // Catch:{ all -> 0x00c5 }
            com.yunos.tv.tvsdk.media.mediacodec.MediaDecoderThread$THREAD_STATUS r6 = com.yunos.tv.tvsdk.media.mediacodec.MediaDecoderThread.THREAD_STATUS.INIT     // Catch:{ all -> 0x00c5 }
            if (r2 == r6) goto L_0x00af
            r0 = r26
            com.yunos.tv.tvsdk.media.mediacodec.MediaDecoderThread$THREAD_STATUS r2 = r0.mThreadStatus     // Catch:{ all -> 0x00c5 }
            com.yunos.tv.tvsdk.media.mediacodec.MediaDecoderThread$THREAD_STATUS r6 = com.yunos.tv.tvsdk.media.mediacodec.MediaDecoderThread.THREAD_STATUS.PAUSE     // Catch:{ all -> 0x00c5 }
            if (r2 != r6) goto L_0x00c8
        L_0x00af:
            java.lang.String r2 = "VideoDecoderThread"
            java.lang.String r6 = "pause"
            android.util.Log.i(r2, r6)     // Catch:{ InterruptedException -> 0x00c0 }
            r0 = r26
            java.lang.Object r2 = r0.mLocker     // Catch:{ InterruptedException -> 0x00c0 }
            r2.wait()     // Catch:{ InterruptedException -> 0x00c0 }
            goto L_0x009f
        L_0x00c0:
            r12 = move-exception
            r12.printStackTrace()     // Catch:{ all -> 0x00c5 }
            goto L_0x009f
        L_0x00c5:
            r2 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x00c5 }
            throw r2
        L_0x00c8:
            java.lang.String r2 = "VideoDecoderThread"
            java.lang.String r6 = "continue"
            android.util.Log.i(r2, r6)     // Catch:{ all -> 0x00c5 }
            r0 = r26
            long r6 = r0.mStartMsec     // Catch:{ all -> 0x00c5 }
            long r24 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x00c5 }
            long r24 = r24 - r18
            long r6 = r6 + r24
            r0 = r26
            r0.mStartMsec = r6     // Catch:{ all -> 0x00c5 }
        L_0x00e1:
            monitor-exit(r4)     // Catch:{ all -> 0x00c5 }
            r0 = r26
            android.media.MediaExtractor r2 = r0.extractor
            long r10 = r2.getCachedDuration()
            r6 = 0
            int r2 = (r10 > r6 ? 1 : (r10 == r6 ? 0 : -1))
            if (r2 > 0) goto L_0x0144
            r2 = 6
            r0 = r26
            r0.sendMsgCode(r2)
            long r18 = java.lang.System.currentTimeMillis()
            r17 = 0
        L_0x00fc:
            r0 = r26
            android.media.MediaExtractor r2 = r0.extractor
            long r6 = r2.getCachedDuration()
            r24 = 0
            int r2 = (r6 > r24 ? 1 : (r6 == r24 ? 0 : -1))
            if (r2 > 0) goto L_0x0123
            r0 = r26
            java.lang.Object r4 = r0.mLocker
            monitor-enter(r4)
            r0 = r26
            com.yunos.tv.tvsdk.media.mediacodec.MediaDecoderThread$THREAD_STATUS r2 = r0.mThreadStatus     // Catch:{ all -> 0x0227 }
            com.yunos.tv.tvsdk.media.mediacodec.MediaDecoderThread$THREAD_STATUS r6 = com.yunos.tv.tvsdk.media.mediacodec.MediaDecoderThread.THREAD_STATUS.STOP     // Catch:{ all -> 0x0227 }
            if (r2 != r6) goto L_0x0210
            java.lang.String r2 = "VideoDecoderThread"
            java.lang.String r6 = "wait load stop"
            android.util.Log.i(r2, r6)     // Catch:{ all -> 0x0227 }
            r17 = 1
            monitor-exit(r4)     // Catch:{ all -> 0x0227 }
        L_0x0123:
            java.lang.String r2 = "VideoDecoderThread"
            java.lang.String r4 = "wait done continue"
            android.util.Log.i(r2, r4)
            r0 = r26
            long r6 = r0.mStartMsec
            long r24 = java.lang.System.currentTimeMillis()
            long r24 = r24 - r18
            long r6 = r6 + r24
            r0 = r26
            r0.mStartMsec = r6
            if (r17 != 0) goto L_0x0144
            r2 = 7
            r0 = r26
            r0.sendMsgCode(r2)
        L_0x0144:
            if (r16 != 0) goto L_0x0176
            r0 = r26
            android.media.MediaCodec r2 = r0.decoder
            r6 = 10000(0x2710, double:4.9407E-320)
            int r3 = r2.dequeueInputBuffer(r6)
            if (r3 < 0) goto L_0x0176
            r9 = r15[r3]
            r0 = r26
            android.media.MediaExtractor r2 = r0.extractor
            r4 = 0
            int r5 = r2.readSampleData(r9, r4)
            if (r5 >= 0) goto L_0x022a
            java.lang.String r2 = "VideoDecoderThread"
            java.lang.String r4 = "InputBuffer BUFFER_FLAG_END_OF_STREAM"
            android.util.Log.d(r2, r4)
            r0 = r26
            android.media.MediaCodec r2 = r0.decoder
            r4 = 0
            r5 = 0
            r6 = 0
            r8 = 4
            r2.queueInputBuffer(r3, r4, r5, r6, r8)
            r16 = 1
        L_0x0176:
            r0 = r26
            android.media.MediaCodec r2 = r0.decoder
            r6 = 10000(0x2710, double:4.9407E-320)
            int r20 = r2.dequeueOutputBuffer(r14, r6)
            switch(r20) {
                case -3: goto L_0x0244;
                case -2: goto L_0x0256;
                case -1: goto L_0x0279;
                default: goto L_0x0183;
            }
        L_0x0183:
            long r6 = r14.presentationTimeUs
            r24 = 1000(0x3e8, double:4.94E-321)
            long r22 = r6 / r24
            r0 = r26
            java.lang.Object r4 = r0.mSeekLocker
            monitor-enter(r4)
            r0 = r26
            long r6 = r0.mSeekMsec     // Catch:{ all -> 0x0284 }
            r24 = 0
            int r2 = (r6 > r24 ? 1 : (r6 == r24 ? 0 : -1))
            if (r2 <= 0) goto L_0x01d3
            r0 = r26
            boolean r2 = r0.mSeekAfter     // Catch:{ all -> 0x0284 }
            r6 = 1
            if (r2 != r6) goto L_0x01a7
            r0 = r26
            long r6 = r0.mSeekMsec     // Catch:{ all -> 0x0284 }
            int r2 = (r22 > r6 ? 1 : (r22 == r6 ? 0 : -1))
            if (r2 >= 0) goto L_0x01b5
        L_0x01a7:
            r0 = r26
            boolean r2 = r0.mSeekAfter     // Catch:{ all -> 0x0284 }
            if (r2 != 0) goto L_0x01d3
            r0 = r26
            long r6 = r0.mSeekMsec     // Catch:{ all -> 0x0284 }
            int r2 = (r22 > r6 ? 1 : (r22 == r6 ? 0 : -1))
            if (r2 > 0) goto L_0x01d3
        L_0x01b5:
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0284 }
            long r6 = r6 - r22
            r0 = r26
            r0.mStartMsec = r6     // Catch:{ all -> 0x0284 }
            r6 = 0
            r0 = r26
            r0.mSeekMsec = r6     // Catch:{ all -> 0x0284 }
            r0 = r26
            com.yunos.tv.tvsdk.media.mediacodec.MediaDecoderThread$OnDecoderListener r2 = r0.mOnDecoderListener     // Catch:{ all -> 0x0284 }
            if (r2 == 0) goto L_0x01d3
            r0 = r26
            com.yunos.tv.tvsdk.media.mediacodec.MediaDecoderThread$OnDecoderListener r2 = r0.mOnDecoderListener     // Catch:{ all -> 0x0284 }
            r6 = 5
            r2.onDecoder(r6)     // Catch:{ all -> 0x0284 }
        L_0x01d3:
            monitor-exit(r4)     // Catch:{ all -> 0x0284 }
        L_0x01d4:
            long r6 = java.lang.System.currentTimeMillis()
            r0 = r26
            long r0 = r0.mStartMsec
            r24 = r0
            long r6 = r6 - r24
            int r2 = (r22 > r6 ? 1 : (r22 == r6 ? 0 : -1))
            if (r2 <= 0) goto L_0x01ee
            r6 = 10
            sleep(r6)     // Catch:{ InterruptedException -> 0x01ea }
            goto L_0x01d4
        L_0x01ea:
            r12 = move-exception
            r12.printStackTrace()
        L_0x01ee:
            r0 = r26
            android.media.MediaCodec r2 = r0.decoder
            r4 = 1
            r0 = r20
            r2.releaseOutputBuffer(r0, r4)
        L_0x01f8:
            int r2 = r14.flags
            r2 = r2 & 4
            if (r2 == 0) goto L_0x0045
            r13 = 4
            r2 = 2
            r0 = r26
            r0.sendMsgCode(r2)
            java.lang.String r2 = "VideoDecoderThread"
            java.lang.String r4 = "OutputBuffer BUFFER_FLAG_END_OF_STREAM, decode finished!!!!!"
            android.util.Log.d(r2, r4)
            goto L_0x0062
        L_0x0210:
            monitor-exit(r4)     // Catch:{ all -> 0x0227 }
            java.lang.String r2 = "VideoDecoderThread"
            java.lang.String r4 = "wait load data buffer"
            android.util.Log.d(r2, r4)     // Catch:{ InterruptedException -> 0x0221 }
            r6 = 100
            sleep(r6)     // Catch:{ InterruptedException -> 0x0221 }
            goto L_0x00fc
        L_0x0221:
            r12 = move-exception
            r12.printStackTrace()
            goto L_0x0123
        L_0x0227:
            r2 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0227 }
            throw r2
        L_0x022a:
            r0 = r26
            android.media.MediaCodec r2 = r0.decoder
            r4 = 0
            r0 = r26
            android.media.MediaExtractor r6 = r0.extractor
            long r6 = r6.getSampleTime()
            r8 = 0
            r2.queueInputBuffer(r3, r4, r5, r6, r8)
            r0 = r26
            android.media.MediaExtractor r2 = r0.extractor
            r2.advance()
            goto L_0x0176
        L_0x0244:
            java.lang.String r2 = "VideoDecoderThread"
            java.lang.String r4 = "INFO_OUTPUT_BUFFERS_CHANGED"
            android.util.Log.d(r2, r4)
            r0 = r26
            android.media.MediaCodec r2 = r0.decoder
            java.nio.ByteBuffer[] r21 = r2.getOutputBuffers()
            goto L_0x01f8
        L_0x0256:
            java.lang.String r2 = "VideoDecoderThread"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r6 = "New format "
            java.lang.StringBuilder r4 = r4.append(r6)
            r0 = r26
            android.media.MediaCodec r6 = r0.decoder
            android.media.MediaFormat r6 = r6.getOutputFormat()
            java.lang.StringBuilder r4 = r4.append(r6)
            java.lang.String r4 = r4.toString()
            android.util.Log.d(r2, r4)
            goto L_0x01f8
        L_0x0279:
            java.lang.String r2 = "VideoDecoderThread"
            java.lang.String r4 = "dequeueOutputBuffer timed outm, try again later!"
            android.util.Log.d(r2, r4)
            goto L_0x01f8
        L_0x0284:
            r2 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0284 }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tv.tvsdk.media.mediacodec.MediaDecoderThread.run():void");
    }

    private void sendMsgCode(int msgCode) {
        if (this.mOnDecoderListener != null) {
            this.mOnDecoderListener.onDecoder(msgCode);
        }
    }
}
