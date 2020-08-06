package com.yunos.tv.blitz.video;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {
    public static final int IO_BUFFER_SIZE = 16384;

    public static String readString(InputStream is) throws IOException {
        return new String(readBytes(is));
    }

    public static String readString(BufferedInputStream is) throws IOException {
        return new String(readBytes(is));
    }

    public static byte[] readBytes(InputStream is) throws IOException {
        byte[] bArr = new byte[0];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[16384];
        while (true) {
            try {
                int len = is.read(buffer);
                if (len == -1) {
                    break;
                }
                baos.write(buffer, 0, len);
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            } catch (Throwable th) {
                if (baos != null) {
                    try {
                        baos.close();
                    } catch (Exception e2) {
                        throw th;
                    }
                }
                if (is != null) {
                    is.close();
                }
                throw th;
            }
        }
        byte[] result = baos.toByteArray();
        if (baos != null) {
            try {
                baos.close();
            } catch (Exception e3) {
            }
        }
        if (is != null) {
            is.close();
        }
        return result;
    }

    public static byte[] readBytes(BufferedInputStream is) throws IOException {
        byte[] bArr = new byte[0];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[16384];
        while (true) {
            try {
                int len = is.read(buffer);
                if (len == -1) {
                    break;
                }
                baos.write(buffer, 0, len);
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            } catch (Throwable th) {
                if (baos != null) {
                    try {
                        baos.close();
                    } catch (Exception e2) {
                        throw th;
                    }
                }
                if (is != null) {
                    is.close();
                }
                throw th;
            }
        }
        byte[] result = baos.toByteArray();
        if (baos != null) {
            try {
                baos.close();
            } catch (Exception e3) {
            }
        }
        if (is != null) {
            is.close();
        }
        return result;
    }

    public static void writeToLocal(InputStream is, String localPath) throws IOException {
        File file = new File(localPath);
        if (!file.exists()) {
            File p = file.getParentFile();
            if (!p.exists()) {
                p.mkdirs();
            }
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buff = new byte[16384];
        while (true) {
            try {
                int len = is.read(buff);
                if (len == -1) {
                    break;
                }
                fos.write(buff, 0, len);
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            } catch (Throwable th) {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (Exception e2) {
                        throw th;
                    }
                }
                if (is != null) {
                    is.close();
                }
                throw th;
            }
        }
        if (fos != null) {
            try {
                fos.close();
            } catch (Exception e3) {
                return;
            }
        }
        if (is != null) {
            is.close();
        }
    }
}
