package com.iflytek.xiri.scene;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;
import org.json.JSONObject;

public class Scene {
    public static final String ADDSCENECOMMAND_ACTION = "com.iflytek.xiri2.topActivity.QUERY";
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
            if (Scene.ADDSCENECOMMAND_ACTION.equals(intent.getAction())) {
                Log.d("XiriScene", "ADDSCENECOMMAND_ACTION onReceive " + Uri.decode(intent.toURI()));
                String pkgname = intent.getStringExtra("pkgname") == null ? "com.iflytek.xiri" : intent.getStringExtra("pkgname");
                if ("com.iflytek.xiri".equals(pkgname)) {
                    intent.setAction("com.iflytek.xiri2.topActivity.COMMIT");
                    intent.putExtra("_scene", Scene.this.mISceneListenner.onQuery());
                    intent.putExtra("_package", Scene.this.mContext.getPackageName());
                    intent.putExtra("_objhash", Scene.this.token);
                    intent.setPackage(pkgname);
                    Scene.this.mContext.startService(intent);
                } else {
                    intent.setAction("tv.yuyin.topActivity.COMMIT");
                    intent.putExtra("_scene", Scene.this.mISceneListenner.onQuery());
                    intent.putExtra("_package", Scene.this.mContext.getPackageName());
                    intent.putExtra("_objhash", Scene.this.token);
                    intent.setPackage(pkgname);
                    Scene.this.mContext.startService(intent);
                }
                Log.d("XiriScene", "ADDSCENECOMMAND_ACTION startService " + Uri.decode(intent.toURI()));
            } else if ("com.iflytek.xiri2.scenes.EXECUTE".equals(intent.getAction())) {
                Log.d("XiriScene", "FUZZY_SCENE_SERVICE_ACTION " + Uri.decode(intent.toURI()));
                Log.d("SCENE_TIME", "StartTime " + System.currentTimeMillis());
                Log.d("XiriScene", "mContext getPackagename " + Scene.this.mContext.getPackageName());
                if (intent.hasExtra("_objhash") && intent.getStringExtra("_objhash").equals(Scene.this.token + "")) {
                    if (intent.hasExtra("_scene")) {
                        String sceneId = intent.getStringExtra("_scene");
                        Log.d("SCENE_TIME", "fromIntent sceneId " + sceneId);
                        try {
                            String userSceneId = new JSONObject(Scene.this.mISceneListenner.onQuery()).getString("_scene");
                            Log.d("SCENE_TIME", "userSceneId  " + sceneId);
                            if (userSceneId.equals(sceneId)) {
                                Scene.this.mISceneListenner.onExecute(intent);
                                Log.d("SCENE_TIME", "EndTime " + System.currentTimeMillis());
                                Log.d("XiriScene", "FUZZY_SCENE_SERVICE_ACTION exe " + Uri.decode(intent.toURI()));
                            }
                        } catch (Exception e) {
                        }
                    } else {
                        Scene.this.mISceneListenner.onExecute(intent);
                        Log.d("XiriScene", "else Scene exe " + Uri.decode(intent.toURI()));
                    }
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public int token;

    public Scene(Context context) {
        this.mContext = context;
        this.token = hashCode();
        this.mIntentFilter = new IntentFilter();
        this.mIntentFilter.addAction("com.iflytek.xiri2.scenes.EXECUTE");
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
