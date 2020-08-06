package com.taobao.ju.track.csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CsvFileUtil {
    private static final char CHAR = ',';
    private static final Charset CHARSET = Charset.forName("UTF-8");
    private static final String FILE_EXTENSIONS = ".*.csv";

    public static boolean isCSV(File file) {
        return file != null && isCSV(file.getName());
    }

    public static boolean isCSV(String filename) {
        return filename != null && filename.matches(FILE_EXTENSIONS);
    }

    public static String[] readHeaders(String filename) {
        if (filename == null) {
            return null;
        }
        return readHeaders(new File(filename));
    }

    public static String[] readHeaders(File file) {
        return readHeaders(file, CHARSET);
    }

    public static String[] readHeaders(String filename, Charset charset) {
        if (filename == null) {
            return null;
        }
        return readHeaders(new File(filename), charset);
    }

    public static String[] readHeaders(File file, Charset charset) {
        if (file != null && file.exists()) {
            try {
                return readHeaders((InputStream) new FileInputStream(file), charset);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String[] readHeaders(InputStream inputStream) {
        return readHeaders(inputStream, CHARSET);
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0025  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String[] readHeaders(java.io.InputStream r4, java.nio.charset.Charset r5) {
        /*
            if (r4 == 0) goto L_0x0020
            r1 = 0
            com.taobao.ju.track.csv.CsvReader r2 = new com.taobao.ju.track.csv.CsvReader     // Catch:{ Exception -> 0x0017 }
            r3 = 44
            r2.<init>((java.io.InputStream) r4, (char) r3, (java.nio.charset.Charset) r5)     // Catch:{ Exception -> 0x0017 }
            r2.readHeaders()     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            java.lang.String[] r3 = r2.getHeaders()     // Catch:{ Exception -> 0x002c, all -> 0x0029 }
            if (r2 == 0) goto L_0x0016
            r2.close()
        L_0x0016:
            return r3
        L_0x0017:
            r0 = move-exception
        L_0x0018:
            r0.printStackTrace()     // Catch:{ all -> 0x0022 }
            if (r1 == 0) goto L_0x0020
            r1.close()
        L_0x0020:
            r3 = 0
            goto L_0x0016
        L_0x0022:
            r3 = move-exception
        L_0x0023:
            if (r1 == 0) goto L_0x0028
            r1.close()
        L_0x0028:
            throw r3
        L_0x0029:
            r3 = move-exception
            r1 = r2
            goto L_0x0023
        L_0x002c:
            r0 = move-exception
            r1 = r2
            goto L_0x0018
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.ju.track.csv.CsvFileUtil.readHeaders(java.io.InputStream, java.nio.charset.Charset):java.lang.String[]");
    }

    public static List<String[]> read(String filename) {
        if (filename == null) {
            return null;
        }
        return read(new File(filename));
    }

    public static List<String[]> read(File file) {
        return read(file, CHARSET);
    }

    public static List<String[]> read(String filename, Charset charset) {
        if (filename == null) {
            return null;
        }
        return read(new File(filename), charset);
    }

    public static List<String[]> read(File file, Charset charset) {
        if (file != null && file.exists()) {
            try {
                return read((InputStream) new FileInputStream(file), charset);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static List<String[]> read(InputStream inputStream) {
        return read(inputStream, CHARSET);
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0027  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0035  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.List<java.lang.String[]> read(java.io.InputStream r5, java.nio.charset.Charset r6) {
        /*
            if (r5 == 0) goto L_0x002a
            r2 = 0
            com.taobao.ju.track.csv.CsvReader r3 = new com.taobao.ju.track.csv.CsvReader     // Catch:{ Exception -> 0x003c }
            r4 = 44
            r3.<init>((java.io.InputStream) r5, (char) r4, (java.nio.charset.Charset) r6)     // Catch:{ Exception -> 0x003c }
            r3.readHeaders()     // Catch:{ Exception -> 0x0020, all -> 0x0039 }
            java.util.ArrayList r0 = new java.util.ArrayList     // Catch:{ Exception -> 0x0020, all -> 0x0039 }
            r0.<init>()     // Catch:{ Exception -> 0x0020, all -> 0x0039 }
        L_0x0012:
            boolean r4 = r3.readRecord()     // Catch:{ Exception -> 0x0020, all -> 0x0039 }
            if (r4 == 0) goto L_0x002c
            java.lang.String[] r4 = r3.getValues()     // Catch:{ Exception -> 0x0020, all -> 0x0039 }
            r0.add(r4)     // Catch:{ Exception -> 0x0020, all -> 0x0039 }
            goto L_0x0012
        L_0x0020:
            r1 = move-exception
            r2 = r3
        L_0x0022:
            r1.printStackTrace()     // Catch:{ all -> 0x0032 }
            if (r2 == 0) goto L_0x002a
            r2.close()
        L_0x002a:
            r0 = 0
        L_0x002b:
            return r0
        L_0x002c:
            if (r3 == 0) goto L_0x002b
            r3.close()
            goto L_0x002b
        L_0x0032:
            r4 = move-exception
        L_0x0033:
            if (r2 == 0) goto L_0x0038
            r2.close()
        L_0x0038:
            throw r4
        L_0x0039:
            r4 = move-exception
            r2 = r3
            goto L_0x0033
        L_0x003c:
            r1 = move-exception
            goto L_0x0022
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.ju.track.csv.CsvFileUtil.read(java.io.InputStream, java.nio.charset.Charset):java.util.List");
    }

    public static String[][] readAsArray(String filename) {
        if (filename == null) {
            return null;
        }
        return readAsArray(new File(filename));
    }

    public static String[][] readAsArray(File file) {
        return readAsArray(file, CHARSET);
    }

    public static String[][] readAsArray(String filename, Charset charset) {
        if (filename == null) {
            return null;
        }
        return readAsArray(new File(filename), charset);
    }

    public static String[][] readAsArray(File file, Charset charset) {
        List<String[]> resultList = read(file, charset);
        if (resultList == null || resultList.size() <= 0 || resultList.get(0).length <= 0) {
            return null;
        }
        String[][] result = (String[][]) Array.newInstance(String.class, new int[]{resultList.size(), resultList.get(0).length});
        resultList.toArray(result);
        return result;
    }

    public static File write(File file, List<Object> data) {
        return write(file, CHARSET, data);
    }

    public static File write(File file, Charset charset, List<Object> data) {
        if (file != null) {
            write(file.getPath(), charset, (List<?>) data);
        }
        return file;
    }

    public static File write(String filepath, List<Object> data) {
        return write(filepath, CHARSET, (List<?>) data);
    }

    public static File write(String filepath, Charset charset, List<?> data) {
        File file = null;
        try {
            file = createFile(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!(file == null || filepath == null || data == null)) {
            CsvWriter csvWriter = new CsvWriter(filepath, ',', charset);
            try {
                if (data.size() <= 0 || !(data.get(0) instanceof Map)) {
                    for (Object row : data) {
                        if (row instanceof String[]) {
                            csvWriter.writeRecord((String[]) row);
                        } else {
                            csvWriter.write(String.valueOf(row));
                        }
                    }
                } else {
                    Set keySet = ((Map) data.get(0)).keySet();
                    int colSize = keySet.size();
                    String[] titles = new String[colSize];
                    keySet.toArray(titles);
                    List<String[]> newData = new ArrayList<>();
                    newData.add(titles);
                    Iterator i$ = data.iterator();
                    while (i$.hasNext()) {
                        String[] row2 = new String[colSize];
                        Map map = (Map) i$.next();
                        for (int i = 0; i < colSize; i++) {
                            row2[i] = (String) map.get(titles[i]);
                        }
                        newData.add(row2);
                    }
                    write(filepath, charset, (List<?>) newData);
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            } finally {
                csvWriter.close();
            }
        }
        return file;
    }

    private static File createFile(String filepath) throws IOException {
        File f = new File(filepath);
        if (!f.exists()) {
            File parent = f.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }
            f.createNewFile();
        }
        return f;
    }
}
