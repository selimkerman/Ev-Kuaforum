package com.abaskan.evkuaforum.BarberActivity;

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

import com.abaskan.evkuaforum.BarberClass.Barber;
import com.abaskan.evkuaforum.R;
import com.abaskan.evkuaforum.BarberClass.UserAdressDataHandler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BarberPersonalInfoActivity extends AppCompatActivity {
    Toolbar barberPersonalInfoToolbar;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    EditText barberPersonalInfoName, barberPersonalInfoStoreName, barberPersonalInfoPhone, barberPersonalInfoDistrict, barberPersonalInfoProvince, barberPersonalInfoAdress;
    RadioButton barberPersonalInfoMaleRadioButton, barberPersonalInfoFemaleRadioButton, barberPersonalInfoAllRadioButton, barberPersonalCheckedRadioButton;
    RadioGroup barberPersonalInfoRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_personal_info);
        findViewById();
        toolbar();
        getCurrentUserInfo();

    }
    public void findViewById() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        barberPersonalInfoName = findViewById(R.id.barberPersonalInfoName);
        barberPersonalInfoStoreName = findViewById(R.id.barberPersonalInfoStoreName);
        barberPersonalInfoPhone = findViewById(R.id.barberPersonalInfoPhone);
        barberPersonalInfoProvince = findViewById(R.id.barberPersonalInfoProvince);
        barberPersonalInfoDistrict = findViewById(R.id.barberPersonalInfoDistrict);
        barberPersonalInfoAdress = findViewById(R.id.barberPersonalInfoAdress);
        barberPersonalInfoMaleRadioButton = findViewById(R.id.barberPersonalInfoMaleRadioButton);
        barberPersonalInfoFemaleRadioButton = findViewById(R.id.barberPersonalInfoFemaleRadioButton);
        barberPersonalInfoAllRadioButton = findViewById(R.id.barberPersonalInfoAllRadioButton);
        barberPersonalInfoRadioGroup = findViewById(R.id.barberPersonalInfoRadioGroup);

    }

    public void toolbar() {
        barberPersonalInfoToolbar = findViewById(R.id.barberPersonalInfoToolbar);
        barberPersonalInfoToolbar.setTitle("Bilgilerim");
        barberPersonalInfoToolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        setSupportActionBar(barberPersonalInfoToolbar);
        barberPersonalInfoToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getCurrentUserInfo() {

        final DocumentReference documentReference = firebaseFirestore.collection("Barbers").document(firebaseAuth.getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Barber currentBarber = documentSnapshot.toObject(Barber.class);
                barberPersonalInfoName.setText(currentBarber.getBarberName());
                barberPersonalInfoStoreName.setText(currentBarber.getBarberStoreName());
                barberPersonalInfoPhone.setText(currentBarber.getBarberPhone());
                barberPersonalInfoProvince.setText(currentBarber.getBarberProvince());
                barberPersonalInfoDistrict.setText(currentBarber.getBarberDistrict());
                barberPersonalInfoAdress.setText(currentBarber.getBarberAdress());

                if (currentBarber.getBarberType().matches("Erkek")) {
                    barberPersonalInfoMaleRadioButton.setChecked(true);
                } else if (currentBarber.getBarberType().matches("Kadın")) {
                    barberPersonalInfoFemaleRadioButton.setChecked(true);
                } else {
                    barberPersonalInfoAllRadioButton.setChecked(true);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BarberPersonalInfoActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void barberPersonalInfoButtonClick(View view) {
        String name = barberPersonalInfoName.getText().toString();
        String store = barberPersonalInfoStoreName.getText().toString();
        String phone = barberPersonalInfoPhone.getText().toString();
        String province = barberPersonalInfoProvince.getText().toString();
        String district = barberPersonalInfoDistrict.getText().toString();
        String adress = barberPersonalInfoAdress.getText().toString();

        int checkedType = barberPersonalInfoRadioGroup.getCheckedRadioButtonId();
        barberPersonalCheckedRadioButton = findViewById(checkedType);
        String barberType = barberPersonalCheckedRadioButton.getText().toString();

        Map<String, Object> currentBarber = new HashMap<>();
        currentBarber.put("barberName", name);
        currentBarber.put("barberStoreName", store);
        currentBarber.put("barberPhone", phone);
        currentBarber.put("barberProvince", province);
        currentBarber.put("barberDistrict", district);
        currentBarber.put("barberAdress", adress);
        currentBarber.put("barberType", barberType);

        String adressLong = adress + " " + district + ", " + province;
        new getCoordinates().execute(adressLong.replace(" ","+"));

        firebaseFirestore.collection("Barbers").document(firebaseAuth.getUid())
                .update(currentBarber).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(BarberPersonalInfoActivity.this, "Bilgileriniz Kaydedildi", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BarberPersonalInfoActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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
                currentUser.put("barberAdressLat", userLat);
                currentUser.put("barberAdressLng", userLng);

                firebaseFirestore.collection("Barbers").document(firebaseAuth.getUid())
                        .update(currentUser);

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

}
