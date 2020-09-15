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

import com.abaskan.evkuaforum.BarberActivity.BarberMapsActivity;
import com.abaskan.evkuaforum.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class BarberCustomersAdapter extends RecyclerView.Adapter<BarberCustomersAdapter.BarberCustomersHolder>{
    private Context context;
    private ArrayList<String> userNameList;
    private ArrayList<String> appointmentTimeList;
    private ArrayList<String> serviceNameList;
    private ArrayList<String> userIdList;
    private ArrayList<Double> distanceList;
    private ArrayList<Integer> totalPriceList;
    private ArrayList<String> adressList;
    private ArrayList<Long> appointmentLongList;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    public BarberCustomersAdapter(Context context, ArrayList<String> userNameList, ArrayList<String> appointmentTimeList, ArrayList<String> serviceNameList, ArrayList<String> userIdList, ArrayList<Double> distanceList, ArrayList<Integer> totalPriceList, ArrayList<String> adressList, ArrayList<Long> appointmentLongList) {
        this.context = context;
        this.userNameList = userNameList;
        this.appointmentTimeList = appointmentTimeList;
        this.serviceNameList = serviceNameList;
        this.userIdList = userIdList;
        this.distanceList = distanceList;
        this.totalPriceList = totalPriceList;
        this.adressList = adressList;
        this.appointmentLongList = appointmentLongList;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public BarberCustomersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_barber_customers,parent,false);
        return new BarberCustomersHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BarberCustomersHolder holder, final int position) {
        holder.barberCustomersUserIcon.setText(userNameList.get(position).substring(0,1));
        Random random = new Random();
        final int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        ((GradientDrawable) holder.barberCustomersUserIcon.getBackground()).setColor(color);

        holder.barberCustomersUserName.setText(userNameList.get(position));
        holder.barberCustomersTime.setText(appointmentTimeList.get(position));
        holder.barberCustomersService.setText(serviceNameList.get(position));
        if (distanceList.get(position) == 0){
            holder.barberCustomersDistance.setText("Yer : İş Yeri");
        }else {
            holder.barberCustomersDistance.setText(distanceList.get(position) + " Km");
        }
        holder.barberCustomersPrice.setText(totalPriceList.get(position) + " TL");

        holder.barberCustomersCardView.setOnLongClickListener(new View.OnLongClickListener() {
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
                        adressList.remove(position);
                        distanceList.remove(position);
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

                holder.barberCustomersCardView.setClickable(false);
                return true;
            }
        });

        holder.barberCustomersCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference documentReference = firebaseFirestore.collection("Users").document(userIdList.get(position));
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map userInfo = documentSnapshot.getData();
                        final double userLat = (double) userInfo.get("userAdressLat");
                        final double userLng = (double) userInfo.get("userAdressLng");

                        DocumentReference documentReference1 = firebaseFirestore.collection("Barbers")
                                .document(firebaseAuth.getUid());
                        documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                double barberLat = documentSnapshot.getDouble("barberAdressLat");
                                double barberLng = documentSnapshot.getDouble("barberAdressLng");
                                String storeName = documentSnapshot.getString("barberStoreName");

                                Intent toMap = new Intent(context, BarberMapsActivity.class);
                                toMap.putExtra("userName",userNameList.get(position));
                                toMap.putExtra("userId",userIdList.get(position));
                                toMap.putExtra("userAdress",adressList.get(position));
                                toMap.putExtra("userLat",userLat);
                                toMap.putExtra("userLng",userLng);
                                toMap.putExtra("distance",distanceList.get(position));
                                toMap.putExtra("barberLat",barberLat);
                                toMap.putExtra("barberLng",barberLng);
                                toMap.putExtra("storeName",storeName);
                                toMap.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(toMap);
                            }
                        });





                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return userNameList.size();
    }

    public class BarberCustomersHolder extends RecyclerView.ViewHolder{
        TextView barberCustomersUserIcon;
        TextView barberCustomersUserName;
        TextView barberCustomersTime;
        TextView barberCustomersService;
        TextView barberCustomersDistance;
        TextView barberCustomersPrice;
        CardView barberCustomersCardView;
        public BarberCustomersHolder(@NonNull View itemView) {
            super(itemView);
            barberCustomersUserIcon = itemView.findViewById(R.id.barberCustomersUserIcon);
            barberCustomersUserName = itemView.findViewById(R.id.barberCustomersUserName);
            barberCustomersTime = itemView.findViewById(R.id.barberCustomersTime);
            barberCustomersService = itemView.findViewById(R.id.barberCustomersService);
            barberCustomersDistance = itemView.findViewById(R.id.barberCustomersDistance);
            barberCustomersPrice = itemView.findViewById(R.id.barberCustomersPrice);
            barberCustomersCardView = itemView.findViewById(R.id.barberCustomersCardView);
        }
    }
}
