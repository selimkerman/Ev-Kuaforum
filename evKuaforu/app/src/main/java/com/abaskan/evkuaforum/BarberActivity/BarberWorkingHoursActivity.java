package com.abaskan.evkuaforum.BarberActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.abaskan.evkuaforum.BarberAdapter.BarberWorkingHoursAdapter;
import com.abaskan.evkuaforum.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class BarberWorkingHoursActivity extends AppCompatActivity {
    Toolbar barberWorkingHoursToolbar;
    RecyclerView barberWorkingHoursRv;
    FloatingActionButton barberWorkingHoursFab;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ArrayList<String> dayList;
    ArrayList<String> hourList;
    BarberWorkingHoursAdapter barberWorkingHoursAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_working_hours);
        findViewById();
        toolbar();
        getHoursFromFirebase();
        setAdapterForHours();
    }

    public void findViewById() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        barberWorkingHoursFab = findViewById(R.id.barberWorkingHoursFab);
        dayList = new ArrayList<>();
        hourList = new ArrayList<>();

    }

    public void toolbar() {
        barberWorkingHoursToolbar = findViewById(R.id.barberWorkingHoursToolbar);
        barberWorkingHoursToolbar.setTitle("Çalışma Saatlerim");
        barberWorkingHoursToolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        setSupportActionBar(barberWorkingHoursToolbar);
        barberWorkingHoursToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void toAddWorkingHoursClick(View view){
                Intent toAddHours = new Intent(BarberWorkingHoursActivity.this, BarberAddWorkingHours.class);
                startActivity(toAddHours);
                finish();
    }

    public void getHoursFromFirebase (){
        CollectionReference collectionReference = firebaseFirestore.collection("BarberFields").document(firebaseAuth.getUid())
                .collection("WorkingHours");
        collectionReference.orderBy("sequence", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        Map<String, Object> hoursData = documentSnapshot.getData();
                        String day = (String) hoursData.get("day");
                        String hours = (String) hoursData.get("hours");

                        dayList.add(day);
                        hourList.add(hours);
                        barberWorkingHoursAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public void setAdapterForHours (){
        barberWorkingHoursRv = findViewById(R.id.barberWorkingHoursRv);
        barberWorkingHoursRv.setHasFixedSize(true);
        barberWorkingHoursRv.setLayoutManager(new LinearLayoutManager(this));
        barberWorkingHoursAdapter = new BarberWorkingHoursAdapter(this,dayList,hourList);
        barberWorkingHoursRv.setAdapter(barberWorkingHoursAdapter);
    }
}
