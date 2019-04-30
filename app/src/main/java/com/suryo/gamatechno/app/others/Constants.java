package com.suryo.gamatechno.app.others;

public class Constants {
    static final String TAG = "suryo";

    public static final String baseUrl = "http://167.99.66.123:2727";
    public static final String prefixEmail = "_1";
    public static final String http = "http://";

    public static final String REFRESH_MESSAGE = "refresh_message";

    public static abstract class ColorButton {
        public static final int BLACK = 1;
        public static final int BLUE = 2;
        public static final int RED = 3;
    }

    public static abstract class NotifId {
        public static int FG_NOTIF = 99;
    }

    public static abstract class ReqPendingIntent {
        static final int NOTIF = 3;
        static final int NOTIF_SUB = 0;         // starting (counting)
        static final int NOTIF_REPLY = 1000;    // starting (counting)
        static final int NOTIF_MARK = 2000;     // starting (counting)
    }


    public static String START_FG_BG_SYNC = "com.suryo.gamatechno.app.startforeground.bg.sync";
    public static String STOP_FG_BG_SYNC = "com.suryo.gamatechno.app.stopforeground.bg.sync";
}
