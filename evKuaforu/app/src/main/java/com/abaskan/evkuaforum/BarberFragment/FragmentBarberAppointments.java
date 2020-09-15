package com.abaskan.evkuaforum.BarberFragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abaskan.evkuaforum.BarberAdapter.BarberAppointmentsAdapter;
import com.abaskan.evkuaforum.R;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


public class FragmentBarberAppointments extends Fragment {
    Toolbar barberAppointmentsToolbar;
    RecyclerView barberAppointmentsRv;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<String> userNameList;
    private ArrayList<String> appointmentTimeList;
    private ArrayList<String> serviceNameList;
    private ArrayList<Integer> totalPriceList;
    private ArrayList<Long> appointmentLongList;
    ArrayList<String> userIdList;
    private ArrayList<String> placeList;
    BarberAppointmentsAdapter barberAppointmentsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_barber_appointments, container, false);
        toolbar(view);
        findViewById(view);
        getAppointmentsFromFirebase();
        setAdapter(view);

        return view;
    }

    public void toolbar(View view){
        barberAppointmentsToolbar = view.findViewById(R.id.barberAppointmentsToolbar);
        barberAppointmentsToolbar.setTitle("     Randevularım");
        barberAppointmentsToolbar.setTitleTextColor(Color.WHITE);
    }

    public void findViewById(View view){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userNameList = new ArrayList<>();
        appointmentTimeList = new ArrayList<>();
        serviceNameList = new ArrayList<>();
        totalPriceList = new ArrayList<>();
        userIdList = new ArrayList<>();
        placeList = new ArrayList<>();
        appointmentLongList = new ArrayList<>();
    }

    public void getAppointmentsFromFirebase() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        Date today = Calendar.getInstance().getTime();
        final String date = dateFormat.format(today);
        long dateLong = Long.parseLong(date);
        CollectionReference collectionReference = firebaseFirestore.collection("BarberFields").document(firebaseAuth.getUid())
            .collection("Appointments");

    collectionReference.whereGreaterThanOrEqualTo("appointmentLong",dateLong)
            .orderBy("appointmentLong", Query.Direction.ASCENDING)
            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
        @Override
        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            if(queryDocumentSnapshots != null){
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                    Map<String,Object> appointmentsData = documentSnapshot.getData();
                    final String appointmentsTime = appointmentsData.get("date") + " " + appointmentsData.get("hour");
                    final String serviceName = (String) appointmentsData.get("serviceName");
                    final String userId = (String) appointmentsData.get("userId");
                    final double totalPrice = (double) appointmentsData.get("totalPrice");
                    String userName = (String) appointmentsData.get("userName");
                    String place = (String) appointmentsData.get("place");
                    long appointmentLong = (long) appointmentsData.get("appointmentLong");
                    appointmentTimeList.add(appointmentsTime);
                    serviceNameList.add(serviceName);
                    userIdList.add(userId);
                    totalPriceList.add((int) totalPrice);
                    userNameList.add(userName);
                    placeList.add(place);
                    appointmentLongList.add(appointmentLong);
                    barberAppointmentsAdapter.notifyDataSetChanged();
                }
            }
        }
    });

    }

    public void setAdapter (View view){
        barberAppointmentsRv = view.findViewById(R.id.barberAppointmentsRv);
        barberAppointmentsRv.setHasFixedSize(true);
        barberAppointmentsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        barberAppointmentsAdapter = new BarberAppointmentsAdapter(getActivity(),userNameList,appointmentTimeList,serviceNameList,totalPriceList,userIdList,placeList,appointmentLongList);
        barberAppointmentsRv.setAdapter(barberAppointmentsAdapter);
    }

}
