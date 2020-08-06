package android.support.v4.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.xmlpull.v1.XmlPullParserException;

public class FileProvider extends ContentProvider {
    private static final String ATTR_NAME = "name";
    private static final String ATTR_PATH = "path";
    private static final String[] COLUMNS = {"_display_name", "_size"};
    private static final File DEVICE_ROOT = new File(WVNativeCallbackUtil.SEPERATER);
    private static final String META_DATA_FILE_PROVIDER_PATHS = "android.support.FILE_PROVIDER_PATHS";
    private static final String TAG_CACHE_PATH = "cache-path";
    private static final String TAG_EXTERNAL = "external-path";
    private static final String TAG_EXTERNAL_CACHE = "external-cache-path";
    private static final String TAG_EXTERNAL_FILES = "external-files-path";
    private static final String TAG_FILES_PATH = "files-path";
    private static final String TAG_ROOT_PATH = "root-path";
    private static HashMap<String, PathStrategy> sCache = new HashMap<>();
    private PathStrategy mStrategy;

    interface PathStrategy {
        File getFileForUri(Uri uri);

        Uri getUriForFile(File file);
    }

    public boolean onCreate() {
        return true;
    }

    public void attachInfo(Context context, ProviderInfo info) {
        super.attachInfo(context, info);
        if (info.exported) {
            throw new SecurityException("Provider must not be exported");
        } else if (!info.grantUriPermissions) {
            throw new SecurityException("Provider must grant uri permissions");
        } else {
            this.mStrategy = getPathStrategy(context, info.authority);
        }
    }

    public static Uri getUriForFile(Context context, String authority, File file) {
        return getPathStrategy(context, authority).getUriForFile(file);
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int i;
        File file = this.mStrategy.getFileForUri(uri);
        if (projection == null) {
            projection = COLUMNS;
        }
        String[] cols = new String[projection.length];
        Object[] values = new Object[projection.length];
        int length = projection.length;
        int i2 = 0;
        int i3 = 0;
        while (i2 < length) {
            String col = projection[i2];
            if ("_display_name".equals(col)) {
                cols[i3] = "_display_name";
                i = i3 + 1;
                values[i3] = file.getName();
            } else if ("_size".equals(col)) {
                cols[i3] = "_size";
                i = i3 + 1;
                values[i3] = Long.valueOf(file.length());
            } else {
                i = i3;
            }
            i2++;
            i3 = i;
        }
        String[] cols2 = copyOf(cols, i3);
        Object[] values2 = copyOf(values, i3);
        MatrixCursor cursor = new MatrixCursor(cols2, 1);
        cursor.addRow(values2);
        return cursor;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0012, code lost:
        r3 = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(r1.getName().substring(r2 + 1));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getType(android.net.Uri r7) {
        /*
            r6 = this;
            android.support.v4.content.FileProvider$PathStrategy r4 = r6.mStrategy
            java.io.File r1 = r4.getFileForUri(r7)
            java.lang.String r4 = r1.getName()
            r5 = 46
            int r2 = r4.lastIndexOf(r5)
            if (r2 < 0) goto L_0x0027
            java.lang.String r4 = r1.getName()
            int r5 = r2 + 1
            java.lang.String r0 = r4.substring(r5)
            android.webkit.MimeTypeMap r4 = android.webkit.MimeTypeMap.getSingleton()
            java.lang.String r3 = r4.getMimeTypeFromExtension(r0)
            if (r3 == 0) goto L_0x0027
        L_0x0026:
            return r3
        L_0x0027:
            java.lang.String r3 = "application/octet-stream"
            goto L_0x0026
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.content.FileProvider.getType(android.net.Uri):java.lang.String");
    }

    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("No external inserts");
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("No external updates");
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return this.mStrategy.getFileForUri(uri).delete() ? 1 : 0;
    }

    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        return ParcelFileDescriptor.open(this.mStrategy.getFileForUri(uri), modeToMode(mode));
    }

    private static PathStrategy getPathStrategy(Context context, String authority) {
        PathStrategy strat;
        synchronized (sCache) {
            strat = sCache.get(authority);
            if (strat == null) {
                try {
                    strat = parsePathStrategy(context, authority);
                    sCache.put(authority, strat);
                } catch (IOException e) {
                    throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", e);
                } catch (XmlPullParserException e2) {
                    throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", e2);
                }
            }
        }
        return strat;
    }

    private static PathStrategy parsePathStrategy(Context context, String authority) throws IOException, XmlPullParserException {
        SimplePathStrategy strat = new SimplePathStrategy(authority);
        XmlResourceParser in = context.getPackageManager().resolveContentProvider(authority, 128).loadXmlMetaData(context.getPackageManager(), META_DATA_FILE_PROVIDER_PATHS);
        if (in == null) {
            throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
        }
        while (true) {
            int type = in.next();
            if (type == 1) {
                return strat;
            }
            if (type == 2) {
                String tag = in.getName();
                String name = in.getAttributeValue((String) null, "name");
                String path = in.getAttributeValue((String) null, "path");
                File target = null;
                if (TAG_ROOT_PATH.equals(tag)) {
                    target = DEVICE_ROOT;
                } else if (TAG_FILES_PATH.equals(tag)) {
                    target = context.getFilesDir();
                } else if (TAG_CACHE_PATH.equals(tag)) {
                    target = context.getCacheDir();
                } else if (TAG_EXTERNAL.equals(tag)) {
                    target = Environment.getExternalStorageDirectory();
                } else if (TAG_EXTERNAL_FILES.equals(tag)) {
                    File[] externalFilesDirs = ContextCompat.getExternalFilesDirs(context, (String) null);
                    if (externalFilesDirs.length > 0) {
                        target = externalFilesDirs[0];
                    }
                } else if (TAG_EXTERNAL_CACHE.equals(tag)) {
                    File[] externalCacheDirs = ContextCompat.getExternalCacheDirs(context);
                    if (externalCacheDirs.length > 0) {
                        target = externalCacheDirs[0];
                    }
                }
                if (target != null) {
                    strat.addRoot(name, buildPath(target, path));
                }
            }
        }
    }

    static class SimplePathStrategy implements PathStrategy {
        private final String mAuthority;
        private final HashMap<String, File> mRoots = new HashMap<>();

        public SimplePathStrategy(String authority) {
            this.mAuthority = authority;
        }

        public void addRoot(String name, File root) {
            if (TextUtils.isEmpty(name)) {
                throw new IllegalArgumentException("Name must not be empty");
            }
            try {
                this.mRoots.put(name, root.getCanonicalFile());
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to resolve canonical path for " + root, e);
            }
        }

        public Uri getUriForFile(File file) {
            String path;
            try {
                String path2 = file.getCanonicalPath();
                Map.Entry<String, File> mostSpecific = null;
                for (Map.Entry<String, File> root : this.mRoots.entrySet()) {
                    String rootPath = root.getValue().getPath();
                    if (path2.startsWith(rootPath) && (mostSpecific == null || rootPath.length() > mostSpecific.getValue().getPath().length())) {
                        mostSpecific = root;
                    }
                }
                if (mostSpecific == null) {
                    throw new IllegalArgumentException("Failed to find configured root that contains " + path2);
                }
                String rootPath2 = mostSpecific.getValue().getPath();
                if (rootPath2.endsWith(WVNativeCallbackUtil.SEPERATER)) {
                    path = path2.substring(rootPath2.length());
                } else {
                    path = path2.substring(rootPath2.length() + 1);
                }
                return new Uri.Builder().scheme("content").authority(this.mAuthority).encodedPath(Uri.encode(mostSpecific.getKey()) + '/' + Uri.encode(path, WVNativeCallbackUtil.SEPERATER)).build();
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to resolve canonical path for " + file);
            }
        }

        public File getFileForUri(Uri uri) {
            String path = uri.getEncodedPath();
            int splitIndex = path.indexOf(47, 1);
            String tag = Uri.decode(path.substring(1, splitIndex));
            String path2 = Uri.decode(path.substring(splitIndex + 1));
            File root = this.mRoots.get(tag);
            if (root == null) {
                throw new IllegalArgumentException("Unable to find configured root for " + uri);
            }
            File file = new File(root, path2);
            try {
                File file2 = file.getCanonicalFile();
                if (file2.getPath().startsWith(root.getPath())) {
                    return file2;
                }
                throw new SecurityException("Resolved path jumped beyond configured root");
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to resolve canonical path for " + file);
            }
        }
    }

    private static int modeToMode(String mode) {
        if (UploadQueueMgr.MSGTYPE_REALTIME.equals(mode)) {
            return 268435456;
        }
        if ("w".equals(mode) || "wt".equals(mode)) {
            return 738197504;
        }
        if ("wa".equals(mode)) {
            return 704643072;
        }
        if ("rw".equals(mode)) {
            return 939524096;
        }
        if ("rwt".equals(mode)) {
            return 1006632960;
        }
        throw new IllegalArgumentException("Invalid mode: " + mode);
    }

    private static File buildPath(File base, String... segments) {
        File cur;
        int length = segments.length;
        int i = 0;
        File cur2 = base;
        while (i < length) {
            String segment = segments[i];
            if (segment != null) {
                cur = new File(cur2, segment);
            } else {
                cur = cur2;
            }
            i++;
            cur2 = cur;
        }
        return cur2;
    }

    private static String[] copyOf(String[] original, int newLength) {
        String[] result = new String[newLength];
        System.arraycopy(original, 0, result, 0, newLength);
        return result;
    }

    private static Object[] copyOf(Object[] original, int newLength) {
        Object[] result = new Object[newLength];
        System.arraycopy(original, 0, result, 0, newLength);
        return result;
    }
}
