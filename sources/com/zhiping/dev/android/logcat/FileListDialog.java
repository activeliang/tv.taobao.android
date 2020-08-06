package com.zhiping.dev.android.logcat;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.zhiping.dev.android.oss.BaseIClient;
import com.zhiping.dev.android.oss.IClient;
import java.io.File;
import java.util.List;
import org.apache.commons.codec.binary.Base32;

class FileListDialog extends Dialog {
    private static final String TAG = FileListDialog.class.getSimpleName();
    /* access modifiers changed from: private */
    public LinearLayout linearLayout;
    LogcatConfig logcatConfig;

    public FileListDialog(Context context, LogcatConfig logcatConfig2) {
        super(context);
        this.logcatConfig = logcatConfig2;
        ScrollView scrollView = new ScrollView(context);
        this.linearLayout = new LinearLayout(context);
        this.linearLayout.setOrientation(1);
        scrollView.addView(this.linearLayout);
        setContentView(scrollView);
        inflateDialog();
    }

    /* access modifiers changed from: private */
    public void inflateDialog() {
        this.linearLayout.removeAllViews();
        List<File> logFiles = this.logcatConfig.listLogs();
        int i = 0;
        while (logFiles != null && i < logFiles.size()) {
            File file = logFiles.get(i);
            Log.d(TAG, "listFiles :" + file.getAbsolutePath());
            this.linearLayout.addView(new LogItemVH(file).itemView, new LinearLayout.LayoutParams(-1, -2));
            i++;
        }
    }

    private class LogItemVH {
        Button del = ((Button) this.itemView.findViewById(R.id.del));
        File file;
        boolean isExistOnOSS;
        View itemView;
        TextView txt = ((TextView) this.itemView.findViewById(R.id.txt));
        Button upload = ((Button) this.itemView.findViewById(R.id.upload));

        public LogItemVH(File data) {
            this.file = data;
            this.itemView = LayoutInflater.from(FileListDialog.this.getContext()).inflate(R.layout.log_item, FileListDialog.this.linearLayout, false);
            this.itemView.setTag(this);
            FileListDialog.this.logcatConfig.getOssClient().isObjExist(FileListDialog.this.logcatConfig.getBucketName(), FileListDialog.this.logcatConfig.getObjectKey(this.file), new BaseIClient.BaseIClientCallBack(FileListDialog.this) {
                public void onSuccess(Object... objects) {
                    super.onSuccess(objects);
                    if (objects != null && objects.length > 0 && (objects[0] instanceof Boolean) && objects[0].booleanValue()) {
                        LogItemVH.this.isExistOnOSS = true;
                    }
                    LogItemVH.this.inflateItem();
                }

                public void onFailure(Object... objects) {
                    LogItemVH.this.inflateItem();
                }
            });
        }

        /* access modifiers changed from: private */
        public void inflateItem() {
            this.txt.setMaxLines(1);
            this.txt.setPadding(10, 10, 10, 10);
            this.txt.setEllipsize(TextUtils.TruncateAt.MIDDLE);
            this.txt.setMaxWidth(850);
            String showName = this.file.getName();
            if (showName != null) {
                String[] parts = showName.split("_");
                if (parts.length >= 2) {
                    try {
                        String pA = parts[0];
                        if (new Base32().isInAlphabet(parts[1])) {
                            showName = pA + "_" + new String(new Base32().decode(parts[1]));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            this.txt.setText(showName + "(文件大小：" + LogcatConfig.getFitSize(this.file.length()) + ")");
            this.del.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    LogItemVH.this.file.delete();
                    FileListDialog.this.inflateDialog();
                }
            });
            if (!this.isExistOnOSS) {
                this.upload.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        FileListDialog.this.logcatConfig.appendExtInfo(LogItemVH.this.file, new String[0]);
                        FileListDialog.this.logcatConfig.getOssClient().put(LogItemVH.this.file, FileListDialog.this.logcatConfig.getBucketName(), FileListDialog.this.logcatConfig.getObjectKey(LogItemVH.this.file), (IClient.CallBack) new BaseIClient.BaseIClientCallBack() {
                            public void onSuccess(Object... objects) {
                                Toast.makeText(FileListDialog.this.getContext(), "上传成功！", 0).show();
                                LogItemVH.this.upload.setEnabled(false);
                                LogItemVH.this.upload.setText("已上传！");
                            }

                            public void onProgress(Object... objects) {
                            }

                            public void onFailure(Object... objects) {
                                Toast.makeText(FileListDialog.this.getContext(), "上传失败！", 0).show();
                            }
                        });
                    }
                });
                return;
            }
            this.upload.setEnabled(false);
            this.upload.setText("已上传！");
        }
    }
}
