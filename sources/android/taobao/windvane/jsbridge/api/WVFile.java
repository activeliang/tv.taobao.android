package android.taobao.windvane.jsbridge.api;

import android.taobao.windvane.cache.WVCacheManager;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.text.TextUtils;
import com.alibaba.sdk.android.oss.common.RequestParameters;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.json.JSONObject;

public class WVFile extends WVApiPlugin {
    public static final long FILE_MAX_SIZE = 5242880;

    public boolean execute(String action, String params, WVCallBackContext callback) {
        if ("read".equals(action)) {
            read(params, callback);
        } else if (!"write".equals(action)) {
            return false;
        } else {
            write(params, callback);
        }
        return true;
    }

    public final void read(String params, WVCallBackContext callback) {
        String dir;
        String fileName = "";
        String share = "";
        if (!TextUtils.isEmpty(params)) {
            try {
                JSONObject jsObj = new JSONObject(params);
                fileName = jsObj.optString("fileName");
                share = jsObj.optString("share", "false");
                if (fileName == null || fileName.contains(File.separator)) {
                    throw new Exception();
                }
            } catch (Exception e) {
                callback.error(new WVResult("HY_PARAM_ERR"));
            }
        }
        String dir2 = WVCacheManager.getInstance().getCacheDir(false);
        if (dir2 == null) {
            WVResult result = new WVResult();
            result.addData("error", "GET_DIR_FAILED");
            callback.error(result);
            return;
        }
        if (!"true".equalsIgnoreCase(share)) {
            dir = (dir2 + File.separator) + WVUtils.getHost(this.mWebView.getUrl());
        } else {
            dir = (dir2 + File.separator) + "wvShareFiles";
        }
        String data = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(dir + File.separator + fileName));
            byte[] buffer = new byte[fileInputStream.available()];
            fileInputStream.read(buffer);
            String data2 = new String(buffer, "UTF-8");
            try {
                fileInputStream.close();
                data = data2;
            } catch (FileNotFoundException e2) {
                data = data2;
                WVResult result2 = new WVResult();
                result2.addData("error", "FILE_NOT_FOUND");
                callback.error(result2);
                WVResult result3 = new WVResult();
                result3.addData("data", data);
                callback.success(result3);
            } catch (Exception e3) {
                e = e3;
                data = data2;
                WVResult result4 = new WVResult();
                result4.addData("error", "READ_FILE_FAILED");
                callback.error(result4);
                e.printStackTrace();
                WVResult result32 = new WVResult();
                result32.addData("data", data);
                callback.success(result32);
            }
        } catch (FileNotFoundException e4) {
            WVResult result22 = new WVResult();
            result22.addData("error", "FILE_NOT_FOUND");
            callback.error(result22);
            WVResult result322 = new WVResult();
            result322.addData("data", data);
            callback.success(result322);
        } catch (Exception e5) {
            e = e5;
            WVResult result42 = new WVResult();
            result42.addData("error", "READ_FILE_FAILED");
            callback.error(result42);
            e.printStackTrace();
            WVResult result3222 = new WVResult();
            result3222.addData("data", data);
            callback.success(result3222);
        }
        WVResult result32222 = new WVResult();
        result32222.addData("data", data);
        callback.success(result32222);
    }

    public final void write(String params, WVCallBackContext callback) {
        String dir;
        WVResult result;
        String mode = "";
        String data = "";
        String fileName = "";
        String share = "";
        WVResult result2 = null;
        if (!TextUtils.isEmpty(params)) {
            try {
                JSONObject jSONObject = new JSONObject(params);
                mode = jSONObject.optString("mode", "write");
                data = jSONObject.optString("data");
                fileName = jSONObject.optString("fileName");
                share = jSONObject.optString("share", "false");
                if (mode == null || fileName == null || fileName.contains(File.separator)) {
                    throw new Exception();
                }
            } catch (Exception e) {
                result2 = new WVResult();
                result2.addData("error", "PARAMS_ERROR");
                callback.error(result2);
            }
        }
        String dir2 = WVCacheManager.getInstance().getCacheDir(false);
        if (dir2 == null) {
            WVResult result3 = new WVResult();
            result3.addData("error", "GET_DIR_FAILED");
            callback.error(result3);
            return;
        }
        if (!"true".equalsIgnoreCase(share)) {
            dir = (dir2 + File.separator) + WVUtils.getHost(this.mWebView.getUrl());
        } else {
            dir = (dir2 + File.separator) + "wvShareFiles";
        }
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File dataFile = new File(dir + File.separator + fileName);
        if (dataFile.exists()) {
            if ("write".equalsIgnoreCase(mode)) {
                WVResult result4 = new WVResult();
                result4.addData("error", "FILE_EXIST");
                callback.error(result4);
                return;
            }
            result = result2;
        } else {
            try {
                dataFile.createNewFile();
                result = result2;
            } catch (IOException e2) {
                result2 = new WVResult();
                result2.addData("error", "MAKE_FILE_FAILED");
                callback.error(result2);
            }
        }
        try {
            boolean isAppend = RequestParameters.SUBRESOURCE_APPEND.equalsIgnoreCase(mode);
            if (!canWriteFile(dataFile.length(), data, isAppend)) {
                WVResult result5 = new WVResult();
                try {
                    result5.addData("error", "FILE_TOO_LARGE");
                    callback.error(result5);
                } catch (Exception e3) {
                    e = e3;
                }
            } else {
                FileOutputStream fout = new FileOutputStream(dataFile, isAppend);
                try {
                    fout.write(data.getBytes());
                    fout.close();
                    WVResult wVResult = result;
                } catch (Exception e4) {
                    e = e4;
                    FileOutputStream fileOutputStream = fout;
                    WVResult wVResult2 = result;
                    WVResult result6 = new WVResult();
                    result6.addData("error", "WRITE_FILE_FAILED");
                    callback.error(result6);
                    e.printStackTrace();
                    callback.success();
                }
                callback.success();
            }
        } catch (Exception e5) {
            e = e5;
            WVResult wVResult3 = result;
            WVResult result62 = new WVResult();
            result62.addData("error", "WRITE_FILE_FAILED");
            callback.error(result62);
            e.printStackTrace();
            callback.success();
        }
    }

    private boolean canWriteFile(long fileLength, String data, boolean isAppend) {
        long total;
        if (isAppend) {
            total = fileLength + ((long) data.length());
        } else {
            total = (long) data.length();
        }
        if (total > FILE_MAX_SIZE) {
            return false;
        }
        return true;
    }
}
