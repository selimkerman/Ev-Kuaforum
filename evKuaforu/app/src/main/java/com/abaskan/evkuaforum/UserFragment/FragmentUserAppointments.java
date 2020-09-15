package com.abaskan.evkuaforum.UserFragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abaskan.evkuaforum.R;
import com.abaskan.evkuaforum.UserAdapter.UserAppointmentsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class FragmentUserAppointments extends Fragment {
    Toolbar userAppointmentsToolbar;
    RecyclerView userAppointmentsRv;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<String> barberImageUrlList;
    ArrayList<String> barberNameList;
    ArrayList<String> appointmentTimeList;
    ArrayList<String> serviceNamelist;
    ArrayList<Integer> totalPriceList;
    ArrayList<String> barberIdList;
    ArrayList<String> placeList;
    ArrayList<Long> appointmentLongList;
    UserAppointmentsAdapter userAppointmentsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_appointments, container, false);
        toolbar(view);
        findViewById(view);
        getAppointmentsFromFirebase();
        setAdapter(view);
        return view;
    }

    public void toolbar(View view) {
        userAppointmentsToolbar = view.findViewById(R.id.userAppointmentsToolbar);
        userAppointmentsToolbar.setTitle("     Randevularım");
        userAppointmentsToolbar.setTitleTextColor(Color.WHITE);
    }

    public void findViewById(View view) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        barberImageUrlList = new ArrayList<>();
        barberNameList = new ArrayList<>();
        appointmentTimeList = new ArrayList<>();
        serviceNamelist = new ArrayList<>();
        totalPriceList = new ArrayList<>();
        barberIdList = new ArrayList<>();
        placeList = new ArrayList<>();
        appointmentLongList = new ArrayList<>();
    }

    public void getAppointmentsFromFirebase() {

        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        Date today = Calendar.getInstance().getTime();
        final String date = dateFormat.format(today);
        long dateLong = Long.parseLong(date);

        CollectionReference collectionReference = firebaseFirestore.collection("UserFields").document(firebaseAuth.getUid())
                .collection("Appointments");
        collectionReference.whereGreaterThanOrEqualTo("appointmentLong", dateLong)
                .orderBy("appointmentLong", Query.Direction.ASCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        Map<String, Object> appointmentsData = documentSnapshot.getData();
                        final String barberId = (String) appointmentsData.get("barberId");
                        final String appointmentsTime = appointmentsData.get("date") + " " + appointmentsData.get("hour");
                        final String serviceName = (String) appointmentsData.get("serviceName");
                        final String place = (String) appointmentsData.get("place");
                        final double totalPrice = documentSnapshot.getDouble("totalPrice");
                        final long appointmentLong = (long) appointmentsData.get("appointmentLong");

                        DocumentReference barberReference = firebaseFirestore.collection("Barbers").document(barberId);
                        barberReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Map<String, Object> barberData = documentSnapshot.getData();
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

                                userAppointmentsAdapter.notifyDataSetChanged();
                            }
                        });


                    }
                }
            }
        });
    }

    public void setAdapter(View view) {
        userAppointmentsRv = view.findViewById(R.id.userAppointmentsRv);
        userAppointmentsRv.setHasFixedSize(true);
        userAppointmentsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        userAppointmentsAdapter = new UserAppointmentsAdapter(getActivity(), barberImageUrlList, barberNameList, appointmentTimeList, serviceNamelist, totalPriceList, barberIdList, placeList, appointmentLongList);
        userAppointmentsRv.setAdapter(userAppointmentsAdapter);
    }
}
