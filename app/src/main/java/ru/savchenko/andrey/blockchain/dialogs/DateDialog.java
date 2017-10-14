package ru.savchenko.andrey.blockchain.dialogs;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

import ru.savchenko.andrey.blockchain.interfaces.SetDataFromDialog;

/**
 * Created by savchenko on 14.10.17.
 */

public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    private SetDataFromDialog setDataFromDialog;

    public void setSetDataFromDialog(SetDataFromDialog setDataFromDialog) {
        this.setDataFromDialog = setDataFromDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        setDataFromDialog.setData(year, month, day);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);

    }
}
