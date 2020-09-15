package com.abaskan.evkuaforum.UserActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.abaskan.evkuaforum.R;
import com.abaskan.evkuaforum.UserFragment.FragmentHours;
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
import java.util.HashMap;
import java.util.Map;

public class UserBookAppointmentActivity extends AppCompatActivity {
    ToggleButton toggleButton;
    Toolbar bookAppointmentToolbar;
    CalendarView bookAppointmentCalendarView;
    CardView bookAppointmentCardView;
    Button bookAppointmentButton;
    TextView bookAppointmentServiceName, bookAppointmentServicePrice, bookAppointmentDistance, bookAppointmentDistancePrice, bookAppointmentTotalPrice;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ArrayList<String> hourList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book_appointment);
        toolbar();
        findViewById();
        createFirebaseFields();
        getService();
        setHourAdapter();


    }

    public void toolbar() {
        bookAppointmentToolbar = findViewById(R.id.bookAppointmentToolbar);
        bookAppointmentToolbar.setTitle("Randevu Al");
        bookAppointmentToolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        setSupportActionBar(bookAppointmentToolbar);
        bookAppointmentToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void findViewById() {
        bookAppointmentCalendarView = findViewById(R.id.bookAppointmentCalendarView);
        bookAppointmentCardView = findViewById(R.id.bookAppointmentCardView);
        bookAppointmentServiceName = findViewById(R.id.bookAppointmentServiceName);
        bookAppointmentServicePrice = findViewById(R.id.bookAppointmentServicePrice);
        bookAppointmentDistance = findViewById(R.id.bookAppointmentDistance);
        bookAppointmentDistancePrice = findViewById(R.id.bookAppointmentDistancePrice);
        bookAppointmentTotalPrice = findViewById(R.id.bookAppointmentTotalPrice);
        toggleButton = findViewById(R.id.toggleButton);
        bookAppointmentButton = findViewById(R.id.bookAppointmentButton);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        hourList = new ArrayList<>();
    }

    public void getService() {
        String serviceName = getIntent().getStringExtra("serviceName");
        final String servicePrice = getIntent().getStringExtra("servicePrice");
        double distance = getIntent().getDoubleExtra("distance", 1);
        double extraPrice = getIntent().getDoubleExtra("extraPrice", 2);
        final int totalPrice = Integer.parseInt(servicePrice) + (int) extraPrice;
        bookAppointmentServiceName.setText(serviceName);
        bookAppointmentServicePrice.setText(servicePrice + " TL");
        bookAppointmentDistance.setText("Mesafe : " + distance + " Km");
        bookAppointmentDistancePrice.setText((int)extraPrice + " TL");
        bookAppointmentTotalPrice.setText(totalPrice + " TL");

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    bookAppointmentDistancePrice.setVisibility(View.INVISIBLE);
                    bookAppointmentDistance.setVisibility(View.INVISIBLE);
                    bookAppointmentTotalPrice.setText(servicePrice + " TL");
                } else {
                    bookAppointmentDistancePrice.setVisibility(View.VISIBLE);
                    bookAppointmentDistance.setVisibility(View.VISIBLE);
                    bookAppointmentTotalPrice.setText(totalPrice + " TL");
                }
            }
        });
    }

    public void bookAppointmentButtonClick(View view) {


        @SuppressLint("SimpleDateFormat") final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        java.util.Date today = new java.util.Date();
        final String date = dateFormat.format(today);
        final long dateLong = Long.parseLong(date);

        final String barberId = getIntent().getStringExtra("barberId");
        final String barberName = getIntent().getStringExtra("barberName");
        DocumentReference documentReference = firebaseFirestore.collection("UserFields").document(firebaseAuth.getUid())
                .collection("SelectedHours").document("Time");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onSuccess(final DocumentSnapshot documentSnapshot) {

                Map<String, Object> date = documentSnapshot.getData();
                final String hour = (String) date.get("hour");
                final String time = (String) date.get("time");
                if (!hour.equals("null") && !time.equals("null")) {
                    String hourFormat = (String) date.get("formatHour");
                    String timeFormat = (String) date.get("formatTime");

                    String longTime = timeFormat + hourFormat;

                    final long appointmentLong = Long.parseLong(longTime);

                    CollectionReference collectionReference = firebaseFirestore.collection("UserFields")
                            .document(firebaseAuth.getUid()).collection("Appointments");
                    collectionReference.whereEqualTo("appointmentLong", appointmentLong).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.getDocuments().isEmpty()) {
                                for (DocumentSnapshot documentSnapshot1 : queryDocumentSnapshots.getDocuments()) {
                                    if (documentSnapshot1.getData() != null) {
                                        Toast.makeText(UserBookAppointmentActivity.this, "Aynı saatte alınmış randevunuz bulunuyor. Lütfen farklı bir saat veya tarih seçiniz.", Toast.LENGTH_LONG).show();
                                        break;
                                    }
                                }
                            }else {

                                if (dateLong < appointmentLong) {
                                    String servicePriceString = getIntent().getStringExtra("servicePrice");
                                    double servicePrice = Double.parseDouble(servicePriceString);
                                    double distance = getIntent().getDoubleExtra("distance", 1);
                                    double extraPrice = getIntent().getDoubleExtra("extraPrice", 2);
                                    double totalPrice;
                                    String place ;

                                    if(toggleButton.isChecked()){
                                        totalPrice = servicePrice;
                                        distance = 0;
                                        place = toggleButton.getTextOn().toString();
                                    }else{
                                        totalPrice = servicePrice + extraPrice;
                                        place = toggleButton.getTextOff().toString();
                                    }


                                    final Map<String, Object> newAppointment = new HashMap<>();
                                    newAppointment.put("appointmentLong", appointmentLong);
                                    newAppointment.put("userId", firebaseAuth.getUid());
                                    newAppointment.put("barberId", barberId);
                                    newAppointment.put("date", time);
                                    newAppointment.put("hour", hour);
                                    newAppointment.put("serviceName", bookAppointmentServiceName.getText());
                                    newAppointment.put("servicePrice", servicePrice);
                                    newAppointment.put("totalPrice", totalPrice);
                                    newAppointment.put("distance", distance);
                                    newAppointment.put("barberName",barberName);
                                    newAppointment.put("place",place);

                                    DocumentReference userReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
                                    userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                            Map<String, Object> userData = documentSnapshot.getData();
                                            String userName = (String) userData.get("userName");
                                            String userAdress = userData.get("userAdress") + " " + userData.get("userDistrict") + ", " + userData.get("userProvince");

                                            newAppointment.put("userName", userName);
                                            newAppointment.put("userAdress", userAdress);

                                            firebaseFirestore.collection("UserFields").document(firebaseAuth.getUid())
                                                    .collection("Appointments").document(String.valueOf(appointmentLong)).set(newAppointment);
                                            firebaseFirestore.collection("BarberFields").document(barberId)
                                                    .collection("Appointments").document(String.valueOf(appointmentLong)).set(newAppointment);
                                            firebaseFirestore.collection("BarberFields").document(barberId)
                                                    .collection("Customers").document(String.valueOf(appointmentLong)).set(newAppointment);
                                        }
                                    });

                                    Toast.makeText(UserBookAppointmentActivity.this, "Randevu Başarıyla Alındı", Toast.LENGTH_LONG).show();
                                } else {
                                   Toast.makeText(UserBookAppointmentActivity.this, "Tarih geçildi", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(UserBookAppointmentActivity.this, "Tarih ve saat seçiniz", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void setHourAdapter() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.hourFrameLayout, new FragmentHours());
        fragmentTransaction.commit();
    }


    public void createFirebaseFields() {
        Map<String, Object> hour = new HashMap<>();
        hour.put("hour", "null");
        hour.put("time", "null");

        firebaseFirestore.collection("UserFields").document(firebaseAuth.getUid())
                .collection("SelectedHours").document("Time").set(hour);
    }
}
