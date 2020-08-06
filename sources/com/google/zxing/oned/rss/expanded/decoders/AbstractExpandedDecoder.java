package com.google.zxing.oned.rss.expanded.decoders;

import android.taobao.windvane.monitor.WVMonitorConstants;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

public abstract class AbstractExpandedDecoder {
    private final GeneralAppIdDecoder generalDecoder;
    private final BitArray information;

    public abstract String parseInformation() throws NotFoundException, FormatException;

    AbstractExpandedDecoder(BitArray information2) {
        this.information = information2;
        this.generalDecoder = new GeneralAppIdDecoder(information2);
    }

    /* access modifiers changed from: protected */
    public final BitArray getInformation() {
        return this.information;
    }

    /* access modifiers changed from: protected */
    public final GeneralAppIdDecoder getGeneralDecoder() {
        return this.generalDecoder;
    }

    public static AbstractExpandedDecoder createDecoder(BitArray information2) {
        if (information2.get(1)) {
            return new AI01AndOtherAIs(information2);
        }
        if (!information2.get(2)) {
            return new AnyAIDecoder(information2);
        }
        switch (GeneralAppIdDecoder.extractNumericValueFromBitArray(information2, 1, 4)) {
            case 4:
                return new AI013103decoder(information2);
            case 5:
                return new AI01320xDecoder(information2);
            default:
                switch (GeneralAppIdDecoder.extractNumericValueFromBitArray(information2, 1, 5)) {
                    case 12:
                        return new AI01392xDecoder(information2);
                    case 13:
                        return new AI01393xDecoder(information2);
                    default:
                        switch (GeneralAppIdDecoder.extractNumericValueFromBitArray(information2, 1, 7)) {
                            case 56:
                                return new AI013x0x1xDecoder(information2, "310", "11");
                            case 57:
                                return new AI013x0x1xDecoder(information2, "320", "11");
                            case 58:
                                return new AI013x0x1xDecoder(information2, "310", "13");
                            case 59:
                                return new AI013x0x1xDecoder(information2, "320", "13");
                            case 60:
                                return new AI013x0x1xDecoder(information2, "310", WVMonitorConstants.FORCE_ONLINE_FAILED);
                            case 61:
                                return new AI013x0x1xDecoder(information2, "320", WVMonitorConstants.FORCE_ONLINE_FAILED);
                            case 62:
                                return new AI013x0x1xDecoder(information2, "310", WVMonitorConstants.MAPPING_URL_MATCH_FAILED);
                            case 63:
                                return new AI013x0x1xDecoder(information2, "320", WVMonitorConstants.MAPPING_URL_MATCH_FAILED);
                            default:
                                throw new IllegalStateException("unknown decoder: " + information2);
                        }
                }
        }
    }
}
