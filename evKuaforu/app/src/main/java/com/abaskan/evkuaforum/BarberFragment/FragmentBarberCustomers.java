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

import com.abaskan.evkuaforum.BarberAdapter.BarberCustomersAdapter;
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

public class FragmentBarberCustomers extends Fragment {
    Toolbar barberCustomersToolbar;
    RecyclerView barberCustomersRv;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<String> userNameList;
    private ArrayList<String> appointmentTimeList;
    private ArrayList<String> serviceNameList;
    private ArrayList<Double> distanceList;
    private ArrayList<Integer> totalPriceList;
    private ArrayList<String> adressList;
    private ArrayList<Long> appointmentLongList;
    ArrayList<String> userIdList;
    BarberCustomersAdapter barberCustomersAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_barber_customers, container, false);

        toolbar(view);
        findViewById();
        getCustomersFomFirebase();
        setAdapter(view);


        return view;
    }

    public void toolbar(View view) {
        barberCustomersToolbar = view.findViewById(R.id.barberCustomersToolbar);
        barberCustomersToolbar.setTitle("     Müşterilerim");
        barberCustomersToolbar.setTitleTextColor(Color.WHITE);
    }

    public void findViewById() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userNameList = new ArrayList<>();
        appointmentTimeList = new ArrayList<>();
        serviceNameList = new ArrayList<>();
        distanceList = new ArrayList<Double>();
        userIdList = new ArrayList<>();
        adressList = new ArrayList<>();
        totalPriceList = new ArrayList<>();
        appointmentLongList = new ArrayList<>();
    }

    public void getCustomersFomFirebase() {

        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        Date today = Calendar.getInstance().getTime();
        final String date = dateFormat.format(today);
        long dateLong = Long.parseLong(date);

        final CollectionReference collectionReference = firebaseFirestore.collection("BarberFields").document(firebaseAuth.getUid())
                .collection("Customers");

        collectionReference.whereGreaterThanOrEqualTo("appointmentLong",dateLong)
                .orderBy("appointmentLong")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        Map<String, Object> customersData = documentSnapshot.getData();
                        final String appointmentsTime = customersData.get("date") + " " + customersData.get("hour");
                        final String serviceName = (String) customersData.get("serviceName");
                        final String userId = (String) customersData.get("userId");
                        double distance = (double) customersData.get("distance");
                        final double totalPrice = (double) customersData.get("totalPrice");
                        String userName = (String) customersData.get("userName");
                        long appointmentLong = (long) customersData.get("appointmentLong");
                        String adress = customersData.get("userAdress") + " " + customersData.get("userDistrict") + ", " + customersData.get("userProvince");

                        appointmentTimeList.add(appointmentsTime);
                        serviceNameList.add(serviceName);
                        userIdList.add(userId);
                        distanceList.add(distance);
                        totalPriceList.add((int) totalPrice);
                        userNameList.add(userName);
                        adressList.add(adress);
                        appointmentLongList.add(appointmentLong);
                        barberCustomersAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }


    public void setAdapter(View view) {
        barberCustomersRv = view.findViewById(R.id.barberCustomersRv);
        barberCustomersRv.setHasFixedSize(true);
        barberCustomersRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        barberCustomersAdapter = new BarberCustomersAdapter(getActivity(), userNameList, appointmentTimeList, serviceNameList, userIdList, distanceList, totalPriceList, adressList,appointmentLongList);
        barberCustomersRv.setAdapter(barberCustomersAdapter);
    }
}
