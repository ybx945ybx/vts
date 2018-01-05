package examp.com.vts.net;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.util.concurrent.TimeUnit;

import examp.com.vts.VtsApplication;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by a55 on 2017/11/2.
 */

public class RetrofitFactory {
    private static final     OkHttpClient           okHttpClient;
    private static final HttpLoggingInterceptor loggingIntercepter;

    static {
        loggingIntercepter = new HttpLoggingInterceptor();

//        if (BuildConfig.DEBUG) {
//            loggingIntercepter.setLevel(HttpLoggingInterceptor.Level.NONE);
//        }

        // 持久化cook管理
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(VtsApplication.getContext()));

//        OkHttpClient.Builder builder = new OkHttpClient.Builder().cookieJar(cookieJar);
//        OkHttpClient okHttpClient = new OkHttpClient();

        okHttpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(loggingIntercepter)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

//        okHttpClient.interceptors().add(new ReadCookiesInterceptor());
//        okHttpClient.interceptors().add(new SaveCookiesInterceptor());

    }

    public static <T> T createService(Class<T> clazz) {
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://39.106.32.139:8080/")
                .baseUrl("http://47.104.84.211:8292/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit.create(clazz);
    }
}
