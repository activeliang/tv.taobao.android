package com.yunos.tv.blitz.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BzResult {
    public static final String CLOSED = "HY_CLOSED";
    public static final String FAIL = "HY_FAILED";
    public static final String NOT_LOGIN = "HY_NOT_LOGIN";
    public static final String NO_METHOD = "HY_NO_HANDLER";
    public static final String NO_PERMISSION = "HY_NO_PERMISSION";
    public static final String PARAM_ERR = "HY_PARAM_ERR";
    public static final BzResult RET_CLOSED = new BzResult("HY_CLOSED");
    public static final BzResult RET_FAIL = new BzResult("HY_FAILED");
    public static final BzResult RET_NOT_LOGIN = new BzResult(NOT_LOGIN);
    public static final BzResult RET_NO_METHOD = new BzResult("HY_NO_HANDLER");
    public static final BzResult RET_NO_PERMISSION = new BzResult("HY_NO_PERMISSION");
    public static final BzResult RET_PARAM_ERR = new BzResult("HY_PARAM_ERR");
    public static final BzResult RET_SUCCESS = new BzResult("HY_SUCCESS");
    public static final String SUCCESS = "HY_SUCCESS";
    private JSONObject result = new JSONObject();
    private int success = 0;

    public BzResult() {
    }

    public BzResult(String retString) {
        setResult(retString);
    }

    public void setData(JSONObject data) {
        if (data != null) {
            this.result = data;
        }
    }

    public void addData(String key, String value) {
        if (key != null && value != null) {
            try {
                this.result.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void addData(String key, boolean value) {
        if (key != null) {
            try {
                this.result.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void addData(String key, int value) {
        if (key != null) {
            try {
                this.result.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void addData(String key, JSONObject value) {
        if (key != null && value != null) {
            try {
                this.result.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void addData(String key, JSONArray value) {
        if (key != null && value != null) {
            try {
                this.result.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSuccess() {
        this.success = 1;
    }

    public void setResult(String retString) {
        try {
            this.result.put("ret", retString);
            this.success = "HY_SUCCESS".equals(retString) ? 1 : -1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String toJsonString() {
        try {
            if (this.success == 1) {
                this.result.put("ret", "HY_SUCCESS");
            } else if (this.success == 0) {
                this.result.put("ret", "HY_FAILED");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this.result.toString();
    }
}
