package com.abaskan.evkuaforum.BarberAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abaskan.evkuaforum.R;

import java.util.ArrayList;

public class BarberWorkingHoursAdapter extends RecyclerView.Adapter<BarberWorkingHoursAdapter.BarberWorkingHoursHolder>{
    private Context context;
    private ArrayList<String> dayList;
    private ArrayList<String> hourList;

    public BarberWorkingHoursAdapter(Context context, ArrayList<String> dayList, ArrayList<String> hourList) {
        this.context = context;
        this.dayList = dayList;
        this.hourList = hourList;
    }

    @NonNull
    @Override
    public BarberWorkingHoursHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_barber_hours,parent,false);
        return new BarberWorkingHoursHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarberWorkingHoursHolder holder, int position) {
        holder.workingBarberDayText.setText(dayList.get(position));
        holder.workingBarberHourText.setText(hourList.get(position));
    }

    @Override
    public int getItemCount() {
        return hourList.size();
    }

    public class BarberWorkingHoursHolder extends RecyclerView.ViewHolder{
        TextView workingBarberDayText;
        TextView workingBarberHourText;
        public BarberWorkingHoursHolder(@NonNull View itemView) {
            super(itemView);
            workingBarberDayText = itemView.findViewById(R.id.workingBarberDayText);
            workingBarberHourText = itemView.findViewById(R.id.workingBarberHourText);
        }
    }
}
