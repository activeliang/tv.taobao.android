package com.bftv.fui.tell;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.util.Pair;
import com.bftv.fui.constantplugin.Constant;
import com.bftv.fui.constantplugin.Switch;
import com.bftv.fui.constantplugin.bean.InterceptorNet;
import com.bftv.fui.tell.ITellMessage;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class TellManager {
    private static final String ADD_SCORE = "add_score";
    private static final String CLEAR_ASR = "clear_asr";
    private static final String DDZ = "ddz";
    private static final String FLAG = "#";
    private static final String NEED_ASR = "need_asr";
    private static final String NOTICE = "notice";
    private static final String PULL = "pull";
    private static final String ROOT = "root";
    private static final String SEND_ASR = "send_asr";
    private static final String TRANSLATE = "translate";
    private static final String TTS = "tts";
    private static final String TYPE_CONTROL = "type_control";
    private static final TellManager ourInstance = new TellManager();
    /* access modifiers changed from: private */
    public ITellMessage mITellMessage;
    /* access modifiers changed from: private */
    public LinkedBlockingQueue<Tell> mLinkedBlockingQueue = new LinkedBlockingQueue<>();
    /* access modifiers changed from: private */
    public Pair<String, List<InterceptorNet>> mListNlpSystemPair;
    /* access modifiers changed from: private */
    public Pair<String, List<String>> mNlpCache;
    /* access modifiers changed from: private */
    public HashMap<String, Object> mQueue = new HashMap<>(5);

    public static TellManager getInstance() {
        return ourInstance;
    }

    private TellManager() {
    }

    public void typeControl(Application context, int type) {
        if (!Switch.getIsUseSdk()) {
            Log.e("Less", "No_Use_SDK_RETURN");
        } else if (this.mITellMessage == null) {
            onBind(context, new TypeControlConnection());
            this.mQueue.put(TYPE_CONTROL, Integer.valueOf(type));
        } else {
            typeControl(type);
        }
    }

    private class TypeControlConnection extends BaseConnection {
        private TypeControlConnection() {
            super();
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            super.onServiceConnected(componentName, iBinder);
            TellManager.this.typeControl(((Integer) TellManager.this.mQueue.get(TellManager.TYPE_CONTROL)).intValue());
        }
    }

    /* access modifiers changed from: private */
    public void typeControl(int type) {
        if (!Switch.getIsUseSdk()) {
            Log.e("Less", "No_Use_SDK_RETURN");
            return;
        }
        try {
            this.mITellMessage.typeControl(type);
        } catch (RemoteException e) {
            e.printStackTrace();
            this.mITellMessage = null;
        }
    }

    public void nlpSystem(Application context, String className, List<InterceptorNet> list) {
        if (!Switch.getIsUseSdk()) {
            Log.e("Less", "No_Use_SDK_RETURN");
        } else if (this.mITellMessage == null) {
            onBind(context, new NlpSystemConnection());
            this.mListNlpSystemPair = new Pair<>(className, list);
        } else {
            nlpSystem(className, list);
        }
    }

    private class NlpSystemConnection extends BaseConnection {
        private NlpSystemConnection() {
            super();
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            super.onServiceConnected(componentName, iBinder);
            if (TellManager.this.mListNlpSystemPair != null) {
                TellManager.this.nlpSystem((String) TellManager.this.mListNlpSystemPair.first, (List) TellManager.this.mListNlpSystemPair.second);
            }
        }
    }

    /* access modifiers changed from: private */
    public void nlpSystem(String className, List<InterceptorNet> list) {
        if (!Switch.getIsUseSdk()) {
            Log.e("Less", "No_Use_SDK_RETURN");
            return;
        }
        try {
            this.mITellMessage.nlpSystem(className, list);
        } catch (RemoteException e) {
            e.printStackTrace();
            this.mITellMessage = null;
        }
        this.mListNlpSystemPair = null;
    }

    public void nlpCache(Application context, String className, List<String> list) {
        if (!Switch.getIsUseSdk()) {
            Log.e("Less", "No_Use_SDK_RETURN");
        } else if (this.mITellMessage == null) {
            onBind(context, new NlpCacheConnection());
            this.mNlpCache = new Pair<>(className, list);
        } else {
            nlpCache(className, list);
        }
    }

    private class NlpCacheConnection extends BaseConnection {
        private NlpCacheConnection() {
            super();
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            super.onServiceConnected(componentName, iBinder);
            if (TellManager.this.mNlpCache != null) {
                TellManager.this.nlpCache((String) TellManager.this.mNlpCache.first, (List) TellManager.this.mNlpCache.second);
            }
        }
    }

    /* access modifiers changed from: private */
    public void nlpCache(String className, List<String> list) {
        if (!Switch.getIsUseSdk()) {
            Log.e("Less", "No_Use_SDK_RETURN");
            return;
        }
        try {
            this.mITellMessage.nlpCache(className, list);
        } catch (RemoteException e) {
            e.printStackTrace();
            this.mITellMessage = null;
        }
        this.mNlpCache = null;
    }

    public void addScore(Application context, String pck, String className, int score) {
        if (!Switch.getIsUseSdk()) {
            Log.e("Less", "No_Use_SDK_RETURN");
        } else if (this.mITellMessage == null) {
            onBind(context, new AddScoreConnection());
            this.mQueue.put(ADD_SCORE, pck + "#" + className + "#" + score);
        } else {
            addScore(pck, className, score);
        }
    }

    private class AddScoreConnection extends BaseConnection {
        private AddScoreConnection() {
            super();
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            super.onServiceConnected(componentName, iBinder);
            String[] data = ((String) TellManager.this.mQueue.get(TellManager.ADD_SCORE)).split("#");
            TellManager.this.addScore(data[0], data[1], Integer.parseInt(data[2]));
        }
    }

    /* access modifiers changed from: private */
    public void addScore(String pck, String className, int score) {
        if (!Switch.getIsUseSdk()) {
            Log.e("Less", "No_Use_SDK_RETURN");
            return;
        }
        try {
            this.mITellMessage.addScore(pck, className, score);
        } catch (RemoteException e) {
            e.printStackTrace();
            this.mITellMessage = null;
        }
    }

    public void closeVoice(Application context) {
        if (!Switch.getIsUseSdk()) {
            Log.e("Less", "No_Use_SDK_RETURN");
        } else if (this.mITellMessage == null) {
            onBind(context, new CloseVoiceConnection());
        } else {
            closeVoice();
        }
    }

    private class CloseVoiceConnection extends BaseConnection {
        private CloseVoiceConnection() {
            super();
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            super.onServiceConnected(componentName, iBinder);
            TellManager.this.closeVoice();
        }
    }

    /* access modifiers changed from: private */
    public void closeVoice() {
        try {
            this.mITellMessage.closeVoice();
        } catch (RemoteException e) {
            e.printStackTrace();
            this.mITellMessage = null;
        }
    }

    public void tell(Application context, Tell tell) {
        if (!Switch.getIsUseSdk()) {
            Log.e("Less", "No_Use_SDK_RETURN");
        } else if (this.mITellMessage == null) {
            onBind(context, new SendConnection());
            try {
                this.mLinkedBlockingQueue.put(tell);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e("Less", "this bug very serious you run <<<<tell(Application context, Tell tell)>>>> error:" + e.getMessage());
            }
        } else {
            new TellTask().execute(new Tell[]{tell});
        }
    }

    private class SendConnection extends BaseConnection {
        private SendConnection() {
            super();
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            super.onServiceConnected(componentName, iBinder);
            TellManager.this.tell((Tell) TellManager.this.mLinkedBlockingQueue.poll());
        }
    }

    /* access modifiers changed from: private */
    public void tell(Tell tell) {
        if (!Switch.getIsUseSdk()) {
            Log.e("Less", "No_Use_SDK_RETURN");
        } else if (tell == null) {
            Log.e("Less", "ao!!! no other app hope tell but Tell is NULL");
        } else {
            try {
                this.mITellMessage.tell(tell);
            } catch (RemoteException e) {
                e.printStackTrace();
                this.mITellMessage = null;
            }
        }
    }

    public void tts(Application context, TTS tts) {
        if (!Switch.getIsUseSdk()) {
            Log.e("Less", "No_Use_SDK_RETURN");
        } else if (this.mITellMessage == null) {
            onBind(context, new TTSConnection());
            this.mQueue.put(TTS, tts);
        } else {
            tts(tts);
        }
    }

    private class TTSConnection extends BaseConnection {
        private TTSConnection() {
            super();
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            super.onServiceConnected(componentName, iBinder);
            TellManager.this.tts((TTS) TellManager.this.mQueue.get(TellManager.TTS));
        }
    }

    /* access modifiers changed from: private */
    public void tts(TTS tts) {
        try {
            this.mITellMessage.ttsControl(tts);
        } catch (RemoteException e) {
            e.printStackTrace();
            this.mITellMessage = null;
        }
    }

    public void onShow(Application context) {
        if (!Switch.getIsUseSdk()) {
            Log.e("Less", "No_Use_SDK_RETURN");
        } else if (this.mITellMessage == null) {
            onBind(context, new OnShowConnection());
        } else {
            onShow();
        }
    }

    private class OnShowConnection extends BaseConnection {
        private OnShowConnection() {
            super();
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            super.onServiceConnected(componentName, iBinder);
            TellManager.this.onShow();
        }
    }

    /* access modifiers changed from: private */
    public void onShow() {
        try {
            this.mITellMessage.onShow();
        } catch (RemoteException e) {
            e.printStackTrace();
            this.mITellMessage = null;
        }
    }

    public void ddzGameTransform(Application context, String json) {
        if (!Switch.getIsUseSdk()) {
            Log.e("Less", "No_Use_SDK_RETURN");
        } else if (this.mITellMessage == null) {
            onBind(context, new DDZGameConnection());
            this.mQueue.put(DDZ, json);
        } else {
            ddzGame(json);
        }
    }

    private class DDZGameConnection extends BaseConnection {
        private DDZGameConnection() {
            super();
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            super.onServiceConnected(componentName, iBinder);
            TellManager.this.ddzGame((String) TellManager.this.mQueue.get(TellManager.DDZ));
        }
    }

    /* access modifiers changed from: private */
    public void ddzGame(String json) {
        try {
            this.mITellMessage.ddzGameTransform(json);
        } catch (RemoteException e) {
            e.printStackTrace();
            this.mITellMessage = null;
        }
    }

    public void sendNotice(Application context, Notice notice) {
        if (!Switch.getIsUseSdk()) {
            Log.e("Less", "No_Use_SDK_RETURN");
        } else if (this.mITellMessage == null) {
            onBind(context, new NoticeConnection());
            this.mQueue.put(NOTICE, notice);
        } else {
            sendNotice(notice);
        }
    }

    private class NoticeConnection extends BaseConnection {
        private NoticeConnection() {
            super();
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            super.onServiceConnected(componentName, iBinder);
            TellManager.this.sendNotice((Notice) TellManager.this.mQueue.get(TellManager.NOTICE));
        }
    }

    /* access modifiers changed from: private */
    public void sendNotice(Notice notice) {
        try {
            this.mITellMessage.sendNotice(notice);
        } catch (RemoteException e) {
            e.printStackTrace();
            this.mITellMessage = null;
        }
    }

    public void isNeedTranslate(Application context, String pck, String className, boolean isNeedPinYin) {
        if (!Switch.getIsUseSdk()) {
            Log.e("Less", "No_Use_SDK_RETURN");
        } else if (this.mITellMessage == null) {
            onBind(context, new NeedTranslateConnection());
            this.mQueue.put(TRANSLATE, pck + "#" + className + "#" + isNeedPinYin);
        } else {
            isNeedTranslate(pck, className, isNeedPinYin);
        }
    }

    private class NeedTranslateConnection extends BaseConnection {
        private NeedTranslateConnection() {
            super();
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            super.onServiceConnected(componentName, iBinder);
            String[] data = ((String) TellManager.this.mQueue.get(TellManager.TRANSLATE)).split("#");
            TellManager.this.isNeedTranslate(data[1], data[2], Boolean.parseBoolean(data[3]));
        }
    }

    /* access modifiers changed from: private */
    public void isNeedTranslate(String pck, String className, boolean isNeedPinYin) {
        try {
            this.mITellMessage.isNeedAsr(pck, className, isNeedPinYin);
        } catch (RemoteException e) {
            e.printStackTrace();
            this.mITellMessage = null;
        }
    }

    public void farPull(Application context, String pck) {
        if (!Switch.getIsUseSdk()) {
            Log.e("Less", "No_Use_SDK_RETURN");
        } else if (this.mITellMessage == null) {
            onBind(context, new FarPullConnection());
            this.mQueue.put(PULL, pck);
        } else {
            farPull(pck);
        }
    }

    private class FarPullConnection extends BaseConnection {
        private FarPullConnection() {
            super();
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            super.onServiceConnected(componentName, iBinder);
            TellManager.this.farPull((String) TellManager.this.mQueue.get(TellManager.PULL));
        }
    }

    /* access modifiers changed from: private */
    public void farPull(String pck) {
        try {
            this.mITellMessage.farPull(pck);
        } catch (RemoteException e) {
            e.printStackTrace();
            this.mITellMessage = null;
        }
    }

    public void needAsr(Application context, String pck) {
        if (!Switch.getIsUseSdk()) {
            Log.e("Less", "No_Use_SDK_RETURN");
        } else if (this.mITellMessage == null) {
            onBind(context, new NeedAsrConnection());
            this.mQueue.put(NEED_ASR, pck);
        } else {
            needAsr(pck);
        }
    }

    private class NeedAsrConnection extends BaseConnection {
        private NeedAsrConnection() {
            super();
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            super.onServiceConnected(componentName, iBinder);
            TellManager.this.needAsr((String) TellManager.this.mQueue.get(TellManager.NEED_ASR));
        }
    }

    /* access modifiers changed from: private */
    public void needAsr(String pck) {
        try {
            this.mITellMessage.needAsr(pck);
        } catch (RemoteException e) {
            e.printStackTrace();
            this.mITellMessage = null;
        }
    }

    public void clearAsr(Application context, String pck, String className) {
        if (!Switch.getIsUseSdk()) {
            Log.e("Less", "No_Use_SDK_RETURN");
        } else if (this.mITellMessage == null) {
            onBind(context, new ClearAsrConnection());
            this.mQueue.put(CLEAR_ASR, pck + "#" + className);
        } else {
            clearAsr(pck, className);
        }
    }

    private class ClearAsrConnection extends BaseConnection {
        private ClearAsrConnection() {
            super();
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            super.onServiceConnected(componentName, iBinder);
            String[] asrData = ((String) TellManager.this.mQueue.get(TellManager.CLEAR_ASR)).split("#");
            TellManager.this.clearAsr(asrData[0], asrData[1]);
        }
    }

    /* access modifiers changed from: private */
    public void clearAsr(String pck, String className) {
        try {
            this.mITellMessage.clearAsr(pck, className);
        } catch (RemoteException e) {
            e.printStackTrace();
            this.mITellMessage = null;
        }
    }

    public void sendAsr(Application context, String pck, String asr) {
        if (!Switch.getIsUseSdk()) {
            Log.e("Less", "No_Use_SDK_RETURN");
        } else if (this.mITellMessage == null) {
            onBind(context, new SendAsrConnection());
            this.mQueue.put(SEND_ASR, pck + "#" + asr);
        } else {
            sendAsr(pck, asr);
        }
    }

    private class SendAsrConnection extends BaseConnection {
        private SendAsrConnection() {
            super();
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            super.onServiceConnected(componentName, iBinder);
            String[] asrData = ((String) TellManager.this.mQueue.get(TellManager.SEND_ASR)).split("#");
            TellManager.this.sendAsr(asrData[0], asrData[1]);
        }
    }

    /* access modifiers changed from: private */
    public void sendAsr(String pck, String asr) {
        try {
            this.mITellMessage.sendAsr(pck, asr);
        } catch (RemoteException e) {
            e.printStackTrace();
            this.mITellMessage = null;
        }
    }

    public void enableRoot(Application context, String className, String key) {
        if (!Switch.getIsUseSdk()) {
            Log.e("Less", "No_Use_SDK_RETURN");
            return;
        }
        String pckName = context.getPackageName();
        Log.e("Less", "enableRoot:" + pckName);
        if (this.mITellMessage == null) {
            onBind(context, new EnableRootConnection());
            this.mQueue.put("root", pckName + "#" + className + "#" + key);
            return;
        }
        enableRoot(pckName, className, key);
    }

    private class EnableRootConnection extends BaseConnection {
        private EnableRootConnection() {
            super();
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            super.onServiceConnected(componentName, iBinder);
            String translate = (String) TellManager.this.mQueue.get("root");
            String[] data = translate.split("#");
            Log.e("Less", "EnableRootConnection－data" + translate);
            TellManager.this.enableRoot(data[0], data[1], data[2]);
        }
    }

    /* access modifiers changed from: private */
    public void enableRoot(String pck, String className, String key) {
        Log.e("Less", "enableRoot-pck:" + pck + "|className:" + className + "|key:" + key);
        try {
            this.mITellMessage.enableRoot(pck, className, key);
        } catch (Throwable e) {
            e.printStackTrace();
            this.mITellMessage = null;
            Log.e("Less", "enableRoot到底那里出问题了?:" + e.getMessage());
        }
    }

    public void clearRootAuthority(Application context) {
        if (!Switch.getIsUseSdk()) {
            Log.e("Less", "No_Use_SDK_RETURN");
        } else if (this.mITellMessage == null) {
            onBind(context, new ClearRootAuthorityConnection());
        } else {
            clearRootAuthority();
        }
    }

    private void onBind(Application application, BaseConnection baseConnection) {
        try {
            Intent intent = new Intent("intent.action.bftv.voice");
            intent.setPackage(Constant.VOICE_PACKAGE);
            application.bindService(intent, baseConnection, 1);
        } catch (Throwable throwable) {
            Log.e("Less", "TellManager-onBind:" + throwable.getMessage());
        }
    }

    private class ClearRootAuthorityConnection extends BaseConnection {
        private ClearRootAuthorityConnection() {
            super();
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            super.onServiceConnected(componentName, iBinder);
            TellManager.this.clearRootAuthority();
        }
    }

    private class BaseConnection implements ServiceConnection {
        private BaseConnection() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ITellMessage unused = TellManager.this.mITellMessage = ITellMessage.Stub.asInterface(iBinder);
        }

        public void onServiceDisconnected(ComponentName componentName) {
            ITellMessage unused = TellManager.this.mITellMessage = null;
        }
    }

    private class TellTask extends AsyncTask<Tell, Void, Void> {
        private TellTask() {
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Tell... tells) {
            for (Tell tell : tells) {
                TellManager.this.tell(tell);
            }
            return null;
        }
    }

    /* access modifiers changed from: private */
    public void clearRootAuthority() {
        try {
            this.mITellMessage.clearRootAuthority();
        } catch (RemoteException e) {
            e.printStackTrace();
            this.mITellMessage = null;
        }
    }
}
