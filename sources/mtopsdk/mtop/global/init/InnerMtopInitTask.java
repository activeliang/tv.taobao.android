package mtopsdk.mtop.global.init;

import android.os.Process;
import mtopsdk.common.log.LogAdapter;
import mtopsdk.common.log.TLogAdapterImpl;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.config.AppConfigManager;
import mtopsdk.framework.manager.impl.InnerFilterManagerImpl;
import mtopsdk.mtop.antiattack.AntiAttackHandlerImpl;
import mtopsdk.mtop.deviceid.DeviceIDManager;
import mtopsdk.mtop.domain.EntranceEnum;
import mtopsdk.mtop.features.MtopFeatureManager;
import mtopsdk.mtop.global.MtopConfig;
import mtopsdk.mtop.global.SwitchConfig;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.network.NetworkPropertyServiceImpl;
import mtopsdk.mtop.stat.UploadStatAppMonitorImpl;
import mtopsdk.network.impl.ANetworkCallFactory;
import mtopsdk.security.ISign;
import mtopsdk.security.InnerSignImpl;
import mtopsdk.xstate.XState;

public class InnerMtopInitTask implements IMtopInitTask {
    private static final String TAG = "mtopsdk.InnerMtopInitTask";

    public void executeCoreTask(MtopConfig mtopConfig) {
        LogAdapter tLogAdapterImpl;
        if (MtopConfig.logAdapterImpl != null) {
            tLogAdapterImpl = MtopConfig.logAdapterImpl;
        } else {
            tLogAdapterImpl = new TLogAdapterImpl();
        }
        TBSdkLog.setLogAdapter(tLogAdapterImpl);
        String instanceId = mtopConfig.instanceId;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, instanceId + " [executeInitCoreTask]MtopSDK initcore start. ");
        }
        try {
            Mtop mtopInstance = mtopConfig.mtopInstance;
            MtopFeatureManager.setMtopFeatureFlag(mtopInstance, 1, true);
            MtopFeatureManager.setMtopFeatureFlag(mtopInstance, 2, true);
            MtopFeatureManager.setMtopFeatureFlag(mtopInstance, 4, true);
            MtopFeatureManager.setMtopFeatureFlag(mtopInstance, 5, true);
            if (mtopConfig.uploadStats == null) {
                mtopConfig.uploadStats = new UploadStatAppMonitorImpl();
            }
            mtopConfig.networkPropertyService = new NetworkPropertyServiceImpl();
            XState.init(mtopConfig.context);
            XState.setValue(instanceId, "ttid", mtopConfig.ttid);
            mtopConfig.networkPropertyService.setTtid(mtopConfig.ttid);
            ISign sign = mtopConfig.sign;
            if (sign == null) {
                sign = new InnerSignImpl();
            }
            sign.init(mtopConfig);
            mtopConfig.entrance = EntranceEnum.GW_INNER;
            mtopConfig.sign = sign;
            mtopConfig.appKey = sign.getAppKey(new ISign.SignCtx(mtopConfig.appKeyIndex, mtopConfig.authCode));
            mtopConfig.processId = Process.myPid();
            mtopConfig.filterManager = new InnerFilterManagerImpl();
            if (mtopConfig.antiAttackHandler == null) {
                mtopConfig.antiAttackHandler = new AntiAttackHandlerImpl(mtopConfig.context);
            }
            if (mtopConfig.callFactory == null) {
                mtopConfig.callFactory = new ANetworkCallFactory(mtopConfig.context);
            }
        } catch (Throwable e) {
            TBSdkLog.e(TAG, instanceId + " [executeInitCoreTask]MtopSDK initCore error", e);
        }
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, instanceId + " [executeInitCoreTask]MtopSDK initCore end");
        }
    }

    public void executeExtraTask(MtopConfig mtopConfig) {
        String instanceId = mtopConfig.instanceId;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, instanceId + " [executeInitExtraTask]MtopSDK initExtra start");
        }
        try {
            if (mtopConfig.enableNewDeviceId) {
                DeviceIDManager.getInstance().getDeviceID(mtopConfig.context, mtopConfig.appKey);
            }
            SwitchConfig.getInstance().initConfig(mtopConfig.context);
            AppConfigManager.getInstance().reloadAppConfig(mtopConfig);
        } catch (Throwable e) {
            TBSdkLog.e(TAG, instanceId + " [executeInitExtraTask] execute MtopSDK initExtraTask error.", e);
        }
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, instanceId + " [executeInitExtraTask]MtopSDK initExtra end");
        }
    }
}
