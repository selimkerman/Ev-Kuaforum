package com.abaskan.evkuaforum.BarberActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.abaskan.evkuaforum.R;
import com.abaskan.evkuaforum.UserActivity.UserLoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class BarberLoginActivity extends AppCompatActivity {
    private EditText loginBarberEmail,loginBarberPassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_login);

        findViewById();
/*
        if (firebaseUser != null) {
            Intent loginToMain = new Intent(BarberLoginActivity.this, BarberMainActivity.class);
            startActivity(loginToMain);
            finish();
        }
*/
    }

    public void barberLoginButtonClick (View view){
        String email = loginBarberEmail.getText().toString();
        String password = loginBarberPassword.getText().toString();

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Boş Bırakılan Bölümler Bulunuyor !", Toast.LENGTH_SHORT).show();
        }
        else {
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent loginToMain = new Intent(BarberLoginActivity.this,BarberMainActivity.class);
                    startActivity(loginToMain);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BarberLoginActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void findViewById (){
        loginBarberEmail = findViewById(R.id.loginBarberEmail);
        loginBarberPassword = findViewById(R.id.loginBarberPassword);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void barberSignupTextClick (View view) {
        Intent loginToSignup = new Intent(BarberLoginActivity.this,BarberSignupActivity.class);
        startActivity(loginToSignup);
    }
 
    public void loginBarberForgetPasswordClick (View view) {

    }

    public void toCustomerTextClick (View view) {

        Intent loginToUser = new Intent(BarberLoginActivity.this, UserLoginActivity.class);
        startActivity(loginToUser);


    }
}
