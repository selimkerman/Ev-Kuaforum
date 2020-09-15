package com.abaskan.evkuaforum.UserFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.abaskan.evkuaforum.BarberAdapter.BarberDetailHoursAdapter;
import com.abaskan.evkuaforum.R;
import com.abaskan.evkuaforum.UserActivity.UserChatActivity;
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
import java.util.HashMap;
import java.util.Map;

public class FragmentBarberDetailDetails extends Fragment {
    RecyclerView barberDetailDetailHourRv;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ArrayList<String> dayList;
    ArrayList<String> hourList;
    BarberDetailHoursAdapter barberDetailHoursAdapter;
    Button barberDetailToChatButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_barber_detail_details, container, false);
        findViewById(view);
        getHoursFromFirebase();
        setAdapterForHours(view);
        barberDetailToChatButtonClick(view);

        return view;
    }

    public void findViewById(View view) {
        barberDetailToChatButton = view.findViewById(R.id.barberDetailToChatButton);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        dayList = new ArrayList<>();
        hourList = new ArrayList<>();
    }

    public void getHoursFromFirebase() {
        String uid = getActivity().getIntent().getStringExtra("barberId");
        CollectionReference collectionReference = firebaseFirestore.collection("BarberFields").document(uid)
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
                        barberDetailHoursAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

    public void setAdapterForHours(View view) {
        barberDetailDetailHourRv = view.findViewById(R.id.barberDetailDetailHourRv);
        barberDetailDetailHourRv.setHasFixedSize(true);
        barberDetailDetailHourRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        barberDetailHoursAdapter = new BarberDetailHoursAdapter(getActivity(), dayList, hourList);
        barberDetailDetailHourRv.setAdapter(barberDetailHoursAdapter);
    }

    public void barberDetailToChatButtonClick(View view) {
        barberDetailToChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barberName = getActivity().getIntent().getStringExtra("barberName");
                final String barberId = getActivity().getIntent().getStringExtra("barberId");
                final Map<String, Object> newChat = new HashMap<>();
                newChat.put("barberName",barberName);
                newChat.put("barberId",barberId);
                newChat.put("userId",firebaseAuth.getUid());
                newChat.put("date",getDate());

                DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String,Object> userData = documentSnapshot.getData();
                        String userName = (String) userData.get("userName");
                        newChat.put("userName",userName);
                        firebaseFirestore.collection("UserFields").document(firebaseAuth.getUid()).collection("Chats")
                                .document(barberId).set(newChat);
                        firebaseFirestore.collection("BarberFields").document(barberId).collection("Chats")
                                .document(firebaseAuth.getUid()).set(newChat);
                    }
                });



                Intent toChat = new Intent(getActivity(), UserChatActivity.class);
                toChat.putExtra("barberId",barberId);
                toChat.putExtra("barberName",barberName);
                toChat.putExtra("userId",firebaseAuth.getUid());
                toChat.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivity(toChat);
            }
        });
    }

    public String getDate (){
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String date = dateFormat.format(today);
        return date;
    }
}
