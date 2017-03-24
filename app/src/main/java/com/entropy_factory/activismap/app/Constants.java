package com.entropy_factory.activismap.app;

/**
 * Created by ander on 3/10/16.
 */
public class Constants {

    private static final String TAG = "Constants";

    public static final class APP {
        public static final boolean DEBUG_MODE = ActivisApplication.INSTANCE.isDebuggable();
    }

    public static final class API {
        public static final String BASE_URL = APP.DEBUG_MODE ? "https://activismap-api.owldevelopers.tk/" : "https://activismap-api.owldevelopers.tk/";
        public static final String CLIENT_ID = APP.DEBUG_MODE ? "1_46wnhc8wzoqo0sgkk4cgsgsskw0wos080cwsswkso8co0kg0w4" : "1_46wnhc8wzoqo0sgkk4cgsgsskw0wos080cwsswkso8co0kg0w4";
        public static final String CLIENT_SECRET = APP.DEBUG_MODE ? "7vnu6sdezpk4w0wg0kgkcgg4ocggw40040kscs8ksc4oogko0" : "7vnu6sdezpk4w0wg0kgkcgg4ocggw40040kscs8ksc4oogko0";
    }
}
