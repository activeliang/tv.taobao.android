package android.taobao.windvane.jsbridge.api;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.runtimepermission.PermissionProposer;
import android.taobao.windvane.service.WVEventId;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WVContacts extends WVApiPlugin {
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";
    private static final String[] PHONES_PROJECTION = {"display_name", "data1"};
    private static final String TAG = "WVContacts";
    private WVCallBackContext mCallback = null;

    public boolean execute(String action, final String params, final WVCallBackContext callback) {
        if ("choose".equals(action)) {
            try {
                PermissionProposer.buildPermissionTask(this.mContext, new String[]{"android.permission.READ_CONTACTS"}).setTaskOnPermissionGranted(new Runnable() {
                    public void run() {
                        WVContacts.this.choose(params, callback);
                    }
                }).setTaskOnPermissionDenied(new Runnable() {
                    public void run() {
                        WVResult result = new WVResult();
                        result.addData("msg", "NO_PERMISSION");
                        callback.error(result);
                    }
                }).execute();
            } catch (Exception e) {
            }
        } else if ("find".equals(action)) {
            try {
                PermissionProposer.buildPermissionTask(this.mContext, new String[]{"android.permission.READ_CONTACTS"}).setTaskOnPermissionGranted(new Runnable() {
                    public void run() {
                        WVContacts.this.find(params, callback);
                    }
                }).setTaskOnPermissionDenied(new Runnable() {
                    public void run() {
                        WVResult result = new WVResult();
                        result.addData("msg", "NO_PERMISSION");
                        callback.error(result);
                    }
                }).execute();
            } catch (Exception e2) {
            }
        } else if (!"authStatus".equals(action)) {
            return false;
        } else {
            try {
                PermissionProposer.buildPermissionTask(this.mContext, new String[]{"android.permission.READ_CONTACTS"}).setTaskOnPermissionGranted(new Runnable() {
                    public void run() {
                        WVContacts.this.authStatus(callback);
                    }
                }).setTaskOnPermissionDenied(new Runnable() {
                    public void run() {
                        WVResult result = new WVResult();
                        result.addData("msg", "NO_PERMISSION");
                        callback.error(result);
                    }
                }).execute();
            } catch (Exception e3) {
            }
        }
        WVEventService.getInstance().onEvent(WVEventId.WV_CONTACT_AUTH_STATUS_EVENT);
        return true;
    }

    /* access modifiers changed from: private */
    public void choose(String params, WVCallBackContext callback) {
        this.mCallback = callback;
        Intent intent = new Intent("android.intent.action.PICK", ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        if (this.mContext instanceof Activity) {
            try {
                ((Activity) this.mContext).startActivityForResult(intent, 4003);
            } catch (Exception e) {
                TaoLog.e("WVContacts", "open pick activity fail, " + e.getMessage());
                callback.error();
            }
        }
    }

    /* access modifiers changed from: private */
    public void find(String params, WVCallBackContext callback) {
        String filterName = null;
        String filterNumber = null;
        try {
            JSONObject filter = new JSONObject(params).optJSONObject("filter");
            if (filter != null) {
                filterName = filter.optString("name");
                filterNumber = filter.optString(KEY_PHONE);
            }
        } catch (JSONException e) {
            TaoLog.e("WVContacts", "find contacts when parse params to JSON error, params=" + params);
        }
        List<ContactInfo> list = getPhoneContacts((String) null, filterName, filterNumber);
        if (list == null) {
            TaoLog.w("WVContacts", "find contacts failed");
            callback.error(new WVResult());
            return;
        }
        WVResult result = new WVResult();
        JSONArray array = new JSONArray();
        try {
            for (ContactInfo info : list) {
                JSONObject object = new JSONObject();
                object.put("name", info.name);
                object.put(KEY_PHONE, info.number);
                array.put(object);
            }
        } catch (JSONException e2) {
            TaoLog.e("WVContacts", "put contacts error, " + e2.getMessage());
        }
        result.addData("contacts", array);
        callback.success(result);
    }

    /* access modifiers changed from: private */
    public void authStatus(final WVCallBackContext callback) {
        new AsyncTask<Void, Integer, Void>() {
            /* access modifiers changed from: protected */
            public Void doInBackground(Void... params) {
                WVResult result = new WVResult();
                Cursor cursor = null;
                Uri uri = Uri.parse("content://com.android.contacts/contacts");
                try {
                    cursor = WVContacts.this.mContext.getContentResolver().query(uri, new String[]{"_id"}, (String) null, (String[]) null, (String) null);
                } catch (Exception e) {
                }
                if (cursor == null) {
                    result.addData("isAuthed", "0");
                } else {
                    result.addData("isAuthed", "1");
                }
                callback.success(result);
                if (cursor != null) {
                    try {
                        cursor.close();
                    } catch (Exception e2) {
                    }
                }
                return null;
            }
        }.execute(new Void[0]);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri contact;
        if (requestCode == 4003 && this.mCallback != null) {
            if (resultCode == -1) {
                if (data == null || (contact = data.getData()) == null) {
                    TaoLog.w("WVContacts", "contact data is null");
                    return;
                }
                String contactId = contact.getLastPathSegment();
                if (!TextUtils.isEmpty(contactId)) {
                    List<ContactInfo> list = getPhoneContacts(contactId, (String) null, (String) null);
                    if (list == null || list.isEmpty()) {
                        TaoLog.w("WVContacts", "contact result is empty");
                        this.mCallback.error(new WVResult());
                        return;
                    }
                    ContactInfo info = list.get(0);
                    if (!TextUtils.isEmpty(info.number)) {
                        WVResult result = new WVResult();
                        result.addData("name", info.name);
                        result.addData(KEY_PHONE, info.number);
                        this.mCallback.success(result);
                        return;
                    }
                }
            }
            if (TaoLog.getLogStatus()) {
                TaoLog.d("WVContacts", "choose contact failed");
            }
            this.mCallback.error(new WVResult());
        }
    }

    private List<ContactInfo> getPhoneContacts(String contactId, String filterName, String filterNumber) {
        String selection;
        String[] mSelectionArgs;
        if (TaoLog.getLogStatus()) {
            TaoLog.d("WVContacts", "contactId: " + contactId + " filterName: " + filterName + " filterNumber: " + filterNumber);
        }
        List<ContactInfo> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            if (!TextUtils.isEmpty(contactId)) {
                selection = "_id= ?";
                mSelectionArgs = new String[]{contactId};
            } else if (!TextUtils.isEmpty(filterName) && !TextUtils.isEmpty(filterNumber)) {
                selection = "display_name like ? AND data1 like ?";
                mSelectionArgs = new String[]{"%" + filterName + "%", "%" + filterNumber + "%"};
            } else if (!TextUtils.isEmpty(filterName)) {
                selection = "display_name like ?";
                mSelectionArgs = new String[]{"%" + filterName + "%"};
            } else {
                selection = "data1 like ?";
                mSelectionArgs = new String[]{"%" + filterNumber + "%"};
            }
            cursor = this.mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, selection, mSelectionArgs, (String) null);
            if (cursor == null) {
                Log.w("WVContacts", "cursor is null.");
                if (cursor == null) {
                    return null;
                }
                try {
                    cursor.close();
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                Log.d("WVContacts", "find contacts record " + cursor.getCount());
                while (cursor.moveToNext()) {
                    String name = cursor.getString(0);
                    String number = cursor.getString(1);
                    if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(number)) {
                        ContactInfo info = new ContactInfo();
                        info.name = name;
                        info.number = number;
                        list.add(info);
                    }
                    Log.d("WVContacts", "displayName: " + name + " phoneNumber: " + number);
                }
                if (cursor == null) {
                    return list;
                }
                try {
                    cursor.close();
                    return list;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return list;
                }
            }
        } catch (Exception e3) {
            TaoLog.e("WVContacts", "query phone error, " + e3.getMessage());
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e4) {
                    e4.printStackTrace();
                }
            }
            return null;
        } catch (Throwable th) {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e5) {
                    e5.printStackTrace();
                }
            }
            throw th;
        }
    }

    private class ContactInfo {
        String name;
        String number;

        private ContactInfo() {
        }
    }
}
