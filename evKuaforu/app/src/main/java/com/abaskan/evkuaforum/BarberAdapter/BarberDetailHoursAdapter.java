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

public class BarberDetailHoursAdapter extends RecyclerView.Adapter<BarberDetailHoursAdapter.BarberDetailHoursHolder>{
    private Context context;
    private ArrayList<String> dayList;
    private ArrayList<String> hourList;

    public BarberDetailHoursAdapter(Context context, ArrayList<String> dayList, ArrayList<String> hourList) {
        this.context = context;
        this.dayList = dayList;
        this.hourList = hourList;
    }

    @NonNull
    @Override
    public BarberDetailHoursHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_barber_working_hours,parent,false);
        return new BarberDetailHoursHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarberDetailHoursHolder holder, int position) {
        holder.detailWorkingDayText.setText(dayList.get(position));
        holder.detailWorkingHourText.setText(hourList.get(position));
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    public class BarberDetailHoursHolder extends RecyclerView.ViewHolder{
        TextView detailWorkingDayText;
        TextView detailWorkingHourText;
        public BarberDetailHoursHolder(@NonNull View itemView) {
            super(itemView);
            detailWorkingDayText = itemView.findViewById(R.id.detailWorkingDayText);
            detailWorkingHourText = itemView.findViewById(R.id.detailWorkingHourText);
        }
    }
}
