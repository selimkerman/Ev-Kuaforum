package com.abaskan.evkuaforum.UserActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.abaskan.evkuaforum.R;
import com.abaskan.evkuaforum.UserClass.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserSingupActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private EditText userSignupUserName, userSignupUserSurname,userSignupUserPhone,
            userSignupUserEmail, userSignupUserPassword,userSignupUserRepeatPassword;

    RadioButton maleRadioButton, femaleRadioButton,userCheckedRadioButton;
    RadioGroup userGenderGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_singup);
        findViewById();
    }


    public void userSignupButtonClick (View view){

        String email = userSignupUserEmail.getText().toString();
        String password = userSignupUserPassword.getText().toString();
        String name = userSignupUserName.getText().toString().trim() + " " + userSignupUserSurname.getText().toString().trim();
        String phone = userSignupUserPhone.getText().toString().trim();

        if(email.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Boş Bırakılan Bölümler Bulunuyor !", Toast.LENGTH_SHORT).show();
            if (!password.equals(userSignupUserRepeatPassword.getText().toString())) {
                Toast.makeText(this, "Şifreler Aynı Değil", Toast.LENGTH_SHORT).show();
            }
        }else  {
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    userInfoToFirebase();
                    Intent toUserMain = new Intent(UserSingupActivity.this, UserMainActivity.class);
                    startActivity(toUserMain);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserSingupActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }




    }


    public void findViewById (){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userSignupUserName = findViewById(R.id.userSignupUserName);
        userSignupUserSurname = findViewById(R.id.userSignupUserSurname);
        userSignupUserPhone = findViewById(R.id.userSignupUserPhone);
        userSignupUserEmail = findViewById(R.id.userSignupUserEmail);
        userSignupUserPassword = findViewById(R.id.userSignupUserPassword);
        userSignupUserRepeatPassword = findViewById(R.id.userSignupRepeatPassword);
    }

    public void userInfoToFirebase () {

        String name = userSignupUserName.getText().toString().trim() + " " + userSignupUserSurname.getText().toString().trim();
        String phone = userSignupUserPhone.getText().toString().trim();
        String email = userSignupUserEmail.getText().toString().trim();

        maleRadioButton = findViewById(R.id.maleRadioButton);
        femaleRadioButton = findViewById(R.id.femaleRadioButton);

        userGenderGroup = findViewById(R.id.userGenderGroup);

        int checkedUser = userGenderGroup.getCheckedRadioButtonId();
        userCheckedRadioButton = findViewById(checkedUser);
        String userGender = userCheckedRadioButton.getText().toString();

        User newUser = new User(name,phone,email,"null","null","null",userGender,1.000,1.000);

        firebaseFirestore.collection("Users").document(firebaseAuth.getUid())
                .set(newUser);
    }

}
