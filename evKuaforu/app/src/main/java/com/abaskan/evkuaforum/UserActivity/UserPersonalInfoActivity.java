package com.abaskan.evkuaforum.UserActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.abaskan.evkuaforum.R;
import com.abaskan.evkuaforum.BarberClass.UserAdressDataHandler;
import com.abaskan.evkuaforum.UserClass.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserPersonalInfoActivity extends AppCompatActivity {
    Toolbar userPersonalInfoToolbar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    EditText userPersonalInfoName, userPersonalInfoPhone, userPersonalInfoDistrict,
            userPersonalInfoProvince, userPersonalInfoAdress;
    RadioButton userPersonalInfoMaleRadioButton, userPersonalInfoFemaleRadioButton,userPersonalInfoCheckedRadioButton;
    RadioGroup userPersonalInfoRadioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_personal_info);
        findViewById();
        getCurrentuserPersonalInfo();
        toolbar();

    }

    public void findViewById() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userPersonalInfoName = findViewById(R.id.userPersonalInfoName);
        userPersonalInfoPhone = findViewById(R.id.userPersonalInfoPhone);
        userPersonalInfoProvince = findViewById(R.id.userPersonalInfoProvince);
        userPersonalInfoDistrict = findViewById(R.id.userPersonalInfoDistrict);
        userPersonalInfoAdress = findViewById(R.id.userPersonalInfoAdress);
        userPersonalInfoMaleRadioButton = findViewById(R.id.userPersonalInfoMaleRadioButton);
        userPersonalInfoFemaleRadioButton = findViewById(R.id.userPersonalInfoFemaleRadioButton);
        userPersonalInfoRadioGroup = findViewById(R.id.userPersonalInfoRadioGroup);
    }

    public void toolbar() {
        userPersonalInfoToolbar = findViewById(R.id.userPersonalInfoToolbar);
        userPersonalInfoToolbar.setTitle("Bilgilerim");
        userPersonalInfoToolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        setSupportActionBar(userPersonalInfoToolbar);
        userPersonalInfoToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getCurrentuserPersonalInfo(){

        final DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User currentUser = documentSnapshot.toObject(User.class);
                userPersonalInfoName.setText(currentUser.getUserName());
                userPersonalInfoPhone.setText(currentUser.getUserPhone());
                userPersonalInfoProvince.setText(currentUser.getUserProvince());
                userPersonalInfoDistrict.setText(currentUser.getUserDistrict());
                userPersonalInfoAdress.setText(currentUser.getUserAdress());

                if(currentUser.getUserGender().matches("Erkek")){
                    userPersonalInfoMaleRadioButton.setChecked(true);
                }else{
                    userPersonalInfoFemaleRadioButton.setChecked(true);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserPersonalInfoActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


    public void userPersonalInfoButtonClick(View view){
        String name = userPersonalInfoName.getText().toString();
        String phone = userPersonalInfoPhone.getText().toString();
        String province = userPersonalInfoProvince.getText().toString();
        String district = userPersonalInfoDistrict.getText().toString();
        String adress = userPersonalInfoAdress.getText().toString();


        int checkedUser = userPersonalInfoRadioGroup.getCheckedRadioButtonId();
        userPersonalInfoCheckedRadioButton = findViewById(checkedUser);
        String userGender = userPersonalInfoCheckedRadioButton.getText().toString();

        Map<String, Object> currentUser = new HashMap<>();
        currentUser.put("userName", name);
        currentUser.put("userPhone", phone);
        currentUser.put("userProvince", province);
        currentUser.put("userDistrict", district);
        currentUser.put("userAdress", adress);
        currentUser.put("userGender", userGender);

        String adressLong = adress + " " + district + ", " + province;

        new getCoordinates().execute(adressLong.replace(" ", "+"));

        firebaseFirestore.collection("Users").document(firebaseAuth.getUid())
                .update(currentUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UserPersonalInfoActivity.this,"Bilgileriniz Kaydedildi",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserPersonalInfoActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public static class getCoordinates extends AsyncTask<String,Void,String> {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            try{
                String adress = strings[0].trim();
                UserAdressDataHandler userAdressDataHandler = new UserAdressDataHandler();
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=YOUR_KEY",adress);
                response = userAdressDataHandler.getAdressData(url);

            } catch (Exception e){

            }
            return response;
        }
        protected void onPostExecute (String s){
            try{
                JSONObject jsonObject = new JSONObject(s);

                String lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lat").toString();

                String lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lng").toString();

                double userLat = Double.parseDouble(lat);
                double userLng = Double.parseDouble(lng);

                Map<String, Object> currentUser = new HashMap<>();
                currentUser.put("userAdressLat", userLat);
                currentUser.put("userAdressLng", userLng);

                firebaseFirestore.collection("Users").document(firebaseAuth.getUid())
                        .update(currentUser);

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

}
