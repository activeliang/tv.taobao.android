package android.taobao.atlas.startup.patch;

import android.taobao.atlas.util.FileUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;

public class NativePatchVerifier implements PatchVerifier {
    private Map<String, String> map;

    public NativePatchVerifier(File patchInfo) throws IOException {
        this((InputStream) new FileInputStream(patchInfo));
    }

    public NativePatchVerifier(InputStream patchInfo) throws IOException {
        this.map = new HashMap();
        read(patchInfo);
    }

    public boolean verify(File mergeFile) {
        if (!this.map.containsKey(mergeFile.getName())) {
            return true;
        }
        try {
            return this.map.get(mergeFile.getName()).equals(FileUtils.getMd5ByFile(mergeFile));
        } catch (IOException e) {
            return false;
        }
    }

    public void read(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                return;
            }
            if (line.contains(SymbolExpUtil.SYMBOL_COLON)) {
                this.map.put(line.split(SymbolExpUtil.SYMBOL_COLON)[0], line.split(SymbolExpUtil.SYMBOL_COLON)[1]);
            }
        }
    }
}
