package skku.alticastvux.app;

import android.app.Application;
import android.content.SharedPreferences;

import skku.alticastvux.util.BookMarkUtil;
import skku.alticastvux.util.SharedPreferencesUtil;
import skku.alticastvux.voiceable.ASREventController;

/**
 * Created by woorim on 2018. 7. 20..
 */

public class SKKUVuxApp extends Application {

    private static SKKUVuxApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        SharedPreferencesUtil.init(this);
        BookMarkUtil.init(this);
    }

    public static SKKUVuxApp getInstance() {
        return instance;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
