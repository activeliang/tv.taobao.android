package com.tvtaobao.voicesdk.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import com.tvtaobao.voicesdk.ASRNotify;
import com.tvtaobao.voicesdk.R;
import com.tvtaobao.voicesdk.utils.LogPrint;
import java.util.ArrayList;
import java.util.List;

public class TipsActivity extends Activity {
    private final String TAG = "TipsDialog";
    private Context mContext;
    private AutoTextView mTips;
    private TextView mTts;
    private ArrayList<String> tips = new ArrayList<>();
    private View tipsView;
    private String tts;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_tips);
        initView();
    }

    public void initView() {
        LogPrint.d("TipsDialog", "TipsDialog.initView");
        this.mTts = (TextView) findViewById(R.id.voice_card_search_tts);
        this.mTips = (AutoTextView) findViewById(R.id.voice_card_search_tips);
        this.tts = getIntent().getStringExtra("tts");
        this.tips = getIntent().getStringArrayListExtra("tips");
        setTts(this.tts);
        setTips(this.tips);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                TipsActivity.this.finish();
            }
        }, 7000);
    }

    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        finish();
        return super.onKeyDown(keyCode, event);
    }

    public void setTts(String tts2) {
        LogPrint.d("TipsDialog", "TipsDialog.setPrompt");
        if (this.mTts != null) {
            this.mTts.setText(tts2);
        }
        ASRNotify.getInstance().playTTS(tts2);
    }

    public void setTips(List<String> tips2) {
        new StringBuilder();
        if (tips2 != null && tips2.size() > 0 && this.mTips != null) {
            this.mTips.autoScroll(tips2);
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }
}
