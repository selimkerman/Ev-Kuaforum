package com.abaskan.evkuaforum.UserAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abaskan.evkuaforum.UserActivity.BarberDetailActivity;
import com.abaskan.evkuaforum.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BarberListAdapter extends RecyclerView.Adapter<BarberListAdapter.BarberListHolder>{
    private Context context;

    private ArrayList<String> barberImageUrl;
    private ArrayList<String> barberName;
    private ArrayList<String> barberAdress;
    private ArrayList<String> barberProvince;
    ArrayList<String> barberId;
    private ArrayList<Double> barberDistanceList;
    private ArrayList<Double> barberRatingList;
    private ArrayList<Long> barberCommentCountList;
    FirebaseFirestore firebaseFirestore;


    public BarberListAdapter(Context context, ArrayList<String> barberImageUrl, ArrayList<String> barberName, ArrayList<String> barberAdress, ArrayList<String> barberProvince, ArrayList<String> barberId, ArrayList<Double> barberDistanceList, ArrayList<Double> barberRatingList,ArrayList<Long> barberCommentCountList) {
        this.context = context;
        this.barberImageUrl = barberImageUrl;
        this.barberName = barberName;
        this.barberAdress = barberAdress;
        this.barberProvince = barberProvince;
        this.barberId = barberId;
        this.barberDistanceList = barberDistanceList;
        this.barberRatingList = barberRatingList;
        this.barberCommentCountList = barberCommentCountList;
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public BarberListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_barber_list,parent,false);
        return new  BarberListHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull BarberListHolder holder, final int position) {
        Picasso.get().load(barberImageUrl.get(position)).into(holder.barberListPhoto);
        holder.barberListName.setText(barberName.get(position));
        holder.barberListAdress.setText(barberAdress.get(position));
        holder.barberListProvince.setText(barberProvince.get(position));
        holder.barberListDistance.setText(String.format("%s Km", barberDistanceList.get(position)));
        holder.barberListPoint.setText(String.format(Locale.US,"%.1f",barberRatingList.get(position)));
        holder.barberListCommentCount.setText(String.valueOf(barberCommentCountList.get(position)));

        holder.barberListCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toBarberDetail = new Intent(v.getContext(), BarberDetailActivity.class);
                toBarberDetail.putExtra("barberId",barberId.get(position));
                toBarberDetail.putExtra("barberName",barberName.get(position));
                toBarberDetail.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(toBarberDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return barberAdress.size();
    }


    public class BarberListHolder extends RecyclerView.ViewHolder{
        CardView barberListCard;
        ImageView barberListPhoto;
        TextView barberListName;
        TextView barberListAdress;
        TextView barberListProvince;
        TextView barberListDistance;
        TextView barberListPoint;
        TextView barberListCommentCount;
        public BarberListHolder(@NonNull View itemView) {
            super(itemView);
            barberListPhoto = itemView.findViewById(R.id.barberListPhoto);
            barberListName = itemView.findViewById(R.id.barberListName);
            barberListAdress = itemView.findViewById(R.id.barberListAdress);
            barberListProvince = itemView.findViewById(R.id.barberListProvince);
            barberListDistance = itemView.findViewById(R.id.barberListDistance);
            barberListCard = itemView.findViewById(R.id.barberListCard);
            barberListPoint = itemView.findViewById(R.id.barberListPoint);
            barberListCommentCount = itemView.findViewById(R.id.barberListCommentCount);
        }
    }
}
