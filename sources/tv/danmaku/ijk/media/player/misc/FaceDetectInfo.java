package tv.danmaku.ijk.media.player.misc;

public class FaceDetectInfo {
    public float[] faceLandmarks;
    public int[] faceRect;

    public FaceDetectInfo(int[] faceRect2, float[] faceLandmarks2) {
        this.faceRect = faceRect2;
        this.faceLandmarks = faceLandmarks2;
    }
}
