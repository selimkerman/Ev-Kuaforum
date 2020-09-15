package com.abaskan.evkuaforum.UserAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abaskan.evkuaforum.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HoursAdapter extends RecyclerView.Adapter<HoursAdapter.HoursHolder>{
    private Context context;
    private ArrayList<String> hours;
    private ArrayList<String> selectedHours;
    FirebaseFirestore firebaseFirestore ;
    FirebaseAuth firebaseAuth;
    List<Button> buttonList = new ArrayList<>();


    public HoursAdapter(Context context, ArrayList<String> hours, ArrayList<String> selectedHours) {
        this.context = context;
        this.hours = hours;
        this.selectedHours = selectedHours;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public HoursHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_hour,parent,false);
        return new HoursHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HoursHolder holder, final int position) {

        for(int i=0;i<selectedHours.size();i++){
            for(int j =0;j<hours.size();j++){
                if(selectedHours.get(i).equals(hours.get(j))){
                    hours.set(j,"Dolu");
                    break;
                }
            }
        }

        holder.hourButton.setText(hours.get(position));

        holder.hourButton.setBackgroundColor(Color.parseColor("#f1f1f1"));
        if(!buttonList.contains(holder.hourButton)){
            buttonList.add(holder.hourButton);
        }



        for (Button button : buttonList){
            if(button.getText().equals("Kapalı")){
                button.setClickable(false);
                button.setBackgroundColor(Color.RED);
                button.setTextColor(Color.WHITE);
            } else if (button.getText().equals("Dolu")){
                button.setClickable(false);
                button.setBackgroundColor(Color.parseColor("#ef9a9a"));

            } else{
                button.setBackgroundColor(Color.parseColor("#f1f1f1"));
            }
        }



        holder.hourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (Button button : buttonList){
                    if(button.getText().equals("Kapalı")){
                        button.setClickable(false);
                        button.setBackgroundColor(Color.RED);
                        button.setTextColor(Color.WHITE);
                    } else if (button.getText().equals("Dolu")){
                        button.setClickable(false);
                        button.setBackgroundColor(Color.parseColor("#ef9a9a"));

                    } else{
                        button.setBackgroundColor(Color.parseColor("#f1f1f1"));
                    }
                }


                if(!holder.hourButton.getText().equals("Kapalı") && !holder.hourButton.getText().equals("Dolu") ){
                    holder.hourButton.setBackgroundColor(Color.CYAN);
                    String selected = holder.hourButton.getText().toString();
                    String hourFormat = selected.replace(":","");
                    Map<String,Object> hour = new HashMap<>();
                    hour.put("hour",selected);
                    hour.put("formatHour", hourFormat);
                    firebaseFirestore.collection("UserFields").document(firebaseAuth.getUid())
                            .collection("SelectedHours").document("Time").update(hour);
                }


            }
        });



    }


    @Override
    public int getItemCount() {
        return hours.size();
    }

    public class HoursHolder extends RecyclerView.ViewHolder{
        CardView hourCardView;
        Button hourButton;
        public HoursHolder(@NonNull View itemView) {
            super(itemView);
            hourCardView = itemView.findViewById(R.id.hourCardView);
            hourButton = itemView.findViewById(R.id.hourButton);
        }
    }
}
