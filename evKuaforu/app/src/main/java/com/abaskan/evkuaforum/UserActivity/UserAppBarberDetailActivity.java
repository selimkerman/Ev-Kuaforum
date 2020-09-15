package com.abaskan.evkuaforum.UserActivity;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UserAppBarberDetailActivity extends AppCompatActivity {
    Toolbar userDetailBarberToolbar;
    TextView userDetailBarberIcon, userDetailBarberName, userDetailBarberAppDate, userDetailBarberServiceName, userDetailBarberAdress;
    TextView userDetailBarberServicePrice;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_app_barber_detail);

        findViewById();
        getBarberAdressFromFirebase();
        toolbar();
        getAppointmentInfo();
    }


    public void toolbar (){
        userDetailBarberToolbar = findViewById(R.id.userDetailBarberToolbar);
        userDetailBarberToolbar.setTitle("Kuaför Detayları");
        userDetailBarberToolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        userDetailBarberToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(userDetailBarberToolbar);
        userDetailBarberToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void findViewById(){
        userDetailBarberIcon = findViewById(R.id.userDetailBarberIcon);
        userDetailBarberName = findViewById(R.id.userDetailBarberName);
        userDetailBarberAppDate = findViewById(R.id.userDetailBarberAppDate);
        userDetailBarberServiceName = findViewById(R.id.userDetailBarberServiceName);
        userDetailBarberAdress = findViewById(R.id.userDetailBarberAdress);
        userDetailBarberServicePrice = findViewById(R.id.userDetailBarberServicePrice);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void getAppointmentInfo (){
        String barberName = getIntent().getStringExtra("barberName");
        String appDate = getIntent().getStringExtra("appDate");
        String serviceName = getIntent().getStringExtra("serviceName");
        int totalPrice = getIntent().getIntExtra("totalPrice",1);

        Random random = new Random();
        final int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

        userDetailBarberIcon.setText(barberName.substring(0,1));
        ((GradientDrawable) userDetailBarberIcon.getBackground()).setTint(color);

        userDetailBarberName.setText(barberName);
        userDetailBarberAppDate.setText(appDate);
        userDetailBarberServiceName.setText(serviceName);
        userDetailBarberServicePrice.setText(totalPrice + " TL");
    }

    public void getBarberAdressFromFirebase(){
        String barberId = getIntent().getStringExtra("barberId");
        DocumentReference documentReference = firebaseFirestore.collection("Barbers").document(barberId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String,Object> userData = documentSnapshot.getData();
                String barberAdress = (String) userData.get("barberAdress");
                String barberProvince = (String) userData.get("barberProvince");
                String barberDistrict = (String) userData.get("barberDistrict");
                String adress = barberAdress + " " + barberDistrict + ", " + barberProvince;

                userDetailBarberAdress.setText(adress);
            }
        });
    }

    public void userDetailToBarberPageClick(View view){
        String barberId = getIntent().getStringExtra("barberId");
        Intent toBarberPage = new Intent(UserAppBarberDetailActivity.this, BarberDetailActivity.class);
        toBarberPage.putExtra("barberId",barberId);
        startActivity(toBarberPage);
        //finish();
    }

    public void userDetailBarberToChatClick(View view){

        final String barberId = getIntent().getStringExtra("barberId");
        String barberName = getIntent().getStringExtra("barberName");
        final Map<String, Object> newChat = new HashMap<>();
        int newMessage = 0;
        newChat.put("userId",firebaseAuth.getUid());
        newChat.put("barberId",barberId);
        newChat.put("barberName",barberName);
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



        Intent toChat = new Intent(UserAppBarberDetailActivity.this, UserChatActivity.class);
        toChat.putExtra("barberName",barberName);
        toChat.putExtra("barberId",barberId);
        toChat.putExtra("userId",firebaseAuth.getUid());
        startActivity(toChat);
        //finish();
    }

    public String getDate (){
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String date = dateFormat.format(today);
        return date;
    }
}
