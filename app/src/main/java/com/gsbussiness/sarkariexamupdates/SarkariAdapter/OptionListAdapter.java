package com.gsbussiness.sarkariexamupdates.SarkariAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.gsbussiness.sarkariexamupdates.CategoryContent;
import com.gsbussiness.sarkariexamupdates.SarkariModal.CategoryModal;
import com.gsbussiness.sarkariexamupdates.R;

import java.util.ArrayList;

public class OptionListAdapter extends RecyclerView.Adapter<OptionListAdapter.ViewHolder> {

    Context context;
    private ArrayList<CategoryModal> arrayList;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.option_item
                        ,parent
                        ,false));
    }
    public void filterList(ArrayList<CategoryModal> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        if(filterllist!=null) {
            arrayList = filterllist;
            // below line is to notify our adapter
            // as change in recycler view data.
            notifyDataSetChanged();
        }
    }
    public OptionListAdapter(Context context, ArrayList<CategoryModal> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryModal model = arrayList.get(position);

        holder.heading.setText(model.getName());
    holder.nextbtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(context, CategoryContent.class);
            i.putExtra("name", model.getName());
            i.putExtra("id", model.getPostid());
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView heading;

RelativeLayout nextbtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            heading = itemView.findViewById(R.id.heading);

            nextbtn = itemView.findViewById(R.id.nextbtn);
        }
    }
}
