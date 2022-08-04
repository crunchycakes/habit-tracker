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
     * custom viewholder, reference the views of view_habit
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
        public TextView getDoneButton() {return doneButton;}
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
        holder.getNameView().setText(data.get(position).getName());

        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMMM dd, yyyy - h:mm:ss aa");
        holder.getResetView().setText(data.get(position).getResetDate().format(format));
    }

    @Override
    public int getItemCount() {
        return 0;
    }



}
