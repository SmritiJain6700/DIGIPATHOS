package com.coewithgolap.mlhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.coewithgolap.mlhub.Adapters.CropsViewHolder;
import com.coewithgolap.mlhub.bottomActivities.CommunityActivity;
import com.coewithgolap.mlhub.bottomActivities.CropsDetailsActivity;
import com.coewithgolap.mlhub.bottomActivities.DetectActivity;
import com.coewithgolap.mlhub.mainPageActivity.LoginActivity;
import com.coewithgolap.mlhub.models.CropsModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    RelativeLayout contentView;
    private RecyclerView cropsRecyclerView;
    private DatabaseReference cropRef;
    private LinearLayout cropsProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (InitApplication.getInstance().isNightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        cropRef = FirebaseDatabase.getInstance().getReference().child("Crops");
        cropsRecyclerView = findViewById(R.id.cropsRecyclerView);
        cropsRecyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new
                StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        cropsRecyclerView.setLayoutManager(staggeredGridLayoutManager);

        cropsProgressBar = findViewById(R.id.crops_progree_bar);
        cropsProgressBar.setVisibility(View.VISIBLE);

        crops();

        contentView = findViewById(R.id.content_view);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.crops);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.crops:
                                return true;
                            case R.id.community:
                                HomeActivity.this.startActivity(new Intent(HomeActivity.this,
                                        CommunityActivity.class));
                                HomeActivity.this.finish();
                                HomeActivity.this.overridePendingTransition(0, 0);
                                return true;
                            case R.id.detect:
                                HomeActivity.this.startActivity(new Intent(HomeActivity.this,
                                        DetectActivity.class));
                                HomeActivity.this.finish();
                                HomeActivity.this.overridePendingTransition(0, 0);
                                return true;
                        }
                        return false;
                    }
                });
    }

    private void crops() {
        FirebaseRecyclerOptions<CropsModel> options = new FirebaseRecyclerOptions.Builder<CropsModel>()
                .setQuery(cropRef, CropsModel.class)
                .build();
        FirebaseRecyclerAdapter<CropsModel, CropsViewHolder> cropAdapter = new FirebaseRecyclerAdapter<CropsModel,
                CropsViewHolder>(options) {
            @NonNull
            @Override
            public CropsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crop_items_layout, parent,
                        false);
                CropsViewHolder holder = new CropsViewHolder(view);
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull CropsViewHolder holder, final int position,
                                            @NonNull final CropsModel model) {

                cropsProgressBar.setVisibility(View.GONE);
                holder.name.setText(model.getName());
                holder.description.setText(model.getDescription());
                Picasso.get().load(model.getImage()).into(holder.image);
                //Glide.with(HomeActivity.this).load(model.getImage()).into(holder.image);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this, CropsDetailsActivity.class);
                        intent.putExtra("cId", model.getcId());
                        startActivity(intent);

                    }
                });
            }
        };

        cropsRecyclerView.setAdapter(cropAdapter);
        cropAdapter.startListening();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}