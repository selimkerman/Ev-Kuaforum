package com.abaskan.evkuaforum.UserAdapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.abaskan.evkuaforum.R;
import com.abaskan.evkuaforum.UserActivity.UserAppBarberDetailActivity;
import com.abaskan.evkuaforum.UserActivity.UserMainActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAppointmentsAdapter extends RecyclerView.Adapter<UserAppointmentsAdapter.UserAppointmentsHolder> {
    private Context context;
    private ArrayList<String> barberImageUrlList;
    private ArrayList<String> barberNameList;
    private ArrayList<String> appointmentTimeList;
    private ArrayList<String> serviceNameList;
    private ArrayList<Integer> totalPriceList;
    private ArrayList<String> barberIdList;
    private ArrayList<String> placeList;
    private ArrayList<Long> appointmentLongList;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    public UserAppointmentsAdapter(Context context, ArrayList<String> barberImageUrlList, ArrayList<String> barberNameList, ArrayList<String> appointmentTimeList, ArrayList<String> serviceNameList, ArrayList<Integer> totalPriceList, ArrayList<String> barberIdList, ArrayList<String> placeList, ArrayList<Long> appointmentLongList) {
        this.context = context;
        this.barberImageUrlList = barberImageUrlList;
        this.barberNameList = barberNameList;
        this.appointmentTimeList = appointmentTimeList;
        this.serviceNameList = serviceNameList;
        this.totalPriceList = totalPriceList;
        this.barberIdList = barberIdList;
        this.placeList = placeList;
        this.appointmentLongList = appointmentLongList;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public UserAppointmentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_user_appointments,parent,false);
        return new UserAppointmentsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserAppointmentsHolder holder, final int position) {
        Picasso.get().load(barberImageUrlList.get(position)).into(holder.userAppointmentsImageView);
        holder.userAppointmentsBarberName.setText(barberNameList.get(position));
        holder.userAppointmentsDate.setText(appointmentTimeList.get(position));
        holder.userAppointmentsService.setText(serviceNameList.get(position));
        holder.userAppointmentsPrice.setText(String.format("%s TL", totalPriceList.get(position)));
        holder.userAppointmentsPlace.setText(placeList.get(position));


        holder.userAppointmentCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                //alertDialog.setTitle("Randevu Silinsin Mi ?");
                alertDialog.setMessage("Randevu Silinsin Mi");
                alertDialog.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        firebaseFirestore.collection("UserFields").document(firebaseAuth.getUid())
                                .collection("Appointments").document(String.valueOf(appointmentLongList.get(position))).delete();
                        firebaseFirestore.collection("BarberFields").document(barberIdList.get(position))
                                .collection("Appointments").document(String.valueOf(appointmentLongList.get(position))).delete();
                        firebaseFirestore.collection("BarberFields").document(barberIdList.get(position))
                                .collection("Customers").document(String.valueOf(appointmentLongList.get(position))).delete();

                        barberImageUrlList.remove(position);
                        barberNameList.remove(position);
                        appointmentTimeList.remove(position);
                        serviceNameList.remove(position);
                        totalPriceList.remove(position);
                        barberIdList.remove(position);
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

                holder.userAppointmentCardView.setClickable(false);
                return true;
            }
        });


        holder.userAppointmentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toBarberDetail = new Intent(v.getContext(), UserAppBarberDetailActivity.class);
                toBarberDetail.putExtra("barberName",barberNameList.get(position));
                toBarberDetail.putExtra("appDate",appointmentTimeList.get(position));
                toBarberDetail.putExtra("serviceName",serviceNameList.get(position));
                toBarberDetail.putExtra("barberId",barberIdList.get(position));
                toBarberDetail.putExtra("totalPrice",totalPriceList.get(position));
                toBarberDetail.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(toBarberDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return barberNameList.size();
    }

    public class UserAppointmentsHolder extends RecyclerView.ViewHolder {
        ImageView userAppointmentsImageView;
        TextView userAppointmentsBarberName;
        TextView userAppointmentsDate;
        TextView userAppointmentsService;
        TextView userAppointmentsPrice;
        TextView userAppointmentsPlace;
        CardView userAppointmentCardView;
        public UserAppointmentsHolder(@NonNull View itemView) {
            super(itemView);
            userAppointmentsImageView = itemView.findViewById(R.id.userAppointmentsImageView);
            userAppointmentsBarberName = itemView.findViewById(R.id.userAppointmentsBarberName);
            userAppointmentsDate = itemView.findViewById(R.id.userAppointmentsDate);
            userAppointmentsService = itemView.findViewById(R.id.userAppointmentsService);
            userAppointmentsPrice = itemView.findViewById(R.id.userAppointmentsPrice);
            userAppointmentsPlace = itemView.findViewById(R.id.userAppointmentsPlace);
            userAppointmentCardView = itemView.findViewById(R.id.userAppointmentCardView);
        }
    }
}
