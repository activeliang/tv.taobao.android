package com.ali.auth.third.offline.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.ali.auth.third.core.model.AccountContract;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "offlineAccounts";
    private static final int DATABASE_VERSION = 4;
    private static final String KEY_ID = "_hash";
    private static final String KEY_ID_WHAT = "_hash_key";
    private static final String KEY_NICK = "_nick";
    private static final String KEY_OPENID = "_openid";
    private static final String KEY_USERID = "_userid";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS accounts";
    private static final String TABLE_ACCOUTNS = "accounts";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 4);
    }

    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE accounts(_hash TEXT , _openid TEXT , _userid TEXT , _nick TEXT, _hash_key TEXT  )");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public boolean addAccount(AccountContract accountContract) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(KEY_ID, accountContract.getHash());
            cv.put(KEY_ID_WHAT, accountContract.getHash_key());
            cv.put(KEY_OPENID, accountContract.getOpenid());
            cv.put(KEY_USERID, accountContract.getUserid());
            cv.put(KEY_NICK, accountContract.getNick());
            db.insert("accounts", (String) null, cv);
            return true;
        } catch (Throwable th) {
            return true;
        } finally {
            db.close();
        }
    }

    public AccountContract getAccount(String hash) {
        Cursor cursor = getReadableDatabase().query("accounts", new String[]{KEY_ID, KEY_OPENID, KEY_USERID, KEY_NICK}, "_hash=?", new String[]{hash}, (String) null, (String) null, (String) null, (String) null);
        if (cursor == null || !cursor.moveToFirst()) {
            return null;
        }
        try {
            return new AccountContract(hash, cursor.getString(1), cursor.getString(2), cursor.getString(3));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteAccountByUserId(String userid) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.delete("accounts", "_userid = ?", new String[]{userid});
        } catch (Exception e) {
        } finally {
            db.close();
        }
        return true;
    }

    public boolean deleteAccountByOpenId(String openid) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.delete("accounts", "_openid = ?", new String[]{openid});
        } catch (Throwable th) {
        } finally {
            db.close();
        }
        return true;
    }

    public List<AccountContract> getAllAccounts() {
        List<AccountContract> lists = new ArrayList<>();
        try {
            Cursor cursor = getWritableDatabase().rawQuery("SELECT * FROM  accounts", (String[]) null);
            if (cursor.moveToFirst()) {
                do {
                    AccountContract accountContract = new AccountContract(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                    accountContract.setHash_key(cursor.getString(4));
                    lists.add(accountContract);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lists;
    }

    public boolean dropTable() {
        return true;
    }
}
