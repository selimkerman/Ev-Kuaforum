package com.abaskan.evkuaforum.BarberActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.abaskan.evkuaforum.BarberAdapter.BarberServicesAdapter;
import com.abaskan.evkuaforum.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.HashMap;
import java.util.Map;

public class BarberStoreInfoActivity extends AppCompatActivity {
    ImageView barberStoreInfoImage;
    Toolbar barberStoreInfoToolbar;
    TextView barberStoreInfoPrice, barberStoreInfoWorkingHours;
    RecyclerView barberStoreInfoServicesRv;
    FloatingActionButton barberStoreInfoFab;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    ArrayList<String> serviceName;
    ArrayList<String> servicePrice;
    ArrayList<String> serviceTime;
    BarberServicesAdapter barberServicesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_store_info);
        findViewById();
        toolbar();
        getBarberPhotoAndPriceFromFirebase();
        getServicesFromFirebase();
        setAdapterforServices();

    }

    public void findViewById() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        barberStoreInfoImage = findViewById(R.id.barberStoreInfoImage);
        barberStoreInfoPrice = findViewById(R.id.barberStoreInfoPrice);
        barberStoreInfoWorkingHours = findViewById(R.id.barberStoreInfoWorkingHours);
        barberStoreInfoServicesRv = findViewById(R.id.barberStoreInfoServicesRv);
        barberStoreInfoFab = findViewById(R.id.barberStoreInfoFab);

        serviceName = new ArrayList<>();
        servicePrice = new ArrayList<>();
        serviceTime = new ArrayList<>();
    }

    public void toolbar() {
        barberStoreInfoToolbar = findViewById(R.id.barberStoreInfoToolbar);
        barberStoreInfoToolbar.setTitle("İşyeri Bilgilerim");
        barberStoreInfoToolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        setSupportActionBar(barberStoreInfoToolbar);
        barberStoreInfoToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void toAddNewBarberPhotoClick(View view) {
        Intent toAddPhoto = new Intent(BarberStoreInfoActivity.this, BarberAddPhotoActivity.class);
        startActivity(toAddPhoto);
        finish();
    }

    public void getBarberPhotoAndPriceFromFirebase() {
        final DocumentReference documentReference = firebaseFirestore.collection("Barbers")
                .document(firebaseAuth.getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> data = documentSnapshot.getData();
                String url = (String) data.get("barberImageUrl");
                double price = documentSnapshot.getDouble("barberPrice");
                barberStoreInfoPrice.setText(String.format("%s TL", price));
                if(!url.equals("null")){
                    Picasso.get().load(url).into(barberStoreInfoImage);
                }



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void getServicesFromFirebase() {
        CollectionReference collectionReference = firebaseFirestore.collection("BarberFields").document(firebaseAuth.getUid())
                .collection("Services");
        collectionReference.orderBy("time", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        Map<String, Object> serviceData = documentSnapshot.getData();
                        String name = (String) serviceData.get("serviceName");
                        String price = (String) serviceData.get("servicePrice");
                        String time = (String) serviceData.get("serviceTime");

                        serviceName.add(name);
                        servicePrice.add(price + " TL");
                        serviceTime.add(time + " dk");
                        barberServicesAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public void setAdapterforServices() {
        barberStoreInfoServicesRv = findViewById(R.id.barberStoreInfoServicesRv);
        barberStoreInfoServicesRv.setHasFixedSize(true);
        barberStoreInfoServicesRv.setLayoutManager(new LinearLayoutManager(this));
        barberServicesAdapter = new BarberServicesAdapter(this, serviceName, servicePrice, serviceTime);
        barberStoreInfoServicesRv.setAdapter(barberServicesAdapter);
    }


    public void toAddNewServiceClick(View view) {
        Intent toAddService = new Intent(BarberStoreInfoActivity.this, BarberAddServiceActivity.class);
        startActivity(toAddService);
        finish();
    }

    public void toWorkingHoursClick(View view) {
        Intent toWorkingHours = new Intent(BarberStoreInfoActivity.this, BarberWorkingHoursActivity.class);
        startActivity(toWorkingHours);
        finish();
    }

    public void toSetNewPricePerKmClick(View view) {
        View view1 = getLayoutInflater().inflate(R.layout.barber_price,null);

        final EditText pricePerKmEditText = view1.findViewById(R.id.pricePerKmEditText);

        AlertDialog.Builder alert = new AlertDialog.Builder(BarberStoreInfoActivity.this);

        alert.setTitle("Fiyat Giriniz");
        alert.setView(view1);

        alert.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                double price = Double.parseDouble(pricePerKmEditText.getText().toString());
                Map<String, Object> currentBarber = new HashMap<>();
                currentBarber.put("barberPrice", price);
                firebaseFirestore.collection("Barbers").document(firebaseAuth.getUid())
                        .update(currentBarber);
                barberStoreInfoPrice.setText(pricePerKmEditText.getText().toString() + " TL");
            }
        });

        alert.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.create().show();
    }

}
