package android.taobao.atlas.startup.patch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public abstract class PatchMerger {
    protected List<Future<Boolean>> futures = new ArrayList();
    protected PatchVerifier patchVerifier;
    protected ExecutorService service = null;

    public abstract boolean merge(File file, File file2, File file3);

    public PatchMerger(PatchVerifier patchVerifier2) {
        this.patchVerifier = patchVerifier2;
        this.service = Executors.newFixedThreadPool(3);
    }

    public void sumitForMerge(final File sourceFile, final File patchFile, final File newFile) {
        this.futures.add(this.service.submit(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return Boolean.valueOf(PatchMerger.this.merge(sourceFile, patchFile, newFile));
            }
        }));
    }

    public boolean waitForResult() {
        for (Future future : this.futures) {
            try {
                if (!((Boolean) future.get(5000, TimeUnit.SECONDS)).booleanValue()) {
                    return false;
                }
            } catch (Throwable e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
