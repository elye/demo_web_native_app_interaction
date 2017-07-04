package com.elyeproj.webappinteraction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String JAVASCRIPT_OBJ = "javascript_obj";
    private static final String BASE_URL = "file:///android_asset/webview.html";

    private WebView webView;
    private TextView textFromWeb;
    private Button btnToWeb;
    private EditText edtToWeb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.my_web_view);
        textFromWeb = (TextView) findViewById(R.id.txt_from_web);
        btnToWeb = (Button) findViewById(R.id.btn_send_to_web);
        edtToWeb = (EditText) findViewById(R.id.edit_text_to_web);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        webView.addJavascriptInterface(new JavaScriptInterface(), JAVASCRIPT_OBJ);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.equals(BASE_URL)) {
                    injectJavaScriptFunction();
                }
            }
        });

        webView.loadUrl(BASE_URL);

        btnToWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.evaluateJavascript("javascript: " +
                        "updateFromAndroid(\"" + edtToWeb.getText() + "\")", null);
            }
        });
    }

    private void injectJavaScriptFunction() {
        webView.loadUrl("javascript: " +
                "window.androidObj.textToAndroid = function(message) { " +
                JAVASCRIPT_OBJ + ".textFromWeb(message) }");
    }


    private class JavaScriptInterface {
        @JavascriptInterface
        public void textFromWeb(String fromWeb) {
            textFromWeb.setText(fromWeb);
        }
    }
}
