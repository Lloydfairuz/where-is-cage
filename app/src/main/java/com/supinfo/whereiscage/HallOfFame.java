package com.supinfo.whereiscage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.supinfo.whereiscage.utils.Utils;

import java.util.ArrayList;

/**
 * Created by fairuz on 25/03/17.
 */

public class HallOfFame extends AppCompatActivity{

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hall_of_fame);

        //initialisation du sharedPreferences pour pouvoir recuperer  le nom de l'utilisateur encours.
        sharedPreferences = getBaseContext().getSharedPreferences("WhereIsCagePrefs", MODE_PRIVATE);

        ArrayList<String> score = new ArrayList<>();

        //recuperation et ajout du nom du joueur dans le liste d'objet string on créée un nouvel objet quand la liste n'est pas vide.
        if (score.size()!=0){
            String s = new String("Player: "+sharedPreferences.getString(Utils.USERNAME, null).toUpperCase()+" | Score:  "+ sharedPreferences.getString(Utils.SCORE, null));
            score.add(s);
        }else {
            score.add(new String("Player: "+sharedPreferences.getString(Utils.USERNAME, null).toUpperCase()+" | Score:  "+ sharedPreferences.getString(Utils.SCORE, null)));
        }

        //initialisation de l'adapter qui sera associer à la listview
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, score);


        //initialisation de la listview
        ListView listView = (ListView) findViewById(R.id.listView);

        //affectation de l'adapter permettant de binder les données de a afficher.
        listView.setAdapter(itemsAdapter);
    }

}
