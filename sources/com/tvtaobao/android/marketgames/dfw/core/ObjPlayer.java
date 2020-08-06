package com.tvtaobao.android.marketgames.dfw.core;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.internal.view.SupportMenu;
import android.util.Log;
import com.tvtaobao.android.marketgames.dfw.DfwConfig;
import com.tvtaobao.android.ui3.helper.EasySurfaceView;
import com.tvtaobao.android.values.Flag;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ObjPlayer implements EasySurfaceView.FrameMaker {
    private static final int FLAG_JUMP_DOING = 1;
    /* access modifiers changed from: private */
    public static final String TAG = ObjPlayer.class.getSimpleName();
    private PointF bgn;
    private long bgnTime = 0;
    /* access modifiers changed from: private */
    public GameSceneChoreographer choreographer;
    private long duration = 300;
    private PointF end;
    private PointF end_cp1;
    private PointF end_cp2;
    private Flag flag = new Flag();
    private long jumpBgnTime = 0;
    private long jumpEndTime = 0;
    /* access modifiers changed from: private */
    public JumpListener jumpListener;
    private Paint linePaint;
    private Path movePath = new Path();
    private float[] movePathSimpleArray = new float[(this.pointCount * 2)];
    private boolean movePathSimpleSuccess = false;
    private PointF playerPosCfg;
    private int pointCount = 100;
    /* access modifiers changed from: private */
    public List<PointF> points;
    private Path scalePath = new Path();
    private float[] scalePathSimpleArray = new float[(this.pointCount * 2)];
    private boolean scalePathSimpleSuccess = false;
    private float[] simpleItem = new float[2];
    private BitmapDrawable sprite;
    private PointF tmpPos = new PointF();
    private Paint txtPaint;

    public enum JumpDirection {
        loop_0_to_n,
        loop_n_to_0
    }

    public interface JumpListener {
        void onJumpFinish();
    }

    public ObjPlayer(GameSceneChoreographer choreographer2) {
        this.choreographer = choreographer2;
        this.linePaint = new Paint();
        this.linePaint.setAntiAlias(true);
        this.linePaint.setColor(SupportMenu.CATEGORY_MASK);
        this.linePaint.setStyle(Paint.Style.STROKE);
        this.txtPaint = new Paint();
        this.txtPaint.setAntiAlias(true);
        this.txtPaint.setColor(-16711936);
        this.txtPaint.setStyle(Paint.Style.STROKE);
    }

    public boolean isJumpDoing() {
        if (this.flag.hasFlag(1)) {
            return true;
        }
        return false;
    }

    public void setPlayerPos(PointF playerPos) {
        if (playerPos != null) {
            this.playerPosCfg = new PointF(playerPos.x, playerPos.y);
        }
    }

    public List<PointF> getPoints() {
        return this.points;
    }

    public void setPoints(List<PointF> points2) {
        this.points = points2;
    }

    public void jump(int from, int to, final JumpDirection jumpDirection) {
        Log.i(TAG, "jump params " + from + "," + to + "," + jumpDirection);
        if (this.points != null && !this.points.isEmpty()) {
            int size = this.points.size() * 10000000;
            while (from < 0) {
                from += size;
            }
            while (to < 0) {
                to += size;
            }
            int from2 = from % this.points.size();
            int to2 = to % this.points.size();
            Log.i(TAG, "jump from:" + from2 + " to:" + to2 + " direction:" + jumpDirection);
            if (from2 != to2 && jumpDirection != null && !isJumpDoing()) {
                final AtomicInteger fromObj = new AtomicInteger(from2);
                final AtomicInteger toObj = new AtomicInteger(to2);
                this.jumpListener = new JumpListener() {
                    public void onJumpFinish() {
                        if (fromObj.get() % ObjPlayer.this.points.size() != toObj.get() % ObjPlayer.this.points.size()) {
                            int oldPos = -1;
                            int nowPos = -1;
                            if (jumpDirection == JumpDirection.loop_0_to_n) {
                                oldPos = (fromObj.getAndIncrement() + ObjPlayer.this.points.size()) % ObjPlayer.this.points.size();
                                nowPos = (fromObj.get() + ObjPlayer.this.points.size()) % ObjPlayer.this.points.size();
                            } else if (jumpDirection == JumpDirection.loop_n_to_0) {
                                oldPos = (fromObj.getAndDecrement() + ObjPlayer.this.points.size()) % ObjPlayer.this.points.size();
                                nowPos = (fromObj.get() + ObjPlayer.this.points.size()) % ObjPlayer.this.points.size();
                            }
                            ObjPlayer.this.jump((PointF) ObjPlayer.this.points.get(oldPos), (PointF) ObjPlayer.this.points.get(nowPos), ObjPlayer.this.jumpListener);
                        } else if (ObjPlayer.this.choreographer.getEventListener() != null) {
                            ObjPlayer.this.choreographer.getEventListener().onJumpDone(ObjPlayer.this.choreographer.getContext(), toObj.get());
                        }
                    }
                };
                int oldPos = -1;
                int nowPos = -1;
                if (jumpDirection == JumpDirection.loop_0_to_n) {
                    oldPos = (fromObj.getAndIncrement() + this.points.size()) % this.points.size();
                    nowPos = (fromObj.get() + this.points.size()) % this.points.size();
                } else if (jumpDirection == JumpDirection.loop_n_to_0) {
                    oldPos = (fromObj.getAndDecrement() + this.points.size()) % this.points.size();
                    nowPos = (fromObj.get() + this.points.size()) % this.points.size();
                }
                jump(this.points.get(oldPos), this.points.get(nowPos), this.jumpListener);
            }
        }
    }

    public void jump(PointF a, PointF b, JumpListener jumpListener2) {
        if (a != null && b != null && !isJumpDoing()) {
            this.flag.setFlag(1);
            this.jumpListener = jumpListener2;
            this.bgn = new PointF(a.x, a.y);
            this.end = new PointF(b.x, b.y);
            this.choreographer.applyGlobalScale(this.bgn);
            this.choreographer.applyGlobalScale(this.end);
            this.bgnTime = System.currentTimeMillis();
            this.jumpBgnTime = this.bgnTime + (this.duration / 10);
            this.jumpEndTime = (this.bgnTime + this.duration) - (this.duration / 10);
            this.movePath.reset();
            this.movePath.moveTo(this.bgn.x, this.bgn.y);
            this.end_cp1 = new PointF(((float) ((this.bgn.x > this.end.x ? -1 : 1) * 25)) + this.bgn.x, this.bgn.y - 90.0f);
            this.end_cp2 = new PointF(this.end.x - ((float) ((this.bgn.x > this.end.x ? -1 : 1) * 20)), this.end.y - 90.0f);
            this.movePath.cubicTo(this.end_cp1.x, this.end_cp1.y, this.end_cp2.x, this.end_cp2.y, this.end.x, this.end.y);
            if (this.movePath != null) {
                PathMeasure measure = new PathMeasure(this.movePath, false);
                float pathMeasureLength = measure.getLength();
                Log.i(TAG, "PathMeasure.length " + pathMeasureLength);
                this.movePathSimpleSuccess = true;
                for (int i = 0; i < this.pointCount; i++) {
                    if (measure.getPosTan(((((float) i) * 1.0f) / ((float) this.pointCount)) * pathMeasureLength, this.simpleItem, (float[]) null)) {
                        this.movePathSimpleArray[i * 2] = this.simpleItem[0];
                        this.movePathSimpleArray[(i * 2) + 1] = this.simpleItem[1];
                    } else {
                        this.movePathSimpleSuccess = false;
                    }
                }
            }
            this.scalePath.reset();
            this.scalePath.moveTo(0.0f, 100.0f);
            this.scalePath.cubicTo(30.0f, 100.0f, 70.0f, 85.0f, 100.0f, 85.0f);
            this.scalePath.cubicTo(130.0f, 85.0f, 170.0f, 100.0f, 200.0f, 100.0f);
            this.scalePath.lineTo(800.0f, 100.0f);
            this.scalePath.cubicTo(830.0f, 100.0f, 870.0f, 85.0f, 900.0f, 85.0f);
            this.scalePath.cubicTo(930.0f, 85.0f, 970.0f, 100.0f, 1000.0f, 100.0f);
            if (this.scalePath != null) {
                PathMeasure measure2 = new PathMeasure(this.scalePath, false);
                float pathMeasureLength2 = measure2.getLength();
                Log.i(TAG, "PathMeasure.length " + pathMeasureLength2);
                this.scalePathSimpleSuccess = true;
                for (int i2 = 0; i2 < this.pointCount; i2++) {
                    if (measure2.getPosTan(((((float) i2) * 1.0f) / ((float) this.pointCount)) * pathMeasureLength2, this.simpleItem, (float[]) null)) {
                        this.scalePathSimpleArray[i2 * 2] = this.simpleItem[0];
                        this.scalePathSimpleArray[(i2 * 2) + 1] = this.simpleItem[1];
                    } else {
                        this.scalePathSimpleSuccess = false;
                    }
                }
            }
        }
    }

    public void makeFrame(Canvas canvas, long frame, long time) {
        if (this.sprite == null && this.choreographer.getGrm().playerBmp != null) {
            this.sprite = new BitmapDrawable(this.choreographer.getGrm().playerBmp);
            this.tmpPos.set((float) this.choreographer.getGrm().playerBmp.getWidth(), (float) this.choreographer.getGrm().playerBmp.getHeight());
            this.choreographer.applyGlobalScale(this.tmpPos);
            this.sprite.setBounds(0, 0, (int) this.tmpPos.x, (int) this.tmpPos.y);
        }
        if (this.points != null && this.sprite != null) {
            canvas.save();
            if (this.flag.hasFlag(1)) {
                if (this.movePathSimpleSuccess) {
                    float scaleAnimVal = ((((float) (time - this.bgnTime)) * 1.0f) / ((float) this.duration)) * 1.0f;
                    if (scaleAnimVal < 0.0f) {
                        scaleAnimVal = 0.0f;
                    }
                    if (scaleAnimVal > 1.0f) {
                        scaleAnimVal = 1.0f;
                    }
                    int pos = Math.round(((float) this.pointCount) * scaleAnimVal);
                    if (pos < 0) {
                        pos = 0;
                    }
                    if (pos > this.pointCount - 1) {
                        pos = this.pointCount - 1;
                    }
                    this.sprite.setBounds(0, 0, (int) this.choreographer.applyGlobalScaleX((float) this.sprite.getBitmap().getWidth()), (int) (((float) this.sprite.getBitmap().getHeight()) * this.choreographer.applyGlobalScaleY(this.scalePathSimpleArray[(pos * 2) + 1] / 100.0f)));
                    float moveAnimVal = ((((float) (time - this.jumpBgnTime)) * 1.0f) / ((float) (this.jumpEndTime - this.jumpBgnTime))) * 1.0f;
                    if (moveAnimVal < 0.0f) {
                        moveAnimVal = 0.0f;
                    }
                    if (moveAnimVal > 1.0f) {
                        moveAnimVal = 1.0f;
                    }
                    int pos2 = Math.round(((float) this.pointCount) * moveAnimVal);
                    if (pos2 < 0) {
                        pos2 = 0;
                    }
                    if (pos2 > this.pointCount - 1) {
                        pos2 = this.pointCount - 1;
                    }
                    this.tmpPos.set(this.movePathSimpleArray[pos2 * 2] - (((float) this.sprite.getBounds().width()) / 2.0f), this.movePathSimpleArray[(pos2 * 2) + 1] - (((float) this.sprite.getBounds().height()) * 0.875f));
                    canvas.translate(this.tmpPos.x, this.tmpPos.y);
                    this.sprite.draw(canvas);
                    if (DfwConfig.drawSpriteBounds) {
                        canvas.drawRect(this.sprite.getBounds(), this.linePaint);
                    }
                    if (DfwConfig.drawRenderInfo) {
                        canvas.drawText("playerPos:" + this.tmpPos.toString(), 20.0f, 20.0f, this.txtPaint);
                    }
                    float animVal = ((((float) (time - this.bgnTime)) * 1.0f) / ((float) this.duration)) * 1.0f;
                    if (animVal < 0.0f) {
                        animVal = 0.0f;
                    }
                    if (animVal > 1.0f) {
                        animVal = 1.0f;
                    }
                    if (animVal >= 1.0f) {
                        this.flag.clrFlag(1);
                        if (this.jumpListener != null) {
                            final JumpListener tmp = this.jumpListener;
                            this.choreographer.getUiHandler().post(new Runnable() {
                                public void run() {
                                    if (tmp != null) {
                                        Log.i(ObjPlayer.TAG, "onJumpFinish");
                                        tmp.onJumpFinish();
                                    }
                                }
                            });
                        }
                    }
                }
            } else if (this.movePathSimpleSuccess) {
                int pos3 = this.pointCount - 1;
                this.tmpPos.set(this.movePathSimpleArray[pos3 * 2] - (((float) this.sprite.getBounds().width()) / 2.0f), this.movePathSimpleArray[(pos3 * 2) + 1] - (((float) this.sprite.getBounds().height()) * 0.875f));
                canvas.translate(this.tmpPos.x, this.tmpPos.y);
                this.sprite.draw(canvas);
                if (DfwConfig.drawSpriteBounds) {
                    canvas.drawRect(this.sprite.getBounds(), this.linePaint);
                }
                if (DfwConfig.drawRenderInfo) {
                    canvas.drawText("playerPos:" + this.tmpPos.toString(), 20.0f, 30.0f, this.txtPaint);
                }
            } else {
                this.tmpPos.set(this.playerPosCfg);
                this.choreographer.applyGlobalScale(this.tmpPos);
                if (this.tmpPos != null) {
                    canvas.translate(this.tmpPos.x - (((float) this.sprite.getBounds().width()) / 2.0f), this.tmpPos.y - (((float) this.sprite.getBounds().height()) * 0.875f));
                    this.sprite.draw(canvas);
                    if (DfwConfig.drawSpriteBounds) {
                        canvas.drawRect(this.sprite.getBounds(), this.linePaint);
                    }
                    if (DfwConfig.drawRenderInfo) {
                        canvas.drawText("playerPos:" + this.tmpPos.toString(), 20.0f, 30.0f, this.txtPaint);
                    }
                }
            }
            canvas.restore();
            canvas.save();
            if (DfwConfig.drawAnimPath && this.linePaint != null) {
                if (this.movePath != null && !this.movePath.isEmpty()) {
                    canvas.drawPath(this.movePath, this.linePaint);
                }
                if (this.scalePath != null && !this.scalePath.isEmpty()) {
                    canvas.drawPath(this.scalePath, this.linePaint);
                }
            }
            canvas.restore();
        }
    }
}
