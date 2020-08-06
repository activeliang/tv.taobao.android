package com.uc.webview.export.internal;

import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.internal.utility.Log;
import java.io.File;
import java.io.FileOutputStream;

/* compiled from: ProGuard */
final class e extends Thread {
    final /* synthetic */ File a;
    final /* synthetic */ boolean b;
    final /* synthetic */ String c;
    final /* synthetic */ File d;
    final /* synthetic */ File e;
    final /* synthetic */ File f;
    final /* synthetic */ Runnable g;

    e(File file, boolean z, String str, File file2, File file3, File file4, Runnable runnable) {
        this.a = file;
        this.b = z;
        this.c = str;
        this.d = file2;
        this.e = file3;
        this.f = file4;
        this.g = runnable;
    }

    public final void run() {
        FileOutputStream fileOutputStream = null;
        try {
            if (this.a.exists()) {
                for (File delete : this.a.listFiles()) {
                    delete.delete();
                }
            } else {
                this.a.mkdirs();
            }
            StringBuilder sb = new StringBuilder();
            if (this.b) {
                sb.append("<?xml version='1.0' encoding='utf-8' standalone='yes' ?>\n<map />");
            } else {
                sb.append("<?xml version='1.0' encoding='utf-8' standalone='yes' ?>\n<map>\n<string name=\"current_typeface_path\">").append(this.c).append("</string>\n</map>");
            }
            if (!this.d.exists()) {
                this.d.mkdirs();
            }
            FileOutputStream fileOutputStream2 = new FileOutputStream(this.e);
            try {
                fileOutputStream2.write(sb.toString().getBytes());
                this.f.createNewFile();
                d.d.fontDownloadFinished();
                if (this.g != null) {
                    this.g.run();
                }
                UCCyclone.close(fileOutputStream2);
            } catch (Exception e2) {
                e = e2;
                fileOutputStream = fileOutputStream2;
                try {
                    Log.i("tag_test_log", "updateTypefacePath", e);
                    UCCyclone.close(fileOutputStream);
                } catch (Throwable th) {
                    th = th;
                    UCCyclone.close(fileOutputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = fileOutputStream2;
                UCCyclone.close(fileOutputStream);
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
        }
    }
}
