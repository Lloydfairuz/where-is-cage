package com.supinfo.whereiscage;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.supinfo.whereiscage.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private TextView user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button modeNormalBtn = (Button) findViewById(R.id.modeNormalBtn);
        Button chronoModeOne = (Button) findViewById(R.id.modeOneBtn);
        Button chronoModeTwo = (Button) findViewById(R.id.modeTwoBtn);
        user = (TextView) findViewById(R.id.username);

        modeNormalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,NormalMode.class));
            }
        });

        chronoModeOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ChronoModeOne.class));
            }
        });

        chronoModeTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ChronoModeTwo.class));
            }
        });

        sharedPreferences = getBaseContext().getSharedPreferences("WhereIsCagePrefs", MODE_PRIVATE);
        if (sharedPreferences.getString(Utils.USERNAME, null) == null){
            showDialog();
        }else if (sharedPreferences.getString(Utils.USERNAME, null) != null){
            user.setVisibility(View.VISIBLE);
            user.setText(user.getText().toString()+" \n "+sharedPreferences.getString(Utils.USERNAME, null).toUpperCase());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.playerName) {
            showDialog();
        }else if (id == R.id.hallOfFame){
            startActivity(new Intent(MainActivity.this, HallOfFame.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_box, null);
        dialogBuilder.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.playerName);

        dialogBuilder.setTitle("Where is cage");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                putInSharePreference(editText.getText().toString());
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    public void putInSharePreference(String username){
        sharedPreferences = getBaseContext().getSharedPreferences("WhereIsCagePrefs", MODE_PRIVATE);

            sharedPreferences.edit()
                    .putString(Utils.USERNAME, username)
                    .commit();

        finish();
        startActivity(new Intent(MainActivity.this,MainActivity.class));
    }

}
