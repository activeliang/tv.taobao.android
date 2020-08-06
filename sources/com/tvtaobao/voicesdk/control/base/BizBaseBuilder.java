package com.tvtaobao.voicesdk.control.base;

import com.taobao.ju.track.csv.CsvReader;
import com.tvtaobao.voicesdk.control.BuyIndexControl;
import com.tvtaobao.voicesdk.control.CheckBillControl;
import com.tvtaobao.voicesdk.control.ExitApplicationControl;
import com.tvtaobao.voicesdk.control.FusionSearchControl;
import com.tvtaobao.voicesdk.control.GoodsSearchControl;
import com.tvtaobao.voicesdk.control.LogisticsControl;
import com.tvtaobao.voicesdk.control.OpenIndexControl;
import com.tvtaobao.voicesdk.control.PageIntentControl;
import com.tvtaobao.voicesdk.control.TakeOutAgainControl;
import com.tvtaobao.voicesdk.control.TakeOutProgressControl;
import com.tvtaobao.voicesdk.control.TakeOutSearchControl;
import com.tvtaobao.voicesdk.type.DomainType;

public class BizBaseBuilder {
    public static BizBaseControl builder(String intent) {
        char c = 65535;
        switch (intent.hashCode()) {
            case -1590804987:
                if (intent.equals(DomainType.TAKEOUT_PROGRESS)) {
                    c = 5;
                    break;
                }
                break;
            case -1567198295:
                if (intent.equals(DomainType.TYPE_CHECK_ORDER)) {
                    c = 2;
                    break;
                }
                break;
            case -634325120:
                if (intent.equals(DomainType.TAKEOUT_SEARCH)) {
                    c = 3;
                    break;
                }
                break;
            case -518857506:
                if (intent.equals(DomainType.TYPE_CHECK_BILL)) {
                    c = 1;
                    break;
                }
                break;
            case -452620839:
                if (intent.equals("buy_index")) {
                    c = 8;
                    break;
                }
                break;
            case -94372419:
                if (intent.equals(DomainType.TYPE_SEARCH_FUSION)) {
                    c = 10;
                    break;
                }
                break;
            case -37026168:
                if (intent.equals(DomainType.TAKEOUT_AGAIN)) {
                    c = 4;
                    break;
                }
                break;
            case 206397359:
                if (intent.equals(DomainType.TYPE_EXIT_APPLICATION)) {
                    c = CsvReader.Letters.VERTICAL_TAB;
                    break;
                }
                break;
            case 451772927:
                if (intent.equals(DomainType.TYPE_SEARCH_GOODS)) {
                    c = 0;
                    break;
                }
                break;
            case 681857693:
                if (intent.equals("open_index")) {
                    c = 9;
                    break;
                }
                break;
            case 1234574542:
                if (intent.equals(DomainType.TAKEOUT_GOTO_INDEX)) {
                    c = 6;
                    break;
                }
                break;
            case 1546212196:
                if (intent.equals(DomainType.TYPE_OPEN_PAGE)) {
                    c = 7;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return new GoodsSearchControl();
            case 1:
                return new CheckBillControl();
            case 2:
                return new LogisticsControl();
            case 3:
                return new TakeOutSearchControl();
            case 4:
                return new TakeOutAgainControl();
            case 5:
                return new TakeOutProgressControl();
            case 6:
            case 7:
                return new PageIntentControl();
            case 8:
                return new BuyIndexControl();
            case 9:
                return new OpenIndexControl();
            case 10:
                return new FusionSearchControl();
            case 11:
                return new ExitApplicationControl();
            default:
                return null;
        }
    }
}
