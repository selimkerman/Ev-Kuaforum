package com.abaskan.evkuaforum.BarberActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.abaskan.evkuaforum.BarberAdapter.BarberAppointmentsHistoryAdapter;
import com.abaskan.evkuaforum.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class BarberAppointmentHistoryActivity extends AppCompatActivity {
    Toolbar barberAppointmentsHistoryToolbar;
    RecyclerView barberAppointmentsHistoryRv;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<String> userNameList;
    private ArrayList<String> appointmentsTimeList;
    private ArrayList<String> serviceNameList;
    private ArrayList<Integer> totalPriceList;
    private ArrayList<String> userIdList;
    private ArrayList<String> placeList;
    BarberAppointmentsHistoryAdapter barberAppointmentsHistoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_appointment_history);

        toolbar();
        findViewById();
        getAppointmentsFromFirebase();
        setAdapter();
    }

    public void toolbar(){
        barberAppointmentsHistoryToolbar = findViewById(R.id.barberAppointmentsHistoryToolbar);
        barberAppointmentsHistoryToolbar.setTitle("Randevu Geçmişim");
        barberAppointmentsHistoryToolbar.setTitleTextColor(Color.WHITE);
        barberAppointmentsHistoryToolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        setSupportActionBar(barberAppointmentsHistoryToolbar);
        barberAppointmentsHistoryToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void findViewById(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userNameList = new ArrayList<>();
        appointmentsTimeList = new ArrayList<>();
        serviceNameList = new ArrayList<>();
        totalPriceList = new ArrayList<>();
        userIdList = new ArrayList<>();
        placeList = new ArrayList<>();
    }

    public void getAppointmentsFromFirebase(){
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        Date today = Calendar.getInstance().getTime();
        final String date = dateFormat.format(today);
        long dateLong = Long.parseLong(date);

        CollectionReference collectionReference = firebaseFirestore.collection("BarberFields").document(firebaseAuth.getUid())
                .collection("Appointments");
        collectionReference.whereLessThan("appointmentLong",dateLong).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots != null){
                    for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String,Object> appointmentsData = documentSnapshot.getData();
                        final String userName = (String) appointmentsData.get("userName");
                        final String userId = (String) appointmentsData.get("userId");
                        final String appointmentsTime = appointmentsData.get("date") + " " + appointmentsData.get("hour");
                        final String serviceName = (String) appointmentsData.get("serviceName");
                        String place = (String) appointmentsData.get("place");
                        final double totalPrice = documentSnapshot.getDouble("totalPrice");

                        userNameList.add(userName);
                        userIdList.add(userId);
                        appointmentsTimeList.add(appointmentsTime);
                        serviceNameList.add(serviceName);
                        totalPriceList.add((int) totalPrice);
                        placeList.add(place);
                        barberAppointmentsHistoryAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }


    public void setAdapter (){
        barberAppointmentsHistoryRv = findViewById(R.id.barberAppointmentsHistoryRv);
        barberAppointmentsHistoryRv.setHasFixedSize(true);
        barberAppointmentsHistoryRv.setLayoutManager(new LinearLayoutManager(this));
        barberAppointmentsHistoryAdapter = new BarberAppointmentsHistoryAdapter(this,userNameList,appointmentsTimeList,serviceNameList,totalPriceList,userIdList,placeList);
        barberAppointmentsHistoryRv.setAdapter(barberAppointmentsHistoryAdapter);
    }
}
