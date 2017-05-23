package com.supinfo.whereiscage.utils;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.supinfo.whereiscage.ChronoModeTwo;
import com.supinfo.whereiscage.MainActivity;
import com.supinfo.whereiscage.R;

import java.util.Calendar;

/**
 * Created by fairuz on 17/03/17.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        
        int minute = c.get(Calendar.MINUTE);
        int seconde = c.get(Calendar.SECOND);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, minute, seconde,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int minute, int second) {

        ChronoModeTwo.progressBar.setMax((minute * 60000) + (second * 1000)/1000);
        Chronometre chronometre = new Chronometre((minute * 60000) + (second * 1000),1000, ChronoModeTwo.progressBar,getContext());
        chronometre.start();
    }
}
