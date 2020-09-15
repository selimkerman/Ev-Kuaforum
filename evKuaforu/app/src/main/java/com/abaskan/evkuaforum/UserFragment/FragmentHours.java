package com.abaskan.evkuaforum.UserFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abaskan.evkuaforum.UserAdapter.HoursAdapter;
import com.abaskan.evkuaforum.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FragmentHours extends Fragment {
    RecyclerView appointmentsHourRv;
    CalendarView bookAppointmentCalendarView;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ArrayList<String> hourList;
    HoursAdapter hoursAdapter;
    ArrayList<String> selectedHourList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hours, container, false);
        appointmentsHourRv = view.findViewById(R.id.appointmentsHourRv);
        bookAppointmentCalendarView = view.findViewById(R.id.bookAppointmentCalendarView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        hourList = new ArrayList<>();
        selectedHourList = new ArrayList<>();
        setHourAdapter(view);
        getDate();
        return view;
    }

    public void getDate() {
        bookAppointmentCalendarView.setMinDate(System.currentTimeMillis());
        bookAppointmentCalendarView.setFirstDayOfWeek(2);
        bookAppointmentCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                hourList.clear();
                selectedHourList.clear();
                String date = dateFormat(dayOfMonth) + "." + dateFormat((month + 1)) + "." + year;
                long longDate = Long.parseLong(year + dateFormat((month + 1)) + dateFormat(dayOfMonth));
                String formatTime = String.valueOf(longDate);
                Map<String, Object> data = new HashMap<>();
                data.put("time", date);
                data.put("formatTime", formatTime);
                data.put("hour","null");
                data.put("formatHour","null");

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                String dayName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);

                getSelectedHours(date);
                getHourFromFirebase(dayName);

                firebaseFirestore.collection("UserFields").document(firebaseAuth.getUid())
                        .collection("SelectedHours").document("Time").update(data);
            }

        });

    }

    public void getHourFromFirebase(String dayName) {
        String barberId = getActivity().getIntent().getStringExtra("barberId");
        DocumentReference collectionReference = firebaseFirestore.collection("BarberFields").document(barberId)
                .collection("WorkingHours").document(dayName);

        collectionReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                //assert documentSnapshot != null;
                if (documentSnapshot != null) {
                    if (documentSnapshot.getData() != null) {
                        Map<String, Object> hoursData = documentSnapshot.getData();
                        String hours = (String) hoursData.get("hours");
                        if (hours != null) {
                            String[] hour = hours.split(" ");

                            for (String h : hour) {
                                hourList.add(h);
                            }
                        }
                    }

                    hoursAdapter.notifyDataSetChanged();
                }
            }

        });

    }

    public void getSelectedHours(String date) {
        String barberId = getActivity().getIntent().getStringExtra("barberId");
        CollectionReference collectionReference = firebaseFirestore.collection("BarberFields")
                .document(barberId).collection("Appointments");
        collectionReference.whereEqualTo("date", date).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    for (DocumentSnapshot documentSnapshot1 : queryDocumentSnapshots.getDocuments()) {
                        if (documentSnapshot1.getData() != null) {
                            String hour = (String) documentSnapshot1.get("hour");
                            selectedHourList.add(hour);
                        }
                    }
                }
                hoursAdapter.notifyDataSetChanged();
            }
        });


    }


    public void setHourAdapter(View view) {
        appointmentsHourRv = view.findViewById(R.id.appointmentsHourRv);
        appointmentsHourRv.setHasFixedSize(true);
        appointmentsHourRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        hoursAdapter = new HoursAdapter(getActivity(), hourList, selectedHourList);
        appointmentsHourRv.setAdapter(hoursAdapter);
    }

    public String dateFormat(int value) {
        if (value >= 10) {
            return String.valueOf(value);
        }
        return "0" + value;
    }
}
