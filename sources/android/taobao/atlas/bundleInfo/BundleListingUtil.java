package android.taobao.atlas.bundleInfo;

import android.taobao.atlas.bundleInfo.BundleListing;
import android.text.TextUtils;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class BundleListingUtil {
    public static LinkedHashMap<String, BundleListing.BundleInfo> parseArray(String listingStr) throws Exception {
        LinkedHashMap<String, BundleListing.BundleInfo> infos = new LinkedHashMap<>();
        JSONArray array = new JSONArray(listingStr);
        for (int x = 0; x < array.length(); x++) {
            JSONObject object = array.getJSONObject(x);
            BundleListing.BundleInfo info = new BundleListing.BundleInfo();
            info.name = object.optString("name");
            info.pkgName = object.optString("pkgName");
            info.applicationName = object.optString("applicationName");
            info.version = object.optString("version");
            info.desc = object.optString("desc");
            info.url = object.optString("url");
            info.md5 = object.optString("md5");
            if (TextUtils.isEmpty(object.optString("unique_tag"))) {
                throw new IOException("uniqueTag is empty");
            }
            info.unique_tag = object.optString("unique_tag");
            if (object.has("isInternal")) {
                info.isInternal = object.optBoolean("isInternal");
            }
            if (object.has("isMBundle")) {
                info.isMBundle = object.optBoolean("isMBundle");
            }
            JSONArray dependency = object.optJSONArray("dependency");
            if (dependency != null && dependency.length() > 0) {
                List<String> dependencyList = new ArrayList<>();
                for (int i = 0; i < dependency.length(); i++) {
                    dependencyList.add(dependency.getString(i));
                }
                info.setDependency(dependencyList);
            }
            JSONArray activities = object.optJSONArray("activities");
            if (activities != null && activities.length() > 0) {
                HashMap<String, Boolean> activitiesList = new HashMap<>();
                for (int i2 = 0; i2 < activities.length(); i2++) {
                    activitiesList.put(activities.getString(i2), Boolean.FALSE);
                }
                info.activities = activitiesList;
            }
            JSONArray services = object.optJSONArray("services");
            if (services != null && services.length() > 0) {
                HashMap<String, Boolean> servicesList = new HashMap<>();
                for (int i3 = 0; i3 < services.length(); i3++) {
                    servicesList.put(services.getString(i3), Boolean.FALSE);
                }
                info.services = servicesList;
            }
            JSONArray receivers = object.optJSONArray("receivers");
            if (receivers != null && receivers.length() > 0) {
                HashMap<String, Boolean> receiversList = new HashMap<>();
                for (int i4 = 0; i4 < receivers.length(); i4++) {
                    receiversList.put(receivers.getString(i4), Boolean.FALSE);
                }
                info.receivers = receiversList;
            }
            JSONArray contentProviders = object.optJSONArray("contentProviders");
            if (contentProviders != null && contentProviders.length() > 0) {
                HashMap<String, Boolean> contentProvidersList = new HashMap<>();
                for (int i5 = 0; i5 < contentProviders.length(); i5++) {
                    contentProvidersList.put(contentProviders.getString(i5), Boolean.FALSE);
                }
                info.contentProviders = contentProvidersList;
            }
            JSONObject remoteFragments = object.optJSONObject("remoteFragments");
            if (remoteFragments != null && remoteFragments.length() > 0) {
                HashMap<String, String> remoteFragmentsList = new HashMap<>();
                Iterator keys = remoteFragments.keys();
                while (keys.hasNext()) {
                    String next = keys.next();
                    String remoteFragment = remoteFragments.getString(next);
                    if (remoteFragment == null) {
                        Log.w("BundleListingUtil", "parseArray: No remoteFragment found for next " + next);
                    } else {
                        remoteFragmentsList.put(next, remoteFragment);
                    }
                }
                info.remoteFragments = remoteFragmentsList;
            }
            JSONObject remoteViews = object.optJSONObject("remoteViews");
            if (remoteViews != null && remoteViews.length() > 0) {
                HashMap<String, String> remoteViewsList = new HashMap<>();
                Iterator keys2 = remoteViews.keys();
                while (keys2.hasNext()) {
                    String next2 = keys2.next();
                    String remoteView = remoteViews.getString(next2);
                    if (remoteView == null) {
                        Log.w("BundleListingUtil", "parseArray: No remoteView found for next " + next2);
                    } else {
                        remoteViewsList.put(next2, remoteView);
                    }
                }
                info.remoteViews = remoteViewsList;
            }
            JSONObject remoteTransactors = object.optJSONObject("remoteTransactors");
            if (remoteTransactors != null && remoteTransactors.length() > 0) {
                HashMap<String, String> remoteTransactorsList = new HashMap<>();
                Iterator keys3 = remoteTransactors.keys();
                while (keys3.hasNext()) {
                    String next3 = keys3.next();
                    String remoteTransactor = remoteTransactors.getString(next3);
                    if (remoteTransactor == null) {
                        Log.w("BundleListingUtil", "parseArray: No remoteTransactor found for next " + next3);
                    } else {
                        remoteTransactorsList.put(next3, remoteTransactor);
                    }
                }
                info.remoteTransactors = remoteTransactorsList;
            }
            infos.put(info.getPkgName(), info);
        }
        if (infos.size() > 0) {
            return infos;
        }
        return null;
    }
}
