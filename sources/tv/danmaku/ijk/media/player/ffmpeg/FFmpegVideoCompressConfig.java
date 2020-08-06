package tv.danmaku.ijk.media.player.ffmpeg;

public class FFmpegVideoCompressConfig {
    public int bitrate;
    public int height;
    public String inputPath;
    public String outputPath;
    public int width;

    public static FFmpegVideoCompressConfig create720P() {
        FFmpegVideoCompressConfig config = new FFmpegVideoCompressConfig();
        config.width = 360;
        config.height = 640;
        config.bitrate = 791000;
        return config;
    }
}
