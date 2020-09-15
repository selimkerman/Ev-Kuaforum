package com.abaskan.evkuaforum.UserAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abaskan.evkuaforum.R;
import com.abaskan.evkuaforum.UserActivity.UserAddCommentActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAppointmentHistoryAdapter extends RecyclerView.Adapter<UserAppointmentHistoryAdapter.UserAppointmentHistoryHolder>{
    private Context context;
    private ArrayList<String> barberImageUrlList;
    private ArrayList<String> barberNameList;
    private ArrayList<String> appointmentTimeList;
    private ArrayList<String> serviceNameList;
    private ArrayList<Integer> totalPriceList;
    private ArrayList<String> barberIdList;
    private ArrayList <Boolean> addingCommentList;
    private ArrayList <Long> appointmentLongList;
    private ArrayList<String> placeList;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;


    public UserAppointmentHistoryAdapter(Context context, ArrayList<String> barberImageUrlList, ArrayList<String> barberNameList, ArrayList<String> appointmentTimeList, ArrayList<String> serviceNameList, ArrayList<Integer> totalPriceList, ArrayList<String> barberIdList, ArrayList<Boolean> addingCommentList, ArrayList <Long> appointmentLongList, ArrayList<String> placeList) {
        this.context = context;
        this.barberImageUrlList = barberImageUrlList;
        this.barberNameList = barberNameList;
        this.appointmentTimeList = appointmentTimeList;
        this.serviceNameList = serviceNameList;
        this.totalPriceList = totalPriceList;
        this.barberIdList = barberIdList;
        this.addingCommentList = addingCommentList;
        this.appointmentLongList = appointmentLongList;
        this.placeList = placeList;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public UserAppointmentHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_user_appointment_history,parent,false);
        return new UserAppointmentHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserAppointmentHistoryHolder holder, final int position) {
        Picasso.get().load(barberImageUrlList.get(position)).into(holder.userAppointmentHistoryImageView);
        holder.userAppointmentHistoryBarberName.setText(barberNameList.get(position));
        holder.userAppointmentHistoryDate.setText(appointmentTimeList.get(position));
        holder.userAppointmentHistoryService.setText(serviceNameList.get(position));
        holder.userAppointmentHistoryPrice.setText(String.format("%s TL", totalPriceList.get(position)));
        holder.userAppointmentHistoryPlace.setText(placeList.get(position));

        if(addingCommentList.get(position)){
            holder.userAppointmentHistoryAddComment.setText("Yorum Yapıldı");
            holder.userAppointmentHistoryAddComment.setTextColor(Color.RED);
            holder.userAppointmentHistoryAddComment.setClickable(false);
        }else{
            holder.userAppointmentHistoryAddComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent toAddComment = new Intent(v.getContext(), UserAddCommentActivity.class);
                    toAddComment.putExtra("barberImageUrl",barberImageUrlList.get(position));
                    toAddComment.putExtra("barberName",barberNameList.get(position));
                    toAddComment.putExtra("appDate",appointmentTimeList.get(position));
                    toAddComment.putExtra("serviceName",serviceNameList.get(position));
                    toAddComment.putExtra("barberId",barberIdList.get(position));
                    toAddComment.putExtra("totalPrice",totalPriceList.get(position));
                    toAddComment.putExtra("appointmentLong",appointmentLongList.get(position));
                    toAddComment.putExtra("place",placeList.get(position));
                    toAddComment.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(toAddComment);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return barberNameList.size();
    }

    public class UserAppointmentHistoryHolder extends RecyclerView.ViewHolder{
        ImageView userAppointmentHistoryImageView;
        TextView userAppointmentHistoryBarberName;
        TextView userAppointmentHistoryDate;
        TextView userAppointmentHistoryService;
        TextView userAppointmentHistoryPrice;
        TextView userAppointmentHistoryAddComment;
        TextView userAppointmentHistoryPlace;
        CardView userAppointmentHistoryCardView;
        public UserAppointmentHistoryHolder(@NonNull View itemView) {
            super(itemView);
            userAppointmentHistoryImageView = itemView.findViewById(R.id.userAppointmentHistoryImageView);
            userAppointmentHistoryBarberName = itemView.findViewById(R.id.userAppointmentHistoryBarberName);
            userAppointmentHistoryDate = itemView.findViewById(R.id.userAppointmentHistoryDate);
            userAppointmentHistoryService = itemView.findViewById(R.id.userAppointmentHistoryService);
            userAppointmentHistoryPrice = itemView.findViewById(R.id.userAppointmentHistoryPrice);
            userAppointmentHistoryAddComment = itemView.findViewById(R.id.userAppointmentHistoryAddComment);
            userAppointmentHistoryPlace = itemView.findViewById(R.id.userAppointmentHistoryPlace);
            userAppointmentHistoryCardView = itemView.findViewById(R.id.userAppointmentHistoryCardView);
        }
    }
}
