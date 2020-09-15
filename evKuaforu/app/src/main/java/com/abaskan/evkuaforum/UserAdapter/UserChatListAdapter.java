package com.abaskan.evkuaforum.UserAdapter;

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

import com.abaskan.evkuaforum.R;
import com.abaskan.evkuaforum.UserActivity.UserChatActivity;

import java.util.ArrayList;
import java.util.Random;

public class UserChatListAdapter extends RecyclerView.Adapter<UserChatListAdapter.UserChatListHolder>{
    private Context context;
    private ArrayList<String> barberNameList;
    private ArrayList<String> barberIdList;

    public UserChatListAdapter(Context context, ArrayList<String> barberNameList, ArrayList<String> barberIdList) {
        this.context = context;
        this.barberNameList = barberNameList;
        this.barberIdList = barberIdList;
    }

    @NonNull
    @Override
    public UserChatListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_user_chat_list,parent,false);
        return new UserChatListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserChatListHolder holder, final int position) {
        holder.userChatListBarberIcon.setText(barberNameList.get(position).substring(0,1));
        Random random = new Random();
        final int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        ((GradientDrawable) holder.userChatListBarberIcon.getBackground()).setColor(color);

        holder.userChatListBarberName.setText(barberNameList.get(position));

        holder.userChatListCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toChat = new Intent(v.getContext(), UserChatActivity.class);
                toChat.putExtra("barberId",barberIdList.get(position));
                toChat.putExtra("barberName",barberNameList.get(position));
                toChat.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(toChat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return barberNameList.size();
    }

    public class  UserChatListHolder extends RecyclerView.ViewHolder{
        TextView userChatListBarberIcon;
        TextView userChatListBarberName;
        CardView userChatListCardView;
        public UserChatListHolder(@NonNull View itemView) {
            super(itemView);
            userChatListBarberIcon = itemView.findViewById(R.id.userChatListBarberIcon);
            userChatListBarberName = itemView.findViewById(R.id.userChatListBarberName);
            userChatListCardView = itemView.findViewById(R.id.userChatListCardView);

        }
    }
}
