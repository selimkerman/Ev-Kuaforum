package com.abaskan.evkuaforum.UserFragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abaskan.evkuaforum.BarberClass.BarberSettingsClass;
import com.abaskan.evkuaforum.R;
import com.abaskan.evkuaforum.UserAdapter.UserSettingsAdapter;
import com.abaskan.evkuaforum.UserClass.UserSettingsClass;

import java.util.ArrayList;
import java.util.List;

public class FragmentUserSettings extends Fragment {
    Toolbar userSettingsToolbar;
    RecyclerView userSettingsRv;
    private ArrayList<UserSettingsClass> userSettingsClassArrayList;
    private UserSettingsAdapter userSettingsAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_user_settings, container, false);

        toolbar(view);
        recyclerView(view);

        return view;
    }

    public void toolbar(View view){
        userSettingsToolbar = view.findViewById(R.id.userSettingsToolbar);
        userSettingsToolbar.setTitle("     Ayarlarım");
        userSettingsToolbar.setTitleTextColor(Color.WHITE);
    }

    public void recyclerView (View view){
        userSettingsRv = view.findViewById(R.id.userSettingsRv);
        userSettingsRv.setHasFixedSize(true);
        userSettingsRv.setLayoutManager(new LinearLayoutManager(getActivity()));

        UserSettingsClass s1 = new UserSettingsClass("Kullanıcı Bilgilerim", "icon_person_black");
        UserSettingsClass s2 = new UserSettingsClass("Randevu Geçmişim", "icon_history_black");
        UserSettingsClass s3 = new UserSettingsClass("Değerlendirmelerim", "icon_comment_black");
        UserSettingsClass s4 = new UserSettingsClass("Bildirim Merkezi", "icon_notification_black");
        UserSettingsClass s5 = new UserSettingsClass("Çıkış Yap", "icon_logout_black");

        userSettingsClassArrayList = new ArrayList<>();
        userSettingsClassArrayList.add(s1);
        userSettingsClassArrayList.add(s2);
        userSettingsClassArrayList.add(s3);
        userSettingsClassArrayList.add(s4);
        userSettingsClassArrayList.add(s5);

        userSettingsAdapter = new UserSettingsAdapter(getActivity(), userSettingsClassArrayList);
        userSettingsRv.setAdapter(userSettingsAdapter);
    }
}
