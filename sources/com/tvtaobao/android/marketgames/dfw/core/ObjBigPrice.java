package com.tvtaobao.android.marketgames.dfw.core;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.internal.view.SupportMenu;
import android.util.Log;
import com.tvtaobao.android.marketgames.dfw.DfwConfig;
import com.tvtaobao.android.ui3.helper.EasySurfaceView;
import java.util.List;

public class ObjBigPrice implements EasySurfaceView.FrameMaker {
    /* access modifiers changed from: private */
    public static final String TAG = ObjBigPrice.class.getSimpleName();
    /* access modifiers changed from: private */
    public PointF bgn;
    private long bgnTime = System.currentTimeMillis();
    private Runnable calculatePathTask = new Runnable() {
        public void run() {
            if (ObjBigPrice.this.points != null && ObjBigPrice.this.points.size() >= 2) {
                PointF tmpBgn = (PointF) ObjBigPrice.this.points.get(0);
                PointF tmpEnd = (PointF) ObjBigPrice.this.points.get(1);
                if (tmpBgn != null && tmpEnd != null) {
                    PointF unused = ObjBigPrice.this.bgn = new PointF(tmpBgn.x, tmpBgn.y);
                    PointF unused2 = ObjBigPrice.this.end = new PointF(tmpBgn.x + 300.0f, tmpBgn.y);
                    ObjBigPrice.this.choreographer.applyGlobalScale(ObjBigPrice.this.bgn);
                    ObjBigPrice.this.choreographer.applyGlobalScale(ObjBigPrice.this.end);
                    ObjBigPrice.this.choreographer.applyGlobalScale(tmpEnd);
                    PointF unused3 = ObjBigPrice.this.ctl1 = new PointF(ObjBigPrice.this.bgn.x, tmpEnd.y);
                    PointF unused4 = ObjBigPrice.this.ctl2 = new PointF(ObjBigPrice.this.end.x, tmpEnd.y);
                    ObjBigPrice.this.path.reset();
                    ObjBigPrice.this.path.moveTo(ObjBigPrice.this.bgn.x, ObjBigPrice.this.bgn.y);
                    ObjBigPrice.this.path.cubicTo(ObjBigPrice.this.ctl1.x, ObjBigPrice.this.ctl1.y, ObjBigPrice.this.ctl2.x, ObjBigPrice.this.ctl2.y, ObjBigPrice.this.end.x, ObjBigPrice.this.end.y);
                    PathMeasure measure = new PathMeasure(ObjBigPrice.this.path, false);
                    float pathMeasureLength = measure.getLength();
                    Log.i(ObjBigPrice.TAG, "PathMeasure.length " + pathMeasureLength);
                    boolean simpleSuccess = true;
                    for (int i = 0; i < ObjBigPrice.this.simpleCount; i++) {
                        if (measure.getPosTan(((((float) i) * 1.0f) / ((float) ObjBigPrice.this.simpleCount)) * pathMeasureLength, ObjBigPrice.this.simpleItem, (float[]) null)) {
                            ObjBigPrice.this.simpleArray[i * 2] = ObjBigPrice.this.simpleItem[0];
                            ObjBigPrice.this.simpleArray[(i * 2) + 1] = ObjBigPrice.this.simpleItem[1];
                        } else {
                            simpleSuccess = false;
                        }
                    }
                    if (simpleSuccess) {
                        boolean unused5 = ObjBigPrice.this.simpled = true;
                    }
                    PointF unused6 = ObjBigPrice.this.pos = new PointF(ObjBigPrice.this.bgn.x, ObjBigPrice.this.simpleArray[1]);
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public GameSceneChoreographer choreographer;
    /* access modifiers changed from: private */
    public PointF ctl1;
    /* access modifiers changed from: private */
    public PointF ctl2;
    private long duration = 1200;
    /* access modifiers changed from: private */
    public PointF end;
    private Paint linePaint;
    /* access modifiers changed from: private */
    public Path path = new Path();
    /* access modifiers changed from: private */
    public List<PointF> points;
    /* access modifiers changed from: private */
    public PointF pos;
    /* access modifiers changed from: private */
    public float[] simpleArray = new float[(this.simpleCount * 2)];
    /* access modifiers changed from: private */
    public int simpleCount = 30;
    /* access modifiers changed from: private */
    public float[] simpleItem = new float[2];
    /* access modifiers changed from: private */
    public boolean simpled = false;
    private Drawable sprite;
    private PointF tmpPos = new PointF();
    private Paint txtPaint;

    public ObjBigPrice(GameSceneChoreographer choreographer2) {
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

    public void setPoints(List<PointF> points2) {
        if (points2 != null && points2.size() >= 2) {
            this.points = points2;
            this.path.reset();
        }
    }

    public void makeFrame(Canvas canvas, long frame, long time) {
        if (this.sprite == null && this.choreographer.getGrm().ultimatePrizeBmp != null) {
            this.sprite = new BitmapDrawable(this.choreographer.getGrm().ultimatePrizeBmp);
            this.tmpPos.set((float) this.choreographer.getGrm().ultimatePrizeBmp.getWidth(), (float) this.choreographer.getGrm().ultimatePrizeBmp.getHeight());
            this.choreographer.applyGlobalScale(this.tmpPos);
            this.sprite.setBounds(0, 0, (int) this.tmpPos.x, (int) this.tmpPos.y);
        }
        if (this.path != null && this.path.isEmpty()) {
            this.calculatePathTask.run();
        }
        if (this.bgn != null && this.end != null && this.sprite != null) {
            canvas.save();
            if (!(this.sprite == null || this.pos == null || !this.simpled)) {
                float animVal = ((((float) (time - this.bgnTime)) * 1.0f) / ((float) this.duration)) * 1.0f;
                if (animVal < 0.0f) {
                    animVal = 0.0f;
                }
                if (animVal > 1.0f) {
                    animVal = 1.0f;
                    this.bgnTime = System.currentTimeMillis();
                }
                this.pos.set(this.bgn.x, this.simpleArray[(((int) (((float) (this.simpleCount - 1)) * animVal)) * 2) + 1]);
                canvas.translate(this.pos.x - ((float) (this.sprite.getBounds().width() / 2)), this.pos.y - ((float) (this.sprite.getBounds().height() / 2)));
                this.sprite.draw(canvas);
                if (DfwConfig.drawSpriteBounds) {
                    canvas.drawRect(this.sprite.getBounds(), this.linePaint);
                }
                if (DfwConfig.drawRenderInfo) {
                    canvas.drawText("pos:" + this.pos.toString(), 20.0f, 20.0f, this.txtPaint);
                }
            }
            canvas.restore();
            canvas.save();
            if (DfwConfig.drawAnimPath && this.linePaint != null && this.path != null && !this.path.isEmpty()) {
                canvas.translate(0.0f, 0.0f);
                canvas.drawPath(this.path, this.linePaint);
            }
            canvas.restore();
        }
    }
}
