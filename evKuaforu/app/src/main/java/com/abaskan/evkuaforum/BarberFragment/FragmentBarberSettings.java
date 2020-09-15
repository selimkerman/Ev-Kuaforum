package com.abaskan.evkuaforum.BarberFragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abaskan.evkuaforum.BarberAdapter.BarberSettingsAdapter;
import com.abaskan.evkuaforum.BarberClass.BarberSettingsClass;
import com.abaskan.evkuaforum.R;

import java.util.ArrayList;

public class FragmentBarberSettings extends Fragment {
    Toolbar barberSettingsToolbar;
    RecyclerView barberSettingsRv;
    private ArrayList<BarberSettingsClass> barberSettingsClassArrayList;
    private BarberSettingsAdapter barberSettingsAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_barber_settings, container, false);

        toolbar(view);
        recyclerView(view);


        return view;
    }

    public void toolbar(View view){
        barberSettingsToolbar = view.findViewById(R.id.barberSettingsToolbar);
        barberSettingsToolbar.setTitle("     Ayarlarım");
        barberSettingsToolbar.setTitleTextColor(Color.WHITE);
    }

    public void recyclerView (View view){
        barberSettingsRv = view.findViewById(R.id.barberSettingsRv);
        barberSettingsRv.setHasFixedSize(true);
        barberSettingsRv.setLayoutManager(new LinearLayoutManager(getActivity()));

        BarberSettingsClass s1 = new BarberSettingsClass("Kişisel Bilgilerim", "icon_person_black");
        BarberSettingsClass s2 = new BarberSettingsClass("İşyeri Bilgilerim", "icon_store_black");
        BarberSettingsClass s3 = new BarberSettingsClass("Randevu Geçmişim", "icon_history_black");
        BarberSettingsClass s4 = new BarberSettingsClass("Değerlendirmelerim", "icon_comment_black");
        BarberSettingsClass s5 = new BarberSettingsClass("Bildirim Merkezi", "icon_notification_black");
        BarberSettingsClass s6 = new BarberSettingsClass("Çıkış Yap", "icon_logout_black");

        barberSettingsClassArrayList = new ArrayList<>();
        barberSettingsClassArrayList.add(s1);
        barberSettingsClassArrayList.add(s2);
        barberSettingsClassArrayList.add(s3);
        barberSettingsClassArrayList.add(s4);
        barberSettingsClassArrayList.add(s5);
        barberSettingsClassArrayList.add(s6);

        barberSettingsAdapter = new BarberSettingsAdapter(getActivity(), barberSettingsClassArrayList);
        barberSettingsRv.setAdapter(barberSettingsAdapter);
    }
}
