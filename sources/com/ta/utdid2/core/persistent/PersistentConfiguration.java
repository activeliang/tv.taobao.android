package com.ta.utdid2.core.persistent;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import com.ta.utdid2.android.utils.StringUtils;
import com.ta.utdid2.core.persistent.MySharedPreferences;
import java.io.File;
import java.util.Map;

public class PersistentConfiguration {
    private static final String KEY_TIMESTAMP = "t";
    private static final String KEY_TIMESTAMP2 = "t2";
    private boolean mCanRead = false;
    private boolean mCanWrite = false;
    private String mConfigName = "";
    private Context mContext = null;
    private SharedPreferences.Editor mEditor = null;
    private String mFolderName = "";
    private boolean mIsLessMode = false;
    private boolean mIsSafety = false;
    private MySharedPreferences.MyEditor mMyEditor = null;
    private MySharedPreferences mMySP = null;
    private SharedPreferences mSp = null;
    private TransactionXMLFile mTxf = null;

    public PersistentConfiguration(Context context, String folderName, String configName, boolean isSafety, boolean isLessMode) {
        this.mIsSafety = isSafety;
        this.mIsLessMode = isLessMode;
        this.mConfigName = configName;
        this.mFolderName = folderName;
        this.mContext = context;
        long spT = 0;
        long mySPT = 0;
        if (context != null) {
            this.mSp = context.getSharedPreferences(configName, 0);
            spT = this.mSp.getLong("t", 0);
        }
        String mountedProperty = null;
        try {
            mountedProperty = Environment.getExternalStorageState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtils.isEmpty(mountedProperty)) {
            this.mCanWrite = false;
            this.mCanRead = false;
        } else if (mountedProperty.equals("mounted")) {
            this.mCanWrite = true;
            this.mCanRead = true;
        } else if (mountedProperty.equals("mounted_ro")) {
            this.mCanRead = true;
            this.mCanWrite = false;
        } else {
            this.mCanWrite = false;
            this.mCanRead = false;
        }
        if ((this.mCanRead || this.mCanWrite) && context != null && !StringUtils.isEmpty(folderName)) {
            this.mTxf = getTransactionXMLFile(folderName);
            if (this.mTxf != null) {
                try {
                    this.mMySP = this.mTxf.getMySharedPreferences(configName, 0);
                    mySPT = this.mMySP.getLong("t", 0);
                    if (isLessMode) {
                        spT = this.mSp.getLong(KEY_TIMESTAMP2, 0);
                        mySPT = this.mMySP.getLong(KEY_TIMESTAMP2, 0);
                        if (spT < mySPT && spT > 0) {
                            copySPToMySP(this.mSp, this.mMySP);
                            this.mMySP = this.mTxf.getMySharedPreferences(configName, 0);
                        } else if (spT > mySPT && mySPT > 0) {
                            copyMySPToSP(this.mMySP, this.mSp);
                            this.mSp = context.getSharedPreferences(configName, 0);
                        } else if (spT == 0 && mySPT > 0) {
                            copyMySPToSP(this.mMySP, this.mSp);
                            this.mSp = context.getSharedPreferences(configName, 0);
                        } else if (mySPT == 0 && spT > 0) {
                            copySPToMySP(this.mSp, this.mMySP);
                            this.mMySP = this.mTxf.getMySharedPreferences(configName, 0);
                        } else if (spT == mySPT) {
                            copySPToMySP(this.mSp, this.mMySP);
                            this.mMySP = this.mTxf.getMySharedPreferences(configName, 0);
                        }
                    } else if (spT > mySPT) {
                        copySPToMySP(this.mSp, this.mMySP);
                        this.mMySP = this.mTxf.getMySharedPreferences(configName, 0);
                    } else if (spT < mySPT) {
                        copyMySPToSP(this.mMySP, this.mSp);
                        this.mSp = context.getSharedPreferences(configName, 0);
                    } else if (spT == mySPT) {
                        copySPToMySP(this.mSp, this.mMySP);
                        this.mMySP = this.mTxf.getMySharedPreferences(configName, 0);
                    }
                } catch (Exception e2) {
                }
            }
        }
        if (spT != mySPT || (spT == 0 && mySPT == 0)) {
            long timestamp = System.currentTimeMillis();
            if (!this.mIsLessMode || (this.mIsLessMode && spT == 0 && mySPT == 0)) {
                if (this.mSp != null) {
                    SharedPreferences.Editor editorTmp = this.mSp.edit();
                    editorTmp.putLong(KEY_TIMESTAMP2, timestamp);
                    editorTmp.commit();
                }
                try {
                    if (this.mMySP != null) {
                        MySharedPreferences.MyEditor myEditorTmp = this.mMySP.edit();
                        myEditorTmp.putLong(KEY_TIMESTAMP2, timestamp);
                        myEditorTmp.commit();
                    }
                } catch (Exception e3) {
                }
            }
        }
    }

    private TransactionXMLFile getTransactionXMLFile(String folderName) {
        File rootFolder = getRootFolder(folderName);
        if (rootFolder == null) {
            return null;
        }
        this.mTxf = new TransactionXMLFile(rootFolder.getAbsolutePath());
        return this.mTxf;
    }

    private File getRootFolder(String folderName) {
        File sdCardFile = Environment.getExternalStorageDirectory();
        if (sdCardFile == null) {
            return null;
        }
        File rootFolder = new File(String.format("%s%s%s", new Object[]{sdCardFile.getAbsolutePath(), File.separator, folderName}));
        if (rootFolder == null || rootFolder.exists()) {
            return rootFolder;
        }
        rootFolder.mkdirs();
        return rootFolder;
    }

    private void copySPToMySP(SharedPreferences sp1, MySharedPreferences sp2) {
        MySharedPreferences.MyEditor myEditorTmp;
        if (sp1 != null && sp2 != null && (myEditorTmp = sp2.edit()) != null) {
            myEditorTmp.clear();
            for (Map.Entry<String, ?> e : sp1.getAll().entrySet()) {
                String key = e.getKey();
                Object value = e.getValue();
                if (value instanceof String) {
                    myEditorTmp.putString(key, (String) value);
                } else if (value instanceof Integer) {
                    myEditorTmp.putInt(key, ((Integer) value).intValue());
                } else if (value instanceof Long) {
                    myEditorTmp.putLong(key, ((Long) value).longValue());
                } else if (value instanceof Float) {
                    myEditorTmp.putFloat(key, ((Float) value).floatValue());
                } else if (value instanceof Boolean) {
                    myEditorTmp.putBoolean(key, ((Boolean) value).booleanValue());
                }
            }
            myEditorTmp.commit();
        }
    }

    private void copyMySPToSP(MySharedPreferences sp1, SharedPreferences sp2) {
        SharedPreferences.Editor editorTmp;
        if (sp1 != null && sp2 != null && (editorTmp = sp2.edit()) != null) {
            editorTmp.clear();
            for (Map.Entry<String, ?> e : sp1.getAll().entrySet()) {
                String key = e.getKey();
                Object value = e.getValue();
                if (value instanceof String) {
                    editorTmp.putString(key, (String) value);
                } else if (value instanceof Integer) {
                    editorTmp.putInt(key, ((Integer) value).intValue());
                } else if (value instanceof Long) {
                    editorTmp.putLong(key, ((Long) value).longValue());
                } else if (value instanceof Float) {
                    editorTmp.putFloat(key, ((Float) value).floatValue());
                } else if (value instanceof Boolean) {
                    editorTmp.putBoolean(key, ((Boolean) value).booleanValue());
                }
            }
            editorTmp.commit();
        }
    }

    private boolean checkSDCardXMLFile() {
        if (this.mMySP == null) {
            return false;
        }
        boolean isExist = this.mMySP.checkFile();
        if (isExist) {
            return isExist;
        }
        commit();
        return isExist;
    }

    private void initEditor() {
        if (this.mEditor == null && this.mSp != null) {
            this.mEditor = this.mSp.edit();
        }
        if (this.mCanWrite && this.mMyEditor == null && this.mMySP != null) {
            this.mMyEditor = this.mMySP.edit();
        }
        checkSDCardXMLFile();
    }

    public void putInt(String key, int value) {
        if (!StringUtils.isEmpty(key) && !key.equals("t")) {
            initEditor();
            if (this.mEditor != null) {
                this.mEditor.putInt(key, value);
            }
            if (this.mMyEditor != null) {
                this.mMyEditor.putInt(key, value);
            }
        }
    }

    public void putLong(String key, long value) {
        if (!StringUtils.isEmpty(key) && !key.equals("t")) {
            initEditor();
            if (this.mEditor != null) {
                this.mEditor.putLong(key, value);
            }
            if (this.mMyEditor != null) {
                this.mMyEditor.putLong(key, value);
            }
        }
    }

    public void putBoolean(String key, boolean value) {
        if (!StringUtils.isEmpty(key) && !key.equals("t")) {
            initEditor();
            if (this.mEditor != null) {
                this.mEditor.putBoolean(key, value);
            }
            if (this.mMyEditor != null) {
                this.mMyEditor.putBoolean(key, value);
            }
        }
    }

    public void putFloat(String key, float value) {
        if (!StringUtils.isEmpty(key) && !key.equals("t")) {
            initEditor();
            if (this.mEditor != null) {
                this.mEditor.putFloat(key, value);
            }
            if (this.mMyEditor != null) {
                this.mMyEditor.putFloat(key, value);
            }
        }
    }

    public void putString(String key, String value) {
        if (!StringUtils.isEmpty(key) && !key.equals("t")) {
            initEditor();
            if (this.mEditor != null) {
                this.mEditor.putString(key, value);
            }
            if (this.mMyEditor != null) {
                this.mMyEditor.putString(key, value);
            }
        }
    }

    public void remove(String key) {
        if (!StringUtils.isEmpty(key) && !key.equals("t")) {
            initEditor();
            if (this.mEditor != null) {
                this.mEditor.remove(key);
            }
            if (this.mMyEditor != null) {
                this.mMyEditor.remove(key);
            }
        }
    }

    public void reload() {
        if (!(this.mSp == null || this.mContext == null)) {
            this.mSp = this.mContext.getSharedPreferences(this.mConfigName, 0);
        }
        String mountedProperty = null;
        try {
            mountedProperty = Environment.getExternalStorageState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtils.isEmpty(mountedProperty)) {
            return;
        }
        if (mountedProperty.equals("mounted") || (mountedProperty.equals("mounted_ro") && this.mMySP != null)) {
            try {
                if (this.mTxf != null) {
                    this.mMySP = this.mTxf.getMySharedPreferences(this.mConfigName, 0);
                }
            } catch (Exception e2) {
            }
        }
    }

    public void clear() {
        initEditor();
        long t = System.currentTimeMillis();
        if (this.mEditor != null) {
            this.mEditor.clear();
            this.mEditor.putLong("t", t);
        }
        if (this.mMyEditor != null) {
            this.mMyEditor.clear();
            this.mMyEditor.putLong("t", t);
        }
    }

    public boolean commit() {
        boolean result = true;
        long t = System.currentTimeMillis();
        if (this.mEditor != null) {
            if (!this.mIsLessMode && this.mSp != null) {
                this.mEditor.putLong("t", t);
            }
            if (!this.mEditor.commit()) {
                result = false;
            }
        }
        if (!(this.mSp == null || this.mContext == null)) {
            this.mSp = this.mContext.getSharedPreferences(this.mConfigName, 0);
        }
        String mountedProperty = null;
        try {
            mountedProperty = Environment.getExternalStorageState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!StringUtils.isEmpty(mountedProperty)) {
            if (mountedProperty.equals("mounted")) {
                if (this.mMySP == null) {
                    TransactionXMLFile txf = getTransactionXMLFile(this.mFolderName);
                    if (txf != null) {
                        this.mMySP = txf.getMySharedPreferences(this.mConfigName, 0);
                        if (!this.mIsLessMode) {
                            copySPToMySP(this.mSp, this.mMySP);
                        } else {
                            copyMySPToSP(this.mMySP, this.mSp);
                        }
                        this.mMyEditor = this.mMySP.edit();
                    }
                } else if (this.mMyEditor != null && !this.mMyEditor.commit()) {
                    result = false;
                }
            }
            if (mountedProperty.equals("mounted") || (mountedProperty.equals("mounted_ro") && this.mMySP != null)) {
                try {
                    if (this.mTxf != null) {
                        this.mMySP = this.mTxf.getMySharedPreferences(this.mConfigName, 0);
                    }
                } catch (Exception e2) {
                }
            }
        }
        return result;
    }

    public String getString(String key) {
        checkSDCardXMLFile();
        if (this.mSp != null) {
            String value = this.mSp.getString(key, "");
            if (!StringUtils.isEmpty(value)) {
                return value;
            }
        }
        if (this.mMySP != null) {
            return this.mMySP.getString(key, "");
        }
        return "";
    }

    public int getInt(String key) {
        checkSDCardXMLFile();
        if (this.mSp != null) {
            return this.mSp.getInt(key, 0);
        }
        if (this.mMySP != null) {
            return this.mMySP.getInt(key, 0);
        }
        return 0;
    }

    public long getLong(String key) {
        checkSDCardXMLFile();
        if (this.mSp != null) {
            return this.mSp.getLong(key, 0);
        }
        if (this.mMySP != null) {
            return this.mMySP.getLong(key, 0);
        }
        return 0;
    }

    public float getFloat(String key) {
        checkSDCardXMLFile();
        if (this.mSp != null) {
            return this.mSp.getFloat(key, 0.0f);
        }
        if (this.mMySP != null) {
            return this.mMySP.getFloat(key, 0.0f);
        }
        return 0.0f;
    }

    public boolean getBoolean(String key) {
        checkSDCardXMLFile();
        if (this.mSp != null) {
            return this.mSp.getBoolean(key, false);
        }
        if (this.mMySP != null) {
            return this.mMySP.getBoolean(key, false);
        }
        return false;
    }

    public Map<String, ?> getAll() {
        checkSDCardXMLFile();
        if (this.mSp != null) {
            return this.mSp.getAll();
        }
        if (this.mMySP != null) {
            return this.mMySP.getAll();
        }
        return null;
    }
}
