package com.supinfo.whereiscage;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.supinfo.whereiscage.utils.Chronometre;
import com.supinfo.whereiscage.utils.TouchImageView;
import com.supinfo.whereiscage.utils.Utils;

import java.util.Random;

/**
 * Created by fairuz on 17/03/17.
 */

public class ChronoModeOne extends AppCompatActivity {

    //private ScaleGestureDetector detector;
    private TouchImageView imageView;
    private TextView t;
    private int counter = 0, innerCounter = 0, score = 0;
    private Random rnd;
    private int imgId;
    private TextView textView;
    private TextView scoreText;
    private long foundAt;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chrono_mode_one);

        textView = (TextView) findViewById(R.id.counter);
        scoreText = (TextView) findViewById(R.id.score);

        scoreText.setText(scoreText.getText().toString()+" \n "+score);

        sharedPreferences = getBaseContext().getSharedPreferences("WhereIsCagePrefs", MODE_PRIVATE);

        //initialisaion des images des cage.
        Utils.initHashMap();

        //initialisation de l'imageview.
        final SubsamplingScaleImageView imageView = (SubsamplingScaleImageView)findViewById(R.id.imageView);

        //initialisation des different images.
        final int[] images = new int[] {R.drawable.where_s_cage1, R.drawable.where_s_cage2, R.drawable.where_s_cage3, R.drawable.where_s_cage4, R.drawable.where_s_cage5};
        //on fait un random pour pouvoir afficher une image au harsard.
        rnd = new Random();
        imgId = rnd.nextInt(images.length);

        //affichage de l'image.
        imageView.setImage(ImageSource.resource(images[imgId]));

        setChrono(Utils.time);

        Toast.makeText(ChronoModeOne.this, "Vous avez "+Utils.time/1000+" secondes", Toast.LENGTH_SHORT).show();

        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            //initialisation de l'alert dialog pour l'affichage du message de freeze.

            public AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChronoModeOne.this).setTitle(R.string.app_name).setMessage(R.string.penality).setCancelable(false);

            //cette fonction permet de savoir quand l'utilisateur clique une fois sur l'écran.

            @Override
            public boolean onSingleTapConfirmed(final MotionEvent e) {
                if (imageView.isReady()) {

                    //on recupere les coordonnée du point en lequel il a toucher l'écran .
                    PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());

                    System.out.println("Coordonnée "+sCoord+" x: "+sCoord.x +" && "+ sCoord.y);

                    // si le coordonnée recuperé correspond a la position de cage en fonction de l'image, x en premier et y ensuite le joueur a trouver cage
                    if (sCoord.x > Utils.getCageCoordIn(images[imgId]).x && sCoord.x < (new Float(Utils.getCageCoordIn(images[imgId]).x) + new Float(200.000))){
                        System.out.println("coordonnée x bien ... ");
                        if (sCoord.y > Utils.getCageCoordIn(images[imgId]).y  && sCoord.y < (new Float(Utils.getCageCoordIn(images[imgId]).y) + new Float(300.000)) ){
                            System.out.println("coordonnée y bien ");
                            Toast.makeText(ChronoModeOne.this,"well done", Toast.LENGTH_SHORT).show();

                            //on incremente le score.
                            scoreText.setText("SCORE : \n "+(score += 1));

                            // on refait un random pour lui afficher la pochaine image.
                            rnd = new Random();
                            imgId = rnd.nextInt(images.length);
                            imageView.setImage(ImageSource.resource(images[imgId]));

                            // on réinitialise les coordonnée de cage pour la nouvelle image.
                            Utils.getCageCoordIn(images[imgId]);

                            // on réinitialise le compteur avec le temps qui reste apres avoir trouvé cage.
                            int newTime = Integer.valueOf(""+foundAt/1000);
                            counter = 0; textView.setText(counter+" secondes");
                            setChrono(newTime);

                            Toast.makeText(ChronoModeOne.this, "Vous avez "+newTime+" secondes", Toast.LENGTH_SHORT).show();


                            //On ajoute le nom du joueur et le score au fur et a mesure qu'il trouve cage sur l'image.
                            Utils.putInSharePreference(ChronoModeOne.this, sharedPreferences.getString(Utils.USERNAME, null),""+score);

                        }else {

                            // si le joueur ne trouve pas cage sur l'image on freeze l'écrant pendant  seconde.

                            System.out.println(" coordonnée y pas bon .... "+Utils.getCageCoordIn(images[imgId]).y+" && "+(new Float(Utils.getCageCoordIn(images[imgId]).y)+ new Float(300.000)));

                            final AlertDialog alert = alertDialog.create();
                            alert.setCancelable(false);
                            alert.show();

                            //affichage du dialog box pour 2 secondes.
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
                    Toast.makeText(ChronoModeOne.this, "Image pas prêt", Toast.LENGTH_SHORT).show();
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

    }

    //cette fonction permet de re-definir l'état du chronometre une fois que l'utilisateur a trouver cage.
    public void setChrono(int time){

        // initialisation du chronometre
        new CountDownTimer(time,1000){

            // ontick represente a chaque seconde, ici apres chaque seconde on initialise le compteur.
            @Override
            public void onTick(long millisUntilFinished) {
                counter += 1;
                innerCounter += 1;
                foundAt = millisUntilFinished;
                System.out.println("compteur: "+counter+" innerCounter: "+innerCounter);
                textView.setText(counter+" secondes");
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }
}
