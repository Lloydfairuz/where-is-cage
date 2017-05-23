package com.supinfo.whereiscage.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PointF;

import com.supinfo.whereiscage.R;

import java.util.HashMap;

/**
 * Created by fairuz on 25/03/17.
 */

public class Utils {

    public static int time = 120000;
    public static final String USERNAME = "playerName";
    public static final String SCORE = "playerScore";
    static HashMap<Integer, PointF>  cage = new HashMap<>();
    private static SharedPreferences sharedPreferences;


    //cette fonction permet d'initialiser les differentes images de cage avec leur coordonnée dans l'imageview.
    public static void initHashMap(){
        cage.put(R.drawable.where_s_cage1,new PointF(new Float(2490.1077),new Float(2670.578)));
        cage.put(R.drawable.where_s_cage2,new PointF(new Float(4585.613),new Float(1466.3231)));
        cage.put(R.drawable.where_s_cage3,new PointF(new Float(4853.447),new Float(3889.9927)));
        cage.put(R.drawable.where_s_cage4,new PointF(new Float(4707.6514),new Float(2540.1157)));
        cage.put(R.drawable.where_s_cage5,new PointF(new Float(1820.1721),new Float(1054.0143)));
    }

    /*cette fonction permet de recuperer la position de cage dans l'image afficher.
    * elle prend en parametre un drawable qui est celle de l'image en question */
    public static PointF getCageCoordIn(int drawable){
        return cage.get(drawable);
    }

    /* cette fonction permet d'écrire le score et le nom d'utilisateur dans les sharedpreferences
    * elle prend en parametre le context de l'activiter en cours, le username et le score .*/
    public static void putInSharePreference(Context context, String username, String score){

        sharedPreferences = context.getSharedPreferences("WhereIsCagePrefs", context.MODE_PRIVATE);

        //persistance des donnée dans le sharedpreference
        sharedPreferences.edit()
                .putString(Utils.USERNAME, username)
                .putString(Utils.SCORE, score)
                .commit();

    }
}
