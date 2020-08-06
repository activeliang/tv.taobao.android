package android.taobao.atlas.startup.patch;

import android.util.Log;
import com.alibaba.patch.PatchUtils;
import java.io.File;

public class NativePatchMerger extends PatchMerger {
    private static final String TAG = "NativePatchMerger";

    public NativePatchMerger(PatchVerifier patchVerifier) {
        super(patchVerifier);
    }

    public boolean merge(File sourceFile, File patchFile, File newFile) {
        if (sourceFile == null || !sourceFile.exists()) {
            Log.e(TAG, "sourceFile  == null || source File is not exists!");
            return false;
        } else if (patchFile == null || !patchFile.exists()) {
            Log.e(TAG, "patchFile == null || patchFile is not exist!");
            return false;
        } else if (newFile == null) {
            Log.e(TAG, "newFile == null");
            return false;
        } else {
            if (newFile.exists()) {
                if (this.patchVerifier != null && this.patchVerifier.verify(newFile)) {
                    return true;
                }
                newFile.delete();
            }
            long start = System.currentTimeMillis();
            int result = PatchUtils.applyPatch(sourceFile.getAbsolutePath(), newFile.getAbsolutePath(), patchFile.getAbsolutePath());
            Log.e("patchMerger", "merge so-->" + newFile.getAbsolutePath() + " cost:" + String.valueOf(System.currentTimeMillis() - start));
            if (result == 0 && newFile.exists()) {
                if (this.patchVerifier == null) {
                    return true;
                }
                if (this.patchVerifier != null && this.patchVerifier.verify(newFile)) {
                    return true;
                }
                newFile.delete();
            }
            if (sourceFile.canWrite()) {
                sourceFile.delete();
            }
            patchFile.delete();
            return false;
        }
    }
}
