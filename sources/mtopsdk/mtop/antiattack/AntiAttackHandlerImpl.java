package mtopsdk.mtop.antiattack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.atomic.AtomicBoolean;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.global.SwitchConfig;
import mtopsdk.xstate.XState;

public class AntiAttackHandlerImpl implements AntiAttackHandler {
    private static final int DEFAULT_WAIT_RESULT_TIME_OUT = 20000;
    private static final String MTOPSDK_ANTI_ATTACK_ACTION = "mtopsdk.mtop.antiattack.checkcode.validate.activity_action";
    private static final String MTOPSDK_ANTI_ATTACK_RESULT_ACTION = "mtopsdk.extra.antiattack.result.notify.action";
    private static final String TAG = "mtopsdk.AntiAttackHandlerImpl";
    final BroadcastReceiver antiAttackReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            try {
                TBSdkLog.i(AntiAttackHandlerImpl.TAG, "[onReceive]AntiAttack result: " + intent.getStringExtra("Result"));
            } catch (Exception e) {
            } finally {
                AntiAttackHandlerImpl.this.handler.removeCallbacks(AntiAttackHandlerImpl.this.timeoutRunnable);
                AntiAttackHandlerImpl.this.isHandling.set(false);
            }
        }
    };
    final Handler handler = new Handler(Looper.getMainLooper());
    private final IntentFilter intentFilter = new IntentFilter(MTOPSDK_ANTI_ATTACK_RESULT_ACTION);
    final AtomicBoolean isHandling = new AtomicBoolean(false);
    final Context mContext;
    final Runnable timeoutRunnable = new Runnable() {
        public void run() {
            if (AntiAttackHandlerImpl.this.isHandling.compareAndSet(true, false)) {
                TBSdkLog.w(AntiAttackHandlerImpl.TAG, "waiting antiattack result time out!");
                try {
                    AntiAttackHandlerImpl.this.mContext.unregisterReceiver(AntiAttackHandlerImpl.this.antiAttackReceiver);
                } catch (Exception e) {
                }
            }
        }
    };

    public AntiAttackHandlerImpl(Context context) {
        this.mContext = context;
    }

    public void handle(String location, String ext) {
        if (StringUtils.isNotBlank(location)) {
            try {
                String loc = new StringBuilder(location).toString();
                boolean isBackground = XState.isAppBackground();
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    StringBuilder builder = new StringBuilder("[handle]execute new 419 Strategy,");
                    builder.append("location=").append(loc);
                    builder.append(", isBackground=").append(isBackground);
                    TBSdkLog.i(TAG, builder.toString());
                }
                if (!isBackground && this.isHandling.compareAndSet(false, true)) {
                    long timeOutInterval = SwitchConfig.getInstance().getGlobalAttackAttackWaitInterval();
                    this.handler.postDelayed(this.timeoutRunnable, timeOutInterval > 0 ? 1000 * timeOutInterval : 20000);
                    Intent in = new Intent();
                    in.setAction(MTOPSDK_ANTI_ATTACK_ACTION);
                    in.setPackage(this.mContext.getPackageName());
                    in.setFlags(268435456);
                    in.putExtra("Location", loc);
                    this.mContext.startActivity(in);
                    this.mContext.registerReceiver(this.antiAttackReceiver, this.intentFilter);
                }
            } catch (Exception e) {
                TBSdkLog.w(TAG, "[handle] execute new 419 Strategy error.", (Throwable) e);
            }
        }
    }
}
