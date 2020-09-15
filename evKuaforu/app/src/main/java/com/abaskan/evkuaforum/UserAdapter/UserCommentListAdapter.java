package com.abaskan.evkuaforum.UserAdapter;

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

import java.util.ArrayList;
import java.util.Random;

public class UserCommentListAdapter extends RecyclerView.Adapter<UserCommentListAdapter.UserCommentListHolder>{
    private Context context;
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

    public UserCommentListAdapter(Context context, ArrayList<String> commentList, ArrayList<Double> ratingList, ArrayList<String> barberNameList, ArrayList<String> appointmentTimeList, ArrayList<String> serviceNameList, ArrayList<Integer> totalPriceList, ArrayList<String> barberIdList, ArrayList<String> placeList) {
        this.context = context;
        this.commentList = commentList;
        this.ratingList = ratingList;
        this.barberNameList = barberNameList;
        this.appointmentTimeList = appointmentTimeList;
        this.serviceNameList = serviceNameList;
        this.totalPriceList = totalPriceList;
        this.barberIdList = barberIdList;
        this.placeList = placeList;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public UserCommentListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_user_comments,parent,false);

        return new UserCommentListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserCommentListHolder holder, int position) {
        holder.userCommentListBarberIcon.setText(barberNameList.get(position).substring(0,1));
        Random random = new Random();
        final int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        ((GradientDrawable) holder.userCommentListBarberIcon.getBackground()).setColor(color);

        holder.userCommentListBarberName.setText(barberNameList.get(position));
        holder.userCommentListDate.setText(appointmentTimeList.get(position));
        holder.userCommentListService.setText(serviceNameList.get(position));
        holder.userCommentListPrice.setText(String.format("%s TL",totalPriceList.get(position)));
        holder.userCommentListRating.setText(String.valueOf(ratingList.get(position)));
        holder.userCommentListComment.setText(commentList.get(position));
        holder.userCommentListPlace.setText(placeList.get(position));
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class UserCommentListHolder extends RecyclerView.ViewHolder{
        CardView userCommentListCardview;
        TextView userCommentListBarberIcon;
        TextView userCommentListBarberName;
        TextView userCommentListDate;
        TextView userCommentListService;
        TextView userCommentListPrice;
        TextView userCommentListRating;
        TextView userCommentListComment;
        TextView userCommentListPlace;

        public UserCommentListHolder(@NonNull View itemView) {
            super(itemView);
            userCommentListCardview = itemView.findViewById(R.id.userCommentListCardview );
            userCommentListBarberIcon = itemView.findViewById(R.id.userCommentListBarberIcon );
            userCommentListBarberName = itemView.findViewById(R.id.userCommentListBarberName );
            userCommentListDate = itemView.findViewById(R.id.userCommentListDate );
            userCommentListService = itemView.findViewById(R.id.userCommentListService );
            userCommentListPrice = itemView.findViewById(R.id.userCommentListPrice );
            userCommentListRating = itemView.findViewById(R.id.userCommentListRating );
            userCommentListComment = itemView.findViewById(R.id.userCommentListComment );
            userCommentListPlace = itemView.findViewById(R.id.userCommentListPlace);
        }
    }
}