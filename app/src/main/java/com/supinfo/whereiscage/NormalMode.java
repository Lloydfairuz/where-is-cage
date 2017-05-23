package com.supinfo.whereiscage;

import android.content.SharedPreferences;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.supinfo.whereiscage.utils.TouchImageView;
import com.supinfo.whereiscage.utils.Utils;

import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Created by fairuz on 17/03/17.
 */

public class NormalMode extends AppCompatActivity {

    private TouchImageView imageView;
    private TextView t;
    private int counter = 0, innerCounter = 0, score = 0;
    private  int marge = 100;
    HashMap<Integer, PointF> cage = new HashMap<>();
    private Random rnd;
    private int imageId,imgId;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chrono_mode_one);

        //initialisaion des images des cage.
        Utils.initHashMap();

        sharedPreferences = getBaseContext().getSharedPreferences("WhereIsCagePrefs", MODE_PRIVATE);

        final SubsamplingScaleImageView imageView = (SubsamplingScaleImageView)findViewById(R.id.imageView);


        final int[] images = new int[] {R.drawable.where_s_cage1, R.drawable.where_s_cage2, R.drawable.where_s_cage3, R.drawable.where_s_cage4, R.drawable.where_s_cage5};

        rnd = new Random();
        imgId = rnd.nextInt(images.length);

        imageView.setImage(ImageSource.resource(images[imgId]));


        final TextView textView = (TextView) findViewById(R.id.counter);
        final TextView scoreText = (TextView) findViewById(R.id.score);


        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(final MotionEvent e) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(NormalMode.this).setTitle(R.string.app_name).setMessage(R.string.penality).setCancelable(false);

                if (imageView.isReady()) {
                    PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());

                    System.out.println("Coordonnée "+sCoord+" x: "+sCoord.x +" && "+ sCoord.y);

                    if (sCoord.x > Utils.getCageCoordIn(images[imgId]).x && sCoord.x < (new Float(Utils.getCageCoordIn(images[imgId]).x) + new Float(200.000))){
                        System.out.println("coordonnée x bien ... ");
                        if (sCoord.y > Utils.getCageCoordIn(images[imgId]).y && sCoord.y < (new Float(Utils.getCageCoordIn(images[imgId]).y) + new Float(300.000))){

                            System.out.println("coordonnée y bien ");
                            Toast.makeText(NormalMode.this,"well done", Toast.LENGTH_SHORT).show();
                            scoreText.setText("SCORE : \n "+(score += 1));
                            rnd = new Random();
                            imgId = rnd.nextInt(images.length);
                                imageView.setImage(ImageSource.resource(images[imgId]));
                                Utils.getCageCoordIn(images[imgId]);

                            Utils.putInSharePreference(NormalMode.this, sharedPreferences.getString(Utils.USERNAME, null),""+score);

                        }else {
                            System.out.println(" coordonnée y pas bon .... "+Utils.getCageCoordIn(images[imgId]).y+" && "+(new Float(Utils.getCageCoordIn(images[imgId]).y)+ new Float(300.000)));

                            final AlertDialog alert = alertDialog.create();
                            alert.setCancelable(false);
                            alert.show();
                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    if (alert.isShowing()) {
                                        alert.dismiss();
                                    }
                                }
                            }, 2000);
                        }
                    } else {
                        System.out.println(" coordonnée x pas bon .... "+Utils.getCageCoordIn(images[imgId]).x+" && "+ (new Float(Utils.getCageCoordIn(images[imgId]).x)+ new Float(200.000)));

                        final AlertDialog alert = alertDialog.create();
                        alert.show();
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                if (alert.isShowing()) {
                                    alert.dismiss();
                                }
                            }
                        }, 2000);
                    }
                }else{
                    Toast.makeText(NormalMode.this, "Image pas prêt", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });

        // chronometre de la parti.
        new CountDownTimer(120000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                counter += 1;
                innerCounter += 1;
                if (innerCounter < 60){
                    System.out.println("compteur: "+counter+" innerCounter: "+innerCounter);
                    textView.setText(counter+" secondes");
                }else  {
                    textView.setText(Integer.parseInt(""+innerCounter/60)+" minutes \n "+counter+" secondes");
                }

            }

            @Override
            public void onFinish() {
                // quand le temps fini ici on le relance
                start();
                counter = 0;
            }
        }.start();
    }
}
