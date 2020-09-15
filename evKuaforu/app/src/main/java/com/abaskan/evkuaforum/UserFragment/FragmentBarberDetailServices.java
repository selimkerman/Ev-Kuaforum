package com.abaskan.evkuaforum.UserFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abaskan.evkuaforum.BarberAdapter.BarberDetailServicesAdapter;
import com.abaskan.evkuaforum.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class FragmentBarberDetailServices extends Fragment {
    ImageView barberDetailServicesImageView;
    TextView barberDetailServicesStoreName, barberDetailServicesProvince, barberDetailServicesPrice;
    RecyclerView barberDetailServicesRv;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    ArrayList<String> serviceName;
    ArrayList<String> servicePrice;
    ArrayList<String> serviceTime;
    String barberId;
    String barberName;
    BarberDetailServicesAdapter barberDetailServicesAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_barber_detail_services, container, false);
        findViewById(view);
        getServicesFromFirebase();
        setAdapterforServices(view);
        getBarberInfoFromFirebase();

        return view;
    }

    public void findViewById (View view){
        barberDetailServicesImageView = view.findViewById(R.id.barberDetailServicesImageView);
        barberDetailServicesStoreName = view.findViewById(R.id.barberDetailServicesStoreName);
        barberDetailServicesProvince = view.findViewById(R.id.barberDetailServicesProvince);
        barberDetailServicesPrice = view.findViewById(R.id.barberDetailServicesPrice);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        serviceName = new ArrayList<>();
        servicePrice = new ArrayList<>();
        serviceTime = new ArrayList<>();
    }

    public void getBarberInfoFromFirebase(){
        String uid = getActivity().getIntent().getStringExtra("barberId");
        final DocumentReference documentReference = firebaseFirestore.collection("Barbers")
                .document(uid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> data = documentSnapshot.getData();
                String url = (String) data.get("barberImageUrl");
                double price = documentSnapshot.getDouble("barberPrice");
                String name = (String) data.get("barberStoreName");
                String province = data.get("barberDistrict") + ", " + data.get("barberProvince");

                Picasso.get().load(url).into(barberDetailServicesImageView);
                barberDetailServicesStoreName.setText(name);
                barberDetailServicesProvince.setText(province);
               barberDetailServicesPrice.setText(String.format("%s TL", price));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getServicesFromFirebase() {
    String uid = getActivity().getIntent().getStringExtra("barberId");
        CollectionReference collectionReference = firebaseFirestore.collection("BarberFields").document(uid)
                .collection("Services");
        collectionReference.orderBy("time",Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots != null){
                    for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        Map<String, Object> serviceData = documentSnapshot.getData();
                        String name = (String) serviceData.get("serviceName");
                        String price = (String) serviceData.get("servicePrice");
                        String time = (String) serviceData.get("serviceTime");

                        serviceName.add(name);
                        servicePrice.add(price);
                        serviceTime.add(time + " dk");
                        barberDetailServicesAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public void setAdapterforServices(View view) {
        barberDetailServicesRv = view.findViewById(R.id.barberDetailServicesRv);
        barberDetailServicesRv.setHasFixedSize(true);
        barberDetailServicesRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        barberId = getActivity().getIntent().getStringExtra("barberId");
        barberName = getActivity().getIntent().getStringExtra("barberName");
        barberDetailServicesAdapter = new BarberDetailServicesAdapter(getActivity(), serviceName, servicePrice, serviceTime,barberId,barberName);
        barberDetailServicesRv.setAdapter(barberDetailServicesAdapter);
    }

}
