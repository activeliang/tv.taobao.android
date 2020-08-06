package android.taobao.windvane.extra.uc;

import android.os.Build;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class WVUCUtils {
    private static final String TAG = "WVUCUtils";
    private static String sAbi;
    private static String sAbi2;
    private static String[] sAbiList;
    private static String sArch;
    private static String sCpuAbi;
    private static String[] sSupportedABIs;

    public static boolean isArchContains(String keyword) {
        String abilist;
        if (sArch == null) {
            sArch = System.getProperty("os.arch");
        }
        if (sArch != null && sArch.toLowerCase().contains(keyword)) {
            return true;
        }
        if (sAbi == null) {
            try {
                sAbi = Build.CPU_ABI;
                sAbi2 = Build.CPU_ABI2;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (sAbi != null && sAbi.toLowerCase().contains(keyword)) {
            return true;
        }
        if (sSupportedABIs == null && Build.VERSION.SDK_INT >= 21) {
            try {
                sSupportedABIs = (String[]) Build.class.getField("SUPPORTED_ABIS").get((Object) null);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        if (sSupportedABIs != null && sSupportedABIs.length > 0 && sSupportedABIs[0] != null && sSupportedABIs[0].toLowerCase().contains(keyword)) {
            return true;
        }
        if (sCpuAbi == null) {
            sCpuAbi = getFromSystemProp("ro.product.cpu.abi");
        }
        if (sCpuAbi != null && sCpuAbi.toLowerCase().contains(keyword)) {
            return true;
        }
        if (!(sAbiList != null || (abilist = getFromSystemProp("ro.product.cpu.abilist")) == null || abilist.length() == 0)) {
            sAbiList = abilist.split(",");
        }
        if (sAbiList == null || sAbiList.length <= 0 || sAbiList[0] == null || !sAbiList[0].toLowerCase().contains(keyword)) {
            return false;
        }
        return true;
    }

    private static String getFromSystemProp(String key) {
        FileInputStream propFIS = null;
        InputStreamReader propISR = null;
        BufferedReader propBR = null;
        try {
            FileInputStream propFIS2 = new FileInputStream(new File("/system/build.prop"));
            try {
                InputStreamReader propISR2 = new InputStreamReader(propFIS2);
                try {
                    BufferedReader propBR2 = new BufferedReader(propISR2);
                    while (true) {
                        try {
                            String line = propBR2.readLine();
                            if (line == null) {
                                close(propBR2);
                                close(propISR2);
                                close(propFIS2);
                                BufferedReader bufferedReader = propBR2;
                                InputStreamReader inputStreamReader = propISR2;
                                FileInputStream fileInputStream = propFIS2;
                                break;
                            } else if (line.contains(key)) {
                                String[] cols = line.split("=");
                                if (cols.length == 2 && cols[0].trim().equals(key)) {
                                    String trim = cols[1].trim();
                                    close(propBR2);
                                    close(propISR2);
                                    close(propFIS2);
                                    BufferedReader bufferedReader2 = propBR2;
                                    InputStreamReader inputStreamReader2 = propISR2;
                                    FileInputStream fileInputStream2 = propFIS2;
                                    return trim;
                                }
                            }
                        } catch (Throwable th) {
                            th = th;
                            propBR = propBR2;
                            propISR = propISR2;
                            propFIS = propFIS2;
                            close(propBR);
                            close(propISR);
                            close(propFIS);
                            throw th;
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    propISR = propISR2;
                    propFIS = propFIS2;
                    close(propBR);
                    close(propISR);
                    close(propFIS);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                propFIS = propFIS2;
                close(propBR);
                close(propISR);
                close(propFIS);
                throw th;
            }
        } catch (Throwable th4) {
            e = th4;
            e.printStackTrace();
            close(propBR);
            close(propISR);
            close(propFIS);
            return null;
        }
        return null;
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
