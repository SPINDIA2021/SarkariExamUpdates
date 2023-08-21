package com.gsbussiness.sarkariexamupdates;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.gsbussiness.sarkariexamupdates.SarkariAdapter.OptionListAdapter;
import com.gsbussiness.sarkariexamupdates.SarkariHelper.ConstClass;
import com.gsbussiness.sarkariexamupdates.SarkariHelper.PreferClass;
import com.gsbussiness.sarkariexamupdates.SarkariModal.CategoryModal;
import com.gsbussiness.sarkariexamupdates.services.VolleyClass;
import com.gsbussiness.sarkariexamupdates.services.VolleyListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class Category extends AppCompatActivity {
    private Context context;
    private RecyclerView mRecyclerView;
    private ArrayList<CategoryModal> arrayList = new ArrayList<>();
    private ImageView back;
    private VolleyClass volleyClass;
    private TextView textView,titlebar;
    private ShimmerFrameLayout shimmerFrameLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    Intent intent;
    String title;
    SearchView searchView;
    OptionListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getWindow().setFlags(1024,1024);
        titlebar = findViewById(R.id.titlebar);
        back = findViewById(R.id.backbutton);
        searchView = findViewById(R.id.searchview);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        intent=getIntent();
        title=intent.getStringExtra("title");
        titlebar.setText(title+" List");
        context = this;
        volleyClass = new VolleyClass(context);

        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        mRecyclerView = findViewById(R.id.recyc_ipo);
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
            Log.e("URL","URL::  "+  ConstClass.decryptMsg(ConstClass.parseHexStr2Byte(ConstClass.categoryurl)));

            URL url = new URL(ConstClass.decryptMsg(ConstClass.parseHexStr2Byte(ConstClass.categoryurl)));
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("it");



            for (int i = 0; i < nodeList.getLength(); i++) {

                Node node = nodeList.item(i);

                CategoryModal model = new CategoryModal();

                Element fstElmnt = (Element) node;
                NodeList nameList = fstElmnt.getElementsByTagName("n");
                Element nameElement = (Element) nameList.item(0);
                nameList = nameElement.getChildNodes();


                model.setName(((Node) nameList.item(0)).getNodeValue());

                NodeList websiteList = fstElmnt.getElementsByTagName("i");
                Element websiteElement = (Element) websiteList.item(0);
                websiteList = websiteElement.getChildNodes();
                model.setPostid(((Node) websiteList.item(0)).getNodeValue());


                NodeList catlistid = fstElmnt.getElementsByTagName("p");
                Element catlistidElement = (Element) catlistid.item(0);
                catlistid = catlistidElement.getChildNodes();
                String catid=((Node) catlistid.item(0)).getNodeValue();

                if(title.equalsIgnoreCase("Category") &&  catid.equalsIgnoreCase("40")){
                    arrayList.add(model);
                }else if(title.equalsIgnoreCase("Profession") &&  catid.equalsIgnoreCase("37")){
                    arrayList.add(model);
                }
                else if(title.equalsIgnoreCase("Qualification") &&  catid.equalsIgnoreCase("38")){
                    arrayList.add(model);
                }
                else if(title.equalsIgnoreCase("Location") &&  catid.equalsIgnoreCase("39")){
                    arrayList.add(model);
                }

            }

                                            setAdapter();

                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
        } catch (Exception e) {
            System.out.println("XML Pasing Excpetion = " + e);
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
             adapter = new OptionListAdapter(context, arrayList);
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // inside on query text change method we are
                    // calling a method to filter our recycler view.
                    filter(newText);
                    return false;
                }
            });
        }
    }
    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<CategoryModal> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        if(!text.trim().isEmpty()) {
            for (CategoryModal item : arrayList) {
                // checking if the entered string matched with any item of our recycler view.
                if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                    // if the item is matched we are
                    // adding it to our filtered list.
                    filteredlist.add(item);
                }
            }
            if (filteredlist.isEmpty()) {
                // if no item is added in filtered list we are
                // displaying a toast message as no data found.
                Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
            } else {
                // at last we are passing that filtered
                // list to our adapter class.
                adapter.filterList(filteredlist);
            }
        }else{
            if(adapter!=null)
                adapter.filterList(arrayList);
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
