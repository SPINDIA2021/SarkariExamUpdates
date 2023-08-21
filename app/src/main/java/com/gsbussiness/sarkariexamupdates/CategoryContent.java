package com.gsbussiness.sarkariexamupdates;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.gsbussiness.sarkariexamupdates.SarkariAdapter.GovtJoblistAdapter;
import com.gsbussiness.sarkariexamupdates.SarkariHelper.ConstClass;
import com.gsbussiness.sarkariexamupdates.SarkariHelper.PreferClass;
import com.gsbussiness.sarkariexamupdates.SarkariModal.GovtJobModal;
import com.gsbussiness.sarkariexamupdates.services.VolleyClass;
import com.gsbussiness.sarkariexamupdates.services.VolleyListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class CategoryContent extends AppCompatActivity implements GovtJoblistAdapter.OnListClick {
    private ImageView backbutton;
    private Context context;
    private RecyclerView mRecyclerView;
    private ArrayList<GovtJobModal> arrayList = new ArrayList<>();
    private ImageView back;
    private VolleyClass volleyClass;
    private TextView textView, titlebar;
    private ShimmerFrameLayout shimmerFrameLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    Intent intent;
    String name;
    String id;


    InterstitialAd mMobInterstitialAds;

    public void InterstitialLoad() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("1ADAD30F02CD84CDE72190C2ABE5EB5E")).build();
        MobileAds.setRequestConfiguration(configuration);
        InterstitialAd.load(getApplicationContext(), getString(R.string.Admob_Interstitial), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                CategoryContent.this.mMobInterstitialAds = interstitialAd;
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
            this.mMobInterstitialAds.show(CategoryContent.this);
        }
    }


    AdView adView1;

    public void BannerLoadads() {
        adView1 = new com.google.android.gms.ads.AdView(CategoryContent.this);
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
        setContentView(R.layout.activity_category_content);
        getWindow().setFlags(1024, 1024);
        intent = getIntent();
        BannerLoadads();
        InterstitialLoad();
        name = intent.getStringExtra("name");
        id = intent.getStringExtra("id");
        back = findViewById(R.id.backbutton);
        titlebar = findViewById(R.id.titlebar);
        titlebar.setText(name + " Job List");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        context = this;
        volleyClass = new VolleyClass(context);

        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        mRecyclerView = findViewById(R.id.recyc_ipo);
//        back = findViewById(R.id.iv_back);
//        back.setOnClickListener(v -> {
//            onBackPressed();
//        });
        textView = findViewById(R.id.txt_noData);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.txt_back);
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getdata();

            }
        });

        if (ConstClass.isNetworkAvailable(context)) {
            if (arrayList.size() == 0) {
                getdata();
            } else {
                setAdapter();
            }
        } else {
            ConstClass.ShowToast(context, "no network available");
        }
    }

    private void getdata() {

        String jwt = "";
        arrayList.clear();


        try {

            HashMap<String, String> map = new HashMap<>();
            map.put("labelid", id);
            map.put("archive", "0");
            map.put("page", "1");
            map.put("langId", "1");
            map.put("pagesize", "100");

            Log.e("URL","URL::  /ID"+  ConstClass.decryptMsg(ConstClass.parseHexStr2Byte(ConstClass.baseurl))+" / "+id);


            volleyClass.doPost(ConstClass.decryptMsg(ConstClass.parseHexStr2Byte(ConstClass.baseurl)), map, new VolleyListener() {

                @Override
                public void response(String result) {
                    Log.e("dta", result);
                    try {
                        if (!result.isEmpty()) {
                            arrayList.clear();

                            JSONObject jsonObject = new JSONObject(result);

                            if (jsonObject.getString("responsetext").equalsIgnoreCase("ok")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);


                                    GovtJobModal model = new GovtJobModal();
                                    model.setJobTitle(object.getString("jobTitle"));
                                    model.setJobContent(object.getString("jobContent"));
                                    model.setPostid(object.getString("postid"));
                                    model.setPostPublishDate(object.getString("postPublishDate"));
                                    model.setRow(object.getString("row"));
                                    model.setSeconds(object.getString("seconds"));
                                    model.setFormattedDate(object.getString("formattedDate"));
                                    model.setTotalRows(object.getString("totalRows"));
                                    model.setRow1(object.getString("row1"));

                                    model.setExpiredDate(object.getString("expiredDate"));
                                    model.setUrl(object.getString("url"));
//                                    if (i != 0 && i % 5 == 0)
//                                        arrayList.add(null);
                                    arrayList.add(model);
                                }


                                setAdapter();

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
    public void onclick(String title, String api, int pos) {

        String uri = null;

        Intent i = new Intent(context, AllgovjobContent.class);
        i.putExtra("slugid", arrayList.get(pos).getPostid());

        startActivity(i);
        ShowFunUAds();


    }

    private void setAdapter() {
        if (arrayList.size() == 0) {
            textView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            mRecyclerView.setLayoutManager(manager);
            GovtJoblistAdapter adapter = new GovtJoblistAdapter(context, arrayList, this);
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
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