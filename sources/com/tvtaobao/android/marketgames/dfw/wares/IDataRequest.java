package com.tvtaobao.android.marketgames.dfw.wares;

public interface IDataRequest {

    public interface IDRCallBack<T> {
        void onFailed(String str);

        void onSuccess(T t);
    }

    void reqGameSceneCfg(IDRCallBack<IBoGameData> iDRCallBack);

    void reqRock(IDRCallBack<IBoDiceResult> iDRCallBack);
}
