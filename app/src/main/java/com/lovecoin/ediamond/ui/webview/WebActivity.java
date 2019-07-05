package com.lovecoin.ediamond.ui.webview;

import android.os.Build;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.ui.base.BaseActivity;
import com.lovecoin.ediamond.widget.CustomToolbar;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import activitystarter.Arg;
import butterknife.BindView;

/**
 * web
 */

public class WebActivity extends BaseActivity {
    @Arg String url;

    @BindView(R.id.webView) WebView webView;
    @BindView(R.id.toolbar) CustomToolbar toolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void onBeforeSetContentLayout() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    protected void initView() {
        WebSettings ws = webView.getSettings();
        ws.setSavePassword(false);
        ws.setJavaScriptEnabled(false);
        ws.setAllowFileAccess(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(new H5WebViewClient());
        webView.setWebChromeClient(new H5WebChromeClient());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void initData() {
        webView.loadUrl(url);
    }

    class H5WebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);

        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            //Logger.d("progress:%s", newProgress);

        }
    }

    class H5WebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            String title = view.getTitle();
            toolbar.setTitle(title);
        }
    }

}
