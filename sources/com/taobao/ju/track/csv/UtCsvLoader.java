package com.taobao.ju.track.csv;

import android.content.Context;
import android.text.TextUtils;
import com.taobao.ju.track.constants.Constants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtCsvLoader {
    public Map<String, Map<String, String>> load(Context context, String filename) {
        return read(context, filename);
    }

    private Map<String, Map<String, String>> read(Context context, String filename) {
        if (!CsvFileUtil.isCSV(filename)) {
            return null;
        }
        String[] headers = new String[0];
        if (context != null) {
            try {
                if (context.getAssets() != null) {
                    headers = CsvFileUtil.readHeaders(context.getAssets().open(filename));
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (headers == null) {
                    return null;
                }
            } catch (Throwable th) {
                if (headers == null) {
                    return null;
                }
                throw th;
            }
        }
        if (headers == null) {
            return null;
        }
        List<String[]> ruleData = null;
        if (context != null) {
            try {
                if (context.getAssets() != null) {
                    ruleData = CsvFileUtil.read(context.getAssets().open(filename));
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                if (0 == 0) {
                    return null;
                }
            } catch (Throwable th2) {
                if (0 == 0) {
                    return null;
                }
                throw th2;
            }
        }
        if (ruleData == null) {
            return null;
        }
        Map<String, Map<String, String>> result = new HashMap<>();
        for (String[] rowData : ruleData) {
            if (headers.length != rowData.length) {
                return null;
            }
            readRow(result, headers, rowData, Constants.CSV_PARAM_INTERNAL_KEY);
            readRow(result, headers, rowData, Constants.CSV_PARAM_INTERNAL_ANDROID_PAGE_KEY);
        }
        return result;
    }

    private void readRow(Map<String, Map<String, String>> result, String[] headers, String[] rowData, String internalKey) {
        String rowKey = null;
        Map<String, String> row = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            if (!internalKey.equals(headers[i])) {
                row.put(headers[i], rowData[i]);
            } else if (rowKey == null) {
                rowKey = rowData[i];
            }
        }
        if (!TextUtils.isEmpty(rowKey)) {
            if (rowKey.startsWith("[") && rowKey.endsWith("]")) {
                rowKey = rowKey.substring(1, rowKey.length() - 1);
            }
            result.put(rowKey, row);
        }
    }
}
