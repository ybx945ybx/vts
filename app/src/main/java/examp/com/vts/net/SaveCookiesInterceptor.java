package examp.com.vts.net;

import java.io.IOException;
import java.util.HashSet;
import java.util.prefs.Preferences;

import examp.com.vts.VtsApplication;
import examp.com.vts.utils.SPUtils;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by a55 on 2017/12/5.
 */

public class SaveCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();

            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }

            SPUtils.putStringSet(VtsApplication.getContext(), "cookes", cookies);
//            Preferences.getDefaultPreferences().edit()
//                    .putStringSet(Preferences.PREF_COOKIES, cookies)
//                    .apply();
        }

        return originalResponse;
    }
}