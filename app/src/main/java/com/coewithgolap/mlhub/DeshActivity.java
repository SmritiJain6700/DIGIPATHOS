package com.coewithgolap.mlhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.coewithgolap.mlhub.mainPageActivity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class DeshActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desh);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (FirebaseAuth.getInstance().getCurrentUser()==null){
                startActivity(new Intent(DeshActivity.this, LoginActivity.class));
                finish();
            }
            else {
                startActivity(new Intent(DeshActivity.this, HomeActivity.class));
                finish();
            }
        }, 2000);
    }
}
