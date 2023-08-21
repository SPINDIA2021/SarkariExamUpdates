package com.gsbussiness.sarkariexamupdates;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.gsbussiness.sarkariexamupdates.SarkariHelper.ConstClass;
import com.gsbussiness.sarkariexamupdates.SarkariHelper.PreferClass;
import com.gsbussiness.sarkariexamupdates.services.VolleyClass;
import com.gsbussiness.sarkariexamupdates.services.VolleyListener;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class DailynewsContent extends AppCompatActivity {
    private ImageView backbutton;
    TextView heading,time,lastdate;
    private VolleyClass volleyClass;
    private ShimmerFrameLayout shimmerFrameLayout;
    private Context context;
    Intent intent;
    String slugid;
    WebView descrption;
    TextView titlebar;
    AdView adView1;
    public void BannerLoadads() {
        adView1 = new com.google.android.gms.ads.AdView(DailynewsContent.this);
        RelativeLayout adContainer = (RelativeLayout) findViewById(R.id.adMobView);
        adView1.setAdUnitId(getString(R.string.Admob_Banner));
        adContainer.addView(adView1);
        loadBanner();
    }

    private void loadBanner() {
        AdRequest adRequest = new AdRequest.Builder().build();
        AdSize adSize = getAdSize();
        adView1.setAdSize(adSize);
        adView1.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allgovjob_content);
        getWindow().setFlags(1024,1024);


        context = this;
        volleyClass = new VolleyClass(context);
        intent=getIntent();

        slugid=intent.getStringExtra("slugid");
        BannerLoadads();
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        backbutton = findViewById(R.id.backbutton);
        titlebar = findViewById(R.id.titlebar);
        titlebar.setText("Details");
        heading = findViewById(R.id.heading);
        time = findViewById(R.id.time);
        lastdate = findViewById(R.id.lastdate);
        descrption = findViewById(R.id.descrption);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        descrption.getSettings().setJavaScriptEnabled(true);
        descrption.getSettings().setGeolocationEnabled(true);
        descrption.setSoundEffectsEnabled(true);
        descrption.setWebViewClient(new WebViewClient());
        getGovjob();

    }
    private void getGovjob() {

        String jwt = "";



        try {

            HashMap<String, String> map = new HashMap<>();
            map.put("postid", slugid);
            map.put("updateViewdate", "0");
            map.put("url", "");
            map.put("uuid", "1");

            Log.e("URL","URL::  /Slugid"+  ConstClass.decryptMsg(ConstClass.parseHexStr2Byte(ConstClass.getpostdetails))+" / "+slugid);

            volleyClass.doPost(ConstClass.decryptMsg(ConstClass.parseHexStr2Byte(ConstClass.getpostdetails)), map, new VolleyListener() {

                @Override
                public void response(String result) {
                    Log.e("dta", result);
                    try {
                        if (!result.isEmpty()) {


                            JSONObject jsonObject = new JSONObject(result);

                            if (jsonObject.getString("responsetext").equalsIgnoreCase("ok")) {

                                JSONObject object =  new JSONObject(jsonObject.getString("data"));

                                heading.setText(object.getString("jobTitle"));
                                time.setText(object.getString("formattedDate"));
                                lastdate.setText("Last Date:"+object.getString("expiredDate"));
                                //descrption.loadUrl("http://tutorials.jenkov.com");
                                descrption.setWebViewClient(new WebViewClient() {
                                    @Override
                                    public void onPageFinished(WebView view, String url)
                                    {
                                        // hide element by class name
                                        descrption.loadUrl("javascript:(function() { " + "document.getElementsByTagName('h1')[0].style.display = 'none'; " + "})()");

                                    }
                                });
                                descrption.loadDataWithBaseURL(null,object.getString("jobContent"), "text/html; charset=utf-8", "UTF-8",null);




                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
                            } else {
                                ConstClass.ShowToast(context, jsonObject.getString("responsetext"));
                            }
                        } else {
                            ConstClass.ShowToast(context, "response is null");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void failed(VolleyError error) {
                    ConstClass.ShowToast(context, "" + error);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();
    }

    @Override
    protected void onPause() {
        shimmerFrameLayout.stopShimmer();
        super.onPause();
    }
}
