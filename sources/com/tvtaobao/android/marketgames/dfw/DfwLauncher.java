package com.tvtaobao.android.marketgames.dfw;

import android.content.Context;
import android.content.Intent;
import com.tvtaobao.android.marketgames.dfw.act.GameDaFuWengActivity;
import com.tvtaobao.android.marketgames.dfw.wares.IDataRequest;
import com.tvtaobao.android.marketgames.dfw.wares.IEventListener;
import com.tvtaobao.android.marketgames.dfw.wares.IImageLoader;
import java.util.HashMap;

public class DfwLauncher {
    private static String PARAM_GameLauncher = "DfwLauncher";
    private static HashMap<String, DfwLauncher> gameLauncherHashMap = new HashMap<>();
    public IDataRequest dataRequest;
    public IEventListener eventListener;
    public IImageLoader imageLoader;
    public Context sourceContext;

    private DfwLauncher(Context sourceContext2, IDataRequest dataRequest2, IImageLoader imageLoader2, IEventListener eventListener2) {
        this.sourceContext = sourceContext2;
        this.dataRequest = dataRequest2;
        this.imageLoader = imageLoader2;
        this.eventListener = eventListener2;
    }

    private void launch() {
        String paramsVal = "" + hashCode();
        gameLauncherHashMap.put(paramsVal, this);
        Intent intent = new Intent(this.sourceContext, GameDaFuWengActivity.class);
        intent.putExtra(PARAM_GameLauncher, paramsVal);
        this.sourceContext.startActivity(intent);
    }

    public boolean isCfgOk() {
        if (this.dataRequest == null || this.imageLoader == null || this.eventListener == null) {
            return false;
        }
        return true;
    }

    public static DfwLauncher getFromIntent(Intent intent) {
        String paramVal;
        if (intent == null || (paramVal = intent.getStringExtra(PARAM_GameLauncher)) == null) {
            return null;
        }
        return gameLauncherHashMap.remove(paramVal);
    }

    public static void launchGame(Context sourceContext2, IDataRequest dataRequest2, IImageLoader imgLoader, IEventListener eventListener2) {
        new DfwLauncher(sourceContext2, dataRequest2, imgLoader, eventListener2).launch();
    }
}
