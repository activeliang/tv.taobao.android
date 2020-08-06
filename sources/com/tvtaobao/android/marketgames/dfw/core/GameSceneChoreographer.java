package com.tvtaobao.android.marketgames.dfw.core;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceView;
import com.tvtaobao.android.marketgames.dfw.DfwConfig;
import com.tvtaobao.android.marketgames.dfw.core.ObjDice;
import com.tvtaobao.android.marketgames.dfw.core.ObjPlayer;
import com.tvtaobao.android.marketgames.dfw.wares.IBoGameData;
import com.tvtaobao.android.marketgames.dfw.wares.IDataRequest;
import com.tvtaobao.android.marketgames.dfw.wares.IEventListener;
import com.tvtaobao.android.marketgames.dfw.wares.IImageLoader;
import com.tvtaobao.android.ui3.helper.EasySurfaceView;
import com.tvtaobao.android.values.Flag;

public class GameSceneChoreographer implements EasySurfaceView.FrameMaker {
    public static final int FLAG_SKIP_DRAW = 1;
    private static final String TAG = GameSceneChoreographer.class.getSimpleName();
    private Rect bgBmpRect = new Rect();
    private int bgColor = Color.parseColor("#414c5c");
    private Activity context;
    private IDataRequest dataRequest;
    private ObjDice dice;
    private IEventListener eventListener;
    public Flag flag = new Flag();
    private IBoGameData gameData;
    private PointF globalScale = new PointF(1.0f, 1.0f);
    private GameResourceManager grm;
    private ObjPlayer player;
    private ObjBigPrice price;
    private Paint sceneBmpPaint = new Paint();
    private SurfaceView surfaceView;
    Rect surfaceViewSize = new Rect();
    private Paint txtPaint = new Paint();
    private Handler uiHandler = new Handler(Looper.getMainLooper());
    private Rect viewRect = new Rect();

    public GameSceneChoreographer(Activity context2, SurfaceView surfaceView2, IImageLoader imgLoader, IBoGameData gameData2, IDataRequest dataRequest2, IEventListener eventListener2) {
        this.context = context2;
        this.surfaceView = surfaceView2;
        this.gameData = gameData2;
        this.dataRequest = dataRequest2;
        this.eventListener = eventListener2;
        if (this.gameData != null) {
            try {
                this.bgColor = Color.parseColor(this.gameData.getBgColor());
            } catch (Throwable th) {
            }
        }
        this.grm = new GameResourceManager(context2, imgLoader, gameData2);
        this.grm.loadResource();
        this.player = new ObjPlayer(this);
        this.dice = new ObjDice(this);
        this.price = new ObjBigPrice(this);
        this.sceneBmpPaint.setAntiAlias(true);
        this.txtPaint.setAntiAlias(true);
        this.txtPaint.setColor(-16711936);
        this.txtPaint.setStyle(Paint.Style.STROKE);
        if (this.gameData != null) {
            this.player.setPoints(this.gameData.getPositions());
            PointF playPos = null;
            if (this.gameData.getPositions() != null && !this.gameData.getPositions().isEmpty() && this.gameData.getPositions().size() > this.gameData.getPlayerPos() && this.gameData.getPlayerPos() >= 0) {
                playPos = this.gameData.getPositions().get(this.gameData.getPlayerPos());
            }
            this.player.setPlayerPos(playPos);
            this.dice.setBgnAndEnd(this.gameData.getDiceBgnAndEnd());
            this.price.setPoints(gameData2.getGameUltimatePrizePos());
        }
        this.surfaceViewSize.set(surfaceView2.getWidth(), surfaceView2.getHeight(), 0, 0);
    }

    public GameResourceManager getGrm() {
        return this.grm;
    }

    public Handler getUiHandler() {
        return this.uiHandler;
    }

    public IDataRequest getDataRequest() {
        return this.dataRequest;
    }

    public Activity getContext() {
        return this.context;
    }

    public IEventListener getEventListener() {
        return this.eventListener;
    }

    public void onGameDataUpdate() {
        if (this.gameData != null) {
            this.player.setPoints(this.gameData.getPositions());
            if (this.gameData.getPositions() != null && !this.gameData.getPositions().isEmpty() && this.gameData.getPositions().size() > this.gameData.getPlayerPos() && this.gameData.getPlayerPos() >= 0) {
                this.player.setPlayerPos(this.gameData.getPositions().get(this.gameData.getPlayerPos()));
            }
        }
    }

    public boolean isRollOrJumpDoing() {
        if (this.player.isJumpDoing() || this.dice.isRolling()) {
            return true;
        }
        return false;
    }

    public void jump(int fromPos, int toPos, ObjPlayer.JumpDirection direction) {
        this.player.jump(fromPos, toPos, direction);
    }

    public void playDice(ObjDice.DiceRltListener diceRltListener) {
        this.dice.play(diceRltListener);
    }

    public void onBtnRollDicePress(boolean press) {
        this.dice.onBtnRollDicePress(press);
    }

    public void applyGlobalScale(PointF p) {
        if (p != null) {
            p.x *= this.globalScale.x;
            p.y *= this.globalScale.y;
        }
    }

    public void applyGlobalScale(RectF r) {
        if (r != null) {
            r.right = r.left + (r.width() * this.globalScale.x);
            r.bottom = r.top + (r.height() * this.globalScale.y);
        }
    }

    public void applyGlobalScale(Rect r) {
        if (r != null) {
            r.right = (int) (((float) r.left) + (((float) r.width()) * this.globalScale.x));
            r.bottom = (int) (((float) r.top) + (((float) r.height()) * this.globalScale.y));
        }
    }

    public float applyGlobalScaleX(float f) {
        return this.globalScale.x * f;
    }

    public float applyGlobalScaleY(float f) {
        return this.globalScale.y * f;
    }

    public void makeFrame(Canvas canvas, long frame, long time) {
        if (!this.flag.hasFlag(1)) {
            if (!(this.surfaceView.getWidth() == this.surfaceViewSize.left && this.surfaceView.getHeight() == this.surfaceViewSize.top)) {
                this.surfaceViewSize.set(this.surfaceView.getWidth(), this.surfaceView.getHeight(), this.surfaceViewSize.right, this.surfaceViewSize.bottom);
                Log.i(TAG, "surfaceView size changed:" + this.surfaceViewSize);
            }
            canvas.drawColor(this.bgColor);
            if (this.grm.gameSceneBmp != null) {
                this.globalScale.set((((float) this.surfaceViewSize.left) * 1.0f) / 1920.0f, (((float) this.surfaceViewSize.top) * 1.0f) / 1080.0f);
                this.bgBmpRect.set(0, 0, this.grm.gameSceneBmp.getWidth(), this.grm.gameSceneBmp.getHeight());
                this.viewRect.set(0, 0, this.surfaceViewSize.left, this.surfaceViewSize.top);
                canvas.drawBitmap(this.grm.gameSceneBmp, this.bgBmpRect, this.viewRect, this.sceneBmpPaint);
            }
            this.player.makeFrame(canvas, frame, time);
            this.price.makeFrame(canvas, frame, time);
            this.dice.makeFrame(canvas, frame, time);
            if (DfwConfig.drawRenderInfo) {
                canvas.save();
                canvas.drawText("surfaceViewSize:" + this.surfaceViewSize.toString(), 20.0f, 20.0f, this.txtPaint);
                canvas.drawText("globalScale:" + this.globalScale.toString(), 20.0f, 50.0f, this.txtPaint);
                canvas.restore();
            }
        }
    }

    public void onDestory() {
        this.surfaceView = null;
        this.gameData = null;
        this.eventListener = null;
        this.dataRequest = null;
        this.grm = null;
        this.uiHandler = null;
    }
}
