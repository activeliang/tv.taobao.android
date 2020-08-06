package android.taobao.atlas.util.log.impl;

import android.taobao.atlas.util.log.IMonitor;
import java.util.Map;

public class AtlasMonitor {
    public static final String ACTIVITY_THREAD_HOOK_BAD_TOKEN_EXCEPTION = "activity_thread_hook_bad_token_exception";
    public static final String ACTIVITY_THREAD_HOOK_CLASS_NOT_FOUND_EXCEPTION = "activity_thread_hook_class_not_found_exception";
    public static final String ACTIVITY_THREAD_HOOK_EXCEPTION = "activity_thread_hook_exception";
    public static final String ADD_BAD_APP_TOKEN = "add_bad_app_token";
    public static final String ADD_BAD_SUBWINDOW_TOKEN = "add_bad_subwindow_token";
    public static final String BUNDLE_DEPENDENCY_ERROR = "bundle_dependency_error";
    public static final String CONTAINER_APPEND_ASSETPATH_FAIL = "container_append_assetpath_fail";
    public static final String CONTAINER_BUNDLEINFO_PARSE_FAIL = "container_bundleinfo_parse_fail";
    public static final String CONTAINER_BUNDLE_SOURCE_MISMATCH = "container_bundle_mismatch";
    public static final String CONTAINER_BUNDLE_SOURCE_UNZIP_FAIL = "container_bundle_unzip_fail";
    public static final String CONTAINER_DEXOPT_FAIL = "container_dexopt_fail";
    public static final String CONTAINER_LOADEDAPK_CHANGE = "container_loadapk_change";
    public static final String CONTAINER_SOLIB_UNZIP_FAIL = "container_solib_unzip_fail";
    public static final String DD_BUNDLE_MISMATCH = "dd_bundle_mismatch";
    public static final String DD_BUNDLE_RESOLVEFAIL = "bundle_resolve_fail";
    public static final String INSTALL = "install";
    public static final String INSTRUMENTATION_HOOK_CLASS_NOT_FOUND_EXCEPTION = "instrumentation_hook_class_not_found_exception";
    public static final String NEWCOMPONENT_SERVICE = "newcomponent_service";
    public static final String VALIDATE_CLASSES = "validate_classes";
    public static final String WALKROUND_GETLAYOUT = "walkround_getlayout";
    private static IMonitor externalMonitor;
    private static AtlasMonitor singleton;

    public static synchronized AtlasMonitor getInstance() {
        AtlasMonitor atlasMonitor;
        synchronized (AtlasMonitor.class) {
            if (singleton == null) {
                singleton = new AtlasMonitor();
            }
            atlasMonitor = singleton;
        }
        return atlasMonitor;
    }

    public static void setExternalMonitor(IMonitor monitor) {
        externalMonitor = monitor;
    }

    public void report(String errCode, Map<String, Object> detail, Throwable throwable) {
        if (externalMonitor != null) {
            externalMonitor.report(errCode, detail, throwable);
        }
    }
}
