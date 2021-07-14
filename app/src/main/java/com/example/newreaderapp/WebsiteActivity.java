package com.example.newreaderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebsiteActivity extends AppCompatActivity {

    private WebView web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);

        Intent intent=getIntent();
        if(null!= intent){
            String url=intent.getStringExtra("url");
            if(url!= null){
                web=findViewById(R.id.website);
                web.setWebViewClient(new WebViewClient());
                web.getSettings().setJavaScriptEnabled(true);
                web.loadUrl(url);

            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(web.canGoBack()){
            getObbDir();
        }else{
            super.onBackPressed();
        }
    }
}