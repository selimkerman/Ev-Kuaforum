package com.abaskan.evkuaforum.UserActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.abaskan.evkuaforum.UserFragment.FragmentBarberDetailDetails;
import com.abaskan.evkuaforum.UserFragment.FragmentBarberDetailReviews;
import com.abaskan.evkuaforum.UserFragment.FragmentBarberDetailServices;
import com.abaskan.evkuaforum.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class BarberDetailActivity extends AppCompatActivity {
    private TabLayout barberDetailTabs;
    private ViewPager barberDetailViewPager;
    private Toolbar barberDetailToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_detail);
        toolbar();
        viewPagerAdapter();
    }


    public void toolbar(){
        barberDetailToolbar = findViewById(R.id.barberDetailToolbar);
        barberDetailToolbar.setTitle("Kuaför Detayları");
        barberDetailToolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        barberDetailToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(barberDetailToolbar);
        barberDetailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void viewPagerAdapter(){
        barberDetailTabs = findViewById(R.id.barberDetailTabs);
        barberDetailViewPager = findViewById(R.id.barberDetailViewPager);
        BarberDetailViewPagerAdapter adapter = new BarberDetailViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentBarberDetailServices(),"Servisler");
        adapter.addFragment(new FragmentBarberDetailDetails(),"Detaylar");
        adapter.addFragment(new FragmentBarberDetailReviews(),"Yorumlar");

        barberDetailViewPager.setAdapter(adapter);
        barberDetailTabs.setupWithViewPager(barberDetailViewPager);
    }

    class BarberDetailViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public BarberDetailViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }

        public void addFragment (Fragment fragment, String title){
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }
    }
}
