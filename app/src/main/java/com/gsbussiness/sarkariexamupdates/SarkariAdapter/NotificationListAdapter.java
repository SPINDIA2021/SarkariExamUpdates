package com.gsbussiness.sarkariexamupdates.SarkariAdapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gsbussiness.sarkariexamupdates.AllgovjobContent;
import com.gsbussiness.sarkariexamupdates.SarkariModal.GovtJobModal;
import com.gsbussiness.sarkariexamupdates.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {

    Context context;
    private ArrayList<GovtJobModal> arrayList;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item
                        ,parent
                        ,false));
    }
    public void filterList(ArrayList<GovtJobModal> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        if(filterllist!=null) {
            arrayList = filterllist;
            // below line is to notify our adapter
            // as change in recycler view data.
            notifyDataSetChanged();
        }
    }
    public NotificationListAdapter(Context context, ArrayList<GovtJobModal> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GovtJobModal model = arrayList.get(position);

        holder.title.setText(model.getJobTitle());
        holder.descrption.setText(model.getJobContent());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            long time = sdf.parse( model.getPostPublishDate()).getTime();
            long now = System.currentTimeMillis();
            CharSequence ago =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
            holder.time.setText(ago+"");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, AllgovjobContent.class);
                i.putExtra("slugid", model.getPostid());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }
        });
//    holder.nextbtn.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Intent i = new Intent(context, CategoryContent.class);
//            i.putExtra("name", model.getName());
//            i.putExtra("id", model.getPostid());
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
//        }
//    });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,descrption,time;

RelativeLayout nextbtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            descrption = itemView.findViewById(R.id.descrption);
            nextbtn = itemView.findViewById(R.id.nextbtn);
        }
    }
}
