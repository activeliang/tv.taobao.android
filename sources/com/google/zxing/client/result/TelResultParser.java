package com.google.zxing.client.result;

import android.taobao.windvane.extra.uc.WVUCWebViewClient;
import com.google.zxing.Result;

public final class TelResultParser extends ResultParser {
    public TelParsedResult parse(Result result) {
        String telURI;
        String rawText = getMassagedText(result);
        if (!rawText.startsWith(WVUCWebViewClient.SCHEME_TEL) && !rawText.startsWith("TEL:")) {
            return null;
        }
        if (rawText.startsWith("TEL:")) {
            telURI = WVUCWebViewClient.SCHEME_TEL + rawText.substring(4);
        } else {
            telURI = rawText;
        }
        int queryStart = rawText.indexOf(63, 4);
        return new TelParsedResult(queryStart < 0 ? rawText.substring(4) : rawText.substring(4, queryStart), telURI, (String) null);
    }
}
