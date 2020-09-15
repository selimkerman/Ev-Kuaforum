package com.abaskan.evkuaforum.BarberAdapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abaskan.evkuaforum.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Random;

public class BarberAppointmentsHistoryAdapter extends RecyclerView.Adapter<BarberAppointmentsHistoryAdapter.BarberAppointmentsHistoryHolder>{
    private Context context;
    private ArrayList<String> userNameList;
    private ArrayList<String> AppointmentsTimeList;
    private ArrayList<String> serviceNameList;
    private ArrayList<Integer> totalPriceList;
    private ArrayList<String> userIdList;
    private ArrayList<String> placeList;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public BarberAppointmentsHistoryAdapter(Context context, ArrayList<String> userNameList, ArrayList<String> AppointmentsTimeList, ArrayList<String> serviceNameList, ArrayList<Integer> totalPriceList, ArrayList<String> userIdList, ArrayList<String> placeList) {
        this.context = context;
        this.userNameList = userNameList;
        this.AppointmentsTimeList = AppointmentsTimeList;
        this.serviceNameList = serviceNameList;
        this.totalPriceList = totalPriceList;
        this.userIdList = userIdList;
        this.placeList = placeList;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public BarberAppointmentsHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_barber_appointments_history,parent,false);
        return new BarberAppointmentsHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarberAppointmentsHistoryHolder holder, int position) {
        holder.barberAppointmentsHistoryUserIcon.setText(userNameList.get(position).substring(0,1));
        Random random = new Random();
        final int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        ((GradientDrawable) holder.barberAppointmentsHistoryUserIcon.getBackground()).setColor(color);

        holder.barberAppointmentsHistoryUserName.setText(userNameList.get(position));
        holder.barberAppointmentsHistoryTime.setText(AppointmentsTimeList.get(position));
        holder.barberAppointmentsHistoryService.setText(serviceNameList.get(position));
        holder.barberAppointmentsHistoryPlace.setText(placeList.get(position));
        holder.barberAppointmentsHistoryPrice.setText(totalPriceList.get(position) + " TL");
    }

    @Override
    public int getItemCount() {
        return userNameList.size();
    }

    public class BarberAppointmentsHistoryHolder extends RecyclerView.ViewHolder{
        TextView barberAppointmentsHistoryUserIcon;
        TextView barberAppointmentsHistoryUserName;
        TextView barberAppointmentsHistoryTime;
        TextView barberAppointmentsHistoryService;
        TextView barberAppointmentsHistoryPrice;
        TextView barberAppointmentsHistoryPlace;
        CardView barberAppointmentsHistoryCardView;

        public BarberAppointmentsHistoryHolder(@NonNull View itemView) {
            super(itemView);
            barberAppointmentsHistoryUserIcon = itemView.findViewById(R.id.barberAppointmentsHistoryUserIcon);
            barberAppointmentsHistoryUserName = itemView.findViewById(R.id.barberAppointmentsHistoryUserName);
            barberAppointmentsHistoryTime = itemView.findViewById(R.id.barberAppointmentsHistoryTime);
            barberAppointmentsHistoryService = itemView.findViewById(R.id.barberAppointmentsHistoryService);
            barberAppointmentsHistoryPrice = itemView.findViewById(R.id.barberAppointmentsHistoryPrice);
            barberAppointmentsHistoryPlace = itemView.findViewById(R.id.barberAppointmentsHistoryPlace);
            barberAppointmentsHistoryCardView = itemView.findViewById(R.id.barberAppointmentsHistoryCardView);
        }
        }
}
