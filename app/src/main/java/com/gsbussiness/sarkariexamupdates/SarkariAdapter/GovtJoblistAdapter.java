package com.gsbussiness.sarkariexamupdates.SarkariAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.gsbussiness.sarkariexamupdates.SarkariHelper.PreferClass;
import com.gsbussiness.sarkariexamupdates.SarkariModal.GovtJobModal;
import com.gsbussiness.sarkariexamupdates.R;

import java.util.ArrayList;

public class GovtJoblistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    String nativeid;

    private static final int MENU_ITEM_VIEW_TYPE = 0;
    private static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;
    Context context;
    private ArrayList<GovtJobModal> arrayList;
    OnListClick onListClick;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == UNIFIED_NATIVE_AD_VIEW_TYPE) {
            //Inflate ad native container
            View bannerLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_native_adview, parent, false);

            //Create View Holder
            AdHolder myAdViewHolder = new AdHolder(bannerLayoutView);

            return myAdViewHolder;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_list_item, parent, false);

            //Create View Holder
            ViewHolder myHolder = new ViewHolder(view);
            return myHolder;
        }
    }
    public GovtJoblistAdapter(Context context, ArrayList<GovtJobModal> arrayList, OnListClick onListClick) {
        this.context = context;
        this.arrayList = arrayList;
        this.nativeid = PreferClass.getString(context,PreferClass.NativeAds);
        this.onListClick = onListClick;
    }
    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position) == null) {
            return UNIFIED_NATIVE_AD_VIEW_TYPE;
        }
        return MENU_ITEM_VIEW_TYPE;
    }
    public interface OnListClick{
        void onclick(String title,String api,int pos);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);
        if (viewType == UNIFIED_NATIVE_AD_VIEW_TYPE) {
            AdHolder adHolder = (AdHolder) holder;


            adHolder.frameLayout.removeAllViews();
            nativeAd(adHolder);
        }else {
            ViewHolder holder1 = (ViewHolder) holder;
            GovtJobModal model = arrayList.get(position);
            holder1.heading.setText(model.getJobTitle());
            holder1.time.setText(model.getFormattedDate());
            holder1.descrption.setText(model.getJobContent());

            holder1.qualification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onListClick.onclick(model.getJobTitle(), "", position);

                }
            });
        }


    }
    public class AdHolder extends RecyclerView.ViewHolder {

        private FrameLayout frameLayout;
        RelativeLayout adnote;


        public AdHolder(@NonNull View itemView) {
            super(itemView);

            frameLayout = itemView.findViewById(R.id.native_ad_container2);
            adnote = itemView.findViewById(R.id.adnote);
        }
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView  heading,time,descrption;
CardView qualification;
        CardView ipoinformation;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.heading);
            time = itemView.findViewById(R.id.time);
            qualification = itemView.findViewById(R.id.qualification);

            descrption = itemView.findViewById(R.id.descrption);

        }
    }

    private void nativeAd(final AdHolder holder) {

        AdLoader.Builder builder = new AdLoader.Builder(context, nativeid);
        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {
                Log.i("nativeAd","onNativeAdLoaded");
                NativeAdView adView = (NativeAdView) LayoutInflater.from(context).inflate(R.layout.nativebannersmall, null);
                populateUnifiedNativeAdView(nativeAd, adView);
                holder.adnote.setVisibility(View.GONE);
                holder.frameLayout.setVisibility(View.VISIBLE);
                holder.frameLayout.removeAllViews();
                holder.frameLayout.addView(adView);
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

    private void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView) {
//        MediaView mediaView = adView.findViewById(R.id.ad_media);
//        adView.setMediaView(mediaView);
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
}
