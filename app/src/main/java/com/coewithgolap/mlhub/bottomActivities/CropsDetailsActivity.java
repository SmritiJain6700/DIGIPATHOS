package com.coewithgolap.mlhub.bottomActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.coewithgolap.mlhub.R;
import com.coewithgolap.mlhub.SliderActivities.CultureActivity;
import com.coewithgolap.mlhub.SliderActivities.DetailsActivity;
import com.coewithgolap.mlhub.models.CropsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CropsDetailsActivity extends AppCompatActivity {

    private ImageView mainImageView;
    private TextView tempTv, cropNameTv, cropScTv, cropDetailsTv;
    private String cropID = "";
    private ProgressBar cropsProgressBar;

    private RelativeLayout detailsRL, cultureRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crops_details);


        cropID = getIntent().getStringExtra("cId");

        mainImageView = (ImageView) findViewById(R.id.mainImageView);
        tempTv = (TextView) findViewById(R.id.weatherDegreeText);
        cropNameTv = (TextView) findViewById(R.id.cropName);
        cropScTv = (TextView) findViewById(R.id.cropScientificName);
        cropDetailsTv = (TextView) findViewById(R.id.cropDetails);
        detailsRL = (RelativeLayout) findViewById(R.id.detailsCard);
        cultureRL = (RelativeLayout) findViewById(R.id.cultureCard);

        cropsProgressBar = findViewById(R.id.progress_bar);
        //cropsProgressBar.setVisibility(View.VISIBLE);
        detailsRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CropsDetailsActivity.this, DetailsActivity.class);
                intent.putExtra("cId", cropID);
                startActivity(intent);
            }
        });

        cultureRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CropsDetailsActivity.this, CultureActivity.class);
                intent.putExtra("cId", cropID);
                startActivity(intent);
            }
        });

        getCropDetails(cropID);
    }

    private void getCropDetails(String cropID) {

        DatabaseReference cropRef = FirebaseDatabase.getInstance().getReference().child("Crops");

        cropRef.child(cropID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    CropsModel cropsModel = dataSnapshot.getValue(CropsModel.class);

                    cropNameTv.setText(cropsModel.getName());
                    cropScTv.setText(cropsModel.getDescription());
                    cropDetailsTv.setText(cropsModel.getDetails());
                    tempTv.setText(cropsModel.getTemp());
                    //Picasso.get().load(cropsModel.getmImg()).into(mainImageView);
                    //Glide.with(CropsDetailsActivity.this).load(cropsModel.getmImg()).into(mainImageView);
                    Glide.with(CropsDetailsActivity.this)
                            .load(cropsModel.getmImg())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    cropsProgressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    cropsProgressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(mainImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}