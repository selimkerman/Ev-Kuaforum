package com.abaskan.evkuaforum.BarberActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.abaskan.evkuaforum.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BarberAddServiceActivity extends AppCompatActivity {
    Toolbar barberAddServiceToolbar;
    EditText barberAddServiceName;
    EditText barberAddServicePrice;
    EditText barberAddServiceTime;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_add_service);
        toolbar();
        findViewById();

    }


    public void toolbar(){
        barberAddServiceToolbar = findViewById(R.id.barberAddServiceToolbar);
        barberAddServiceToolbar.setTitle("Servis Ekle");
        barberAddServiceToolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        setSupportActionBar(barberAddServiceToolbar);
        barberAddServiceToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void findViewById(){
        barberAddServiceName = findViewById(R.id.barberAddServiceName);
        barberAddServicePrice = findViewById(R.id.barberAddServicePrice);
        barberAddServiceTime = findViewById(R.id.barberAddServiceTime);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    public void barberAddServiceButtonClick (View view){
        String name = barberAddServiceName.getText().toString().trim();
        String price = barberAddServicePrice.getText().toString().trim();
        String time = barberAddServiceTime.getText().toString().trim();


        Map<String,Object> barberService = new HashMap<>();
        barberService.put("serviceName",name);
        barberService.put("servicePrice",price);
        barberService.put("serviceTime",time);
        barberService.put("time",System.currentTimeMillis());

        firebaseFirestore.collection("BarberFields").document(firebaseAuth.getUid())
                .collection("Services").add(barberService).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(BarberAddServiceActivity.this,"Kaydedildi",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BarberAddServiceActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
}
