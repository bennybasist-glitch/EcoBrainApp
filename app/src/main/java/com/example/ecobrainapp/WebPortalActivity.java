package com.example.ecobrainapp;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class WebPortalActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_portal);

        webView = findViewById(R.id.webViewEco);
        progressBar = findViewById(R.id.progressBarWeb);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        // Configuración de los botones del carrusel
        CardView btnSedema = findViewById(R.id.btnPortalSedema);
        CardView btnSemarnat = findViewById(R.id.btnPortalSemarnat);
        CardView btnEdomex = findViewById(R.id.btnPortalEdomex);

        btnSedema.setOnClickListener(v -> webView.loadUrl("https://sedema.cdmx.gob.mx/"));
        btnSemarnat.setOnClickListener(v -> webView.loadUrl("https://apps1.semarnat.gob.mx:8443/dgeia/informe18/index.html"));
        btnEdomex.setOnClickListener(v -> webView.loadUrl("https://sma.edomex.gob.mx/"));

        // Carga inicial
        webView.loadUrl("https://sedema.cdmx.gob.mx/");
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}