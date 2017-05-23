package com.supinfo.whereiscage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by fairuz on 26/03/17.
 */

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //SplashScreen elle s'affiche 3 secondes avant l'activité principale.

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                //passage au mainactivity.

                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
