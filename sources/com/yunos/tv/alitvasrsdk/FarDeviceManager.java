package com.yunos.tv.alitvasrsdk;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import org.json.JSONException;
import org.json.JSONObject;

public class FarDeviceManager {
    private static final String COMMAND = "command";
    private static final String COMMAND_START = "cmd_start";
    private static final String COMMAND_STOP = "cmd_stop";
    private static final String COMMAND_STREAM = "cmd_stream";
    private static final String COMMAND_VOLUME = "cmd_volume";
    protected static final String KEY_FINISHED = "finish";
    protected static final String KEY_QUESTION = "question";
    protected static final String KEY_RET = "ret";
    protected static final String KEY_VOLUME = "volume";
    private static final String PARAM = "param";
    private static final String TYPE = "type";
    private static final String TYPE_ASR = "asr";
    private static FarDeviceManager gInstance;
    private AliTVASRManager mService;

    public static FarDeviceManager getInstance() {
        return gInstance;
    }

    public static FarDeviceManager getInstance(Context context) {
        return getInstance(context, (AliTVASRManager) null);
    }

    public static FarDeviceManager getInstance(Context context, AliTVASRManager service) {
        if (gInstance == null) {
            synchronized (FarDeviceManager.class) {
                if (gInstance == null) {
                    gInstance = new FarDeviceManager(context, service);
                }
            }
        }
        return gInstance;
    }

    public FarDeviceManager(Context context, AliTVASRManager service) {
        if (service == null) {
            service = new AliTVASRManager();
            try {
                service.setAliTVASREnable(false);
                service.setNoRegister(true);
                service.init(context.getApplicationContext(), false);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        this.mService = service;
    }

    private Bundle createBundle(String type, String command, String param) {
        Bundle data = new Bundle();
        data.putString("type", type);
        data.putString("command", command);
        data.putString("param", param);
        return data;
    }

    public void start() {
        this.mService.onFarDeviceData(createBundle("asr", "cmd_start", (String) null));
    }

    public void stop() {
        this.mService.onFarDeviceData(createBundle("asr", "cmd_stop", (String) null));
    }

    public void onStream(String question, boolean isFinished) {
        try {
            JSONObject param = new JSONObject();
            param.put("question", question);
            param.put("finish", isFinished);
            this.mService.onFarDeviceData(createBundle("asr", "cmd_stream", param.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onVolume(int volume) {
        try {
            JSONObject param = new JSONObject();
            param.put("volume", volume);
            this.mService.onFarDeviceData(createBundle("asr", "cmd_volume", param.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
