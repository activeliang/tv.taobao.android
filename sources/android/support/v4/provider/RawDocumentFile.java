package android.support.v4.provider;

import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class RawDocumentFile extends DocumentFile {
    private File mFile;

    RawDocumentFile(DocumentFile parent, File file) {
        super(parent);
        this.mFile = file;
    }

    public DocumentFile createFile(String mimeType, String displayName) {
        String extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
        if (extension != null) {
            displayName = displayName + "." + extension;
        }
        File target = new File(this.mFile, displayName);
        try {
            target.createNewFile();
            return new RawDocumentFile(this, target);
        } catch (IOException e) {
            Log.w("DocumentFile", "Failed to createFile: " + e);
            return null;
        }
    }

    public DocumentFile createDirectory(String displayName) {
        File target = new File(this.mFile, displayName);
        if (target.isDirectory() || target.mkdir()) {
            return new RawDocumentFile(this, target);
        }
        return null;
    }

    public Uri getUri() {
        return Uri.fromFile(this.mFile);
    }

    public String getName() {
        return this.mFile.getName();
    }

    public String getType() {
        if (this.mFile.isDirectory()) {
            return null;
        }
        return getTypeForName(this.mFile.getName());
    }

    public boolean isDirectory() {
        return this.mFile.isDirectory();
    }

    public boolean isFile() {
        return this.mFile.isFile();
    }

    public boolean isVirtual() {
        return false;
    }

    public long lastModified() {
        return this.mFile.lastModified();
    }

    public long length() {
        return this.mFile.length();
    }

    public boolean canRead() {
        return this.mFile.canRead();
    }

    public boolean canWrite() {
        return this.mFile.canWrite();
    }

    public boolean delete() {
        deleteContents(this.mFile);
        return this.mFile.delete();
    }

    public boolean exists() {
        return this.mFile.exists();
    }

    public DocumentFile[] listFiles() {
        ArrayList<DocumentFile> results = new ArrayList<>();
        File[] files = this.mFile.listFiles();
        if (files != null) {
            for (File file : files) {
                results.add(new RawDocumentFile(this, file));
            }
        }
        return (DocumentFile[]) results.toArray(new DocumentFile[results.size()]);
    }

    public boolean renameTo(String displayName) {
        File target = new File(this.mFile.getParentFile(), displayName);
        if (!this.mFile.renameTo(target)) {
            return false;
        }
        this.mFile = target;
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0008, code lost:
        r2 = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(r4.substring(r1 + 1).toLowerCase());
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String getTypeForName(java.lang.String r4) {
        /*
            r3 = 46
            int r1 = r4.lastIndexOf(r3)
            if (r1 < 0) goto L_0x001d
            int r3 = r1 + 1
            java.lang.String r3 = r4.substring(r3)
            java.lang.String r0 = r3.toLowerCase()
            android.webkit.MimeTypeMap r3 = android.webkit.MimeTypeMap.getSingleton()
            java.lang.String r2 = r3.getMimeTypeFromExtension(r0)
            if (r2 == 0) goto L_0x001d
        L_0x001c:
            return r2
        L_0x001d:
            java.lang.String r2 = "application/octet-stream"
            goto L_0x001c
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.provider.RawDocumentFile.getTypeForName(java.lang.String):java.lang.String");
    }

    private static boolean deleteContents(File dir) {
        File[] files = dir.listFiles();
        boolean success = true;
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    success &= deleteContents(file);
                }
                if (!file.delete()) {
                    Log.w("DocumentFile", "Failed to delete " + file);
                    success = false;
                }
            }
        }
        return success;
    }
}
