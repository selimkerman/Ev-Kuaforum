package com.abaskan.evkuaforum.UserFragment;

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

import com.abaskan.evkuaforum.R;
import com.abaskan.evkuaforum.UserAdapter.UserChatListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FragmentUserChats extends Fragment {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    Toolbar userChatListToolbar;
    RecyclerView userChatListRv;
    private ArrayList<String> barberNameList;
    private ArrayList<String> barberIdList;
    UserChatListAdapter userChatListAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_chats, container, false);
        toolbar(view);
        findViewById();
        getChatListFromFirebase();
        setAdapter(view);
        return view;
    }

    public void toolbar(View view){
        userChatListToolbar = view.findViewById(R.id.userChatListToolbar);
        userChatListToolbar.setTitle("     Mesajlarım");
        userChatListToolbar.setTitleTextColor(Color.WHITE);
    }

    public void findViewById(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        barberNameList = new ArrayList<>();
        barberIdList = new ArrayList<>();
    }

    public void getChatListFromFirebase (){
        CollectionReference collectionReference = firebaseFirestore.collection("UserFields").document(firebaseAuth.getUid()).collection("Chats");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots != null){
                    for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String,Object> barberData = documentSnapshot.getData();
                        String barberId = (String) barberData.get("barberId");
                        String barberName = (String) barberData.get("barberName");

                        barberNameList.add(barberName);
                        barberIdList.add(barberId);
                        userChatListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }


     public void setAdapter (View view){
         userChatListRv = view.findViewById(R.id.userChatListRv);
         userChatListRv.setHasFixedSize(true);
         userChatListRv.setLayoutManager(new LinearLayoutManager(getActivity()));
         userChatListAdapter = new UserChatListAdapter(getActivity(),barberNameList,barberIdList);
         userChatListRv.setAdapter(userChatListAdapter);
    }

}
