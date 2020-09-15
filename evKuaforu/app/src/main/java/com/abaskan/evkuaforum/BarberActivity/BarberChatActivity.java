package com.abaskan.evkuaforum.BarberActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.abaskan.evkuaforum.BarberAdapter.BarberChatAdapter;
import com.abaskan.evkuaforum.UserClass.Message;
import com.abaskan.evkuaforum.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarberChatActivity extends AppCompatActivity {
    Toolbar barberChatsToolbar;
    RecyclerView barberChatsRv;
    EditText barberChatsText;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    List<Message> messageList;
    BarberChatAdapter barberChatAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_chat);
        toolbar();
        findViewById();
        getMessageFromFirebase();


    }

    public void barberChatsSendButtonClick (View view){
        String barberId = firebaseAuth.getUid();
        String userId = getIntent().getStringExtra("userId");
        String message = barberChatsText.getText().toString().trim();


        if(message.isEmpty()){
            barberChatsText.setText("");
            barberChatsText.setHint("Boş mesaj gönderilemez");

        }else{
            barberChatsText.setHint("Mesajınızı yazabilirsiniz");
            String date = getDate();
            sendMessageToFirebase(userId,barberId,message,date,barberId);
            barberChatsText.setText("");
        }




    }

    public void sendMessageToFirebase (final String userId, final String barberId, String message, String date, String from) {
        final String messageId = databaseReference.child("Chats").child(barberId).child(userId).push().getKey();


        final Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("message",message);
        messageMap.put("date",date);
        messageMap.put("from",from);

        databaseReference.child("Chats").child(barberId).child(userId).child(messageId).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                databaseReference.child("Chats").child(userId).child(barberId).child(messageId).setValue(messageMap);
            }
        });
    }

    public void getMessageFromFirebase (){
        String userId = getIntent().getStringExtra("userId");
        databaseReference.child("Chats").child(firebaseAuth.getUid()).child(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                messageList.add(message);
                barberChatAdapter.notifyDataSetChanged();
                barberChatsRv.scrollToPosition(messageList.size() -1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void toolbar(){
        String userName = getIntent().getStringExtra("userName");
        barberChatsToolbar = findViewById(R.id.barberChatsToolbar);
        barberChatsToolbar.setTitle(userName);
        barberChatsToolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        barberChatsToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(barberChatsToolbar);
        barberChatsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void findViewById(){
        barberChatsText = findViewById(R.id.barberChatsText);
        barberChatsRv = findViewById(R.id.barberChatsRv);
        messageList = new ArrayList<>();
        barberChatsRv.setHasFixedSize(true);
        barberChatsRv.setLayoutManager(new LinearLayoutManager(this));
        barberChatAdapter = new BarberChatAdapter(this,messageList);
        barberChatsRv.setAdapter(barberChatAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public String getDate (){
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String date = dateFormat.format(today);
        return date;
    }
}
