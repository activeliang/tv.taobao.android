package android.taobao.windvane.config;

import android.taobao.windvane.thread.WVThreadPool;
import android.taobao.windvane.util.ConfigStorage;
import android.text.TextUtils;
import java.lang.reflect.Field;
import org.json.JSONException;
import org.json.JSONObject;

public class ModuleConfig {
    private static final String SPNAME = "ModuleConfig";
    public boolean url_updateConfig;

    private ModuleConfig() {
        this.url_updateConfig = false;
    }

    private static class SingletonHolder {
        public static final ModuleConfig instance = getInstance();

        private SingletonHolder() {
        }

        private static ModuleConfig getInstance() {
            ModuleConfig config = new ModuleConfig();
            try {
                String data = ConfigStorage.getStringVal(ModuleConfig.SPNAME, ConfigStorage.KEY_DATA);
                if (!TextUtils.isEmpty(data)) {
                    JSONObject jso = new JSONObject(data);
                    for (Field field : config.getClass().getFields()) {
                        field.setBoolean(config, jso.getBoolean(field.getName()));
                    }
                }
            } catch (Exception e) {
            }
            return config;
        }
    }

    public static ModuleConfig getInstance() {
        return SingletonHolder.instance;
    }

    public void saveConfig() {
        final JSONObject jso = new JSONObject();
        for (Field field : getClass().getFields()) {
            try {
                jso.put(field.getName(), field.getBoolean(this));
            } catch (IllegalAccessException e) {
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        WVThreadPool.getInstance().execute(new Runnable() {
            public void run() {
                ConfigStorage.putStringVal(ModuleConfig.SPNAME, ConfigStorage.KEY_DATA, jso.toString());
            }
        });
    }
}
