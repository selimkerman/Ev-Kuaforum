package com.abaskan.evkuaforum.UserActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.abaskan.evkuaforum.BarberActivity.BarberLoginActivity;
import com.abaskan.evkuaforum.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserLoginActivity extends AppCompatActivity {
    private EditText userLoginUserEmail,userLoginUserPassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        findViewById();
/*
        if (firebaseUser != null) {
            Intent loginToMain = new Intent(UserLoginActivity.this, UserMainActivity.class);
            startActivity(loginToMain);
            finish();
        }
*/
    }

    public void userLoginButtonClick (View view){
        String email = userLoginUserEmail.getText().toString();
        String password = userLoginUserPassword.getText().toString();

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Boş Bırakılan Bölümler Bulunuyor !", Toast.LENGTH_SHORT).show();
        }
        else {
            firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Intent loginToMain = new Intent(UserLoginActivity.this, UserMainActivity.class);
                            startActivity(loginToMain);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserLoginActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            });

        }


    }

    public void findViewById(){
        userLoginUserEmail = findViewById(R.id.userLoginUserEmail);
        userLoginUserPassword = findViewById(R.id.userLoginUserPassword);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public void userSignupTextClick (View view) {
        Intent loginToSignup = new Intent(UserLoginActivity.this, UserSingupActivity.class);
        //finish();
        startActivity(loginToSignup);
    }

    public void userLoginForgetPasswordClick (View view) {

    }

    public void toBarberTextClick (View view) {
        Intent userLogintoLogin = new Intent(UserLoginActivity.this, BarberLoginActivity.class);
        //finish();
        startActivity(userLogintoLogin);
    }
}
