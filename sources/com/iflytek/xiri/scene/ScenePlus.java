package com.iflytek.xiri.scene;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import org.json.JSONObject;

public class ScenePlus {
    public static final String ADDSCENECOMMAND_ACTION = "com.iflytek.xiri2.allActivity.QUERY";
    public static final String COMMIT_ACTION = "com.iflytek.xiri2.topActivity.COMMIT";
    public static final String FUZZY_SCENE_SERVICE_ACTION = "com.iflytek.xiri2.scenes.EXECUTE";
    public static final String NEW_COMMIT_ACTION = "tv.yuyin.topActivity.COMMIT";
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public ISceneListener mISceneListenner;
    IntentFilter mIntentFilter;
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String pkgname;
            if (ScenePlus.ADDSCENECOMMAND_ACTION.equals(intent.getAction())) {
                Log.d("XiriScene", "ADDSCENECOMMAND_ACTION onReceive " + Uri.decode(intent.toURI()));
                ActivityManager activityManager = (ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY);
                if (activityManager.getRunningTasks(1) == null || activityManager.getRunningTasks(1).size() <= 0 || (activityManager.getRunningTasks(1).get(0).topActivity.getPackageName() != null && activityManager.getRunningTasks(1).get(0).topActivity.getPackageName().equals(context.getPackageName()))) {
                    if (intent.getStringExtra("pkgname") == null) {
                        pkgname = "com.iflytek.xiri";
                    } else {
                        pkgname = intent.getStringExtra("pkgname");
                    }
                    Log.d("XiriScene", "TAG:" + ScenePlus.this.mTag);
                    if ("com.iflytek.xiri".equals(pkgname)) {
                        intent.setAction("com.iflytek.xiri2.topActivity.COMMIT");
                        intent.putExtra("_scene", ScenePlus.this.mISceneListenner.onQuery());
                        intent.putExtra("_package", ScenePlus.this.mContext.getPackageName());
                        intent.putExtra("_objhash", ScenePlus.this.token);
                        intent.setPackage(pkgname);
                        ScenePlus.this.mContext.startService(intent);
                    } else {
                        intent.setAction("tv.yuyin.topActivity.COMMIT");
                        intent.putExtra("_scene", ScenePlus.this.mISceneListenner.onQuery());
                        intent.putExtra("_package", ScenePlus.this.mContext.getPackageName());
                        intent.putExtra("_objhash", ScenePlus.this.token);
                        intent.putExtra("action", "com.iflytek.xiri2.scenes.EXECUTE" + ScenePlus.this.mTag);
                        intent.setPackage(pkgname);
                        ScenePlus.this.mContext.startService(intent);
                    }
                    Log.d("XiriScene", "ADDSCENECOMMAND_ACTION startService " + Uri.decode(intent.toURI()));
                }
            } else if (("com.iflytek.xiri2.scenes.EXECUTE" + ScenePlus.this.mTag).equals(intent.getAction())) {
                Log.d("XiriScene", "FUZZY_SCENE_SERVICE_ACTION " + Uri.decode(intent.toURI()));
                Log.d("SCENE_TIME", "StartTime " + System.currentTimeMillis());
                Log.d("XiriScene", "mContext getPackagename " + ScenePlus.this.mContext.getPackageName());
                if (intent.hasExtra("_objhash") && intent.getStringExtra("_objhash").equals(ScenePlus.this.token + "")) {
                    if (intent.hasExtra("_scene")) {
                        String sceneId = intent.getStringExtra("_scene");
                        Log.d("SCENE_TIME", "fromIntent sceneId " + sceneId);
                        try {
                            String userSceneId = new JSONObject(ScenePlus.this.mISceneListenner.onQuery()).getString("_scene");
                            Log.d("SCENE_TIME", "userSceneId  " + sceneId);
                            if (userSceneId.equals(sceneId)) {
                                ScenePlus.this.mISceneListenner.onExecute(intent);
                                Log.d("SCENE_TIME", "EndTime " + System.currentTimeMillis());
                                Log.d("XiriScene", "FUZZY_SCENE_SERVICE_ACTION exe " + Uri.decode(intent.toURI()));
                            }
                        } catch (Exception e) {
                        }
                    } else {
                        ScenePlus.this.mISceneListenner.onExecute(intent);
                        Log.d("XiriScene", "else Scene exe " + Uri.decode(intent.toURI()));
                    }
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public String mTag = "";
    /* access modifiers changed from: private */
    public int token;

    public ScenePlus(Context context) {
        this.mContext = context;
        this.token = hashCode();
        this.mIntentFilter = new IntentFilter();
        this.mTag = System.currentTimeMillis() + "";
        this.mIntentFilter.addAction("com.iflytek.xiri2.scenes.EXECUTE" + this.mTag);
        this.mIntentFilter.addAction(ADDSCENECOMMAND_ACTION);
    }

    public void init(ISceneListener listenner) {
        this.mContext.registerReceiver(this.mReceiver, this.mIntentFilter);
        this.mISceneListenner = listenner;
    }

    public void release() {
        this.mContext.unregisterReceiver(this.mReceiver);
    }
}
