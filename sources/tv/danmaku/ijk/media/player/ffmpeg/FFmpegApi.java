package tv.danmaku.ijk.media.player.ffmpeg;

import java.util.ArrayList;
import java.util.List;

public class FFmpegApi {
    private static final int AAC = 86018;
    private static final int H264 = 28;
    private static final int YUV420P = 0;
    private static final int YUVJ420P = 12;

    public static native String av_base64_encode(byte[] bArr);

    private static native FFmpegVideoInfo dumpVideoInfo(String str);

    public static native int videoCompress(FFmpegVideoCompressConfig fFmpegVideoCompressConfig);

    public int getH264Code() {
        return 28;
    }

    public int getAACCode() {
        return AAC;
    }

    public static FFmpegVideoInfo getVideoInfo(String videoFile) {
        FFmpegVideoInfo info = dumpVideoInfo(videoFile);
        info.H264 = 28;
        info.AAC = AAC;
        return info;
    }

    public static List<Integer> getSupportPixList() {
        ArrayList<Integer> supportPix = new ArrayList<>();
        supportPix.add(0);
        supportPix.add(12);
        return supportPix;
    }
}
