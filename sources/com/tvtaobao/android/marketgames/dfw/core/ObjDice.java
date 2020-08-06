package com.tvtaobao.android.marketgames.dfw.core;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.internal.view.SupportMenu;
import android.util.Log;
import com.tvtaobao.android.marketgames.dfw.DfwConfig;
import com.tvtaobao.android.marketgames.dfw.wares.IBoDiceResult;
import com.tvtaobao.android.marketgames.dfw.wares.IDataRequest;
import com.tvtaobao.android.ui3.helper.EasySurfaceView;
import com.tvtaobao.android.ui3.widget.UI3Toast;
import com.tvtaobao.android.values.Flag;
import java.util.List;

public class ObjDice implements EasySurfaceView.FrameMaker {
    private static final int FLAG_DICE_ROLLING = 1;
    private static final int FLAG_ON_BTN_ROLL_DICE_PRESSING = 2;
    /* access modifiers changed from: private */
    public static final String TAG = ObjDice.class.getSimpleName();
    /* access modifiers changed from: private */
    public PointF bgn;
    private long bgnTime = 0;
    private Runnable calculatePathTask = new Runnable() {
        public void run() {
            PointF aa = null;
            PointF bb = null;
            if (ObjDice.this.points != null && ObjDice.this.points.size() >= 2) {
                aa = (PointF) ObjDice.this.points.get(0);
                bb = (PointF) ObjDice.this.points.get(1);
            }
            if (aa != null && bb != null) {
                PointF unused = ObjDice.this.bgn = new PointF(aa.x, aa.y);
                PointF unused2 = ObjDice.this.end = new PointF(bb.x, bb.y);
                ObjDice.this.choreographer.applyGlobalScale(ObjDice.this.bgn);
                ObjDice.this.choreographer.applyGlobalScale(ObjDice.this.end);
                ObjDice.this.path.reset();
                if (DfwConfig.dicePathStyle == DicePathStyle.A) {
                    ObjDice.this.path.moveTo(ObjDice.this.bgn.x, ObjDice.this.bgn.y);
                    PointF pointF = new PointF(ObjDice.this.bgn.x + (((ObjDice.this.end.x - ObjDice.this.bgn.x) * 4.0f) / 5.0f), ObjDice.this.bgn.y + (((ObjDice.this.end.y - ObjDice.this.bgn.y) * 4.0f) / 5.0f));
                    float len = (float) Math.sqrt(Math.pow((double) (pointF.y - ObjDice.this.bgn.y), 2.0d) + Math.pow((double) (pointF.x - ObjDice.this.bgn.x), 2.0d));
                    PointF pointF2 = new PointF(ObjDice.this.bgn.x - (len / 2.0f), ObjDice.this.bgn.y);
                    PointF pointF3 = new PointF(pointF.x - (len / 2.0f), pointF.y);
                    ObjDice.this.path.cubicTo(pointF2.x, pointF2.y, pointF3.x, pointF3.y, pointF.x, pointF.y);
                    PointF pointF4 = new PointF(pointF.x - ((pointF.x - ObjDice.this.bgn.x) / 3.0f), pointF.y + ((ObjDice.this.bgn.y - pointF.y) / 3.0f));
                    float len2 = (float) Math.sqrt(Math.pow((double) (pointF.y - pointF4.y), 2.0d) + Math.pow((double) (pointF.x - pointF4.x), 2.0d));
                    PointF pointF5 = new PointF(pointF.x + len2, pointF.y);
                    PointF pointF6 = new PointF(pointF4.x + len2, pointF4.y);
                    ObjDice.this.path.cubicTo(pointF5.x, pointF5.y, pointF6.x, pointF6.y, pointF4.x, pointF4.y);
                    float len3 = (float) Math.sqrt(Math.pow((double) (ObjDice.this.end.y - pointF4.y), 2.0d) + Math.pow((double) (ObjDice.this.end.x - pointF4.x), 2.0d));
                    PointF pointF7 = new PointF(pointF4.x - (len3 / 2.0f), pointF4.y);
                    PointF pointF8 = new PointF(ObjDice.this.end.x - (len3 / 2.0f), ObjDice.this.end.y);
                    ObjDice.this.path.cubicTo(pointF7.x, pointF7.y, pointF8.x, pointF8.y, ObjDice.this.end.x, ObjDice.this.end.y);
                    PathMeasure pathMeasure = new PathMeasure(ObjDice.this.path, false);
                    float pathMeasureLength = pathMeasure.getLength();
                    Log.i(ObjDice.TAG, "PathMeasure.length " + pathMeasureLength);
                    boolean simpleSuccess = true;
                    for (int i = 0; i < ObjDice.this.pointCount; i++) {
                        if (pathMeasure.getPosTan(((((float) i) * 1.0f) / ((float) ObjDice.this.pointCount)) * pathMeasureLength, ObjDice.this.simpleItem, (float[]) null)) {
                            ObjDice.this.simpleArray[i * 2] = ObjDice.this.simpleItem[0];
                            ObjDice.this.simpleArray[(i * 2) + 1] = ObjDice.this.simpleItem[1];
                        } else {
                            simpleSuccess = false;
                        }
                    }
                    boolean unused3 = ObjDice.this.isSimpleOver = simpleSuccess;
                } else if (DfwConfig.dicePathStyle == DicePathStyle.B) {
                    ObjDice.this.path.moveTo(10.0f, ObjDice.this.bgn.y);
                    PointF pointF9 = new PointF(100.0f, ObjDice.this.choreographer.applyGlobalScaleY(915.0f));
                    PointF cp14p1 = new PointF(50.0f, ObjDice.this.bgn.y);
                    PointF pointF10 = new PointF(pointF9.x - 40.0f, pointF9.y);
                    ObjDice.this.path.cubicTo(cp14p1.x, cp14p1.y, pointF10.x, pointF10.y, pointF9.x, pointF9.y);
                    PointF pointF11 = new PointF(pointF9.x + 600.0f, ObjDice.this.choreographer.applyGlobalScaleY(215.0f));
                    PointF cp14p2 = new PointF(pointF9.x + 250.0f, pointF9.y);
                    PointF pointF12 = new PointF(pointF11.x - 250.0f, pointF11.y);
                    ObjDice.this.path.cubicTo(cp14p2.x, cp14p2.y, pointF12.x, pointF12.y, pointF11.x, pointF11.y);
                    PointF pointF13 = new PointF(pointF11.x + 300.0f, ObjDice.this.choreographer.applyGlobalScaleY(516.0f));
                    PointF cp14p3 = new PointF(pointF11.x + 100.0f, pointF11.y);
                    PointF pointF14 = new PointF(pointF13.x - 100.0f, pointF13.y);
                    ObjDice.this.path.cubicTo(cp14p3.x, cp14p3.y, pointF14.x, pointF14.y, pointF13.x, pointF13.y);
                    PointF pointF15 = new PointF(pointF13.x + 100.0f, ObjDice.this.choreographer.applyGlobalScaleY(327.0f));
                    PointF cp14p4 = new PointF(pointF13.x + 40.0f, pointF13.y);
                    PointF pointF16 = new PointF(pointF15.x - 40.0f, pointF15.y);
                    ObjDice.this.path.cubicTo(cp14p4.x, cp14p4.y, pointF16.x, pointF16.y, pointF15.x, pointF15.y);
                    PointF pointF17 = new PointF(pointF15.x + 100.0f, ObjDice.this.choreographer.applyGlobalScaleY(435.0f));
                    PointF cp14p5 = new PointF(pointF15.x + 40.0f, pointF15.y);
                    PointF pointF18 = new PointF(pointF17.x - 40.0f, pointF17.y);
                    ObjDice.this.path.cubicTo(cp14p5.x, cp14p5.y, pointF18.x, pointF18.y, pointF17.x, pointF17.y);
                    float x = (ObjDice.this.end.x + ObjDice.this.bgn.x) / 2.0f;
                    PathMeasure pathMeasure2 = new PathMeasure(ObjDice.this.path, false);
                    float pathMeasureLength2 = pathMeasure2.getLength();
                    Log.i(ObjDice.TAG, "PathMeasure.length " + pathMeasureLength2);
                    boolean simpleSuccess2 = true;
                    for (int i2 = 0; i2 < ObjDice.this.pointCount; i2++) {
                        if (pathMeasure2.getPosTan(((((float) i2) * 1.0f) / ((float) ObjDice.this.pointCount)) * pathMeasureLength2, ObjDice.this.simpleItem, (float[]) null)) {
                            ObjDice.this.simpleArray[i2 * 2] = x;
                            ObjDice.this.simpleArray[(i2 * 2) + 1] = ObjDice.this.simpleItem[1];
                        } else {
                            simpleSuccess2 = false;
                        }
                    }
                    boolean unused4 = ObjDice.this.isSimpleOver = simpleSuccess2;
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public GameSceneChoreographer choreographer;
    /* access modifiers changed from: private */
    public IBoDiceResult diceResult;
    /* access modifiers changed from: private */
    public Runnable diceResultError;
    private DiceRltListener diceRltListener;
    /* access modifiers changed from: private */
    public PointF end;
    private Flag flag = new Flag();
    /* access modifiers changed from: private */
    public boolean isSimpleOver = false;
    private Paint linePaint;
    /* access modifiers changed from: private */
    public Path path = new Path();
    /* access modifiers changed from: private */
    public int pointCount = 300;
    /* access modifiers changed from: private */
    public List<PointF> points;
    private float[] scaleArray = {0.9f, 1.0f, 1.2f, 1.26f, 1.32f, 1.38f, 1.44f, 1.38f, 1.32f, 1.0f, 1.32f, 1.0f};
    /* access modifiers changed from: private */
    public float[] simpleArray = new float[(this.pointCount * 2)];
    /* access modifiers changed from: private */
    public float[] simpleItem = new float[2];
    private Drawable sprite;
    private Rect tmpRect = new Rect();
    private Paint txtPaint;

    public enum DicePathStyle {
        A,
        B
    }

    public interface DiceRltListener {
        void onDiceRlt(int i);
    }

    public ObjDice(GameSceneChoreographer gsc) {
        this.choreographer = gsc;
        this.linePaint = new Paint();
        this.linePaint.setAntiAlias(true);
        this.linePaint.setColor(SupportMenu.CATEGORY_MASK);
        this.linePaint.setStyle(Paint.Style.STROKE);
        this.txtPaint = new Paint();
        this.txtPaint.setAntiAlias(true);
        this.txtPaint.setColor(-16711936);
        this.txtPaint.setStyle(Paint.Style.STROKE);
    }

    public void setBgnAndEnd(List<PointF> list) {
        if (list != null && list.size() >= 2) {
            this.points = list;
            this.path.reset();
        }
    }

    public void play(DiceRltListener diceRltListener2) {
        if (!this.flag.hasFlag(1)) {
            this.diceRltListener = diceRltListener2;
            this.bgnTime = System.currentTimeMillis();
            this.flag.setFlag(1);
            this.diceResult = null;
            this.diceResultError = null;
            this.choreographer.getDataRequest().reqRock(new IDataRequest.IDRCallBack<IBoDiceResult>() {
                public void onSuccess(IBoDiceResult iBoDiceResult) {
                    IBoDiceResult unused = ObjDice.this.diceResult = iBoDiceResult;
                }

                public void onFailed(String msg) {
                    Runnable unused = ObjDice.this.diceResultError = new Runnable() {
                        public void run() {
                            UI3Toast.makeToast(ObjDice.this.choreographer.getContext(), "骰子转晕了\n请稍后再试").show();
                        }
                    };
                }
            });
        }
    }

    public boolean isRolling() {
        return this.flag.hasFlag(1);
    }

    public void onBtnRollDicePress(boolean press) {
        if (press) {
            this.flag.setFlag(2);
        } else {
            this.flag.clrFlag(2);
        }
    }

    public void makeFrame(Canvas canvas, long frame, long time) {
        if (this.path != null && this.path.isEmpty()) {
            this.calculatePathTask.run();
        }
        if (this.bgn != null && this.end != null) {
            canvas.save();
            if (this.choreographer.getGrm().isDiceLoadFinis()) {
                if (this.flag.hasFlag(1)) {
                    if (this.isSimpleOver) {
                        float animVal = ((((float) (time - this.bgnTime)) * 1.0f) / ((float) DfwConfig.diceRollDuration)) * 1.0f;
                        float totalAnimVal = ((((float) (time - this.bgnTime)) * 1.0f) / ((float) (DfwConfig.diceRollDuration + DfwConfig.diceResultDuration))) * 1.0f;
                        if (totalAnimVal < 0.0f) {
                            totalAnimVal = 0.0f;
                        }
                        if (animVal < 0.0f) {
                            animVal = 0.0f;
                        }
                        if (animVal < 1.0f) {
                            int pos = Math.round(((float) this.pointCount) * animVal);
                            if (pos < 0) {
                                pos = 0;
                            }
                            if (pos > this.pointCount - 1) {
                                pos = this.pointCount - 1;
                            }
                            float scale = this.scaleArray[(int) (((float) this.scaleArray.length) * animVal)];
                            this.tmpRect.set(0, 0, 240, 240);
                            Rect rect = this.tmpRect;
                            rect.set(0, 0, (int) (((float) rect.width()) * scale), (int) (((float) rect.height()) * scale));
                            this.choreographer.applyGlobalScale(rect);
                            this.sprite = this.choreographer.getGrm().diceAnimBmps[(int) (frame % ((long) this.choreographer.getGrm().diceAnimBmps.length))];
                            this.sprite.setBounds(rect);
                            canvas.translate(this.simpleArray[pos * 2] - ((float) (rect.width() / 2)), this.simpleArray[(pos * 2) + 1] - ((float) (rect.height() / 2)));
                            this.sprite.draw(canvas);
                            if (DfwConfig.drawSpriteBounds) {
                                canvas.drawRect(this.sprite.getBounds(), this.linePaint);
                            }
                        } else if (animVal >= 1.0f) {
                            int pos2 = Math.round(((float) this.pointCount) * animVal);
                            if (pos2 < 0) {
                                pos2 = 0;
                            }
                            if (pos2 > this.pointCount - 1) {
                                pos2 = this.pointCount - 1;
                            }
                            int alpha = (int) ((255.0f * (animVal - 1.0f)) / 1.0f);
                            if (alpha < 0) {
                                alpha = 0;
                            }
                            if (alpha > 255) {
                                alpha = 255;
                            }
                            if (this.diceResult != null) {
                                this.sprite = this.choreographer.getGrm().diceRltBmps[(this.diceResult.getDiceResult() - 1) % this.choreographer.getGrm().diceRltBmps.length];
                                if (this.sprite instanceof BitmapDrawable) {
                                    this.tmpRect.set(0, 0, ((BitmapDrawable) this.sprite).getBitmap().getWidth(), ((BitmapDrawable) this.sprite).getBitmap().getHeight());
                                } else {
                                    this.tmpRect.set(0, 0, 780, 450);
                                }
                                Rect rect2 = this.tmpRect;
                                this.choreographer.applyGlobalScale(rect2);
                                canvas.translate(this.simpleArray[pos2 * 2] - ((float) (rect2.width() / 2)), (float) ((this.choreographer.surfaceViewSize.top / 2) - (rect2.height() / 2)));
                                this.sprite.setBounds(rect2);
                                this.sprite.setAlpha(alpha);
                                this.sprite.draw(canvas);
                                if (DfwConfig.drawSpriteBounds) {
                                    canvas.drawRect(this.sprite.getBounds(), this.linePaint);
                                }
                            } else {
                                this.tmpRect.set(0, 0, 240, 240);
                                Rect rect3 = this.tmpRect;
                                this.choreographer.applyGlobalScale(rect3);
                                this.sprite = this.choreographer.getGrm().diceAnimBmps[(int) (frame % ((long) this.choreographer.getGrm().diceAnimBmps.length))];
                                this.sprite.setBounds(rect3);
                                canvas.translate(this.simpleArray[pos2 * 2] - ((float) (rect3.width() / 2)), this.simpleArray[(pos2 * 2) + 1] - ((float) (rect3.height() / 2)));
                                this.sprite.draw(canvas);
                                if (DfwConfig.drawSpriteBounds) {
                                    canvas.drawRect(this.sprite.getBounds(), this.linePaint);
                                }
                            }
                        }
                        if (totalAnimVal >= 1.0f && !(this.diceResult == null && this.diceResultError == null)) {
                            this.flag.clrFlag(1);
                            if (!(this.diceRltListener == null || this.diceResult == null)) {
                                final DiceRltListener tmp = this.diceRltListener;
                                final int diceRlt = this.diceResult.getDiceResult();
                                this.diceRltListener = null;
                                this.choreographer.getUiHandler().post(new Runnable() {
                                    public void run() {
                                        if (tmp != null) {
                                            Log.i(ObjDice.TAG, "onDiceRlt " + diceRlt);
                                            tmp.onDiceRlt(diceRlt);
                                        }
                                    }
                                });
                            }
                            if (this.diceResultError != null) {
                                this.choreographer.getUiHandler().post(this.diceResultError);
                            }
                            this.diceRltListener = null;
                            this.diceResult = null;
                            this.diceResultError = null;
                        }
                    }
                } else if (this.bgn != null) {
                    this.tmpRect.set(0, 0, 240, 240);
                    Rect rect4 = this.tmpRect;
                    if (this.flag.hasFlag(2)) {
                        rect4.set(0, 0, (int) (((float) rect4.width()) * 0.9f), (int) (((float) rect4.height()) * 0.9f));
                    }
                    this.choreographer.applyGlobalScale(rect4);
                    canvas.translate(this.bgn.x - ((float) (rect4.width() / 2)), this.bgn.y - ((float) (rect4.height() / 2)));
                    this.sprite = this.choreographer.getGrm().diceStatic;
                    this.sprite.setBounds(rect4);
                    this.sprite.draw(canvas);
                    if (DfwConfig.drawSpriteBounds) {
                        canvas.drawRect(this.sprite.getBounds(), this.linePaint);
                    }
                }
            }
            canvas.restore();
            canvas.save();
            if (DfwConfig.drawAnimPath && this.isSimpleOver && this.path != null && !this.path.isEmpty() && this.linePaint != null) {
                canvas.drawPath(this.path, this.linePaint);
            }
            canvas.restore();
        }
    }
}
