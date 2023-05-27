package com.coewithgolap.mlhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.coewithgolap.mlhub.mainPageActivity.AboutApp;
import com.coewithgolap.mlhub.mainPageActivity.AboutCorn;
import com.coewithgolap.mlhub.mainPageActivity.AboutDeveloper;
import com.coewithgolap.mlhub.mainPageActivity.HowToDetect;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void aboutCorn(View view) {
        startActivity(new Intent(MainActivity.this, AboutCorn.class));
    }

    public void howToDetect(View view) {
        startActivity(new Intent(MainActivity.this, HowToDetect.class));
    }

    public void aboutApp(View view) {
        startActivity(new Intent(MainActivity.this, AboutApp.class));
    }

    public void aboutDeveloper(View view) {
        startActivity(new Intent(MainActivity.this, AboutDeveloper.class));
    }

    public void detectDiseaseBtnClick(View view) {
        startActivity(new Intent(MainActivity.this, FirstActivity.class));
    }

}