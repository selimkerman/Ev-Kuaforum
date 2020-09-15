package com.abaskan.evkuaforum.UserFragment;

import android.app.SearchManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.abaskan.evkuaforum.R;
import com.abaskan.evkuaforum.UserActivity.UserMainActivity;
import com.abaskan.evkuaforum.UserAdapter.BarberListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class FragmentUserMainPage extends Fragment {
    Toolbar userMainPageToolbar;
    RecyclerView userMainPageRv;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ArrayList<String> barberImageUrl;
    ArrayList<String> barberName;
    ArrayList<String> barberAdress;
    ArrayList<String> barberProvince;
    ArrayList<Double> barberDistance;
    BarberListAdapter barberListAdapter;
    ArrayList<Double> barberRatingList;
    ArrayList<Long> barberCommentCountList;
    ArrayList<String> barberUid;

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_main_page, container, false);
        toolbar(view);
        findViewById(view);
        addDistanceToFirebase();
        getBarberDataFromFirebase();
        setAdapter(view);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void toolbar(View view) {
        userMainPageToolbar = view.findViewById(R.id.userMainPageToolbar);
        userMainPageToolbar.setTitle("     Ana Sayfa");
        userMainPageToolbar.setTitleTextColor(Color.WHITE);
        ((UserMainActivity)getContext()).setSupportActionBar(userMainPageToolbar);
    }

    public void findViewById(View view) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        barberImageUrl = new ArrayList<>();
        barberAdress = new ArrayList<>();
        barberName = new ArrayList<>();
        barberProvince = new ArrayList<>();
        barberDistance = new ArrayList<>();
        barberUid = new ArrayList<>();
        barberRatingList = new ArrayList<>();
        barberCommentCountList = new ArrayList<>();
    }

    public void getBarberDataFromFirebase() {
        CollectionReference collectionReference = firebaseFirestore.collection("Barbers");
        collectionReference.orderBy("distance").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (queryDocumentSnapshots != null) {

                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {

                        final Map<String, Object> barberData = documentSnapshot.getData();

                        final String uid = documentSnapshot.getId();
                        final String url = (String) barberData.get("barberImageUrl");
                        final String name = (String) barberData.get("barberStoreName");
                        final String adress = (String) barberData.get("barberAdress");
                        final String province = (String) barberData.get("barberProvince");
                        final String district = (String) barberData.get("barberDistrict");
                        double barberPoint = documentSnapshot.getDouble("barberPoint");
                        long commentCount = documentSnapshot.getLong("commentCount");

                        if (!adress.equals("null") && !url.equals("null") && !district.equals("null")) {

                            double distance = (double) barberData.get("distance");

                            barberImageUrl.add(url);
                            barberName.add(name);
                            barberAdress.add(adress);
                            barberProvince.add(district + ", " + province);
                            barberUid.add(uid);
                            barberDistance.add(distance);
                            barberRatingList.add(barberPoint);
                            barberCommentCountList.add(commentCount);

                            barberListAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    public void setAdapter(View view) {
        userMainPageRv = view.findViewById(R.id.userMainPageRv);
        userMainPageRv.setHasFixedSize(true);
        userMainPageRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        barberListAdapter = new BarberListAdapter(getActivity(), barberImageUrl, barberName, barberAdress, barberProvince, barberUid, barberDistance, barberRatingList, barberCommentCountList);
        userMainPageRv.setAdapter(barberListAdapter);
    }

    public void addDistanceToFirebase() {
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                final Location userLocation = new Location("");
                if(documentSnapshot.getData()!= null){
                    Map<String, Object> userData = documentSnapshot.getData();
                    double userLat = (double) userData.get("userAdressLat");
                    double userLng = (double) userData.get("userAdressLng");
                    userLocation.setLatitude(userLat);
                    userLocation.setLongitude(userLng);
                }

                CollectionReference collectionReference = firebaseFirestore.collection("Barbers");
                collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (queryDocumentSnapshots != null) {

                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                if(documentSnapshot.getData() != null){
                                    Map<String, Object> barberData = documentSnapshot.getData();

                                    String uid = documentSnapshot.getId();
                                    String url = (String) barberData.get("barberImageUrl");
                                    String adress = (String) barberData.get("barberAdress");
                                    String district = (String) barberData.get("barberDistrict");

                                    if (!adress.equals("null") && !url.equals("null") && !district.equals("null")) {

                                        double barberLat = (double) barberData.get("barberAdressLat");
                                        double barberLng = (double) barberData.get("barberAdressLng");
                                        Location barberLocation = new Location("");

                                        barberLocation.setLatitude(barberLat);
                                        barberLocation.setLongitude(barberLng);

                                        double distanceInMeters = userLocation.distanceTo(barberLocation);
                                        double distanceInKm = distanceInMeters / 1000.0;
                                        String distanceFormat = String.format(Locale.US, "%.2f", distanceInKm);
                                        double distanceDouble = Double.parseDouble(distanceFormat);
                                        firebaseFirestore.collection("Barbers").document(uid).update("distance", distanceDouble);
                                    }
                                }


                            }
                        }
                    }
                });
            }
        });

    }
}
