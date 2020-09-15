package com.abaskan.evkuaforum.BarberActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.abaskan.evkuaforum.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class BarberMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView mapDistanceText,mapNameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        double distance = getIntent().getDoubleExtra("distance",10);
        String userName = getIntent().getStringExtra("userName");


        mapDistanceText = findViewById(R.id.mapDistanceText);
        mapNameText =findViewById(R.id.mapNameText);

        mapDistanceText.setText(distance + " Km");
        mapNameText.setText(userName);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        double userLat = getIntent().getDoubleExtra("userLat",0);
        double userLng = getIntent().getDoubleExtra("userLng",0);
        String userName = getIntent().getStringExtra("userName");

        double barberLat = getIntent().getDoubleExtra("barberLat",0);
        double barberLng = getIntent().getDoubleExtra("barberLng",0);
        String storeName = getIntent().getStringExtra("storeName");
        //String adress = getIntent().getStringExtra("userAdress");

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    ,1);
        }

            LatLng user = new LatLng(userLat, userLng);
            mMap.addMarker(new MarkerOptions().position(user).title(userName));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user,12.16f));
/*
            LatLng barber = new LatLng(barberLat, barberLng);
            mMap.addMarker(new MarkerOptions().position(barber).title(storeName));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(barber,12.16f));


 */

    }

}
