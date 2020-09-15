package com.abaskan.evkuaforum.UserActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.abaskan.evkuaforum.R;
import com.abaskan.evkuaforum.UserFragment.FragmentUserAppointments;
import com.abaskan.evkuaforum.UserFragment.FragmentUserChats;
import com.abaskan.evkuaforum.UserFragment.FragmentUserMainPage;
import com.abaskan.evkuaforum.UserFragment.FragmentUserSettings;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserMainActivity extends AppCompatActivity {
    private BottomNavigationView userBottomNavigationView;
    private Fragment tempFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        userBottomNavigatonMenu();
    }

    public void userBottomNavigatonMenu() {
        userBottomNavigationView = findViewById(R.id.userBottomNavigationView);
        getSupportFragmentManager().beginTransaction().add(R.id.userFragmentHolder,new FragmentUserMainPage()).commit();

        userBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.actionUserMainPage:
                        tempFragment = new FragmentUserMainPage();
                        break;
                    case R.id.actionUserAppointments:
                        tempFragment = new FragmentUserAppointments();
                        break;

                    case R.id.actionUserChats:
                        tempFragment = new FragmentUserChats();
                        break;

                    case R.id.actionUserSettings:
                        tempFragment = new FragmentUserSettings();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.userFragmentHolder,tempFragment).commit();

                return true;
            }
        });
    }
}
