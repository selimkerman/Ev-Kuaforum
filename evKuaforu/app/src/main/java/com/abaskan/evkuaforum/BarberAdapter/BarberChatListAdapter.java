package com.abaskan.evkuaforum.BarberAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abaskan.evkuaforum.BarberActivity.BarberChatActivity;
import com.abaskan.evkuaforum.R;

import java.util.ArrayList;
import java.util.Random;

public class BarberChatListAdapter extends RecyclerView.Adapter<BarberChatListAdapter.BarberChatListHolder>{
    private Context context;
    private ArrayList<String> userNameList;
    private ArrayList<String> userIdList;

    public BarberChatListAdapter(Context context, ArrayList<String> userNameList, ArrayList<String> userIdList) {
        this.context = context;
        this.userNameList = userNameList;
        this.userIdList = userIdList;
    }

    @NonNull
    @Override
    public BarberChatListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_barber_chat_list,parent,false);
        return new BarberChatListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarberChatListHolder holder, final int position) {
        holder.barberChatListUserIcon.setText(userNameList.get(position).substring(0,1));
        Random random = new Random();
        final int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        ((GradientDrawable) holder.barberChatListUserIcon.getBackground()).setColor(color);

        holder.barberChatListUserName.setText(userNameList.get(position));

        holder.barberChatListCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toChat = new Intent(v.getContext(), BarberChatActivity.class);
                toChat.putExtra("userId",userIdList.get(position));
                toChat.putExtra("userName",userNameList.get(position));
                toChat.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(toChat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userNameList.size();
    }

    public class BarberChatListHolder extends RecyclerView.ViewHolder{
        TextView barberChatListUserIcon;
        TextView barberChatListUserName;
        CardView barberChatListCardView;
        public BarberChatListHolder(@NonNull View itemView) {
            super(itemView);
            barberChatListUserIcon = itemView.findViewById(R.id.barberChatListUserIcon);
            barberChatListUserName = itemView.findViewById(R.id.barberChatListUserName);
            barberChatListCardView = itemView.findViewById(R.id.barberChatListCardView);

        }
    }
}
