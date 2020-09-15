package com.abaskan.evkuaforum.BarberActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.abaskan.evkuaforum.BarberAdapter.BarberDetailReviewsAdapter;
import com.abaskan.evkuaforum.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class BarberCommentListActivity extends AppCompatActivity {
    Toolbar barberCommentListToolbar;
    TextView barberCommentListPoint, barberCommentListCount1, barberCommentListCount2, barberCommentListCount3,
            barberCommentListCount4, barberCommentListCount5;
    RecyclerView barberCommentListRv;
    ProgressBar barberCommentListProgressPoint1, barberCommentListProgressPoint2, barberCommentListProgressPoint3,
            barberCommentListProgressPoint4, barberCommentListProgressPoint5;

    private ArrayList<String> commentList;
    private ArrayList<Double> ratingList;
    private ArrayList<String> userNameList;
    private ArrayList<String> appointmentTimeList;
    private ArrayList<String> serviceNameList;
    private ArrayList<Integer> totalPriceList;
    private ArrayList<String> userIdList;
    private ArrayList<String> placeList;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    BarberDetailReviewsAdapter barberDetailReviewsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_comment_list);

        toolbar();
        findViewById();
        setRatings();
        getAverageRatingFromFirebase();
        getCommentsFromFirebase();
        setAdapter();
    }

    public void toolbar() {
        barberCommentListToolbar = findViewById(R.id.barberCommentListToolbar);
        barberCommentListToolbar.setTitle("Değerlendirmelerim");
        barberCommentListToolbar.setTitleTextColor(Color.WHITE);
        barberCommentListToolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        setSupportActionBar(barberCommentListToolbar);
        barberCommentListToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
        userNameList = new ArrayList<>();
        appointmentTimeList = new ArrayList<>();
        ratingList = new ArrayList<>();
        totalPriceList = new ArrayList<>();
        userIdList = new ArrayList<>();
        serviceNameList = new ArrayList<>();
        placeList = new ArrayList<>();

        barberCommentListPoint = findViewById(R.id.barberCommentListPoint);
        barberCommentListCount1 = findViewById(R.id.barberCommentListCount1);
        barberCommentListCount2 = findViewById(R.id.barberCommentListCount2);
        barberCommentListCount3 = findViewById(R.id.barberCommentListCount3);
        barberCommentListCount4 = findViewById(R.id.barberCommentListCount4);
        barberCommentListCount5 = findViewById(R.id.barberCommentListCount5);

        barberCommentListProgressPoint1 = findViewById(R.id.barberCommentListProgressPoint1);
        barberCommentListProgressPoint2 = findViewById(R.id.barberCommentListProgressPoint2);
        barberCommentListProgressPoint3 = findViewById(R.id.barberCommentListProgressPoint3);
        barberCommentListProgressPoint4 = findViewById(R.id.barberCommentListProgressPoint4);
        barberCommentListProgressPoint5 = findViewById(R.id.barberCommentListProgressPoint5);

        barberCommentListRv = findViewById(R.id.barberCommentListRv);
    }

    public void setRatings(){
        getRatingsFromFirebase("1",barberCommentListCount1);
        getRatingsFromFirebase("2",barberCommentListCount2);
        getRatingsFromFirebase("3",barberCommentListCount3);
        getRatingsFromFirebase("4",barberCommentListCount4);
        getRatingsFromFirebase("5",barberCommentListCount5);

        setProgressBarProgress("1",barberCommentListProgressPoint1);
        setProgressBarProgress("2",barberCommentListProgressPoint2);
        setProgressBarProgress("3",barberCommentListProgressPoint3);
        setProgressBarProgress("4",barberCommentListProgressPoint4);
        setProgressBarProgress("5",barberCommentListProgressPoint5);
    }

    public void getRatingsFromFirebase(String rating, final TextView countText){

        DocumentReference documentReference = firebaseFirestore.collection("BarberFields").document(firebaseAuth.getUid())
                .collection("Ratings").document(rating);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getData() != null){
                    Map countTotal = documentSnapshot.getData();
                    long count = (long) countTotal.get("totalComment");
                    countText.setText(String.valueOf(count));
                }
            }
        });
    }

    public void getAverageRatingFromFirebase(){

        final DocumentReference documentReference = firebaseFirestore.collection("BarberFields").document(firebaseAuth.getUid())
                .collection("Ratings").document("totalRating");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getData() != null){
                    Map averagePoint = documentSnapshot.getData();
                    double point = (double) averagePoint.get("averagePoint");
                   barberCommentListPoint.setText(String.format(Locale.US,"%.2f",point));
                }
            }
        });
    }

    public void setProgressBarProgress(final String rating, final ProgressBar progressBar){

        final DocumentReference documentReference = firebaseFirestore.collection("BarberFields").document(firebaseAuth.getUid())
                .collection("Ratings").document("totalRating");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getData() != null){
                    Map totalCount = documentSnapshot.getData();
                    final long totalComment = (long) totalCount.get("totalComment");

                    DocumentReference documentReference2 = firebaseFirestore.collection("BarberFields").document(firebaseAuth.getUid())
                            .collection("Ratings").document(rating);

                    documentReference2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.getData() != null){
                                Map countTotal = documentSnapshot.getData();
                                long count = (long) countTotal.get("totalComment");

                                double progress = (double) count / totalComment;
                                progressBar.setProgress((int)(progress*100));
                            }
                        }
                    });
                }
            }
        });
    }

    public void getCommentsFromFirebase(){
        CollectionReference collectionReference = firebaseFirestore.collection("BarberFields")
                .document(firebaseAuth.getUid()).collection("Comments");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots != null) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        if(documentSnapshot.getData() != null){
                            Map<String, Object> commentData = documentSnapshot.getData();
                            final String userId = (String) commentData.get("userId");
                            final String appointmentsTime = (String) commentData.get("appDate");
                            String userName = (String) commentData.get("userName");
                            String comment = (String) commentData.get("comment");
                            double rating = documentSnapshot.getDouble("rating");
                            final String serviceName = (String) commentData.get("serviceName");
                            double totalPrice = documentSnapshot.getDouble("totalPrice");
                            String place = (String) commentData.get("place");

                            commentList.add(comment);
                            ratingList.add(rating);
                            userNameList.add(userName);
                            appointmentTimeList.add(appointmentsTime);
                            serviceNameList.add(serviceName);
                            totalPriceList.add((int) totalPrice);
                            userIdList.add(userId);
                            placeList.add(place);
                            barberDetailReviewsAdapter.notifyDataSetChanged();
                        }

                    }
                }
            }
        });
    }

    public void setAdapter(){
        barberCommentListRv.setHasFixedSize(true);
        barberCommentListRv.setLayoutManager(new LinearLayoutManager(this));
        barberDetailReviewsAdapter = new BarberDetailReviewsAdapter(this,commentList,ratingList,userNameList,appointmentTimeList,serviceNameList,totalPriceList,userIdList,placeList);
        barberCommentListRv.setAdapter(barberDetailReviewsAdapter);
    }


}
