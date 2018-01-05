package examp.com.vts.net;

import java.io.IOException;
import java.util.HashSet;
import java.util.prefs.Preferences;

import examp.com.vts.VtsApplication;
import examp.com.vts.utils.SPUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by a55 on 2017/12/5.
 */

public class ReadCookiesInterceptor implements   Interceptor {

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request.Builder builder     = chain.request().newBuilder();
        HashSet<String> preferences = (HashSet) SPUtils.getStringSet(VtsApplication.getContext(), "cookes", new HashSet<>());
        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
            //            Log.v("OkHttp", "Adding Header: " + cookie); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
        }

        return chain.proceed(builder.build());
    }
}
