package com.yunos.tv.blitz.service;

import android.taobao.windvane.jsbridge.utils.WVUtils;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BlitzServiceUtils {
    public static final String CCLIENT_ACTION = "clientAction";
    public static final String CCLIENT_EXTRAS = "extras";
    public static final String CIFACE_ARGS = "ifaceArgs";
    public static final String CIFACE_NAME = "ifaceName";
    public static final String CIFACE_TYPE = "ifaceType";
    public static final int CIFACE_TYPE_JS = 1;
    public static final int CIFACE_TYPE_NATIVE = 0;
    public static final String CINTERFACE_COUNT = "ifaceCount";
    public static final String CINTERFACE_LIST = "ifaceList";
    public static final String CMSG_INFO = "msgInfo";
    public static final String CREPLY_TYPE = "replyType";
    public static final String CRESLUT = "result";
    public static final String CSERVICE_URI = "serviceUri";
    public static final String CSEVICE_IFACE_LIST = "getServiceIface";
    public static final String CSUCCESS = "success";
    public static final int E_SC_BIND_SERVICE = 2;
    public static final int E_SC_IFACE_CALL = 4;
    public static final int E_SC_START_SERVICE = 0;
    public static final int E_SC_STOP_SERVICE = 1;
    public static final int E_SC_UNBIND_SERVICE = 3;
    public static final int E_SS_IFACE_CALL = 0;
    public static final int E_SS_LISTENER_IFACE_CALL = 1;

    public static List<String> getAndroidServiceIfaceList(Class<?> clazz) {
        List<String> ifaceList = new ArrayList<>();
        Method[] methods = clazz.getMethods();
        for (Method name : methods) {
            ifaceList.add(name.getName());
        }
        return ifaceList;
    }

    public static String buildParamsForSetIfaceList(String params, List<String> ifaceList) {
        try {
            JSONObject jsonObject = new JSONObject(params);
            JSONObject resultObject = new JSONObject();
            JSONObject ifaceListInfo = new JSONObject();
            ifaceListInfo.put(CINTERFACE_COUNT, ifaceList.size());
            ifaceListInfo.put(CINTERFACE_LIST, new JSONArray(ifaceList));
            resultObject.put("result", ifaceListInfo);
            resultObject.put(CMSG_INFO, jsonObject.get(CMSG_INFO));
            return resultObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String buildParamsForSetCallResult(String params, String callResult) {
        try {
            JSONObject msgInfoObject = new JSONObject(params).getJSONObject(CMSG_INFO);
            msgInfoObject.put("dstCoreIndex", msgInfoObject.getDouble("srcCoreIndex"));
            msgInfoObject.put("message", callResult);
            msgInfoObject.put("messageType", 6);
            JSONObject resultObject = new JSONObject();
            resultObject.put(CMSG_INFO, msgInfoObject);
            return resultObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getServiceUri(String params) {
        try {
            return new JSONObject(params).getString(CSERVICE_URI);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getServiceUriWithoutPlatformInfo(String params) {
        try {
            String serviceUri = new JSONObject(params).getString(CSERVICE_URI);
            if (serviceUri.lastIndexOf(WVUtils.URL_DATA_CHAR) > 0) {
                return serviceUri.substring(0, serviceUri.lastIndexOf(WVUtils.URL_DATA_CHAR));
            }
            return serviceUri;
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getInterfaceName(String params) {
        try {
            return new JSONObject(params).getString(CIFACE_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getInterfaceType(String params) {
        try {
            return new JSONObject(params).getInt(CIFACE_TYPE);
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static List<String> getInterfaceArgs(String params) {
        List<String> argsList = new ArrayList<>();
        try {
            JSONArray ifaceArray = new JSONArray(new JSONObject(params).getJSONObject(CMSG_INFO).getString("message"));
            for (int i = 0; i < ifaceArray.length(); i++) {
                argsList.add(ifaceArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return argsList;
    }
}
