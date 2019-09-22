package com.bonnuslabs.Bonnusv3sdk_Demo;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import mx.bonnuslabs.Bonnusv3sdk_Demo.R;

public class BonnusWebAppActivity extends AppCompatActivity {

    private WebView webView;
    private Boolean canBack = false;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonnus_web_app);
        progressBar = findViewById(R.id.progressBar);
        webViewConfiguration();
    }

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    private void webViewConfiguration(){
        webView = findViewById(R.id.webView);

        webView.addJavascriptInterface(new BonnusWebAppActivity.WebInterface(), "AndroidInterface");
        webView.setWebViewClient(new BonnusWebAppActivity.WebClient());

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(false);
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setDatabaseEnabled(false);
        webView.getSettings().setAllowFileAccess(false);
        webView.getSettings().setAllowContentAccess(false);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);

        if(getIntent().hasExtra(MainActivity.URL_TO_LOAD)){
            webView.loadUrl(getIntent().getStringExtra(MainActivity.URL_TO_LOAD));
        }
    }

    private class WebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon){
            super.onPageStarted(view,url,favicon);
        }

        @Override
        public void onPageFinished(WebView view,String url){
            progressBar.setVisibility(View.GONE);
            injectJavaScriptFunction();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true;
        }
    }

    private class WebInterface{
        @JavascriptInterface
        public void onMessage(String result){
            if(result.equals("Leave Clicked") || result.equals("On Continue Shared") || result.equals("LeaveOK")){
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("about:blank");
                        webView.clearCache(true);
                        webView.clearHistory();
                        BonnusWebAppActivity.this.canBack = true;
                        BonnusWebAppActivity.this.onBackPressed();
                    }
                });
            }
        }
    }

    private void injectJavaScriptFunction() {
        //webView.loadUrl("javascript: callJavaScriptFunction();");
    }

    @Override
    public void onBackPressed() {
        if(canBack)
            super.onBackPressed();
    }
}
