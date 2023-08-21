package com.gsbussiness.sarkariexamupdates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class CategoryDetails extends AppCompatActivity {
    private ImageView backbutton;
    RelativeLayout nextbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);
        getWindow().setFlags(1024,1024);

        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        nextbtn = findViewById(R.id.nextbtn);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CategoryDetails.this, CategoryContent.class));
            }
        });
    }
}