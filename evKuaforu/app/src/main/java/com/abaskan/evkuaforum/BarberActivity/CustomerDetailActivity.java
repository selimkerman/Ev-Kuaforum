package com.abaskan.evkuaforum.BarberActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.abaskan.evkuaforum.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerDetailActivity extends AppCompatActivity {
    Toolbar barberDetailUserToolbar;
    TextView barberDetailUserIcon, barberDetailUserName, barberDetailUserAppDate, barberDetailUserServiceName,
             barberDetailUserAdress, barberDetailUserPrice;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_customer_detail);

        findViewById();
        getUserAdressFromFirebase();
        toolbar();
        getAppointmentInfo();
    }

    public void toolbar (){
        barberDetailUserToolbar = findViewById(R.id.barberDetailUserToolbar);
        barberDetailUserToolbar.setTitle("Müşteri Detayları");
        barberDetailUserToolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        barberDetailUserToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(barberDetailUserToolbar);
        barberDetailUserToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void findViewById(){
        barberDetailUserIcon = findViewById(R.id.barberDetailUserIcon);
        barberDetailUserName = findViewById(R.id.barberDetailUserName);
        barberDetailUserAppDate = findViewById(R.id.barberDetailUserAppDate);
        barberDetailUserServiceName = findViewById(R.id.barberDetailUserServiceName);
        barberDetailUserAdress = findViewById(R.id.barberDetailUserAdress);
        barberDetailUserPrice = findViewById(R.id.barberDetailUserPrice);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void getAppointmentInfo (){
        int color = getIntent().getIntExtra("iconColor",-7041594);
        String userName = getIntent().getStringExtra("userName");
        String appDate = getIntent().getStringExtra("appDate");
        String serviceName = getIntent().getStringExtra("serviceName");
        int totalPrice = getIntent().getIntExtra("totalPrice",1);

        barberDetailUserIcon.setText(userName.substring(0,1));
        ((GradientDrawable) barberDetailUserIcon.getBackground()).setTint(color);

        barberDetailUserName.setText(userName);
        barberDetailUserAppDate.setText(appDate);
        barberDetailUserServiceName.setText(serviceName);
        barberDetailUserPrice.setText(totalPrice + " TL");
    }

    public void getUserAdressFromFirebase(){
        String userId = getIntent().getStringExtra("userId");
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(userId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String,Object> userData = documentSnapshot.getData();
                String userAdress = (String) userData.get("userAdress");
                String userProvince = (String) userData.get("userProvince");
                String userDistrict = (String) userData.get("userDistrict");
                String adress = userAdress + " " + userDistrict + ", " + userProvince;

                barberDetailUserAdress.setText(adress);
            }
        });
    }

    public void barberDetailUserToChatClick (View view){
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date today = Calendar.getInstance().getTime();
        String dateString = dateFormat.format(today);
        long dateLong = Long.parseLong(dateString);
        String userName = getIntent().getStringExtra("userName");
        final String userId = getIntent().getStringExtra("userId");
        int newMessage = 0;
        final Map<String, Object> newChat = new HashMap<>();
        newChat.put("barberId",firebaseAuth.getUid());
        newChat.put("userId",userId);
        newChat.put("userName",userName);
        newChat.put("date",getDate());
        newChat.put("dateLong",dateLong);


        DocumentReference documentReference = firebaseFirestore.collection("Barbers").document(firebaseAuth.getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String,Object> userData = documentSnapshot.getData();
                String userName = (String) userData.get("barberName");
                newChat.put("barberName",userName);
                firebaseFirestore.collection("BarberFields").document(firebaseAuth.getUid()).collection("Chats")
                        .document(userId).set(newChat);
                firebaseFirestore.collection("UserFields").document(userId).collection("Chats")
                        .document(firebaseAuth.getUid()).set(newChat);
            }
        });

        Intent toChat = new Intent(CustomerDetailActivity.this, BarberChatActivity.class);
        toChat.putExtra("userName",userName);
        toChat.putExtra("userId",userId);
        toChat.putExtra("barberId",firebaseAuth.getUid());
        startActivity(toChat);
        finish();
    }

    public void barberDetailUserToMapClick(View view){
        final String userName = getIntent().getStringExtra("userName");
        final String userId = getIntent().getStringExtra("userId");
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(userId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                final String userAdress = documentSnapshot.getString("userAdress") + " " + documentSnapshot.getString("userDistrict")
                        + ", " + documentSnapshot.getString("userProvince");
                final double userLat = documentSnapshot.getDouble("userAdressLat");
                final double userLng = documentSnapshot.getDouble("userAdressLng");

                CollectionReference collectionReference = firebaseFirestore.collection("BarberFields").document(firebaseAuth.getUid())
                        .collection("Customers");

                collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    }
                });

                collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot1 : queryDocumentSnapshots.getDocuments()){

                            String userIdFirebase = documentSnapshot1.getString("userId");
                                if(userIdFirebase.matches(userId)){
                                    final double distance = documentSnapshot1.getDouble("distance");

                                    DocumentReference documentReference1 = firebaseFirestore.collection("Barbers")
                                    .document(firebaseAuth.getUid());
                                    documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            double barberLat = documentSnapshot.getDouble("barberAdressLat");
                                            double barberLng = documentSnapshot.getDouble("barberAdressLng");
                                            String storeName = documentSnapshot.getString("barberStoreName");

                                            Intent toMap = new Intent(CustomerDetailActivity.this, BarberMapsActivity.class);
                                            toMap.putExtra("userName",userName);
                                            toMap.putExtra("userId",userId);
                                            toMap.putExtra("userAdress",userAdress);
                                            toMap.putExtra("userLat",userLat);
                                            toMap.putExtra("userLng",userLng);
                                            toMap.putExtra("distance",distance);
                                            toMap.putExtra("barberLat",barberLat);
                                            toMap.putExtra("barberLng",barberLng);
                                            toMap.putExtra("storeName",storeName);
                                            startActivity(toMap);
                                            finish();

                                        }
                                    });

                                    break;
                            }
                        }

                    }
                });
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
