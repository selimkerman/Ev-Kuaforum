package com.abaskan.evkuaforum.BarberAdapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abaskan.evkuaforum.BarberActivity.CustomerDetailActivity;
import com.abaskan.evkuaforum.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Random;

public class BarberAppointmentsAdapter extends RecyclerView.Adapter<BarberAppointmentsAdapter.BarberAppointmentsHolder>{
    private Context context;
    private ArrayList<String> userNameList;
    private ArrayList<String> appointmentTimeList;
    private ArrayList<String> serviceNameList;
    private ArrayList<Integer> totalPriceList;
    private ArrayList<String> userIdList;
    private ArrayList<String> placeList;
    private ArrayList<Long> appointmentLongList;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public BarberAppointmentsAdapter(Context context, ArrayList<String> userNameList, ArrayList<String> appointmentTimeList, ArrayList<String> serviceNameList, ArrayList<Integer> totalPriceList, ArrayList<String> userIdList, ArrayList<String> placeList, ArrayList<Long> appointmentLongList) {
        this.context = context;
        this.userNameList = userNameList;
        this.appointmentTimeList = appointmentTimeList;
        this.serviceNameList = serviceNameList;
        this.totalPriceList = totalPriceList;
        this.userIdList = userIdList;
        this.placeList = placeList;
        this.appointmentLongList = appointmentLongList;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public BarberAppointmentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_barber_appointments,parent,false);
        return new BarberAppointmentsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BarberAppointmentsHolder holder, final int position) {
        holder.barberAppointmentsUserIcon.setText(userNameList.get(position).substring(0,1));
        Random random = new Random();
        final int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        ((GradientDrawable) holder.barberAppointmentsUserIcon.getBackground()).setColor(color);

        holder.barberAppointmentsUserName.setText(userNameList.get(position));
        holder.barberAppointmentsTime.setText(appointmentTimeList.get(position));
        holder.barberAppointmentsService.setText(serviceNameList.get(position));
        holder.barberAppointmentsPrice.setText(String.format("%s TL", totalPriceList.get(position)));
        holder.barberAppointmentsPlace.setText(placeList.get(position));

        holder.barberAppointmentsCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setMessage("Randevu Silinsin Mi");

                alertDialog.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseFirestore.collection("UserFields").document(userIdList.get(position))
                                .collection("Appointments").document(String.valueOf(appointmentLongList.get(position))).delete();
                        firebaseFirestore.collection("BarberFields").document(firebaseAuth.getUid())
                                .collection("Appointments").document(String.valueOf(appointmentLongList.get(position))).delete();
                        firebaseFirestore.collection("BarberFields").document(firebaseAuth.getUid())
                                .collection("Customers").document(String.valueOf(appointmentLongList.get(position))).delete();

                        userNameList.remove(position);
                        appointmentTimeList.remove(position);
                        serviceNameList.remove(position);
                        totalPriceList.remove(position);
                        userIdList.remove(position);
                        placeList.remove(position);
                        appointmentLongList.remove(position);

                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,getItemCount());

                    }
                });

                alertDialog.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialog.create().show();

                holder.barberAppointmentsCardView.setClickable(false);
                return true;
            }
        });

        holder.barberAppointmentsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toUserDetail = new Intent(v.getContext(), CustomerDetailActivity.class);
                toUserDetail.putExtra("iconColor",color);
                toUserDetail.putExtra("userName",userNameList.get(position));
                toUserDetail.putExtra("appDate",appointmentTimeList.get(position));
                toUserDetail.putExtra("serviceName",serviceNameList.get(position));
                toUserDetail.putExtra("totalPrice",totalPriceList.get(position));
                toUserDetail.putExtra("userId",userIdList.get(position));

                toUserDetail.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(toUserDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userNameList.size();
    }

    public class BarberAppointmentsHolder extends RecyclerView.ViewHolder{
        TextView barberAppointmentsUserIcon;
        TextView barberAppointmentsUserName;
        TextView barberAppointmentsTime;
        TextView barberAppointmentsService;
        TextView barberAppointmentsPrice;
        TextView barberAppointmentsPlace;
        CardView barberAppointmentsCardView;

        public BarberAppointmentsHolder(@NonNull View itemView) {
            super(itemView);
            barberAppointmentsUserIcon = itemView.findViewById(R.id.barberAppointmentsUserIcon);
            barberAppointmentsUserName = itemView.findViewById(R.id.barberAppointmentsUserName);
            barberAppointmentsTime = itemView.findViewById(R.id.barberAppointmentsTime);
            barberAppointmentsService = itemView.findViewById(R.id.barberAppointmentsService);
            barberAppointmentsPrice = itemView.findViewById(R.id.barberAppointmentsPrice);
            barberAppointmentsPlace = itemView.findViewById(R.id.barberAppointmentsPlace);
            barberAppointmentsCardView = itemView.findViewById(R.id.barberAppointmentsCardView);
        }
    }
}
