package com.tvtaobao.android.marketgames.dfw;

import com.tvtaobao.android.marketgames.dfw.core.ObjDice;
import com.yunos.tv.alitvasr.sdk.AbstractClientManager;

public class DfwConfig {
    public static ObjDice.DicePathStyle dicePathStyle = ObjDice.DicePathStyle.B;
    public static long diceResultDuration = AbstractClientManager.BIND_SERVICE_TIMEOUT;
    public static long diceRollDuration = 1000;
    public static boolean drawAnimPath = false;
    public static boolean drawRenderInfo = false;
    public static boolean drawSpriteBounds = false;
    public static long resourceLoad_TimeOut = 30000;
    public static long rollDiceBtnEffectDuration = AbstractClientManager.BIND_SERVICE_TIMEOUT;
    public static boolean showDebugInfo = false;
    public static boolean showRenderThreadLog = false;
    public static boolean test_loadTimeOut = false;
}
