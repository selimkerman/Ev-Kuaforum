package com.abaskan.evkuaforum.UserFragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class FragmentBarberDetailReviews extends Fragment {
    TextView barberDetailCommentPoint, barberDetailCommentCount1, barberDetailCommentCount2, barberDetailCommentCount3,
            barberDetailCommentCount4, barberDetailCommentCount5;
    RecyclerView barberDetailCommentRv;
    ProgressBar barberDetailCommentProgressPoint1, barberDetailCommentProgressPoint2, barberDetailCommentProgressPoint3,
            barberDetailCommentProgressPoint4, barberDetailCommentProgressPoint5;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_barber_detail_reviews, container, false);

        findViewById(view);
        setRatings();
        getAverageRatingFromFirebase();
        getCommentsFromFirebase();
        setAdapter();

        return view;
    }

    public void findViewById(View view){
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

        barberDetailCommentPoint = view.findViewById(R.id.barberDetailCommentPoint);
        barberDetailCommentCount1 = view.findViewById(R.id.barberDetailCommentCount1);
        barberDetailCommentCount2 = view.findViewById(R.id.barberDetailCommentCount2);
        barberDetailCommentCount3 = view.findViewById(R.id.barberDetailCommentCount3);
        barberDetailCommentCount4 = view.findViewById(R.id.barberDetailCommentCount4);
        barberDetailCommentCount5 = view.findViewById(R.id.barberDetailCommentCount5);

        barberDetailCommentProgressPoint1 = view.findViewById(R.id.barberDetailCommentProgressPoint1);
        barberDetailCommentProgressPoint2 = view.findViewById(R.id.barberDetailCommentProgressPoint2);
        barberDetailCommentProgressPoint3 = view.findViewById(R.id.barberDetailCommentProgressPoint3);
        barberDetailCommentProgressPoint4 = view.findViewById(R.id.barberDetailCommentProgressPoint4);
        barberDetailCommentProgressPoint5 = view.findViewById(R.id.barberDetailCommentProgressPoint5);

        barberDetailCommentRv = view.findViewById(R.id.barberDetailCommentRv);
    }

    public void setRatings(){
        getRatingsFromFirebase("1",barberDetailCommentCount1);
        getRatingsFromFirebase("2",barberDetailCommentCount2);
        getRatingsFromFirebase("3",barberDetailCommentCount3);
        getRatingsFromFirebase("4",barberDetailCommentCount4);
        getRatingsFromFirebase("5",barberDetailCommentCount5);

        setProgressBarProgress("1",barberDetailCommentProgressPoint1);
        setProgressBarProgress("2",barberDetailCommentProgressPoint2);
        setProgressBarProgress("3",barberDetailCommentProgressPoint3);
        setProgressBarProgress("4",barberDetailCommentProgressPoint4);
        setProgressBarProgress("5",barberDetailCommentProgressPoint5);
    }

    public void getRatingsFromFirebase(String rating, final TextView countText){
        String barberId = getActivity().getIntent().getStringExtra("barberId");

        DocumentReference documentReference = firebaseFirestore.collection("BarberFields").document(barberId)
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
        String barberId = getActivity().getIntent().getStringExtra("barberId");

        final DocumentReference documentReference = firebaseFirestore.collection("BarberFields").document(barberId)
                .collection("Ratings").document("totalRating");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getData() != null){
                    Map averagePoint = documentSnapshot.getData();
                    double point = (double) averagePoint.get("averagePoint");
                    barberDetailCommentPoint.setText(String.format(Locale.US,"%.2f",point));
                }
            }
        });
    }


    public void setProgressBarProgress(final String rating, final ProgressBar progressBar){
        final String barberId = getActivity().getIntent().getStringExtra("barberId");

        final DocumentReference documentReference = firebaseFirestore.collection("BarberFields").document(barberId)
                .collection("Ratings").document("totalRating");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getData() != null){
                    Map totalCount = documentSnapshot.getData();
                    final long totalComment = (long) totalCount.get("totalComment");

                    DocumentReference documentReference2 = firebaseFirestore.collection("BarberFields").document(barberId)
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
        String barberId = getActivity().getIntent().getStringExtra("barberId");
        CollectionReference collectionReference = firebaseFirestore.collection("BarberFields")
                .document(barberId).collection("Comments");
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
        barberDetailCommentRv.setHasFixedSize(true);
        barberDetailCommentRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        barberDetailReviewsAdapter = new BarberDetailReviewsAdapter(getActivity(),commentList,ratingList,userNameList,appointmentTimeList,serviceNameList,totalPriceList,userIdList,placeList);
        barberDetailCommentRv.setAdapter(barberDetailReviewsAdapter);
    }
}
