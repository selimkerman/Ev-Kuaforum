package com.abaskan.evkuaforum.UserActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.abaskan.evkuaforum.R;
import com.abaskan.evkuaforum.UserAdapter.UserAppointmentHistoryAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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

public class UserAppointmentHistoryActivity extends AppCompatActivity {
    Toolbar userAppointmentsHistoryToolbar;
    RecyclerView userAppointmentsHistoryRv;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<String> barberImageUrlList;
    ArrayList<String> barberNameList;
    ArrayList<String> appointmentTimeList;
    ArrayList<String> serviceNamelist;
    ArrayList <Integer> totalPriceList;
    ArrayList<String> barberIdList;
    ArrayList<String> placeList;
    ArrayList<Boolean> addingCommentList;
    ArrayList <Long> appointmentLongList;
    UserAppointmentHistoryAdapter userAppointmentHistoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_appointment_history);

        toolbar();
        findViewById();
        getAppointmentsFromFirebase();
        setAdapter();
    }

    public void toolbar(){
        userAppointmentsHistoryToolbar = findViewById(R.id.userAppointmentsHistoryToolbar);
        userAppointmentsHistoryToolbar.setTitle("Randevu Geçmişim");
        userAppointmentsHistoryToolbar.setTitleTextColor(Color.WHITE);
        userAppointmentsHistoryToolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        setSupportActionBar(userAppointmentsHistoryToolbar);
        userAppointmentsHistoryToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void findViewById(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        barberImageUrlList = new ArrayList<>();
        barberNameList = new ArrayList<>();
        appointmentTimeList = new ArrayList<>();
        serviceNamelist = new ArrayList<>();
        totalPriceList = new ArrayList<>();
        barberIdList = new ArrayList<>();
        addingCommentList = new ArrayList<>();
        appointmentLongList = new ArrayList<>();
        placeList = new ArrayList<>();
    }

    public void getAppointmentsFromFirebase(){

        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        Date today = Calendar.getInstance().getTime();
        final String date = dateFormat.format(today);
        long dateLong = Long.parseLong(date);

        CollectionReference collectionReference = firebaseFirestore.collection("UserFields").document(firebaseAuth.getUid())
                .collection("Appointments");
        collectionReference.whereLessThan("appointmentLong",dateLong).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots != null){
                    for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String,Object> appointmentsData = documentSnapshot.getData();
                        final String barberId = (String) appointmentsData.get("barberId");
                        final String appointmentsTime = appointmentsData.get("date") + " " + appointmentsData.get("hour");
                        final String serviceName = (String) appointmentsData.get("serviceName");
                        final String place = (String) appointmentsData.get("place");
                        final double totalPrice = documentSnapshot.getDouble("totalPrice");
                        final long appointmentLong = (long) appointmentsData.get("appointmentLong");
                        boolean addComment = false;

                        if(documentSnapshot.contains("addedComment")){
                            addComment = true;
                        }

                        addingCommentList.add(addComment);

                        DocumentReference barberReference = firebaseFirestore.collection("Barbers").document(barberId);
                        barberReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Map<String,Object> barberData = documentSnapshot.getData();
                                String barberImageUrl = (String) barberData.get("barberImageUrl");
                                String barberName = (String) barberData.get("barberStoreName");

                                barberImageUrlList.add(barberImageUrl);
                                barberNameList.add(barberName);
                                appointmentTimeList.add(appointmentsTime);
                                serviceNamelist.add(serviceName);
                                totalPriceList.add((int) totalPrice);
                                barberIdList.add(barberId);
                                appointmentLongList.add(appointmentLong);
                                placeList.add(place);
                                userAppointmentHistoryAdapter.notifyDataSetChanged();
                            }
                        });

                    }

                }
            }
        });
    }

    public void setAdapter (){
        userAppointmentsHistoryRv = findViewById(R.id.userAppointmentsHistoryRv);
        userAppointmentsHistoryRv.setHasFixedSize(true);
        userAppointmentsHistoryRv.setLayoutManager(new LinearLayoutManager(this));
        userAppointmentHistoryAdapter = new UserAppointmentHistoryAdapter(this,barberImageUrlList,barberNameList,appointmentTimeList,serviceNamelist,totalPriceList,barberIdList,addingCommentList,appointmentLongList,placeList);
        userAppointmentsHistoryRv.setAdapter(userAppointmentHistoryAdapter);
    }
}
