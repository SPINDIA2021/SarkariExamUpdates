package com.gsbussiness.sarkariexamupdates;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.gsbussiness.sarkariexamupdates.SarkariAdapter.NotificationListAdapter;
import com.gsbussiness.sarkariexamupdates.SarkariHelper.ConstClass;
import com.gsbussiness.sarkariexamupdates.SarkariHelper.PreferClass;
import com.gsbussiness.sarkariexamupdates.SarkariModal.GovtJobModal;
import com.gsbussiness.sarkariexamupdates.services.VolleyClass;
import com.gsbussiness.sarkariexamupdates.services.VolleyListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class Notification extends AppCompatActivity {
    private Context context;
    private RecyclerView mRecyclerView;
    private ArrayList<GovtJobModal> arrayList = new ArrayList<>();
    private ImageView back;
    private VolleyClass volleyClass;
    private TextView textView;
    private ShimmerFrameLayout shimmerFrameLayout;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getWindow().setFlags(1024,1024);

        back = findViewById(R.id.backbutton);
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
            map.put("postid", "0");
            map.put("updateViewdate", "0");
            map.put("alertType", "4");

            map.put("uuid", "1");

            Log.e("URL","URL::"+  ConstClass.decryptMsg(ConstClass.parseHexStr2Byte(ConstClass.getnotification)));

            volleyClass.doPost(ConstClass.decryptMsg(ConstClass.parseHexStr2Byte(ConstClass.getnotification)), map, new VolleyListener() {

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
                                 //   model.setRow1(object.getString("row1"));

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
                                ConstClass.ShowToast(context, jsonObject.getString("ok"));
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

    private void setAdapter() {
        if (arrayList.size() == 0) {
            textView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            mRecyclerView.setLayoutManager(manager);
            NotificationListAdapter adapter = new NotificationListAdapter(context, arrayList);
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