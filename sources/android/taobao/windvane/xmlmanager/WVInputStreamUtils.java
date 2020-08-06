package android.taobao.windvane.xmlmanager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.codec.CharEncoding;

public class WVInputStreamUtils {
    static final int BUFFER_SIZE = 4096;

    public static String InputStreamTOString(InputStream in) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        while (true) {
            int count = in.read(data, 0, 4096);
            if (count == -1) {
                return new String(outStream.toByteArray(), CharEncoding.ISO_8859_1);
            }
            outStream.write(data, 0, count);
        }
    }

    public static String InputStreamTOString(InputStream in, String encoding) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        while (true) {
            int count = in.read(data, 0, 4096);
            if (count == -1) {
                return new String(outStream.toByteArray(), CharEncoding.ISO_8859_1);
            }
            outStream.write(data, 0, count);
        }
    }

    public static InputStream StringTOInputStream(String in) throws Exception {
        return new ByteArrayInputStream(in.getBytes(CharEncoding.ISO_8859_1));
    }

    public static byte[] InputStreamTOByte(InputStream in) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        while (true) {
            int count = in.read(data, 0, 4096);
            if (count == -1) {
                return outStream.toByteArray();
            }
            outStream.write(data, 0, count);
        }
    }

    public static InputStream byteTOInputStream(byte[] in) throws Exception {
        return new ByteArrayInputStream(in);
    }

    public static String byteTOString(byte[] in) throws Exception {
        return InputStreamTOString(byteTOInputStream(in));
    }
}
