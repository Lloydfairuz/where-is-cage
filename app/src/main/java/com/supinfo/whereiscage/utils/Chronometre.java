package com.supinfo.whereiscage.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.supinfo.whereiscage.ChronoModeOne;
import com.supinfo.whereiscage.MainActivity;
import com.supinfo.whereiscage.R;

/**
 * Created by fairuz on 18/03/17.
 */

public class Chronometre extends CountDownTimer {

    int counter = 1;
    ProgressBar progressBar;
    private Context context;
    public Chronometre(long millisInFuture, long countDownInterval, ProgressBar progressBar, Context context) {
        super(millisInFuture, countDownInterval);
        this.progressBar = progressBar;
        this.context = context;
    }

    @Override
    public void onTick(long millisUntilFinished) {

        int progress = (int) (millisUntilFinished/1000);
        System.out.println("progress: "+progress);
        //on recupere le progress bar passer en parametre duquel on retrance les seconde pour faire un bar de chargement.
        progressBar.setProgress(progressBar.getMax()-progress);

        // en fonction du temps restant on affiche les secondes.
        if (progress == 10){
            Toast.makeText(context,"10 secondes",Toast.LENGTH_SHORT).show();
        }else if (progress == 5){
            Toast.makeText(context,"5 secondes",Toast.LENGTH_SHORT).show();
        }else if (progress == 20) {
            Toast.makeText(context,"20 secondes",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFinish() {
        // quand le temps fini on le redirige vers l'écran principale.
        new AlertDialog.Builder(context).setTitle(R.string.app_name).setMessage(R.string.end).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                context.startActivity(new Intent(context, MainActivity.class));
            }
        }).show();
    }
}