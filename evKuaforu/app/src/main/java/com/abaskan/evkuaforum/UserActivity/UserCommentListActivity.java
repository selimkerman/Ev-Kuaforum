package com.abaskan.evkuaforum.UserActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.abaskan.evkuaforum.R;
import com.abaskan.evkuaforum.UserAdapter.UserCommentListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class UserCommentListActivity extends AppCompatActivity {
    Toolbar userCommentListToolbar;
    RecyclerView userCommentListRv;
    private ArrayList<String> commentList;
    private ArrayList<Double> ratingList;
    private ArrayList<String> barberNameList;
    private ArrayList<String> appointmentTimeList;
    private ArrayList<String> serviceNameList;
    private ArrayList<Integer> totalPriceList;
    private ArrayList<String> barberIdList;
    private ArrayList<String> placeList;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    UserCommentListAdapter userCommentListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_comment_list);
        toolbar();
        findViewById();
        getCommentsFromFirebase();
        setAdapter();

    }

    public void toolbar(){
        userCommentListToolbar = findViewById(R.id.userCommentListToolbar);
        userCommentListToolbar.setTitle("Değerlendirmelerim");
        userCommentListToolbar.setTitleTextColor(Color.WHITE);
        userCommentListToolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        setSupportActionBar(userCommentListToolbar);
        userCommentListToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void findViewById(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        commentList = new ArrayList<>();
        barberNameList = new ArrayList<>();
        appointmentTimeList = new ArrayList<>();
        ratingList = new ArrayList<>();
        totalPriceList = new ArrayList<>();
        barberIdList = new ArrayList<>();
        serviceNameList = new ArrayList<>();
        placeList = new ArrayList<>();
    }

    public void getCommentsFromFirebase(){
        CollectionReference collectionReference = firebaseFirestore.collection("UserFields").document(firebaseAuth.getUid())
                .collection("Comments");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots != null) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        Map<String, Object> commentData = documentSnapshot.getData();
                        final String barberId = (String) commentData.get("barberId");
                        final String appointmentsTime = (String) commentData.get("appDate");
                        String barberName = (String) commentData.get("barberName");
                        String comment = (String) commentData.get("comment");
                        String place = (String) commentData.get("place");
                        double rating = documentSnapshot.getDouble("rating");
                        final String serviceName = (String) commentData.get("serviceName");
                        double totalPrice = documentSnapshot.getDouble("totalPrice");

                        commentList.add(comment);
                        ratingList.add(rating);
                        barberNameList.add(barberName);
                        appointmentTimeList.add(appointmentsTime);
                        serviceNameList.add(serviceName);
                        totalPriceList.add((int) totalPrice);
                        placeList.add(place);
                        barberIdList.add(barberId);

                        userCommentListAdapter.notifyDataSetChanged();
                    }
                }
            }

        });


    }

    public void setAdapter(){
        userCommentListRv = findViewById(R.id.userCommentListRv);
        userCommentListRv.setHasFixedSize(true);
        userCommentListRv.setLayoutManager(new LinearLayoutManager(this));
        userCommentListAdapter = new UserCommentListAdapter(this,commentList,ratingList,barberNameList,appointmentTimeList,serviceNameList,totalPriceList,barberIdList,placeList);
        userCommentListRv.setAdapter(userCommentListAdapter);
    }

}
