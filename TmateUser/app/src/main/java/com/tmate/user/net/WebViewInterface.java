package com.tmate.user.net;

import android.app.Activity;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;


public class WebViewInterface {
    private WebView mWebView;
    private Activity mContext;
    private final Handler handler = new Handler();

    public WebViewInterface(Activity activity, WebView view) {
        mWebView = view;
        mContext = activity;
    }

    @JavascriptInterface
    public void finishedActivity() {
        mContext.finish();
    }
}
