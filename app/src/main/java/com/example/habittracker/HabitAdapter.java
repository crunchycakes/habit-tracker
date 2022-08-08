package com.example.habittracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.ViewHolder> {

    private ArrayList<Habit> data;

    /**
     * custom viewholder class, reference the views of view_habit
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nameView;
        private TextView resetView;
        private Button doneButton;

        public ViewHolder(View view) {
            super(view);

            nameView = view.findViewById(R.id.habit_textview_name);
            resetView = view.findViewById(R.id.habit_textview_nextreset);
            doneButton = view.findViewById(R.id.habit_button_done);
        }

        public TextView getNameView() {return nameView;}
        public TextView getResetView() {return resetView;}
        public Button getDoneButton() {return doneButton;}
    }

    /**
     * construct adapter, init arraylist data
     * @param dataset arraylist containing habit list for views
     */
    public HabitAdapter(ArrayList<Habit> dataset) {
        data = dataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_habit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Habit habit = data.get(position);
        holder.getNameView().setText(habit.getName());

        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMMM dd, yyyy - h:mm:ss a");
        holder.getResetView().setText(habit.getResetDate().format(format));

        Button button = holder.getDoneButton();

        if (habit.isDone()) {
            updateDoneButton(habit, button, true);
        } else {
            updateDoneButton(habit, button, false);
        }

        // should probably follow this instead: https://stackoverflow.com/a/28304517
        holder.getDoneButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!habit.isDone()) {
                    habit.setDone();
                    updateDoneButton(habit, button, true);
                } else {
                    habit.unsetDone();
                    updateDoneButton(habit, button, false);
                }
            }
        });
    }

    /**
     * update visible habits
     * @param start first position to update, inclusive
     * @param end last position to update, inclusive
     */
    public void update(int start, int end) {
        int count = end - start + 1;
        notifyItemRangeChanged(start, count);
    }

    /**
     * set what state the done button appears as
     * @param habit what habit's state to get
     * @param button which button to modify
     * @param isDone what state to set the done button
     */
    private void updateDoneButton(Habit habit, Button button, boolean isDone) {
        int color;
        String text; // todo: use id instead of hardcoded text
        if (isDone) {
            color = android.R.color.system_accent1_100;
            text = "MARK UNDONE";
        } else {
            color = android.R.color.system_accent1_500;
            text = "MARK DONE";
        }
        button.setText(text);
        button.setBackgroundColor(button.getContext().getResources()
                .getColor(color));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /*
    // also from https://stackoverflow.com/a/32488059
    @Override
    public long getItemId(int position) {

        return 0;
    }
     */

}
