package com.alibaba.analytics.core.config;

import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.config.timestamp.ConfigTimeStampMgr;
import com.alibaba.analytics.core.db.Entity;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.StringUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class UTBaseConfMgr {
    public static String TAG_CONFIG_TIMESTAMP = "timestamp";
    private String DefaultWhiteConfigs1 = "{\"B02N_utap_system\":{\"content\":{\"fu\":30,\"sw_plugin\":0,\"bu\":300}},\"B02N_ut_sample\":{\"content\":{\"1\":{\"cp\":10000},\"1000\":{\"cp\":10000},\"2000\":{\"cp\":10000},\"2100\":{\"cp\":10000},\"3002\":{\"cp\":10000},\"3003\":{\"cp\":10000},\"3004\":{\"cp\":10000},\"4007\":{\"cp\":10000},\"5002\":{\"cp\":10000},\"5004\":{\"cp\":10000},\"5005\":{\"cp\":10000},\"6004\":{\"cp\":10000},\"9001\":{\"cp\":10000},\"9002\":{\"cp\":10000},\"9003\":{\"cp\":10000},\"9101\":{\"cp\":10000},\"9199\":{\"cp\":10000},\"15301\":{\"cp\":10000},\"15302\":{\"cp\":1},\"15303\":{\"cp\":10000},\"15304\":{\"cp\":10000},\"15305\":{\"cp\":10000},\"15306\":{\"cp\":100},\"15307\":{\"cp\":10000},\"15391\":{\"cp\":100},\"15392\":{\"cp\":10000},\"15393\":{\"cp\":1200},\"15394\":{\"cp\":10000},\"19999\":{\"cp\":10000},\"21032\":{\"cp\":10000},\"21034\":{\"cp\":1},\"21064\":{\"cp\":10000},\"22064\":{\"cp\":1},\"61001\":{\"cp\":10000},\"61006\":{\"cp\":10000},\"61007\":{\"cp\":10000},\"65001\":{\"cp\":1},\"65100\":{\"cp\":1},\"65101\":{\"cp\":4},\"65104\":{\"cp\":10000},\"65105\":{\"cp\":10000},\"65111\":{\"cp\":100},\"65113\":{\"cp\":10000},\"65114\":{\"cp\":10000},\"65125\":{\"cp\":10000},\"65132\":{\"cp\":10000},\"65171\":{\"cp\":100},\"65172\":{\"cp\":100},\"65173\":{\"cp\":100},\"65174\":{\"cp\":100},\"65175\":{\"cp\":100},\"65176\":{\"cp\":100},\"65177\":{\"cp\":100},\"65178\":{\"cp\":100},\"65180\":{\"cp\":900},\"65183\":{\"cp\":10000},\"65200\":{\"cp\":10000},\"65501\":{\"cp\":10000},\"65502\":{\"cp\":10000},\"65503\":{\"cp\":10000},\"66001\":{\"cp\":100},\"66003\":{\"cp\":10000},\"66101\":{\"cp\":10000},\"66404\":{\"cp\":10000},\"66602\":{\"cp\":10000}}},\"B02N_ut_stream\":{\"content\":{\"1\":{\"stm\":\"stm_x\"},\"1000\":{\"stm\":\"stm_p\"},\"2000\":{\"stm\":\"stm_p\"},\"2100\":{\"stm\":\"stm_c\"},\"4007\":{\"stm\":\"stm_d\"},\"5002\":{\"stm\":\"stm_d\"},\"5004\":{\"stm\":\"stm_d\"},\"5005\":{\"stm\":\"stm_d\"},\"6004\":{\"stm\":\"stm_d\"},\"9001\":{\"stm\":\"stm_d\"},\"9002\":{\"stm\":\"stm_d\"},\"9003\":{\"stm\":\"stm_d\"},\"9101\":{\"stm\":\"stm_d\"},\"9199\":{\"stm\":\"stm_d\"},\"15301\":{\"stm\":\"stm_x\"},\"15302\":{\"stm\":\"stm_x\"},\"15303\":{\"stm\":\"stm_x\"},\"15304\":{\"stm\":\"stm_x\"},\"15305\":{\"stm\":\"stm_x\"},\"15306\":{\"stm\":\"stm_x\"},\"15307\":{\"stm\":\"stm_x\"},\"15391\":{\"stm\":\"stm_x\"},\"15392\":{\"stm\":\"stm_x\"},\"15393\":{\"stm\":\"stm_x\"},\"15394\":{\"stm\":\"stm_x\"},\"19999\":{\"stm\":\"stm_d\"},\"21032\":{\"stm\":\"stm_x\"},\"21034\":{\"stm\":\"stm_x\"},\"21064\":{\"stm\":\"stm_x\"},\"22064\":{\"stm\":\"stm_x\"},\"61001\":{\"stm\":\"stm_x\"},\"61006\":{\"stm\":\"stm_x\"},\"61007\":{\"stm\":\"stm_x\"},\"65001\":{\"stm\":\"stm_x\"},\"65100\":{\"stm\":\"stm_x\"},\"65101\":{\"stm\":\"stm_x\"},\"65104\":{\"stm\":\"stm_x\"},\"65105\":{\"stm\":\"stm_x\"},\"65111\":{\"stm\":\"stm_x\"},\"65113\":{\"stm\":\"stm_x\"},\"65114\":{\"stm\":\"stm_x\"},\"65125\":{\"stm\":\"stm_x\"},\"65132\":{\"stm\":\"stm_x\"},\"65171\":{\"stm\":\"stm_x\"},\"65172\":{\"stm\":\"stm_x\"},\"65173\":{\"stm\":\"stm_x\"},\"65174\":{\"stm\":\"stm_x\"},\"65175\":{\"stm\":\"stm_x\"},\"65176\":{\"stm\":\"stm_x\"},\"65177\":{\"stm\":\"stm_x\"},\"65178\":{\"stm\":\"stm_x\"},\"65180\":{\"stm\":\"stm_x\"},\"65183\":{\"stm\":\"stm_x\"},\"65200\":{\"stm\":\"stm_d\"},\"65501\":{\"stm\":\"stm_d\"},\"65502\":{\"stm\":\"stm_d\"},\"65503\":{\"stm\":\"stm_d\"},\"66001\":{\"stm\":\"stm_d\"},\"66003\":{\"stm\":\"stm_d\"},\"66101\":{\"stm\":\"stm_d\"},\"66404\":{\"stm\":\"stm_d\"}}},\"B02N_ut_bussiness\":{\"content\":{\"tpk\":[{\"kn\":\"adid\",\"ty\":\"nearby\"},{\"kn\":\"ucm\",\"ty\":\"nearby\"},{\"kn\":\"bdid\",\"ty\":\"far\"},{\"kn\":\"ref_pid\",\"ty\":\"far\"},{\"kn\":\"pid\",\"ty\":\"far\"},{\"kn\":\"tpa\",\"ty\":\"far\"},{\"kn\":\"point\",\"ty\":\"far\"},{\"kn\":\"ali_trackid\",\"ty\":\"far\"},{\"kn\":\"xncode\",\"ty\":\"nearby\"}]}}}";
    private String DefaultWhiteConfigs2 = "{\"B02N_utap_system\":{\"content\":{\"fu\":30,\"sw_plugin\":0,\"bu\":300,\"delay\":{\"2101\":{\"all_d\":1,\"arg1\":[\"%Page_Home_Button-Huichang%\",\"%Page_Home_Button-renqunhuichang%\",\"JuJRT_JRT_LIST_LOAD\",\"JuJRT_JRT_LIST\"]},\"2102\":{\"all_d\":1},\"19999\":{\"all_d\":0,\"arg1\":[\"Show1212Box_aop\",\"Page_Home_Show-LeiMuHuiChang\",\"Page_Home_Show-RenQunHuiChang\"]}}}},\"B02N_ap_counter\":{\"content\":{\"event_type\":{\"cp\":1000}}},\"B02N_ap_alarm\":{\"content\":{\"event_type\":{\"cp\":1000}}},\"B02N_ap_stat\":{\"content\":{\"event_type\":{\"cp\":1000}}},\"B02N_ut_sample\":{\"content\":{\"1\":{\"cp\":10000},\"1000\":{\"cp\":10000},\"2000\":{\"cp\":10000},\"2100\":{\"cp\":10000},\"4007\":{\"cp\":10000},\"5002\":{\"cp\":10000},\"5004\":{\"cp\":10000},\"5005\":{\"cp\":10000},\"6004\":{\"cp\":10000},\"9001\":{\"cp\":10000},\"9002\":{\"cp\":10000},\"9003\":{\"cp\":10000},\"9101\":{\"cp\":10000},\"9199\":{\"cp\":10000},\"15301\":{\"cp\":10000},\"15302\":{\"cp\":1},\"15303\":{\"cp\":10000},\"15304\":{\"cp\":10000},\"15305\":{\"cp\":10000},\"15306\":{\"cp\":100},\"15307\":{\"cp\":10000},\"15391\":{\"cp\":100},\"15392\":{\"cp\":10000},\"15393\":{\"cp\":1200},\"15394\":{\"cp\":10000},\"19999\":{\"cp\":10000},\"21032\":{\"cp\":10000},\"21034\":{\"cp\":1},\"21064\":{\"cp\":10000},\"22064\":{\"cp\":1},\"61001\":{\"cp\":10000},\"61006\":{\"cp\":10000},\"61007\":{\"cp\":10000},\"65001\":{\"cp\":1},\"65100\":{\"cp\":1},\"65101\":{\"cp\":4},\"65104\":{\"cp\":10000},\"65105\":{\"cp\":10000},\"65111\":{\"cp\":100},\"65113\":{\"cp\":10000},\"65114\":{\"cp\":10000},\"65125\":{\"cp\":10000},\"65132\":{\"cp\":10000},\"65171\":{\"cp\":100},\"65172\":{\"cp\":100},\"65173\":{\"cp\":100},\"65174\":{\"cp\":100},\"65175\":{\"cp\":100},\"65176\":{\"cp\":100},\"65177\":{\"cp\":100},\"65178\":{\"cp\":100},\"65180\":{\"cp\":900},\"65183\":{\"cp\":10000},\"65200\":{\"cp\":10000},\"65501\":{\"cp\":10000},\"65502\":{\"cp\":10000},\"65503\":{\"cp\":10000},\"66001\":{\"cp\":100},\"66003\":{\"cp\":10000},\"66101\":{\"cp\":10000},\"66404\":{\"cp\":10000}}},\"B02N_ut_stream\":{\"content\":{\"1\":{\"stm\":\"stm_x\"},\"1000\":{\"stm\":\"stm_p\"},\"2000\":{\"stm\":\"stm_p\"},\"2100\":{\"stm\":\"stm_c\"},\"4007\":{\"stm\":\"stm_d\"},\"5002\":{\"stm\":\"stm_d\"},\"5004\":{\"stm\":\"stm_d\"},\"5005\":{\"stm\":\"stm_d\"},\"6004\":{\"stm\":\"stm_d\"},\"9001\":{\"stm\":\"stm_d\"},\"9002\":{\"stm\":\"stm_d\"},\"9003\":{\"stm\":\"stm_d\"},\"9101\":{\"stm\":\"stm_d\"},\"9199\":{\"stm\":\"stm_d\"},\"15301\":{\"stm\":\"stm_x\"},\"15302\":{\"stm\":\"stm_x\"},\"15303\":{\"stm\":\"stm_x\"},\"15304\":{\"stm\":\"stm_x\"},\"15305\":{\"stm\":\"stm_x\"},\"15306\":{\"stm\":\"stm_x\"},\"15307\":{\"stm\":\"stm_x\"},\"15391\":{\"stm\":\"stm_x\"},\"15392\":{\"stm\":\"stm_x\"},\"15393\":{\"stm\":\"stm_x\"},\"15394\":{\"stm\":\"stm_x\"},\"19999\":{\"stm\":\"stm_d\"},\"21032\":{\"stm\":\"stm_x\"},\"21034\":{\"stm\":\"stm_x\"},\"21064\":{\"stm\":\"stm_x\"},\"22064\":{\"stm\":\"stm_x\"},\"61001\":{\"stm\":\"stm_x\"},\"61006\":{\"stm\":\"stm_x\"},\"61007\":{\"stm\":\"stm_x\"},\"65001\":{\"stm\":\"stm_x\"},\"65100\":{\"stm\":\"stm_x\"},\"65101\":{\"stm\":\"stm_x\"},\"65104\":{\"stm\":\"stm_x\"},\"65105\":{\"stm\":\"stm_x\"},\"65111\":{\"stm\":\"stm_x\"},\"65113\":{\"stm\":\"stm_x\"},\"65114\":{\"stm\":\"stm_x\"},\"65125\":{\"stm\":\"stm_x\"},\"65132\":{\"stm\":\"stm_x\"},\"65171\":{\"stm\":\"stm_x\"},\"65172\":{\"stm\":\"stm_x\"},\"65173\":{\"stm\":\"stm_x\"},\"65174\":{\"stm\":\"stm_x\"},\"65175\":{\"stm\":\"stm_x\"},\"65176\":{\"stm\":\"stm_x\"},\"65177\":{\"stm\":\"stm_x\"},\"65178\":{\"stm\":\"stm_x\"},\"65180\":{\"stm\":\"stm_x\"},\"65183\":{\"stm\":\"stm_x\"},\"65200\":{\"stm\":\"stm_d\"},\"65501\":{\"stm\":\"stm_d\"},\"65502\":{\"stm\":\"stm_d\"},\"65503\":{\"stm\":\"stm_d\"},\"66001\":{\"stm\":\"stm_d\"},\"66003\":{\"stm\":\"stm_d\"},\"66101\":{\"stm\":\"stm_d\"},\"66404\":{\"stm\":\"stm_d\"}}},\"B02N_ut_bussiness\":{\"content\":{\"tpk\":[{\"kn\":\"adid\",\"ty\":\"nearby\"},{\"kn\":\"ucm\",\"ty\":\"nearby\"},{\"kn\":\"bdid\",\"ty\":\"far\"},{\"kn\":\"ref_pid\",\"ty\":\"far\"},{\"kn\":\"pid\",\"ty\":\"far\"},{\"kn\":\"tpa\",\"ty\":\"far\"},{\"kn\":\"point\",\"ty\":\"far\"},{\"kn\":\"ali_trackid\",\"ty\":\"far\"},{\"kn\":\"xncode\",\"ty\":\"nearby\"}]}}}";
    private Vector<UTOrangeConfBiz> mConfBizList = new Vector<>();
    private Map<String, UTOrangeConfBiz> mConfBizMap = new HashMap();
    private String mDefaultWhiteConfigs = null;
    private List<UTDBConfigEntity> mLocalDBConfigEntities = new LinkedList();

    public abstract void requestOnlineConfig();

    public void setDefaultWhiteConfigs(String aConfigText) {
        this.mDefaultWhiteConfigs = aConfigText;
    }

    public UTBaseConfMgr() {
        setDefaultWhiteConfigs(this.DefaultWhiteConfigs1);
    }

    public synchronized void addConfBiz(UTOrangeConfBiz aBusiness) {
        if (aBusiness != null) {
            this.mConfBizList.add(aBusiness);
            for (String lGroupname : aBusiness.getOrangeGroupnames()) {
                this.mConfBizMap.put(lGroupname, aBusiness);
            }
        }
    }

    public synchronized void removeConfBiz(UTOrangeConfBiz aBusiness) {
        this.mConfBizList.remove(aBusiness);
        for (String lGroupname : aBusiness.getOrangeGroupnames()) {
            this.mConfBizMap.remove(lGroupname);
        }
    }

    /* access modifiers changed from: protected */
    public final synchronized List<UTDBConfigEntity> getLocalDBConfigEntities() {
        return this.mLocalDBConfigEntities;
    }

    private final synchronized void _updateLocalConfigEntities(List<UTDBConfigEntity> aDBEntityList) {
        this.mLocalDBConfigEntities = aDBEntityList;
    }

    /* access modifiers changed from: protected */
    public final synchronized void init() {
        this.mLocalDBConfigEntities = _loadAllDBConfig();
        if ((this.mLocalDBConfigEntities == null || this.mLocalDBConfigEntities.size() == 0) && this.mDefaultWhiteConfigs != null) {
            try {
                this.mLocalDBConfigEntities = UTConfigUtils.convertOnlineJsonConfToUTDBConfigEntities(new JSONObject(this.mDefaultWhiteConfigs));
            } catch (JSONException e) {
                Logger.e((String) null, e, new Object[0]);
            }
        }
        return;
    }

    private final synchronized List<UTDBConfigEntity> _loadAllDBConfig() {
        return Variables.getInstance().getDbMgr().find(UTDBConfigEntity.class, (String) null, (String) null, -1);
    }

    /* access modifiers changed from: protected */
    public final synchronized void deleteDBConfigEntity(String aGroupname) {
        if (aGroupname != null) {
            if (this.mLocalDBConfigEntities != null) {
                List<UTDBConfigEntity> lDeletedList = new LinkedList<>();
                for (UTDBConfigEntity lEntity : this.mLocalDBConfigEntities) {
                    if (aGroupname.equals(lEntity.getGroupname())) {
                        lEntity.delete();
                        lDeletedList.add(lEntity);
                    }
                }
                for (UTDBConfigEntity lEntity2 : lDeletedList) {
                    this.mLocalDBConfigEntities.remove(lEntity2);
                }
            }
        }
    }

    private final synchronized void _clearAllOnlineConfig() {
        Variables.getInstance().getDbMgr().clear((Class<? extends Entity>) UTDBConfigEntity.class);
        _updateLocalConfigEntities(new LinkedList<>());
    }

    private final synchronized void _dispatchConfig(List<UTDBConfigEntity> aConfigEntities) {
        if (aConfigEntities != null) {
            Map<String, UTDBConfigEntity> mConfigEntityMap = new HashMap<>();
            for (UTDBConfigEntity aConfigEntity : aConfigEntities) {
                if (aConfigEntity.getGroupname() != null) {
                    mConfigEntityMap.put(aConfigEntity.getGroupname(), aConfigEntity);
                }
            }
            Iterator i$ = this.mConfBizList.iterator();
            while (i$.hasNext()) {
                UTOrangeConfBiz lConfBiz = i$.next();
                for (String lGroupname : lConfBiz.getOrangeGroupnames()) {
                    if (mConfigEntityMap.containsKey(lGroupname)) {
                        UTDBConfigEntity lDBConfEntity = mConfigEntityMap.get(lGroupname);
                        if (Logger.isDebug()) {
                            Logger.d("", "Groupname", lGroupname, "DBConfEntity", StringUtils.transStringToMap(lDBConfEntity.getConfContent()));
                        }
                        lConfBiz.onOrangeConfigurationArrive(lGroupname, StringUtils.transStringToMap(lDBConfEntity.getConfContent()));
                    } else {
                        lConfBiz.onNonOrangeConfigurationArrive(lGroupname);
                    }
                }
            }
        }
    }

    private final synchronized void _dispatchNonConfig() {
        Iterator<UTOrangeConfBiz> it = this.mConfBizList.iterator();
        while (it.hasNext()) {
            UTOrangeConfBiz lConfBiz = it.next();
            for (String lGroupname : lConfBiz.getOrangeGroupnames()) {
                lConfBiz.onNonOrangeConfigurationArrive(lGroupname);
            }
        }
    }

    /* access modifiers changed from: protected */
    public final void dispatchLocalCacheConfigs() {
        _dispatchConfig(getLocalDBConfigEntities());
    }

    private final synchronized List<UTDBConfigEntity> _mergeAndSaveConfig(List<UTDBConfigEntity> aConfigEntities) {
        List<UTDBConfigEntity> lResultEntities;
        Map<String, UTDBConfigEntity> mLocalConfigEntityMap = new HashMap<>();
        for (UTDBConfigEntity lConfigEntity : this.mLocalDBConfigEntities) {
            if (lConfigEntity.getGroupname() != null) {
                mLocalConfigEntityMap.put(lConfigEntity.getGroupname(), lConfigEntity);
            }
        }
        lResultEntities = new LinkedList<>();
        for (UTDBConfigEntity lConfigItem : aConfigEntities) {
            String aGroupname = lConfigItem.getGroupname();
            if (aGroupname != null) {
                UTDBConfigEntity lLocalConfigItem = mLocalConfigEntityMap.get(aGroupname);
                if (lLocalConfigItem != null) {
                    if (!lConfigItem.is304()) {
                        lLocalConfigItem.setConfContent(lConfigItem.getConfContent());
                        lLocalConfigItem.setConfTimestamp(lConfigItem.getConfTimestamp());
                    }
                    lLocalConfigItem.store();
                    lResultEntities.add(lLocalConfigItem);
                } else {
                    deleteDBConfigEntity(aGroupname);
                    lConfigItem.store();
                    lResultEntities.add(lConfigItem);
                    this.mLocalDBConfigEntities.add(lConfigItem);
                }
            }
        }
        return lResultEntities;
    }

    /* access modifiers changed from: protected */
    public final synchronized void updateAndDispatch(List<UTDBConfigEntity> aNewConfigEntities, boolean aCleanDB) {
        if (aNewConfigEntities == null) {
            _dispatchNonConfig();
        } else {
            if (aCleanDB) {
                _clearAllOnlineConfig();
            }
            List<UTDBConfigEntity> lNeedDispatchConfigs = _mergeAndSaveConfig(aNewConfigEntities);
            if (lNeedDispatchConfigs != null && lNeedDispatchConfigs.size() > 0) {
                _dispatchConfig(lNeedDispatchConfigs);
            }
        }
    }

    /* access modifiers changed from: protected */
    public final synchronized void updateAndDispatch(UTDBConfigEntity aNewConfigEntity, boolean aCleanDB) {
        List<UTDBConfigEntity> lConfigEntities = new LinkedList<>();
        lConfigEntities.add(aNewConfigEntity);
        updateAndDispatch(lConfigEntities, aCleanDB);
    }

    public final synchronized void updateAndDispatch(String nameapace, Map<String, String> configs) {
        if (configs != null) {
            String timestamp = configs.remove(TAG_CONFIG_TIMESTAMP);
            if (!TextUtils.isEmpty(timestamp)) {
                ConfigTimeStampMgr.getInstance().put(nameapace, timestamp);
            }
            long timestampLong = 0;
            if (timestamp != null) {
                try {
                    timestampLong = Long.valueOf(timestamp).longValue();
                } catch (Throwable th) {
                    Logger.e("parse Timestamp error", "timestamp", timestamp);
                }
            }
            updateAndDispatch(UTConfigUtils.convertKVToDBConfigEntity(nameapace, configs, timestampLong), false);
        }
        return;
    }

    public static void sendConfigTimeStamp(String type) {
    }
}
