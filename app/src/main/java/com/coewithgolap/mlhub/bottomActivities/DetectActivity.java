package com.coewithgolap.mlhub.bottomActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coewithgolap.mlhub.Fetch;
import com.coewithgolap.mlhub.JSONParser;
import com.coewithgolap.mlhub.PestsActivity;
import com.coewithgolap.mlhub.bottomActivities.detectActivities.AppleActivity;
import com.coewithgolap.mlhub.FirstActivity;
import com.coewithgolap.mlhub.HomeActivity;
import com.coewithgolap.mlhub.R;
import com.example.mytensor.TensorActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DetectActivity extends AppCompatActivity {

    RelativeLayout contentView, pests;
    Button detectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        detectBtn = (Button) findViewById(R.id.detectBtn);

        startActivties();

        pests = findViewById(R.id.pests);
        pests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetectActivity.this, PestsActivity.class));
            }
        });

        contentView = findViewById(R.id.content_view);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.detect);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.crops:
                                DetectActivity.this.startActivity(new Intent(DetectActivity.this,
                                        HomeActivity.class));
                                DetectActivity.this.finish();
                                DetectActivity.this.overridePendingTransition(0, 0);
                                return true;
                            case R.id.community:
                                DetectActivity.this.startActivity(new Intent(DetectActivity.this,
                                        CommunityActivity.class));
                                DetectActivity.this.finish();
                                DetectActivity.this.overridePendingTransition(0, 0);
                                return true;
                            case R.id.detect:
                                return true;
                        }
                        return false;
                    }
                });

        JSONParser.setActivity(this);

        new Fetch(DetectActivity.this).execute();
    }

    private void startActivties() {
        detectBtn.setOnClickListener(view -> {
            startActivity(new Intent(DetectActivity.this, TensorActivity.class));
        });
    }


    public void updateWeather(String code, String temp, String descrip) {


        TextView weatherDesc = findViewById(R.id.description);
        weatherDesc.setText(descrip);

        TextView weatherTemp = findViewById(R.id.Temp);
        weatherTemp.setText(temp);

        getWeatherIcon(code);
    }

    public void getWeatherIcon(String code){

        String icon;

        Typeface weatherFont = Typeface.createFromAsset(getAssets(), "weather.ttf");

        if (code.startsWith("2") ) { //thunder
            icon = getString(R.string.thunder_weather);

        } else if (code.startsWith("3")) { //drizzle

            icon = getString(R.string.drizzle_weather);
        } else if (code.startsWith("5")) { //rain
            icon = getString(R.string.rainy_weather);

        }else if (code.startsWith("6")) {
            icon = getString(R.string.snowy_weather);
            //snow
        } else if (code.startsWith("7")) {
            icon = getString(R.string.foggy_weather);
            //foggy
        } else if (code.equals("800")) {
            icon = getString(R.string.clear_weather);
            //clear
        } else if(code.startsWith("80") ) {
            icon = getString(R.string.cloudy_weather);
            //cloudy
        } else {
            icon = "N/A";
        }

        //code to set weather icons
        if (!icon.equals("N/A")) {
            TextView weatherIcon = findViewById(R.id.weatherIcon);
            weatherIcon.setText(icon);
            weatherIcon.setTypeface(weatherFont);
        }
    }
}