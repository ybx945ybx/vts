package examp.com.vts;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * Created by a55 on 2017/11/26.
 */

public class VtsApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        // 初始化Logger
        Logger.init("wwht").logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);
        // 初始化Stetho
        Stetho.initializeWithDefaults(this);
        //  初始化fresco
        Fresco.initialize(this);

    }


    public static Context getContext() {
        return context;
    }
}
