package com.abaskan.evkuaforum.BarberAdapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abaskan.evkuaforum.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.Random;

public class BarberDetailReviewsAdapter extends RecyclerView.Adapter<BarberDetailReviewsAdapter.BarberDetailCommentHolder>{
    private Context context;
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


    public BarberDetailReviewsAdapter(Context context, ArrayList<String> commentList, ArrayList<Double> ratingList, ArrayList<String> userNameList, ArrayList<String> appointmentTimeList, ArrayList<String> serviceNameList, ArrayList<Integer> totalPriceList, ArrayList<String> userIdList, ArrayList<String> placeList) {
        this.context = context;
        this.commentList = commentList;
        this.ratingList = ratingList;
        this.userNameList = userNameList;
        this.appointmentTimeList = appointmentTimeList;
        this.serviceNameList = serviceNameList;
        this.totalPriceList = totalPriceList;
        this.userIdList = userIdList;
        this.placeList = placeList;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public BarberDetailCommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_barber_detail_comment,parent,false);

        return new BarberDetailCommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarberDetailCommentHolder holder, int position) {
        holder.barberDetailCommentUserIcon.setText(userNameList.get(position).substring(0,1));
        Random random = new Random();
        final int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        ((GradientDrawable) holder.barberDetailCommentUserIcon.getBackground()).setColor(color);

        holder.barberDetailCommentUserName.setText(userNameList.get(position));
        holder.barberDetailCommentDate.setText(appointmentTimeList.get(position));
        holder.barberDetailCommentService.setText(serviceNameList.get(position));
        holder.barberDetailCommentPrice.setText(String.format("%s TL",totalPriceList.get(position)));
        holder.barberDetailCommentRating.setText(String.valueOf(ratingList.get(position)));
        holder.barberDetailCommentComment.setText(commentList.get(position));
        holder.barberDetailCommentPlace.setText(placeList.get(position));
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class BarberDetailCommentHolder extends RecyclerView.ViewHolder{
        CardView barberDetailCommentCardview;
        TextView barberDetailCommentUserIcon;
        TextView barberDetailCommentUserName;
        TextView barberDetailCommentDate;
        TextView barberDetailCommentService;
        TextView barberDetailCommentPrice;
        TextView barberDetailCommentRating;
        TextView barberDetailCommentComment;
        TextView barberDetailCommentPlace;

        public BarberDetailCommentHolder(@NonNull View itemView) {
            super(itemView);
            barberDetailCommentCardview = itemView.findViewById(R.id.barberDetailCommentCardview );
            barberDetailCommentUserIcon = itemView.findViewById(R.id.barberDetailCommentUserIcon );
            barberDetailCommentUserName = itemView.findViewById(R.id.barberDetailCommentUserName );
            barberDetailCommentDate = itemView.findViewById(R.id.barberDetailCommentDate );
            barberDetailCommentService = itemView.findViewById(R.id.barberDetailCommentService );
            barberDetailCommentPrice = itemView.findViewById(R.id.barberDetailCommentPrice );
            barberDetailCommentRating = itemView.findViewById(R.id.barberDetailCommentRating );
            barberDetailCommentComment = itemView.findViewById(R.id.barberDetailCommentComment );
            barberDetailCommentPlace = itemView.findViewById(R.id.barberDetailCommentPlace);
        }
    }
}
