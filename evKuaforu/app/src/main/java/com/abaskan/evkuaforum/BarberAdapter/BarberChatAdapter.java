package com.abaskan.evkuaforum.BarberAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abaskan.evkuaforum.UserClass.Message;
import com.abaskan.evkuaforum.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class BarberChatAdapter extends RecyclerView.Adapter<BarberChatAdapter.BarberChatHolder>{
    Context context;
    List<Message> messageList;
    String barberId;
    FirebaseAuth firebaseAuth;
    int viewBarber = 1, viewUser = 2;
    Boolean state;

    public BarberChatAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
        firebaseAuth = FirebaseAuth.getInstance();
        barberId = firebaseAuth.getUid();
        state = false;
    }

    @NonNull
    @Override
    public BarberChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == viewBarber){
            view = LayoutInflater.from(context).inflate(R.layout.card_message_right,parent,false);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.card_message_left,parent,false);
        }
        return new BarberChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarberChatHolder holder, int position) {
        Message message = messageList.get(position);
        holder.messageText.setText(message.getMessage());

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class BarberChatHolder extends RecyclerView.ViewHolder{
        TextView messageText;
        public BarberChatHolder(@NonNull View itemView) {
            super(itemView);

            if (state){
                messageText = itemView.findViewById(R.id.messageTextRight);
            }else {
                messageText = itemView.findViewById(R.id.messageTextLeft);
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        if(messageList.get(position).getFrom().equals(barberId)){
            state = true;
            return viewBarber;
        }else{
            state = false;
            return viewUser;
        }
    }
}

