package app.dongzhao.com.graduationproject.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import app.dongzhao.com.graduationproject.R;

public class NewsDetailActicity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_webview);

        View view = View.inflate(NewsDetailActicity.this, R.layout.news_webview, null);
        WebView webView = (WebView) view.findViewById(R.id.webview);
        webView.loadUrl("www.baidu.com");
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }
}
