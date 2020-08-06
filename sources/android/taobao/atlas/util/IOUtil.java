package android.taobao.atlas.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipFile;

public class IOUtil {
    public static void quietClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable th) {
            }
        }
    }

    public static void quietClose(ZipFile zip) {
        if (zip != null) {
            try {
                zip.close();
            } catch (Throwable th) {
            }
        }
    }

    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        try {
            byte[] by = new byte[1024];
            while (true) {
                int c = in.read(by);
                if (c != -1) {
                    out.write(by, 0, c);
                } else {
                    out.flush();
                    quietClose((Closeable) out);
                    quietClose((Closeable) in);
                    return;
                }
            }
        } catch (IOException e) {
            throw e;
        } catch (Throwable th) {
            quietClose((Closeable) out);
            quietClose((Closeable) in);
            throw th;
        }
    }
}
