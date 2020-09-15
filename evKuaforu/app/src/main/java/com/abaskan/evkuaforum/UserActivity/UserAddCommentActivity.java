package com.abaskan.evkuaforum.UserActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abaskan.evkuaforum.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class UserAddCommentActivity extends AppCompatActivity {
    Toolbar userAddCommentToolbar;
    ImageView userAddCommentImageView;
    TextView userAddCommentBarberName, userAddCommentDate, userAddCommentService, userAddCommentPrice, userAddCommentPlace;
    RatingBar userAddCommentRatingBar;
    EditText userAddCommentCommentText;
    Button userAddCommentButton;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add_comment);
        toolbar();
        findViewById();
        getAppointmentInfo();

    }

    public void userAddCommentButtonClick (View view){
        final String barberId = getIntent().getStringExtra("barberId");
        String barberName = getIntent().getStringExtra("barberName");
        final String appDate = getIntent().getStringExtra("appDate");
        String serviceName = getIntent().getStringExtra("serviceName");
        String place = getIntent().getStringExtra("place");
        double totalPrice = getIntent().getIntExtra("totalPrice",1);

        String comment = userAddCommentCommentText.getText().toString().trim();
        double rating = userAddCommentRatingBar.getRating();

        final int ratingInt = (int) userAddCommentRatingBar.getRating();

        final Map commentData = new HashMap();
        commentData.put("barberId",barberId);
        commentData.put("barberName",barberName);
        commentData.put("appDate",appDate);
        commentData.put("serviceName",serviceName);
        commentData.put("totalPrice",totalPrice);
        commentData.put("comment",comment);
        commentData.put("rating",rating);
        commentData.put("place",place);
        commentData.put("userId",firebaseAuth.getUid());

        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String userName = documentSnapshot.getString("userName");
                commentData.put("userName",userName);

                firebaseFirestore.collection("UserFields").document(firebaseAuth.getUid()).collection("Comments")
                        .document(appDate).set(commentData);

                firebaseFirestore.collection("BarberFields").document(barberId).collection("Comments")
                        .document(appDate).set(commentData);

                Map addComment = new HashMap();
                addComment.put("addedComment",true);
                long appointmentLong = getIntent().getLongExtra("appointmentLong",5);

                firebaseFirestore.collection("UserFields").document(firebaseAuth.getUid())
                        .collection("Appointments").document(String.valueOf(appointmentLong)).update(addComment);

                setTotalRating();
                setRating(String.valueOf(ratingInt));

                Toast.makeText(UserAddCommentActivity.this, "Yorumunuz Kaydedildi", Toast.LENGTH_LONG).show();

                userAddCommentButton.setClickable(false);
                if(!userAddCommentButton.isClickable()){
                    userAddCommentButton.setText("Daha Önce Yorum Yapıldı");
                }

            }
        });
    }

    public void getAppointmentInfo (){
        String barberName = getIntent().getStringExtra("barberName");
        String appDate = getIntent().getStringExtra("appDate");
        String serviceName = getIntent().getStringExtra("serviceName");
        int totalPrice = getIntent().getIntExtra("totalPrice",1);
        String barberImageUrl = getIntent().getStringExtra("barberImageUrl");
        String place = getIntent().getStringExtra("place");

        Picasso.get().load(barberImageUrl).into(userAddCommentImageView);

        userAddCommentBarberName.setText(barberName);
        userAddCommentDate.setText(appDate);
        userAddCommentService.setText(serviceName);
        userAddCommentPrice.setText(totalPrice + " TL");
        userAddCommentPlace.setText(place);
    }

    public void toolbar(){
        userAddCommentToolbar = findViewById(R.id.userAddCommentToolbar);
        userAddCommentToolbar.setTitle("Yorum Yap");
        userAddCommentToolbar.setTitleTextColor(Color.WHITE);
        userAddCommentToolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        setSupportActionBar(userAddCommentToolbar);
        userAddCommentToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void findViewById(){
        userAddCommentImageView = findViewById(R.id.userAddCommentImageView);
        userAddCommentBarberName = findViewById(R.id.userAddCommentBarberName);
        userAddCommentDate = findViewById(R.id.userAddCommentDate);
        userAddCommentService = findViewById(R.id.userAddCommentService);
        userAddCommentPrice = findViewById(R.id.userAddCommentPrice);
        userAddCommentRatingBar = findViewById(R.id.userAddCommentRatingBar);
        userAddCommentCommentText = findViewById(R.id.userAddCommentCommentText);
        userAddCommentButton = findViewById(R.id.userAddCommentButton);
        userAddCommentPlace = findViewById(R.id.userAddCommentPlace);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void setTotalRating (){
        final String barberId = getIntent().getStringExtra("barberId");

        DocumentReference documentReference = firebaseFirestore.collection("BarberFields").document(barberId)
                .collection("Ratings").document("totalRating");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getData() == null){
                    Map totalRating = new HashMap();
                    totalRating.put("totalComment",1);
                    totalRating.put("averagePoint",0.0);
                    firebaseFirestore.collection("BarberFields").document(barberId)
                            .collection("Ratings").document("totalRating").set(totalRating);
                    updateBarberPoint();
                }

                else{
                    Map gettingTotalRating = documentSnapshot.getData();
                    long totalComment  = (long) gettingTotalRating.get("totalComment");
                    totalComment += 1;
                    gettingTotalRating.put("totalComment",totalComment);
                    firebaseFirestore.collection("BarberFields").document(barberId)
                            .collection("Ratings").document("totalRating").update(gettingTotalRating);
                    updateBarberPoint();
                }
            }
        });
    }


    public void setRating (final String rating){
        final String barberId = getIntent().getStringExtra("barberId");

        DocumentReference documentReference = firebaseFirestore.collection("BarberFields").document(barberId)
                .collection("Ratings").document(rating);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getData() == null){
                    Map totalRating = new HashMap();
                    totalRating.put("totalComment",1);
                    firebaseFirestore.collection("BarberFields").document(barberId)
                            .collection("Ratings").document(rating).set(totalRating);
                }

                else{
                    Map gettingTotalRating = documentSnapshot.getData();
                    long totalComment  = (long) gettingTotalRating.get("totalComment");
                    totalComment += 1;
                    gettingTotalRating.put("totalComment",totalComment);
                    firebaseFirestore.collection("BarberFields").document(barberId)
                            .collection("Ratings").document(rating).update(gettingTotalRating);
                }
            }
        });
    }

    public void updateBarberPoint (){
        final String barberId = getIntent().getStringExtra("barberId");
        DocumentReference documentReference = firebaseFirestore.collection("BarberFields").document(barberId)
                .collection("Ratings").document("totalRating");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map gettingTotalRating = documentSnapshot.getData();
                long totalComment  = (long) gettingTotalRating.get("totalComment");
                double averagePoint = (double) gettingTotalRating.get("averagePoint");

                averagePoint = (averagePoint*(totalComment -1) + userAddCommentRatingBar.getRating()) / totalComment;
                Map newAverage = new HashMap();
                newAverage.put("averagePoint",averagePoint);

                Map newBarberPoint = new HashMap();
                newBarberPoint.put("barberPoint",averagePoint);
                newBarberPoint.put("commentCount",totalComment);

                firebaseFirestore.collection("BarberFields").document(barberId)
                        .collection("Ratings").document("totalRating").update(newAverage);

                firebaseFirestore.collection("Barbers").document(barberId).update(newBarberPoint);
            }
        });

    }

}
