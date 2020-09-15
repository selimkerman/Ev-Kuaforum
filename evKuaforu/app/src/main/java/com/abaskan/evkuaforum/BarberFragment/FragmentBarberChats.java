package com.abaskan.evkuaforum.BarberFragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abaskan.evkuaforum.BarberAdapter.BarberChatListAdapter;
import com.abaskan.evkuaforum.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FragmentBarberChats extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    Toolbar barberChatListToolbar;
    RecyclerView barberChatListRv;
    private ArrayList<String> userNameList;
    private ArrayList<String> userIdList;
    BarberChatListAdapter barberChatListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_barber_chats, container, false);
        toolbar(view);
        findViewById();
        getChatListFromFirebase();
        setAdapter(view);


        return view;
    }

    public void toolbar(View view){
        barberChatListToolbar = view.findViewById(R.id.barberChatListToolbar);
        barberChatListToolbar.setTitle("     Mesajlarım");
        barberChatListToolbar.setTitleTextColor(Color.WHITE);
    }

    public void findViewById(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userNameList = new ArrayList<>();
        userIdList = new ArrayList<>();
    }

    public void getChatListFromFirebase (){
        CollectionReference collectionReference = firebaseFirestore.collection("BarberFields").document(firebaseAuth.getUid())
                .collection("Chats");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots != null){
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String,Object> userData = documentSnapshot.getData();
                        String userId = (String) userData.get("userId");
                        String userName = (String) userData.get("userName");
                       
                        userNameList.add(userName);
                        userIdList.add(userId);

                        barberChatListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

    public void setAdapter (View view){
        barberChatListRv = view.findViewById(R.id.barberChatListRv);
        barberChatListRv.setHasFixedSize(true);
        barberChatListRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        barberChatListAdapter = new BarberChatListAdapter(getActivity(),userNameList,userIdList);
        barberChatListRv.setAdapter(barberChatListAdapter);
    }


}
