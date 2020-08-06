package com.yunos.tv.alitvasrsdk;

import android.taobao.windvane.monitor.WVMonitorConstants;
import android.text.TextUtils;
import com.alibaba.analytics.core.Constants;

public class AppContextType {

    public enum PageType {
        PAGE_TYPE_DEFAULT("0"),
        PAGE_TYPE_YINGSHI_DEFAULT("1"),
        PAGE_TYPE_MOVIE_DETAIL_PLAY("2"),
        PAGE_TYPE_MOVIE_FULL_PLAY("3"),
        PAGE_TYPE_SERIES_DETAIL_PLAY("4"),
        PAGE_TYPE_SERIES_FULL_PLAY("5"),
        PAGE_TYPE_VERIETY_SHOW_DETAIL_PLAY(Constants.LogTransferLevel.L6),
        PAGE_TYPE_VERIETY_SHOW_FULL_PLAY(Constants.LogTransferLevel.L7);
        
        String pageType;

        private PageType(String pageType2) {
            this.pageType = pageType2;
        }

        public static PageType valueOfPageType(String value) {
            PageType pageType2 = PAGE_TYPE_DEFAULT;
            if (TextUtils.isEmpty(value)) {
                return pageType2;
            }
            if (value.equals("0")) {
                pageType2 = PAGE_TYPE_DEFAULT;
            } else if (value.equals("1")) {
                pageType2 = PAGE_TYPE_YINGSHI_DEFAULT;
            } else if (value.equals("2")) {
                pageType2 = PAGE_TYPE_MOVIE_DETAIL_PLAY;
            } else if (value.equals("3")) {
                pageType2 = PAGE_TYPE_MOVIE_FULL_PLAY;
            } else if (value.equals("4")) {
                pageType2 = PAGE_TYPE_SERIES_DETAIL_PLAY;
            } else if (value.equals("5")) {
                pageType2 = PAGE_TYPE_SERIES_FULL_PLAY;
            } else if (value.equals(Constants.LogTransferLevel.L6)) {
                pageType2 = PAGE_TYPE_VERIETY_SHOW_DETAIL_PLAY;
            } else if (value.equals(Constants.LogTransferLevel.L7)) {
                pageType2 = PAGE_TYPE_VERIETY_SHOW_FULL_PLAY;
            }
            return pageType2;
        }
    }

    public enum PayType {
        WATCH_BY_FREE("watch_by_free"),
        WATCH_BY_VIP("watch_by_vip"),
        BUY_VIP("buy_vip"),
        BUY_SINGLE("buy_single"),
        BUY_VIP_SINGLE("buy_vip_single"),
        BUY_TICKET_SINGLE("buy_ticket_single"),
        BUY_VIP_TICKET_SINGLE("buy_vip_ticket_single");
        
        String type;

        private PayType(String type2) {
            this.type = type2;
        }

        public static PayType valueOfPayType(String value) {
            PayType payType = WATCH_BY_FREE;
            if (TextUtils.isEmpty(value)) {
                return payType;
            }
            if (value.equals("watch_by_free")) {
                payType = WATCH_BY_FREE;
            } else if (value.equals("watch_by_vip")) {
                payType = WATCH_BY_VIP;
            } else if (value.equals("buy_vip")) {
                payType = BUY_VIP;
            } else if (value.equals("buy_single")) {
                payType = BUY_SINGLE;
            } else if (value.equals("buy_vip_single")) {
                payType = BUY_VIP_SINGLE;
            } else if (value.equals("buy_ticket_single")) {
                payType = BUY_TICKET_SINGLE;
            } else if (value.equals("buy_vip_ticket_single")) {
                payType = BUY_VIP_TICKET_SINGLE;
            }
            return payType;
        }
    }

    public enum SceneType {
        ENTER_DETAIL("enter_detail"),
        EXIT_DETAIL("exit_detail"),
        POPUP_BUY_DIALOG_PAUSE("popup_buy_dialog_pause"),
        POPUP_BUY_DIALOG_TAIL("popup_buy_dialog_tail");
        
        String type;

        private SceneType(String type2) {
            this.type = type2;
        }

        public static SceneType valueOfSceneType(String value) {
            SceneType sceneType = ENTER_DETAIL;
            if (TextUtils.isEmpty(value)) {
                return sceneType;
            }
            if (value.equals("enter_detail")) {
                sceneType = ENTER_DETAIL;
            } else if (value.equals("exit_detail")) {
                sceneType = EXIT_DETAIL;
            } else if (value.equals("popup_buy_dialog_pause")) {
                sceneType = POPUP_BUY_DIALOG_PAUSE;
            } else if (value.equals("popup_buy_dialog_tail")) {
                sceneType = POPUP_BUY_DIALOG_TAIL;
            }
            return sceneType;
        }
    }

    public enum SoundPageType {
        SOUND_PAGE_TYPE_NONE("0"),
        SOUND_PAGE_TYPE_NOLY_TITLE("1"),
        SOUND_PAGE_TYPE_RESULT_COMMAND("2"),
        SOUND_PAGE_TYPE_RESULT_YINGSHI("3"),
        SOUND_PAGE_TYPE_RESULT_HISTORY("4"),
        SOUND_PAGE_TYPE_RESULT_FAVOR("5"),
        SOUND_PAGE_TYPE_RESULT_SELECTION(Constants.LogTransferLevel.L6),
        SOUND_PAGE_TYPE_RESULT_DEFINITION(Constants.LogTransferLevel.L7),
        SOUND_PAGE_TYPE_RESULT_RATIO(Constants.LogTransferLevel.HIGH),
        SOUND_PAGE_TYPE_RESULT_WEATHER("9"),
        SOUND_PAGE_TYPE_RESULT_FILM_NEWS("10"),
        SOUND_PAGE_TYPE_RESULT_MEMO("11"),
        SOUND_PAGE_TYPE_RESULT_TAKEOUT_DISH("12"),
        SOUND_PAGE_TYPE_RESULT_TAKEOUT_ORDER("13"),
        SOUND_PAGE_TYPE_RESULT_TAKEOUT_PAY("14"),
        SOUND_PAGE_TYPE_RESULT_TAKEOUT_PAY_RESULT(WVMonitorConstants.FORCE_ONLINE_FAILED);
        
        String soundPageType;

        private SoundPageType(String soundPageType2) {
            this.soundPageType = soundPageType2;
        }

        public static SoundPageType valueOfPageType(String value) {
            SoundPageType soundPageType2 = SOUND_PAGE_TYPE_NONE;
            if (TextUtils.isEmpty(value)) {
                return soundPageType2;
            }
            if (value.equals("0")) {
                soundPageType2 = SOUND_PAGE_TYPE_NONE;
            } else if (value.equals("1")) {
                soundPageType2 = SOUND_PAGE_TYPE_NOLY_TITLE;
            } else if (value.equals("2")) {
                soundPageType2 = SOUND_PAGE_TYPE_RESULT_COMMAND;
            } else if (value.equals("3")) {
                soundPageType2 = SOUND_PAGE_TYPE_RESULT_YINGSHI;
            } else if (value.equals("4")) {
                soundPageType2 = SOUND_PAGE_TYPE_RESULT_HISTORY;
            } else if (value.equals("5")) {
                soundPageType2 = SOUND_PAGE_TYPE_RESULT_FAVOR;
            } else if (value.equals(Constants.LogTransferLevel.L6)) {
                soundPageType2 = SOUND_PAGE_TYPE_RESULT_SELECTION;
            } else if (value.equals(Constants.LogTransferLevel.L7)) {
                soundPageType2 = SOUND_PAGE_TYPE_RESULT_DEFINITION;
            } else if (value.equals(Constants.LogTransferLevel.HIGH)) {
                soundPageType2 = SOUND_PAGE_TYPE_RESULT_RATIO;
            } else if (value.equals("9")) {
                soundPageType2 = SOUND_PAGE_TYPE_RESULT_WEATHER;
            } else if (value.equals("10")) {
                soundPageType2 = SOUND_PAGE_TYPE_RESULT_FILM_NEWS;
            } else if (value.equals("11")) {
                soundPageType2 = SOUND_PAGE_TYPE_RESULT_MEMO;
            } else if (value.equals("12")) {
                soundPageType2 = SOUND_PAGE_TYPE_RESULT_TAKEOUT_DISH;
            } else if (value.equals("13")) {
                soundPageType2 = SOUND_PAGE_TYPE_RESULT_TAKEOUT_ORDER;
            } else if (value.equals("14")) {
                soundPageType2 = SOUND_PAGE_TYPE_RESULT_TAKEOUT_PAY;
            } else if (value.equals(WVMonitorConstants.FORCE_ONLINE_FAILED)) {
                soundPageType2 = SOUND_PAGE_TYPE_RESULT_TAKEOUT_PAY_RESULT;
            }
            return soundPageType2;
        }
    }
}
