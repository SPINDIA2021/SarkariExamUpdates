package com.gsbussiness.sarkariexamupdates;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.net.URLEncoder;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    CardView findgovjob,currentaffairs,dailynews,examresult,category,profession,qualification,location;
    ImageView notification;
    LinearLayout layWhatsapp;

    private NativeAd mobNativeView;
    private void NativeBinding(NativeAd nativeAd, NativeAdView adView) {
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }
        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }
        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(nativeAd);
    }
    public void NativeShow(final FrameLayout frameLayout) {
        AdLoader.Builder builder = new AdLoader.Builder(getApplication(), getString(R.string.Admob_Native));

        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {

                boolean isDestroyed = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    isDestroyed = isDestroyed();
                }
                if (isDestroyed || isFinishing() || isChangingConfigurations()) {
                    nativeAd.destroy();
                    return;
                }
                if (MainActivity.this.mobNativeView != null) {
                    MainActivity.this.mobNativeView.destroy();
                }
                MainActivity.this.mobNativeView = nativeAd;
                NativeAdView adView = (NativeAdView) getLayoutInflater().inflate(R.layout.mobnative, null);
                NativeBinding(nativeAd, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }
        });

        VideoOptions videoOptions = new VideoOptions.Builder().build();
        com.google.android.gms.ads.nativead.NativeAdOptions adOptions = new com.google.android.gms.ads.nativead.NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {


            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());


    }
    public void NativeLoad() {
        NativeShow((FrameLayout) findViewById(R.id.mobadslayout));
    }

    InterstitialAd mMobInterstitialAds;
    public void InterstitialLoad() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("1ADAD30F02CD84CDE72190C2ABE5EB5E")).build();
        MobileAds.setRequestConfiguration(configuration);
        InterstitialAd.load(getApplicationContext(), getString(R.string.Admob_Interstitial), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                MainActivity.this.mMobInterstitialAds = interstitialAd;
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
            this.mMobInterstitialAds.show(MainActivity.this);
        }
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024,1024);
        setContentView(R.layout.activity_main);

        InterstitialLoad();
        NativeLoad();


        findgovjob = findViewById(R.id.findgovjob);
        currentaffairs = findViewById(R.id.currentaffairs);
        dailynews = findViewById(R.id.dailynews);
        examresult = findViewById(R.id.examresult);
        category = findViewById(R.id.category);
        profession = findViewById(R.id.profession);
        qualification = findViewById(R.id.qualification);
        location = findViewById(R.id.location);
        notification = findViewById(R.id.notification);
        layWhatsapp=findViewById(R.id.lay_whatsapp);

        findgovjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AllGovjob.class));
                    ShowFunUAds();
            }
        });

        currentaffairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Currentaffairs.class));
                    ShowFunUAds();
            }
        });

        dailynews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Dailynews.class));
                    ShowFunUAds();
            }
        });

        examresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Examresults.class));
                    ShowFunUAds();
            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Category.class).putExtra("title","Category"));
                    ShowFunUAds();
            }
        });

        profession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Category.class).putExtra("title","Profession"));
                    ShowFunUAds();
            }
        });

        qualification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Category.class).putExtra("title","Qualification"));
                    ShowFunUAds();
           }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Category.class).putExtra("title","Location"));
                    ShowFunUAds();
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, Notification.class));
                    ShowFunUAds();
            }
        });

        layWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWhatsApp();
            }
        });
    }


    private void openWhatsApp(){

        PackageManager pm=getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = "YOUR TEXT HERE";

            PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }

    }

}