package examp.com.vts;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;

public class ProtocolsActivity extends BaseActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocols);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        webView = findViewById(R.id.webView);
//        // 初始化设置
//        // Init WebView Setting
//        WebSettings webSettings = webView.getSettings();
//        // 使用硬件加速
//        webSettings.setBlockNetworkImage(true);
//        // 支持js打开一个窗口
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        //设置编码
//        webSettings.setDefaultTextEncodingName("utf-8");
//        // 支持js
//        webSettings.setJavaScriptEnabled(true);
//        //加载网络上的图片资源
//        webSettings.setBlockNetworkImage(false);
//        // 设置useragent
//        //        String ua = webView.getSettings().getUserAgentString();
//        //        webSettings.setUserAgentString(ua + "@@@zg_Android");
//        //        webSettings.setUserAgentString(ua + "@@@zg_Android" + "___" + BuildConfig.VERSION_NAME);
//
//        // 设置 Cookie
////        setCookie();
////
////        // 设置js交互
////        webView.addJavascriptInterface(new JSHook(new Gson()), AppModel);
//
//        // 获取HTML Title
//        WebChromeClient wvcc = new WebChromeClient() {
//
//        };
//        webView.setWebChromeClient(wvcc);
//
//        // 设置 WebViewClient
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String newUrl) {
//                // 当打开新链接时，使用当前的 WebView，不会使用系统其他浏览器
//                view.loadUrl(newUrl);
//                return true;
//            }
//
//        });
//        webView.loadUrl("https://www.baidu.com/");

    }
}
