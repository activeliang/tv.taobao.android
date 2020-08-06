package com.tvtaobao.voicesdk.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tvtaobao.voicesdk.R;
import com.tvtaobao.voicesdk.dialog.base.BaseDialog;
import com.tvtaobao.voicesdk.utils.DialogManager;
import com.tvtaobao.voicesdk.utils.LogPrint;
import com.tvtaobao.voicesdk.view.AutoTextView;
import java.util.List;

public class TipsDialog extends BaseDialog {
    private final String TAG = "TipsDialog";
    private RelativeLayout mLayoutTips;
    private AutoTextView mTips;
    private TextView mTts;

    public TipsDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_tips);
        initView();
    }

    public void initView() {
        LogPrint.d("TipsDialog", "TipsDialog.initView");
        this.mTts = (TextView) findViewById(R.id.voice_card_search_tts);
        this.mTips = (AutoTextView) findViewById(R.id.voice_card_search_tips);
        this.mLayoutTips = (RelativeLayout) findViewById(R.id.voice_layout_tips);
    }

    public void show() {
        super.show();
        DialogManager.getManager().pushDialog(this);
        delayDismiss(7000);
    }

    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        dismiss();
        return true;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return true;
    }

    public void setTts(String tts, String spoken) {
        LogPrint.d("TipsDialog", "TipsDialog.setPrompt");
        if (this.mTts != null) {
            this.mTts.setText(tts);
        }
        playTTS(spoken);
    }

    public void setTips(List<String> tips) {
        if (tips == null || tips.size() <= 0 || this.mTips == null) {
            this.mLayoutTips.setVisibility(4);
            return;
        }
        this.mLayoutTips.setVisibility(0);
        this.mTips.autoScroll(tips);
    }
}
