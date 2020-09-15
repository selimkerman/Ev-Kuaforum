package com.abaskan.evkuaforum.BarberActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.abaskan.evkuaforum.BarberClass.Barber;
import com.abaskan.evkuaforum.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BarberSignupActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private EditText signupBarberName, signupBarberShopName, signupBarberProvince, signupBarberPhone,
            signupBarberEmail, signupBarberPassword;
    RadioButton barberCheckedRadioButton;
    RadioGroup barbersignupRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_signup);
        findViewById();
    }

    public void barberSignupButtonClick(View view) {
        String email = signupBarberEmail.getText().toString();
        String password = signupBarberPassword.getText().toString();
        String name = signupBarberName.getText().toString().trim();
        String storeName = signupBarberShopName.getText().toString().trim();
        String province = signupBarberProvince.getText().toString().trim();
        String phone = signupBarberPhone.getText().toString().trim();

        if(email.isEmpty() || password.isEmpty() || name.isEmpty() || storeName.isEmpty() || province.isEmpty() || phone.isEmpty()){
            Toast.makeText(this, "Boş Bırakılan Bölümler Bulunuyor !", Toast.LENGTH_SHORT).show();
        }
        else{
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    barberInfoToFirebase();
                    Intent toBarberMain = new Intent(BarberSignupActivity.this, BarberMainActivity.class);
                    startActivity(toBarberMain);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BarberSignupActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }


    }


    public void findViewById() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        signupBarberName = findViewById(R.id.signupBarberName);
        signupBarberShopName = findViewById(R.id.signupBarberShopName);
        signupBarberProvince = findViewById(R.id.signupBarberProvince);
        signupBarberPhone = findViewById(R.id.signupBarberPhone);
        signupBarberEmail = findViewById(R.id.signupBarberEMail);
        signupBarberPassword = findViewById(R.id.signupBarberPassword);
        barbersignupRadioGroup = findViewById(R.id.barberSignupRadioGroup);
    }

    public void barberInfoToFirebase() {
        String name = signupBarberName.getText().toString().trim();
        String storeName = signupBarberShopName.getText().toString().trim();
        String province = signupBarberProvince.getText().toString().trim();
        String phone = signupBarberPhone.getText().toString().trim();
        String email = signupBarberEmail.getText().toString().trim();

        int selectedId = barbersignupRadioGroup.getCheckedRadioButtonId();
        barberCheckedRadioButton = findViewById(selectedId);
        String type = barberCheckedRadioButton.getText().toString();

        Barber newBarber = new Barber(name, storeName, province, phone,
                email, type, "null", "null",
                "null", System.currentTimeMillis(),
                0.000, 1,1.000,1.000,0);

        firebaseFirestore.collection("Barbers").document(firebaseAuth.getUid())
                .set(newBarber);
    }
}
