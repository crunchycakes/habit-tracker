package com.example.habittracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;

import java.time.ZonedDateTime;

public class AddHabitFragment extends DialogFragment {

    private DatePicker datePicker;
    private EditText editTime;
    private EditText editName;
    private Spinner spinner;
    private OnFragmentFinishListener listener;

    public AddHabitFragment() {
        super(R.layout.fragment_addhabit);
    }

    public interface OnFragmentFinishListener {
        void onFragmentFinish(Habit newHabit);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentFinishListener) {
            listener = (OnFragmentFinishListener) context;
        } else {
            throw new RuntimeException(context.toString() + " has not implemented OnFragmentFinishListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_addhabit, null);

        datePicker = view.findViewById(R.id.addhabit_datepicker_datepicker);
        editTime = view.findViewById(R.id.addhabit_edittext_time);
        editName = view.findViewById(R.id.addhabit_edittext_name);
        spinner = view.findViewById(R.id.addhabit_spinner_units);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.array_spinnertimeunits, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.setView(view).setTitle("Add new habit")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (editName.getText().toString().trim().length() <= 0
                            || editTime.getText().toString().trim().length() <= 0) {
                            return;
                        }

                        int day = datePicker.getDayOfMonth();
                        int month = datePicker.getMonth();
                        int year = datePicker.getYear();
                        ZonedDateTime dateTime = ZonedDateTime.of(year, month, day,
                                0, 0, 0, 0, MainActivity.ZONE);
                        long dateTimeMillis = dateTime.toInstant().toEpochMilli();

                        // todo: this REALLY shouldn't be hardcoded; put ints in resources or smth
                        long multiplier = 0;
                        switch (spinner.getSelectedItem().toString()) {
                            case "days":
                                multiplier = 86400000;
                                break;
                            case "hours":
                                multiplier = 3600000;
                                break;
                            default:
                                throw new RuntimeException("Failed to get milli multiplier from spinner");
                        }

                        long interval = multiplier * Long.parseLong(editTime.getText().toString());

                        Habit newHabit = new Habit(
                                editName.getText().toString(),
                                dateTimeMillis,
                                interval
                                );
                        listener.onFragmentFinish(newHabit);
                    }
                }).create();

    }

}





