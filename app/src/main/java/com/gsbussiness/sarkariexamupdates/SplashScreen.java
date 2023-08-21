package com.gsbussiness.sarkariexamupdates;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.Arrays;

public class SplashScreen extends AppCompatActivity {

    InterstitialAd mMobInterstitialAds;
    public void InterstitialLoad() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("1ADAD30F02CD84CDE72190C2ABE5EB5E")).build();
        MobileAds.setRequestConfiguration(configuration);
        InterstitialAd.load(getApplicationContext(), getString(R.string.Admob_Interstitial), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                SplashScreen.this.mMobInterstitialAds = interstitialAd;
                interstitialAd.setFullScreenContentCallback(
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {

                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {

                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                            }
                        });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            }
        });

    }
    private void ShowFunUAds() {
        if (this.mMobInterstitialAds != null) {
            this.mMobInterstitialAds.show(SplashScreen.this);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(1024,1024);

        InterstitialLoad();

        new Handler().postDelayed(new Runnable() {
            public void run() {

                    startActivity(new Intent(SplashScreen.this, StartScreen.class));
                ShowFunUAds();
                    finish();
                            }
        }, 6000);
    }

}