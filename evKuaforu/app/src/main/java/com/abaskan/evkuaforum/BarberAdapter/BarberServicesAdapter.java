package com.abaskan.evkuaforum.BarberAdapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abaskan.evkuaforum.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BarberServicesAdapter extends RecyclerView.Adapter<BarberServicesAdapter.BarberServicesHolder>{
    private Context context;
    private ArrayList<String> serviceName;
    private ArrayList<String> servicePrice;
    private ArrayList<String> serviceTime;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public BarberServicesAdapter(Context context, ArrayList<String> serviceName, ArrayList<String> servicePrice, ArrayList<String> serviceTime) {
        this.context = context;
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.serviceTime = serviceTime;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public BarberServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_barber_service_list,parent,false);
        return new BarberServicesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarberServicesHolder holder, final int position) {
        holder.barberServiceListName.setText(serviceName.get(position));
        holder.barberServiceListPrice.setText(servicePrice.get(position));
        holder.barberServiceListTime.setText(serviceTime.get(position));



        holder.barberServiceListCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setMessage("Servis Silinsin Mi");

                alertDialog.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CollectionReference collectionReference = firebaseFirestore.collection("BarberFields").document(firebaseAuth.getUid())
                                .collection("Services");

                        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                int count = 0, state = 5;
                                if (queryDocumentSnapshots != null) {
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                        if(documentSnapshot.getData() != null && getItemCount() > 0 && state == 5){
                                            if(count == position){
                                                state = 4;
                                                firebaseFirestore.collection("BarberFields").document(firebaseAuth.getUid())
                                                        .collection("Services").document(documentSnapshot.getId()).delete();
                                                serviceName.remove(position);
                                                servicePrice.remove(position);
                                                serviceTime.remove(position);

                                                notifyItemRemoved(position);
                                                notifyItemRangeChanged(position,getItemCount());

                                            }
                                            count++;
                                        }
                                    }
                                }
                            }
                        });

                    }
                });

                alertDialog.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialog.create().show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceName.size();
    }

    public class BarberServicesHolder extends RecyclerView.ViewHolder{
        CardView barberServiceListCardView;
        TextView barberServiceListName;
        TextView barberServiceListPrice;
        TextView barberServiceListTime;
        public BarberServicesHolder(@NonNull View itemView) {
            super(itemView);
            barberServiceListCardView = itemView.findViewById(R.id.barberServiceListCardView);
            barberServiceListName = itemView.findViewById(R.id.barberServiceListName);
            barberServiceListPrice = itemView.findViewById(R.id.barberServiceListPrice);
            barberServiceListTime = itemView.findViewById(R.id.barberServiceListTime);
        }
    }
}
