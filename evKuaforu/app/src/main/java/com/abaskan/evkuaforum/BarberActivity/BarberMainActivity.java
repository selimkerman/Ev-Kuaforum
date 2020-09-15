package com.abaskan.evkuaforum.BarberActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.abaskan.evkuaforum.BarberFragment.FragmentBarberAppointments;
import com.abaskan.evkuaforum.BarberFragment.FragmentBarberChats;
import com.abaskan.evkuaforum.BarberFragment.FragmentBarberCustomers;
import com.abaskan.evkuaforum.BarberFragment.FragmentBarberSettings;
import com.abaskan.evkuaforum.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BarberMainActivity extends AppCompatActivity {
    private BottomNavigationView barberBottomNavigationView;
    private Fragment tempFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_main);
        BarberBottomNavigationMenu();
    }

    public void BarberBottomNavigationMenu() {
        barberBottomNavigationView = findViewById(R.id.barberBottomNavigationView);
        getSupportFragmentManager().beginTransaction().add(R.id.barberFragmentHolder, new FragmentBarberAppointments()).commit();

        barberBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.actionBarberAppointments:
                        tempFragment = new FragmentBarberAppointments();
                        break;
                    case R.id.actionBarberCustomers:
                        tempFragment = new FragmentBarberCustomers();
                        break;

                    case R.id.actionBarberChats:
                        tempFragment = new FragmentBarberChats();
                        break;


                    case R.id.actionBarberSettings:
                        tempFragment = new FragmentBarberSettings();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.barberFragmentHolder, tempFragment).commit();
                return true;
            }
        });
    }
}
