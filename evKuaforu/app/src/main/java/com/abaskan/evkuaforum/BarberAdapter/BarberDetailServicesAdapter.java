package com.abaskan.evkuaforum.BarberAdapter;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abaskan.evkuaforum.R;
import com.abaskan.evkuaforum.UserActivity.UserBookAppointmentActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Locale;

public class BarberDetailServicesAdapter extends RecyclerView.Adapter<BarberDetailServicesAdapter.BarberDetailServicesHolder>{
    private Context context;
    private ArrayList<String> serviceName;
    private ArrayList<String> servicePrice;
    private ArrayList<String> serviceTime;
    private String barberId;
    private String barberName;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    public BarberDetailServicesAdapter(Context context, ArrayList<String> serviceName, ArrayList<String> servicePrice, ArrayList<String> serviceTime,String barberId, String barberName) {
        this.context = context;
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.serviceTime = serviceTime;
        this.barberId = barberId;
        this.barberName = barberName;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public BarberDetailServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_barber_price_list,parent,false);
        return new BarberDetailServicesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarberDetailServicesHolder holder, final int position) {
        holder.barberPriceServiceName.setText(serviceName.get(position));
        holder.barberPriceServicePrice.setText(servicePrice.get(position) + " TL");
        holder.barberPriceServiceTime.setText(serviceTime.get(position));
        holder.buttonAddServicesToCart.setOnClickListener(new View.OnClickListener() {

            Location userLocation = new Location("");

            Location barberLocation = new Location("");

            @Override
            public void onClick(View v) {

                DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        double userLat = documentSnapshot.getDouble("userAdressLat");
                        double userLng = documentSnapshot.getDouble("userAdressLng");
                        userLocation.setLatitude(userLat);
                        userLocation.setLongitude(userLng);

                        DocumentReference documentReference1 = firebaseFirestore.collection("Barbers").document(barberId);
                        documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                double barberLat = documentSnapshot.getDouble("barberAdressLat");
                                double barberLng = documentSnapshot.getDouble("barberAdressLng");
                                double pricePerKm = documentSnapshot.getDouble("barberPrice");
                                barberLocation.setLatitude(barberLat);
                                barberLocation.setLongitude(barberLng);

                                double distanceInMeters = userLocation.distanceTo(barberLocation);
                                double distanceInKm = distanceInMeters / 1000.0;
                                String distanceFormat = String.format(Locale.US, "%.2f", distanceInKm);
                                double distanceDouble = Double.parseDouble(distanceFormat);

                                double extraPrice = Math.round((distanceInMeters * pricePerKm ) / 1000.0);

                                Intent toBookAppointment = new Intent(context, UserBookAppointmentActivity.class);
                                toBookAppointment.putExtra("barberId",barberId);
                                toBookAppointment.putExtra("serviceName",serviceName.get(position));
                                toBookAppointment.putExtra("servicePrice",servicePrice.get(position));
                                toBookAppointment.putExtra("serviceTime",serviceTime.get(position));
                                toBookAppointment.putExtra("distance",distanceDouble);
                                toBookAppointment.putExtra("extraPrice",extraPrice);
                                toBookAppointment.putExtra("barberName",barberName);
                                toBookAppointment.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(toBookAppointment);
                            }
                        });

                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceName.size();
    }

    public class BarberDetailServicesHolder extends RecyclerView.ViewHolder{
        CardView barberPriceListCardView;
        TextView barberPriceServiceName;
        TextView barberPriceServicePrice;
        TextView barberPriceServiceTime;
        Button buttonAddServicesToCart;
        public BarberDetailServicesHolder(@NonNull View itemView) {
            super(itemView);
            barberPriceListCardView = itemView.findViewById(R.id.barberPriceListCardView);
            barberPriceServiceName = itemView.findViewById(R.id.barberPriceServiceName);
            barberPriceServicePrice = itemView.findViewById(R.id.barberPriceServicePrice);
            barberPriceServiceTime = itemView.findViewById(R.id.barberPriceServiceTime);
            buttonAddServicesToCart = itemView.findViewById(R.id.buttonAddServicesToCart);
        }
    }
}
